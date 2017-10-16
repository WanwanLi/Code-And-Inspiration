#include <QFile>
#include "QEnergy.h"
#include <QTextStream>

var QEnergy::parallelEnergy(const Vector4v& plane1, const Vector4v& plane2)
{
	var dot=stanMath.normalizedPlane(plane1).head(3).
	dot(stanMath.normalizedPlane(plane2).head(3));
	return 1-dot*dot;
}
var QEnergy::perpendicularEnergy(const Vector4v& plane1, const Vector4v& plane2)
{
	return 1-parallelEnergy(plane1, plane2);
}
var QEnergy::collinearEnergy(const MatrixXv& curve, const Vector4v& plane, int start, int end)
{
	MatrixXv subcurve=stanMath.subCurve(curve, start ,end);
	MatrixXv projectedcurve=stanMath.projectedCurve(subcurve, plane);
	return stanMath.distanceSquareBetween(subcurve, projectedcurve);
}
var QEnergy::totalEnergy(const VectorXi& sketch, const VectorXi& joint, const VectorXv& plane)
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
Vector3v QEnergy::sketchPoint(Vector3i point, const MatrixXv& planes)
{
	var x=point(0)*1.0/sketchWidth;
	var y=point(1)*1.0/sketchHeight;
	return stanMath.intersectPlane
	(
		Vector3v((x-0.5)*2, (y-0.5)*2, 0), 
		viewDirection.head(3), 
		planes.col(point(2))
	);
}
MatrixXv QEnergy::sketchCurves(const MatrixXi& sketch, const MatrixXv& planes)
{
	MatrixXv curves(3, sketch.cols());
	for(int i=0; i<curves.cols(); i++)
	{
		curves.col(i)=sketchPoint(sketch.col(i), planes);
	}
	return curves;
}
MatrixXd QEnergy::sketchPlanes(VectorXd plane)
{
	Map<MatrixXd> planes(plane.data(), 4, plane.size()/4);
	MatrixXd sketch(4, planes.cols());
	QStanMath<double> math;
	for(int i=0; i<sketch.cols(); i++)
	{
		sketch.col(i)=math.normalizedPlane(planes.col(i));
	}
	return sketch;
}
int QEnergy::getJointType(QString s)
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
bool QEnergy::loadSketchFile(QString fileName)
{
	vector<double> plane;
	vector<int> joint, sketch;
	QFile file(fileName); QStringList line;
	if(!file.open(QIODevice::ReadOnly))return false;	
	QTextStream textStream(&file);
	bool isNotValidSketch=true;
	while(!textStream.atEnd())
	{
		line=textStream.readLine().split(" ");
		if(line.size()<3)continue;
		if(line[0]=="s")
		{
			sketchWidth=line[1].toInt();
			sketchHeight=line[2].toInt();
			isNotValidSketch=false;
		}
		if(isNotValidSketch)continue;
		if(line[0]=="m")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			int z=line[3].toInt();
			sketch.push_back(x);
			sketch.push_back(y);
			sketch.push_back(z);
		}
		else if(line[0]=="l")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			int z=line[3].toInt();
			sketch.push_back(x);
			sketch.push_back(y);
			sketch.push_back(z);
		}
		else if(line[0]=="c")
		{
			int c1=line[1].toInt();
			int c2=line[2].toInt();
			int c3=line[3].toInt();
			int c4=line[4].toInt();
			int c5=line[5].toInt();
			int c6=line[6].toInt();
			int c7=line[7].toInt();
			int c8=line[8].toInt();
			int c9=line[9].toInt();
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
		else if(line[0]=="p")
		{
			double d1=line[1].toDouble();
			double d2=line[2].toDouble();
			double d3=line[3].toDouble();
			double d4=line[4].toDouble();
			plane.push_back(d1);
			plane.push_back(d2);
			plane.push_back(d3);
			plane.push_back(d4);
		}
		else
		{
			int t0=getJointType(line[0]);
			if(t0==0)continue;
			int t1=line[1].toInt();
			int t2=line[2].toInt();
			int t3=line[3].toInt();
			int t4=line[4].toInt();
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
