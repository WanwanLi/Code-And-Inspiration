#include "QOpenGLCube.h"

QOpenGLCube::QOpenGLCube(GLuint vertexLength, GLuint indexLength): QOpenGLMesh(vertexLength, indexLength)
{
	typedef QVector3D V;
	typedef QVector2D T;
	typedef GLvertex G;
	QVector3D p[]=
	{
		V(1.0f,  -1.0f, 1.0f), V(1.0f,  1.0f, 1.0f), V(-1.0f,  1.0f, 1.0f), V(-1.0f,  -1.0f, 1.0f), 
		V(1.0f,  -1.0f, -1.0f), V(1.0f,  1.0f, -1.0f), V(-1.0f,  1.0f, -1.0f), V(-1.0f,  -1.0f, -1.0f)
	};
	QVector3D n[]=
	{
		V(1.0f,  0.0f, 0.0f), V(0.0f,  1.0f, 0.0f), V(0.0f,  0.0f, 1.0f),
		V(-1.0f,  0.0f, 0.0f), V(0.0f,  -1.0f, 0.0f), V(0.0f,  0.0f, -1.0f)
	};
	QVector2D t[]=
	{
		T(0.0f,  0.0f), T(0.33f, 0.0f), T(0.66f, 0.0f), T(1.0f, 0.0f), 
		T(0.0f,  0.5f), T(0.33f, 0.5f), T(0.66f, 0.5f), T(1.0f, 0.5f), 
		T(0.0f,  1.0f), T(0.33f, 1.0f), T(0.66f, 1.0f), T(1.0f, 1.0f)
	};
	GLvertex v[]=
	{
		G(p[0], n[2], t[5]), G(p[1], n[2], t[1]), G(p[2], n[2], t[0]), G(p[3], n[2], t[4]),
		G(p[7], n[5], t[6]), G(p[6], n[5], t[2]), G(p[5], n[5], t[1]), G(p[4], n[5], t[5]), 
		G(p[1], n[1], t[7]), G(p[5], n[1], t[3]), G(p[6], n[1], t[2]), G(p[2], n[1], t[6]), 
		G(p[0], n[4], t[5]), G(p[3], n[4], t[4]), G(p[7], n[4], t[8]), G(p[4], n[4], t[9]),
		G(p[0], n[0], t[9]), G(p[4], n[0], t[10]), G(p[5], n[0], t[6]), G(p[1], n[0], t[5]), 
		G(p[3], n[3], t[11]), G(p[2], n[3], t[7]), G(p[6], n[3], t[6]), G(p[7], n[3], t[10])
	};
	this->addQuad(v[0], v[1], v[2], v[3]);
	this->addQuad(v[4], v[5], v[6], v[7]);
	this->addQuad(v[8], v[9], v[10], v[11]);
	this->addQuad(v[12], v[13], v[14], v[15]);
	this->addQuad(v[16], v[17], v[18], v[19]);
	this->addQuad(v[20], v[21], v[22], v[23]);
	this->createBuffers();
}
