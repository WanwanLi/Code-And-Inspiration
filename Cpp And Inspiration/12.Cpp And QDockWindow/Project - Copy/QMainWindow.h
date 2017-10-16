#include <QLayout>
#include <QWidget>
#include <QMenuBar>
#include <QGroupBox>
#include <QComboBox>
#include <QApplication>
#include <QFormLayout>
#include <QProgressBar>

class QMainWindow : public QWidget
{
	Q_OBJECT
	public:	
	QMainWindow(QWidget* widget=0);

	private slots:
	void quit();

	private:
	QStringList itemList;
	QComboBox* comboBox;
	QProgressBar* progressBar;
	QMenuBar* newQMenuBar();
	QGridLayout* newQGridLayout();
	QVBoxLayout* newQVBoxLayout();
	QHBoxLayout* newQHBoxLayout();
	QFormLayout* newQFormLayout();
	enum{gridRows=3, pushButtonsLength=9};
	QGroupBox* newQGroupBox(QString title, QLayout* layout);
};
