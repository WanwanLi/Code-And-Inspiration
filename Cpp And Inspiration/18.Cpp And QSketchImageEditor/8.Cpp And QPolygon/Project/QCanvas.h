#include <QPen>
#include <QColor>
#include <QImage>
#include <QWidget>
#include <QPaintEvent>
#include "QShape.h"

class QCanvas : public QWidget
{
	Q_OBJECT
	public:
	QShape shape;
	QCanvas(QWidget* widget=0);
	bool loadImage(QString fileName);
	bool saveImage(QString fileName, const char* fileFormat);

	public slots:
	void clear();
	bool isModified();
	void setEditable();
	void resizeCanvas();
	void createPolygon();
	void setAutoAligned();
	void switchLineCurveMode();

	private:
	QBrush brush;
	QColor bgColor;
	QImage image;
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
