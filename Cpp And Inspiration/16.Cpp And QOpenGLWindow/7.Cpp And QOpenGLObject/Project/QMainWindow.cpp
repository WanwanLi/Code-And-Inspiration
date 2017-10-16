#include <QLabel>
#include <QSlider>
#include <QLineEdit>
#include <QSpinBox>
#include <QPushButton>
#include <QRadioButton>
#include "QMainWindow.h"
#include "QOpenGLWindow.h"
#include <QDialogButtonBox>
#define OK QDialogButtonBox::Ok
#define CANCEL QDialogButtonBox::Cancel

QMainWindow::QMainWindow(QWidget* widget) : QWidget(widget)
{
	this->setWindowTitle("QOpenGLWindow");
 	this->setMinimumSize(850, 550);
	this->itemList<<"item1"<<"item2";
	this->itemList<<"item3"<<"item4";
	this->comboBox=new QComboBox();
	this->comboBox->addItems(itemList);
	this->progressBar=new QProgressBar();
	this->progressBar->setRange(0, 100);
	this->progressBar->setValue(50);
	this->setLayout(newQVBoxLayout());
}
QVBoxLayout* QMainWindow::newQVBoxLayout()
{
	QVBoxLayout* VBoxLayout=new QVBoxLayout();
	VBoxLayout->setMenuBar(newQMenuBar());
	VBoxLayout->addWidget(newQGroupBox("QGridLayout", newQGridLayout()));
	VBoxLayout->addWidget(new QDialogButtonBox(OK|CANCEL));
	return VBoxLayout;
}
QHBoxLayout* QMainWindow::newQHBoxLayout()
{
	QHBoxLayout* HBoxLayout=new QHBoxLayout();
	for(int i=0; i<pushButtonsLength; i++) 
	{
		QPushButton* pushButton=new QPushButton();
		pushButton->setText(tr("QPushButton%1").arg(i + 1));
		if(i==pushButtonsLength/2)pushButton->setFlat(true);
		else if(i%2==1)
		{
			pushButton->setCheckable(true); 
			pushButton->setChecked(true);
		}
		HBoxLayout->addWidget(pushButton);
	}
	return HBoxLayout;
}
QGridLayout* QMainWindow::newQGridLayout()
{
	QGridLayout* gridLayout=new QGridLayout();
	QGroupBox* HBox=newQGroupBox("QHBoxLayout", newQHBoxLayout());
	QGroupBox* form=newQGroupBox("QFormLayout", newQFormLayout());
	for (int i=1; i<=gridRows; i++) 
	{
		gridLayout->addWidget(new QLabel(tr("Line %1:").arg(i)), i + 1, 0);
	}
	gridLayout->addWidget(comboBox, 2, 1, 1, 1);
	gridLayout->addWidget(new QLineEdit("QLineEdit1"), 3, 1);
	gridLayout->addWidget(new QRadioButton("QRadioButton1"), 4, 1);
	gridLayout->addWidget(new QOpenGLWindow(), 1, 2, 4, 1);
	gridLayout->addWidget(HBox, 0, 0, 1, 4);
	gridLayout->addWidget(form, 1, 3, 3, 1);
	gridLayout->setRowStretch(0, 10);
	gridLayout->setRowStretch(1, 90);
	gridLayout->setColumnStretch(0, 1);
	gridLayout->setColumnStretch(1, 8);
	gridLayout->setColumnStretch(2, 90);
	gridLayout->setColumnStretch(3, 1);
	return gridLayout;
}
QFormLayout* QMainWindow::newQFormLayout()
{
	QFormLayout* formLayout = new QFormLayout();
	formLayout->addRow(new QLabel("Line 1:"), new QSlider(Qt::Horizontal, this));
	formLayout->addRow(new QLabel("Line 2:"), new QSpinBox());
	formLayout->addRow(new QLabel("Line 3:"), progressBar);
	return formLayout;
}
QMenuBar* QMainWindow::newQMenuBar()
{
	QMenu* menu=new QMenu("File", this);
	QAction* action=menu->addAction("Quit");
	connect(action, &QAction::triggered, this, &QMainWindow::quit);
	QMenuBar* menuBar = new QMenuBar();
	menuBar->addMenu(menu);
	return menuBar;
}
QGroupBox* QMainWindow::newQGroupBox(QString title, QLayout* layout)
{
	QGroupBox* groupBox=new QGroupBox();
	groupBox->setLayout(layout);
	groupBox->setTitle(title);
	return groupBox;
}
void QMainWindow::quit()
{
	QApplication::quit();
}
