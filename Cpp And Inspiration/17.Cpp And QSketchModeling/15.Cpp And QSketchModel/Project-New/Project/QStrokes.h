#include <QVector>
#include <QVector2D>

class QStrokes
{
	public:
	QVector<QVector<int>> sketchPaths, sketchCurves, curvesPoints, curvesCoords, matchCurves, matchPoints;
	void debugSketchCurves(), debugCurvesPoints();
	QVector<int> markerLines, markerPoints;
	void debug(), debugAll();

	void initialize(), create();
	QStrokes& operator<<(int stroke);
	
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
	QVector2D ctrlPoints[4];
	QVector2D bezierPoints[4];
	QVector<bool> isJoint;
	QVector2D xAxis, yAxis, zAxis;
	enum{MOVE=-3, LINE, CUBIC};
	void completeSketchCurves();
	qreal cross2(QVector2D p1, QVector2D p2);
	bool isJointType(int curveIndex);
	qreal length(const QVector<int>& curve);
	bool isSymmetric(const QVector<int>& curve);
	QVector2D getBezierCurvePoint(qreal param);
	QVector<int> getCurvesPoints(int index, int size);
	QVector<int> path, sketch, relation, sketchTypes;
	QVector2D tangent(const QVector<int>& curve, int index);
	QString pathStr(int path), typeStr(int index);
	QVector2D getPoint(int curveIndex, int pointIndex);
	bool isParallel(const QVector2D& x, const QVector2D& y);
	void getCurvesRelations(), getSketchCurves();
	QVector2D reflect(QVector2D point, QVector2D origin, QVector2D axis);
	void completeOpenCurveWithJoint(int curveIndex);
	bool isJointType(const QVector<qreal>& positions);
	qreal error=0.01, minDistance=5, curveStep=10.0;
	QVector2D intersect(QVector2D p1, QVector2D p2, QVector2D p3, QVector2D p4);
	QVector<qreal> getCurveKnots(const QVector<int>& curve);
	enum{LINE_SEGMENT, CLOSE_CURVE, OPEN_CURVE};
	bool equals(const QVector<int>& curve, int index1, int index2);
	QVector2D getLinePoint(const QVector<int>& curve, int index, qreal t);
	QVector2D getCubicPoint(const QVector<int>& curve, int index, qreal t);
	bool isSimilar(const QVector<int>& curve1, const QVector<int>& curve2);
	void getCurvesRelations(int curveIndex1, int curveIndex2);
	bool isIdentical(const QVector<int>& curve1, const QVector<int>& curve2);
};


