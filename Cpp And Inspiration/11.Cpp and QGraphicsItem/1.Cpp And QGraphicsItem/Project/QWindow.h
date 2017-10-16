#include <QApplication>
#include <QGraphicsView>
#include <QGraphicsEffect>
#include <QLinearGradient>

class QWindow : public QGraphicsView
{
	Q_OBJECT
	public:
	QWindow(QWidget* widget=0);

	private:
	qreal w=600, h=400;
	QList<QGraphicsItem*> list;
	QGraphicsItem* graphicsItem;
	QGraphicsScene* graphicsScene;
	QLinearGradient* newQLinearGradient(QColor c1, QColor c2, qreal x1, qreal y1, qreal x2, qreal y2);
	QGraphicsItem* newQGraphicsTextItem(QString text, QPen pen, qreal blurRadius, qreal dx, qreal dy, qreal x, qreal y);
	QGraphicsItem* newQGraphicsPathItem(QPen pen, QBrush brush, qreal blurRadius, qreal dx, qreal dy, qreal x, qreal y);
	QGraphicsItem* newQGraphicsPolygonItem(QPen pen, QBrush brush, qreal blurRadius, qreal dx, qreal dy, qreal x, qreal y);
	QGraphicsItem* newQGraphicsPixmapItem(QString fileName, const char* format, qreal blurRadius, qreal dx, qreal dy, qreal x, qreal y);
	QGraphicsItem* newQGraphicsItem(QString shape, QPen pen, QBrush brush, qreal blurRadius, qreal dx, qreal dy, qreal x, qreal y, qreal w, qreal h);
};
