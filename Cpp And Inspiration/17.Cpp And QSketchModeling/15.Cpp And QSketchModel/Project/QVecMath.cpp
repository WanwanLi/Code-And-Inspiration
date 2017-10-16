#include "QVecMath.h"

QVector4D QVecMath::createPlane(const QVector3D& point, const QVector3D& normal)
{
	QVector3D direction=normal.normalized();
	return QVector4D(direction, -dot(point, direction));
}
QVector3D QVecMath::centerPoint(const QVector4D& plane)
{
	qreal A=plane.x(), B=plane.y(), C=plane.z(), D=plane.w(), E=1e-5; QVector3D P(0, 0, 0);
	if(qAbs(A)>E)P[0]=-D/A; else if(qAbs(B)>E)P[1]=-D/B; else P[2]=-D/C; return P;
}
QVector3D QVecMath::intersectPlane(const QVector3D& origin, const QVector3D& direction, const QVector4D& plane)
{
	QVector3D normal=plane.toVector3D();
	if(dot(normal, direction)==0)return origin;
	QVector3D center=centerPoint(plane);
	QVector3D connection=center-origin;
	qreal distance=dot(normal, connection)/dot(normal, direction);
	return origin+distance*direction;
}
QVector3D QVecMath::rotate(const QVector3D& vector, const QVector3D& axis, qreal angle)
{
	QMatrix4x4 matrix4x4; matrix4x4.setToIdentity();
	matrix4x4.rotate(angle, axis.x(), axis.y(), axis.z());
	return matrix4x4.map(vector);
}
qreal QVecMath::clamp(qreal value, qreal min, qreal max)
{
	return value<min?min:value>max?max:value;
}
bool QVecMath::isParallel(const QVector4D& plane1, const QVector4D& plane2, qreal error)
{
	QVector3D normal1=plane1.toVector3D().normalized();
	QVector3D normal2=plane2.toVector3D().normalized();
	return acos(abs(dot(normal1, normal2)))<error;
}
bool QVecMath::isPerpendicular(const QVector4D& plane1, const QVector4D& plane2, qreal error)
{
	QVector3D normal1=plane1.toVector3D().normalized();
	QVector3D normal2=plane2.toVector3D().normalized();
	return (PI/2-acos(abs(dot(normal1, normal2))))<error;
}
bool QVecMath::isCollinear(const QVector3D& begin, const QVector3D& end, const QVector4D& plane, qreal error)
{
	QVector3D P0=begin, P1=end;
	QVector3D normal=plane.toVector3D().normalized();
	QVector3D Q0=QVecMath::intersectPlane(P0, -normal, plane);
	QVector3D Q1=QVecMath::intersectPlane(P1, -normal, plane);
	return QVecMath::distanceSquareBetween(P0, P1, Q0, Q1)<error;
}
qreal QVecMath::distanceSquareBetween(const QVector3D& P0, const QVector3D& P1, const QVector3D& Q0, const QVector3D& Q1)
{
	QVector3D u=P1-P0, v=Q1-Q0, C=P0-Q0, k=u-v;		
	return dot(C, C)+dot(k, k)/3+dot(C, k);
}