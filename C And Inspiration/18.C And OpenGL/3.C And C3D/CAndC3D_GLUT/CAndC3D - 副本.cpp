#include "c3d.h"
int main(int argc,char** argv)
{
	BranchGroup* branchGroup=newBranchGroup();
	Material* Material1=newMaterial();
	setDiffuseColor(Material1,newColor3f(1,0,0));
	Appearance* Appearance1=newAppearance();
	setMaterial(Appearance1,Material1);
	addChild(branchGroup,newSphere(0.1f,Appearance1));
	Appearance* Appearance2=newAppearance();
	Material* Material2=newMaterial();
	setDiffuseColor(Material2,newColor3f(0,1,0));
	setMaterial(Appearance2,Material2);
	Transform3D* transform3D=newTransform3D();
	setTranslation(transform3D,newVector3d(0,-0.45,0));
	setScale(transform3D,newVector3d(1,0.5,1.5));
	setRotation(transform3D,newAxisAngle4d(1,1,1,PI/4));
	TransformGroup* TransformGroup1=newTransformGroup(transform3D);
	transform3D=newTransform3D();
	setRotation(transform3D,newAxisAngle4d(1,0,0,PI/4));
	TransformGroup* TransformGroup2=newTransformGroup(transform3D);
	transform3D=newTransform3D();
	setTranslation(transform3D,newVector3d(0,0.25,0));
	TransformGroup* TransformGroup3=newTransformGroup(transform3D);
	addChild(TransformGroup3,newCone(0.1f,0.2f,Appearance1));
	addChild(TransformGroup2,TransformGroup3);
	addChild(TransformGroup1,newSphere(0.05f,Appearance2));
	addChild(branchGroup,TransformGroup1);
	addChild(branchGroup,TransformGroup2);
	SimpleUniverse* simpleUniverse=newSimpleUniverse();
	addBranchGraph(simpleUniverse,branchGroup);
	getViewingPlatform(simpleUniverse);
	return 0;
}