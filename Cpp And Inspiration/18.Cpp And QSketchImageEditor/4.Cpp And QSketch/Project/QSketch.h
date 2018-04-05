#include "QDefine.h"
#include <QPainterPath>

class QSketch : public QPainterPath
{
	private:
	enum{MOVE, LINE, CUBIC};
	QVector<vec2> point2D; veci path;

	public:
	void update(), clear();
	QSketch():QPainterPath(){}
	bool load(QString fileName);
	bool save(QString fileName);
	void operator=(QPoint point);
	void operator+=(QPoint point);
	QSize size; void resize(QSize size);
	QSketch(QSize size):size(size), QPainterPath(){}
};
