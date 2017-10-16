#include <QFile>
#include "QEnergy.h"
#include <QTextStream>
#define QVarMath QStanMath<var>
#define QDoubleMath QStanMath<double>
using namespace std;

var QEnergy::parallelEnergy(const Vector4v& plane1, const Vector4v& plane2)
{
	var dot=QVarMath::normalizedPlane(plane1).head(3).
	dot(QVarMath::normalizedPlane(plane2).head(3));
	return 1-dot*dot;
}
var QEnergy::perpendicularEnergy(const Vector4v& plane1, const Vector4v& plane2)
{
	return 1-parallelEnergy(plane1, plane2);
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
	var energy=0; Vector3v f=forward;
	for(int i=0; i<joints.cols(); i++)
	{
		VectorXi t=joints.col(i);
		switch(t(0))
		{
			case PARALLEL: energy+=parallelEnergy(planes.col(t(1)), planes.col(t(2))); break;
			case COLLINEAR: energy+=collinearEnergy(curves, planes.col(t(1)), t(2), t(3)); break;
			case PERPENDICULAR: energy+=perpendicularEnergy(planes.col(t(1)), planes.col(t(2))); break;
			case FORESHORTENING: energy+=parallelEnergy(planes.col(t(1)), Vector4v(f(0), f(1), f(2), 0)); break;
			case GROUND_PARALLEL: energy+=parallelEnergy(planes.col(t(1)), groundPlane); break;
			case GROUND_COLLINEAR: energy+=collinearEnergy(curves, groundPlane, t(1), t(2)); break;
			case GROUND_PERPENDICULAR: energy+=perpendicularEnergy(planes.col(t(1)), groundPlane); break;
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
	if(joint=="PAR")return PARALLEL;
	else if(joint=="COL")return COLLINEAR;
	else if(joint=="PER")return PERPENDICULAR;
	else if(joint=="FOR")return FORESHORTENING;
	else if(joint=="GPAR")return GROUND_PARALLEL;
	else if(joint=="GCOL")return GROUND_COLLINEAR;
	else if(joint=="GPER")return GROUND_PERPENDICULAR;
	else return NONE;
}
QString QEnergy::getJointName(int joint)
{
	if(joint==PARALLEL)return "PAR";
	else if(joint==COLLINEAR)return "COL";
	else if(joint==PERPENDICULAR)return "PER";
	else if(joint==FORESHORTENING)return "FOR";
	else if(joint==GROUND_PARALLEL)return "GPAR";
	else if(joint==GROUND_COLLINEAR)return "GCOL";
	else if(joint==GROUND_PERPENDICULAR)return "GPER";
	else return "NONE";
}
bool QEnergy::initialize()
{
	this->strokes.create();
	vector<int> path=strokes.path.toStdVector();
	vector<int> sketch=strokes.sketch.toStdVector();
	vector<int> joint=strokes.relation.toStdVector();
	vector<double> plane=strokes.plane.toStdVector();
	this->sketchVector=Map<VectorXi>(&sketch[0], sketch.size());
	this->planeVector=Map<VectorXd>(&plane[0], plane.size());
	this->jointVector=Map<VectorXi>(&joint[0], joint.size());
	this->pathVector=Map<VectorXi>(&path[0], path.size());
	this->viewer.initViewInfo();
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
