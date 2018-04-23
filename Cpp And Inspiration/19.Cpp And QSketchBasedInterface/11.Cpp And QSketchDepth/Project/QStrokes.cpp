#include "QStrokes.h"
#include "QVecMath.h"

qreal& QStrokes::operator[](int index)
{
	return isDepthTestEnabled?this->depthPath[index]:this->path[index];
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
		this->index=path.size();
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
	this->index=prev(path.size());
	this->graph--;
	this->begin=prev(begin);
	this->end=prev(end);
	while(index>2&&path[index]==MOVE)
	{
		this->removeLast();
		this->index=prev(path.size());
		this->begin=prev(begin);
		this->end=prev(end);
	}
}
void QStrokes::createGraph()
{
	for(int i=2, n=next(i); n<path.size(); i=n, n=next(i))
	{
		if(path[n]==MOVE)continue;
		this->graph+=new int[2]{i, n};
	}
	this->begin=path.size();
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
	return isDepthTestEnabled?this->depthPath.size():this->path.size();
}
void QStrokes::startPath(QPoint& point, QVector2D& direction)
{
	this->index=path.size();
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
		this->index=path.size();
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
	for(int i=2, n=next(i); n<path.size(); i=n, n=next(i))
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
void QStrokes::updateDepthPath()
{
	if(!isDepthTestEnabled)return;
	this->depthPath.clear();
	this->depthPath<<path[0];
	this->depthPath<<path[1];
	for(int i=2; i<path.size(); i=next(i))
	{
		this->addDepthPath(i);
	}
}
void QStrokes::updateDepthTest()
{
	this->isDepthTestEnabled=!isDepthTestEnabled;
}
void QStrokes::addDepthPath(int index)
{
	this->appendDepthPath(index);
	QVector<qreal> positions, depths;
	positions.clear(); depths.clear();
	if(intersectDepthPath(positions, depths, index, next(index)))
	{
if(positions.size()==5)
{
qDebug()<<"p0="<<graph.getPoint(index);
qDebug()<<"p1="<<graph.getPoint(next(index));
qDebug()<<"positions:"<<positions;
qDebug()<<"depths: "<<depths;
}
		this->appendDepthPath(index, MOVE, positions[0]);
		for(int i=0; i<positions.size()-1; i++)
		{
			qreal t0=positions[i+0], t1=positions[i+1];
if(positions.size()==5)
{
qDebug()<<"depth("<<t0<<")="<<depth(index, t0);
}
			if(isEndPosition(positions, i))this->appendDepthPath(index, LINE, t1);
			else if(equals(depth(index, t0), depths[i+0])&&equals(depth(index, t1), depths[i+1]))
			{
				this->appendDepthPath(index, LINE, t1);
			}
			else if(greater(depth(index, t0), depths[i+0])&&greater(depth(index, t1), depths[i+1]))
			{
if(positions.size()==5)
{
qDebug()<<"MOVE "<<t0<<", "<<t1;
}
				this->appendDepthPath(index, MOVE, t1);
			}
			else this->appendDepthPath(index, LINE, t1);
		}
	}
}
bool QStrokes::equals(qreal x, qreal y)
{
	return abs(x-y)<1e-5;
}
bool QStrokes::greater(qreal x, qreal y)
{
	return equals(x, y)?true:x>y;
}
bool QStrokes::isEndPosition(const QVector<qreal>& positions, int index)
{
	int i0=index+0, i1=index+1, E=positions.size()-1;
	qreal t0=positions[i0], t1=positions[i1];
	if(i0==0)if(t0==0||t0==1)return true;
	if(i1==E)if(t1==0||t1==1)return true;
	return false;
}
bool QStrokes::intersectDepthPath(QVector<qreal>& positions, QVector<qreal>& depths, int index0, int index1)
{
	qreal position, depth;
	if(index1>=path.size())return false;
	int plane0=planeIndex(index1);
	QVector<bool> intersectPlane;
	QVector<int> intersectTimes;
	QVector<qreal> planeDepths;
	QVector<qreal> planePositions;
	QVector<qreal> intersectDepths;
	QVector<qreal> intersectPositions;
	for(int i=0; i<planes.size(); i++)
	{
		intersectPlane<<false;
		intersectTimes<<0;
		planeDepths<<0;
		planePositions<<0; 
		intersectDepths<<0<<0;
		intersectPositions<<0<<0;
	}
	QVector3D p0=graph.getPoint(index0).toVector3D();
	QVector3D p1=graph.getPoint(index1).toVector3D();
	depths.insert(insert(positions, 0), (p0-viewer.eye()).length());
	depths.insert(insert(positions, 1), (p1-viewer.eye()).length());
	if(path[index1]==MOVE)return false;
	else if(path[index1]==LINE)
	{
		for(int i=2, j=next(i); j<path.size(); i=j, j=next(i))
		{
			int plane1=planeIndex(j);
			if(plane0==plane1||path[j]==MOVE)continue;
			minimizeEndDepth(0, p0, positions, depths, plane1);
			minimizeEndDepth(1, p1, positions, depths, plane1);
			if(!intersectPlane[plane1]&&intersectLineWithPlane(position, depth, index0, index1, plane1))
			{
				intersectPlane[plane1]=true; planePositions[plane1]=position; planeDepths[plane1]=depth;
			}
			if(path[j]==LINE&&intersectLineWithLine(position, depth, index0, index1, i, j))
			{
				updateIntersectionInfo(intersectTimes, intersectPositions, intersectDepths, position, depth, plane1);
			}
		}
	}
	else if(path[index1]==CUBIC)return false;
	for(int i=0; i<planes.size(); i++)
	{
		if(intersectTimes[i]>=2)
		{
			depths.insert(insert(positions, intersectPositions[i*2+0]), intersectDepths[i*2+0]);
			depths.insert(insert(positions, intersectPositions[i*2+1]), intersectDepths[i*2+1]);
			if(intersectPlane[i]&&planePositions[i]>=intersectPositions[i*2+0])
			if(planePositions[i]<=intersectPositions[i*2+1])
			{
				depths.insert(insert(positions, planePositions[i]), planeDepths[i]);
			}
		}
	}
	if(positions.size()<=2)return false;
	return true;
}
void QStrokes::minimizeEndDepth(qreal position, QVector3D end, QVector<qreal>& positions, QVector<qreal>& depths, int plane)
{
	QVector3D eye=viewer.eye(), point=QVecMath::intersectPlane(eye, (end-eye).normalized(), planes[plane]);
	qreal depth=(point-eye).length(); int t=positions.indexOf(position); if(depth<depths[t])depths[t]=depth;
}
void QStrokes::updateIntersectionInfo(QVector<int>& times, QVector<qreal>& positions, QVector<qreal>& depths, qreal position, qreal depth, int plane)
{
	int i=plane; times[i]++;
	if(times[i]==1){positions[i*2+0]=position; depths[i*2+0]=depth;}
	else if(times[i]==2)
	{
		if(position<positions[i*2+0])
		{
			positions[i*2+1]=positions[i*2+0]; depths[i*2+1]=depths[i*2+0];
			positions[i*2+0]=position; depths[i*2+0]=depth;
		}
		else {positions[i*2+1]=position; depths[i*2+1]=depth;}
	}
	else 
	{
		if(position<positions[i*2+0]){positions[i*2+0]=position; depths[i*2+0]=depth;}
		else if(position>positions[i*2+1]){positions[i*2+1]=position; depths[i*2+1]=depth;}
	}
}
QVector2D QStrokes::getPoint(int index)
{
	qreal x=path[index+0];
	qreal y=path[index+1];
	qreal z=path[index+2];
	QPoint p=viewer.lookAt(x, y, z);
	return QVector2D(p.x(), p.y());
}
bool QStrokes::intersectLineWithLine(qreal& position, qreal& depth, int start0, int end0, int start1, int end1)
{
	QVector2D p1=getPoint(start0+1), p2=getPoint(end0+1);
	QVector2D p3=getPoint(start1+1), p4=getPoint(end1+1);
	if(QVecMath::isParallel(p1, p2, p3, p4, 1e-5))return false;
	QVector2D p=QVecMath::intersectLineWithLine(p1, p2, p3, p4);
	qreal t=((p-p3)/(p4-p3)).x(); if(t<0||t>1)return false;
	QVector3D q0=graph.getPoint(start1).toVector3D();
	QVector3D q1=graph.getPoint(end1).toVector3D();
	depth=(q0*(1-t)+q1*t-viewer.eye()).length();
	position=((p-p1)/(p2-p1)).x();
	return true;
}
bool QStrokes::intersectLineWithPlane(qreal& position, qreal& depth, int start, int end, int plane)
{
	QVector3D p0=graph.getPoint(start).toVector3D(), p1=graph.getPoint(end).toVector3D();
	QVector3D p=QVecMath::intersectPlane(p0, (p1-p0).normalized(), planes[plane]);	
	depth=(p-viewer.eye()).length(); position=((p-p0)/(p1-p0)).x();
	return (0<=position&&position<=1)?true:false;
}
qreal QStrokes::depth(int index, qreal position)
{
	int nextIndex=next(index); 
	if(nextIndex>=path.size())return 0;
	if(path[nextIndex]==LINE)
	{
		QVector3D p0=graph.getPoint(index).toVector3D();
		QVector3D p1=graph.getPoint(nextIndex).toVector3D();
		return (p0*(1-position)+p1*position-viewer.eye()).length();
	}
	return 0;
}
void QStrokes::appendDepthPath(int index)
{
	for(int i=index, nextIndex=next(index); i<path.size()&&i<nextIndex; this->depthPath<<path[i++]);
}
void QStrokes::appendDepthPath(int index, int type, qreal position)
{
	int nextIndex=next(index); 
	if(nextIndex>=path.size())return;
	if(position<0||position>1)return;
	QVector4D p0=graph.getPoint(index);
	QVector4D p1=graph.getPoint(nextIndex);
	QVector4D p=p0*(1-position)+p1*position;
	this->depthPath<<type<<p.x()<<p.y()<<p.z()<<p.w();
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
int QStrokes::insert(QVector<qreal>& vector, qreal value)
{
	int i=0; while(i<vector.size()&&vector[i]<value)i++;
	vector.insert(i, value); return i;
}
