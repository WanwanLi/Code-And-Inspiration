#include <QPoint>
#include <QVector>
#include "QGraph.h"
#include <QVector3D>
#include <QVector4D>
#include <QTextStream>

class QPlanes
{
	public:
	int size();
	QPlanes();
	int index=0;
	qreal error=0.1;
	QVector<int> line;
	bool isOnCreating=false;
	void operator=(QPoint point);
	void finish(QViewer& viewer);
	QVector<qreal> lines, ground;
	void operator<<=(QPoint point);
	QVector3D* getQuad(int index);
	void removeFirst(), removeLast();
	QVector4D& operator[](int index);
	void operator<<(QVector4D plane);
	void operator>>(QTextStream& textStream);
	void create(), update(), reset(), undo(), clear(), show();

	private:
	qreal range=1.0;
	QPoint begin, end;
	QVector4D groundPlane;
	qreal minDistance=0.15;
	enum{MOVE=-3, LINE, CUBIC};
	QVector3D get(QVector3D normal);
	QVector<QVector4D> planes, stack;
	int search(const QVector4D& plane);
	QVector3D p000, p001, p011, p010;
	QVector3D p100, p101, p111, p110;
	QVector3D xAxis=QVector3D(1, 0, 0);
	QVector3D yAxis=QVector3D(0, 1, 0);
	QVector3D zAxis=QVector3D(0, 0, 1);
	void createGroundPlane(qreal length, qreal height, qreal width);
	QVector3D* intersectPlane(const QVector4D& plane, const QVector3D& axis, QVector3D* points);
};
