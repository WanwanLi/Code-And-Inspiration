#include <QFile>
#include <QDebug>
#include <QTextStream>
#include "QSketch.h"

QSketch::QSketch(QSize size) : QPainterPath()
{
	this->sketchSize=size;
	this->strokes<<size.width();
	this->strokes<<size.height();
}
bool QSketch::load(QString fileName)
{
	QFile file(fileName); QStringList line;
	if(!file.open(QIODevice::ReadOnly))return false;	
	QTextStream textStream(&file); this->clear();
	bool isNotValidSketch=true; this->strokes.clear(); 
	while(!textStream.atEnd())
	{
		line=textStream.readLine().split(" ");
		if(line[0]=="s"&&line.size()>=3)
		{
			int w=line[1].toInt();
			int h=line[2].toInt();
			this->strokes<<w<<h;
			sketchSize=QSize(w, h);
			isNotValidSketch=false;
		}
		if(isNotValidSketch)continue;
		if(line[0]=="m")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			this->strokes<<MOVE;
			this->strokes<<x<<y;
			this->moveTo(x, y);
		}
		else if(line[0]=="l")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			this->strokes<<LINE;
			this->strokes<<x<<y;
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
			this->strokes<<CUBIC;
			this->strokes<<c1<<c2<<c3;
			this->strokes<<c4<<c5<<c6;
			this->cubicTo(c1, c2, c3, c4, c5, c6);
		}
	}
	qDebug()<<strokes;
	return isNotValidSketch?false:true;
}
bool QSketch::save(QString fileName)
{
	QFile file(fileName);
	if(!file.open(QIODevice::WriteOnly))return false;
	QTextStream textStream(&file); 
	textStream<<"s "<<strokes[0]<<" ";
	textStream<<strokes[1]<<"\r\n";
	for(int i=2; i<strokes.size(); i++)
	{
		if(strokes[i]==MOVE)
		{
			textStream<<"m "<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<"\r\n";
		}
		else if(strokes[i]==LINE)
		{
			textStream<<"l "<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<"\r\n";
		}
		else if(strokes[i]==CUBIC)
		{
			textStream<<"c "<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<"\r\n";
		}
	}
	return true;
}
void QSketch::move(QPoint point)
{
	this->strokes<<MOVE<<point.x()<<point.y();
}
void QSketch::line(QPoint point)
{
	this->strokes<<LINE<<point.x()<<point.y();
}
void QSketch::clear()
{
	this->strokes.clear(); 
	this->strokes<<sketchSize.width();
	this->strokes<<sketchSize.height();
	this->swap(QPainterPath());
}
QSize QSketch::size()
{
	return sketchSize;
}
