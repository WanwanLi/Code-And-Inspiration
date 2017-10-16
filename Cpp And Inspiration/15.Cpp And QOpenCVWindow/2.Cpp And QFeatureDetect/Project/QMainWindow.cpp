#include <QSlider>
#include <QSpinBox>
#include <QComboBox>
#include "QMainWindow.h"
#define OK QDialogButtonBox::Ok
#define CANCEL QDialogButtonBox::Cancel

QMainWindow::QMainWindow(QWidget* widget) : QWidget(widget)
{
 	this->setMinimumSize(800, 600);
	this->setWindowTitle("QOpenCVWindow");
	this->openCVWindow=new QOpenCVWindow();
	this->dialogButtonBox=new QDialogButtonBox(OK|CANCEL);
	this->setLayout(newQVBoxLayout());
}
QVBoxLayout* QMainWindow::newQVBoxLayout()
{
	QVBoxLayout* VBoxLayout=new QVBoxLayout();
	VBoxLayout->setMenuBar(newQMenuBar());
	VBoxLayout->addWidget(newQGroupBox("QGridLayout", newQGridLayout()));
	VBoxLayout->addWidget(dialogButtonBox);
	return VBoxLayout;
}
QHBoxLayout* QMainWindow::newQHBoxLayout()
{
	QHBoxLayout* HBoxLayout=new QHBoxLayout();
	for(int i=0; i<pushButtonsLength; i++) 
	{
		this->pushButtons[i]=new QPushButton(tr("QPushButton[%1]").arg(i + 1));
		HBoxLayout->addWidget(pushButtons[i]);
	}
	return HBoxLayout;
}
QGridLayout* QMainWindow::newQGridLayout()
{
	QGridLayout* gridLayout=new QGridLayout();
	QGroupBox* HBox=newQGroupBox("QHBoxLayout", newQHBoxLayout());
	QGroupBox* form=newQGroupBox("QFormLayout", newQFormLayout());
	for (int i = 0; i < gridRows; i++) 
	{
		this->labels[i] = new QLabel(tr("Line %1:").arg(i + 1));
		this->lineEdits[i]=new QLineEdit();
		gridLayout->addWidget(labels[i], i + 2, 0);
		gridLayout->addWidget(lineEdits[i], i + 2, 1);
	}
	gridLayout->addWidget(openCVWindow, 1, 2, 4, 1);
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
	QFormLayout *formLayout = new QFormLayout();
	formLayout->addRow(new QLabel("Line 1:"), new QSlider());
	formLayout->addRow(new QLabel("Line 2:"), new QComboBox());
	formLayout->addRow(new QLabel("Line 3:"), new QSpinBox());
	return formLayout;
}
QMenuBar* QMainWindow::newQMenuBar()
{
	QMenu* menu=new QMenu("File", this);
	QAction* action=menu->addAction("Quit");
	connect(action, &QAction::triggered, this,  &QMainWindow::quit);
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
