#include <Eigen/Core>
using namespace Eigen;

#define Template(T) template<typename T>
Template(Scalar) class Param
{
	public:
	Scalar step=0.1;
	Scalar epsilon=1e-5;
	int max_iterations=100;
};
Template(Scalar) class Solver
{
	public:
	Param<Scalar> param;
	Solver(Param<Scalar> p):param(p){}
	typedef Matrix<Scalar, Dynamic, 1> Vector;
	typedef Matrix<Scalar, Dynamic, Dynamic> Matrix;
	Template(Function) int minimize(Function& f, Vector& x, Scalar& fx)
	{
		Scalar zero=param.epsilon, step=param.step;
		int t=0, time=param.max_iterations; Vector dx;
		for
		(
			fx=f(x, dx); 
			dx.dot(dx)>zero&&t<time;
			x-=step*dx, fx=f(x, dx), t++
		);
		return t;
	}
};
#undef Template(T)
