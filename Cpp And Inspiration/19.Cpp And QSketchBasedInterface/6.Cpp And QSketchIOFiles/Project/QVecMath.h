#include <QVector3D>
#include <QVector4D>
#include <QMatrix4x4>
#define dot QVector3D::dotProduct
#define cross QVector3D::crossProduct

class QVecMath
{
	public:
	static qreal clamp(qreal value, qreal min, qreal max);
	static QVector3D centerPoint(const QVector4D& plane);
	static QVector4D createPlane(const QVector3D& point, const QVector3D& normal);
	static QVector3D rotate(const QVector3D& vector, const QVector3D& axis, qreal angle);
	static bool isParallel(const QVector4D& plane1, const QVector4D& plane2, qreal error);
	static bool isPerpendicular(const QVector4D& plane1, const QVector4D& plane2, qreal error);
	static bool isCollinear(const QVector3D& begin, const QVector3D& end, const QVector4D& plane, qreal error);
	static QVector3D intersectPlane(const QVector3D& origin, const QVector3D& direction, const QVector4D& plane);
	static qreal distanceSquareBetween(const QVector3D& P0, const QVector3D& P1, const QVector3D& Q0, const QVector3D& Q1);
};