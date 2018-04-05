#include <QObject>
#include <QThread>
#include <Eigen/Dense>
using namespace Eigen;

class QOptimizer : public QObject
{
	Q_OBJECT

	public slots:
	void start();
	void quit();
	void valueChanged(int iteration, const VectorXd& variable);

	signals:
	void started();
	void finished();
	void setValue(int value);

	public:
	int minimum=0;
	int maximum=100;
	QOptimizer(QThread* thread);
	QString fileName="QOptimizer";
	void connectToThread(QThread* thread);
};
