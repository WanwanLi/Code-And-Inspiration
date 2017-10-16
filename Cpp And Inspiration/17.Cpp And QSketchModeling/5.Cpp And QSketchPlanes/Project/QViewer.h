#include <QSize>
#include <QVector2D>
#include <QVector3D>

class QViewer
{
	public:
	QSize size;
	void resize(QSize size);
	QPoint lookAt(qreal x, qreal y, qreal z);
	QVector3D projectAt(const QPoint& point);
	QPoint lookAt(int x, int y,const QVector4D& plane);
	qreal viewDistance=12.0, minDistance=6.0, maxDistance=24.0;
	QVector3D projectAt(const QPoint& point, const QVector4D& plane);
	QVector3D projectAt(const QVector2D& point, const QVector4D& plane);
	void updateViewDistance(qreal translation), updateViewDirection(QPoint rotation);
	QVector3D forward=QVector3D(0, 0, -1), up=QVector3D(0, 1, 0), right=QVector3D(1, 0, 0);
	qreal aspectRatio=1.0, screenScale=100.0, focalLength=0.1, rotateSpeed=0.4, translateSpeed=0.5;
};