#include "QSketch.h"

class QMesh
{
	public:
	QMesh(QSketch& sketch);
	QVector<QVector3D*> quads;
	QVector<QVector<qreal>> coords;

	private:
	QPlanes planes;
	void getCoordinates();
	QVector<qreal> strokes;
	qreal curveLength=0.05;
	enum{MOVE=-3, LINE, CUBIC};
	QVector3D pointAt(QVector3D* ctrlPoints, qreal t);
	void addPoint(QVector<qreal>& coordinates, const QVector3D& point, QVector3D* quad);
	void addCurve(QVector<qreal>& coordinates, QVector3D* ctrlPoints,  QVector3D* quad);
};
