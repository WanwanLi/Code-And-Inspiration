#include<stdio.h>
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
void searchGraphAlongShortestPath(Graph* G,String sourse,String destination,void (*visitNodeFunc)(Graph* G,int node))
{
	int n=G->n,m=G->m,i,j,k,d;
	int s=getIndex(G->nodes,sourse,n);
	int t=getIndex(G->nodes,destination,n);
	int* select=newInt(n);
	for(i=0;i<n;i++)select[i]=false;
	select[s]=true;
	int* previous=newInt(n);
	for(i=0;i<n;i++)previous[i]=s;
	previous[s]=-1;
	double* distance=newDouble(n);
	for(i=0;i<n;i++)distance[i]=G->costs[s][i];
	for(k=0;k<n-1;k++)
	{
		double minDistance=INF;d=0;
		for(i=0;i<n;i++)
		{
			if(!select[i]&&distance[i]<minDistance)
			{
				minDistance=distance[i];
				d=i;
			}
		}
		select[d]=true;
		for(i=0;i<n;i++)
		{
			if(select[i])continue;
			for(j=0;j<n;j++)
			{
				if(select[j]&&distance[j]+G->costs[j][i]<distance[i])
				{
					distance[i]=distance[j]+G->costs[j][i];
					previous[i]=j;
				}
			}
		}
	}
	List* S=newList();
	while(t!=-1)
	{
		pushInt(S,t);
		t=previous[t];
	}
	while(S->length)visitNodeFunc(G,nextInt(S));
}
Graph* newMinSpanningTree(Graph* G,double* cost,int* previous)
{
	Graph* T=(Graph*)malloc(sizeof(Graph));
	int n=G->n,i,j,k;
	T->nodes=G->nodes;
	T->n=n;
	T->costs=new_Double(n,n);
	T->m=0;
	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			if(i==j)T->costs[i][j]=0;
			else T->costs[i][j]=INF;
		}
	}
	for(i=0;i<n;i++)
	{
		j=i;k=j;j=previous[j];
		while(j!=-1)
		{
			if(T->costs[j][k]==INF)
			{
				T->costs[j][k]=cost[k];
				T->costs[k][j]=cost[k];
				T->m++;
			}
			k=j;j=previous[j];
		}
	}
	return T;
}
Graph* getMinSpanningTree(Graph* G)
{
	int n=G->n,m=G->m,i,j,k,d;
	int* select=newInt(n);
	for(i=0;i<n;i++)select[i]=false;
	select[0]=true;
	int* previous=newInt(n);
	for(i=0;i<n;i++)previous[i]=0;
	previous[0]=-1;
	double* cost=newDouble(n);
	for(i=0;i<n;i++)cost[i]=G->costs[0][i];
	for(k=0;k<n-1;k++)
	{
		double minDistance=INF;d=0;
		for(i=0;i<n;i++)
		{
			if(!select[i]&&cost[i]<minDistance)
			{
				minDistance=cost[i];
				d=i;
			}
		}
		select[d]=true;
		for(i=0;i<n;i++)
		{
			if(select[i])continue;
			for(j=0;j<n;j++)
			{
				if(select[j]&&G->costs[j][i]<cost[i])
				{
					cost[i]=G->costs[j][i];
					previous[i]=j;
				}
			}
		}
	}
	return newMinSpanningTree(G,cost,previous);
}
double getTotalCostOfGraph(Graph* G)
{
	int n=G->n,m=G->m,i,j;
	double totalCost=0;
	for(i=0;i<n;i++)
	{
		for(j=i+1;j<n;j++)
		{
			if(G->costs[i][j]!=INF)totalCost+=G->costs[i][j];
		}
	}
	return totalCost;
}
double getTotalCostOfDirectedGraph(Graph* G)
{
	int n=G->n,m=G->m,i,j;
	double totalCost=0;
	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			if(G->costs[i][j]!=INF)totalCost+=G->costs[i][j];
		}
	}
	return totalCost;
}
Graph* loadGraphFromFile(String fileName)
{
	FILE* file=fopen(fileName,"r");
	if(file==null)return null;
	String s,c='\0';
	int n=0,b=true,i=0,j,k;
	List* nodeList=newList();
	while(!feof(file))
	{
		s=newString(0);
		fscanf(file,"%s",s);
		if(b){addString(nodeList,s);b=false;}
		fscanf(file,"%c",&c);
		if(c=='\n'||feof(file)){n++;b=true;}
	}
	fclose(file);
	Graph* G=(Graph*)malloc(sizeof(Graph));
	G->nodes=new_String(n);
	for(i=0;nodeList->length;i++)G->nodes[i]=nextString(nodeList);
	G->n=n;
	G->costs=new_Double(n,n);
	G->m=0;
	int** adjacencyTable=(int**)malloc(n*sizeof(int*));
	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			if(i==j)G->costs[i][j]=0;
			else G->costs[i][j]=INF;
		}
	}
	G->adjacencyTable=adjacencyTable;
	file=fopen(fileName,"r");
	List* edgeList;
	for(i=0;!feof(file);)
	{
		s=newString(0);
		fscanf(file,"%s",s);
		if(b){b=false;edgeList=newList();}
		else 
		{
			j=getIndex(G->nodes,s,G->n);
			addInt(edgeList,j);
			G->costs[i][j]=1;
			G->m++;
		}
		fscanf(file,"%c",&c);
		if(c=='\n'||feof(file))
		{
			k=edgeList->length;
			adjacencyTable[i]=newInt(k+1);
			adjacencyTable[i][0]=k;
			for(j=1;edgeList->length;j++)
			{
				adjacencyTable[i][j]=nextInt(edgeList);
//printf("(%d,%d)  ",i,adjacencyTable[i][j]);
			}
			i++;
//printf("\n");
			b=true;
		}
	//	printf("%s",s);
	//	printf("%c",c);
	}
	G->adjacencyTable=adjacencyTable;
	return G;
}
