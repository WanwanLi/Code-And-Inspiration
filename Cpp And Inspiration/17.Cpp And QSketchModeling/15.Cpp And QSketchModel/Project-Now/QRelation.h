#include <QVector>
#include <QVector2D>
#include <QVector3D>
#include <QVector4D>
typedef QVector2D vec2;
typedef QVector3D vec3;
typedef QVector4D vec4;
typedef QVector<int> array;

class QRelation
{
	public:
	QVector<array> curvesPoints, curvesCoords, sketchPaths, sketchCurves, matchCurves, matchPoints;
	QRelation(const array& path, const array& sketch, vec2 xAxis, vec2 yAxis, vec2 zAxis);
	array path, sketch, relation, sketchVector, sketchSizes, sketchTypes, sketchPlanes;
	void create(), update(), debugAll(), debugSketchCurves(), debugCurvesPoints();
	array markerLines, markerPoints, startPoints;
	QVector<QVector<vec4>> parallelLines;
	int& operator[](int index);
	int planesSize, size();
	QRelation(){}
	private:
	enum 
	{
		VERTICAL,
		PARALLEL, 
		DISTANCE,
		COPLANAR,
		HORIZONTAL,
		SAME_POINTS,
		PERPENDICULAR,
		CONTACT_POINTS, 
		PARALLEL_PLANES,
		SYMMETRIC, IDENTICAL, 
	};
	int curveSize=21;
	vec2 ctrlPoints[4];
	array contactPoints;
	vec2 bezierPoints[4];
	QVector<bool> isJoint;
	vec2 xAxis, yAxis, zAxis;
	vec2 getPoint(int index);
	enum{MOVE, LINE, CUBIC};
	void initializeSketchCurves();
	void completeSketchCurves();
	qreal cross2(vec2 p1, vec2 p2);
	qreal length(const array& curve);
	bool isSymmetric(const array& curve);
	void getContactPoints(int curveIndex);
	vec2 getBezierCurvePoint(qreal param);
	void getCurvesRelations(int curveIndex);
	array getCurvesPoints(int index, int size);
	int indexOf(int curveIndex, int pointIndex);
	int getIndex(int curveIndex, int pointIndex);
	QString pathStr(int path), typeStr(int index);
	vec2 tangent(const array& curve, int index);
	vec2 getPoint(int curveIndex, int pointIndex);
	void addParallelLines(vec4 line1, vec4 line2);
	bool isParallel(const vec2& x, const vec2& y);
	vec2 reflect(vec2 point, vec2 origin, vec2 axis);
	bool isJointType(int curveIndex, int& planeIndex);
	void completeOpenCurveWithJoint(int curveIndex);
	bool isJointType(const QVector<qreal>& positions);
	int nextContactPoint(int curveIndex, int pointIndex);
	vec2 intersect(vec2 p1, vec2 p2, vec2 p3, vec2 p4);
	QVector<qreal> getCurveKnots(const array& curve);
	qreal error=0.05, minDistance=5.0, curveStep=10.0;
	enum{LINE_SEGMENT, CLOSE_CURVE, OPEN_CURVE};
	bool equals(const array& curve, int index1, int index2);
	vec2 getLinePoint(const array& curve, int index, qreal t);
	bool isSimilar(const array& curve1, const array& curve2);
	vec2 getCubicPoint(const array& curve, int index, qreal t);
	void getCurvesRelations(int curveIndex1, int curveIndex2);
	bool isIdentical(const array& curve1, const array& curve2);
};


