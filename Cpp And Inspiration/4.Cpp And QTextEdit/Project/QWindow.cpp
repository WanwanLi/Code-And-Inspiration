#include "QWindow.h"
#include <QFileDialog>
#include <QTextStream>
#include <QMessageBox>

QWindow::QWindow(QWidget* widget) : QWidget(widget)
{
 	this->setMinimumSize(500, 400);
	this->textEdit=new QTextEdit(this);
 	this->textEdit->setGeometry(5, 20, 500-10, 400-40);
	this->menuBar=new QMenuBar(this);
	this->action1=newQAction("Open", QKeySequence::Open, "Open file.");
	this->action2=newQAction("Save", QKeySequence::Save, "Save file.");
	this->menu=newQMenu(menuBar, "File");
	connect(action1, &QAction::triggered, this, &QWindow::openFile);
	connect(action2, &QAction::triggered, this, &QWindow::saveFile);
}
QAction* QWindow::newQAction(QString text, QKeySequence::StandardKey shortcuts, QString statusTip)
{
	QAction* action=new QAction(text, this);
	action->setShortcuts(shortcuts);
	action->setStatusTip(statusTip);
	return action;
}
QMenu* QWindow::newQMenu(QMenuBar* menuBar, QString text)
{	
	QMenu* menu=menuBar->addMenu(text);
	menu->addAction(action1);
	menu->addSeparator();
	menu->addAction(action2);
	return menu;
}
void QWindow::openFile()
{
	QString fileName = QFileDialog::getOpenFileName(this, "Open File", QString(), "Text Files (*.txt)");
	QFile file(fileName);
	if(!file.open(QIODevice::ReadOnly)){QMessageBox::critical(this, "Error", "Can not open file"); return;}
	QTextStream textStream(&file);
	this->textEdit->setText(textStream.readAll());
	file.close();
}
void QWindow::saveFile()
{
	QString fileName = QFileDialog::getSaveFileName(this, "Save File", QString(), "Text Files (*.txt)");
	QFile file(fileName);
	if(!file.open(QIODevice::WriteOnly)){QMessageBox::critical(this, "Error", "Can not save file"); return;}
	QTextStream textStream(&file);
	textStream<<textEdit->toPlainText().toUpper();
	file.close();
}
