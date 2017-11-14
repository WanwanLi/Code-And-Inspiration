#include <QThread>
#include <QTextEdit>
#include <QApplication>
#include <QBasicTimer>
#include <QPushButton>
#include <QProgressBar>

class QOptimizer;
class QWindow : public QWidget
{
	Q_OBJECT
	public:
	QWindow(QWidget* widget=0);

	signals:
	void quit();

	private slots:
	void setValue(int value);

	private:	
	int counter;
	QThread* thread;
	QTextEdit* textEdit;
	QOptimizer* optimizer;
	QBasicTimer* basicTimer;
	QPushButton* pushButton;
	QProgressBar* progressBar;
	void timerEvent(QTimerEvent* event), clicked(bool clicked);
	QPushButton* newQPushButton(QString&, QString&, QWidget*, QPixmap&, QFont&, QIcon&);
};
