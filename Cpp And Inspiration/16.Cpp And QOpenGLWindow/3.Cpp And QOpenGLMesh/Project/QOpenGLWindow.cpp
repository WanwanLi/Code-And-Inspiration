#include "QOpenGLWindow.h"

void QOpenGLWindow::initializeGL()
{
	initializeOpenGLFunctions();
	glMesh=newQOpenGLMesh();
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	glProgram=newQOpenGLShaderProgram("shader.v", "shader.f");
	glProgram->enableAttributeArray("color");
	glProgram->enableAttributeArray("position");
}
void QOpenGLWindow::paintGL()
{
	glClear(GL_COLOR_BUFFER_BIT);
	glBindAttributeBuffer("position", glMesh->positionBuffer, 3);
	glBindAttributeBuffer("color", glMesh->colorBuffer, 3);
	glDrawTriangleArrays(); glDrawTriangleElements();
}
void QOpenGLWindow::resizeGL(int width, int height)
{

}
void QOpenGLWindow::teardownGL()
{
	delete glMesh;
	delete glProgram;
}
QOpenGLWindow::~QOpenGLWindow()
{
	makeCurrent();
	teardownGL();
}
QOpenGLMesh* QOpenGLWindow::newQOpenGLMesh()
{
	QOpenGLMesh* mesh=new QOpenGLMesh(3, 3);
	mesh->addPosition(0.00f,  0.75f, 1.0f);
	mesh->addPosition(0.75f, -0.75f, 1.0f);
	mesh->addPosition(-0.75f, -0.75f, 1.0f);
	mesh->addColor(1.0f, 0.0f, 0.0f);
	mesh->addColor(0.0f, 1.0f, 0.0f);
	mesh->addColor(0.0f, 0.0f, 1.0f);
	mesh->addIndices(0, 1, 2);
	mesh->createBuffers();
	return mesh;
}
QOpenGLShaderProgram* QOpenGLWindow::newQOpenGLShaderProgram(QString vShader, QString fShader)
{
	QOpenGLShaderProgram* program=new QOpenGLShaderProgram();
	program->addShaderFromSourceFile(QOpenGLShader::Vertex, vShader);
	program->addShaderFromSourceFile(QOpenGLShader::Fragment, fShader);
	program->link(); program->bind(); return program;
}
void QOpenGLWindow::glBindAttributeBuffer(const char* name, QOpenGLBuffer* glBuffer, GLuint tupleSize)
{
	glBuffer->bind();
	glProgram->setAttributeBuffer(name, GL_FLOAT, 0, tupleSize, sizeof(GLfloat)*tupleSize);
}
void QOpenGLWindow::glDrawTriangleArrays()
{
	glDrawArrays(GL_TRIANGLES, 0, glMesh->vertexLength);
}
void QOpenGLWindow::glDrawTriangleElements()
{
	glMesh->indexBuffer->bind();
	glDrawElements(GL_TRIANGLES, glMesh->indexLength, GL_UNSIGNED_SHORT, 0);
}