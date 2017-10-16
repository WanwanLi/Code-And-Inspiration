#include "QEnergy.h"
#include <QPainterPath>

class QOptimizer
{
	public:
	bool isValid;
	int iterations;
	double epsilon; 
	QEnergy energy;
	int maxIterations;
	double minEnergy;
	void createRelations();
	void minimizeEnergy();
	MatrixXd sketchCurves;
	MatrixXd sketchPlanes;
	void perturbPlanes(qreal noise);
	void drawMarkers(QPainter& painter);
	bool loadSketchFile(QString fileName);
	QVector<int> markerLines, markerPoints;
	MatrixXv toMatrixXv(const VectorXd & vectorXd);
	MatrixXd toMatrixXd(const MatrixXv& matrixXv);
	QVector<QVector<int>> matchPoints, matchCurves;
	void drawMatchPoints(QPainter& painter, int& index);
	QOptimizer(double e, int t):epsilon(e), maxIterations(t), isValid(false){}

};
