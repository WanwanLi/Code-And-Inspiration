#include <QPen>
#include <QColor>
#include "QLayer.h"
#include <QWidget>
#include <QPaintEvent>

class QCanvas : public QWidget
{
	Q_OBJECT
	public:
	QCanvas(QWidget* widget=0);
	bool loadImage(QString fileName);
	bool saveImage(QString fileName, const char* fileFormat);

	public slots:
	bool isModified();
	void clearImage();
	void resizeImage();

	private:
	int lineWidth;
	QPen pen;
	QRect rect;
	QPoint point;
	QColor color;
	QColor bgColor;
	QImage* canvas;
	QWidget* widget;
	QImage background;
	bool isMousePressed;
	bool isImageModified;
	int minDistance=5;
	QLayer* selectedLayer;
	QVector<QLayer> layers;
	void drawLine(QPoint point);
	QLayer* layerOf(QPoint point);
	void resizeWindow(int margin);
	bool isClose(QPoint p0, QPoint p1);
	void paintEvent(QPaintEvent* event);
	void drawEditingTools(QPainter& painter);
	void mouseMoveEvent(QMouseEvent* event);
	void mousePressEvent(QMouseEvent* event);
	void mouseReleaseEvent(QMouseEvent* event);
};
