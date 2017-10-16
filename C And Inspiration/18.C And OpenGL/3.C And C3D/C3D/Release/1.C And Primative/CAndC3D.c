#include "C3D.h"
int main()
{
	loadOpenGLPlatform();
	BranchGroup* branchGroup=newBranchGroup();
	Material* Material1=newMaterial();
	setDiffuseColor(Material1,newColor3f(1,0,0));
	Appearance* Appearance1=newAppearance();
	setMaterial(Appearance1,Material1);
	addChild_BP(branchGroup,newSphere(0.1f,Appearance1));
	Appearance* Appearance2=newAppearance();
	Material* Material2=newMaterial();
	setDiffuseColor(Material2,newColor3f(0,1,1));
	setMaterial(Appearance2,Material2);
	Transform3D* transform3D=newTransform3D();
	setTranslation(transform3D,newVector3d(0,-0.45,-2));
	setScale(transform3D,newVector3d(1,0.5,1.5));
	setRotation(transform3D,newAxisAngle4d(1,1,1,PI/4));
	TransformGroup* TransformGroup1=newTransformGroup(transform3D);
	transform3D=newTransform3D();
	setRotation(transform3D,newAxisAngle4d(1,0,0,PI/4));
	TransformGroup* TransformGroup2=newTransformGroup(transform3D);
	transform3D=newTransform3D();
	setTranslation(transform3D,newVector3d(0,0.5,0));
	TransformGroup* TransformGroup3=newTransformGroup(transform3D);
	addChild_TP(TransformGroup3,newCone(0.1f,0.2f,Appearance1));
	addChild_TT(TransformGroup2,TransformGroup3);
	addChild_TP(TransformGroup1,newSphere(0.05f,Appearance2));
	addChild_BT(branchGroup,TransformGroup1);
	addChild_BT(branchGroup,TransformGroup2);
	BranchGroup* BranchGroup1=newBranchGroup();
	Material1=newMaterial();
	setDiffuseColor(Material1,newColor3f(0,1,1));
	Appearance1=newAppearance();
	setMaterial(Appearance1,Material1);
	addChild_BP(BranchGroup1,newColorCube(0.2f,Appearance1));
	SimpleUniverse* simpleUniverse=newSimpleUniverse();
	addBranchGraph(simpleUniverse,BranchGroup1);
	addBranchGraph(simpleUniverse,branchGroup);
	setViewPlatformBehavior(OrbitBehavior);
	getViewingPlatform(simpleUniverse);
	return 0;
}
