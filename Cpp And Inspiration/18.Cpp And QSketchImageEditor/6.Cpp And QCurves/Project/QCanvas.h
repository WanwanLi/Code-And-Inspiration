#include <QPen>
#include <QColor>
#include <QImage>
#include <QWidget>
#include <QPaintEvent>
#include "QSketch.h"

class QCanvas : public QWidget
{
	Q_OBJECT
	public:
	QCanvas(QWidget* widget=0);
	bool loadImage(QString fileName);
	bool saveImage(QString fileName, const char* fileFormat);

	public slots:
	void clear();
	bool isModified();
	void resizeCanvas();

	private:
	QColor bgColor;
	QImage image;
	QSketch sketch;
	QWidget* widget;
	QPen pen, marker;
	void resizeImage();
	bool isMousePressed;
	bool isImageModified;
	void resizeWindow(int margin);
	void paintEvent(QPaintEvent* event);
	void mouseMoveEvent(QMouseEvent* event);
	void mousePressEvent(QMouseEvent* event);
	void mouseReleaseEvent(QMouseEvent* event);
};
