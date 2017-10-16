#include <QFile>
#include "QSketch.h"
#include <QTextStream>

bool QSketch::load(QString fileName)
{
	QFile file(fileName); QStringList line;
	if(!file.open(QIODevice::ReadOnly))return false;	
	QTextStream textStream(&file); this->clear();
	bool isNotValidSketch=true; 	this->strokes.clear(); 
	while(!textStream.atEnd())
	{
		line=textStream.readLine().split(" ");
		if(line[0]=="s"&&line.size()>=3)
		{
			int w=line[1].toInt();
			int h=line[2].toInt();
			this->resize(QSize(w, h));
			isNotValidSketch=false;
		}
		if(isNotValidSketch)continue;
		if(line[0]=="m")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			this->strokes<<MOVE;
			this->strokes<<x<<y;
		}
		else if(line[0]=="l")
		{
			int x=line[1].toInt();
			int y=line[2].toInt();
			this->strokes<<LINE;
			this->strokes<<x<<y;
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
		}
	}
	if(!isNotValidSketch)this->strokes.createGraph();
	return isNotValidSketch?false:true;
}
bool QSketch::save(QString fileName)
{
	QFile file(fileName); QString endl="\r\n";
	if(!file.open(QIODevice::WriteOnly))return false;
	QTextStream textStream(&file); 
	textStream<<"s "<<strokes[0]<<" ";
	textStream<<strokes[1]<<endl;
	for(int i=2; i<strokes.size(); i++)
	{
		if(strokes[i]==MOVE)
		{
			textStream<<"m "<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<endl;
		}
		else if(strokes[i]==LINE)
		{
			textStream<<"l "<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<endl;
		}
		else if(strokes[i]==CUBIC)
		{
			textStream<<"c "<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<" ";
			textStream<<strokes[++i]<<endl;
		}
	}
	return true;
}
void QSketch::update()
{
	this->swap(QPainterPath());
	for(int i=2; i<strokes.size(); i++)
	{
		if(strokes[i]==MOVE)
		{
			qreal x=strokes[++i];
			qreal y=strokes[++i];
			qreal z=strokes[++i];
			QPoint p=viewer.lookAt(x, y, z);
			this->moveTo(p.x(), p.y());
		}
		else if(strokes[i]==LINE)
		{
			qreal x=strokes[++i];
			qreal y=strokes[++i];
			qreal z=strokes[++i];
			QPoint p=viewer.lookAt(x, y, z);
			this->lineTo(p.x(), p.y());
		}
		else if(strokes[i]==CUBIC)
		{
			qreal x1=strokes[++i], y1=strokes[++i], z1=strokes[++i];
			qreal x2=strokes[++i], y2=strokes[++i], z2=strokes[++i];
			qreal x3=strokes[++i], y3=strokes[++i], z3=strokes[++i];
			QPoint p1=viewer.lookAt(x1, y1, z1);
			QPoint p2=viewer.lookAt(x2, y2, z2);
			QPoint p3=viewer.lookAt(x3, y3, z3);
			this->cubicTo(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y());
		}
	}
	this->planes.update();
	if(planes.isOnCreating)
	{
		for(int i=0; i<planes.line.size(); i++)
		{
			int p=planes.line[i], x=planes.line[++i], y=planes.line[++i];
			if(p==MOVE)this->moveTo(x, y); else this->lineTo(x, y);
		}
	}
	else
	{
		for(int i=0; i<planes.lines.size(); i++)
		{
			int s=(int)planes.lines[i];
			qreal x=planes.lines[++i];
			qreal y=planes.lines[++i];
			qreal z=planes.lines[++i];
			QPoint p=viewer.lookAt(x, y, z);
			if(s==MOVE)this->moveTo(p.x(), p.y()); 
			else this->lineTo(p.x(), p.y());
		}
	}
	for(int i=0; i<planes.ground.size(); i++)
	{
		int g=(int)planes.ground[i];
		qreal x=planes.ground[++i];
		qreal y=planes.ground[++i];
		qreal z=planes.ground[++i];
		QPoint p=viewer.lookAt(x, y, z);
		if(g==MOVE)this->moveTo(p.x(), p.y());
		else this->lineTo(p.x(), p.y());
	}
}
void QSketch::resize(QSize size)
{
	this->size=size;
	this->viewer.resize(size);
	if(strokes.size()==0)
	{
		this->strokes<<size.width();
		this->strokes<<size.height();
	}
	else
	{
		this->strokes[0]=size.width();
		this->strokes[1]=size.height();
	}
}
void QSketch::clear()
{
	this->strokes.clear(); 
	this->strokes<<size.width();
	this->strokes<<size.height();
	this->swap(QPainterPath());
}
