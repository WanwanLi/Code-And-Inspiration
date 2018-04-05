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
	int* edge=edges.last();
	for(int i=0; i<vertices.size(); i++)
	{
		if(i==edge[1])continue;
		if(isClose(vertices[i], vertices[edge[1]]))
		{
			this->vertices.removeLast();
			edge[1]=i; return;
		}
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
	for(int i=0; i<vertices.size(); i++)
	{
		if(isClose(vertices[i], vertex))return i;
	}
	return append(vertex);
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
bool QGraph::isClose(int i, int j)
{
	QVector2D x=getPoint(i), y=getPoint(j);
	if(x.distanceToPoint(y)<minDistance)
	{
		this->setPoint(j, x); return true;
	}
	else return false;
}
QVector2D QGraph::getPoint(int index)
{
	if(path[index]==MOVE)return QVector2D(path[index+1], path[index+2]);
	if(path[index]==LINE)return QVector2D(path[index+1], path[index+2]);
	if(path[index]==CUBIC)return QVector2D(path[index+5], path[index+6]);
	return QVector2D(0, 0);
}
void QGraph::setPoint(int index, QVector2D& point)
{
	if(path[index]==MOVE){path[index+1]=point.x(); path[index+2]=point.y();}
	else if(path[index]==LINE){path[index+1]=point.x(); path[index+2]=point.y();}
	else if(path[index]==CUBIC){path[index+5]=point.x(); path[index+6]=point.y();}
}
void QGraph::clear()
{
	this->vertices.clear();
	this->edges.clear();
}

