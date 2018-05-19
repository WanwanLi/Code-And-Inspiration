#include <QFile>
#include <QDebug>
#include "QSketch.h"
#include "QAnalyzer.h"
#define dot(x, y) vec2::dotProduct(x, y)

QString QAnalyzer::toString(int value)
{
	if(value==LOOP)return "LOOP";
	else if(value==VERTICAL)return "VER";
	else if(value==PARALLEL)return "PAR";
	else if(value==HORIZONTAL)return "HOR";
	else if(value==PERPENDICULAR)return "PER";
	else return "NULL";
}
int QAnalyzer::toValue(QString string)
{
	if(string=="LOOP")return LOOP;
	else if(string=="VER")return VERTICAL;
	else if(string=="PAR")return PARALLEL;
	else if(string=="HOR")return HORIZONTAL;
	else if(string=="PER")return PERPENDICULAR;
	else return -1;
}
int QAnalyzer::count(int value)
{
	switch(value)
	{
		case LOOP: return 2;
		case VERTICAL: return 2;
		case PARALLEL: return 4;
		case HORIZONTAL: return 2;
		case PERPENDICULAR: return 3;
	}
	return 0;
}
void QAnalyzer::load(QSketch* sketch)
{
	this->sketch=sketch;
	this->path=sketch->path;
	this->point2D=sketch->point2D;
	for(int start, end, i=0; i<path.size(); i++)
	{
		if(path[i]==QSketch::MOVE)
		{
			start=i; for(i++; i<path.size()&&
			path[i]!=QSketch::MOVE; i++); i--; end=i;
			this->curves<<vec2(start, end);
			if(point2D[end]==point2D[start])
			this->loops<<vec2(start, end);
		}
	}
}
void QAnalyzer::run()
{
	for(vec2 curve : curves)
	{
		int i=curve.x(), j=curve.y();
		this->getCurveRegularity(i, j);
	}
	for(vec2 loop : loops)
	{
		this->regularity<<LOOP<<loop.x()<<loop.y();
	}
	for(int i=0; i<parallelLines.size(); i++)
	{
		for(int j=0; j<parallelLines[i].size()-1; j++)
		{
			int line0=parallelLines[i][j+0];
			int line1=parallelLines[i][j+1];
			this->regularity<<PARALLEL;
			this->regularity<<line0<<next(line0);
			this->regularity<<line1<<next(line1);
		}
	}
}
void QAnalyzer::save(QString fileName)
{
	QFile file(fileName); QString endl="\r\n";
	if(!file.open(QIODevice::WriteOnly))return;
	QTextStream textStream(&file);
	this->sketch->save(textStream);
	for(int i=0; i<regularity.size(); i++)
	{
		textStream<<QAnalyzer::toString(regularity[i]);
		int n=QAnalyzer::count(regularity[i]);
		for(int t=0; t<n; t++)
		textStream<<" "<<regularity[++i];
		textStream<<endl;
	}
	file.close();
}
void QAnalyzer::operator<<(QStringList& list)
{
	int value=toValue(list[0]);
	if(value<0)return;
	int n=count(value);
	this->regularity<<value;
	for(int t=1; t<=n; t++)
	this->regularity<<list[t].toInt();
}
void insert(QVector<QVector<vec2>>& pointsCluster, vec2 startPoint, vec2 endPoint)
{
	#define has(x, y) x.indexOf(y)>=0
	for(int i=0; i<pointsCluster.size(); i++)
	{
		if(has(pointsCluster[i], startPoint)&&has(pointsCluster[i], endPoint))return;
		else if(has(pointsCluster[i], startPoint))
		{
			for(int j=i+1; j<pointsCluster.size(); j++)
			{
				if(has(pointsCluster[j], endPoint))
				{
					pointsCluster[i]+=pointsCluster[j];
					pointsCluster.removeAt(j); return;
				}
			}
			pointsCluster[i]<<endPoint; return;
		}
		else if(has(pointsCluster[i], endPoint))
		{
			for(int j=i+1; j<pointsCluster.size(); j++)
			{
				if(has(pointsCluster[j], startPoint))
				{
					pointsCluster[i]+=pointsCluster[j];
					pointsCluster.removeAt(j); return;
				}
			}
			pointsCluster[i]<<startPoint; return;
		}
	}
	#undef has(x, y)
	QVector<vec2> points;
	points<<startPoint<<endPoint;
	pointsCluster<<points;
}
void draw(QPainter& painter, QVector<QVector<vec2>> pointsCluster)
{
	QString label="I"; for(int i=0; i<pointsCluster.size(); i++, label+=" I")
	{
		for(int j=0; j<pointsCluster[i].size(); j++)
		{
			vec2 p=pointsCluster[i][j]+vec2(0, -5);
			painter.drawText(QPointF((int)p.x(), (int)p.y()), label);
		}
	}
}
void QAnalyzer::drawRegularity(QPainter& painter)
{
	QVector<QVector<vec2>> parallelCenters;
	for(int i=0; i<regularity.size(); i++)
	{
		switch(regularity[i])
		{
			case HORIZONTAL:
			{
				int r1=regularity[i+1], r2=regularity[i+2];
				vec2 p1=point2D[r1], p2=point2D[r2], m1=(p1+p2)/2, m2=m1+vec2(0, -20);
				painter.drawLine((int)m1.x(), (int)m1.y(), (int)m2.x(), (int)m2.y());
				painter.drawText(QPointF((int)m2.x(), (int)m2.y()), "HOR"); break;
			}
			case VERTICAL:
			{
				int r1=regularity[i+1], r2=regularity[i+2];
				vec2 p1=point2D[r1], p2=point2D[r2], m1=(p1+p2)/2, m2=m1+vec2(20, 0);
				painter.drawLine((int)m1.x(), (int)m1.y(), (int)m2.x(), (int)m2.y());
				painter.drawText(QPointF((int)m2.x(), (int)m2.y()), "VER"); break;
			}
			case PERPENDICULAR:
			{
				int r1=regularity[i+1], r2=regularity[i+2], r3=regularity[i+3], len=20;
				vec2 p1=point2D[r1], p2=point2D[r2], p3=point2D[r3];
				vec2 a=p2+len*(p1-p2).normalized(), b=p2+len*(p3-p2).normalized();
				painter.drawLine((int)p2.x(), (int)p2.y(), (int)a.x(), (int)a.y());
				painter.drawLine((int)p2.x(), (int)p2.y(), (int)b.x(), (int)b.y());
				painter.drawText(QPointF((int)p2.x(), (int)p2.y()), "PER"); break;
			}
			case PARALLEL:
			{
				int r1=regularity[i+1], r2=regularity[i+2];
				int r3=regularity[i+3], r4=regularity[i+4];
				vec2 p1=point2D[r1], p2=point2D[r2], p5=(p1+p2)/2;
				vec2 p3=point2D[r3], p4=point2D[r4], p6=(p3+p4)/2;
				insert(parallelCenters, p5, p6); break;
			}
		}
		draw(painter, parallelCenters);
		i+=count(regularity[i]);
	}
}
void QAnalyzer::getCurveRegularity(int startIndex, int endIndex)
{
	for(int i=startIndex; i<endIndex; i++)
	{
		if(isParallel(tangentAt(i), yAxis))
		this->regularity<<VERTICAL<<i<<next(i);
		else if(isParallel(tangentAt(i), xAxis))
		this->regularity<<HORIZONTAL<<i<<next(i);
	}
	for(int i=isLoop(startIndex)?startIndex:startIndex+1; i<endIndex; i++)
	{
		vec2 tangent1=tangentAt(prev(i)), tangent2=tangentAt(i);
		if(dot(tangent1, tangent2)*dot(tangent1, tangent2)<maxError)
		this->regularity<<PERPENDICULAR<<prev(i)<<i<<next(i);
	}
	for(int i=startIndex; i<endIndex-1; i++)
	{
		vec2 tangent=tangentAt(i);
		for(int j=i+1; j<endIndex; j++)
		{
			if(isParallel(tangent, tangentAt(j)))
			this->addParallelLines(i, j);
		}
	}
}
void QAnalyzer::addParallelLines(int line1, int line2)
{
	#define has(x, y) x.indexOf(y)>=0
	for(int i=0; i<parallelLines.size(); i++)
	{
		if(has(parallelLines[i], line1)&&has(parallelLines[i], line2))return;
		else if(has(parallelLines[i], line1))
		{
			if(!isParallelToLines(i, line2))return;
			for(int j=i+1; j<parallelLines.size(); j++)
			{
				if(has(parallelLines[j], line2))
				{
					this->parallelLines[i]+=parallelLines[j];
					this->parallelLines.removeAt(j);
					this->avgLineDirections.removeAt(j);
					updateAvgLineDirections(i); return;
				}
			}
			this->parallelLines[i]<<line2;
			updateAvgLineDirections(i); return;
		}
		else if(has(parallelLines[i], line2))
		{
			if(!isParallelToLines(i, line1))return;
			for(int j=i+1; j<parallelLines.size(); j++)
			{
				if(has(parallelLines[j], line1))
				{
					this->parallelLines[i]+=parallelLines[j];
					this->parallelLines.removeAt(j);
					this->avgLineDirections.removeAt(j);
					updateAvgLineDirections(i); return;
				}
			}
			this->parallelLines[i]<<line1;
			updateAvgLineDirections(i); return;
		}
	}
	vec2 dir1=tangentAt(line1), dir2=tangentAt(line2);
	veci lines; lines<<line1<<line2; this->parallelLines<<lines;
	this->avgLineDirections<<addLineDirection(dir1, dir2).normalized();
	#undef has
}
void QAnalyzer::updateAvgLineDirections(int index)
{
	vec2 sum=vec2(0, 0);
	if(parallelLines[index].size()==0)return;
	for(int line : parallelLines[index])
	{
		vec2 dir=tangentAt(line);
		sum=addLineDirection(sum, dir);
	}
	this->avgLineDirections[index]=sum.normalized();
}
bool QAnalyzer::isParallelToLines(int lines, int line)
{
	vec2 dir1=avgLineDirections[lines];
	vec2 dir2=tangentAt(line);
	return isParallel(dir1, dir2);
}
void QAnalyzer::clear()
{
	this->loops.clear();
	this->curves.clear();
	this->regularity.clear();
	this->parallelLines.clear();
	this->avgLineDirections.clear();
}
int QAnalyzer::prev(int index)
{
	for(vec2 loop : loops)
	if(index==loop.x())
	return loop.y()-1;
	return index-1;
}
int QAnalyzer::next(int index)
{
	for(vec2 loop : loops)
	if(index+1==loop.y())
	return loop.x();
	return index+1;
}
bool QAnalyzer::isLoop(int startIndex)
{
	for(vec2 loop : loops)
	if(startIndex==loop.x())
	return true;
	return false;
}
vec2 QAnalyzer::tangentAt(int index)
{
	vec2 start=point2D[index];
	vec2 end=point2D[next(index)];
	return (end-start).normalized();
}
vec2 QAnalyzer::addLineDirection(vec2 dir1, vec2 dir2)
{
	if(dir1.length()==0)return dir2;
	return dot(dir1.normalized(), dir2)>0?dir1+dir2:dir1-dir2;
}
bool QAnalyzer::isParallel(vec2 x, vec2 y)
{
	return abs(1-abs(dot(x.normalized(), y.normalized())))<error;
}
