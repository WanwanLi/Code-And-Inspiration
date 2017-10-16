#include <stan/math.hpp>
#include <Eigen/Dense>
#include <LBFGS.h>
using namespace std;
using namespace Eigen;
using namespace LBFGSpp;
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
	template <typename T>
	T operator()(const VectorXt& arg) const
	{
		T x=arg(0), y=arg(1);
		T u=vec(0), v=vec(1);
		return func(x, y, u, v);
	}
};
double f(const VectorXd& arg, VectorXd& grad)
{
	VectorXd v(2); v<<-2, 2;
	Functor functor(v); double val;
	gradient(functor, arg, val, grad); return val;
}
int main(int argc, char* argv[])
{
	LBFGSParam<double> param;
	param.epsilon=1e-6;
	param.max_iterations=100;
	LBFGSSolver<double> solver(param);
	VectorXd x(2); x<<0, 0; double fx;
	int t=solver.minimize(f, x, fx);
	cout<<t<<" iterations"<<endl;
	cout<<"x="<<endl<<x<<endl;
	cout<<"f(x)="<<fx<<endl;
}
