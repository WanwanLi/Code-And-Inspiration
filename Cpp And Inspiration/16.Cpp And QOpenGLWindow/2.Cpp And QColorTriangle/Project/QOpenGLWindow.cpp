#include "QOpenGLWindow.h"

GLuint glTupleSize=3;
GLuint glLength=3;
GLfloat glColors[]=
{
	1.0f, 0.0f, 0.0f,
	0.0f, 1.0f, 0.0f,
	0.0f, 0.0f, 1.0f
};
GLfloat glPositions[]=
{
	0.00f,  0.75f, 1.0f,
	0.75f, -0.75f, 1.0f,
	-0.75f, -0.75f, 1.0f
};
void QOpenGLWindow::initializeGL()
{
	initializeOpenGLFunctions();
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	glProgram=newQOpenGLShaderProgram("shader.v", "shader.f");
	glProgram->enableAttributeArray("color");
	glProgram->enableAttributeArray("position");
	glColorBuffer=newQOpenGLBuffer(glColors, glTupleSize, glLength); 
	glPositionBuffer=newQOpenGLBuffer(glPositions, glTupleSize, glLength); 
}
void QOpenGLWindow::paintGL()
{
	glClear(GL_COLOR_BUFFER_BIT);
	glBindAttributeBuffer("position", glPositionBuffer, glTupleSize);
	glBindAttributeBuffer("color", glColorBuffer, glTupleSize);
	glDrawArrays(GL_TRIANGLES, 0, glLength);
}
void QOpenGLWindow::resizeGL(int width, int height)
{
	qDebug() <<"width="<< width<<", height="<<height;
}
void QOpenGLWindow::teardownGL()
{
	delete glProgram;
	delete glColorBuffer;
	delete glPositionBuffer;
}
QOpenGLWindow::~QOpenGLWindow()
{
	makeCurrent();
	teardownGL();
}
QOpenGLShaderProgram* QOpenGLWindow::newQOpenGLShaderProgram(QString vShader, QString fShader)
{
	QOpenGLShaderProgram* program=new QOpenGLShaderProgram();
	program->addShaderFromSourceFile(QOpenGLShader::Vertex, vShader);
	program->addShaderFromSourceFile(QOpenGLShader::Fragment, fShader);
	program->link(); program->bind(); return program;
}
QOpenGLBuffer* QOpenGLWindow::newQOpenGLBuffer(GLfloat* buffer, GLuint tupleSize, GLuint length)
{
	QOpenGLBuffer* openGLBuffer=new QOpenGLBuffer(); 
	openGLBuffer->setUsagePattern(QOpenGLBuffer::StaticDraw); 
	openGLBuffer->create(); openGLBuffer->bind();
	openGLBuffer->allocate(buffer, sizeof(GLfloat)*tupleSize*length);
	openGLBuffer->release(); return openGLBuffer;
}
void QOpenGLWindow::glBindAttributeBuffer(const char* name, QOpenGLBuffer* glBuffer, GLuint tupleSize)
{
	glBuffer->bind();
	glProgram->setAttributeBuffer(name, GL_FLOAT, 0, tupleSize, tupleSize*sizeof(GLfloat));
}
