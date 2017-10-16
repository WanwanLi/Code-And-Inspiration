#include <QLabel>
#include <QLayout>
#include <QWidget>
#include <QTextEdit>
#include <QMenuBar>
#include <QGroupBox>
#include <QComboBox>
#include <QApplication>
#include <QFormLayout>
#include <QProgressBar>
#include <QPushButton>
#include "QLanguages.h"

class QWindow : public QWidget
{
	Q_OBJECT
	public:	
	QWindow(QWidget* widget=0);

	private slots:
	void quit();
	void activated1(QString text);
	void activated2(QString text);

	private:
	QList<QPushButton*> pushButtonList;
	QList<QComboBox*> comboBoxList;
	QList<QGroupBox*> groupBoxList;
	QList<QTextEdit*> textEditList;
	QList<QAction*> actionList;
	QList<QMenu*> menuList;
	QList<QLabel*> labelList;
	QStringList itemList1;
	QStringList itemList2;
	void getChildrenLists();
	void updateLanguage();
	QLanguages languages;
	QComboBox* comboBox1;
	QComboBox* comboBox2;
	QProgressBar* progressBar;
	QMenuBar* newQMenuBar();
	QGridLayout* newQGridLayout();
	QVBoxLayout* newQVBoxLayout();
	QHBoxLayout* newQHBoxLayout();
	QFormLayout* newQFormLayout();
	QVector<QColor> getWoodColorSet();
	QVector<QColor> getSnowColorSet();
	QVector<QColor> getGrassColorSet();
	QVector<QColor> getFlameColorSet();
	enum{gridRows=3, pushButtonsLength=5};
	QGroupBox* newQGroupBox(QString title, QLayout* layout);
};
