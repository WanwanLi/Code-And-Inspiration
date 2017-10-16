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
void TOS(Graph* G,int node)
{
	printf("%s->",G->nodes[node]);
}
int main()
{
	String nodes[]=
	{
		"V1","V2","V3","V4","V5","V6","V7"
	};
	int n=7;
	String edges[]=
	{
		"V1","V4",
		"V1","V5",
		"V1","V7",
		"V2","V3",
		"V2","V5",
		"V2","V6",
		"V3","V4",
		"V3","V5",
		"V4","V5",
		"V5","V6",
		"V5","V7",
		"V6","V7"
	};
	double costs[]=
	{
		1,1,1,1,1,1,1,1,1,1,1,1
	};
	int m=12;
	Graph* G=newDirectedGraph(nodes,n,edges,costs,m);
	printf("\nGraph G is: \n");
	printGraph(G);
	createAdjacencyTable(G);
	printf("\n\nGraph G.AdjacencyTable is: \n\n");
	printAdjacencyTable(G);
	printf("\n\nGraph G.TopologicalOrder is: \n\n");
	searchGraphInTopologicalOrder(G,TOS);
	return 0;
}
