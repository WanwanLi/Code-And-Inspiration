#include <QFile>
#include <QDebug>
#include "QEnergy.h"
#include "QProblem.h"
#include "QOptimizer.h"
#include <QTextStream>

QOptimizer::QOptimizer(QThread* thread)
{
	this->moveToThread(thread);
	this->connectToThread(thread);
}
void QOptimizer::connectToThread(QThread* thread)
{
	connect(thread, &QThread::started, this, &QOptimizer::start);
	connect(this, &QOptimizer::finished, this, &QOptimizer::quit);
	connect(this, &QOptimizer::finished, thread, &QThread::quit);
}
void QOptimizer::start()
{
	QString endl="\n";
	QString fileName="energy.fsk";
	QEnergy energy;
	if(!energy.loadSketchFile(fileName))
	{
		qDebug()<<"Error: in load file."<<endl;
		emit finished(); return;
	}
	QProblem f(&energy);
	connect(&f, &QProblem::valueChanged, this, &QOptimizer::valueChanged);
	VectorXd x=energy.planeVector;
	Criteria<double> criteria=Criteria<double>::defaults();
	criteria.iterations=maximum;
	GradientDescentSolver<QProblem> solver;
	solver.setStopCriteria(criteria);
	solver.minimize(f, x);
	emit finished();
}
QString toString(const VectorXd& vector)
{
	QString string="["+QString::number(vector[0]);
	for(int i=1; i<vector.size(); i++)
	{
		string+=", "+QString::number(vector[i]);
	}
	return string+"]";
}
void QOptimizer::valueChanged(int iteration, const VectorXd& variable)
{
	QFile file(fileName+"."+QString::number(iteration));
	if(!file.open(QIODevice::WriteOnly))return;
	QTextStream textStream(&file);
	textStream<<toString(variable);
	for(int i=0; i<500; i++)
	qDebug()<<toString(variable);
	file.close(); emit setValue(iteration);
}
void QOptimizer::quit()
{
	emit setValue(minimum);
}
