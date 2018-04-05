#include <QPoint>
#include <QVector>
#include <QVector2D>

class QCurve
{
	public:
	int size();
	void clear();
	qreal length();
	bool isCubic();
	bool isLinear();
	bool isCircular();
	qreal fittingError;
	int minLength=10;
	void operator--(int);
	qreal dirError=1.0;
	qreal minError=2.0;
	qreal maxError=4.0;
	int maxIterations=800;
	QVector2D direction();
	QVector2D ctrlPoints[4];
	QVector2D ctrlTangents[2];
	void operator+=(QPoint& point);
	QVector2D& operator[](int index);
	QVector<QVector2D> curvePoints;

	private:
	QVector2D bezierPoints[4];
	QVector2D tangent(int i, int j);
	QVector2D pointAt(qreal param);
	qreal* getChordLengthParameters();
	qreal getFittingError(qreal* params);
	void optimizeParameters(qreal* params);
	bool isCloseTo(qreal src, qreal dest, qreal error);
	qreal optimize(QVector2D& curvePoint, qreal param);
	qreal B0(qreal u), B1(qreal u), B2(qreal u), B3(qreal u);
	qreal distanceToCircle(const QVector2D& center, qreal radius);
	QVector2D pointAt(QVector2D ctrlPoints[], qreal param, int degree);
	QVector2D tangent(const QVector2D& center, const QVector2D& point);
	void getCtrlPoints(qreal* params, QVector2D& leftTangent, QVector2D& rightTangent);
};
