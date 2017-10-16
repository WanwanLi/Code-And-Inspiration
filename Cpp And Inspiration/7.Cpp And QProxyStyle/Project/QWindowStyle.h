#include <QProxyStyle>

class QWindowStyle : public QProxyStyle
{
	Q_OBJECT
	public:
	QWindowStyle(QString styleName, QString imageType, QVector<QColor> colorSet);
	void polish(QPalette& palette) override;
	void polish(QWidget* widget) override;
	void unpolish(QWidget* widget) override;

	private:
	QVector<QColor> colorSet;
	QString buttonImageName;
	QString styleName, imageType;
	QString backgroundImageName;
	void setBrush(QPalette& palette);
	void setTexture(QPalette& palette);
	enum{Base=0, Highlight=1, Palette=2, Foreground=3};
	void setPixmap(QPalette& palette, QPalette::ColorRole colorRole, QPixmap pixmap);
};