#include "QDefine.h"
#include "QCurve.h"
#include <QPainterPath>

class QSketch : public QPainterPath
{
	private:
	veci path, point2D;
	enum{MOVE, LINE, CUBIC};
	vec2 getPoint2D(int startIndex);
	void setPoint2D(int startIndex, vec2 point);

	public:
	QCurve curve;
	void update(), clear();
	bool load(QString fileName);
	bool save(QString fileName);
	void operator=(QPoint point);
	void operator<<(QPoint point);
	void operator<<=(QPoint point);
	QSize size; void resize(QSize size);
	QSketch():curve(QCurve(this)), QPainterPath(){}
	QSketch(QSize size):size(size), curve(QCurve(this)), QPainterPath(){}
};
