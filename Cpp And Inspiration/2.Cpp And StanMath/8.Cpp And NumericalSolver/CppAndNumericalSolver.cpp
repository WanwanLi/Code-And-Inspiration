#include <iostream>
#include <meta.h>
#include <problem.h>
#include <solver/all.h>
#include <stan/math.hpp>

#define CALL_BACK 0
using namespace std;
using namespace Eigen;
using namespace cppoptlib;
using namespace stan::math;
typedef Matrix<var, Dynamic, 1> VectorXv;

var func(var x, var y, var u, var v)
{
	return (x+u)*(x+u)+(y+v)*(y+v)+u*v;
}
class Function : public Problem<double> 
{
	public:
	double val;
	VectorXd vec, grad;
	Function(const VectorXd& vec, const VectorXd& arg)
	{
		this->vec=vec;
		this->getValue(arg);
	}
	void getValue(const VectorXd& arg)
	{
		auto functor=[&](const VectorXv& arg)
		{ 
			return func(arg[0], arg[1], vec[0], vec[1]);
		};
		stan::math::gradient(functor, arg, val, grad);
	}
	double value(const VectorXd& arg)
	{
		this->getValue(arg);
		return val;
	}
	void gradient(const VectorXd& variable, VectorXd &grad)
	{
		grad=this->grad;
	}
	#ifdef CALL_BACK
	bool callback(const Criteria<double> &criteria, const VectorXd& arg) 
	{
		cout<<"Iterations: "<<criteria.iterations<<endl;
		cout<<"x="<<arg<<endl<<"f(x)="<<value(arg)<<endl;
		return true;
	}
	#endif
};
int main(int argc, char* argv[]) 
{
	VectorXd v(2), x(2); v<<-2, 2; x<<-1, 2; Function f(v, x);
	Criteria<double> criteria=Criteria<double>::defaults();
	criteria.iterations=100; 
	GradientDescentSolver<Function> solver;
	solver.setStopCriteria(criteria);
	solver.minimize(f, x);
	cout<<"x="<<endl<<x<<endl<<"f(x)="<<f(x)<<endl;
	cout<<"Solver status:" <<endl<< solver.status()<<endl;
	cout<<"Solver criteria:"<<endl<<solver.criteria()<<endl;
	return 0;
}