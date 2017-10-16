#include <QTextEdit>
#include <QMenuBar>
#include <QApplication>

class QWindow : public QWidget
{
	Q_OBJECT
	public:	
	QWindow(QWidget* widget=0);

	private slots:
	void openFile();
	void saveFile();

	private:
	QMenu* menu;
	QMenuBar* menuBar;
	QTextEdit* textEdit;
	QAction* action1;
	QAction* action2;
	QMenu* newQMenu(QMenuBar* menuBar, QString text);
	QAction* newQAction(QString text, QKeySequence::StandardKey shortcuts, QString statusTip);
};
