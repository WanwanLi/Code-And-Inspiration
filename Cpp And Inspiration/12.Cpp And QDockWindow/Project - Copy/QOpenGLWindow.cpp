#include <QDebug>
#include "QOpenGLWindow.h"

void QOpenGLWindow::initializeGL()
{
	initializeOpenGLFunctions();
	printOpenGLContextInformation();
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
}
void QOpenGLWindow::paintGL()
{
	glClear(GL_COLOR_BUFFER_BIT);
}
void QOpenGLWindow::resizeGL(int width, int height)
{
	qDebug() <<"width="<< width<<", height="<<height;
}
void QOpenGLWindow::teardownGL()
{

}
QOpenGLWindow::~QOpenGLWindow()
{
	makeCurrent();
	teardownGL();
}
void QOpenGLWindow::printOpenGLContextInformation()
{
	QString glVersion = reinterpret_cast<const char*>(glGetString(GL_VERSION)), glProfile;
	QString glType = (context()->isOpenGLES()) ? "OpenGL ES" : "OpenGL";
	#define CASE(c) case QSurfaceFormat::c: glProfile = #c; break
	switch (format().profile())
	{
		CASE(NoProfile);
		CASE(CoreProfile);
		CASE(CompatibilityProfile);
	}
	#undef CASE
	qDebug() << glType << glVersion << "(" << glProfile << ")";
}

