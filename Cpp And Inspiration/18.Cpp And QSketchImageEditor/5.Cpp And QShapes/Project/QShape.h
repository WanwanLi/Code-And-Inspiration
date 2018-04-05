#include "QSketch.h"
#include <QPainter>

class QShape : public QSketch
{
	public:
	void clear();
	QShape():QSketch(){}
	void operator=(QPoint point);
	void operator<<(QPoint point);
	void drawPoints(QPainter& painter);
	void drawEndPoints(QPainter& painter);
	bool isEditable=false, isAutoAligned=true;

	private:
	void getIndex(QPoint point);
	vec2 getPoint(QPoint point);
	QPoint toQPoint(vec2 point);
	void addLineTo(QPoint point);
	QPoint NONE=QPoint(-100, -100);
	bool isStarted=false; int index=-1;
	bool equals(QPoint point1, QPoint point2);
	QPoint movePoint=NONE, lastPoint=NONE;
	qreal error=20; bool is(int type); int type=LINE;
};
