var ObjectUniformGrid=function(objectData,boundingBox,level,row,column)
{
	var coordinates=objectData.coordinates;
	var indices=objectData.indices;
	this.table=[]
	this.level=level;
	this.row=row;
	this.column=column;
	this.minX=boundingBox[0];
	this.minY=boundingBox[1];
	this.minZ=boundingBox[2];
	this.maxX=boundingBox[3];
	this.maxY=boundingBox[4];
	this.maxZ=boundingBox[5];
	this.getIntervals(level,row,column);
	for(var i=0;i<level*row*column;i++)this.table[i]=[];
	for(var i=0;i<indices.length;i+=3)
	{
		var p0=indices[i+0]*3,p1=indices[i+1]*3,p2=indices[i+2]*3;
		var x0=coordinates[p0+0],y0=coordinates[p0+1],z0=coordinates[p0+2];
		var x1=coordinates[p1+0],y1=coordinates[p1+1],z1=coordinates[p1+2];
		var x2=coordinates[p2+0],y2=coordinates[p2+1],z2=coordinates[p2+2];
		this.addCoordinateIndex(i,x0,y0,z0,x1,y1,z1,x2,y2,z2);
	}
	this.createIndexTables();
};
ObjectUniformGrid.prototype.getIntervals=function(level,row,column)
{
	this.dX=(this.maxX-this.minX)/(column-1);
	this.dY=(this.maxY-this.minY)/(level-1);
	this.dZ=(this.maxZ-this.minZ)/(row-1);
};
ObjectUniformGrid.prototype.addCoordinateIndex=function(coordinateIndex0,x0,y0,z0,x1,y1,z1,x2,y2,z2)
{
	var minX=this.min(x0,this.min(x1,x2));
	var minY=this.min(y0,this.min(y1,y2));
	var minZ=this.min(z0,this.min(z1,z2));
	var maxX=this.max(x0,this.max(x1,x2));
	var maxY=this.max(y0,this.max(y1,y2));
	var maxZ=this.max(z0,this.max(z1,z2));
	var j0=Math.floor((minX-this.minX)/this.dX);
	var i0=Math.floor((minZ-this.minZ)/this.dZ);
	var k0=Math.floor((minY-this.minY)/this.dY);
	var j1=Math.floor((maxX-this.minX)/this.dX);
	var i1=Math.floor((maxZ-this.minZ)/this.dZ);
	var k1=Math.floor((maxY-this.minY)/this.dY);
	for(var k=k0;k<=k1;k++)
	{
		if(k>=level)k=level-1;
		for(var i=i0;i<=i1;i++)
		{
			if(i>=row)i=row-1;
			for(j=j0;j<=j1;j++)
			{
				if(j>=column)j=column-1;
				var p=k*row*column+i*column+j;
				this.table[p].push(coordinateIndex0);
			}
		}
	}
};
ObjectUniformGrid.prototype.createIndexTables=function()
{
	var k=0;
	this.primaryIndexTable=[];
	this.secondaryIndexTable=[];
	for(var i=0;i<this.table.length;i++)
	{
		this.primaryIndexTable[i]=k;
		var length=this.table[i].length;
		this.secondaryIndexTable[k++]=length;
		for(var j=0;j<length;j++)
		{
			this.secondaryIndexTable[k++]=this.table[i][j];
		}
	}
};
ObjectUniformGrid.prototype.min=function (x,y){return x<=y?x:y;};
ObjectUniformGrid.prototype.max=function (x,y){return x>=y?x:y;};
