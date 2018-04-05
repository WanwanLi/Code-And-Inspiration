#include <QVector>
#include "QViewer.h"
#include <QVector4D>

class QGraph
{
	public:
	int size();
	void clear();
	void updateEdge();
	void operator--(int);
	int operator[](int index);
	void operator+=(int* edge);
	QVector4D getPoint(int index);
	QGraph(QVector<qreal>& p, QViewer& v):path(p), viewer(v){}

	private:
	QViewer& viewer;
	QVector<qreal>& path;
	QVector<int> vertices;
	QVector<int*> edges;
	void updateVertices();
	qreal minDistance=30.0;
	enum{MOVE=-3, LINE, CUBIC};
	int add(int vertex), append(int vertex);
	void setPoint(int index, QVector4D& point);
	void addVertex(QVector<int>& vertices, int index);
	qreal distanceBetween(const QVector4D& point1, const QVector4D& point2);
};
