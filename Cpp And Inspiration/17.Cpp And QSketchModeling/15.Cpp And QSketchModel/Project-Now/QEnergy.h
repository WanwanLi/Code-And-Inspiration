#include <QPoint>
#include <QString>
#include <QVector>
#include <QVector2D>
#include <QVector3D>
#include "QStanMath.h"
#include <stan/math.hpp>
typedef QVector<int>QArray;
using namespace stan::math;
#define Vector2v Matrix<var, 2, 1>
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
		SAME_POINTS,
		PERPENDICULAR,
		CONTACT_POINTS, 
		PARALLEL_PLANES,
		SYMMETRIC, IDENTICAL
	};
	int planeSize;
	int jointDim=7;
	int sketchWidth;
	int sketchHeight;
	void initViewInfo();
	QArray samePoints;
	void resetViewInfo();
	MatrixXi jointMatrix;
	double viewInfo[12];
	double viewDistance=12;
	enum{MOVE, LINE, CUBIC};
	Vector2v pointAt(int index);
	int getJointType(QString joint);
	QString getJointName(int joint);
	QArray toArray(VectorXi vector);
	Vector3v up=Vector3v(0, 1, 0);
	void perturbPlanes(qreal noise);
	Vector3v right=Vector3v(1, 0, 0);
	Vector3v forward=Vector3v(0, 0, -1);
	VectorXd planeVector, variableVector;
	bool loadSketchFile(QString fileName);
	var planeEnergy(const VectorXv& planes);
	var depthEnergy(const VectorXv& planes);
	var totalEnergy(const VectorXv& variables);
	void copySameGradients(VectorXd& grad);
	Vector3v toVector3v(QVector3D vector3D);
	QVector3D toQVector3D(Vector3v vector3v);
	MatrixXv sketchCurves(VectorXd& variables);
	MatrixXd sketchPlanes(VectorXd& variables);
	VectorXi sketchVector, pathVector, jointVector;
	var accuracyEnergy(const VectorXv& variables);
	QPoint canvasPoint(double x, double y, double z);
	var foreshorteningEnergy(const VectorXv& variables);
	Vector2v pointAt(const VectorXv& variable, int index);
	QVector2D canvasDirection(double x, double y, double z);
	Vector2v toVector2v(const VectorXd& vectorXd, int startIndex);
	Vector4v toVector4v(const VectorXd& vectorXd, int startIndex);
	double aspectRatio=1.0, screenScale=100.0, focalLength=0.1;
	Vector3v sketchPoint(const Vector2i& point, const Vector4v& plane);
	Vector3v sketchPoint(const Vector2v& point, const Vector4v& plane);
	var parallelEnergy(const Vector4v& plane1, const Vector4v& plane2);
	var collinearEnergy(const MatrixXv& curve, const Vector4v& plane, int start, int end);
	var verticalEnergy(const Vector2v& startPoint, const Vector2v& endPoint, const Vector4v& plane);
	var totalEnergy(const VectorXi& sketch, const VectorXi& joint, const VectorXv& plane, int planeSize);
	var distanceEnergy(const Vector2v& point1, const Vector4v& plane1, const Vector2v& point2, const Vector4v& plane2);
	var coplanarEnergy(const Vector2v& startPoint, const Vector2v& endPoint, const Vector4v& srcPlane, const Vector4v& destPlane);
	var perpendicularEnergy(const Vector2v& leftPoint, const Vector2v& midPoint, const Vector2v& rightPoint, const Vector4v& plane);
	var parallelEnergy(const Vector2v& startPoint1, const Vector2v& endPoint1, const Vector4v& plane1, const Vector2v& startPoint2, const Vector2v& endPoint2, const Vector4v& plane2);
	var groundCoplanarEnergy(const Vector2v& startPoint1, const Vector2v& endPoint1, const Vector4v& plane1, const Vector2v& startPoint2, const Vector2v& endPoint2, const Vector4v& plane2);
};
