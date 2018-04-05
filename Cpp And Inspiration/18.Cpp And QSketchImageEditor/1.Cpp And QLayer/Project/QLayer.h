#include <QImage>
#include <QVector>
#include <QPainter>

class QLayer : public QImage
{
	public:
	QImage image();
	QLayer():QImage(){}
	QVector<QPoint> ctrlPoints;
	bool contains(QPoint point);
	bool load(QString fileName);
	void reset(QPainter& painter);
	void translate(QPainter& painter);

	private:
	QPoint translation;
	QMatrix transformation;
};
