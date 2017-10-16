#include "QWindow.h"

QWindow::QWindow(QWidget* widget) : QMainWindow(widget)
{
 	this->setMinimumSize(200, 300);
	this->treeView=new QTreeView(this);
	this->setCentralWidget(treeView);
	this->treeView->setModel(newQStandardItemModel());
	this->treeView->expandAll();
	connect(treeView->selectionModel(), &QItemSelectionModel::selectionChanged, this, &QWindow::selectionChanged);
}
QStandardItemModel* QWindow::newQStandardItemModel()
{
	QStandardItemModel* standardItemModel=new QStandardItemModel();
	QStandardItem* standardItem=standardItemModel->invisibleRootItem();
	QStandardItem* standardItem1=new QStandardItem("North America");
	QStandardItem* standardItem2=new QStandardItem("Canada");
	QStandardItem* standardItem3=new QStandardItem("United States");
	QStandardItem* standardItem4=new QStandardItem("Boston");
	QStandardItem* standardItem5=new QStandardItem("Europe");
	QStandardItem* standardItem6=new QStandardItem("Italy");
	QStandardItem* standardItem7=new QStandardItem("Rome");
	QStandardItem* standardItem8=new QStandardItem("Verona");
	standardItem->appendRow(standardItem1);
	standardItem1->appendRow(standardItem2);
	standardItem1->appendRow(standardItem3);
	standardItem3->appendRow(standardItem4);
	standardItem->appendRow(standardItem5);
	standardItem5->appendRow(standardItem6);
	standardItem6->appendRow(standardItem7);
	standardItem6->appendRow(standardItem8);
	return standardItemModel;
}
void QWindow::selectionChanged(QItemSelection newSelection, QItemSelection oldSelection)
{
	const QModelIndex modelIndex=treeView->selectionModel()->currentIndex();
	QString text=modelIndex.data(Qt::DisplayRole).toString();
	QModelIndex index=modelIndex; int level=1; 
	while(index.parent()!=QModelIndex())
	{
		index=index.parent();
		level++;
	}
	QString windowTitle=QString("%1, Level %2").arg(text).arg(level);
    	this->setWindowTitle(windowTitle);
}
