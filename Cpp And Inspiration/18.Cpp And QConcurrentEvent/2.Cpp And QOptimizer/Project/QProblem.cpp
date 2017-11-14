#include <QDebug>
#include "QEnergy.h"
#include "QProblem.h"

QProblem::QProblem(QEnergy* energy)
{
	this->energy=energy;
	this->getValue(energy->planeVector);
}
VectorXv toVectorXv(const VectorXd& vectorXd)
{
	VectorXv vectorXv(vectorXd.size());
	for(int i=0; i<vectorXd.size(); i++)vectorXv[i]=vectorXd[i];
	return vectorXv;
}
std::vector<var> toVector(const VectorXv& vectorXv)
{
	std::vector<var> vars(vectorXv.data(), vectorXv.data()+vectorXv.size()); return vars;
}
void QProblem::getValue(const VectorXd& variable)
{
	auto totalEnergy=[&](const VectorXv& variable)
	{ 
		return energy->totalEnergy(variable);
	};
	stan::math::gradient(totalEnergy, variable, val, grad);
}
double QProblem::value(const VectorXd& variable)
{
	this->getValue(variable);
	return val;
}
void QProblem::gradient(const VectorXd& variable, VectorXd &grad)
{
	grad=this->grad;
}
#define emitUnderLine(x) {QString s=""; for(int i=0; i<x; i++)s+="_"; qDebug()<<s.left(30);}
bool QProblem::callback(const Criteria<double> &criteria, const VectorXd& variable) 
{
	emit valueChanged(criteria.iterations, variable);
	//emitUnderLine(100000); 
	return true;
}
