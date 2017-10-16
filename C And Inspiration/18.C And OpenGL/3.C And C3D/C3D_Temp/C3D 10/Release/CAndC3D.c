#include "C3D.h"
#include "IsoSurface3D.h"
#include "NurbsSurface3D.h"
#include "NurbsIsoSurface3D.h"
int main()
{
	loadOpenGLPlatform();
	BranchGroup* BranchGroup1=newBranchGroup();
	DirectionalLight* DirectionalLight1=newDirectionalLight(newColor3f(0.8,0.8,0.8),newVector3f(-2,-2,-2));
	addChild_BL(BranchGroup1,DirectionalLight1);
	double CtrlValues[]=
	{
		2.0,2.0,2.0,
		2.0,2.0,2.0,
		2.0,2.0,2.0,

		2.0,2.0,2.0,
		2.0,-100.0,2.0,
		2.0,2.0,2.0,

		2.0,2.0,2.0,
		2.0,2.0,2.0,
		2.0,2.0,2.0

	};
	double Weights[]=
	{
		1.0,1.0,1.0,
		1.0,1.0,1.0,
		1.0,1.0,1.0,

		1.0,1.0,1.0,
		1.0,1.0,1.0,
		1.0,1.0,1.0,

		1.0,1.0,1.0,
		1.0,1.0,1.0,
		1.0,1.0,1.0
	};
	int m=3,n=3,l=3,uOrder=2,vOrder=2,wOrder=2,step=80,uStep=step,vStep=step,wStep=step,i,j,k;
	double r=1.2,value=1.0;
	double* uKnots=getBezierUniformNurbsKnots(m,uOrder);
	double* vKnots=getBezierUniformNurbsKnots(n,vOrder);
	double* wKnots=getBezierUniformNurbsKnots(l,wOrder);
	double*** ctrlValues=new__Double(l,m,n);double*** weights=new__Double(l,m,n);
	for(k=0;k<l;k++)for(i=0;i<m;i++)for(j=0;j<n;j++){ctrlValues[k][i][j]=CtrlValues[k*m*n+i*n+j];weights[k][i][j]=Weights[k*m*n+i*n+j];}
	Point3f* p0=newPoint3f(r,r,r),*p1=newPoint3f(-r,-r,-r);
	Shape3D* NurbsIsoSurface3D=newNurbsIsoSurface3D(p0,p1,value,ctrlValues,l,m,n,weights,uKnots,vKnots,wKnots,uOrder,vOrder,wOrder,uStep,vStep,wStep);
	Appearance* Appearance1=newAppearance();
	Material* Material1=newMaterial();
	setDiffuseColor(Material1,newColor3f(0,1,0));
	setMaterial(Appearance1,Material1);
	setAppearance(NurbsIsoSurface3D,Appearance1);
	addChild_BS(BranchGroup1,NurbsIsoSurface3D);
	SimpleUniverse* simpleUniverse=newSimpleUniverse();
	addBranchGraph(simpleUniverse,BranchGroup1);
//	setViewPlatformBehavior(OrbitBehavior);
	getViewingPlatform(simpleUniverse);
	return 0;
}
