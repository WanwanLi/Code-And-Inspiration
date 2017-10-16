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
		"A","B","C","D","E"
	};
	int n=5;
	String edges[]=
	{
		"A","B",
		"A","D",
		"B","E",
		"B","D",
		"C","A",
		"D","C",
		"E","D"
	};
	double costs[]=
	{
		1,1,1,1,1,1,1
	};
	int m=7;
	Graph* G=newDirectedGraph(nodes,n,edges,costs,m);
	printf("\nGraph G is: \n");
	printGraph(G);
	createAdjacencyTable(G);
	printf("\n\nGraph G.AdjacencyTable is: \n\n");
	printAdjacencyTable(G);
	reverseGraph(G);
	printf("\n\nGraph G.reverse is: \n");
	printGraph(G);
	printf("\n\nGraph G.reverse.AdjacencyTable is: \n");
	printAdjacencyTable(G);
	printf("\nG.isStronglyConnected is: %s \n",isStronglyConnected(G)?"true":"false");
	String newEdges[]=
	{
		"A","B",
		"A","C",
		"A","D",
		"B","D",
		"B","E",
		"D","C",
		"E","D"
	};
	G=newDirectedGraph(nodes,n,newEdges,costs,m);
	createAdjacencyTable(G);
	printf("\n\nGraph new G.AdjacencyTable is: \n\n");
	printAdjacencyTable(G);
	printf("\nnew G.BFS: ");
	breadthFirstSearchGraph(G,"A",BFS);
	reverseGraph(G);
	printf("\nreverse new G.BFS: ");
	breadthFirstSearchGraph(G,"A",BFS);
	printf("\nG.isStronglyConnected is: %s \n",isStronglyConnected(G)?"true":"false");
	return 0;
}
