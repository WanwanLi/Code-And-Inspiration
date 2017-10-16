#include <LBFGS.h>
#include <QDebug>
#include <QPainter>
#include "QRelation.h"
#include "QOptimizer.h"
using namespace LBFGSpp;

struct Energy
{
	QEnergy& energy; const VectorXi sketch; const VectorXi joint;
	Energy(QEnergy& e, const VectorXi& s, const VectorXi& t):energy(e), sketch(s), joint(t){}
	double operator()(const VectorXd& plane, VectorXd& grad)
	{
		double value;
		auto totalEnergy=[&](const VectorXv& plane) 
		{ 
			return energy.totalEnergy(sketch, joint, plane); 
		};
		gradient(totalEnergy, plane, value, grad);
		return value;
	}
};
bool QOptimizer::loadSketchFile(QString fileName)
{
	if(!energy.loadSketchFile(fileName))return false;
	VectorXd x=energy.planeVector; 
	VectorXi sketch=energy.sketchVector;
	Map<MatrixXi> sketches(sketch.data(), 3, sketch.size()/3);
	this->sketchPlanes=energy.sketchPlanes(energy.planeVector); 
	this->sketchCurves=toMatrixXd(energy.sketchCurves(sketches, toMatrixXv(x)));
	this->markerPoints.clear(); this->markerLines.clear(); 
	this->matchPoints.clear(); this->matchCurves.clear();
	this->iterations=0; this->minEnergy=0; return true;
}
void QOptimizer::minimizeEnergy()
{
	energy.resetViewInfo();
	VectorXd x=energy.planeVector; 
	VectorXi sketch=energy.sketchVector;
	VectorXi joint=energy.jointVector;
	LBFGSParam<double> param;
	param.epsilon=epsilon; double fx;
	param.max_iterations=maxIterations;
	LBFGSSolver<double> solver(param);
	Energy f(energy, sketch, joint);
	this->iterations=solver.minimize(f, x, fx);
	qDebug()<<"it: "<<iterations;
	Map<MatrixXi> sketches(sketch.data(), 3, sketch.size()/3);
	this->sketchCurves=toMatrixXd(energy.sketchCurves(sketches, toMatrixXv(x)));
	this->sketchPlanes=energy.sketchPlanes(x);
	this->minEnergy=fx; 
	qDebug()<<"minE: "<<minEnergy;
}
void QOptimizer::perturbPlanes(qreal noise)
{
	energy.perturbPlanes(noise);
	VectorXd x=energy.planeVector; 
	VectorXi sketch=energy.sketchVector;
	Map<MatrixXi> sketches(sketch.data(), 3, sketch.size()/3);
	this->sketchPlanes=energy.sketchPlanes(energy.planeVector); 
	this->sketchCurves=toMatrixXd(energy.sketchCurves(sketches, toMatrixXv(x)));
	this->iterations=0; this->minEnergy=0; 
}
void QOptimizer::drawMarkers(QPainter& painter)
{
	for(int i=0; i<markerLines.size(); i+=4)
	{
		int x0=markerLines[i+0];
		int y0=markerLines[i+1];
		int x1=markerLines[i+2];
		int y1=markerLines[i+3];
		painter.drawLine(x0, y0, x1, y1);
	}
	for(int i=0; i<markerPoints.size(); i+=2)
	{
		int x=markerPoints[i+0];
		int y=markerPoints[i+1];
		painter.drawEllipse(QPoint(x, y), 2, 2);
	}
}
void QOptimizer::drawMatchPoints(QPainter& painter, int& index)
{
	if(matchPoints.size()==0)return;
	if(index<0)index+=matchPoints.size();
	else index%=matchPoints.size();
	array points=matchPoints[index];
	for(int i=0; i<points.size()/2; i++)
	{
		int x=points[i*2+0], y=points[i*2+1];
		painter.drawEllipse(QPoint(x, y), 2, 2);
	}
	array curve=matchCurves[index];
	for(int i=0; i<curve.size()/2-1; i++)
	{
		int x0=curve[(i+0)*2+0], y0=curve[(i+0)*2+1];
		int x1=curve[(i+1)*2+0], y1=curve[(i+1)*2+1];
		painter.drawLine(x0, y0, x1, y1);
	}
}
void QOptimizer::createRelations()
{
	array path=energy.toArray(energy.pathVector);
	array sketch=energy.toArray(energy.sketchVector);
	vec2 x=energy.canvasDirection(1, 0, 0);
	vec2 y=energy.canvasDirection(0, 1, 0);
	vec2 z=energy.canvasDirection(0, 0, 1);
	QRelation relation(path, sketch, x, y, z);
	QPainterPath* painterPath=new QPainterPath();
	relation.create(); //relation.debugAll(); relation.debug(); 
	relation.debugSketchCurves(); //relation.debugCurvesPoints();
	this->markerPoints=relation.markerPoints;
	this->markerLines=relation.markerLines;
	this->matchCurves=relation.matchCurves;
	this->matchPoints=relation.matchPoints;
}
MatrixXv QOptimizer::toMatrixXv(const VectorXd & vectorXd)
{
	Map<const MatrixXd> matrixXd(vectorXd.data(), 4, vectorXd.size()/4);
	MatrixXv matrixXv(matrixXd.rows(), matrixXd.cols());
	for(int i=0; i<matrixXv.rows(); i++)
	{
		for(int j=0; j<matrixXv.cols(); j++)
		{
			matrixXv(i, j)=matrixXd(i, j);
		}
	}
	return matrixXv;
}
MatrixXd QOptimizer::toMatrixXd(const MatrixXv& matrixXv)
{
	MatrixXd matrixXd(matrixXv.rows(), matrixXv.cols());
	for(int i=0; i<matrixXd.rows(); i++)
	{
		for(int j=0; j<matrixXd.cols(); j++)
		{
			matrixXd(i, j)=matrixXv(i, j).val();
		}
	}
	return matrixXd;
}


