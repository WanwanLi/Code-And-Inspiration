#include <QSize>
#include <QVector3D>
#include <QPainterPath>

class QCurves : public QPainterPath
{
	public:
	bool load(QString fileName);
	bool save(QString fileName);
	QVector3D right,up,forward;
	double viewDistance;
	void optimize();
	void update();
	void create();
	QSize size();
	void clear();

	private:
	int width, height;
	QSize canvasSize;
	enum{MOVE, LINE, CUBIC};
	QVector<int> joints, indices;
	QVector<double> curves, planes;
	QVector3D project(double x, double y, double z);
};
