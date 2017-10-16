#include "c3d.h"
int main(int argc,char** argv)
{
	BranchGroup* branchGroup=newBranchGroup();
	addChild(branchGroup,newSphere(0.1f,null));
	SimpleUniverse* simpleUniverse=newSimpleUniverse();
	addBranchGraph(simpleUniverse,branchGroup);
	getViewingPlatform(simpleUniverse,argc,argv);
	return 0;
}