#include "QStrokes.h"
#include "QVecMath.h"

qreal& QStrokes::operator[](int index)
{
	return this->path[index];
}
void QStrokes::operator=(QPoint point)
{
	QVector3D p=viewer.projectAt(point, planes[-1]);
	this->path<<MOVE<<p.x()<<p.y()<<p.z()<<planes.index;
	this->startPath(point, QVector2D(0, 0));
}
void QStrokes::operator+=(QPoint point)
{
	this->curve+=point;
	if(curve.length()<curve.minLength)this->setPath(LINE);
	else if(curve.isLinear())this->setPath(LINE);
	else if(curve.isCircular())this->setPath(CUBIC);
	else if(curve.isCubic())this->setPath(CUBIC);
	else 
	{ 
		this->curve--; 
		if(curve.isLinear())this->setPath(LINE);
		else if(curve.isCircular())this->setPath(CUBIC);
		else if(curve.isCubic())this->setPath(CUBIC);
		this->startPath(point, curve.direction());
	}
}
QStrokes& QStrokes::operator<<(qreal path)
{
	this->path<<path;
	return *this;
}
void QStrokes::operator<<=(const QVector<int>& strokes)
{
	for(int i=0; i<strokes.size(); i++)
	{
		this->index=size();
		if(strokes[i]==MOVE)
		{
			this->begin=next(begin);
			int x=strokes[++i], y=strokes[++i];
			QVector3D p=viewer.projectAt(QPoint(x, y), planes[-1]);
			this->path<<MOVE<<p.x()<<p.y()<<p.z()<<planes.index;
		}
		else if(strokes[i]==LINE)
		{
			int x=strokes[++i], y=strokes[++i];
			QVector3D p=viewer.projectAt(QPoint(x, y), planes[-1]);
			this->path<<LINE<<p.x()<<p.y()<<p.z()<<planes.index;
		}
		else if(strokes[i]==CUBIC)
		{
			this->path<<CUBIC;
			int x1=strokes[++i], y1=strokes[++i];
			int x2=strokes[++i], y2=strokes[++i];
			int x3=strokes[++i], y3=strokes[++i];
			QVector3D p1=viewer.projectAt(QPoint(x1, y1), planes[-1]);
			QVector3D p2=viewer.projectAt(QPoint(x2, y2), planes[-1]);
			QVector3D p3=viewer.projectAt(QPoint(x3, y3), planes[-1]);
			this->path<<p1.x()<<p1.y()<<p1.z()<<planes.index;
			this->path<<p2.x()<<p2.y()<<p2.z()<<planes.index;
			this->path<<p3.x()<<p3.y()<<p3.z()<<planes.index;
		}
		this->updateGraph();
	}
}
void QStrokes::operator--(int)
{
	this->removeLast();
	this->index=prev(size());
	this->graph--;
	this->begin=prev(begin);
	this->end=prev(end);
	while(index>2&&path[index]==MOVE)
	{
		this->removeLast();
		this->index=prev(size());
		this->begin=prev(begin);
		this->end=prev(end);
	}
}
void QStrokes::createGraph()
{
	for(int i=2, n=next(i); n<size(); i=n, n=next(i))
	{
		if(path[n]==MOVE)continue;
		this->graph+=new int[2]{i, n};
	}
	this->begin=size();
}
void QStrokes::updateGraph()
{
	this->end=index;
	this->graph+=new int[2]{begin, end};
	this->begin=end;
}
int QStrokes::prev(int index)
{
	if(index<=2)return 2;
	if(path[index-5]==MOVE)return index-5;
	if(path[index-5]==LINE)return index-5;
	if(path[index-13]==CUBIC)return index-13;
}
int QStrokes::next(int index)
{
	if(index==0)return 2;
	if(path[index]==MOVE)return index+5;
	if(path[index]==LINE)return index+5;
	if(path[index]==CUBIC)return index+13;
}
int QStrokes::planeIndex(int index)
{
	if(index==0)return 2;
	if(path[index]==MOVE)return path[index+4];
	if(path[index]==LINE)return path[index+4];
	if(path[index]==CUBIC)return path[index+12];
}
void QStrokes::clear()
{
	this->index=0;
	this->begin=0;
	this->path.clear();
	this->graph.clear();
}
void QStrokes::update()
{
	this->graph.updateEdge();
}
int QStrokes::size()
{
	return this->path.size();
}
void QStrokes::startPath(QPoint& point, QVector2D& direction)
{
	this->index=size();
	this->curve.clear();
	this->curve+=point;
	this->curve.ctrlTangents[0]=direction;
	QVector3D p=viewer.projectAt(point, planes[-1]);
	this->path<<LINE<<p.x()<<p.y()<<p.z()<<planes.index;
	if(direction.length()==0)this->begin=next(begin);
	this->updateGraph();
}
void QStrokes::setPath(int type)
{
	this->removeLast();
	QVector2D* points=curve.ctrlPoints;
	if(type==LINE)
	{
		QVector3D p=viewer.projectAt(points[3], planes[-1]);
		this->path<<LINE<<p.x()<<p.y()<<p.z()<<planes.index;
	}
	else if(type==CUBIC)
	{
		this->path<<CUBIC;
		QVector3D p1=viewer.projectAt(points[1], planes[-1]);
		QVector3D p2=viewer.projectAt(points[2], planes[-1]);
		QVector3D p3=viewer.projectAt(points[3], planes[-1]);
		this->path<<p1.x()<<p1.y()<<p1.z()<<planes.index;
		this->path<<p2.x()<<p2.y()<<p2.z()<<planes.index;
		this->path<<p3.x()<<p3.y()<<p3.z()<<planes.index;
	}
}
void QStrokes::append(const QVector<qreal>& strokes)
{
	for(int i=0; i<strokes.size(); i++)
	{
		this->index=size();
		int s=(int)strokes[i]; if(s==MOVE)this->begin=next(begin);
		qreal x=strokes[++i], y=strokes[++i], z=strokes[++i];
		this->path<<s<<x<<y<<z<<(int)strokes[++i];
	}
}
void QStrokes::addQuad(QVector<qreal>& strokes, QVector3D* points, int index)
{
	strokes<<MOVE<<points[0].x()<<points[0].y()<<points[0].z()<<index;
	for(int i=1; i<4; i++)
	{
		strokes<<LINE<<points[i].x()<<points[i].y()<<points[i].z()<<index;
	}
}
void QStrokes::extrudePlanes(int from, int to)
{
	QVector3D points[5];
	QVector<qreal> strokes;
	QVector4D plane=planes[to];
	if(from=-1)from=planes.index;
	QVector3D normal=plane.toVector3D();
	for(int i=2, n=next(i); n<size(); i=n, n=next(i))
	{
		if(planeIndex(i)!=from||planeIndex(n)!=from)continue;
		if(path[i]==CUBIC||path[n]==CUBIC||path[n]==MOVE)continue;
		QVector3D p00=this->graph.getPoint(i).toVector3D();
		QVector3D p01=this->graph.getPoint(n).toVector3D();
		QVector3D tangent=p01-p00; if(tangent.length()==0)continue; 
		QVector3D p10=QVecMath::intersectPlane(p00, normal, plane);
		QVector3D p11=QVecMath::intersectPlane(p01, normal, plane);
		points[0]=p00; points[1]=p10; points[2]=p11; points[3]=p01; points[4]=p00; 
		this->planes<<QVecMath::createPlane(p00, cross(tangent.normalized(), normal));
		this->addQuad(strokes, points, planes.index);
	}
	this->append(strokes);
}
void QStrokes::removeLast()
{
	if(index<=2)return;
	if(path[index]==MOVE)this->removeLast(5);
	else if(path[index]==LINE)this->removeLast(5);
	else if(path[index]==CUBIC)this->removeLast(13);
}
void QStrokes::removeLast(int length)
{
	for(int i=0; i<length; i++)this->path.removeLast();
}
