#include <QVector>
#include <QVector3D>

class QGraph
{
	public:
	void clear();
	void updateEdge();
	void operator--(int);
	void operator+=(int* edge);
	QGraph(QVector<qreal>& p):path(p){}


	private:
	QVector<qreal>& path;
	QVector<int> vertices;
	QVector<int*> edges;
	void updateVertices();
	qreal minDistance=0.2;
	enum{MOVE=-3, LINE, CUBIC};
	QVector3D getPoint(int index);
	int add(int vertex), append(int vertex);
	void setPoint(int index, QVector3D& point);
	void addVertex(QVector<int>& vertices, int index);
};
