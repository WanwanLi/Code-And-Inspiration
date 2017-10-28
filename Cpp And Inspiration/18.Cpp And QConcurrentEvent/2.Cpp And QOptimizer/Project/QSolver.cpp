#include <QDebug>
#include "QSolver.h"
#include "QEnergy.h"
#include "QProblem.h"

QSolver::QSolver(QThread* thread)
{
	this->moveToThread(thread);
	this->connectToThread(thread);
}
void QSolver::connectToThread(QThread* thread)
{
	connect(thread, &QThread::started, this, &QSolver::start);
	connect(this, &QSolver::finished, this, &QSolver::quit);
	connect(this, &QSolver::finished, thread, &QThread::quit);
}
void QSolver::start()
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
	connect(&f, &QProblem::valueUpdated, this, &QSolver::valueUpdated);
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
void QSolver::valueUpdated(int iteration, const VectorXd& x)
{
	emit valueChanged(iteration);
	qDebug()<<toString(x);
}
void QSolver::quit()
{
	emit valueChanged(minimum);
}
