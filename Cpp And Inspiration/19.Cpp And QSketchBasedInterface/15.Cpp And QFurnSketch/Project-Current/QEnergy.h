#include <QPoint>
#include <QString>
#include <QVector>
#include <QVector2D>
#include <QVector3D>
#include "QStanMath.h"
#include <stan/math.hpp>
typedef QVector<int>QArray;
using namespace stan::math;
#define Vector3v Matrix<var, 3, 1>
#define Vector4v Matrix<var, 4, 1>
#define VectorXv Matrix<var, Dynamic, 1>
#define MatrixXv Matrix<var, Dynamic, Dynamic>

class QEnergy
{
	public:
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
	int jointDim=15;
	int sketchWidth;
	int sketchHeight;
	void initViewInfo();
	void resetViewInfo();
	double viewInfo[12];
	VectorXd planeVector;
	double viewDistance=12;
	enum{MOVE, LINE, CUBIC};
	int getJointType(QString joint);
	QString getJointName(int joint);
	QArray toArray(VectorXi vector);
	Vector3v up=Vector3v(0, 1, 0);
	void perturbPlanes(qreal noise);
	Vector3v right=Vector3v(1, 0, 0);
	Vector3v forward=Vector3v(0, 0, -1);
	bool loadSketchFile(QString fileName);
	MatrixXd sketchPlanes(VectorXd plane);
	Vector3v toVector3v(QVector3D vector3D);
	QVector3D toQVector3D(Vector3v vector3v);
	Vector4v groundPlane=Vector4v(0, 1, 0, 0.5);
	VectorXi sketchVector, pathVector, jointVector;
	QPoint canvasPoint(double x, double y, double z);
	QVector2D canvasDirection(double x, double y, double z);
	Vector3v sketchPoint(Vector3i point, const MatrixXv& planes);
	double aspectRatio=1.0, screenScale=100.0, focalLength=0.1;
	Vector3v sketchPoint(const Vector2i& point, const Vector4v& plane);
	var parallelEnergy(const Vector4v& plane1, const Vector4v& plane2);
	var translateEnergy(const MatrixXi& sketches, const MatrixXv& planes);
	MatrixXv sketchCurves(const MatrixXi& sketch, const MatrixXv& planes);
	var perpendicularEnergy(const Vector4v& plane1, const Vector4v& plane2);
	var collinearEnergy(const MatrixXv& curve, const Vector4v& plane, int start, int end);
	var totalEnergy(const VectorXi& sketch, const VectorXi& joint, const VectorXv& plane);
	var verticalEnergy(const Vector2i& startPoint, const Vector2i& endPoint, const Vector4v& plane);
	var distanceEnergy(const Vector2i& point1, const Vector4v& plane1, const Vector2i& point2, const Vector4v& plane2);
	var coplanarEnergy(const Vector2i& startPoint, const Vector2i& endPoint, const Vector4v& srcPlane, const Vector4v& destPlane);
	var perpendicularEnergy(const Vector2i& leftPoint, const Vector2i& midPoint, const Vector2i& rightPoint, const Vector4v& plane);
	var parallelEnergy(const Vector2i& startPoint1, const Vector2i& endPoint1, const Vector4v& plane1, const Vector2i& startPoint2, const Vector2i& endPoint2, const Vector4v& plane2);
};
