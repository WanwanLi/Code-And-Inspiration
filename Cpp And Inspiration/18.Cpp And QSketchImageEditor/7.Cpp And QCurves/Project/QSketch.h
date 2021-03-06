#include "QDefine.h"
#include <QPainterPath>

class QSketch : public QPainterPath
{
	public:
	enum{MOVE, LINE, CUBIC};
	QSketch():QPainterPath(){}
	bool load(QString fileName);
	bool save(QString fileName);
	void operator=(QPoint point);
	void operator+=(QPoint point);
	QSize size; void resize(QSize size);
	QVector<vec2> point2D; veci path;
	void update(), clear(), removeLast();
	QSketch(QSize size):size(size), QPainterPath(){}
};
