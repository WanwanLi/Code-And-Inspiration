#include <QLabel>
#include <QSlider>
#include <QWidget>
#include <QFileDialog>
#include <QBoxLayout>
#include <QApplication>
#include <QPushButton>
#include <QMediaPlayer>
#include <QVideoWidget>

class QWindow : public QWidget
{
	Q_OBJECT
	public:	
	QWindow(QWidget* widget=0);

	private slots:
	void play();
	void open();
	void error();
	void sliderMoved(int sliderPosition);
	void positionChanged(qint64 position);
	void durationChanged(qint64 duration);

	private:
	QLabel* label;
	QSlider* slider;
	QPushButton* playButton;
	QPushButton* openButton;
	QMediaPlayer* mediaPlayer;
	QVideoWidget* videoWidget;
	QBoxLayout* newQBoxLayout();
	QFileDialog* newQFileDialog(QString windowTitle, QString nameFilter);
};
