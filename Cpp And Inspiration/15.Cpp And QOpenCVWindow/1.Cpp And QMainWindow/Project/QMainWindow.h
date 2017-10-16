#include <QLabel>
#include <QLayout>
#include <QWidget>
#include <QLineEdit>
#include <QMenuBar>
#include <QGroupBox>
#include <QApplication>
#include <QPushButton>
#include <QFormLayout>
#include <QDialogButtonBox>
#include "QOpenCVWindow.h"

class QMainWindow : public QWidget
{
	Q_OBJECT
	public:	
	QMainWindow(QWidget* widget=0);

	private slots:
	void quit();

	private:
	QOpenCVWindow* openCVWindow;
	QDialogButtonBox* dialogButtonBox;
	enum { gridRows = 3, pushButtonsLength = 6 };
	QPushButton* pushButtons[pushButtonsLength];
	QLineEdit* lineEdits[gridRows]; QLabel* labels[gridRows];
	QGroupBox* newQGroupBox(QString title, QLayout* layout);
	QFormLayout* newQFormLayout();
	QHBoxLayout* newQHBoxLayout();
	QVBoxLayout* newQVBoxLayout();
	QGridLayout* newQGridLayout();
	QMenuBar* newQMenuBar();
};
