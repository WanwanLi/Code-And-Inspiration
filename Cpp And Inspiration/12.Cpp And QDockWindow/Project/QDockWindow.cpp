#include <QLabel>
#include <QSlider>
#include <QLineEdit>
#include <QSpinBox>
#include <QPushButton>
#include <QRadioButton>
#include "QDockWindow.h"
#include <QDockWidget>
#include <QTextEdit>
#include "QOpenGLWindow.h"
#include <QDialogButtonBox>
#define OK QDialogButtonBox::Ok
#define CANCEL QDialogButtonBox::Cancel

QDockWindow::QDockWindow(QWidget* widget) : QMainWindow(widget)
{
	this->setWindowTitle("QDockWindow");
 	this->setMinimumSize(850, 550);
	this->itemList<<"item1"<<"item2";
	this->itemList<<"item3"<<"item4";
	this->comboBox=new QComboBox();
	this->comboBox->addItems(itemList);
	this->progressBar=new QProgressBar();
	this->progressBar->setRange(0, 100);
	this->progressBar->setValue(50);
	this->setCentralWidget(newQCentralWidget());
	this->addMenu();

QDockWidget* dw = new QDockWidget();
    dw->setObjectName("RightDockWidget");
    dw->setWindowTitle("RightDockWidget");
    dw->setWidget(new QTextEdit());
	this->addDockWidget(Qt::RightDockWidgetArea, dw);

 dw = new QDockWidget();
    dw->setObjectName("TopDockWidget");
    dw->setWindowTitle("TopDockWidget");
    dw->setWidget(new QTextEdit());
	 dw->setMaximumSize(5000, 100);
	this->addDockWidget(Qt::TopDockWidgetArea, dw);

 dw = new QDockWidget();
    dw->setObjectName("BottomDockWidget");
    dw->setWindowTitle("BottomDockWidget");
    dw->setWidget(new QTextEdit());
	this->addDockWidget(Qt::BottomDockWidgetArea, dw);
}
QWidget* QDockWindow::newQCentralWidget()
{
	QWidget* centralWidget=new QWidget();
	QVBoxLayout* VBoxLayout=new QVBoxLayout();
	VBoxLayout->addWidget(newQGroupBox("QGridLayout", newQGridLayout()));
	VBoxLayout->addWidget(new QDialogButtonBox(OK|CANCEL));
	centralWidget->setLayout(VBoxLayout);
	return centralWidget;
}
QHBoxLayout* QDockWindow::newQHBoxLayout()
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
QGridLayout* QDockWindow::newQGridLayout()
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
	//gridLayout->addWidget(new QOpenGLWindow(), 1, 2, 4, 1);
	gridLayout->addWidget(new QTextEdit(), 1, 2, 4, 1);
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
QFormLayout* QDockWindow::newQFormLayout()
{
	QFormLayout* formLayout = new QFormLayout();
	formLayout->addRow(new QLabel("Line 1:"), new QSlider(Qt::Horizontal, this));
	formLayout->addRow(new QLabel("Line 2:"), new QSpinBox());
	formLayout->addRow(new QLabel("Line 3:"), progressBar);
	return formLayout;
}
void QDockWindow::addMenu()
{
	QMenu* menu=new QMenu("File", this);
    QDockWidget* dw = new QDockWidget();
    dw->setObjectName("LeftDockWidget");
    dw->setWindowTitle("LeftDockWidget");
	dw->setWidget(new QTextEdit());
	this->addDockWidget(Qt::LeftDockWidgetArea, dw);
	menu->addAction(dw->toggleViewAction());
	QAction* action=menu->addAction("Quit");
	connect(action, &QAction::triggered, this, &QDockWindow::quit);
	this->menuBar()->addMenu(menu);
}
QGroupBox* QDockWindow::newQGroupBox(QString title, QLayout* layout)
{
	QGroupBox* groupBox=new QGroupBox();
	groupBox->setLayout(layout);
	groupBox->setTitle(title);
	return groupBox;
}
void QDockWindow::quit()
{
	QApplication::quit();
}
