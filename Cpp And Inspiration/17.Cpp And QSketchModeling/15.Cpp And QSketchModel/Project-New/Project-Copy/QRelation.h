#include <QVector>
#include <QVector2D>
typedef QVector2D vec2;
typedef QVector<int> array;

class QRelation
{
	public:
	QVector<array> sketchPaths, sketchCurves, curvesPoints, curvesCoords, matchCurves, matchPoints;
	QRelation(const array& path, const array& sketch, vec2 xAxis, vec2 yAxis, vec2 zAxis);
	void debugSketchCurves(), debugCurvesPoints();
	void create( ), debug(), debugAll();
	array markerLines, markerPoints;
	
	private:
	enum 
	{
		NONE,
		SIMILAR,
		IDENTICAL,
		SYMMETRIC,
		PARALLEL,
		PARALLEL_X, 
		PARALLEL_Y,
		PARALLEL_Z,
		PERPENDICULAR,
		PERPENDICULAR_X,
		PERPENDICULAR_Y,
		PERPENDICULAR_Z
	};
	int curveSize=21;
	vec2 ctrlPoints[4];
	vec2 bezierPoints[4];
	QVector<bool> isJoint;
	vec2 xAxis, yAxis, zAxis;
	enum{MOVE, LINE, CUBIC};
	void completeSketchCurves();
	qreal cross2(vec2 p1, vec2 p2);
	bool isJointType(int curveIndex);
	qreal length(const array& curve);
	bool isSymmetric(const array& curve);
	vec2 getBezierCurvePoint(qreal param);
	array getCurvesPoints(int index, int size);
	array path, sketch, relation, sketchTypes;
	vec2 tangent(const array& curve, int index);
	QString pathStr(int path), typeStr(int index);
	vec2 getPoint(int curveIndex, int pointIndex);
	bool isParallel(const vec2& x, const vec2& y);
	void getCurvesRelations(), getSketchCurves();
	vec2 reflect(vec2 point, vec2 origin, vec2 axis);
	void completeOpenCurveWithJoint(int curveIndex);
	bool isJointType(const QVector<qreal>& positions);
	qreal error=0.01, minDistance=5, curveStep=10.0;
	vec2 intersect(vec2 p1, vec2 p2, vec2 p3, vec2 p4);
	QVector<qreal> getCurveKnots(const array& curve);
	enum{LINE_SEGMENT, CLOSE_CURVE, OPEN_CURVE};
	bool equals(const array& curve, int index1, int index2);
	vec2 getLinePoint(const array& curve, int index, qreal t);
	vec2 getCubicPoint(const array& curve, int index, qreal t);
	bool isSimilar(const array& curve1, const array& curve2);
	void getCurvesRelations(int curveIndex1, int curveIndex2);
	bool isIdentical(const array& curve1, const array& curve2);
};


