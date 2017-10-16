#include <QSize>
#include <QVector3D>
#include <QPainterPath>

class QSketch
{
	public:
	void clear();
	int length();
	QSize size();
	void create();
	void update();
	void minimizeEnergy();
	void createRelations();
	double getViewDistance();
	bool load(QString fileName);
	bool save(QString fileName);
	QVector3D*getViewDirection();
	QPainterPath& operator[](int i);
	void perturbPlanes(qreal noise);
	QVector<QPainterPath> painterPaths;
	void drawMarkers(QPainter& painter);
	void setViewDistance(double viewDistance);
	void drawMatchPoints(QPainter& painter, int& index);
	void setViewDirection(QVector3D& right, QVector3D& up, QVector3D& forward);

	private:
	int width, height;
	QSize canvasSize;
	enum{MOVE, LINE, CUBIC};
	void updatePainterPaths();
	QVector<int> joints, indices;
	QVector<double> sketch, planes;
	void moveTo(int x, int y, int index), lineTo(int x, int y, int index);
	void cubicTo(int x1, int y1, int x2, int y2, int x3, int y3, int index);
};
