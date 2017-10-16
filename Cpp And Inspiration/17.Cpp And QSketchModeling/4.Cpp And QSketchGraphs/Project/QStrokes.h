#include <QPoint>
#include <QVector>
#include <QVector2D>
#include "QCurve.h"
#include "QGraph.h"

class QStrokes
{
	public:
	int size();
	void update();
	void undo(), clear();
	void createGraph();
	int& operator[](int index);
	void operator=(QPoint point);
	void operator+=(QPoint point);
	QStrokes& operator<<(int path);


	private:
	QCurve curve;
	void removeLast();
	QVector<int> path;
	void updateGraph();
	void setPath(int type);
	enum{MOVE, LINE, CUBIC};
	void removeLast(int length);
	QGraph graph=QGraph(path);
	void startPath(QPoint& point, QVector2D& direction);
	int begin=0, end, index, next(int index), prev(int index);
};
