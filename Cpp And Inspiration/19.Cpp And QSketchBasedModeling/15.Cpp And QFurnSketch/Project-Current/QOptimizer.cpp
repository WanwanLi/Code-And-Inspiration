#include <QFile>
#include <LBFGS.h>
#include <QDebug>
#include <QPainter>
#include "QRelation.h"
#include "QOptimizer.h"
#include <QTextStream>
using namespace LBFGSpp;

struct Energy
{
	QEnergy& energy; const VectorXi sketch; const VectorXi joint;
	Energy(QEnergy& e, const VectorXi& s, const VectorXi& t):energy(e), sketch(s), joint(t){}
	double operator()(const VectorXd& plane, VectorXd& grad)
	{
		double value;
		auto totalEnergy=[&](const VectorXv& plane) 
		{ 
			return energy.totalEnergy(sketch, joint, plane); 
		};
		gradient(totalEnergy, plane, value, grad);
		return value;
	}
};
bool QOptimizer::loadSketchFile(QString fileName)
{
	if(!energy.loadSketchFile(fileName))return false;
	VectorXd x=energy.planeVector; 
	VectorXi sketch=energy.sketchVector;
	Map<MatrixXi> sketches(sketch.data(), 3, sketch.size()/3);
	this->sketchPlanes=energy.sketchPlanes(energy.planeVector); 
	this->sketchCurves=toMatrixXd(energy.sketchCurves(sketches, toMatrixXv(x)));
	this->markerPoints.clear(); this->markerLines.clear(); 
	this->matchPoints.clear(); this->matchCurves.clear();
	this->iterations=0; this->minEnergy=0; return true;
}
void QOptimizer::minimizeEnergy()
{
	energy.resetViewInfo();
	VectorXd x=energy.planeVector; 
	VectorXi sketch=energy.sketchVector;
	VectorXi joint=energy.jointVector;
	LBFGSParam<double> param;
	param.epsilon=epsilon; double fx;
	param.max_iterations=maxIterations;
	LBFGSSolver<double> solver(param);
	Energy f(energy, sketch, joint);
	this->iterations=solver.minimize(f, x, fx);
	qDebug()<<"it: "<<iterations;
	Map<MatrixXi> sketches(sketch.data(), 3, sketch.size()/3);
	this->sketchCurves=toMatrixXd(energy.sketchCurves(sketches, toMatrixXv(x)));
	this->sketchPlanes=energy.sketchPlanes(x);
	this->minEnergy=fx; 
	qDebug()<<"minE: "<<minEnergy;
}
void QOptimizer::perturbPlanes(qreal noise)
{
	energy.perturbPlanes(noise);
	VectorXd x=energy.planeVector; 
	VectorXi sketch=energy.sketchVector;
	Map<MatrixXi> sketches(sketch.data(), 3, sketch.size()/3);
	this->sketchPlanes=energy.sketchPlanes(energy.planeVector); 
	this->sketchCurves=toMatrixXd(energy.sketchCurves(sketches, toMatrixXv(x)));
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
void saveRelation(QEnergy e, QRelation relation, QString fileName)
{
	enum 
	{
		VERTICAL,
		PARALLEL, 
		DISTANCE,
		COPLANAR,
		HORIZONTAL,
		PERPENDICULAR,
		PARALLEL_PLANES,
		SYMMETRIC, IDENTICAL
	};
	enum{MOVE, LINE, CUBIC};
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
				int c5=sketch[k++], c6=sketch[k++]; j++;
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
			case VERTICAL: 
			{
				int c1=relation[++i], c2=relation[++i], c3=relation[++i], c4=relation[++i], c5=relation[++i];
				textStream<<"VER "<<c1<<" "<<c2<<" "<<c3<<" "<<c4<<" "<<c5<<endl; break;
			}
			case PARALLEL: 
			{
				int c1=relation[++i], c2=relation[++i], c3=relation[++i], c4=relation[++i], c5=relation[++i];
				int c6=relation[++i], c7=relation[++i], c8=relation[++i], c9=relation[++i], c10=relation[++i];
				textStream<<"PAR "<<c1<<" "<<c2<<" "<<c3<<" "<<c4<<" "<<c5<<" ";
				textStream<<c6<<" "<<c7<<" "<<c8<<" "<<c9<<" "<<c10<<endl; break;
			}
			case PERPENDICULAR: 
			{
				int c1=relation[++i], c2=relation[++i], c3=relation[++i], c4=relation[++i];
				int c5=relation[++i], c6=relation[++i], c7=relation[++i];
				textStream<<"PER "<<c1<<" "<<c2<<" "<<c3<<" "<<c4;
				textStream<<" "<<c5<<" "<<c6<<" "<<c7<<endl; break;
			}
			case PARALLEL_PLANES: 
			{
				int c1=relation[++i], c2=relation[++i];
				textStream<<"PARP "<<c1<<" "<<c2<<endl; break;
			}
			case DISTANCE:
			{
				int c1=relation[++i], c2=relation[++i], c3=relation[++i];
				int c4=relation[++i], c5=relation[++i], c6=relation[++i];
				textStream<<"DIS "<<c1<<" "<<c2<<" "<<c3<<" ";
				textStream<<c4<<" "<<c5<<" "<<c6<<endl; break;
			}
			case COPLANAR:
			{
				int c1=relation[++i], c2=relation[++i], c3=relation[++i];
				int c4=relation[++i], c5=relation[++i], c6=relation[++i];
				textStream<<"COP "<<c1<<" "<<c2<<" "<<c3<<" ";
				textStream<<c4<<" "<<c5<<" "<<c6<<endl; break;
			}
		}
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
	QString fileName="relation.sky";
	saveRelation(energy, relation, fileName);
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


