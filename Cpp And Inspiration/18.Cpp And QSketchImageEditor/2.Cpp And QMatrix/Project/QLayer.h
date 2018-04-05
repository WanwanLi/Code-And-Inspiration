#include <QImage>
#include <QVector>
#include <QPainter>

class QLayer : public QImage
{
	public:
	QImage image();
	QLayer():QImage(){}
	QPoint controlPoint();
	static int minDistance;
	bool contains(QPoint point);
	bool load(QString fileName);
	void reset(QPainter& painter);
	QVector<QPoint> controlPoints;
	QVector<QPoint> cornerPoints;
	bool getControlPoint(QPoint point);
	bool setControlPoint(QPoint point);
	void setTransform(QPainter& painter);
	static bool isClose(QPoint p0, QPoint p1);
	void drawControlPoints(QPainter& painter, int radius);

	private:
	enum
	{
		CENTER,
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT
	};
	bool is(int index);
	int controlIndex=-1;
	QTransform transform;
	void updateTransform();

};
