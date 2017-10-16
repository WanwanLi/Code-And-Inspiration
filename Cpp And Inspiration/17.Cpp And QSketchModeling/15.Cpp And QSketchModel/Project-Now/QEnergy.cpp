#include <QFile>
#include <QDebug>
#include "QEnergy.h"
#include <QTextStream>
#define QVarMath QStanMath<var>
#define QDoubleMath QStanMath<double>
using namespace std;

var QEnergy::depthEnergy(const VectorXv& planes)
{
	int planeIndex=sketchVector(2);
	Vector2i point=sketchVector.head(2);
	Vector4v plane=planes.segment(planeIndex*4, 4);
	var z=sketchPoint(point, plane)(2); return z*z;
}
var QEnergy::accuracyEnergy(const VectorXv& variables)
{
	var weight=0.01, error=0;
	int size=sketchVector.size()/3;
	for(int i=0, p=planeSize*4; i<size; i++)
	{
		var x0=sketchVector(i*3+0);
		var y0=sketchVector(i*3+1);
		var x1=variables(p+i*2+0);
		var y1=variables(p+i*2+1);
		var dx=x1-x0, dy=y1-y0;
		error+=dx*dx+dy*dy;
	}
//qDebug()<<"accuracyEnergy: "<<(weight*error).val();
	return weight*error/size;
}
var QEnergy::foreshorteningEnergy(const VectorXv& variables)
{
	var weight=1, zSquare=0.001;
	int size=sketchVector.size()/3;
	for(int i=0; i<size; i++)
	{
		int planeIndex=sketchVector(i*3+2);
		Vector4v plane=variables.segment(planeIndex*4, 4);
		var z=sketchPoint(pointAt(variables, i), plane)(2); zSquare+=z*z;
	}
//qDebug()<<"foreshorteningEnergy: "<<(weight*zSquare).val();
	return weight*(1.0/(zSquare/size));
}
var QEnergy::parallelEnergy(const Vector4v& plane1, const Vector4v& plane2)
{
	var dot=QVarMath::normalizedPlane(plane1).head(3).
	dot(QVarMath::normalizedPlane(plane2).head(3));
	return 1-dot*dot;
}
var QEnergy::distanceEnergy(const Vector2v& point1, const Vector4v& plane1, const Vector2v& point2, const Vector4v& plane2)
{
	return (sketchPoint(point1, plane1)-sketchPoint(point2, plane2)).norm();
}
var QEnergy::verticalEnergy(const Vector2v& startPoint, const Vector2v& endPoint, const Vector4v& plane)
{
	Vector3v start=sketchPoint(startPoint, plane);
	Vector3v end=sketchPoint(endPoint, plane);
	var dot=(end-start).normalized().dot(Vector3v(0, 1, 0));
	return 1-dot*dot;
}
var QEnergy::perpendicularEnergy(const Vector2v& leftPoint, const Vector2v& midPoint, const Vector2v& rightPoint, const Vector4v& plane)
{
	Vector3v left=sketchPoint(leftPoint, plane);
	Vector3v mid=sketchPoint(midPoint, plane);
	Vector3v right=sketchPoint(rightPoint, plane);
	var dot=(left-mid).normalized().dot((right-mid).normalized());
	return dot*dot;
}
var QEnergy::parallelEnergy(const Vector2v& startPoint1, const Vector2v& endPoint1, const Vector4v& plane1, const Vector2v& startPoint2, const Vector2v& endPoint2, const Vector4v& plane2)
{
	Vector3v start1=sketchPoint(startPoint1, plane1);
	Vector3v end1=sketchPoint(endPoint1, plane1);
	Vector3v start2=sketchPoint(startPoint2, plane2);
	Vector3v end2=sketchPoint(endPoint2, plane2);
	var dot=(end1-start1).normalized().dot((end2-start2).normalized());
	return 1-dot*dot;
}
var QEnergy::coplanarEnergy(const Vector2v& startPoint, const Vector2v& endPoint, const Vector4v& srcPlane, const Vector4v& destPlane)
{
	Vector3v start1=sketchPoint(startPoint, srcPlane);
	Vector3v end1=sketchPoint(endPoint, srcPlane);
	Vector3v start2=QVarMath::projectedPoint(start1, destPlane);
	Vector3v end2=QVarMath::projectedPoint(end1, destPlane);
	return 0.00*QVarMath::integrateDistanceSquare(start1, end1, start2, end2);
}
var QEnergy::collinearEnergy(const MatrixXv& curve, const Vector4v& plane, int start, int end)
{
	MatrixXv subcurve=QVarMath::subCurve(curve, start ,end);
	MatrixXv projectedcurve=QVarMath::projectedCurve(subcurve, plane);
	return QVarMath::distanceSquareBetween(subcurve, projectedcurve);
}
var QEnergy::groundCoplanarEnergy(const Vector2v& startPoint1, const Vector2v& endPoint1, const Vector4v& plane1, const Vector2v& startPoint2, const Vector2v& endPoint2, const Vector4v& plane2)
{
	Vector3v start1=sketchPoint(startPoint1, plane1);
	Vector3v end1=sketchPoint(endPoint1, plane1);
	Vector3v start2=sketchPoint(startPoint2, plane2);
	Vector3v end2=sketchPoint(endPoint2, plane2);
	var weight=10, dy=start1(1)-end1(1), sum=dy*dy;
	dy=start2(1)-end2(1); sum+=dy*dy;
	dy=start1(1)-end2(1); sum+=dy*dy;
	dy=start2(1)-end1(1); return weight*(sum+dy*dy);
}
var QEnergy::planeEnergy(const VectorXv& planes)
{
	#define planeAt(x) planes.segment(x*4, 4)
	var energy=depthEnergy(planes);
	for(int i=0; i<jointMatrix.cols(); i++)
	{
		VectorXi t=jointMatrix.col(i);
		switch(t(0))
		{
			case PARALLEL_PLANES: energy+=parallelEnergy(planeAt(t(1)), planeAt(t(2))); break;
			case VERTICAL: energy+=verticalEnergy(pointAt(t(1)), pointAt(t(2)), planeAt(t(3))); break;
			case DISTANCE: energy+=distanceEnergy(pointAt(t(1)), planeAt(t(2)), pointAt(t(3)), planeAt(t(4))); break;
			case COPLANAR: energy+=coplanarEnergy(pointAt(t(1)), pointAt(t(2)), planeAt(t(3)), planeAt(t(4))); break;
			case PERPENDICULAR: energy+=perpendicularEnergy(pointAt(t(1)), pointAt(t(2)), pointAt(t(3)), planeAt(t(4))); break;
			case PARALLEL: energy+=parallelEnergy(pointAt(t(1)), pointAt(t(2)), planeAt(t(3)), pointAt(t(4)), pointAt(t(5)), planeAt(t(6))); break;
		}
	}
	return energy;
}
var QEnergy::totalEnergy(const VectorXv& variables)
{
	#define planeOf(x) variables.segment(x*4, 4)
	#define pointOf(x) pointAt(variables, x)
	var energy=depthEnergy(variables);
	energy+=accuracyEnergy(variables);
	for(int i=0; i<jointMatrix.cols(); i++)
	{
		VectorXi t=jointMatrix.col(i);
		switch(t(0))
		{
			case PARALLEL_PLANES: energy+=parallelEnergy(planeOf(t(1)), planeOf(t(2))); break;
			case VERTICAL: energy+=verticalEnergy(pointOf(t(1)), pointOf(t(2)), planeOf(t(3))); break;
			case DISTANCE: energy+=distanceEnergy(pointOf(t(1)), planeOf(t(2)), pointOf(t(3)), planeOf(t(4))); break;
			case COPLANAR: energy+=coplanarEnergy(pointOf(t(1)), pointOf(t(2)), planeOf(t(3)), planeOf(t(4))); break;
			case PERPENDICULAR: energy+=perpendicularEnergy(pointOf(t(1)), pointOf(t(2)), pointOf(t(3)), planeOf(t(4))); break;
			case PARALLEL: energy+=parallelEnergy(pointOf(t(1)), pointOf(t(2)), planeOf(t(3)), pointOf(t(4)), pointOf(t(5)), planeOf(t(6))); break;
			case CONTACT_POINTS: energy+=groundCoplanarEnergy(pointOf(t(1)), pointOf(t(2)), planeOf(t(3)), pointOf(t(4)), pointOf(t(5)), planeOf(t(6))); break;
		}
	}
	return energy;
}
Vector3v QEnergy::sketchPoint(const Vector2i& point, const Vector4v& plane)
{
	return sketchPoint(Vector2v(point(0), point(1)), plane);
}
Vector3v QEnergy::sketchPoint(const Vector2v& point, const Vector4v& plane)
{
	Vector3v viewDirection=-forward;
	Vector3v eye=viewDirection*viewDistance;
	Vector3v focus=viewDirection*(viewDistance-focalLength);
	var x=(point(0)-sketchWidth/2)/(sketchWidth/2)/screenScale;
	var y=(point(1)-sketchHeight/2)/(sketchHeight/2)/aspectRatio/screenScale;
	return QVarMath::intersectPlane(eye, (focus+x*right-y*up)-eye, plane);
}
QPoint QEnergy::canvasPoint(double x, double y, double z)
{
	Vector3v point=Vector3v(x, y, z);
	Vector3v viewDirection=-forward;
	Vector3v eye=viewDirection*viewDistance;
	Vector3v focus=viewDirection*(viewDistance-focalLength);
	Vector4v viewPlane=QVarMath::createPlane(focus, viewDirection);
	Vector3v position=QVarMath::intersectPlane(eye, point-eye, viewPlane);
	x=screenScale*position.dot(right).val()*sketchWidth/2+sketchWidth/2; 
	y=screenScale*aspectRatio*position.dot(up).val()*sketchHeight/2+sketchHeight/2;
	return QPoint((int)x, sketchHeight-(int)y);
}
QVector2D QEnergy::canvasDirection(double x, double y, double z)
{
	Vector3v point=Vector3v(x, y, z);
	Vector3v viewDirection=-forward;
	Vector3v eye=viewDirection*viewDistance;
	Vector3v focus=viewDirection*(viewDistance-focalLength);
	Vector4v viewPlane=QVarMath::createPlane(focus, viewDirection);
	Vector3v position=QVarMath::intersectPlane(eye, point-eye, viewPlane);
	return QVector2D(position.dot(right).val(), y=position.dot(up).val()).normalized();
}
MatrixXv QEnergy::sketchCurves(VectorXd& variables)
{
	if(variables.size()==planeVector.size())
	{
		qDebug()<<"variables==planeVector";
		MatrixXv curves(3, sketchVector.size()/3);
		for(int i=0; i<curves.cols(); i++)
		{
			int planeIndex=sketchVector(i*3+2)*4;
			Vector2i point=sketchVector.segment(i*3, 2);
			Vector4v plane=toVector4v(variables, planeIndex);
			curves.col(i)=sketchPoint(point, plane);
		}
		return curves;
	}
	qDebug()<<"variables==planeVector+sketchVector";
	int size=sketchVector.size()/3;
	MatrixXv curves(3, size);
	for(int i=0; i<size; i++)
	{
		int pointIndex=planeSize*4+i*2;
		int planeIndex=sketchVector(i*3+2)*4;
		Vector2v point=toVector2v(variables, pointIndex);
		Vector4v plane=toVector4v(variables, planeIndex);
		curves.col(i)=sketchPoint(point, plane);
	}
	return curves;
}
MatrixXd QEnergy::sketchPlanes(VectorXd& variables)
{
	Map<MatrixXd> planes(variables.data(), 4, planeSize);
	for(int i=0; i<planeSize; i++)
	{
		planes.col(i)=QDoubleMath::normalizedPlane(planes.col(i));
	}
	return planes;
}
void QEnergy::perturbPlanes(qreal noise)
{
	MatrixXd planes=sketchPlanes(planeVector);
	for(int i=0; i<planes.cols(); i++)
	{
		planes.col(i)=QDoubleMath::perturbedPlane(planes.col(i), noise);
	}
	this->planeVector=Map<VectorXd>(planes.data(), planes.size());
}
int QEnergy::getJointType(QString joint)
{
	if(joint=="VER")return VERTICAL;
	else if(joint=="DIS")return DISTANCE;
	else if(joint=="PAR")return PARALLEL;
	else if(joint=="COP")return COPLANAR;
	else if(joint=="HOR")return HORIZONTAL;
	else if(joint=="SAME")return SAME_POINTS;
	else if(joint=="CON")return CONTACT_POINTS;
	else if(joint=="PER")return PERPENDICULAR;
	else if(joint=="PARP")return PARALLEL_PLANES;
	else if(joint=="SYM")return SYMMETRIC;
	else if(joint=="ID")return IDENTICAL;
	else return -1;
}
QString QEnergy::getJointName(int joint)
{
	if(joint==VERTICAL)return "VER";
	else if(joint==DISTANCE)return "DIS";
	else if(joint==PARALLEL)return "PAR";
	else if(joint==COPLANAR)return "COP";
	else if(joint==HORIZONTAL)return "HOR";
	else if(joint==SAME_POINTS)return "SAME";
	else if(joint==CONTACT_POINTS)return "CON";
	else if(joint==PERPENDICULAR)return "PER";
	else if(joint==PARALLEL_PLANES)return "PARP";
	else if(joint==SYMMETRIC)return "SYM";
	else if(joint==IDENTICAL)return "ID";
	else return "NULL";
}
bool QEnergy::loadSketchFile(QString fileName)
{
	vector<int> joint, sketch, path;
	vector<double> plane, variable;
	QFile file(fileName); QStringList line;
	if(!file.open(QIODevice::ReadOnly))return false;	
	QTextStream textStream(&file);
	bool isValidSketch=false; planeSize=0;
	while(!textStream.atEnd())
	{
		line=textStream.readLine().split(" ");
		if(line.size()==0)continue;
		if(line[0]=="s")
		{
			if(line.size()<3)continue;
			sketchWidth=line[1].toInt();
			sketchHeight=line[2].toInt();
			aspectRatio=sketchWidth+0.0;
			aspectRatio/=sketchHeight;
			isValidSketch=true;
			continue;
		}
		if(!isValidSketch)continue;
		if(line[0]=="v")
		{
			viewDistance=line[1].toDouble();
			focalLength=line[2].toDouble();
			screenScale=line[3].toDouble();
		}
		else if(line[0]=="u")
		{
			up(0)=line[1].toDouble();
			up(1)=line[2].toDouble();
			up(2)=line[3].toDouble();
		}
		else if(line[0]=="r")
		{
			right(0)=line[1].toDouble();
			right(1)=line[2].toDouble();
			right(2)=line[3].toDouble();
		}
		else if(line[0]=="f")
		{
			forward(0)=line[1].toDouble();
			forward(1)=line[2].toDouble();
			forward(2)=line[3].toDouble();
		}
		else if(line[0]=="m")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			int z=line[3].toInt();
			path.push_back(MOVE);
			sketch.push_back(x);
			sketch.push_back(y);
			sketch.push_back(z);
		}
		else if(line[0]=="l")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			int z=line[3].toInt();
			path.push_back(LINE);
			sketch.push_back(x);
			sketch.push_back(y);
			sketch.push_back(z);
		}
		else if(line[0]=="c")
		{
			int c1=line[1].toInt();
			int c2=line[2].toInt();
			int c3=line[3].toInt();
			int c4=line[4].toInt();
			int c5=line[5].toInt();
			int c6=line[6].toInt();
			int c7=line[7].toInt();
			int c8=line[8].toInt();
			int c9=line[9].toInt();
			path.push_back(CUBIC);
			sketch.push_back(c1);
			sketch.push_back(c2);
			sketch.push_back(c3);
			sketch.push_back(c4);
			sketch.push_back(c5);
			sketch.push_back(c6);
			sketch.push_back(c7);
			sketch.push_back(c8);
			sketch.push_back(c9);
		}
		else if(line[0]=="p")
		{
			double d1=line[1].toDouble();
			double d2=line[2].toDouble();
			double d3=line[3].toDouble();
			double d4=line[4].toDouble();
			plane.push_back(d1);
			plane.push_back(d2);
			plane.push_back(d3);
			plane.push_back(d4);
		}
		else
		{
			int t=getJointType(line[0]), i;
			if(t<0)continue;
			joint.push_back(t);
			switch(t)
			{
				case VERTICAL:
				{
					for(i=1; i<=3; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case PERPENDICULAR:
				{
					for(i=1; i<=4; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case PARALLEL:
				{
					for(i=1; i<=6; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case PARALLEL_PLANES:
				{
					for(i=1; i<=2; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case DISTANCE:
				{
					for(i=1; i<=4; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case COPLANAR:
				{
					for(i=1; i<=4; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case CONTACT_POINTS:
				{
					for(i=1; i<=6; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case SAME_POINTS:
				{
					int l1=line[1].toInt(), l2=line[2].toInt();
					this->samePoints<<l1<<l2;
					joint.pop_back(); break;
				}
			}
		}
	}
	file.close();
	if(isValidSketch)
	{
		for(double p : plane)variable.push_back(p);
		for(int i=0; i<sketch.size(); i+=3)
		{
			variable.push_back(sketch[i+0]);
			variable.push_back(sketch[i+1]);
		}
		this->jointMatrix=Map<MatrixXi>(joint.data(), jointDim, joint.size()/jointDim);
		this->variableVector=Map<VectorXd>(variable.data(), variable.size());
		this->sketchVector=Map<VectorXi>(sketch.data(), sketch.size());
		this->planeVector=Map<VectorXd>(plane.data(), plane.size());
		this->jointVector=Map<VectorXi>(joint.data(), joint.size());
		this->pathVector=Map<VectorXi>(path.data(), path.size());
		this->planeSize=plane.size()/4; this->initViewInfo();
	}
	return isValidSketch;
}
void QEnergy::initViewInfo()
{
	this->viewInfo[0]=viewDistance;
	this->viewInfo[1]=focalLength;
	this->viewInfo[2]=screenScale;
	this->viewInfo[3]=up(0).val();
	this->viewInfo[4]=up(1).val();
	this->viewInfo[5]=up(2).val();
	this->viewInfo[6]=right(0).val();
	this->viewInfo[7]=right(1).val();
	this->viewInfo[8]=right(2).val();
	this->viewInfo[9]=forward(0).val();
	this->viewInfo[10]=forward(1).val();
	this->viewInfo[11]=forward(2).val();
}
void QEnergy::resetViewInfo()
{
	this->viewDistance=viewInfo[0];
	this->focalLength=viewInfo[1];
	this->screenScale=viewInfo[2];
	this->up(0)=viewInfo[3];
	this->up(1)=viewInfo[4];
	this->up(2)=viewInfo[5];
	this->right(0)=viewInfo[6];
	this->right(1)=viewInfo[7];
	this->right(2)=viewInfo[8];
	this->forward(0)=viewInfo[9];
	this->forward(1)=viewInfo[10];
	this->forward(2)=viewInfo[11];
}
Vector2v QEnergy::pointAt(int index)
{
	int x=sketchVector(index*3+0);
	int y=sketchVector(index*3+1);
	return Vector2v(x, y);
}
Vector2v QEnergy::pointAt(const VectorXv& variable, int index)
{
	int i=planeSize*4+index*2;
	return variable.segment(i, 2);
}
void QEnergy::copySameGradients(VectorXd& grad)
{
	for(int i=0; i<samePoints.size(); i+=2)
	{
		int index0=samePoints[i+0];
		int index1=samePoints[i+1];
		int src=planeSize*4+index0*2;
		int dest=planeSize*4+index1*2;
		grad[dest+0]=grad[src+0]; 
		grad[dest+1]=grad[src+1]; 
	}
}
Vector3v QEnergy::toVector3v(QVector3D vector3D)
{
	return Vector3v(vector3D.x(), vector3D.y(), vector3D.z());
}
Vector2v QEnergy::toVector2v(const VectorXd& vectorXd, int startIndex)
{
	return Vector2v(vectorXd(startIndex+0), vectorXd(startIndex+1));
}
Vector4v QEnergy::toVector4v(const VectorXd& vectorXd, int startIndex)
{
	return Vector4v(vectorXd(startIndex+0), vectorXd(startIndex+1), vectorXd(startIndex+2), vectorXd(startIndex+3));
}
QVector3D QEnergy::toQVector3D(Vector3v vector3v)
{
	return QVector3D(vector3v(0).val(), vector3v(1).val(), vector3v(2).val());
}
QArray QEnergy::toArray(VectorXi vector)
{
	QArray result;
	for(int i=0; i<vector.size(); i++)result<<vector[i];
	return result;
}
