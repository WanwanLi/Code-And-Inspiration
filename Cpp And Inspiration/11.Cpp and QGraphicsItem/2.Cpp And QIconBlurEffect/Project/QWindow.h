#include <QKeyEvent>
#include <QApplication>
#include <QMouseEvent>
#include <QGraphicsView>
#include <QPropertyAnimation>
#include <QGraphicsPixmapItem>

class QWindow : public QGraphicsView
{
	Q_OBJECT
	Q_PROPERTY(qreal frame READ frame WRITE updateScene)

	public:
	const qreal frame();
	void updateScene(qreal index);
	void keyPressEvent(QKeyEvent* event);
	void mousePressEvent(QMouseEvent* event);
	QWindow(QWidget* widget=0);

	private:	
	qreal index;
	qreal w=500, h=400;
	qreal s=0.7, dx=-30, dy=-30;
	QStringList getImageList(QString dir);
	QPropertyAnimation* propertyAnimation;
	QList<QGraphicsPixmapItem*> pixmapList;
	QGraphicsScene* newQGraphicsScene(qreal x, qreal y, qreal w, qreal h);
};
