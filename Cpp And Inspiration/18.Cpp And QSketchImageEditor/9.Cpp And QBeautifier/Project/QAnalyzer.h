#include "QDefine.h"
#include <QPainter>

class QSketch;
class QAnalyzer
{
	public:
	enum
	{
		LOOP,
		VERTICAL,
		PARALLEL, 
		HORIZONTAL,
		PERPENDICULAR
	};

	void run(), clear();
	QSketch* sketch;
	veci path, regularity;
	vec2 yAxis=vec2(0, 1);
	vec2 xAxis=vec2(1, 0);
	vec2 tangentAt(int index);
	static int count(int value);
	int toValue(QString string);
	QString toString(int value);
	bool isLoop(int startIndex);
	void load(QSketch* sketch);
	void save(QString fileName);
	QVector<veci> parallelLines;
	bool isParallel(vec2 x, vec2 y);
	int prev(int index), next(int index);
	void operator<<(QStringList& list);
	qreal error=0.01, maxError=0.05;
	QVector<vec2> avgLineDirections;
	QVector<vec2> point2D, curves, loops;
	void drawRegularity(QPainter& painter);
	bool isParallelToLines(int lines, int line);
	void updateAvgLineDirections(int index);
	void addParallelLines(int line1, int line2);
	vec2 addLineDirection(vec2 dir1, vec2 dir2);
	void getCurveRegularity(int startIndex, int endIndex);
};
