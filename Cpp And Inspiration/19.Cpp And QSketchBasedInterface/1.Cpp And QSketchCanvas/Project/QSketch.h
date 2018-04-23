#include <QPainterPath>

class QSketch : public QPainterPath
{
	public:
	QSketch(QSize size);
	bool load(QString fileName);
	bool save(QString fileName);
	void move(QPoint point);
	void line(QPoint point);
	void clear();
	QSize size();

	private:
	QSize sketchSize;
	QVector<int> strokes;
	enum{MOVE, LINE, CUBIC};
};
