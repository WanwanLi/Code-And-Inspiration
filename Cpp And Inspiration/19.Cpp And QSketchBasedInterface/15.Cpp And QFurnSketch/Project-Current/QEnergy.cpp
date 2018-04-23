#include <QFile>
#include <QDebug>
#include "QEnergy.h"
#include <QTextStream>
#define QVarMath QStanMath<var>
#define QDoubleMath QStanMath<double>
using namespace std;

var QEnergy::translateEnergy(const MatrixXi& sketches, const MatrixXv& planes)
{
	var z=sketchPoint(sketches.col(0), planes)(2); return z*z;
}
var QEnergy::parallelEnergy(const Vector4v& plane1, const Vector4v& plane2)
{
	var dot=QVarMath::normalizedPlane(plane1).head(3).
	dot(QVarMath::normalizedPlane(plane2).head(3));
	return 1-dot*dot;
}
var QEnergy::distanceEnergy(const Vector2i& point1, const Vector4v& plane1, const Vector2i& point2, const Vector4v& plane2)
{
	return (sketchPoint(point1, plane1)-sketchPoint(point2, plane2)).norm();
}
var QEnergy::verticalEnergy(const Vector2i& startPoint, const Vector2i& endPoint, const Vector4v& plane)
{
	Vector3v start=sketchPoint(startPoint, plane);
	Vector3v end=sketchPoint(endPoint, plane);
	var dot=(end-start).normalized().dot(Vector3v(0, 1, 0));
	return 1-dot*dot;
}
var QEnergy::perpendicularEnergy(const Vector2i& leftPoint, const Vector2i& midPoint, const Vector2i& rightPoint, const Vector4v& plane)
{
	Vector3v left=sketchPoint(leftPoint, plane);
	Vector3v mid=sketchPoint(midPoint, plane);
	Vector3v right=sketchPoint(rightPoint, plane);
	var dot=(left-mid).normalized().dot((right-mid).normalized());
	return dot*dot;
}
var QEnergy::parallelEnergy(const Vector2i& startPoint1, const Vector2i& endPoint1, const Vector4v& plane1, const Vector2i& startPoint2, const Vector2i& endPoint2, const Vector4v& plane2)
{
	Vector3v start1=sketchPoint(startPoint1, plane1);
	Vector3v end1=sketchPoint(endPoint1, plane1);
	Vector3v start2=sketchPoint(startPoint2, plane2);
	Vector3v end2=sketchPoint(endPoint2, plane2);
	var dot=(end1-start1).normalized().dot((end2-start2).normalized());
	return 1-dot*dot;
}
var QEnergy::perpendicularEnergy(const Vector4v& plane1, const Vector4v& plane2)
{
	return 1-parallelEnergy(plane1, plane2);
}
var QEnergy::coplanarEnergy(const Vector2i& startPoint, const Vector2i& endPoint, const Vector4v& srcPlane, const Vector4v& destPlane)
{
	Vector3v start1=sketchPoint(startPoint, srcPlane);
	Vector3v end1=sketchPoint(endPoint, srcPlane);
	Vector3v start2=QVarMath::projectedPoint(start1, destPlane);
	Vector3v end2=QVarMath::projectedPoint(end1, destPlane);
	return QVarMath::integrateDistanceSquare(start1, end1, start2, end2);
}
var QEnergy::collinearEnergy(const MatrixXv& curve, const Vector4v& plane, int start, int end)
{
	MatrixXv subcurve=QVarMath::subCurve(curve, start ,end);
	MatrixXv projectedcurve=QVarMath::projectedCurve(subcurve, plane);
	return QVarMath::distanceSquareBetween(subcurve, projectedcurve);
}
var QEnergy::totalEnergy(const VectorXi& sketch, const VectorXi& joint, const VectorXv& plane)
{
	Map<const MatrixXi> joints(joint.data(), jointDim, joint.size()/jointDim);
	Map<const MatrixXi> sketches(sketch.data(), 3, sketch.size()/3);
	Map<const MatrixXv> planes(plane.data(), 4, plane.size()/4);
	MatrixXv curves=sketchCurves(sketches, planes);
	var energy=translateEnergy(sketches, planes);
	for(int i=0; i<joints.cols(); i++)
	{
		VectorXi t=joints.col(i);
		switch(t(0))
		{
			case PARALLEL_PLANES: energy+=parallelEnergy(planes.col(t(1)), planes.col(t(2))); break;
			case VERTICAL: energy+=verticalEnergy(Vector2i(t(1), t(2)), Vector2i(t(3), t(4)), planes.col(t(5))); break;
			case DISTANCE: energy+=distanceEnergy(Vector2i(t(1), t(2)), planes.col(t(3)), Vector2i(t(4), t(5)), planes.col(t(6))); break;
			case COPLANAR: energy+=coplanarEnergy(Vector2i(t(1), t(2)), Vector2i(t(3), t(4)), planes.col(t(5)), planes.col(t(6))); break;
			case PERPENDICULAR: energy+=perpendicularEnergy(Vector2i(t(1), t(2)), Vector2i(t(3), t(4)), Vector2i(t(5), t(6)), planes.col(t(7))); break;
			case PARALLEL: energy+=parallelEnergy(Vector2i(t(1), t(2)), Vector2i(t(3), t(4)), planes.col(t(5)), Vector2i(t(6), t(7)), Vector2i(t(8), t(9)), planes.col(t(10))); break;

/*
			Vector3v f=forward;
			case FORESHORTENING: energy+=parallelEnergy(planes.col(t(1)), Vector4v(f(0), f(1), f(2), 0)); break;
			case GROUND_PARALLEL: energy+=parallelEnergy(planes.col(t(1)), groundPlane); break;
			case GROUND_COLLINEAR: energy+=collinearEnergy(curves, groundPlane, t(1), t(2)); break;
			case GROUND_PERPENDICULAR: energy+=perpendicularEnergy(planes.col(t(1)), groundPlane); break;
*/
		}
	}
	return energy;
}
Vector3v QEnergy::sketchPoint(Vector3i point, const MatrixXv& planes)
{
	Vector3v viewDirection=-forward;
	Vector3v eye=viewDirection*viewDistance;
	Vector3v focus=viewDirection*(viewDistance-focalLength);
	double x=(point(0)-sketchWidth/2+0.0)/(sketchWidth/2)/screenScale;
	double y=(point(1)-sketchHeight/2+0.0)/(sketchHeight/2)/aspectRatio/screenScale;
	return QVarMath::intersectPlane(eye, (focus+x*right-y*up)-eye, planes.col(point(2)));
}
Vector3v QEnergy::sketchPoint(const Vector2i& point, const Vector4v& plane)
{
	Vector3v viewDirection=-forward;
	Vector3v eye=viewDirection*viewDistance;
	Vector3v focus=viewDirection*(viewDistance-focalLength);
	double x=(point(0)-sketchWidth/2+0.0)/(sketchWidth/2)/screenScale;
	double y=(point(1)-sketchHeight/2+0.0)/(sketchHeight/2)/aspectRatio/screenScale;
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
MatrixXv QEnergy::sketchCurves(const MatrixXi& sketch, const MatrixXv& planes)
{
	MatrixXv curves(3, sketch.cols());
	for(int i=0; i<curves.cols(); i++)
	{
		curves.col(i)=sketchPoint(sketch.col(i), planes);
	}
	return curves;
}
MatrixXd QEnergy::sketchPlanes(VectorXd plane)
{
	Map<MatrixXd> planes(plane.data(), 4, plane.size()/4);
	MatrixXd sketch(4, planes.cols());
	for(int i=0; i<sketch.cols(); i++)
	{
		sketch.col(i)=QDoubleMath::normalizedPlane(planes.col(i));
	}
	return sketch;
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
	else if(joint==PERPENDICULAR)return "PER";
	else if(joint==PARALLEL_PLANES)return "PARP";
	else if(joint==SYMMETRIC)return "SYM";
	else if(joint==IDENTICAL)return "ID";
	else return "NULL";
}
bool QEnergy::loadSketchFile(QString fileName)
{
	vector<double> plane;
	vector<int> joint, sketch, path;
	QFile file(fileName); QStringList line;
	if(!file.open(QIODevice::ReadOnly))return false;	
	QTextStream textStream(&file);
	bool isValidSketch=false;
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
					for(i=1; i<=5; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case PERPENDICULAR:
				{
					for(i=1; i<=7; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case PARALLEL:
				{
					for(i=1; i<=10; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case PARALLEL_PLANES:
				{
					for(i=1; i<=2; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case DISTANCE:
				{
					for(i=1; i<=6; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
				case COPLANAR:
				{
					for(i=1; i<=6; joint.push_back(line[i++].toInt()));
					for(; i<jointDim; i++)joint.push_back(0); break;
				}
			}
		}
	}
	file.close();
	if(isValidSketch)
	{
		sketchVector=Map<VectorXi>(sketch.data(), sketch.size());
		planeVector=Map<VectorXd>(plane.data(), plane.size());
		jointVector=Map<VectorXi>(joint.data(), joint.size());
		pathVector=Map<VectorXi>(path.data(), path.size());
		this->initViewInfo();
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
Vector3v QEnergy::toVector3v(QVector3D vector3D)
{
	return Vector3v(vector3D.x(), vector3D.y(), vector3D.z());
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
