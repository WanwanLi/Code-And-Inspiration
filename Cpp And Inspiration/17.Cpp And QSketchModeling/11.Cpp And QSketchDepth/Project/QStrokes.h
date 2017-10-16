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
	qreal& operator[](int index);
	void operator=(QPoint point);
	void operator+=(QPoint point);
	QStrokes& operator<<(qreal path);
	qreal depth(int index, qreal position);
	QGraph graph=QGraph(path, viewer);
	void updateDepthPath(), updateDepthTest();
	void operator<<=(const QVector<int>& strokes);
	bool isSymmetric=false, isDepthTestEnabled=false;
	QStrokes(QViewer& v, QPlanes& p) : viewer(v), planes(p){}
	void clear(), update(), createGraph(), extrudePlanes(int from, int to);
	int begin=0, end, index, next(int index), prev(int index), planeIndex(int index);


	private:
	QCurve curve;
	void removeLast();
	void updateGraph();
	void setPath(int type);
	void removeLast(int length);
	enum{MOVE=-3, LINE, CUBIC};
	QVector2D getPoint(int index);
	QVector<qreal> path, depthPath;
	void append(const QVector<qreal>& strokes);
	int insert(QVector<qreal>& vector, qreal value);
	void startPath(QPoint& point, QVector2D& direction);
	bool equals(qreal x, qreal y), greater(qreal x, qreal y);
	void appendDepthPath(int index, int type, qreal position);
	void appendDepthPath(int index), addDepthPath(int index);
	bool isEndPosition(const QVector<qreal>& positions, int index);
	void addQuad(QVector<qreal>& strokes, QVector3D* points, int index);
	bool intersectLineWithPlane(qreal& position, qreal& depth, int start, int end, int plane);
	bool intersectLineWithLine(qreal& position, qreal& depth, int start0, int end0, int start1, int end1);
	bool intersectDepthPath(QVector<qreal>& positions, QVector<qreal>& depths, int index0, int index1);
	void minimizeEndDepth(qreal position, QVector3D end, QVector<qreal>& positions, QVector<qreal>& depths, int plane);
	void updateIntersectionInfo(QVector<int>& times, QVector<qreal>& positions, QVector<qreal>& depths, qreal position, qreal depth, int plane);
};
