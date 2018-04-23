#include <QFile>
#include <QDebug>
#include <QTextStream>
#include "QSketch.h"

bool QSketch::load(QString fileName)
{
	QFile file(fileName); QStringList line;
	if(!file.open(QIODevice::ReadOnly))return false;	
	QTextStream textStream(&file); 
	bool isNotValidSketch=true;
	while(!textStream.atEnd())
	{
		line=textStream.readLine().split(" ");
		if(line[0]=="#"||line[0]=="//")continue;
		if(line[0]=="s"&&line.size()>=3)
		{
			int w=line[1].toInt();
			int h=line[2].toInt();
			this->size=QSize(w, h);
			isNotValidSketch=false;
			this->clear();
		}
		if(isNotValidSketch)return false;
		if(line[0]=="m")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			this->path<<MOVE;
			this->point2D<<x<<y;
			this->moveTo(x, y);
		}
		else if(line[0]=="l")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			this->path<<LINE;
			this->point2D<<x<<y;
			this->lineTo(x, y);
		}
		else if(line[0]=="c")
		{
			int c1=line[1].toInt();
			int c2=line[2].toInt();
			int c3=line[3].toInt();
			int c4=line[4].toInt();
			int c5=line[5].toInt();
			int c6=line[6].toInt();
			this->path<<CUBIC;
			this->point2D<<c1<<c2<<c3;
			this->point2D<<c4<<c5<<c6;
			this->cubicTo(c1, c2, c3, c4, c5, c6);
		}
	}
	return isNotValidSketch?false:true;
}
bool QSketch::save(QString fileName)
{
	QFile file(fileName);
	if(!file.open(QIODevice::WriteOnly))return false;
	QTextStream textStream(&file); 
	textStream<<"s "<<size.width()<<" ";
	textStream<<size.height()<<endl;
	for(int i=0, j=0; i<path.size(); i++)
	{
		if(path[i]==MOVE)
		{
			textStream<<"m "<<point2D[j++]<<" ";
			textStream<<point2D[j++]<<endl;
		}
		else if(path[i]==LINE)
		{
			textStream<<"l "<<point2D[j++]<<" ";
			textStream<<point2D[j++]<<endl;
		}
		else if(path[i]==CUBIC)
		{
			textStream<<"c "<<point2D[j++]<<" ";
			textStream<<point2D[j++]<<" ";
			textStream<<point2D[j++]<<" ";
			textStream<<point2D[j++]<<" ";
			textStream<<point2D[j++]<<" ";
			textStream<<point2D[j++]<<endl;
		}
	}
	return true;
}
void QSketch::operator=(QPoint point)
{
	this->curve=point;
	this->path<<MOVE;
	this->moveTo(point.x(), point.y());
	this->point2D<<point.x()<<point.y();
}
void QSketch::operator<<(QPoint point)
{
	this->curve<<point;
	this->path<<LINE;
	this->lineTo(point.x(), point.y());
	this->point2D<<point.x()<<point.y();
}
void QSketch::operator<<=(QPoint point)
{
	this->curve<<=point;
	this->path<<LINE;
	this->lineTo(point.x(), point.y());
	this->point2D<<point.x()<<point.y();
}
vec2 devide(QSize x, QSize y)
{
	return vec2((x.width()+0.0)/y.width(), (x.height()+0.0)/y.height());
}
void QSketch::resize(QSize size)
{
	for(int i=0; i<point2D.size(); i+=2)
	{
		this->setPoint2D(i, getPoint2D(i)*devide(size, this->size));
	}
	this->size=size;
}
vec2 QSketch::getPoint2D(int startIndex)
{
	return vec2
	(
		this->point2D[startIndex+0],
		this->point2D[startIndex+1]
	);
}
void QSketch::setPoint2D(int startIndex, vec2 point)
{
	this->point2D[startIndex+0]=point.x();
	this->point2D[startIndex+1]=point.y();
}
void QSketch::update()
{
	this->swap(QPainterPath());
	for(int i=0, j=0; i<path.size(); i++)
	{
		if(path[i]==MOVE)
		{
			int x=point2D[j++];
			int y=point2D[j++];
			this->moveTo(x, y);
		}
		else if(path[i]==LINE)
		{
			int x=point2D[j++];
			int y=point2D[j++];
			this->lineTo(x, y);
		}
		else if(path[i]==CUBIC)
		{
			int c1=point2D[j++], c2=point2D[j++]; 
			int c3=point2D[j++], c4=point2D[j++];
			int c5=point2D[j++], c6=point2D[j++];
			this->cubicTo(c1, c2, c3, c4, c5, c6);
		}
	}
}
void QSketch::clear()
{
	this->path.clear(); 
	this->curve.clear();
	this->point2D.clear();
	this->swap(QPainterPath());
}
