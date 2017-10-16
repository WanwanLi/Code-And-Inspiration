#include "QWindowStyle.h"
#include "QWindow.h"
#include <QSlider>
#include <QSpinBox>
#include <QStyleFactory>
#include <QMessageBox>
#include <QDialogButtonBox>
#define Tr languages.translate
#define En languages.getEnglish
#define OK QDialogButtonBox::Ok
#define CANCEL QDialogButtonBox::Cancel

QWindow::QWindow(QWidget* widget) : QWidget(widget)
{
 	this->setMinimumSize(500, 500);
	this->itemList1<<"wood"<<"snow";
	this->itemList1<<"grass"<<"flame";
	this->itemList1<<QStyleFactory::keys();
	this->itemList2<<"chinese"<<"hindi";
	this->itemList2<<"japanese"<<"korean";
	this->itemList2<<"thai"<<"vietnamese";
	this->itemList2<<"french"<<"english";
	this->comboBox1=new QComboBox();
	this->comboBox1->addItems(itemList1);
	this->comboBox2=new QComboBox();
	this->comboBox2->addItems(itemList2);
	this->progressBar=new QProgressBar();
	this->progressBar->setRange(0, 100);
	this->progressBar->setValue(50);
	this->setLayout(newQVBoxLayout());
	this->getChildrenLists();
	connect(comboBox1, SIGNAL(activated(QString)), this, SLOT(activated1(QString)));
	connect(comboBox2, SIGNAL(activated(QString)), this, SLOT(activated2(QString)));
}
QVBoxLayout* QWindow::newQVBoxLayout()
{
	QVBoxLayout* VBoxLayout=new QVBoxLayout();
	VBoxLayout->setMenuBar(newQMenuBar());
	VBoxLayout->addWidget(newQGroupBox("QHBoxLayout", newQHBoxLayout()));
	VBoxLayout->addWidget(newQGroupBox("QGridLayout", newQGridLayout()));
	VBoxLayout->addWidget(newQGroupBox("QFormLayout", newQFormLayout()));
	VBoxLayout->addWidget(new QTextEdit("QTextEdit.2"));
	VBoxLayout->addWidget(new QDialogButtonBox(OK|CANCEL));
	return VBoxLayout;
}
QHBoxLayout* QWindow::newQHBoxLayout()
{
	QHBoxLayout* HBoxLayout=new QHBoxLayout();
	for(int i=0; i<pushButtonsLength; i++) 
	{
		QPushButton* pushButton=new QPushButton();
		pushButton->setText(tr("QPushButton.%1").arg(i + 1));
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
		gridLayout->addWidget(new QLabel(tr("Line.%1:").arg(i + 1)), i + 1, 0);
	}
	gridLayout->addWidget(comboBox1, 1, 1, 1, 1);
	gridLayout->addWidget(comboBox2, 2, 1);
	gridLayout->addWidget(new QSpinBox(), 3, 1);
	gridLayout->addWidget(new QTextEdit("QTextEdit.1"), 0, 2, 4, 1);
	gridLayout->setColumnStretch(0, 1);
	gridLayout->setColumnStretch(1, 10);
	gridLayout->setColumnStretch(2, 30);
	return gridLayout;
}
QFormLayout* QWindow::newQFormLayout()
{
	QFormLayout* formLayout = new QFormLayout();
	formLayout->addRow(new QLabel("Line.1:"), new QSlider(Qt::Horizontal, this));
	formLayout->addRow(new QLabel("Line.2:"), new QSpinBox());
	formLayout->addRow(new QLabel("Line.3:"), progressBar);
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
void QWindow::activated1(QString text)
{
	QStyle* style; text=En(text);
	if(text=="wood")style=new QWindowStyle(text, "png", getWoodColorSet());
	else if(text=="snow")style=new QWindowStyle(text, "png", getSnowColorSet());
	else if(text=="grass")style=new QWindowStyle(text, "png", getGrassColorSet());
	else if(text=="flame")style=new QWindowStyle(text, "png", getFlameColorSet());
	else style=QStyleFactory::create(text);
	QApplication::setStyle(style);
}
void QWindow::activated2(QString text)
{
	QStyle* style; text=En(text);
	if(text=="english")this->languages.loadEnglish();
	else if(!this->languages.load(text))
	{
		QMessageBox::critical(this, "Error", text+tr(": This language file does not exists!"));
	}
	this->updateLanguage();
}
void QWindow::getChildrenLists()
{
	this->labelList=this->findChildren<QLabel*>();
	this->menuList=this->findChildren<QMenu*>();
	this->actionList=this->findChildren<QAction*>();
	this->textEditList=this->findChildren<QTextEdit*>();
	this->groupBoxList=this->findChildren<QGroupBox*>();
	this->comboBoxList=this->findChildren<QComboBox*>();
	this->pushButtonList=this->findChildren<QPushButton*>();
}
void QWindow::updateLanguage()
{
	this->setWindowTitle(Tr("Languages"));
	foreach(QLabel* label, labelList)
	{
		QStringList stringList=label->text().split(".");
		if(stringList.length()==1)label->setText(Tr(En(stringList[0])));
		else label->setText(Tr(En(stringList[0]))+tr(".")+stringList[1]);
	}
	foreach(QMenu* menu, menuList)
	{
		menu->setTitle(Tr(En(menu->title())));
	}
	foreach(QAction* action, actionList)
	{
		action->setText(Tr(En(action->text())));
	}
	foreach(QTextEdit* textEdit, textEditList)
	{
		QStringList stringList=textEdit->toPlainText().split(".");
		if(stringList.length()==1)textEdit->setText(Tr(En(stringList[0])));
		else textEdit->setText(Tr(En(stringList[0]))+tr(".")+stringList[1]);
	}
	foreach(QGroupBox* groupBox, groupBoxList)
	{
		groupBox->setTitle(Tr(En(groupBox->title())));
	}
	foreach(QComboBox* comboBox, comboBoxList)
	{
		for(int i=0; i<comboBox->count(); i++)
		{
			comboBox->setItemText(i, Tr(En(comboBox->itemText(i))));
		}
	}
	foreach(QPushButton* pushButton, pushButtonList)
	{
		QStringList stringList=pushButton->text().split(".");
		if(stringList.length()==1)pushButton->setText(Tr(En(stringList[0])));
		else pushButton->setText(Tr(En(stringList[0]))+tr(".")+stringList[1]);
	}
}
QVector<QColor> QWindow::getWoodColorSet()
{
	QVector<QColor> colorSet;
	QColor Palette=QColor(212, 140, 95);
	QColor Base=QColor(236, 182, 120);
	QColor Highlight=Qt::darkGreen;
	QColor BrightText=Qt::white;
	QColor Foreground=Qt::black;
	colorSet<<Base<<Highlight;
	colorSet<<Palette<<Foreground;
	return colorSet;
}
QVector<QColor> QWindow::getSnowColorSet()
{
	QVector<QColor> colorSet;
	QColor Palette=Qt::darkCyan;
	QColor Base=QColor(220, 255, 255);
	QColor Highlight=QColor(0, 95, 95);
	QColor BrightText=Qt::white;
	QColor Foreground=Qt::black;
	colorSet<<Base<<Highlight;
	colorSet<<Palette<<Foreground;
	return colorSet;
}
QVector<QColor> QWindow::getGrassColorSet()
{
	QVector<QColor> colorSet;
	QColor Palette=Qt::darkGreen;
	QColor Base=QColor(0, 50, 0);
	QColor Highlight=QColor(150, 220, 0);
	QColor BrightText=QColor(100, 100, 0);
	QColor Foreground=QColor(220, 250, 100);
	colorSet<<Base<<Highlight;
	colorSet<<Palette<<Foreground;
	return colorSet;
}
QVector<QColor> QWindow::getFlameColorSet()
{
	QVector<QColor> colorSet;
	QColor Palette=Qt::darkRed;
	QColor Base=QColor(50, 0, 0);
	QColor Highlight=QColor(200, 100, 0);
	QColor BrightText=Qt::white;
	QColor Foreground=QColor(250, 200, 150);
	colorSet<<Base<<Highlight;
	colorSet<<Palette<<Foreground;
	return colorSet;
}
void QWindow::quit()
{
	QApplication::quit();
}
