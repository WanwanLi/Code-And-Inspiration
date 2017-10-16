#include <QFile>
#include <LBFGS.h>
#include <QDebug>
#include <QPainter>
#include "QSketch.h"
#include "QRelation.h"
#include "QOptimizer.h"
#include <QTextStream>
using namespace LBFGSpp;
LBFGSParam<double> param;
LBFGSSolver<double> solver(param);

enum 
{
	VERTICAL,
	PARALLEL, 
	DISTANCE,
	COPLANAR,
	HORIZONTAL,
	SAME_POINTS,
	PERPENDICULAR,
	CONTACT_POINTS, 
	PARALLEL_PLANES,
	SYMMETRIC, IDENTICAL
};
enum{MOVE, LINE, CUBIC};
QRelation qRelation;
struct PlaneEnergy
{
	QEnergy& energy;
	PlaneEnergy(QEnergy& e):energy(e){}
	double operator()(const VectorXd& plane, VectorXd& grad)
	{
		double value;
		auto planeEnergy=[&](const VectorXv& plane) 
		{ 
			return energy.planeEnergy(plane); 
		};
		gradient(planeEnergy, plane, value, grad);
		return value;
	}
};
struct TotalEnergy
{
	int count=0;
	int timer=0;
	int interval=20;
	QOptimizer* optimizer;
	TotalEnergy(QOptimizer* o):optimizer(o){}


	/* std::function< void( const VectorXd& variable ) > callback;
	int callback_every = 1;
	int callback_count = 0; */


	double operator()(const VectorXd& variable, VectorXd& grad)
	{
		//callback_count += 1; 
		// if( callback && callback_count % callback_every == 0 ) callback( variable );

		double value;
		auto totalEnergy=[&](const VectorXv& variable)
		{ 
			return optimizer->energy.totalEnergy(variable);
		};
		gradient(totalEnergy, variable, value, grad);
		optimizer->energy.copySameGradients(grad);
		if(++timer%interval==0)
		{
			//energy.variableVector=variable;
			//optimizer->update(variable); 
			qDebug()<<"opt: "<<count++;
			timer=0;
//QString s="variable: "; for(int i=0; i<variable.size(); s+=QString::number(variable[i++])+", "); qDebug()<<s;
		}
		//for(int i=0; i<4*energy.planeSize; grad[i++]=0);
		//for(int i=4*energy.planeSize; i<grad.size(); grad[i++]=0); //grad[4*planeSize:] = 0;

//QString s="grad: "; for(int i=0; i<grad.size(); s+=QString::number(grad[i++])+", "); qDebug()<<s;
		return value;
	}
};
bool QOptimizer::loadSketchFile(QString fileName)
{
	if(!energy.loadSketchFile(fileName))return false;
	VectorXd x=energy.planeVector; 
	param.epsilon=epsilon; 
	param.max_iterations=step;
	this->sketchPlanes=energy.sketchPlanes(x); 
	this->sketchCurves=toMatrixXd(energy.sketchCurves(x));
	this->markerPoints.clear(); this->markerLines.clear(); 
	this->matchPoints.clear(); this->matchCurves.clear();
	this->iterations=0; this->minEnergy=0; return true;
}
void QOptimizer::minimizePlaneEnergy()
{
	energy.resetViewInfo();
	VectorXd x=energy.planeVector; 
	PlaneEnergy f(energy); double fx;
	LBFGSSolver<double> solver(param);
	this->iterations=solver.minimize(f, x, fx);
	qDebug()<<"iteration times: "<<iterations;
	this->sketchCurves=toMatrixXd(energy.sketchCurves(x));
	this->sketchPlanes=energy.sketchPlanes(x);
	this->minEnergy=fx; 
	qDebug()<<"minPlaneEnergy: "<<minEnergy;
}
/*void QOptimizer::minimizeTotalEnergy()
{
	energy.resetViewInfo();
	TotalEnergy f(this); double fx;
//	f.callback_every = 10;
/*	f.callback = []( const VectorXd& variable )
	{
		// save to file "optimization-in-progress.XYZ"

	};
qDebug()<<"x";
	VectorXd x=energy.variableVector; 
	
qDebug()<<"solver.minimize";
	this->iterations=solver->minimize(f, x, fx);
qDebug()<<"solver.minimize-finishied";
	this->minEnergy=fx; qDebug()<<"Emin: "<<fx;
	qDebug()<<"iteration times: "<<iterations;
}*/
/*void QOptimizer::minimizeTotalEnergy()
{
	this->iterations=0;  int t;
	this->energy.resetViewInfo();
	TotalEnergy f(this); double fx;
	VectorXd x=energy.variableVector; 
	LBFGSSolver<double> solver(param);	
	do
	{
		if(iterations>maxIterations)break;
		t=solver.minimize(f, x, fx);
		this->iterations+=t;
		this->update(x);
	}
	while(t==step);
	this->minEnergy=fx; 
	qDebug()<<"Emin: "<<minEnergy;
	qDebug()<<"iteration times: "<<iterations;
}*/
void QOptimizer::minimizeTotalEnergy()
{
	if(!sketch->isMinimizingEnergy)return;
	if(++this->timer<interval)return;
	if(iterations==0)
	{
		this->energy.resetViewInfo();
		this->x=energy.variableVector;
	}
	TotalEnergy f(this); double fx;
	int t=solver.minimize(f, x, fx);
	this->iterations+=t;
	this->update();
	this->timer=0;
	if(iterations>maxIterations||t<step)
	{
		this->minEnergy=fx; 
		qDebug()<<"Emin:"<<fx;
		qDebug()<<"iteration:"<<iterations;
		this->sketch->isMinimizingEnergy=false;
	}
}
void QOptimizer::update()
{
	this->sketchCurves=toMatrixXd(energy.sketchCurves(x));
	this->sketchPlanes=energy.sketchPlanes(x);
	this->sketch->clear();
	this->sketch->create();
	this->sketch->update();
}
void QOptimizer::perturbPlanes(qreal noise)
{
	energy.perturbPlanes(noise);
	VectorXd x=energy.planeVector;
	this->sketchPlanes=energy.sketchPlanes(x); 
	this->sketchCurves=toMatrixXd(energy.sketchCurves(x));
	this->iterations=0; this->minEnergy=0; 
}
void QOptimizer::drawMarkers(QPainter& painter)
{
	for(int i=0; i<markerLines.size(); i+=4)
	{
		int x0=markerLines[i+0];
		int y0=markerLines[i+1];
		int x1=markerLines[i+2];
		int y1=markerLines[i+3];
		painter.drawLine(x0, y0, x1, y1);
	}
	for(int i=0; i<markerPoints.size(); i+=2)
	{
		int x=markerPoints[i+0];
		int y=markerPoints[i+1];
		painter.drawEllipse(QPoint(x, y), 2, 2);
	}
	for(int i=0; i<startPoints.size(); i+=2)
	{
		float x=startPoints[i+0]-10.0f;
		float y=startPoints[i+1]-10.0f;
		painter.drawText(QPointF(x, y), QString::number(i/2));
	}
}
void QOptimizer::drawMatchPoints(QPainter& painter, int& index)
{
	if(matchPoints.size()==0)return;
	if(index<0)index+=matchPoints.size();
	else index%=matchPoints.size();
	array points=matchPoints[index];
	for(int i=0; i<points.size()/2; i++)
	{
		int x=points[i*2+0], y=points[i*2+1];
		painter.drawEllipse(QPoint(x, y), 2, 2);
	}
	array curve=matchCurves[index];
	for(int i=0; i<curve.size()/2-1; i++)
	{
		int x0=curve[(i+0)*2+0], y0=curve[(i+0)*2+1];
		int x1=curve[(i+1)*2+0], y1=curve[(i+1)*2+1];
		painter.drawLine(x0, y0, x1, y1);
	}
}
void insert(QVector<QVector<vec2>>& pointsCluster, vec2 startPoint, vec2 endPoint)
{
	#define has(x, y) x.indexOf(y)>=0
	for(int i=0; i<pointsCluster.size(); i++)
	{
		if(has(pointsCluster[i], startPoint)&&has(pointsCluster[i], endPoint))return;
		else if(has(pointsCluster[i], startPoint))
		{
			for(int j=i+1; j<pointsCluster.size(); j++)
			{
				if(has(pointsCluster[j], endPoint))
				{
					pointsCluster[i]+=pointsCluster[j];
					pointsCluster.removeAt(j); return;
				}
			}
			pointsCluster[i]<<endPoint; return;
		}
		else if(has(pointsCluster[i], endPoint))
		{
			for(int j=i+1; j<pointsCluster.size(); j++)
			{
				if(has(pointsCluster[j], startPoint))
				{
					pointsCluster[i]+=pointsCluster[j];
					pointsCluster.removeAt(j); return;
				}
			}
			pointsCluster[i]<<startPoint; return;
		}
	}
	QVector<vec2> points;
	points<<startPoint<<endPoint;
	pointsCluster<<points; 
}
void draw(QPainter& painter, QVector<QVector<vec2>> pointsCluster)
{
	QString label="I"; for(int i=0; i<pointsCluster.size(); i++, label+=" I")
	{
		for(int j=0; j<pointsCluster[i].size(); j++)
		{
			vec2 p=pointsCluster[i][j]+vec2(0, -5);
			painter.drawText(QPointF((int)p.x(), (int)p.y()), label);
		}
	}
}
void QOptimizer::drawRelations(QPainter& painter)
{
	array sketch=qRelation.sketchVector;
	#define pointOf(t) vec2(sketch[t*2+0], sketch[t*2+1])
	QVector<QVector<vec2>> paralelLines;
	for(int i=0; i<qRelation.size(); i++)
	{
		switch(qRelation[i])
		{
			case VERTICAL: 
			{
				int r1=qRelation[++i], r2=qRelation[++i], r3=qRelation[++i];
				vec2 p1=pointOf(r1), p2=pointOf(r2), m1=(p1+p2)/2, m2=m1+vec2(20, 0);
				painter.drawLine((int)m1.x(), (int)m1.y(), (int)m2.x(), (int)m2.y());
				painter.drawText(QPointF((int)m2.x(), (int)m2.y()), "VER"); break;
			}
			case PARALLEL:
			{
				int r1=qRelation[++i], r2=qRelation[++i], r3=qRelation[++i];
				int r4=qRelation[++i], r5=qRelation[++i], r6=qRelation[++i];
				vec2 p1=pointOf(r1), p2=pointOf(r2), p3=(p1+p2)/2;
				vec2 p4=pointOf(r4), p5=pointOf(r5), p6=(p4+p5)/2;
				insert(paralelLines, p3, p6); break;
			}
			case PERPENDICULAR:
			{
				int r1=qRelation[++i], r2=qRelation[++i];
				int r3=qRelation[++i], r4=qRelation[++i]; vec2 p2=pointOf(r2);
				painter.drawText(QPointF((int)p2.x(), (int)p2.y()), "PER"); break;
			}
			case CONTACT_POINTS: 
			{
				int r1=qRelation[++i], r2=qRelation[++i], r3=qRelation[++i];
				int r4=qRelation[++i], r5=qRelation[++i], r6=qRelation[++i];
				vec2 p1=pointOf(r1), p2=pointOf(r2);
				vec2 p3=pointOf(r4), p4=pointOf(r5); int dy=15;
				painter.drawText(QPointF((int)p1.x(), (int)p1.y()+dy), "_|_"); 
				painter.drawText(QPointF((int)p2.x(), (int)p2.y()+dy), "_|_"); 
				painter.drawText(QPointF((int)p3.x(), (int)p3.y()+dy), "_|_"); 
				painter.drawText(QPointF((int)p4.x(), (int)p4.y()+dy), "_|_"); break;
			}
			case PARALLEL_PLANES: i+=2; break;
			case DISTANCE: i+=4; break;
			case COPLANAR: i+=4; break;
			case SAME_POINTS: i+=2;  break;
		}
	}
	draw(painter, paralelLines);
}
void saveRelation(QEnergy e, QRelation relation, QString fileName)
{
	QFile file(fileName); QString endl="\r\n";
	if(!file.open(QIODevice::WriteOnly))return;
	QTextStream textStream(&file); 
	textStream<<"s "<<e.sketchWidth;
	textStream<<" "<<e.sketchHeight<<" ";
	textStream<<e.viewDistance<<endl;
	double A=e.up(0).val(), B=e.up(1).val(), C=e.up(2).val();
	double D=e.right(0).val(), E=e.right(1).val(), F=e.right(2).val();
	double G=e.forward(0).val(), H=e.forward(1).val(), I=e.forward(2).val();
	textStream<<"u "<<A<<" "<<B<<" "<<C<<endl;
	textStream<<"r "<<D<<" "<<E<<" "<<F<<endl;
	textStream<<"f "<<G<<" "<<H<<" "<<I<<endl;
	for(int i=0; i<relation.planesSize; i++)
	{
		textStream<<"p 0 0 1 0"<<endl;
	}
	array planes=relation.sketchPlanes;
	for(int i=0; i<relation.sketchPaths.size(); i++)
	{
		array path=relation.sketchPaths[i];
		array sketch=relation.sketchCurves[i];
		for(int j=0, k=0; j<path.size(); j++)
		{
			if(path[j]==MOVE)
			{
				int x=sketch[k++], y=sketch[k++], z=planes[i];
				textStream<<"m "<<x<<" "<<y<<" "<<z<<endl;
			}
			else if(path[j]==LINE)
			{
				int x=sketch[k++], y=sketch[k++], z=planes[i];
				textStream<<"l "<<x<<" "<<y<<" "<<z<<endl;
			}
			else if(path[j]==CUBIC)
			{
				int c1=sketch[k++], c2=sketch[k++]; j++;
				int c3=sketch[k++], c4=sketch[k++]; j++;
				int c5=sketch[k++], c6=sketch[k++];
				textStream<<"c "<<c1<<" "<<c2<<" "<<planes[i];
				textStream<<" "<<c3<<" "<<c4<<" "<<planes[i];
				textStream<<" "<<c5<<" "<<c6<<" "<<planes[i]<<endl;
			}
		}
	}
	for(int i=0; i<relation.size(); i++)
	{
		switch(relation[i])
		{
			case VERTICAL: textStream<<"VER"; for(int t=0; t<3; t++)textStream<<" "<<relation[++i]; break;
			case PARALLEL: textStream<<"PAR"; for(int t=0; t<6; t++)textStream<<" "<<relation[++i]; break;
			case PERPENDICULAR: textStream<<"PER"; for(int t=0; t<4; t++)textStream<<" "<<relation[++i]; break;
			case PARALLEL_PLANES: textStream<<"PARP"; for(int t=0; t<2; t++)textStream<<" "<<relation[++i]; break;
			case DISTANCE: textStream<<"DIS"; for(int t=0; t<4; t++)textStream<<" "<<relation[++i]; break;
			case COPLANAR: textStream<<"COP"; for(int t=0; t<4; t++)textStream<<" "<<relation[++i]; break;
			case SAME_POINTS: textStream<<"SAME"; for(int t=0; t<2; t++)textStream<<" "<<relation[++i]; break;
			case CONTACT_POINTS: textStream<<"CON"; for(int t=0; t<6; t++)textStream<<" "<<relation[++i]; break;
		}
		textStream<<endl;
	}
}
void QOptimizer::createRelations()
{
	array path=energy.toArray(energy.pathVector);
	array sketch=energy.toArray(energy.sketchVector);
	vec2 x=energy.canvasDirection(1, 0, 0);
	vec2 y=energy.canvasDirection(0, 1, 0);
	vec2 z=energy.canvasDirection(0, 0, 1);
	QRelation relation(path, sketch, x, y, z); 
	QPainterPath* painterPath=new QPainterPath();
	relation.create(); //relation.debugAll(); relation.debug();
	relation.debugSketchCurves(); //relation.debugCurvesPoints();
	this->markerPoints=relation.markerPoints;
	this->markerLines=relation.markerLines;
	this->matchCurves=relation.matchCurves;
	this->matchPoints=relation.matchPoints;
	this->startPoints=relation.startPoints;
	QString fileName="relation.sky";
	saveRelation(energy, relation, fileName);
	qRelation=relation;
}
MatrixXv QOptimizer::toMatrixXv(const VectorXd & vectorXd)
{
	Map<const MatrixXd> matrixXd(vectorXd.data(), 4, vectorXd.size()/4);
	MatrixXv matrixXv(matrixXd.rows(), matrixXd.cols());
	for(int i=0; i<matrixXv.rows(); i++)
	{
		for(int j=0; j<matrixXv.cols(); j++)
		{
			matrixXv(i, j)=matrixXd(i, j);
		}
	}
	return matrixXv;
}
MatrixXd QOptimizer::toMatrixXd(const MatrixXv& matrixXv)
{
	MatrixXd matrixXd(matrixXv.rows(), matrixXv.cols());
	for(int i=0; i<matrixXd.rows(); i++)
	{
		for(int j=0; j<matrixXd.cols(); j++)
		{
			matrixXd(i, j)=matrixXv(i, j).val();
		}
	}
	return matrixXd;
}


