#include <QPoint>
#include <QVector>
#include <QVector2D>

class QStrokes
{
	public:
	int size();
	void undo(), clear();
	int& operator[](int index);
	void operator=(QPoint point);
	void operator+=(QPoint point);
	QStrokes& operator<<(int path);

	private:
	QVector<int> path;
	QVector<QPoint> line;
	enum{MOVE, LINE, CUBIC};
	void begin(QPoint point), end(QPoint point);
	qreal distanceToLine(QPoint& point, QVector<QPoint>& line, qreal& length);
	qreal distance(), maxDistance(), length(QPoint point), minLength(), maxLength();
};