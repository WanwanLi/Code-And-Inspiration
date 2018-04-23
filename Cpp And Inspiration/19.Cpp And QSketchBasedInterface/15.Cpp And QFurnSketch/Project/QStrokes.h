#include <QPoint>
#include <QVector>
#include "QCurve.h"
#include "QPlanes.h"
#include <QVector2D>

class QStrokes
{
	public:
	int size();
	QViewer& viewer;
	QPlanes& planes;
	void operator--(int);
	bool isSymmetric=false;
	qreal& operator[](int index);
	void operator=(QPoint point);
	void operator+=(QPoint point);
	QStrokes& operator<<(qreal path);
	QGraph graph=QGraph(path, viewer);
	void operator<<=(const QVector<int>& strokes);
	QStrokes(QViewer& v, QPlanes& p) : viewer(v), planes(p){}
	void clear(), update(), createGraph(), extrudePlanes(int from, int to);
	int begin=0, end, index, next(int index), prev(int index), planeIndex(int index);

	private:
	QCurve curve;
	void removeLast();
	void updateGraph();
	QVector<qreal> path;
	void setPath(int type);
	void removeLast(int length);
	enum{MOVE=-3, LINE, CUBIC};
	void append(const QVector<qreal>& strokes);
	void startPath(QPoint& point, QVector2D& direction);
	void addQuad(QVector<qreal>& strokes, QVector3D* points, int index);
};
