#include <stan/math.hpp>
#include <Eigen/Dense>
using namespace std;
using namespace Eigen;
using namespace stan::math;

var f(var x, var y)
{
	return x*x+y*y;
}
void testGradients1()
{
	var x=0.5, y=1.0;
	vector<double> grad;
	vector<var> arg({x, y});
	var func=f(x, y); func.grad(arg, grad);
	cout<<"f(x, y)="<<func.val()<<endl;
	cout<<"df(x, y)/dx="<<grad[0]<<endl;
	cout<<"df(x, y)/dy="<<grad[1]<<endl;
}
struct functor
{
	const MatrixXd matrix;
	functor(const MatrixXd& m):matrix(m){}
	#define VectorXt Matrix<T, Dynamic, 1>
	template <typename T>
	T operator()(const VectorXt& arg) const
	{
		T result=0;
		for(int i=0; i<matrix.rows(); i++)
		{
			for(int j=0; j<matrix.cols(); j++)
			{
				result+=matrix(i, j)*f(arg[0], arg[1]);
			}
		}
		return result;
	}
};
void testGradients2()
{
	double val;
	VectorXd grad;
	MatrixXd m(2, 2);
	m<<1, 2, 3, 4;
	functor func(m);
	VectorXd arg(2);
	arg<<0.5, 1.0;
	gradient(func, arg, val, grad);
	cout<<"f(x, y)="<<val<<endl;
	cout<<"df(x, y)/dx="<<grad(0)<<endl;
	cout<<"df(x, y)/dy="<<grad(1)<<endl;
}
#define clearAdjoints set_zero_all_adjoints
typedef Matrix<var, Dynamic, 1> VectorXv;
VectorXv g(VectorXv arg)
{
	VectorXv result(3);
	result(0)=arg(0)+arg(1);
	result(1)=arg(0)-arg(1);
	result(2)=arg(0)*arg(1);
	return result;
}
MatrixXd Jacobian(VectorXv func, VectorXv arg)
{
	MatrixXd Jacob(func.size(), arg.size());
	for(int i=0; i<func.size(); i++) 
	{
		clearAdjoints(); 
		func(i).grad();
		for(int j=0; j<arg.size(); j++) 
		{
			Jacob(i, j)=arg(j).adj();
		}
	}
	return Jacob;
}
void testGradients3()
{
	VectorXv arg(2); arg<<0.5, 1.0;
	MatrixXd Jacob=Jacobian(g(arg), arg);
	cout<<"Jacob="<<endl<<Jacob<<endl;
}
int main(int argc, char* argv[])
{
	testGradients1();
	testGradients2();
	testGradients3();
}