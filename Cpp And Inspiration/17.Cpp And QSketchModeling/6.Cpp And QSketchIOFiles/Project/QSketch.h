#include "QStrokes.h"
#include <QPainterPath>

class QSketch : public QPainterPath
{
	public:
	QSize size;
	void clear();
	void update();
	QViewer viewer;
	QPlanes planes;
	void resize(QSize size);
	bool load(QString fileName);
	bool save(QString fileName);
	QStrokes strokes=QStrokes(viewer, planes);

	private:
	enum{MOVE=-3, LINE, CUBIC};
};
