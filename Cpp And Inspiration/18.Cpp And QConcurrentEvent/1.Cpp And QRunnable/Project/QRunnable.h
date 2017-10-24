#include <QObject>
#include <QThread>

class QRunnable : public QObject
{
	Q_OBJECT

	public slots:
	void start();
	void quit();

	signals:
	void started();
	void finished();
	void valueChanged(int value);

	public:
	int interval=400;
	int minimum=0;
	int maximum=100;
	QRunnable(QThread* thread);
	void connectToThread(QThread* thread);
};
