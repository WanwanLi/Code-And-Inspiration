#include <QApplication>
#include <QPushButton>
class QWindow : public QWidget
{
	Q_OBJECT
	public:
	QWindow(QWidget* widget=0);

	private:	
	int counter;
	QPushButton* pushButton;
	QPushButton* newQPushButton(QString&, QString&, QWidget*, QPixmap&, QFont&, QIcon&);
	void clicked(bool clicked);

	signals:
	void quit();
};
