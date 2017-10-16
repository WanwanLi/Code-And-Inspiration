#include <QPen>
#include <QColor>
#include <QImage>
#include <QWidget>
#include <QPaintEvent>
#include <QBasicTimer>
#include "QSketch.h"

class QCanvas : public QWidget
{
	Q_OBJECT
	public:
	QCanvas(QWidget* widget=0);
	bool loadImage(QString fileName);
	bool saveImage(QString fileName, const char* fileFormat);

	public slots:
	void undo();
	void clear();
	bool isModified();
	void resizeImage();

	private:
	QPen pen;
	QColor bgColor;
	QImage image;
	QSketch sketch;
	QWidget* widget;
	void clearImage();
	void drawSketch();
	bool isImageModified;
	QPoint point, position;
	QColor color(int index);
	QVector<QColor> colors;
	QBasicTimer basicTimer;
	void drawLine(QPoint point);
	void resizeWindow(int margin);
	void paintEvent(QPaintEvent* event);
	void timerEvent(QTimerEvent* event);
	void wheelEvent(QWheelEvent* event);
	void keyPressEvent(QKeyEvent* event);
	void mouseMoveEvent(QMouseEvent* event);
	void mousePressEvent(QMouseEvent* event);
	void mouseReleaseEvent(QMouseEvent* event);
	bool isLeftButtonPressed=false, isRightButtonPressed=false;
};
