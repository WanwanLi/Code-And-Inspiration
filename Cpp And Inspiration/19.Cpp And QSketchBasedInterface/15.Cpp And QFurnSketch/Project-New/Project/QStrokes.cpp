#include <cmath>
#include <QDebug>
#include "QStrokes.h"
#include <QVector3D>
#include <QVector4D>
#include <Eigen/Dense>
using namespace Eigen;
#define max(a, b) a>b?a:b
#define str QString::number
#define dot QVector2D::dotProduct
#define cross QVector3D::crossProduct

QStrokes& QStrokes::operator<<(int stroke)
{
	if(stroke<0)this->path<<stroke;
	else this->sketch<<stroke;
	return *this;
}
void QStrokes::initialize()
{
	//this->xAxis=xAxis; this->yAxis=yAxis; this->zAxis=zAxis;
	QVector<int> path=QVector<int>(this->path);
	QVector<int> sketch=QVector<int>(this->sketch);
	this->path.clear(); this->sketch.clear();
	for(int x=-1, y=-1, i=0, j=0; i<path.size(); i++)
	{
		this->path<<path[i];
		if(path[i]==MOVE)
		{
			if(sketch[j+0]!=x&&sketch[j+1]!=y)
			{
				this->sketch<<sketch[j++];
				this->sketch<<sketch[j++];
			}
			else {this->path.removeLast(); j+=3;}
		}
		else if(path[i]==LINE)
		{
			this->sketch<<sketch[j++];
			this->sketch<<sketch[j++];
		}
		else
		{
			this->sketch<<sketch[j++];
			this->sketch<<sketch[j++];
			this->sketch<<sketch[j++];
			this->sketch<<sketch[j++];
			this->sketch<<sketch[j++];
			this->sketch<<sketch[j++];
		}
		x=this->sketch[this->sketch.size()-2];
		y=this->sketch[this->sketch.size()-1];
	}
}
void QStrokes::create()
{
	this->initialize();
	this->getSketchCurves();
	this->completeSketchCurves();
	for(int i=0; i<sketchCurves.size(); i++)
	{
		qreal curveLength=max(length(sketchCurves[i])/curveStep, 5);
		this->curvesCoords<<getCurvesPoints(i, curveLength);
		this->curvesPoints<<getCurvesPoints(i, curveSize);
	}
	this->getCurvesRelations();
}
QVector2D QStrokes::getLinePoint(const QVector<int>& curve, int index, qreal t)
{
	int i0=index, i1=i0+1;
	QVector2D v0=QVector2D(curve[i0*2+0], curve[i0*2+1]);
	QVector2D v1=QVector2D(curve[i1*2+0], curve[i1*2+1]);
	return (1-t)*v0+t*v1;
}
QVector2D QStrokes::getCubicPoint(const QVector<int>& curve, int index, qreal t)
{
	int i0=index, i1=i0+1, i2=i0+2, i3=i0+3;
	ctrlPoints[0]=QVector2D(curve[i0*2+0], curve[i0*2+1]);
	ctrlPoints[1]=QVector2D(curve[i1*2+0], curve[i1*2+1]);
	ctrlPoints[2]=QVector2D(curve[i2*2+0], curve[i2*2+1]);
	ctrlPoints[3]=QVector2D(curve[i3*2+0], curve[i3*2+1]);
	return getBezierCurvePoint(t);
}
QVector<qreal> QStrokes::getCurveKnots(const QVector<int>& curve)
{
	QVector<qreal> knots; qreal length=0.0; knots<<length;
	for(int i=0; i<curve.size()/2-1; i++)
	{
		QVector2D v1=QVector2D(curve[(i+0)*2+0], curve[(i+0)*2+1]);
		QVector2D v2=QVector2D(curve[(i+1)*2+0], curve[(i+1)*2+1]);
		length+=(v2-v1).length(); knots<<length;
	}
	for(qreal& knot : knots)knot/=length; return knots;
}
QVector<int> QStrokes::getCurvesPoints(int index, int size)
{
	QVector<int> path=sketchPaths[index];
	QVector<int> curve=sketchCurves[index];
	QVector<qreal> knots=getCurveKnots(curve);
	qreal dt=1.0/size, t=dt, t0, t1; QVector<int> points;
	for(int i=0, j=1, c=-4; i<size; i++, t+=dt)
	{
		while(j<knots.size()-1&&t>knots[j])j++; t0=knots[j-1]; t1=knots[j]; 
		QVector2D point; if(path[j]==LINE)point=getLinePoint(curve, j-1, (t-t0)/(t1-t0));
		else 
		{
			if(j-1>=c+3)c=j-1; t0=knots[c]; t1=knots[c+3];
			point=getCubicPoint(curve, c, (t-t0)/(t1-t0));
		}
		points<<point.x()<<point.y();
	}
	return points;
}
QVector2D QStrokes::getBezierCurvePoint(qreal param)
{
	int degree=3;
	#define t param
	#define V ctrlPoints
	#define B bezierPoints
	for(int i=0; i<=degree; B[i]=V[i], i++);
	for(int i=1; i<=degree; i++) 
	{	
		for(int j=0; j<=degree-i; j++)
		{
	    		B[j]=(1.0-t)*B[j]+t*B[j+1];
		}
	}
	return B[0];
}
void QStrokes::getCurvesRelations()
{
	for(int i=0; i<sketchCurves.size()-1; i++)
	{
		if(isSymmetric(curvesPoints[i]))
		{
			this->relation<<SYMMETRIC<<i;
		}
		for(int j=i+1; j<sketchCurves.size(); j++)
		{
			this->getCurvesRelations(i, j);
		}
	}
	if(isSymmetric(curvesPoints[sketchCurves.size()-1]))
	{
		this->relation<<SYMMETRIC<<sketchCurves.size()-1;
	}
}
bool QStrokes::isParallel(const QVector2D& x, const QVector2D& y)
{
	return abs(1-abs(dot(x.normalized(), y.normalized())))<error;
}
QVector2D QStrokes::tangent(const QVector<int>& curve, int index)
{
	qreal x0=curve[(index+0)*2+0]+0.0;
	qreal y0=curve[(index+0)*2+1]+0.0;
	qreal x1=curve[(index+1)*2+0]+0.0;
	qreal y1=curve[(index+1)*2+1]+0.0;
	qreal dx=x1-x0, dy=y1-y0;
	qreal dr=sqrt(dx*dx+dy*dy);
	return QVector2D(dx/dr, dy/dr);
}
qreal QStrokes::length(const QVector<int>& curve)
{
	qreal length=0.0;
	for(int i=0; i<curve.size()/2-1; i++)
	{
		QVector2D v1=QVector2D(curve[(i+0)*2+0], curve[(i+0)*2+1]);
		QVector2D v2=QVector2D(curve[(i+1)*2+0], curve[(i+1)*2+1]);
		length+=(v2-v1).length();
	}
	return length;
}
void QStrokes::getCurvesRelations(int curveIndex1, int curveIndex2)
{
	QVector<int> curve1=sketchCurves[curveIndex1];
	QVector<int> curve2=sketchCurves[curveIndex2];
	QVector<int> points1=curvesPoints[curveIndex1];
	QVector<int> points2=curvesPoints[curveIndex2];
	QVector<int> coords1=curvesCoords[curveIndex1];
	QVector<int> coords2=curvesCoords[curveIndex2];
	if(isIdentical(coords1, coords2))
	{
		this->relation<<IDENTICAL;
		this->relation<<curveIndex1;
		this->relation<<curveIndex2;
	}
	/*if(isSimilar(points1, points2))
	{
		this->relation<<SIMILAR;
		this->relation<<curveIndex1;
		this->relation<<curveIndex2;
	}*/
	for(int i=0; i<curve1.size()/2-1; i++)
	{
		QVector2D tangent1=tangent(curve1, i);
		if(isParallel(tangent1, xAxis))
		{
			this->relation<<PARALLEL_X<<curveIndex1;
			this->relation<<PERPENDICULAR_Y<<curveIndex1;
			this->relation<<PERPENDICULAR_Z<<curveIndex1;
		}
		else if(isParallel(tangent1, yAxis))
		{
			this->relation<<PERPENDICULAR_X<<curveIndex1;
			this->relation<<PARALLEL_Y<<curveIndex1;
			this->relation<<PERPENDICULAR_Z<<curveIndex1;
		}
		else if(isParallel(tangent1, zAxis))
		{
			this->relation<<PERPENDICULAR_X<<curveIndex1;
			this->relation<<PERPENDICULAR_Y<<curveIndex1;
			this->relation<<PARALLEL_Z<<curveIndex1;
		}
		for(int j=0; j<curve2.size()/2-1; j++)
		{
			QVector2D tangent2=tangent(curve2, j);
			if(isParallel(tangent1, tangent2))
			{
				this->relation<<PARALLEL;
				this->relation<<curveIndex1;
				this->relation<<curveIndex2;
			}
		}
	}
}
MatrixXd transform2d(const Matrix3d& matrix, const MatrixXd& curve)
{
	VectorXd X=curve.col(0), Y=curve.col(1);
	MatrixXd P=MatrixXd(X.size(), 2);
	for(int i=0; i<P.rows(); i++)
	{
		Vector3d V(X(i), Y(i), 1);
		double x=matrix.row(0)*V;
		double y=matrix.row(1)*V;
		double z=matrix.row(2)*V;
		P.row(i)=Vector2d(x/z, y/z);
	}
	return P;
}
void qDebugMatrix(QString string, const MatrixXd& matrix)
{
	qDebug()<<string+"=";
	for(int i=0; i<matrix.rows(); i++)
	{
		string="       ";
		for(int j=0; j<matrix.cols(); j++)
		{
			string+=str(matrix(i, j))+" ";
		}
		qDebug()<<string;
	}
}
MatrixXd toMatrix(const QVector<int>& curve, int start, int size)
{
	MatrixXd matrix(size, 2);
	for(int i=0; i<size; i++)
	{
		matrix(i, 0)=curve[(i+start)%(curve.size()/2)*2+0];
		matrix(i, 1)=curve[(i+start)%(curve.size()/2)*2+1];
	}
	return matrix;
}
void qDebugVector(QString string, const VectorXd& vector)
{
	string+="={";
	for(int i=0; i<vector.size(); i++)string+=str(vector(i))+", ";
	qDebug()<<string+" ... }";
}
double distanceBetween(const MatrixXd& curve1, const MatrixXd& curve2)
{
	double distance=0.0; 
	for(int i=0; i<curve1.rows(); i++)
	{
		QVector2D v1=QVector2D(curve1(i, 0), curve1(i, 1));
		QVector2D v2=QVector2D(curve2(i, 0), curve2(i, 1));
		distance+=(v2-v1).length();
	}
	return distance/curve1.rows();
}
Matrix3d getTransformMatrix(const MatrixXd& curve1, const MatrixXd& curve2)
{
	VectorXd x1=curve1.col(0), y1=curve1.col(1);
	VectorXd x2=curve2.col(0), y2=curve2.col(1);
	MatrixXd A(x1.size()*2, 6);  VectorXd b(x1.size()*2); 
	for(int i=0; i<x1.size(); i++)
	{
		VectorXd X(6); X<<x1(i),y1(i),1, 0,0,0; A.row(i*2+0)=X; b(i*2+0)=x2(i);
		VectorXd Y(6); Y<<0,0,0, x1(i),y1(i),1; A.row(i*2+1)=Y; b(i*2+1)=y2(i);
	}
	VectorXd h=A.jacobiSvd(ComputeThinU|ComputeThinV).solve(b);
	MatrixXd S=Map<MatrixXd>(h.data(), 3, 2).transpose(); Matrix3d T; 
	T.row(0)=S.row(0); T.row(1)=S.row(1); T.row(2)=Vector3d(0, 0, 1); return T;
}
/*bool QStrokes::isSimilar(const QVector<int>& curve1, const QVector<int>& curve2)
{
	MatrixXd tarCurve=toMatrix(curve2, 0, curve2.size()/2);
	qreal distance=minDistance; MatrixXd M; 
	int start=0; MatrixXd matchCurve, transCurve;
	for(int i=0; i<curve1.size()/2; i++)
	{
		MatrixXd srcCurve=toMatrix(curve1, i, curve1.size()/2);
		MatrixXd T=getTransformMatrix(srcCurve, tarCurve);
		MatrixXd destCurve=transform2d(T, srcCurve);
		qreal d=distanceBetween(tarCurve, destCurve);
		if(d<distance)
		{
			distance=d; M=T; start=i; 
			matchCurve=srcCurve;
			transCurve=destCurve;
		}
	}
	if(distance<minDistance)
	{
		//qDebug()<<"fitting start index: "<<start;
		//qDebugMatrix("Affine Tranform: ", M);
		//qDebug()<<"min distance: "<<distance;
		for(int i=0; i<matchCurve.rows(); i++)
		{
			int x0=matchCurve(i, 0), y0=matchCurve(i, 1);
			int x1=tarCurve(i, 0), y1=tarCurve(i, 1);
			int x2=transCurve(i, 0), y2=transCurve(i, 1);
			this->matchLines<<x0<<y0<<x1<<y1<<x2<<y2;
		}
	}
	return distance<minDistance;
}*/
bool QStrokes::isIdentical(const QVector<int>& curve1, const QVector<int>& curve2)
{
	if(curve1.size()<curve2.size())return isIdentical(curve2, curve1);
	MatrixXd tarCurve=toMatrix(curve2, 0, curve2.size()/2);
	qreal distance=minDistance; MatrixXd M; 
	int start=0; MatrixXd matchCurve, transCurve;
	for(int i=0; i<curve1.size()/2; i++)
	{
		MatrixXd srcCurve=toMatrix(curve1, i, curve2.size()/2);
		MatrixXd T=getTransformMatrix(srcCurve, tarCurve);
		MatrixXd destCurve=transform2d(T, srcCurve);
		qreal d=distanceBetween(tarCurve, destCurve);
		if(d<distance)
		{
			distance=d; M=T; start=i; 
			matchCurve=srcCurve;
			transCurve=destCurve;
		}
	}
	if(distance<minDistance)
	{
		//qDebug()<<"fitting start index: "<<start;
		//qDebugMatrix("Affine Tranform: ", M);
		//qDebug()<<"min distance: "<<distance;
		QVector<int> matchPoints;
		for(int i=0; i<matchCurve.rows(); i++)
		{
			int x0=matchCurve(i, 0), y0=matchCurve(i, 1);
			int x1=tarCurve(i, 0), y1=tarCurve(i, 1);
			int x2=transCurve(i, 0), y2=transCurve(i, 1);
			matchPoints<<x0<<y0<<x1<<y1<<x2<<y2;
		}
		this->matchCurves<<curve1;
		this->matchPoints<<matchPoints;
	}
	return distance<minDistance;
}
bool QStrokes::isSymmetric(const QVector<int>& curve)
{
	int size=curve.size()/2;
	bool symmetric=false;
	QVector2D center=QVector2D(0, 0);
	for(int i=0; i<size; i++)
	{
		center+=QVector2D(curve[i*2+0], curve[i*2+1]);
	}
	center/=size;
	for(int t=0; t<size; t++)
	{
		int i=t, j=(size+t-1)%size; qreal distance=0;
		QVector2D p0=QVector2D(curve[i*2+0], curve[i*2+1]);
		QVector2D p1=QVector2D(curve[j*2+0], curve[j*2+1]);
		QVector2D direction=((p0+p1)/2-center).normalized();
		for(qreal d0=0, d1=0; i!=j&&(i+1)%size!=j; i=(i+1)%size, j=(size+j-1)%size)
		{
			p0.setX(curve[i*2+0]); p0.setY(curve[i*2+1]);
			p1.setX(curve[j*2+0]); p1.setY(curve[j*2+1]);
			distance+=(p0-reflect(p1, center, direction)).length();
		}
		if(distance/size<5)
		{
			//qDebug()<<"isSymmetric: distance-axis(center, direction): "<<distance/size<<"-"<<center<<direction;
			qreal r=500; p0=center-direction*r; p1=center+direction*r; 
			//this->markerLines<<p0.x()<<p0.y()<<p1.x()<<p1.y();
			symmetric=true;
		}
	}
	return symmetric;
}
QVector2D QStrokes::reflect(QVector2D point, QVector2D origin, QVector2D axis)
{
	QVector3D normal=cross(QVector3D(0, 0, 1), QVector3D(axis)).normalized();
	qreal distance=point.distanceToLine(origin, axis);
	QVector2D point1=point+normal.toVector2D()*2*distance;
	if(point1.distanceToLine(origin, axis)==distance)return point1;
	return point+normal.toVector2D()*-2*distance;
}
QString QStrokes::pathStr(int path)
{
	switch(path)
	{
		case MOVE: return "MOVE"; 
		case LINE: return "LINE"; 
		case CUBIC: return "CUBIC"; 
	}
	return "NULL";
}
QString QStrokes::typeStr(int index)
{
	switch(sketchTypes[index])
	{
		case LINE_SEGMENT: return "LINE_SEGMENT"; 
		case CLOSE_CURVE: return "CLOSE_CURVE"; 
		case OPEN_CURVE: return "OPEN_CURVE"; 
	}
	return "NULL";
}
void QStrokes::debugAll()
{
	QString string="path: {";
	for(int p : path)string+=pathStr(p)+", ";
	qDebug()<<string+"... }";
	qDebug()<<"sketch: "<<sketch;
}
void QStrokes::debug()
{
	QString string="relation: {";
	for(int i=0; i<relation.size(); i++)
	{
		switch(relation[i])
		{
			case SYMMETRIC:
			{
				string+=" SYMMETRIC ";
				string+=str(relation[i++]); break;
			}
			case SIMILAR: 
			{
				string+=" SIMILAR "; 
				string+=str(relation[i++])+", ";
				string+=str(relation[i++]); break;
			}
			case IDENTICAL:
			{
				string+=" IDENTICAL "; 
				string+=str(relation[i++])+", ";
				string+=str(relation[i++]); break;
			}
			case PARALLEL: 
			{
				string+=" PARALLEL "; 
				string+=str(relation[i++])+", ";
				string+=str(relation[i++]); break;
			}
			case PARALLEL_X: 
			{
				string+=" PARALLEL_X "; 
				string+=str(relation[i++]); break;
			}
			case PARALLEL_Y: 
			{
				string+=" PARALLEL_Y "; 
				string+=str(relation[i++]); break;
			}
			case PARALLEL_Z: 
			{
				string+=" PARALLEL_Z "; 
				string+=str(relation[i++]); break;
			}
			case PERPENDICULAR_X: 
			{
				string+=" PERPENDICULAR_X "; 
				string+=str(relation[i++]); break;
			}
			case PERPENDICULAR_Y: 
			{
				string+=" PERPENDICULAR_Y "; 
				string+=str(relation[i++]); break;
			}
			case PERPENDICULAR_Z: 
			{
				string+=" PERPENDICULAR_Z "; 
				string+=str(relation[i++]); break;
			}
		}
	}
	qDebug()<<string+"... }";
}
void QStrokes::debugSketchCurves()
{
	int counter=0; 
	qDebug()<<"Sketch Curves are: \n";
	for(QVector<int> sketchCurve : sketchCurves)
	{
		QString string="Path "+str(counter);
		string+=" ["+typeStr(counter)+"] : ";
		for(int sketchPath : sketchPaths[counter])
		string+=pathStr(sketchPath)+", "; qDebug()<<string+"..."; 
		qDebug()<<"Curve "<<counter++<<" : "<<sketchCurve;
	}
}
void QStrokes::debugCurvesPoints()
{
	int counter=0;
	qDebug()<<"Curves Points are: \n";
	for(QVector<int> curvePoints : curvesPoints)
	{
		qDebug()<<"Curve Points:  "<<counter++<<" : "<<curvePoints;
		qDebug()<<"Curve Points Count:  "<<curvePoints.size()/2;
	}
}
bool QStrokes::equals(const QVector<int>& curve, int index1, int index2)
{
	int x1=curve[index1+0], y1=curve[index1+1];
	int x2=curve[index2+0], y2=curve[index2+1];
	return x1==x2&&y1==y2;
}
bool QStrokes::isJointType(int curveIndex)
{
	int k=curveIndex;
	if(sketchTypes[k]!=LINE_SEGMENT)return false;
	QVector2D p1=getPoint(k, 0), p2=getPoint(k, 1);
	QVector<qreal> positions;
	for(int i=0; i<sketchTypes.size(); i++)
	{
		if(sketchTypes[i]==CLOSE_CURVE)
		{
			int size=sketchCurves[i].size()/2;
			for(int j=0; j<size-1; j++)
			{
				QVector2D p3=getPoint(i, j+0), p4=getPoint(i, j+1);
				if(isParallel(p1-p2, p3-p4))continue;
				QVector2D p=intersect(p1, p2, p3, p4);
this->markerPoints<<p.x()<<p.y();
				qreal t=((p-p4)/(p3-p4)).x();
				if(t>=0&&t<=1)positions<<((p-p1)/(p2-p1)).x();
			}
		}
	}
	return isJointType(positions);
} 
bool QStrokes::isJointType(const QVector<qreal>& positions)
{
	if(positions.size()<2)return false;
	bool isLeftJoint=false, isRightJoint=false;
	for(qreal position : positions)
	{
		if(0<position&&position<1)return false;
		isLeftJoint=isLeftJoint||(position<=0);
		isRightJoint=isRightJoint||(position>=1);
	}
	return isLeftJoint&&isRightJoint;
}
void QStrokes::completeSketchCurves()
{
	for(int i=0; i<sketchTypes.size(); i++)
	{
		if(sketchTypes[i]==OPEN_CURVE)
		{
			this->completeOpenCurveWithJoint(i);
		}
	}
}
void QStrokes::completeOpenCurveWithJoint(int curveIndex)
{
	QVector2D leftPoint, rightPoint;
	qreal distance=minDistance;
	int k=curveIndex, jointIndex=-1;
	int size=sketchCurves[k].size()/2;
	QVector2D p1=getPoint(k, 0), p2=getPoint(k, 1);
	QVector2D p3=getPoint(k, size-1), p4=getPoint(k, size-2);
	for(int i=0; i<sketchTypes.size(); i++)
	{
		if(isJoint[i])
		{
			QVector2D p5=getPoint(i, 0), p6=getPoint(i, 1);
			QVector2D p=intersect(p1, p2, p5, p6);
			QVector2D q=intersect(p3, p4, p5, p6);
			qreal l=((p-p5)/(p6-p5)).x();
			qreal r=((q-p5)/(p6-p5)).x();
			qreal du=abs(l-0), dv=abs(r-1);
			if(abs(r-0)<du){du=abs(r-0); dv=abs(l-1);}
			qreal d=(du+dv)/2; 
			if(d<distance)
			{
				distance=d; jointIndex=i;
				leftPoint=p; rightPoint=q;
			}
		}
	}
	if(jointIndex>=0)
	{
		QVector2D p0=leftPoint, p5=rightPoint;
		qreal l=((p0-p1)/(p2-p1)).x(), r=((p5-p3)/(p4-p3)).x();
		if(l<0)this->markerLines<<p0.x()<<p0.y()<<p1.x()<<p1.y();
		else this->markerLines<<p0.x()<<p0.y()<<p2.x()<<p2.y();
		if(r<0)this->markerLines<<p5.x()<<p5.y()<<p3.x()<<p3.y();
		else this->markerLines<<p5.x()<<p5.y()<<p4.x()<<p4.y();
	}
}
void QStrokes::getSketchCurves()
{
	for(int i=0, j=0; i<path.size(); i++)
	{
		if(path[i]==MOVE)
		{
			QVector<int> sketchPath;
			QVector<int> sketchCurve;
			sketchPath<<MOVE;
			sketchCurve<<sketch[j++];
			sketchCurve<<sketch[j++];
			for(i++; i<path.size()&&path[i]!=MOVE; i++)
			{
				if(path[i]==LINE)
				{
					sketchPath<<LINE;
					sketchCurve<<sketch[j++];
					sketchCurve<<sketch[j++]; 
				}
				else
				{
					sketchPath<<CUBIC;
					sketchCurve<<sketch[j++];
					sketchCurve<<sketch[j++]; 
					sketchPath<<CUBIC;
					sketchCurve<<sketch[j++];
					sketchCurve<<sketch[j++]; 
					sketchPath<<CUBIC;
					sketchCurve<<sketch[j++];
					sketchCurve<<sketch[j++]; 
				}
			}
			if(sketchCurve.size()>0)
			{
				this->sketchPaths<<sketchPath;
				this->sketchCurves<<sketchCurve;
			}
			if(i<path.size())i--;
		}
	}
	for(int i=0; i<sketchCurves.size(); i++)
	{
		if(sketchCurves[i].size()==4)this->sketchTypes<<LINE_SEGMENT;
		else
		{
			int first=0, last=sketchCurves[i].size()-2;
			if(equals(sketchCurves[i], first, last))
			{
				this->sketchTypes<<CLOSE_CURVE;
			}
			else this->sketchTypes<<OPEN_CURVE;
		}
	}
	for(int i=0; i<sketchTypes.size(); i++)
	{
		this->isJoint<<isJointType(i);
		if(isJoint[i])
		{
qDebug()<<"true";
			QVector2D p0=getPoint(i, 0), p1=getPoint(i, 1);
			this->markerLines<<p0.x()<<p0.y()<<p1.x()<<p1.y();
		}
	}
qDebug()<<"isJoint="<<isJoint;
}
qreal QStrokes::cross2(QVector2D p1, QVector2D p2)
{
	return p1.x()*p2.y()-p1.y()*p2.x();
}
QVector2D QStrokes::intersect(QVector2D p1, QVector2D p2, QVector2D p3, QVector2D p4)
{
	qreal x=cross2(p1, p2)*(p3.x()-p4.x())-(p1.x()-p2.x())*cross2(p3, p4);
	qreal y=cross2(p1, p2)*(p3.y()-p4.y())-(p1.y()-p2.y())*cross2(p3, p4);
	qreal z=(p1.x()-p2.x())*(p3.y()-p4.y())-(p1.y()-p2.y())*(p3.x()-p4.x());
	return QVector2D(x/z, y/z);
}
QVector2D QStrokes::getPoint(int curveIndex, int pointIndex)
{
	int x=sketchCurves[curveIndex][pointIndex*2+0];
	int y=sketchCurves[curveIndex][pointIndex*2+1];
	return QVector2D(x, y);
}
