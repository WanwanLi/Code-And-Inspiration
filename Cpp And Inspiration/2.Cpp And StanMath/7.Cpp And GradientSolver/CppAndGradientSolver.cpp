#include <stan/math.hpp>
#include <Eigen/Dense>
#include "Solver.hpp"
using namespace Eigen;
using namespace stan::math;

var func(var x, var y, var u, var v)
{
	return (x+u)*(x+u)+(y+v)*(y+v)+u*v;
}
struct Functor
{
	const VectorXd vec;
	Functor(const VectorXd& v):vec(v){}
	#define VectorXt Matrix<T, Dynamic, 1>
	#define Template(T) template<typename T>
	Template(T) T operator()(const VectorXt& arg) const
	{
		T x=arg(0), y=arg(1);
		T u=vec(0), v=vec(1);
		return func(x, y, u, v);
	}
	#undef Template(T) 
};
double f(const VectorXd& arg, VectorXd& grad)
{
	VectorXd v(2); v<<-2, 2; Functor functor(v);
	double val; gradient(functor, arg, val, grad); return val;
}
int main(int argc, char* argv[])
{
	Param<double> param;
	param.epsilon=1e-6;
	param.max_iterations=100;
	Solver<double> solver(param);
	VectorXd x(2); x<<10, 10; double fx;
	int t=solver.minimize(f, x, fx);
	using namespace std;
	cout<<t<<" iterations"<<endl;
	cout<<"x="<<endl<<x<<endl;
	cout<<"f(x)="<<fx<<endl;
}