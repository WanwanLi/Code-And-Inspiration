#include <QFile>
#include "QCurves.h"
#include "QOptimizer.h"
#include <QTextStream>
#define dot QVector3D::dotProduct
#define QIntMath QStanMath<int>
#define QDoubleMath QStanMath<double>
QEnergy energy; QOptimizer optimizer=QOptimizer(1e-6, 100);

bool QCurves::load(QString fileName)
{
	if(optimizer.loadSketchFile(fileName))
	{
		this->clear();
		this->create();
		this->update();
		return true;
	}
	else return false;
}
void QCurves::optimize()
{
	if(optimizer.minimized())
	{
		this->clear();
		this->create();
		this->update();
	}
}
void QCurves::create()
{
	this->curves<<optimizer.energy.sketchWidth;
	this->curves<<optimizer.energy.sketchHeight;
	this->canvasSize=QSize(curves[0], curves[1]);
	this->joints=QIntMath::toQVector(optimizer.energy.jointVector);
	this->planes=QDoubleMath::toQVector(optimizer.sketchPlanes);
	this->indices=QIntMath::toQVector(optimizer.energy.sketchVector);
	MatrixXd sketch=optimizer.sketchCurves; 
	VectorXi path=optimizer.energy.pathVector;
	for(int i=0, j=0; i<path.size(); i++)
	{
		if(path[i]==MOVE)
		{
			Vector3d v=sketch.col(j++);
			this->curves<<MOVE;
			this->curves<<v(0)<<v(1)<<v(2);
		}
		else if(path[i]==LINE)
		{
			Vector3d v=sketch.col(j++);
			this->curves<<LINE;
			this->curves<<v(0)<<v(1)<<v(2);
		}
		else if(path[i]==CUBIC)
		{
			Vector3d c1=sketch.col(j++);
			Vector3d c2=sketch.col(j++);
			Vector3d c3=sketch.col(j++);
			this->curves<<CUBIC;
			this->curves<<c1(0)<<c1(1)<<c1(2);
			this->curves<<c2(0)<<c2(1)<<c2(2);
			this->curves<<c3(0)<<c3(1)<<c3(2);
		}
	}
}
void QCurves::update()
{
	QPainterPath painterPath;
	this->swap(painterPath);
	for(int i=2; i<curves.size(); i++)
	{
		if(curves[i]==MOVE)
		{
			double x=curves[++i];
			double y=curves[++i];
			double z=curves[++i];
			QVector3D p=project(x, y, z);
			this->moveTo((int)p.x(), (int)p.y());
		}
		else if(curves[i]==LINE)
		{
			double x=curves[++i];
			double y=curves[++i];
			double z=curves[++i];
			QVector3D p=project(x, y, z);
			this->lineTo((int)p.x(), (int)p.y());
		}
		else if(curves[i]==CUBIC)
		{
			double c1=curves[++i];
			double c2=curves[++i];
			double c3=curves[++i];
			double c4=curves[++i];
			double c5=curves[++i];
			double c6=curves[++i];
			double c7=curves[++i];
			double c8=curves[++i];
			double c9=curves[++i];
			QVector3D p1=project(c1, c2, c3);
			QVector3D p2=project(c4, c5, c6);
			QVector3D p3=project(c7, c8, c9);
			this->cubicTo
			(
				(int)p1.x(), (int)p1.y(),
				(int)p2.x(), (int)p2.y(),
				(int)p3.x(), (int)p3.y()
			);
		}
	}
}
bool QCurves::save(QString fileName)
{
	QFile file(fileName); QString endl="\r\n";
	if(!file.open(QIODevice::WriteOnly))return false;
	QTextStream textStream(&file); 
	textStream<<"s "<<curves[0];
	textStream<<" "<<curves[1]<<" ";
	textStream<<viewDistance<<endl;
	QVector<int> s=indices; int d=-1;
	double A=up.x(), B=up.y(), C=up.z();
	double D=right.x(), E=right.y(), F=right.z();
	double G=forward.x(), H=forward.y(), I=forward.z();
	textStream<<"u "<<A<<" "<<B<<" "<<C<<endl;
	textStream<<"r "<<D<<" "<<E<<" "<<F<<endl;
	textStream<<"f "<<G<<" "<<H<<" "<<I<<endl;
	for(int i=2; i<curves.size(); i++)
	{
		if(curves[i]==MOVE)
		{
			double x=curves[++i];
			double y=curves[++i];
			double z=curves[++i];
			QVector3D p=project(x, y, z);
			textStream<<"m "<<(int)p.x()<<" ";
			textStream<<(int)p.y()<<" "<<s[d+=3]<<endl;
		}
		else if(curves[i]==LINE)
		{	
			double x=curves[++i];
			double y=curves[++i];
			double z=curves[++i];
			QVector3D p=project(x, y, z);
			textStream<<"l "<<(int)p.x()<<" ";
			textStream<<(int)p.y()<<" "<<s[d+=3]<<endl;
		}
		else if(curves[i]==CUBIC)
		{
			double c1=curves[++i];
			double c2=curves[++i];
			double c3=curves[++i];
			double c4=curves[++i];
			double c5=curves[++i];
			double c6=curves[++i];
			double c7=curves[++i];
			double c8=curves[++i];
			double c9=curves[++i];
			QVector3D p1=project(c1, c2, c3);
			QVector3D p2=project(c4, c5, c6);
			QVector3D p3=project(c7, c8, c9);
			textStream<<"c "<<(int)p1.x()<<" ";
			textStream<<(int)p1.y()<<" ";
			textStream<<s[d+=3]<<" ";
			textStream<<(int)p2.x()<<" ";
			textStream<<(int)p2.y()<<" ";
			textStream<<s[d+=3]<<" ";
			textStream<<(int)p3.x()<<" ";
			textStream<<(int)p3.y()<<" ";
			textStream<<s[d+=3]<<endl;
		}
	}
	for(int i=0; i<planes.size(); i+=4)
	{
		textStream<<"p "<<planes[i+0]<<" ";
		textStream<<planes[i+1]<<" ";
		textStream<<planes[i+2]<<" ";
		textStream<<planes[i+3]<<endl;
	}
	QEnergy energy; 
	int jointDim=energy.jointDim;
	for(int i=0; i<joints.size(); i+=jointDim)
	{
		textStream<<energy.getJointName(joints[i]);
		for(int j=1; j<jointDim; j++)
		{
			textStream<<" "<<joints[i+j];
		}
		textStream<<endl;
	}
	return true;
}
void QCurves::clear()
{
	QPainterPath painterPath;
	this->swap(painterPath);
	this->curves.clear();
}
QVector3D QCurves::project(double x, double y, double z)
{
	double A=forward.x(), B=forward.y(), C=forward.z();
	Vector4d viewPlane=QDoubleMath::createPlane(A, B, C, 0);
	Vector3d point(x, y, z); double scale=(canvasSize.height()+0.0)/canvasSize.width();
	Vector3d viewDirection=viewPlane.head(3), eye=viewDirection*viewDistance;
	Vector3d viewPoint=QDoubleMath::intersectPlane(eye, point-eye, viewPlane);
	QVector3D position=QVector3D(viewPoint(0), viewPoint(1), viewPoint(2));
	QVector3D coord=QVector3D(dot(position, right), dot(position, up), 0);
	double w=coord.x()*scale*canvasSize.width()/2+canvasSize.width()/2; 
	double h=coord.y()*canvasSize.height()/2+canvasSize.height()/2;
	return QVector3D(w, h, 0);
}
QSize QCurves::size()
{
	return canvasSize;
}
