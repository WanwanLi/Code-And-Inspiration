#include "QEnergy.h"

class QOptimizer
{
	public:
	int iterations;
	double epsilon; 
	int maxIterations;
	double minEnergy;
	MatrixXd sketchPlanes;
	bool minimize(QString fileName);
	QOptimizer(double e, int t):epsilon(e), maxIterations(t){}
};