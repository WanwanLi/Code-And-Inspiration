#include "QWindow.h"
#include <QLabel>
#include <QSlider>
#include <QTextEdit>
#include <QLineEdit>
#include <QSpinBox>
#include <QPushButton>
#include <QRadioButton>
#include <QDialogButtonBox>
#define OK QDialogButtonBox::Ok
#define CANCEL QDialogButtonBox::Cancel

QWindow::QWindow(QWidget* widget) : QWidget(widget)
{
	this->setWindowTitle("Basic Layout");
 	this->setMinimumSize(500, 500);
	this->itemList<<"item1"<<"item2"<<"item3"<<"item4";
	this->comboBox=new QComboBox();
	this->comboBox->addItems(itemList);
	this->progressBar=new QProgressBar();
	this->progressBar->setRange(0, 100);
	this->progressBar->setValue(50);
	this->setLayout(newQVBoxLayout());
}
QVBoxLayout* QWindow::newQVBoxLayout()
{
	QVBoxLayout* VBoxLayout=new QVBoxLayout();
	VBoxLayout->setMenuBar(newQMenuBar());
	VBoxLayout->addWidget(newQGroupBox("QHBoxLayout", newQHBoxLayout()));
	VBoxLayout->addWidget(newQGroupBox("QGridLayout", newQGridLayout()));
	VBoxLayout->addWidget(newQGroupBox("QFormLayout", newQFormLayout()));
	VBoxLayout->addWidget(new QTextEdit("QTextEdit2"));
	VBoxLayout->addWidget(new QDialogButtonBox(OK|CANCEL));
	return VBoxLayout;
}
QHBoxLayout* QWindow::newQHBoxLayout()
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
QGridLayout* QWindow::newQGridLayout()
{
	QGridLayout* gridLayout=new QGridLayout();
	for (int i = 0; i < gridRows; i++) 
	{
		gridLayout->addWidget(new QLabel(tr("Line %1:").arg(i + 1)), i + 1, 0);
	}
	gridLayout->addWidget(comboBox, 1, 1, 1, 1);
	gridLayout->addWidget(new QLineEdit("QLineEdit1"), 2, 1);
	gridLayout->addWidget(new QTextEdit("QTextEdit1"), 0, 2, 4, 1);
	gridLayout->addWidget(new QRadioButton("QRadioButton1"), 3, 1);
	gridLayout->setColumnStretch(0, 1);
	gridLayout->setColumnStretch(1, 10);
	gridLayout->setColumnStretch(2, 30);
	return gridLayout;
}
QFormLayout* QWindow::newQFormLayout()
{
	QFormLayout* formLayout = new QFormLayout();
	formLayout->addRow(new QLabel("Line 1:"), new QSlider(Qt::Horizontal, this));
	formLayout->addRow(new QLabel("Line 2:"), new QSpinBox());
	formLayout->addRow(new QLabel("Line 3:"), progressBar);
	return formLayout;
}
QMenuBar* QWindow::newQMenuBar()
{
	QMenu* menu=new QMenu("File", this);
	QAction* action=menu->addAction("Quit");
	connect(action, &QAction::triggered, this, &QWindow::quit);
	QMenuBar* menuBar = new QMenuBar();
	menuBar->addMenu(menu);
	return menuBar;
}
QGroupBox* QWindow::newQGroupBox(QString title, QLayout* layout)
{
	QGroupBox* groupBox=new QGroupBox();
	groupBox->setLayout(layout);
	groupBox->setTitle(title);
	return groupBox;
}
void QWindow::quit()
{
	QApplication::quit();
}
