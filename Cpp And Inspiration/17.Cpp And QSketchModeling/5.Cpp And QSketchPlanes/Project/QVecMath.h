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
	static QVector3D intersectPlane(const QVector3D& origin, const QVector3D& direction, const QVector4D& plane);
};