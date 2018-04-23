#include "QDefine.h"
#include <QPainter>

class QSketch;
class QCubic
{
	public:
	int tangentSize=5;
	QCubic(){} vec2 ctrlPoints[4];
	QVector<vec2> curvePoints;
	vec2 leftTangent, rightTangent;
	QCubic(QVector<vec2> curvePoints);
	bool minimizeFittingError(qreal error);
	vec2 tangent(int startIndex, int endIndex);
	vec2 direction(int startIndex, int endIndex);
	void split(QVector<QCubic>& curves, qreal error);

	private:
	int maxIterations=50;
	vec2 pointAt(qreal param);
	qreal length(), getFittingError();
	void optimizeParameters(); int size();
	vec2 bezierPoints[4]; void getCtrlPoints();
	qreal optimize(vec2 curvePoint, qreal param);
	vec params; void getChordLengthParameters();
	qreal B0(qreal u), B1(qreal u), B2(qreal u), B3(qreal u);
	vec2 pointAt(vec2 ctrlPoints[], qreal param, int degree);
};
class QCurve
{
	public:
	void clear();
	void operator=(QPoint point);
	void operator<<(QPoint point);
	void operator<<=(QPoint point);
	void drawLines(QPainter& painter);
	void drawPolygon(QPainter& painter);
	void drawControlPoints(QPainter& painter);
	QCurve(QSketch* sketch):sketch(sketch){}

	private:
	QSketch* sketch;
	int maxError=10;
	int minError=2;
	qreal error=0.5;
	int minLineLength=20;
	QVector<vec2> point2D;
	vecb isLinear; void linearize();
	veci polygon; void polygonize();
	void split(); QVector<QCubic> curves;
	qreal lengthBetween(int beginIndex, int endIndex);
	bool isLinearBetween(int beginIndex, int endIndex, int& middleIndex, qreal fittingError);
};
