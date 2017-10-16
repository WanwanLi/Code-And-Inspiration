#include "QOptimizer.h"
#include <LBFGS.h>
using namespace LBFGSpp;

struct Energy
{
	QEnergy& energy; const VectorXi sketch; const VectorXi joint;
	Energy(QEnergy& e, const VectorXi& s, const VectorXi& t):energy(e), sketch(s), joint(t){}
	double operator()(const VectorXd& plane, VectorXd& grad)
	{
		double value;
		auto totalEnergy=[&](const VectorXv& plane) 
		{ 
			return energy.totalEnergy(sketch, joint, plane); 
		};
		gradient(totalEnergy, plane, value, grad);
		return value;
	}
};
bool QOptimizer::minimize(QString fileName)
{
	QEnergy energy;
	if(energy.loadSketchFile(fileName))
	{
		VectorXd x=energy.planeVector; 
		VectorXi sketch=energy.sketchVector;
		VectorXi joint=energy.jointVector;
		LBFGSParam<double> param;
		param.epsilon=epsilon; double fx;
		param.max_iterations=maxIterations;
		LBFGSSolver<double> solver(param);
		Energy f(energy, sketch, joint);
		this->iterations=solver.minimize(f, x, fx);
		this->sketchPlanes=energy.sketchPlanes(x);
		this->minEnergy=fx; return true;
	}
	else return false;
}

