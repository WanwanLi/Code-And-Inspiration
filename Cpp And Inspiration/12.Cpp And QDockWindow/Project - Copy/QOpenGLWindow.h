#include <QOpenGLWidget>
#include <QOpenGLFunctions>

class QOpenGLWindow : public QOpenGLWidget, protected QOpenGLFunctions
{
	Q_OBJECT
	public:
	void initializeGL();
	void resizeGL(int width, int height);
	void paintGL();
	void teardownGL();
	~QOpenGLWindow();

	private:
	void printOpenGLContextInformation();
};
