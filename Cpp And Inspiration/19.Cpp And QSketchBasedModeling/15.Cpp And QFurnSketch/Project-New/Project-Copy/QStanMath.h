#include <QVector>
#include <Eigen/Dense>
using namespace Eigen;
#define Vector3t Matrix<T, 3, 1>
#define Vector4t Matrix<T, 4, 1>
#define VectorXt Matrix<T, Dynamic, 1>
#define MatrixXt Matrix<T, Dynamic, Dynamic>
#define Template template<class T>

Template class QStanMath
{
	public:
	static T abs(T x);
	static Vector4t createPlane(T A, T B, T C, T D);
	static Vector3t centerPoint(const Vector4t& plane);
	static QVector<T> toQVector(const VectorXt& vectorXt);
	static QVector<T> toQVector(const MatrixXt& matrixXt);
	static Vector4t normalizedPlane(const Vector4t& plane);
	static Vector4t perturbedPlane(const Vector4t& plane, T noise);
	static MatrixXt subCurve(const MatrixXt& curve, int start, int end);
	static T distanceSquareBetween(MatrixXt& curve1, MatrixXt& curve2);
	static Vector4t createPlane(const Vector3t& point, const Vector3t& normal);
	static Vector3t projectedPoint(const Vector3t& point, const Vector4t& plane);
	static MatrixXt projectedCurve(const MatrixXt& curve, const Vector4t& plane);
	static Vector3t intersectPlane(const Vector3t& origin, const Vector3t& direction, const Vector4t& plane);
	static T integrateDistanceSquare(const Vector3t& P0, const Vector3t& P1, const Vector3t& Q0, const Vector3t& Q1);
};
Template Vector4t QStanMath<T>::createPlane(T A, T B, T C, T D)
{
	Vector4t plane(A, B, C, D);
	return plane/plane.head(3).norm();
}
Template Vector4t QStanMath<T>::createPlane(const Vector3t& point, const Vector3t& normal)
{
	return Vector4t(normal(0), normal(1), normal(2), -point.dot(normal));
}
Template Vector4t QStanMath<T>::normalizedPlane(const Vector4t& plane)
{			
	return plane/plane.head(3).norm();
}
Template Vector4t QStanMath<T>::perturbedPlane(const Vector4t& plane, T noise)
{
	return normalizedPlane(plane+Vector4t::Random()*noise);
}
Template Vector3t QStanMath<T>::centerPoint(const Vector4t& plane)
{
	T A=plane(0), B=plane(1), C=plane(2), D=plane(3), E=1e-5; Vector3t P(0, 0, 0);
	if(abs(A)>E)P(0)=-D/A; else if(abs(B)>E)P(1)=-D/B; else P(2)=-D/C; return P;
}
Template Vector3t QStanMath<T>::intersectPlane(const Vector3t& origin, const Vector3t& direction, const Vector4t& plane)
{
	Vector3t normal=plane.head(3);
	if(normal.dot(direction)==0)return origin;
	Vector3t center=centerPoint(plane);
	Vector3t connection=center-origin;
	T distance=normal.dot(connection)/normal.dot(direction);
	return origin+distance*direction;
}
Template Vector3t QStanMath<T>::projectedPoint(const Vector3t& point, const Vector4t& plane)
{
	return intersectPlane(point, -plane.head(3), plane);
}
Template QVector<T> QStanMath<T>::toQVector(const MatrixXt& matrixXt)
{
	QVector<T> vector;
	for(int i=0; i<matrixXt.cols(); i++)
	{
		for(int j=0; j<matrixXt.rows(); j++)
		{
			vector<<matrixXt(j, i);
		}
	}
	return vector;
}
Template QVector<T> QStanMath<T>::toQVector(const VectorXt& vectorXt)
{
	QVector<T> vector;
	for(int i=0; i<vectorXt.size(); i++)vector<<vectorXt(i);
	return vector;
}
Template MatrixXt QStanMath<T>::subCurve(const MatrixXt& curve, int start, int end)
{
	MatrixXt subcurve(3, end-start+1);
	for(int i=0; i<subcurve.cols(); i++)
	{
		subcurve.col(i)=curve.col(start+i);
	}
	return subcurve;
}
Template MatrixXt QStanMath<T>::projectedCurve(const MatrixXt& curve, const Vector4t& plane)
{
	MatrixXt projectCurve(3, curve.cols());	
	for(int i=0; i<curve.cols(); i++)
	{
		projectCurve.col(i)=projectedPoint(curve.col(i), plane);
	}
	return projectCurve;
}
Template T QStanMath<T>::integrateDistanceSquare(const Vector3t& P0, const Vector3t& P1, const Vector3t& Q0, const Vector3t& Q1)
{
	Vector3t u=P1-P0, v=Q1-Q0, C=P0-Q0, k=u-v;		
	return C.dot(C)+k.dot(k)/3+C.dot(k);
}
Template T QStanMath<T>::distanceSquareBetween(MatrixXt& curve1, MatrixXt& curve2)
{
	T distanceSquare=0;
	for(int i=0; i<curve1.cols()-1; i++)
	{
		distanceSquare+=integrateDistanceSquare
		(
			curve1.col(i+0), curve1.col(i+1), 
			curve2.col(i+0), curve2.col(i+1) 
		);
	}
	return distanceSquare;
}
Template T QStanMath<T>::abs(T x)
{
	return x>0?x:-x;
}
