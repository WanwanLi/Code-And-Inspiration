#define Graph struct Graph
Graph
{
	int n,m;
	double** costs;
	String* nodes;
	int** adjacencyTable;
};
Graph* newGraph(String* nodes,int n,String* edges,double* costs,int m)
{
	Graph* G=(Graph*)malloc(sizeof(Graph));
	G->nodes=nodes;
	G->n=n;
	G->costs=new_Double(n,n);
	G->m=m;
	int i,j;
	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			if(i==j)G->costs[i][j]=0;
			else G->costs[i][j]=INF;
		}
	}
	for(i=0;i<m;i++)
	{
		int u=getIndex(nodes,edges[2*i+0],n);
		int v=getIndex(nodes,edges[2*i+1],n);
		G->costs[u][v]=costs[i];
		G->costs[v][u]=costs[i];
	}
	G->adjacencyTable=null;
	return G;
}
Graph* newDirectedGraph(String* nodes,int n,String* edges,double* costs,int m)
{
	Graph* G=(Graph*)malloc(sizeof(Graph));
	G->nodes=nodes;
	G->n=n;
	G->costs=new_Double(n,n);
	G->m=m;
	int i,j;
	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			if(i==j)G->costs[i][j]=0;
			else G->costs[i][j]=INF;
		}
	}
	for(i=0;i<m;i++)
	{
		int u=getIndex(nodes,edges[2*i+0],n);
		int v=getIndex(nodes,edges[2*i+1],n);
		G->costs[u][v]=costs[i];
	}
	G->adjacencyTable=null;
	return G;
}
void printGraph(Graph* G)
{
	int n=G->n,m=G->m,i,j;
	printf("\n");
	for(j=0;j<n;j++)printf("\t%s",G->nodes[j]);
	for(i=0;i<n;i++)
	{
		printf("\n%s",G->nodes[i]);
		for(j=0;j<n;j++)
		{
			if(G->costs[i][j]==INF)printf("\tINF");
			else printf("\t%.1f",G->costs[i][j]);
		}
	}
	printf("\n");
}
void createAdjacencyTable(Graph* G)
{
	int n=G->n,m=G->m,i,j;
	int** adjacencyTable=(int**)malloc(n*sizeof(int*));
	for(i=0;i<n;i++)
	{
		int k=0;
		for(j=0;j<n;j++)if(i!=j&&G->costs[i][j]!=INF)k++;
		adjacencyTable[i]=newInt(k+1);
		adjacencyTable[i][0]=k;k=1;
		for(j=0;j<n;j++)
		{
			if(i!=j&&G->costs[i][j]!=INF)adjacencyTable[i][k++]=j;
		}
	}
	G->adjacencyTable=adjacencyTable;
}
void printAdjacencyTable(Graph* G)
{
	int n=G->n,m=G->m,i,j;
	for(i=0;i<n;i++)
	{
		printf("%s:",G->nodes[i]);
		int k=G->adjacencyTable[i][0];
		for(j=0;j<k;j++)
		{
			int t=G->adjacencyTable[i][j+1];
			printf("\t%s",G->nodes[t]);
		}
		printf("\n");
	}
}
void breadthFirstSearchGraph(Graph* G,String s,void (*visitNodeFunc)(Graph* G,int node))
{
	int n=G->n,m=G->m,i,j;
	int* isNotVisited=newInt(n);
	for(i=0;i<n;i++)isNotVisited[i]=true;
	int k=getIndex(G->nodes,s,n);
	List* Q=newList();
	addInt(Q,k);
	isNotVisited[k]=false;
	while(Q->length)
	{
		i=nextInt(Q);
		visitNodeFunc(G,i);
		k=G->adjacencyTable[i][0];
		for(j=0;j<k;j++)
		{
			int t=G->adjacencyTable[i][j+1];
			if(isNotVisited[t])
			{
				addInt(Q,t);
				isNotVisited[t]=false;
			}
		}
	}
}
void depthFirstSearchGraph(Graph* G,String s,void (*visitNodeFunc)(Graph* G,int node))
{
	int n=G->n,m=G->m,i,j;
	int* isNotVisited=newInt(n);
	for(i=0;i<n;i++)isNotVisited[i]=true;
	int k=getIndex(G->nodes,s,n);
	List* S=newList();
	pushInt(S,k);
	isNotVisited[k]=false;
	while(S->length)
	{
		i=popInt(S);
		visitNodeFunc(G,i);
		k=G->adjacencyTable[i][0];
		for(j=0;j<k;j++)
		{
			int t=G->adjacencyTable[i][j+1];
			if(isNotVisited[t])
			{
				pushInt(S,t);
				isNotVisited[t]=false;
			}
		}
	}
}
void reverseGraph(Graph* G)
{
	int n=G->n,m=G->m,i,j;
	double** costs=new_Double(n,n);
	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			costs[j][i]=G->costs[i][j];
		}
	}
	G->costs=costs;
	if(G->adjacencyTable)createAdjacencyTable(G);
}
Graph* copyGraph(Graph* G)
{
	Graph* G1=(Graph*)malloc(sizeof(Graph));
	G1->n=G->n;
	G1->m=G->m;
	G1->costs=G->costs;
	G1->nodes=G->nodes;
	G1->adjacencyTable=G->adjacencyTable;
	return G1;
}
int visitedNodesCount=0;
void countVisitedNodesFunc(Graph* G,int node)
{
	visitedNodesCount++;
}
int isStronglyConnected(Graph* G)
{
	visitedNodesCount=0;
	if(G->adjacencyTable==null)createAdjacencyTable(G);
	breadthFirstSearchGraph
	(
		G,G->nodes[0],
		countVisitedNodesFunc
	);
	if(visitedNodesCount<G->n)return 0;
	Graph* R=copyGraph(G);
	reverseGraph(R);
	visitedNodesCount=0;
	breadthFirstSearchGraph
	(
		R,R->nodes[0],
		countVisitedNodesFunc
	);
	if(visitedNodesCount<R->n)return 0;
	return 1;
}
int* getInDegree(Graph* G)
{
	int n=G->n,m=G->m,i,j;
	int* inDegree=newInt(n);
	for(i=0;i<n;i++)inDegree[i]=0;
	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			if(G->costs[i][j]!=0&&G->costs[i][j]!=INF)inDegree[j]++;
		}
	}
	return inDegree;
}
void searchGraphInTopologicalOrder(Graph* G,void (*visitNodeFunc)(Graph* G,int node))
{
	int n=G->n,m=G->m,i,j,k,t;
	int* inDegree=getInDegree(G);
	for(t=0;t<n;t++)
	{
		for(i=0;i<n;i++)if(inDegree[i]==0)break;
		if(i>=n)break;
		visitNodeFunc(G,i);
		inDegree[i]=-1;
		k=G->adjacencyTable[i][0];
		for(j=0;j<k;j++)
		{
			int t=G->adjacencyTable[i][j+1];
			inDegree[t]--;
		}
	}
}
