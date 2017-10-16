#include "util.h"
#include "Graph.h"
void BFS(Graph* G,int node)
{
	printf("%s ",G->nodes[node]);
}
void DFS(Graph* G,int node)
{
	printf("%s_",G->nodes[node]);
}
int main()
{
	String nodes[]=
	{
		"1","2","3","4","5","6","7","8"
	};
	int n=8;
	String edges[]=
	{
		"1","2",
		"1","3",
		"2","3",
		"2","4",
		"2","5",
		"3","5",
		"3","7",
		"3","8",
		"4","5",
		"5","6",
		"7","8"
	};
	double costs[]=
	{
		0.5,
		3.4,
		2.0,
		1.3,
		0.5,
		3.4,
		2.0,
		0.5,
		3.4,
		2.0,
		1.3
	};
	int m=11;
	Graph* G=newGraph(nodes,n,edges,costs,m);
	//Graph* G=newDirectedGraph(nodes,n,edges,costs,m);
	printf("\nGraph G is: \n");
	printGraph(G);
	createAdjacencyTable(G);
	printf("\n\nGraph G.AdjacencyTable is: \n\n");
	printAdjacencyTable(G);
	printf("\n\nGraph G.BFS is: \n");
	breadthFirstSearchGraph(G,"1",BFS);
	printf("\n\nGraph G.DFS is: \n");
	depthFirstSearchGraph(G,"1",DFS);
	return 0;
}
