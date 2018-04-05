#include <QFile>
#include "QSketch.h"
#include "QOptimizer.h"
#include <QTextStream>
#define dot QVector3D::dotProduct
#define QIntMath QStanMath<int>
#define QDoubleMath QStanMath<double>
QOptimizer optimizer=QOptimizer(1e-5, 100);

bool QSketch::load(QString fileName)
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
void QSketch::minimizeEnergy()
{
	optimizer.minimizeEnergy();
	this->clear();
	this->create();
	this->update();
}
void QSketch::drawMatchPoints(QPainter& painter, int& index)
{
	optimizer.drawMatchPoints(painter, index);
}
void QSketch::drawMarkers(QPainter& painter)
{
	optimizer.drawMarkers(painter);
}
void QSketch::createRelations()
{
	optimizer.createRelations();
}
void QSketch::perturbPlanes(qreal noise)
{
	optimizer.perturbPlanes(noise);
	this->clear();
	this->create();
	this->update();
}
void QSketch::create()
{
	this->sketch<<optimizer.energy.sketchWidth;
	this->sketch<<optimizer.energy.sketchHeight;
	this->canvasSize=QSize(sketch[0], sketch[1]);
	this->joints=QIntMath::toQVector(optimizer.energy.jointVector);
	this->planes=QDoubleMath::toQVector(optimizer.sketchPlanes);
	this->indices=QIntMath::toQVector(optimizer.energy.sketchVector);
	while(painterPaths.size()<planes.size())
	this->painterPaths<<QPainterPath();
	MatrixXd sketch=optimizer.sketchCurves; 
	VectorXi path=optimizer.energy.pathVector;
	for(int i=0, j=0; i<path.size(); i++)
	{
		if(path[i]==MOVE)
		{
			Vector3d v=sketch.col(j++);
			this->sketch<<MOVE;
			this->sketch<<v(0)<<v(1)<<v(2);
		}
		else if(path[i]==LINE)
		{
			Vector3d v=sketch.col(j++);
			this->sketch<<LINE;
			this->sketch<<v(0)<<v(1)<<v(2);
		}
		else if(path[i]==CUBIC)
		{
			Vector3d c1=sketch.col(j++);
			Vector3d c2=sketch.col(j++);
			Vector3d c3=sketch.col(j++);
			this->sketch<<CUBIC;
			this->sketch<<c1(0)<<c1(1)<<c1(2);
			this->sketch<<c2(0)<<c2(1)<<c2(2);
			this->sketch<<c3(0)<<c3(1)<<c3(2);
		}
	}
}
void QSketch::update()
{
	this->updatePainterPaths();
	for(int i=2, j=-3; i<sketch.size(); i++)
	{
		if(sketch[i]==MOVE)
		{
			double x=sketch[++i], y=sketch[++i], z=sketch[++i];
			QPoint p=optimizer.energy.canvasPoint(x, y, z);
			this->moveTo(p.x(), p.y(), indices[(j+=3)+2]);
		}
		else if(sketch[i]==LINE)
		{
			double x=sketch[++i], y=sketch[++i], z=sketch[++i];
			QPoint p=optimizer.energy.canvasPoint(x, y, z);
			this->lineTo(p.x(), p.y(), indices[(j+=3)+2]);
		}
		else if(sketch[i]==CUBIC)
		{
			double c1=sketch[++i], c2=sketch[++i], c3=sketch[++i];
			double c4=sketch[++i], c5=sketch[++i], c6=sketch[++i];
			double c7=sketch[++i], c8=sketch[++i], c9=sketch[++i];
			QPoint p1=optimizer.energy.canvasPoint(c1, c2, c3);
			QPoint p2=optimizer.energy.canvasPoint(c4, c5, c6);
			QPoint p3=optimizer.energy.canvasPoint(c7, c8, c9);
			this->cubicTo(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y(), indices[(j+=9)+2]);
		}
	}
}
bool QSketch::save(QString fileName)
{
	#define e optimizer.energy
	QFile file(fileName); QString endl="\r\n";
	if(!file.open(QIODevice::WriteOnly))return false;
	QTextStream textStream(&file); 
	textStream<<"s "<<sketch[0];
	textStream<<" "<<sketch[1]<<" ";
	textStream<<e.viewDistance<<endl;
	QVector<int> s=indices; int d=-1;
	double A=e.up(0).val(), B=e.up(1).val(), C=e.up(2).val();
	double D=e.right(0).val(), E=e.right(1).val(), F=e.right(2).val();
	double G=e.forward(0).val(), H=e.forward(1).val(), I=e.forward(2).val();
	textStream<<"u "<<A<<" "<<B<<" "<<C<<endl;
	textStream<<"r "<<D<<" "<<E<<" "<<F<<endl;
	textStream<<"f "<<G<<" "<<H<<" "<<I<<endl;
	for(int i=2; i<sketch.size(); i++)
	{
		if(sketch[i]==MOVE)
		{
			double x=sketch[++i], y=sketch[++i], z=sketch[++i];
			QPoint p=optimizer.energy.canvasPoint(x, y, z);
			textStream<<"m "<<(int)p.x()<<" ";
			textStream<<(int)p.y()<<" "<<s[d+=3]<<endl;
		}
		else if(sketch[i]==LINE)
		{	
			double x=sketch[++i], y=sketch[++i], z=sketch[++i];
			QPoint p=optimizer.energy.canvasPoint(x, y, z);
			textStream<<"l "<<(int)p.x()<<" ";
			textStream<<(int)p.y()<<" "<<s[d+=3]<<endl;
		}
		else if(sketch[i]==CUBIC)
		{
			double c1=sketch[++i], c2=sketch[++i], c3=sketch[++i];
			double c4=sketch[++i], c5=sketch[++i], c6=sketch[++i];
			double c7=sketch[++i], c8=sketch[++i], c9=sketch[++i];
			QPoint p1=optimizer.energy.canvasPoint(c1, c2, c3);
			QPoint p2=optimizer.energy.canvasPoint(c4, c5, c6);
			QPoint p3=optimizer.energy.canvasPoint(c7, c8, c9);
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
QVector3D* QSketch::getViewDirection()
{
	#define e optimizer.energy
	return new QVector3D[3]
	{
		e.toQVector3D(e.right),
		e.toQVector3D(e.up),
		e.toQVector3D(e.forward)
	};
}
void QSketch::setViewDirection(QVector3D& right, QVector3D& up, QVector3D& forward)
{
	#define e optimizer.energy
	e.right=e.toVector3v(right);
	e.up=e.toVector3v(up);
	e.forward=e.toVector3v(forward);
}
double QSketch::getViewDistance()
{
	return optimizer.energy.viewDistance;
}
void QSketch::setViewDistance(double viewDistance)
{
	optimizer.energy.viewDistance=viewDistance;
}
void QSketch::clear()
{
	this->updatePainterPaths();
	this->sketch.clear();
}
QSize QSketch::size()
{
	return canvasSize;
}
void QSketch::updatePainterPaths()
{
	for(QPainterPath& painterPath : painterPaths)
	{
		QPainterPath newPainterPath=QPainterPath();
		painterPath.swap(newPainterPath);
	}
}
void QSketch::moveTo(int x, int y, int index)
{
	this->painterPaths[index].moveTo(x, y);
}
void QSketch::lineTo(int x, int y, int index)
{
	this->painterPaths[index].lineTo(x, y);
}
void QSketch::cubicTo(int x1, int y1, int x2, int y2, int x3, int y3, int index)
{
	this->painterPaths[index].cubicTo(x1, y1, x2, y2, x3, y3);
}
QPainterPath& QSketch::operator[](int index)
{
	return this->painterPaths[index];
}
int QSketch::length()
{
	return this->painterPaths.size();
}
