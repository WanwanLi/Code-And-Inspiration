#include "QEnergy.h"
#include <QPainterPath>

class QSketch;
class QOptimizer
{
	public:
	int step;
	VectorXd x;
	int timer=0;
	int interval=10;
	bool isValid;
	int iterations;
	void update();
	double epsilon; 
	QEnergy energy;
	QSketch*sketch;
	int maxIterations;
	double minEnergy;
	void createRelations();
	MatrixXd sketchCurves;
	MatrixXd sketchPlanes;
	void minimizeTotalEnergy();
	void minimizePlaneEnergy();
	void perturbPlanes(qreal noise);
	void drawMarkers(QPainter& painter);
	void drawRelations(QPainter& painter);
	bool loadSketchFile(QString fileName);
	MatrixXv toMatrixXv(const VectorXd & vectorXd);
	MatrixXd toMatrixXd(const MatrixXv& matrixXv);
	QVector<QVector<int>> matchPoints, matchCurves;
	QVector<int> markerLines, markerPoints, startPoints;
	void drawMatchPoints(QPainter& painter, int& index);
	QOptimizer(double e, int t, int s): epsilon(e), maxIterations(t), step(s), isValid(false){}
};