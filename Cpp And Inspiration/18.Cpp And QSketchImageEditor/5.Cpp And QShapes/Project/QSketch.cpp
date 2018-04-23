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
			this->point2D<<vec2(x, y);
			this->moveTo(x, y);
		}
		else if(line[0]=="l")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			this->path<<LINE;
			this->point2D<<vec2(x, y);
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
			this->point2D<<vec2(c1, c2);
			this->point2D<<vec2(c3, c4);
			this->point2D<<vec2(c5, c6);
			this->cubicTo(c1, c2, c3, c4, c5, c6);
		}
	}
	return isNotValidSketch?false:true;
}
bool QSketch::save(QString fileName)
{
	QFile file(fileName); QString endl="\r\n";
	if(!file.open(QIODevice::WriteOnly))return false;
	QTextStream textStream(&file); 
	textStream<<"s "<<size.width()<<" ";
	textStream<<size.height()<<endl;
	for(int i=0, j=0; i<path.size(); i++)
	{
		if(path[i]==MOVE)
		{
			vec2 point=point2D[j++];
			textStream<<"m "<<(int)point.x();
			textStream<<" "<<(int)point.y()<<endl;
		}
		else if(path[i]==LINE)
		{
			vec2 point=point2D[j++];
			textStream<<"l "<<(int)point.x();
			textStream<<" "<<(int)point.y()<<endl;
		}
		else if(path[i]==CUBIC)
		{
			vec2 p1=point2D[j++], p2=point2D[j++], p3=point2D[j++];
			textStream<<"c "<<(int)p1.x()<<" "<<(int)p1.y();
			textStream<<" "<<(int)p2.x()<<" "<<(int)p2.y();
			textStream<<" "<<(int)p3.x()<<" "<<(int)p3.y()<<endl;
		}
	}
	return true;
}
void QSketch::operator=(QPoint point)
{
	this->path<<MOVE;
	this->moveTo(point);
	this->point2D<<vec2(point);
}
void QSketch::operator+=(QPoint point)
{
	this->path<<LINE;
	this->lineTo(point);
	this->point2D<<vec2(point);
}
vec2 devide(QSize x, QSize y)
{
	return vec2((x.width()+0.0)/y.width(), (x.height()+0.0)/y.height());
}
void QSketch::resize(QSize size)
{
	vec2 scale=devide(size, this->size);
	for(vec2& p : point2D)p*=scale; this->size=size;
}
void QSketch::update()
{
	this->swap(QPainterPath());
	for(int i=0, j=0; i<path.size(); i++)
	{
		if(path[i]==MOVE){vec2 p=point2D[j++]; this->moveTo(p.x(), p.y());}
		else if(path[i]==LINE){vec2 p=point2D[j++]; this->lineTo(p.x(), p.y());}
		else if(path[i]==CUBIC)
		{
			vec2 p1=point2D[j++], p2=point2D[j++], p3=point2D[j++];
			this->cubicTo(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y());
		}
	}
}
void QSketch::removeLast()
{
	if(path.size()==2)
	{
		if(point2D[0]==point2D[1])this->clear();
	}
	else if(path.size()>2)
	{
		int last=path.size()-1;
		if(point2D[last]==point2D[last-1])
		{
			this->path.removeLast();
			this->point2D.removeLast();
		}
	}
}
void QSketch::clear()
{
	this->path.clear(); 
	this->point2D.clear();
	this->swap(QPainterPath());
}
