#include <QPoint>
#include <QVector>
#include <QVector2D>
#include "QCurve.h"

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
	int index;
	QCurve curve;
	QVector<int> path;
	void setPath(int type);
	enum{MOVE, LINE, CUBIC};
	void removeLast(int length);
	void startPath(QPoint& point, QVector2D& direction);
};
