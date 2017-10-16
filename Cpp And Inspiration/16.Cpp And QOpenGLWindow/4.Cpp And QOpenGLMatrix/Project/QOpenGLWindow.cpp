#include "QOpenGLWindow.h"

void QOpenGLWindow::initializeGL()
{
	this->initializeOpenGLParameters();
	this->initializeOpenGLFunctions();
	glMesh=newQOpenGLMesh();
	glProgram=newQOpenGLShaderProgram("shader.v", "shader.f");
	glProgram->enableAttributeArray("position");
	glProgram->enableAttributeArray("color");
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);
}
void QOpenGLWindow::paintGL()
{
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	glModelviewMatrix.setToIdentity();
	glModelviewMatrix.lookAt(eye, center, up);
	glModelviewMatrix.translate(0.0, 0.0, 0.25);
	glProgram->setUniformValue("modelviewMatrix", glModelviewMatrix);
	glMesh->indexBuffer->bind();
	glBindAttributeBuffer("position", glMesh->positionBuffer, 3);
	glBindAttributeBuffer("color", glMesh->colorBuffer, 3);
	glDrawElements(GL_TRIANGLES, glMesh->indexLength, GL_UNSIGNED_SHORT, 0);
}
void QOpenGLWindow::resizeGL(int width, int height)
{
	glProjectionMatrix.setToIdentity();
	glProjectionMatrix.perspective(fov, (width+0.0)/height, zNear, zFar);
	glProgram->setUniformValue("projectionMatrix", glProjectionMatrix);
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
void QOpenGLWindow::initializeOpenGLParameters()
{
	this->translateSpeed=0.5;
	this->rotateSpeed=0.4;
	this->viewDistance=6;
	this->right.setX(1);
	this->up.setY(1); 
	this->forward.setZ(-1);
	this->eye.setZ(viewDistance);
	this->basicTimer.start(15, this);
}
QOpenGLMesh* QOpenGLWindow::newQOpenGLMesh()
{
	QOpenGLMesh* mesh=new QOpenGLMesh(8, 36);
	mesh->addPosition(1.0f,  -1.0f, 1.0f);
	mesh->addPosition(1.0f,  -1.0f, -1.0f);
	mesh->addPosition(-1.0f,  -1.0f, -1.0f);
	mesh->addPosition(-1.0f,  -1.0f, 1.0f);
	mesh->addPosition(1.0f,  1.0f, 1.0f);
	mesh->addPosition(1.0f,  1.0f, -1.0f);
	mesh->addPosition(-1.0f,  1.0f, -1.0f);
	mesh->addPosition(-1.0f,  1.0f, 1.0f);
	mesh->addColor(1.0f, 0.0f, 1.0f);
	mesh->addColor(1.0f, 0.0f, 0.0f);
	mesh->addColor(0.0f, 0.0f, 0.0f);
	mesh->addColor(0.0f, 0.0f, 1.0f);
	mesh->addColor(1.0f, 1.0f, 1.0f);
	mesh->addColor(1.0f, 1.0f, 0.0f);
	mesh->addColor(0.0f, 1.0f, 0.0f);
	mesh->addColor(0.0f, 1.0f, 1.0f);
	mesh->addIndices(0, 3, 2);
	mesh->addIndices(2, 1, 0);
	mesh->addIndices(4, 5, 6);
	mesh->addIndices(6, 7, 4);
	mesh->addIndices(0, 4, 7);
	mesh->addIndices(7, 3, 0);
	mesh->addIndices(1, 2, 6);
	mesh->addIndices(6, 5, 1);
	mesh->addIndices(0, 1, 5);
	mesh->addIndices(5, 4, 0);
	mesh->addIndices(2, 7, 6);
	mesh->addIndices(3, 7, 2);
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
void QOpenGLWindow::updateViewDirection()
{
	qreal rotUp=-rotateSpeed*mouseMove.x();
	qreal rotRight=-rotateSpeed*mouseMove.y();
	this->forward=rotate(forward, up, rotUp);
	this->right=rotate(right, up, rotUp);
	this->forward=rotate(forward, right, rotRight);
	this->up=rotate(up, right, rotRight);
	this->eye=center-viewDistance*forward;
}
void QOpenGLWindow::updateViewDistance()
{
	this->viewDistance-=translateSpeed*mouseDelta/120;
	this->viewDistance=clamp(viewDistance, 4, 20);
	this->eye=center-viewDistance*forward;
}
QVector3D QOpenGLWindow::rotate(QVector3D vector, QVector3D axis, qreal angle)
{
	rotateMatrix.setToIdentity();
	rotateMatrix.rotate(angle, axis.x(), axis.y(), axis.z());
	return rotateMatrix.map(vector);
}
void QOpenGLWindow::mouseMoveEvent(QMouseEvent* event)
{
	if(isMousePressed)
	{
		this->mouseMove=event->pos()-mousePos;
		this->mousePos=event->pos();
		this->updateViewDirection();
	}
}
void QOpenGLWindow::mousePressEvent(QMouseEvent* event)
{
	if(event->button()==Qt::LeftButton) 
	{
		this->isMousePressed=true;
		this->mousePos=event->pos();
	}
}
void QOpenGLWindow::mouseReleaseEvent(QMouseEvent* event)
{
	if(isMousePressed)
	{
        		this->isMousePressed=false;
	}
}
void QOpenGLWindow::wheelEvent(QWheelEvent* event)
{
	this->mouseDelta=event->delta();
	this->updateViewDistance();
	this->mouseDelta=0;
}
void QOpenGLWindow::timerEvent(QTimerEvent* event)
{
	this->update();
}
qreal QOpenGLWindow::clamp(qreal value, qreal min, qreal max)
{
	return value<min?min:value>max?max:value;
}