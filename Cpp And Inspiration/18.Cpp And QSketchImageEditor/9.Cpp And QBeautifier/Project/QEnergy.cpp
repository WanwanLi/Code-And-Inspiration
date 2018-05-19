#include <QDebug>
#include "QEnergy.h"
#include "QAnalyzer.h"

QEnergy::QEnergy(QVector<vec2> point2D, veci regularity)
{
	vec variables;
	for(vec2 point : point2D)
	variables<<point.x()<<point.y();
	this->point2D=point2D;
	this->regularity=regularity;
	this->variables=toVectorXd(variables);
}
var QEnergy::distanceBetweenDirections(Vector2v dir1, Vector2v dir2)
{
	var dot=dir1.normalized().dot(dir2.normalized()); return 1-dot*dot;
}
var QEnergy::verticalEnergy(Vector2v start, Vector2v end)
{
	return distanceBetweenDirections(end-start, Vector2v(0, 1));
}
var QEnergy::horizontalEnerg(Vector2v start, Vector2v end)
{
	return distanceBetweenDirections(end-start, Vector2v(1, 0));
}
var QEnergy::parallelEnergy(Vector2v start1, Vector2v end1, Vector2v start2, Vector2v end2)
{
	return distanceBetweenDirections(end1-start1, end2-start2);
}
var QEnergy::perpendicularEnergy(Vector2v prev, Vector2v center, Vector2v next)
{
	return 1-distanceBetweenDirections(prev-center, next-center);
}
var QEnergy::weight(int type)
{
	#define R QAnalyzer
	switch(type)
	{
		case R::VERTICAL: return 1;
		case R::PARALLEL: return 2;
		case R::HORIZONTAL: return 1;
	}
	return 1;
	#undef R
}
var QEnergy::totalEnergy(VectorXv variables)
{
	var energy=0;
	for(int i=0; i<regularity.size(); i++)
	{
		#define R QAnalyzer
		#define t(x) regularity[i+x]
		#define pointOf(x) variables.segment((x)*2, 2)
		switch(t(0))
		{
			case R::VERTICAL: energy+=weight(t(0))*verticalEnergy(pointOf(t(1)), pointOf(t(2))); break;
			case R::HORIZONTAL: energy+=weight(t(0))*horizontalEnerg(pointOf(t(1)), pointOf(t(2))); break;
			case R::PARALLEL: energy+=weight(t(0))*parallelEnergy(pointOf(t(1)), pointOf(t(2)), pointOf(t(3)), pointOf(t(4))); break;
			case R::PERPENDICULAR: energy+=weight(t(0))*perpendicularEnergy(pointOf(t(1)), pointOf(t(2)), pointOf(t(3))); break;
		}
		i+=R::count(t(0));
		#undef t(x)
		#undef R
	}
	return energy;
}
VectorXd QEnergy::toVectorXd(vec vector)
{
	std::vector<double> stdVector=vector.toStdVector();
	return Map<VectorXd>(stdVector.data(), stdVector.size());
}
vec QEnergy::toVector(VectorXd vector)
{
	vec result; for(int i=0; i<vector.size(); i++)result<<vector[i]; return result;
}
