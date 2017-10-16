#include <QPoint>
#include <QVector>
#include <QVector2D>
#include "QCurve.h"
#include "QGraph.h"
#include "QPlanes.h"

class QStrokes
{
	public:
	int size();
	QViewer& viewer;
	QPlanes& planes;
	void operator--(int);
	qreal& operator[](int index);
	void operator=(QPoint point);
	void operator+=(QPoint point);
	QStrokes& operator<<(int path);
	void clear(), update(), createGraph();
	QStrokes(QViewer& v, QPlanes& p) : viewer(v), planes(p){}

	private:
	QCurve curve;
	void removeLast();
	void updateGraph();
	void setPath(int type);
	QVector<qreal> path;
	void removeLast(int length);
	QGraph graph=QGraph(path);
	enum{MOVE=-3, LINE, CUBIC};
	void startPath(QPoint& point, QVector2D& direction);
	int begin=0, end, index, next(int index), prev(int index);
};
