#include <iostream>
#include <Eigen/Dense>
using namespace std;
using namespace Eigen;

void testMatrix1()
{
	MatrixXd m(2, 2);
	m(0, 0) = 3;
	m(1, 0) = 2.5;
	m(0, 1) = -1;
	m(1, 1) = m(1, 0) + m(0, 1);
	cout << "m="<< endl<<m << endl;
	m.resize(4, 3); cout<<"sizeof(m)="<<endl<<
	m.size()<<":("<< m.rows()<< ", " << m.cols()<<")\n";
}
void testMatrix2()
{
	#define m matrix1
	double array[4*3]=
	{
		1, 2, 3, 4, 
		5, 6, 7, 8, 
		9, 10, 11, 12
	};
	MatrixXd matrix1=Map<Matrix<double, 4, 3>>(array);
	cout<<"matrix1="<<endl<<matrix1<<endl; 
	Map<MatrixXd> matrix2(matrix1.data(), 3, 4);
	cout<<"matrix2="<<endl<<matrix2<<endl;
	vector<double> stdVector(m.data(), m.data()+m.cols()*m.rows());
	cout<<"stdVector="; for(double value : stdVector)cout<<value<<' '; 
	VectorXd vectorXd=Map<VectorXd>(stdVector.data(), stdVector.size());
	cout<<endl<<"vectorXd="<<endl<<vectorXd<<endl; 
}
void testMatrix3()
{
	Matrix3d m = Matrix3d::Random();
	cout << "rand(m)="<<endl<<m<<endl;
	m+=Matrix3d::Constant(1.2);
	cout << "m+1.2="<<endl<<m<<endl;
	Vector3d v(1, 2, 3);
	cout<< "v ="<<endl<<v<<endl;
	cout << "m*v ="<<endl<<m*v<<endl;
}
void testMatrix4()
{
	MatrixXcf m(2, 3); m<<1, 2, 3, 4, 5, 6;
	cout<<"m="<<endl<<m<<endl; m.transpose(); 
	cout<<"transpose(m)=\n"<<m<<endl; m.transposeInPlace(); 
	cout<<"transposeInPlace(m)=\n"<<m<<endl;
	m.resize(3, 3); m=MatrixXcf::Random(3, 3);
	cout<<"m="<< endl<<m<<endl;
	cout<<"adjoint(m)=\n"<<m.adjoint()<<endl; 
	cout<<"transpose(m)=\n"<<m.transpose()<<endl;
	cout<<"conjugate(m)=\n"<<m.conjugate()<<endl;
}
void testMatrix5()
{
	Vector3d u(1, 2, 3), v(0, 1, 2);
	cout<<"u * v="<<u.dot(v)<<endl;
	cout<<"u x v=\n"<<u.cross(v)<<endl;
	Matrix2d m; m<<1, 2, 3, 4; int i, j;
	cout<<"m=\n"<<m<<endl; int x, y;
	cout<<"sum(m)="<<m.sum()<<endl;
	cout<<"prod(m)="<<m.prod()<<endl;
	cout<<"trace(m)="<<m.trace()<<endl;
	cout<<"mean(m)="<<m.mean()<<endl;
	cout<<"minCoeff(m)="<<m.minCoeff(&i, &j)<<endl;
	cout<<"maxCoeff(m)="<<m.maxCoeff(&x, &y)<<endl;	
	cout<<"positionOfMinCoeff(m)="<<i<<", "<<j<<endl;
	cout<<"positionOfMaxCoeff(m)="<<x<<", "<<y<<endl;
}
void testMatrix6()
{
	ArrayXXd a(3, 3); 
	ArrayXXd b(3, 3); 
	a<<1, 2, 3, 4, 5, 6, 7, 8, 9;
	b<<1, 2, 3, 1, 2, 3, 1, 2, 3;
	cout<<"a="<<endl<<a<<endl;
	cout<<"b="<<endl<<b<<endl;
	MatrixXd m=(a+b); ArrayXXd d=m;
	cout<<"a+b="<<endl<<m<<endl;
	cout<<"a-2="<<endl<<a-2<<endl;
	cout<<"a*b="<<endl<<a+b<<endl<<endl;
	cout<<"a/b="<<endl<<a/b<<endl<<endl;
	ArrayXd c=ArrayXd::Random(5); c*=2;
	cout<<"c="<<endl<<c<<endl;
	cout<<"abs(c)="<<endl<<c.abs()<<endl;
	cout<<"sqrt(c)="<<endl<<c.sqrt()<<endl;
	cout<<"min(c, sqrt(c))="<<endl<<c.min(c.sqrt())<<endl;
}
void testMatrix7()
{
	MatrixXd m(4, 4); 
	m<<1, 2, 3, 4, 5, 6, 7, 8,
	9, 10, 11, 12, 13, 14, 15, 16;
	cout<<"m="<<endl<<m<<endl;
	cout<<"m.Block(1, 1, 2, 2)=\n"
	<<m.block(1, 1, 2, 2)<<endl;
	for(int i=1; i<=3; ++i)
	{
		cout<<"Block at <0,0> with size: "<<i<<"x"<<i<<endl;
		cout<<m.block(0, 0, i, i)<<endl<<endl;
	}
	cout<<"m.row(2)="<<m.row(2)<<endl<<endl;
	cout<<"m.col(3)="<<endl<<m.col(3)<<endl<<endl;
	cout<<"m.leftCols(2)="<<endl<<m.leftCols(2)<<endl<<endl;
	cout<<"m.bottomRows(2)="<<endl<<m.bottomRows(2)<<endl<<endl;
	m.topLeftCorner(2, 2)=m.bottomRightCorner(2, 2);
	cout<<"m.topLeft(2, 2)=m.bottomRigh(2, 2):"<<endl<<m<<endl;
	ArrayXd v(9); v<<1, 2, 3, 4, 5, 6, 7, 8, 9; cout<<"v="<<v<<endl;
	cout<<"v.segment(4, 3)=" <<endl<<v.segment(4, 3)<<endl;
	cout<<"v.head(3) =" <<endl<<v.head(3)<<endl;
	cout<<"v.tail(3)=" <<endl<<v.tail(3)<<endl;
}
int main()
{
	testMatrix1();
	testMatrix2();
	testMatrix3();
	testMatrix4();
	testMatrix5();
	testMatrix6();
	testMatrix7();
}
