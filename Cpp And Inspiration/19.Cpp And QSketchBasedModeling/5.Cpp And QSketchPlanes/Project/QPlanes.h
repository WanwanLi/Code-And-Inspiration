#include <QPoint>
#include <QVector>
#include <QVector3D>
#include <QVector4D>
#include "QViewer.h"

class QPlanes
{
	public:
	int size();
	QPlanes();
	int index=0;
	QVector<int> line;
	void removeLast();
	bool isOnCreating=false;
	void operator=(QPoint point);
	void finish(QViewer& viewer);
	QVector<qreal> lines, ground;
	void operator<<=(QPoint point);
	QVector4D& operator[](int index);
	void create(), update(), reset(), show();

	private:
	void drawPlane();
	qreal range=1.0;
	QPoint begin, end;
	qreal minDistance=0.2;
	QVector<QVector4D> planes;
	enum{MOVE=-3, LINE, CUBIC};
	int search(const QVector4D& plane);
	QVector3D xAxis=QVector3D(1, 0, 0);
	QVector3D yAxis=QVector3D(0, 1, 0);
	QVector3D zAxis=QVector3D(0, 0, 1);
	void createGroundPlane(qreal length, qreal height, qreal width);
	void interectPlane(const QVector4D& plane, const QVector3D& axis, QVector3D* points);
};