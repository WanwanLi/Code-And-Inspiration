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
		"A","B","C","D","E","F","G","H"
	};
	int n=8;
	String edges[]=
	{
		"A","B",
		"A","G",
		"B","C",
		"B","D",
		"C","G",
		"C","E",
		"C","D",
		"E","G",
		"E","F",
		"E","H",
		"E","D",
		"G","F",
		"F","H",
		"H","D"
	};
	double costs[]=
	{
		4, 24, 6, 16, 23, 5, 8, 18, 11, 14, 10, 9, 7, 21
	};
	int m=14;
	Graph* G=newGraph(nodes,n,edges,costs,m);
	printf("\nGraph G is: \n");
	printGraph(G);
	createAdjacencyTable(G);
	printf("\n\nGraph G.AdjacencyTable is: \n\n");
	printAdjacencyTable(G);
	String source="A";
	String destination="H";
	printf("\n\nGraph G.SearchAlongShortestPath ");
	printf("from %s to %s is: \n\n",source,destination);
	searchGraphAlongShortestPath(G,source,destination,Dijkstra);
	Graph* MST=getMinSpanningTree(G);
	createAdjacencyTable(MST);
	printf("\n\nTree MST.AdjacencyTable is: \n\n");
	printAdjacencyTable(MST);
	printf("\n\nTree MST.totalCost is: %.1f\n\n",getTotalCostOfGraph(MST));
	return 0;
}
