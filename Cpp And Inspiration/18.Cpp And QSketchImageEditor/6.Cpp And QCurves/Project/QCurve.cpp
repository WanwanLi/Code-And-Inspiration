#include "QCurve.h"
#include <QDebug>
#define dot vec2::dotProduct

void QCurve::operator=(QPoint point)
{
	this->point2D<<vec2(point);
}
void QCurve::operator<<(QPoint point)
{
	this->point2D<<vec2(point);
}
void QCurve::operator<<=(QPoint point)
{
	this->point2D<<vec2(point);
	this->polygonize(); this->split();
	this->linearize();
}
void QCurve::polygonize()
{
	this->polygon.clear(); this->polygon<<0; 
	for(int i=0, k, e=maxError; i<point2D.size()-1; i++)
	{
		for(int j=i+1; j<point2D.size(); j++)
		if(!isLinearBetween(i, j, k, e))break;
		i=k; this->polygon<<i;
	}
	qDebug()<<point2D.size();
	qDebug()<<polygon;
}
void QCurve::split()
{
	this->curves.clear();
	for(int i=0; i<polygon.size()-1; i++)
	{
		int start=polygon[i], end=polygon[i+1];
		QCubic curve=QCubic(point2D.mid(start, end-start));
		if(curve.minimizeFittingError(error))this->curves<<curve;
		else{QVector<QCubic> split; curve.split(split, error); this->curves<<split;}
	}
}
bool QCurve::isLinearBetween(int beginIndex, int endIndex, int& middleIndex, qreal fittingError)
{
	middleIndex=endIndex;
	vec2 begin=point2D[beginIndex];
	vec2 end=point2D[endIndex]; 
	qreal distance=begin.distanceToPoint(end);
	if(distance<minLineLength)return true;
	qreal error=0.0, maxDistance=0.0;
	vec2 direction=(end-begin).normalized();
	for(int i=beginIndex+1; i<endIndex; i++)
	{
		distance=point2D[i].distanceToLine(begin, direction);
		if(distance>maxDistance)
		{
			maxDistance=distance; middleIndex=i;
		}
		error+=distance;
	}
	return error/(endIndex-beginIndex)<fittingError;
}
void QCurve::drawPolygon(QPainter& painter)
{
	for(int i=0; i<polygon.size()-1; i++)
	{
		vec2 p0=point2D[polygon[i+0]];
		vec2 p1=point2D[polygon[i+1]];
		vec2 p2=(p0+p1)/2+vec2(0, 10);
		painter.drawLine(p0.x(), p0.y(), p1.x(), p1.y());
		painter.drawText(p2.x(), p2.y(), isLinear[i]?"L":"C");
	}
}
void QCurve::drawControlPoints(QPainter& painter)
{
	QPainterPath path;
	for(int i=0; i<curves.size(); i++)
	{
		vec2* p=curves[i].ctrlPoints;
		if(i==0)path.moveTo(p[0].x(), p[0].y());
		path.cubicTo
		(
			p[1].x(), p[1].y(), 
			p[2].x(), p[2].y(),
			p[3].x(), p[3].y()
		);
		painter.drawLine(p[0].x(), p[0].y(), p[1].x(), p[1].y());
		painter.drawLine(p[2].x(), p[2].y(), p[3].x(), p[3].y());
	}
	painter.drawPath(path);
}
void QCurve::linearize()
{
	this->isLinear.clear();
	for(int i=0, e=minError; i<polygon.size()-1; i++)
	{
		int i0=polygon[i+0], i1=polygon[i+1], k;
		this->isLinear<<isLinearBetween(i0, i1, k, e);
	}
}
void QCurve::clear()
{
	this->curves.clear();
	this->polygon.clear();
	this->point2D.clear();
}
QCubic::QCubic(QVector<vec2> curvePoints)
{
	this->curvePoints=curvePoints;
	this->leftTangent=tangent(0, tangentSize);
	this->rightTangent=tangent(size()-1, size()-1-tangentSize);
	this->getChordLengthParameters(); this->getCtrlPoints();
}
vec2 QCubic::direction(int startIndex, int endIndex)
{
	return (curvePoints[endIndex]-curvePoints[startIndex]).normalized();
}
vec2 QCubic::tangent(int startIndex, int endIndex)
{
	vec2 direction(0, 0);
	if(endIndex>startIndex)
	{
		for(int i=startIndex+1; i<size()&&i<endIndex; i++)
		direction+=this->direction(startIndex, i);
	}
	else if(startIndex>endIndex)
	{
		for(int i=startIndex-1; i>=0&&i>endIndex; i--)
		direction-=this->direction(startIndex, i);
	}
	if(direction.length()==0)return vec2(1, 0);
	return direction.normalized();
}

void QCubic::split(QVector<QCubic>& curves, qreal error)
{
	if(curves.size()<=tangentSize){curves<<(*this); return;}
	QCubic subcurve=QCubic(curvePoints.mid(0, size()/2));
	if(subcurve.minimizeFittingError(error))curves<<subcurve;
	else {QVector<QCubic> left; split(left, error); curves<<left;}
	subcurve=QCubic(curvePoints.mid(size()/2, size()-size()/2));
	if(subcurve.minimizeFittingError(error))curves<<subcurve;
	else {QVector<QCubic> right; split(right, error); curves<<right;}
}
void QCubic::getCtrlPoints()
{
	#define u params
	#define P curvePoints
	vec2 (*A)[2]=new QVector2D[size()][2];
	vec2 V0, V1, V2, V3, V00, V01, V32, V33;
	V0=P[0]; V3=P[size()-1]; qreal C[2][2], X[2];
	C[0][0]=C[0][1]=C[1][0]=C[1][1]=X[0]=X[1]=0;
	for(int i=0; i<size(); i++)
	{
		A[i][0]=leftTangent*B1(u[i]);
		A[i][1]=rightTangent*B2(u[i]);
		C[0][0]+=dot(A[i][0], A[i][0]);
		C[0][1]+=dot(A[i][0], A[i][1]);
		C[1][0]+=dot(A[i][1], A[i][0]);
		C[1][1]+=dot(A[i][1], A[i][1]);
		V00=V0*B0(u[i]); V01=V0*B1(u[i]); 
		V32=V3*B2(u[i]); V33=V3*B3(u[i]); 
		X[0]+=dot(A[i][0], P[i]-(V00+V01+V32+V33));
		X[1]+=dot(A[i][1], P[i]-(V00+V01+V32+V33));
	}
	qreal detC01=C[0][0]*C[1][1]-C[1][0]*C[0][1];
	qreal detC0X=C[0][0]*X[1]-C[1][0]*X[0];
	qreal detXC1=X[0]*C[1][1]-X[1]*C[0][1];
	qreal a1=(detC01==0)?0:detXC1/detC01;
	qreal a2=(detC01==0)?0:detC0X/detC01;
	V1=V0+leftTangent*a1; V2=V3+rightTangent*a2;  	
	this->ctrlPoints[0]=V0; this->ctrlPoints[1]=V1;
	this->ctrlPoints[2]=V2; this->ctrlPoints[3]=V3;
	#undef u
	#undef P
}
bool QCubic::minimizeFittingError(qreal error)
{
	for(int i=0; i<maxIterations; i++)
	{
	   	this->optimizeParameters();
		this->getCtrlPoints();
		qreal fittingError=getFittingError();
		if(fittingError<error)return true;
	}
	return false;
}
int QCubic::size()
{
	return curvePoints.size();
}
qreal QCubic::getFittingError()
{
	qreal error=0.0;
	for(int i=0; i<size(); i++)
	{
		error+=pointAt(params[i]).distanceToPoint(curvePoints[i]);
	}
	return error/length();
}
qreal QCubic::length()
{
	qreal curveLength=0;
	for(int i=0; i<size()-1; i++)
	{
		curveLength+=curvePoints[i].distanceToPoint(curvePoints[i+1]);
	}
	return curveLength;
}
void QCubic::getChordLengthParameters()
{
	this->params.clear(); this->params<<0;
	for(int i=1; i<size(); i++)
	{
		qreal distance=curvePoints[i-1].distanceToPoint(curvePoints[i]);
		this->params<<params[i-1]+distance;
	}
	for(int i=1; i<size(); i++)params[i]/=params[size()-1];
}
void QCubic::optimizeParameters()
{
	for(int i=0; i<size(); i++)
	{
		this->params[i]=optimize(curvePoints[i], params[i]);
	}
}
qreal QCubic::optimize(vec2 curvePoint, qreal param)
{
	#define u param
	#define C ctrlPoints
	#define P curvePoint
	vec2 Q, Q1, Q2, C1[3], C2[2];
	for(int i=0; i<=2; C1[i]=3*(C[i+1]-C[i]), i++);
	for(int i=0; i<=1; C2[i]=2*(C1[i+1]-C1[i]), i++);
	Q=pointAt(u); Q1=pointAt(C1, u, 2); Q2=pointAt(C2, u, 1);
	return u-dot(Q-P, Q1)/(dot(Q1, Q1)+dot(Q-P, Q2));
	#undef u
	#undef C
	#undef P
}
vec2 QCubic::pointAt(vec2 ctrlPoints[], qreal param, int degree)
{
	#define t param
	#define P ctrlPoints
	#define B bezierPoints
	for(int i=0; i<=degree; B[i]=P[i], i++);
	for(int i=1; i<=degree; i++) 
	{	
		for(int j=0; j<=degree-i; j++)
		{
	    		B[j]=(1.0-t)*B[j]+t*B[j+1];
		}
	}
	return B[0];
	#undef t
	#undef P
	#undef B
}
vec2 QCubic::pointAt(qreal param)
{
	return pointAt(ctrlPoints, param, 3);
}
qreal QCubic::B0(qreal u)
{
	return (1.0-u)*(1.0-u)*(1.0-u);
}
qreal QCubic::B1(qreal u)
{
	return 3*u*(1.0-u)*(1.0-u);
}
qreal QCubic::B2(qreal u)
{
	return  3*u*u*(1.0-u);
}
qreal QCubic::B3(qreal u)
{
	return u*u*u;
}
