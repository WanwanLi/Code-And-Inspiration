#include <QThread>
#include <QApplication>
#include <QBasicTimer>
#include <QPushButton>
#include <QProgressBar>

class QRunnable;
class QWindow : public QWidget
{
	Q_OBJECT
	public:
	QWindow(QWidget* widget=0);

	signals:
	void quit();

	private:	
	int counter;
	QThread* thread;
	QRunnable* runnable;
	QBasicTimer* basicTimer;
	QPushButton* pushButton;
	QProgressBar* progressBar;
	void timerEvent(QTimerEvent* event), clicked(bool clicked);
	QPushButton* newQPushButton(QString&, QString&, QWidget*, QPixmap&, QFont&, QIcon&);
};
