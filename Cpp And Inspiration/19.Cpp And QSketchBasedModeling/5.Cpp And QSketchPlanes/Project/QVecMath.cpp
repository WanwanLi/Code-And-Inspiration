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