#include "QOpenGLVertex.h"

QOpenGLVertex::QOpenGLVertex()
{
	this->position=newGLvertex3f(0, 0, 0);
	this->normal=newGLvertex3f(0, 0, 0);
	this->color=newGLvertex3f(0, 0, 0);
}
QOpenGLVertex::QOpenGLVertex(QVector3D position, QVector3D normal, QVector3D color)
{
	this->position=newGLvertex3f(position.x(), position.y(), position.z());
	this->normal=newGLvertex3f(normal.x(), normal.y(), normal.z());
	this->color=newGLvertex3f(color.x(), color.y(), color.z());
}
void QOpenGLVertex::setPosition(GLfloat x, GLfloat y, GLfloat z)
{
	this->position[0]=x; this->position[1]=y; this->position[2]=z; 
}
void QOpenGLVertex::setNormal(GLfloat x, GLfloat y, GLfloat z)
{
	this->normal[0]=x; this->normal[1]=y; this->normal[2]=z; 
}
void QOpenGLVertex::setColor(GLfloat r, GLfloat g, GLfloat b)
{
	this->color[0]=r; this->color[1]=g; this->color[2]=b; 
}
GLfloat* QOpenGLVertex::newGLvertex3f(GLfloat f0, GLfloat f1, GLfloat f2)
{
	GLfloat* vertex3f=(GLfloat*)malloc(sizeof(GLfloat)*3);
	vertex3f[0]=f0; vertex3f[1]=f1; vertex3f[2]=f2; return vertex3f;
}