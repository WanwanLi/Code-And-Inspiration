#include <QDebug>
#include "QRunnable.h"

QRunnable::QRunnable(QThread* thread)
{
	this->moveToThread(thread);
	this->connectToThread(thread);
}
void QRunnable::connectToThread(QThread* thread)
{
	connect(thread, &QThread::started, this, &QRunnable::start);
	connect(this, &QRunnable::finished, this, &QRunnable::quit);
	connect(this, &QRunnable::finished, thread, &QThread::quit);
}
void QRunnable::start()
{
	for(int i=minimum; i<maximum; i++)
	{
		for(int j=0; j<interval; j++)
		{
			qDebug()<<"QRunnable::valueChanged: "<<i<<", "<<j;
		}
		emit valueChanged(i);
	}
	emit finished();
}
void QRunnable::quit()
{
	emit valueChanged(minimum);
}