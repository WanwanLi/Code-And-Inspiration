#include "QGraph.h"

void QGraph::operator+=(int* edge)
{
	int begin=add(edge[0]), end=append(edge[1]);
	this->edges<<new int[2]{begin, end};
}
void QGraph::operator--(int)
{
	if(edges.isEmpty())return;
	this->edges.removeLast();
	this->updateVertices();
}
void QGraph::updateEdge()
{
	if(edges.isEmpty())return;
	qreal distance=minDistance;
	int *edge=edges.last(), index=-1;
	QVector3D x, y=getPoint(vertices[edge[1]]), min;
	for(int i=0; i<vertices.size(); i++)
	{
		if(i==edge[1])continue;
		x=getPoint(vertices[i]);
		qreal d=x.distanceToPoint(y);
		if(d<distance){distance=d; index=i; min=x;}
	}
	if(index!=-1)
	{
		this->setPoint(vertices[edge[1]], min);
		this->vertices.removeLast(); edge[1]=index; 
	}
}
int QGraph::append(int vertex)
{
	int index=vertices.size();
	this->vertices<<vertex;
	return index;
}
int QGraph::add(int vertex)
{
	int index=vertices.indexOf(vertex);
	if(index!=-1)return index;
	qreal distance=minDistance;
	QVector3D x, y=getPoint(vertex), min;
	for(int i=0; i<vertices.size(); i++)
	{
		x=getPoint(vertices[i]);
		qreal d=x.distanceToPoint(y);
		if(d<distance){distance=d; index=i; min=x;}
	}
	if(index!=-1){this->setPoint(vertex, min); return index;}
	else return append(vertex);
}
void QGraph::updateVertices()
{
	QVector<int> vertices;
	for(int* edge : edges)
	{
		this->addVertex(vertices, edge[0]);
		this->addVertex(vertices, edge[1]);
	}
	this->vertices.clear();
	this->vertices.append(vertices);
	vertices.clear();
}
void QGraph::addVertex(QVector<int>& vertices, int index)
{
	if(index==vertices.size())vertices<<this->vertices[index];
}
QVector3D QGraph::getPoint(int index)
{
	if((int)path[index]==MOVE)return QVector3D(path[index+1], path[index+2], path[index+3]);
	if((int)path[index]==LINE)return QVector3D(path[index+1], path[index+2], path[index+3]);
	if((int)path[index]==CUBIC)return QVector3D(path[index+9], path[index+10], path[index+11]);
	return QVector3D(0, 0, 0);
}
void QGraph::setPoint(int index, QVector3D& point)
{
	if((int)path[index]==MOVE){path[index+1]=point.x(); path[index+2]=point.y(); path[index+3]=point.z();}
	else if((int)path[index]==LINE){path[index+1]=point.x(); path[index+2]=point.y(); path[index+3]=point.z();}
	else if((int)path[index]==CUBIC){path[index+9]=point.x(); path[index+10]=point.y(); path[index+11]=point.z();}
}
void QGraph::clear()
{
	this->vertices.clear();
	this->edges.clear();
}

