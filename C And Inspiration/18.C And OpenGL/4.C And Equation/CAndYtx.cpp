#include <OpenGL\glut.h>
#include <stdio.h>
float eyeX=2.0,eyeY=2.0,eyeZ=2.0,centerX=0.0,centerY=0.0,centerZ=0.0;
float lightPosition[]={2.0,2.0,2.0,1};
float lightDirection[]={0.0,0.0,2.0,0};
float lightColor[]={1.0,1.0,1.0,1.0};
float diffuseColor[]={1.0,0.0,0.0,1.0};
float specularColor[]={1.0,1.0,1.0,1.0};
float lightShininess=50.0;
double radius=1.0;
int slices=20,stacks=10;
void displayFunc()
{

	glEnable(GL_LIGHT0);
	glLightfv(GL_LIGHT0,GL_POSITION,lightPosition);
	glLightfv(GL_LIGHT0,GL_POSITION,lightDirection);
	glLightfv(GL_LIGHT0,GL_AMBIENT,lightColor);
	glLightfv(GL_LIGHT0,GL_SPECULAR,lightColor);
	glLightfv(GL_LIGHT0,GL_DIFFUSE,lightColor);
	glLightf(GL_LIGHT0,GL_SHININESS,lightShininess);
	glLightModelf(GL_LIGHT_MODEL_LOCAL_VIEWER,1.0);
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	gluLookAt(eyeX,eyeY,eyeZ,centerX,centerY,centerZ,0,1,0);
	glFrontFace(GL_CCW);		// Counter clock-wise polygons face out
	glEnable(GL_CULL_FACE);
//	glCullFace(GL_BACK);
//	glColor3f(1.0,0,0);
	glShadeModel(GL_FLAT);
	glEnable(GL_COLOR_MATERIAL);
	glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
	glMaterialfv(GL_FRONT,GL_DIFFUSE,diffuseColor);
	glMaterialfv(GL_FRONT,GL_SPECULAR,specularColor);
/*
	GLUquadric* gluQuadric=gluNewQuadric();
	gluQuadricDrawStyle(gluQuadric,GLU_LINE);
	gluQuadricNormals(gluQuadric,GLU_FLAT);
	gluQuadricNormals(gluQuadric,GLU_SMOOTH);
	gluSphere(gluQuadric,radius,slices,stacks);
*/
	glutSolidSphere(1.0f, 150, 150);
	glColor3f(1.0,0.0,0.0);
	glBegin(GL_LINES);
	glVertex3f(0,0,0);
	glVertex3f(1.5,0,0);
	glEnd();
	glRasterPos3f(1.5,0,0);
	glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24,'x');
	glColor3f(0.0,1.0,0.0);
	glBegin(GL_LINES);
	glVertex3f(0,0,0);
	glVertex3f(0,1.5,0);
	glEnd();
	glRasterPos3f(0,1.5,0);
	glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24,'y');
	glColor3f(0.0,0.0,1.0);
	glBegin(GL_LINES);
	glVertex3f(0,0,0);
	glVertex3f(0,0,1.5);
	glEnd();
	glRasterPos3f(0,0,1.5);
	glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24,'z');
	glutSwapBuffers();
}
float fieldOfView=60.0,near=0.0,far=100.0;
void reshapeFunc(int windowWidth,int windowHeight)
{
	glViewport(0,0,windowWidth,windowHeight);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(fieldOfView,(windowWidth+0.0)/windowHeight,near,far);
	glutPostRedisplay();
}
int main(int argc,char** argv)
{
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB|GLUT_DEPTH);
	glutInitWindowSize(500,500);
	glutInitWindowPosition(0,0);
	glutCreateWindow("");
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_LIGHTING);
	glEnable(GL_NORMALIZE);
	glEnable(GL_CULL_FACE);		// Do not try to display the back sides
	glutDisplayFunc(displayFunc);
	glutReshapeFunc(reshapeFunc);
	glutMainLoop();
}