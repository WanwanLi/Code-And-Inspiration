#include <QTreeView>
#include <QApplication>
#include <QMainWindow>
#include <QStandardItemModel>

class QWindow : public QMainWindow
{
	Q_OBJECT
	public:	
	QWindow(QWidget* widget=0);

	private slots:
	void selectionChanged(QItemSelection newSelection, QItemSelection oldSelection);

	private:
	QTreeView* treeView;
	QStandardItem* standardItem1;
	QStandardItem* standardItem2;
	QStandardItem* standardItem3;
	QStandardItem* standardItem4;
	QStandardItem* standardItem5;
	QStandardItem* standardItem6;
	QStandardItem* standardItem7;
	QStandardItem* standardItem8;
	QStandardItemModel* newQStandardItemModel();
};
