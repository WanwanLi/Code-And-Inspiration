#include <stan/math.hpp>
#include <Eigen/Dense>
#include <LBFGS.h>
#include <fstream>
using namespace std;
using namespace Eigen;
using namespace LBFGSpp;
using namespace stan::math;
typedef Matrix<var, 3, 1> Vector3v;
typedef Matrix<var, 4, 1> Vector4v;
typedef Matrix<var, Dynamic, 1> VectorXv;
typedef Matrix<var, Dynamic, Dynamic> MatrixXv;

var Gauss(var d)						//Gauss Filter: used to smooth the energy term
{
	var t=0.3, e=0.01, r=2*t*t;
	return exp(-d*d/r)+e;
}
#define Vector4t Matrix<T, 4, 1>
template <typename T>  
Vector4t normalized(const Vector4t& plane)
{			
	return plane/plane.head(3).norm();
}
Vector4v normalizedPlane(var A, var B, var C, var D)		//Get the plane normalized
{
	Vector4v plane(A, B, C, D);				//Plane equation: Ax+By+Cz+D=0
	return plane/plane.head(3).norm();			//Devide A, B, C, D by sqrt(A2+B2+C2);
}
Vector3v centerPoint(const Vector4v& plane)
{
	var A=plane(0), B=plane(1), C=plane(2), D=plane(3), E=1e-5; Vector3v P(0, 0, 0);
	if(abs(A)>E)P(0)=-D/A; else if(abs(B)>E)P(1)=-D/B; else P(2)=-D/C; return P;
							//Find a center point P(x, y, z) on plane :
							//	P(-D/A, 0, 0)	when |A|>0
							//	P(0, -D/B, 0)	when |B|>0
							//	P(0, 0, -D/C)	when |C|>0
}
Vector3v intersectPlane(const Vector3v& origin, const Vector3v& direction, const Vector4v& plane)
{
	Vector3v normal=plane.head(3);			//Normal of a Plane (A, B, C, D) is (A, B, C)
	if(normal.dot(direction)==0)return origin;		//Intersection point is not existing
	Vector3v center=centerPoint(plane);			//Find a center point P(x, y, z) on plane
	Vector3v connection=center-origin;			//Connect the origin point C of a ray with P
	var distance=normal.dot(connection)/normal.dot(direction);
							//Find the distance between C&P along ray.dir
	return origin+distance*direction;			//Find the intersection point with a ray and plane
}
Vector3v projectedPoint(const Vector3v& point, const Vector4v& plane)
{
	return intersectPlane(point, -plane.head(3), plane);
							//project a point C onto plane (n, D) is as same as 
							//intersect a ray from C along -n with plane  (n, D)
}
MatrixXv subCurve(const MatrixXv& curve, unsigned int start, unsigned int end)
{
	MatrixXv subcurve(3, end-start+1);
	for(int i=0; i<subcurve.cols(); i++)
	{
		subcurve.col(i)=curve.col(start+i);
	}
	return subcurve;
}
MatrixXv projectedCurve(const MatrixXv& curve, const Vector4v& plane)	//project a 3D curve onto a given plane by projecting 
{
	MatrixXv projectCurve(3, curve.cols());				//each 3D point of the 3D curve onto a given plane 
	for(int i=0; i<curve.cols(); i++)
	{
		projectCurve.col(i)=projectedPoint(curve.col(i), plane);
	}
	return projectCurve;
}
var integrateDistanceSquare(const Vector3v& P0, const Vector3v& P1, const Vector3v& Q0, const Vector3v& Q1)
{
	Vector3v u=P1-P0, v=Q1-Q0, C=P0-Q0, k=u-v;		
	return C.dot(C)+k.dot(k)/3+C.dot(k);			//Let u=P1-P0, v=Q1-Q0
							//Let P(t)=P0+ut, t is [0, 1]
							//Let Q(t)=Q0+vt, v is [0, 1]
							//Let f(t)=[P(t)-P(t)]^2, is the distance^2
							//So f(t)=[P0-Q0+(u-v)t]^2, let C=P0-Q0, k=u-v;
							//So f(t)=[C+kt]^2=(C+kt).(C+kt), after expand:
							//So f(t)=C.C+k.kt^2+2C.kt, integrate f(t:[0, 1])
							//So Int[f(t)]=C.Ct+k.kt^3/3+C.kt^2, Newton's law:
							//So Int[f(t:[0, 1])]=Int[f(1)]-Int[f(0)]=C.C+k.k/3+C.k
}
var distanceSquareBetween(MatrixXv& curve1, MatrixXv& curve2)
{
	var distanceSquare=0;
	for(int i=0; i<curve1.cols()-1; i++)			//accumulate the distance square foreach line segment
	{
		distanceSquare+=integrateDistanceSquare
		(
			curve1.col(i+0), curve1.col(i+1), 
			curve2.col(i+0), curve2.col(i+1) 
		);
	}
	return distanceSquare;
}
var collinearEnergy(const MatrixXv& curve, const Vector4v& plane, unsigned int start, unsigned int end)
{
	MatrixXv subcurve=subCurve(curve, start ,end);
	MatrixXv projectedcurve=projectedCurve(subcurve, plane);
	return distanceSquareBetween(subcurve, projectedcurve);
}
var parallelEnergy(const Vector4v& plane1, const Vector4v& plane2)		//Check two planes are parallel to each other or not
{
	var dot=normalized(plane1).head(3).
	dot(normalized(plane2).head(3));
	return 1-dot*dot;
}
var perpendicularEnergy(const Vector4v& plane1, const Vector4v& plane2)		//Check two planes are perpendicular to each other or not
{
	return 1-parallelEnergy(plane1, plane2);
}
var accuracyEnergy(MatrixXv& curve1, MatrixXv& curve2)			//Check the accuracy between 2D sketch and 3D curve
{
	var difference=0;
	for(int i=0; i<curve1.rows(); i++)
	{
		var dx=curve1(i, 0)-curve2(i, 0);
		var dy=curve1(i, 1)-curve2(i, 1);
		difference+=sqrt(dx*dx+dy*dy);
	}
	return difference;
}
var foreshorteningEnergy(MatrixXv& curve1, MatrixXv& curve2)
{
	var difference=0;
	for(int i=0; i<curve1.rows()-1; i++)
	{
		var dx=curve2(i+0, 0)-curve2(i+1, 0);
		var dy=curve2(i+0, 1)-curve2(i+1, 1);
		var dz=curve1(i+0, 2)-curve1(i+1, 2);
		var dr=sqrt(dx*dx+dy*dy);
		difference+=Gauss(dr)*dz*dz;
	}
	return difference;
}
var variationEnergy(MatrixXv& curve1, MatrixXv& curve2)
{
	var difference=0;
	for(int i=0; i<curve1.rows()-1; i++)
	{
		var dx1=curve1(i+0, 0)-curve1(i+1, 0);
		var dy1=curve1(i+0, 1)-curve1(i+1, 1);
		var dx2=curve2(i+0, 0)-curve2(i+1, 0);
		var dy2=curve2(i+0, 1)-curve2(i+1, 1);
		var dr1=sqrt(dx1*dx1+dy1*dy1);
		var dr2=sqrt(dx2*dx2+dy2*dy2);
		difference+=Gauss(dr2)*(dr1-dr2)*(dr1-dr2);
	}
	return difference;
}
int sketchWidth=0;
int sketchHeight=0;
const int jointDim=5;
const int curveEnd=-1;
enum 
{
	PARALLEL=1, 
	COLLINEAR=2, 
	PERPENDICULAR,
	FORESHORTENING,
	GROUND_PARALLEL,
	GROUND_COLLINEAR,
	GROUND_PERPENDICULAR
};
const Vector4v groundPlane(0, 1, 0, 0);
const Vector4v viewDirection(0, 0, -1, 0);
Vector3v sketchPoint(Vector3i point, const MatrixXv& planes)
{
	var x=point(0)*1.0/sketchWidth;
	var y=point(1)*1.0/sketchHeight;
	return intersectPlane
	(
		Vector3v((x-0.5)*2, (y-0.5)*2, 0), 
		viewDirection.head(3), 
		planes.col(point(2))
	);
}
MatrixXv sketchCurves(const MatrixXi& sketch, const MatrixXv& planes)
{
	MatrixXv curves(3, sketch.cols());
	for(int i=0; i<curves.cols(); i++)
	{
		curves.col(i)=sketchPoint(sketch.col(i), planes);
	}
	return curves;
}
MatrixXd sketchPlanes(VectorXd plane)
{
	Map<MatrixXd> planes(plane.data(), 4, plane.size()/4);
	MatrixXd sketch(4, planes.cols());
	for(int i=0; i<sketch.cols(); i++)
	{
		Vector4d col=planes.col(i);
		sketch.col(i)=normalized(col);
	}
	return sketch;
}
unsigned int getJointType(string s)
{
	if(s=="PAR")return PARALLEL;
	else if(s=="COL")return COLLINEAR;
	else if(s=="PER")return PERPENDICULAR;
	else if(s=="FOR")return FORESHORTENING;
	else if(s=="GPAR")return GROUND_PARALLEL;
	else if(s=="GCOL")return GROUND_COLLINEAR;
	else if(s=="GPER")return GROUND_PERPENDICULAR;
	else return 0;
}
var totalEnergy(const VectorXi& sketch, const VectorXi& joint, const VectorXv& plane)
{
	Map<const MatrixXi> joints(joint.data(), jointDim, joint.size()/jointDim);
	Map<const MatrixXi> sketches(sketch.data(), 3, sketch.size()/3);
	Map<const MatrixXv> planes(plane.data(), 4, plane.size()/4);
	MatrixXv curves=sketchCurves(sketches, planes);
	var energy=0;
	for(int i=0; i<joints.cols(); i++)
	{
		VectorXi t=joints.col(i);
		switch(t(0))
		{
			case PARALLEL: energy+=parallelEnergy(planes.col(t(1)), planes.col(t(2))); break;
			case COLLINEAR: energy+=collinearEnergy(curves, planes.col(t(1)), t(2), t(3)); break;
			case PERPENDICULAR: energy+=perpendicularEnergy(planes.col(t(1)), planes.col(t(2))); break;
			case FORESHORTENING: energy+=parallelEnergy(planes.col(t(1)), viewDirection); break;
			case GROUND_PARALLEL: energy+=parallelEnergy(planes.col(t(1)), groundPlane); break;
			case GROUND_COLLINEAR: energy+=collinearEnergy(curves, groundPlane, t(1), t(2)); break;
			case GROUND_PERPENDICULAR: energy+=perpendicularEnergy(planes.col(t(1)), groundPlane); break;
		}
	}
	return energy;
}
vector<string> split(string str)
{
	string s;
	vector<string> v;
	istringstream t(str);
	while(t>>s)v.push_back(s);
	return v;
}
bool loadSketch(string fileName, VectorXi& sketchVector, VectorXi& jointVector, VectorXd& planeVector)
{
	string line;
	vector<int> joint;
	vector<int> sketch;
	vector<double> plane;
	ifstream file(fileName);
	if(!file.is_open())return false;
	bool isNotValidSketch=true;
	while(getline(file, line))
	{
		vector<string> v=split(line);
		if(v.size()<3)continue;
		if(v[0]=="s")
		{
			sketchWidth=atoi(v[1].c_str());
			sketchHeight=atoi(v[2].c_str());
			isNotValidSketch=false;
		}
		if(isNotValidSketch)continue;
		if(v[0]=="m")
		{
			int x=atoi(v[1].c_str());
			int y=atoi(v[2].c_str());
			int z=atoi(v[3].c_str());
			sketch.push_back(x);
			sketch.push_back(y);
			sketch.push_back(z);
		}
		else if(v[0]=="l")
		{
			int x=atoi(v[1].c_str());
			int y=atoi(v[2].c_str());
			int z=atoi(v[3].c_str());
			sketch.push_back(x);
			sketch.push_back(y);
			sketch.push_back(z);
		}
		else if(v[0]=="c")
		{
			int c1=atoi(v[1].c_str());
			int c2=atoi(v[2].c_str());
			int c3=atoi(v[3].c_str());
			int c4=atoi(v[4].c_str());
			int c5=atoi(v[5].c_str());
			int c6=atoi(v[6].c_str());
			int c7=atoi(v[7].c_str());
			int c8=atoi(v[8].c_str());
			int c9=atoi(v[9].c_str());
			sketch.push_back(c1);
			sketch.push_back(c2);
			sketch.push_back(c3);
			sketch.push_back(c4);
			sketch.push_back(c5);
			sketch.push_back(c6);
			sketch.push_back(c7);
			sketch.push_back(c8);
			sketch.push_back(c9);
		}
		else if(v[0]=="p")
		{
			double d1=atof(v[1].c_str());
			double d2=atof(v[2].c_str());
			double d3=atof(v[3].c_str());
			double d4=atof(v[4].c_str());
			plane.push_back(d1);
			plane.push_back(d2);
			plane.push_back(d3);
			plane.push_back(d4);
		}
		else
		{
			int t0=getJointType(v[0]);
			if(t0==0)continue;
			int t1=atoi(v[1].c_str());
			int t2=atoi(v[2].c_str());
			int t3=atoi(v[3].c_str());
			int t4=atoi(v[4].c_str());
			joint.push_back(t0);
			joint.push_back(t1);
			joint.push_back(t2);
			joint.push_back(t3);
			joint.push_back(t4);
		}
	}
	file.close();
	if(!isNotValidSketch)
	{
		sketchVector=Map<VectorXi>(&sketch[0], sketch.size());
		planeVector=Map<VectorXd>(&plane[0], plane.size());
		jointVector=Map<VectorXi>(&joint[0], joint.size());
	}
	return isNotValidSketch?false:true;
}
struct Energy
{
	const VectorXi sketch; const VectorXi joint;
	Energy(const VectorXi& s, const VectorXi& t):sketch(s), joint(t){}
	double operator()(const VectorXd& plane, VectorXd& grad)
	{
		double value;
		auto energy=[&](const VectorXv& plane) 
		{ 
			return totalEnergy(sketch, joint, plane); 
		};
		gradient(energy, plane, value, grad);
		return value;
	}
};
int main(int argc, char* argv[]) 
{
	if(argc<2)
	{
		cout<<"Please use cmd: energy <fileName>"<<endl;
		return 0;
	}
	string fileName=argv[1];
	VectorXd x; VectorXi sketch, joint;
	if(!loadSketch(fileName, sketch, joint, x))
	{
		cout<<"Error: in load file: "<<fileName<<endl;
		return 0;
	}
	LBFGSParam<double> param;
	param.epsilon=1e-6; double fx;
	param.max_iterations=100;
	LBFGSSolver<double> solver(param);
	Energy f(sketch, joint);
	int t=solver.minimize(f, x, fx);
	cout<<t<<" iterations"<<endl;
	cout<<"f(x)="<<fx<<endl;
	cout<<"x="<<endl<<sketchPlanes(x)<<endl;
	return 0;
}
