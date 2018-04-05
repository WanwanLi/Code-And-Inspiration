#include <QPoint>
#include <QVector>
#include "QViewer.h"
#include <QVector3D>
#include <QVector4D>
#include <QTextStream>

class QPlanes
{
	public:
	int size();
	QPlanes();
	int index=0;
	QVector<int> line;
	bool isOnCreating=false;
	void removeLast(), clear();
	void operator=(QPoint point);
	void finish(QViewer& viewer);
	QVector<qreal> lines, ground;
	void operator<<=(QPoint point);
	QVector4D& operator[](int index);
	void operator<<(QVector4D plane);
	void create(), update(), reset(), show();
	void operator>>(QTextStream& textStream);

	private:
	qreal range=1.0;
	QPoint begin, end;
	qreal minDistance=0.2;
	QVector<QVector4D> planes;
	enum{MOVE=-3, LINE, CUBIC};
	int search(const QVector4D& plane);
	QVector3D p000, p001, p011, p010;
	QVector3D p100, p101, p111, p110;
	QVector3D xAxis=QVector3D(1, 0, 0);
	QVector3D yAxis=QVector3D(0, 1, 0);
	QVector3D zAxis=QVector3D(0, 0, 1);
	void createGroundPlane(qreal length, qreal height, qreal width);
	void intersectPlane(const QVector4D& plane, const QVector3D& axis, QVector3D* points);
};