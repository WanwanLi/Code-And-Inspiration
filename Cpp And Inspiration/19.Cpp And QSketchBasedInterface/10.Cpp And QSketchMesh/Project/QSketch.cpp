#include <QFile>
#include "QSketch.h"
#include <QTextStream>

bool QSketch::load(QString fileName)
{
	QFile file(fileName); QStringList line;
	if(!file.open(QIODevice::ReadOnly))return false;	
	QTextStream textStream(&file); this->clear();
	this->strokes.clear(); this->planes.clear();
	this->planes.removeFirst();
	bool isNotValidSketch=true; 	
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
		if(line[0]=="v")this->viewer+=line;
		else if(line[0]=="f")this->viewer+=line;
		else if(line[0]=="u")this->viewer+=line;
		else if(line[0]=="r")this->viewer+=line;
		else if(line[0]=="p")
		{
			qreal x=line[1].toDouble();
			qreal y=line[2].toDouble();
			qreal z=line[3].toDouble();
			qreal w=line[4].toDouble();
			this->planes<<QVector4D(x, y, z, w);
		}
		else if(line[0]=="m")
		{
			int x=line[1].toInt(), y=line[2].toInt(), w=line[3].toInt();
			QVector3D p=viewer.projectAt(x, y, planes[w]);
			this->strokes<<MOVE<<p.x()<<p.y()<<p.z()<<w;
		}
		else if(line[0]=="l")
		{
			int x=line[1].toInt(), y=line[2].toInt(), w=line[3].toInt();
			QVector3D p=viewer.projectAt(x, y, planes[w]);
			this->strokes<<LINE<<p.x()<<p.y()<<p.z()<<w;
		}
		else if(line[0]=="c")
		{
			int x1=line[1].toInt(), y1=line[2].toInt(), w1=line[3].toInt();
			int x2=line[4].toInt(), y2=line[5].toInt(), w2=line[6].toInt();
			int x3=line[7].toInt(), y3=line[8].toInt(), w3=line[9].toInt();
			QVector3D p1=viewer.projectAt(x1, y1, planes[w1]);
			QVector3D p2=viewer.projectAt(x2, y2, planes[w2]);
			QVector3D p3=viewer.projectAt(x3, y3, planes[w3]);
			this->strokes<<CUBIC;
			this->strokes<<p1.x()<<p1.y()<<p1.z()<<w1;
			this->strokes<<p2.x()<<p2.y()<<p2.z()<<w2;
			this->strokes<<p3.x()<<p3.y()<<p3.z()<<w3;
		}
	}
	this->strokes.createGraph();
	return isNotValidSketch?false:true;
}
bool QSketch::save(QString fileName)
{
	QFile file(fileName); QString endl="\r\n";
	if(!file.open(QIODevice::WriteOnly))return false;
	QTextStream textStream(&file); 
	textStream<<"s "<<strokes[0]<<" ";
	textStream<<strokes[1]<<" 0"<<endl;
	this->viewer<<textStream;
	for(int i=0; i<planes.size(); i++)
	{
		textStream<<"p "<<planes[i].x()<<" "<<planes[i].y();
		textStream<<" "<<planes[i].z()<<" "<<planes[i].w()<<endl;
	}
	for(int i=2; i<strokes.size(); i++)
	{
		if(strokes[i]==MOVE)
		{
			qreal x=strokes[++i];
			qreal y=strokes[++i];
			qreal z=strokes[++i];
			qreal w=strokes[++i];
			QPoint p=viewer.lookAt(x, y, z);
			textStream<<"m "<<p.x()<<" ";
			textStream<<p.y()<<" "<<w<<endl;
		}
		else if(strokes[i]==LINE)
		{
			qreal x=strokes[++i];
			qreal y=strokes[++i];
			qreal z=strokes[++i];
			qreal w=strokes[++i];
			QPoint p=viewer.lookAt(x, y, z);
			textStream<<"l "<<p.x()<<" ";
			textStream<<p.y()<<" "<<w<<endl;
		}
		else if(strokes[i]==CUBIC)
		{
			qreal x1=strokes[++i], y1=strokes[++i], z1=strokes[++i], w1=strokes[++i];
			qreal x2=strokes[++i], y2=strokes[++i], z2=strokes[++i], w2=strokes[++i];
			qreal x3=strokes[++i], y3=strokes[++i], z3=strokes[++i], w3=strokes[++i];
			QPoint p1=viewer.lookAt(x1, y1, z1);
			QPoint p2=viewer.lookAt(x2, y2, z2);
			QPoint p3=viewer.lookAt(x3, y3, z3);
			textStream<<"c "<<p1.x()<<" "<<p1.y()<<" "<<w1;
			textStream<<" "<<p2.x()<<" "<<p2.y()<<" "<<w2;
			textStream<<" "<<p3.x()<<" "<<p3.y()<<" "<<w3<<endl;
		}
	}
	this->planes>>textStream;
	return true;
}
void QSketch::update()
{
	this->updatePainterPaths();
	for(int i=2; i<strokes.size(); i++)
	{
		if(strokes[i]==MOVE)
		{
			qreal x=strokes[++i];
			qreal y=strokes[++i];
			qreal z=strokes[++i];
			qreal w=strokes[++i];
			QPoint p=viewer.lookAt(x, y, z);
			this->moveTo(p.x(), p.y(), (int)w);
		}
		else if(strokes[i]==LINE)
		{
			qreal x=strokes[++i];
			qreal y=strokes[++i];
			qreal z=strokes[++i];
			qreal w=strokes[++i];
			QPoint p=viewer.lookAt(x, y, z);
			this->lineTo(p.x(), p.y(), (int)w);
		}
		else if(strokes[i]==CUBIC)
		{
			qreal x1=strokes[++i], y1=strokes[++i], z1=strokes[++i], w1=strokes[++i];
			qreal x2=strokes[++i], y2=strokes[++i], z2=strokes[++i], w2=strokes[++i];
			qreal x3=strokes[++i], y3=strokes[++i], z3=strokes[++i], w3=strokes[++i];
			QPoint p1=viewer.lookAt(x1, y1, z1);
			QPoint p2=viewer.lookAt(x2, y2, z2);
			QPoint p3=viewer.lookAt(x3, y3, z3);
			this->cubicTo(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y(), (int)w1);
		}
	}
	if(shapes.isOnCreating)
	{
		for(int i=0; i<shapes.size(); i++)
		{
			int s=shapes[i], x=shapes[++i], y=shapes[++i];
			if(s==MOVE)this->moveTo(x, y, 0); 
			else this->lineTo(x, y, 0);
		}
	}
	this->planes.update();
	if(planes.isOnCreating)
	{
		for(int i=0; i<planes.line.size(); i++)
		{
			int p=planes.line[i], x=planes.line[++i], y=planes.line[++i];
			if(p==MOVE)this->moveTo(x, y, 0); else this->lineTo(x, y, 0);
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
			if(s==MOVE)this->moveTo(p.x(), p.y(), planes.index);
			else this->lineTo(p.x(), p.y(),  planes.index);
		}
	}
	for(int i=0; i<planes.ground.size(); i++)
	{
		int g=(int)planes.ground[i];
		qreal x=planes.ground[++i];
		qreal y=planes.ground[++i];
		qreal z=planes.ground[++i];
		QPoint p=viewer.lookAt(x, y, z);
		if(g==MOVE)this->moveTo(p.x(), p.y(), 0);
		else this->lineTo(p.x(), p.y(), 0);
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
void QSketch::updatePainterPaths()
{
	for(QPainterPath& painterPath : painterPaths)
	{
		QPainterPath newPainterPath=QPainterPath();
		painterPath.swap(newPainterPath);
	}
	while(painterPaths.size()<planes.size())
	this->painterPaths<<QPainterPath();
}
void QSketch::moveTo(int x, int y, int index)
{
	this->painterPaths[index].moveTo(x, y);
}
void QSketch::lineTo(int x, int y, int index)
{
	this->painterPaths[index].lineTo(x, y);
}
void QSketch::cubicTo(int x1, int y1, int x2, int y2, int x3, int y3, int index)
{
	this->painterPaths[index].cubicTo(x1, y1, x2, y2, x3, y3);
}
QPainterPath& QSketch::operator[](int index)
{
	return this->painterPaths[index];
}
int QSketch::length()
{
	return this->painterPaths.size();
}
void QSketch::clear()
{
	this->planes.clear(); 
	this->strokes.clear(); 
	this->painterPaths.clear();
	this->strokes<<size.width();
	this->strokes<<size.height();
}
