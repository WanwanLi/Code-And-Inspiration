#include <QFile>
#include "QWindow.h"
#include "QOptimizer.h"
#include <QTextStream>
#include <QMessageBox>

QWindow::QWindow(QWidget* widget) : QWidget(widget)
{
 	this->counter=0;
	this->thread=new QThread();
 	this->setFixedSize(500, 450);
	this->textEdit=new QTextEdit(this);
	this->basicTimer=new QBasicTimer();
	this->basicTimer->start(500, this);
 	this->pushButton=newQPushButton
	(
		tr("Button"), tr("Tooltip"), 
		this, QPixmap("button.png"),
		QFont("Courier", 20, 1, true), QIcon("icon.gif")
	);
	this->pushButton->setGeometry(175, 50, 150, 45);
	this->textEdit->setGeometry(100, 125, 300, 200);
 	this->pushButton->setCheckable(true);
	this->optimizer=new QOptimizer(thread);
	this->progressBar=new QProgressBar(this);
	this->progressBar->setGeometry(100, 350, 320, 20);
	connect(optimizer, &QOptimizer::setValue, this, &QWindow::setValue);
	connect(pushButton, &QPushButton::clicked, this, &QWindow::clicked);
	connect(this, &QWindow::quit, QApplication::instance(), &QApplication::quit);
}
QPushButton* QWindow::newQPushButton(QString& name, QString& tooltip, QWidget* widget, QPixmap& pixmap, QFont& font, QIcon& icon)
{
	QPushButton* pushButton=new QPushButton(name, widget); QPalette palette; 
	palette.setBrush(pushButton->backgroundRole(), QBrush(pixmap));
	pushButton->setAutoFillBackground(true);
	pushButton->setPalette(palette);
	pushButton->setToolTip(tooltip);
	pushButton->setFont(font);
	pushButton->setIcon(icon);
	pushButton->setFlat(true);
	return pushButton;
}
void QWindow::setValue(int value)
{
	this->progressBar->setValue(value);
	QFile file(optimizer->fileName+"."+QString::number(value));
	if(!file.open(QIODevice::ReadOnly))return;
	QTextStream textStream(&file);
	this->textEdit->setText(textStream.readAll());
	file.close();  file.remove();
}
void QWindow::clicked(bool clicked)
{
	if(thread->isRunning())
	{
		QString message="QProgressBar is running!";
		QMessageBox::critical(this, "Error", message); 
	}
	else thread->start();
}
void QWindow::timerEvent(QTimerEvent* event)
{
	this->pushButton->setText(tr("Time:%1").arg(++this->counter));
	if(counter>=100)emit quit();
}
