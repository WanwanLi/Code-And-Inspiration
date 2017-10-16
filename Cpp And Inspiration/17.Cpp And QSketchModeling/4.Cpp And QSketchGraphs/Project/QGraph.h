#include <QVector>
#include <QVector2D>

class QGraph
{
	public:
	QGraph(QVector<int>& p):path(p){}
	void operator+=(int* edge);
	void operator--(int);
	void updateEdge();
	void clear();


	private:

	QVector<int>& path;
	QVector<int> vertices;
	QVector<int*> edges;
	void updateVertices();
	bool isClose(int i, int j);
	qreal minDistance=30.0;
	enum{MOVE, LINE, CUBIC};
	QVector2D getPoint(int index);
	int add(int vertex), append(int vertex);
	void setPoint(int index, QVector2D& point);
	void addVertex(QVector<int>& vertices, int index);
};
