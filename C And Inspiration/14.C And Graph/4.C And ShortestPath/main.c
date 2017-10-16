#include "util.h"
#include "Graph.h"
void Dijkstra(Graph* G,int node)
{
	printf("%s->",G->nodes[node]);
}
int main()
{
	String nodes[]=
	{
		"V0","V1","V2","V3","V4","V5","V6","V7"
	};
	int n=8;
	String edges[]=
	{
		"V0","V1",
		"V0","V4",
		"V0","V7",
		"V1","V2",
		"V1","V3",
		"V1","V7",
		"V2","V3",
		"V2","V6",
		"V3","V6",
		"V4","V5",
		"V4","V6",
		"V4","V7",
		"V5","V2",
		"V5","V6",
		"V7","V2",
		"V7","V5"
	};
	double costs[]=
	{
		5, 9, 8, 12, 15, 4, 3, 11, 9, 4, 20, 5, 1, 13, 7, 6
	};
	int m=16;
	Graph* G=newDirectedGraph(nodes,n,edges,costs,m);
	printf("\nGraph G is: \n");
	printGraph(G);
	createAdjacencyTable(G);
	printf("\n\nGraph G.AdjacencyTable is: \n\n");
	printAdjacencyTable(G);
	String source="V0";
	String destination="V6";
	printf("\n\nGraph G.SearchAlongShortestPath ");
	printf("from %s to %s is: \n\n",source,destination);
	searchGraphAlongShortestPath(G,source,destination,Dijkstra);
	return 0;
}
