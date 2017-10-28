#include <QObject>
#include <QThread>
#include <Eigen/Dense>
using namespace Eigen;

class QSolver : public QObject
{
	Q_OBJECT

	public slots:
	void start();
	void quit();
	void valueUpdated(int iteration, const VectorXd& x);

	signals:
	void started();
	void finished();
	void valueChanged(int value);

	public:
	int interval=400;
	int minimum=0;
	int maximum=100;
	QSolver(QThread* thread);
	void connectToThread(QThread* thread);
};
