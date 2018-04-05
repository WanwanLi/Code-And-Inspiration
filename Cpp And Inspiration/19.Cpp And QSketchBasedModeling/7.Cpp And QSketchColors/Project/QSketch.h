#include "QStrokes.h"
#include <QPainterPath>

class QSketch
{
	public:
	QSize size;
	int length();
	void clear();
	void update();
	QViewer viewer;
	QPlanes planes;
	void resize(QSize size);
	bool load(QString fileName);
	bool save(QString fileName);
	QPainterPath& operator[](int i);
	QVector<QPainterPath> painterPaths;
	QStrokes strokes=QStrokes(viewer, planes);

	private:
	void updatePainterPaths();
	enum{MOVE=-3, LINE, CUBIC};
	void moveTo(int x, int y, int index), lineTo(int x, int y, int index);
	void cubicTo(int x1, int y1, int x2, int y2, int x3, int y3, int index);
};
