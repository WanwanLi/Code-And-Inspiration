#include <QLayout>
#include <QWidget>
#include <QMenuBar>
#include <QGroupBox>
#include <QComboBox>
#include <QApplication>
#include <QFormLayout>
#include <QProgressBar>
#include <QMainWindow>

class QDockWindow : public QMainWindow
{
	Q_OBJECT
	public:	
	QDockWindow(QWidget* widget=0);

	private slots:
	void quit();

	private:
	void addMenu();
	QStringList itemList;
	QComboBox* comboBox;
	QProgressBar* progressBar;
	QWidget* newQCentralWidget();
	QGridLayout* newQGridLayout();
	QHBoxLayout* newQHBoxLayout();
	QFormLayout* newQFormLayout();
	enum{gridRows=3, pushButtonsLength=9};
	QGroupBox* newQGroupBox(QString title, QLayout* layout);
};
