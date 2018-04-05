#include <QPoint>
#include <QVector>
#include "QStrokes.h"

class QShapes
{
	public:
	int size();
	QVector<int> path;
	int symmetryIndex=-1;
	bool isSymmetric=false;
	bool isOnCreating=false;
	int& operator[](int index);
	void operator=(QPoint point);
	void operator+=(QPoint point);
	enum{RECT=1, ELLIPSE, POLYGON};
	void create(int shape), finish(QStrokes& strokes);
	QVector2D reflect(QPoint& point, QVector2D& orgin, QVector2D& axis);
	void addSymmetry(QStrokes& strokes, QVector2D& orgin, QVector2D& axis);

	private:
	int shape=0;
	QPoint min, max;
	QVector<QPoint> stroke;
	enum{MOVE=-3, LINE, CUBIC};
	qreal c=0.551915, maxError=1.0;
	void updateMinMax(const QPoint& point);
	void getEllipse(), getRectangle(), getPolygon();
	qreal distanceToLine(int beginIndex, int endIndex);
	QVector2D ellipse[13]=
	{
		QVector2D(0,1), QVector2D(c,1), QVector2D(1,c), 
		QVector2D(1,0), QVector2D(1,-c), QVector2D(c,-1), 
		QVector2D(0,-1), QVector2D(-c,-1), QVector2D(-1,-c), 
		QVector2D(-1,0), QVector2D(-1,c), QVector2D(-c,1), QVector2D(0,1)
	};
};
