var ObjectUniformGrid=function(objectData,boundingBox,level,row,column)
{
	this.level=level;
	this.row=row;
	this.column=column;
	this.initTable();
	this.minX=boundingBox[0];
	this.minY=boundingBox[1];
	this.minZ=boundingBox[2];
	this.maxX=boundingBox[3];
	this.maxY=boundingBox[4];
	this.maxZ=boundingBox[5];
	this.getIntervals(level,row,column);
	var indices=objectData.indices;
	var coordinates=objectData.coordinates;
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
ObjectUniformGrid.prototype.initTable=function()
{
	this.table=[];
	var l=this.level;
	var r=this.row;
	var c=this.column;
	for(var k=0;k<l;k++)
	{
		for(var i=0;i<r;i++)
		{
			for(j=0;j<c;j++)
			{
				var p=k*r*c+i*c+j;
				this.table[p]=[];
			}
		}
	}
};
ObjectUniformGrid.prototype.getIntervals=function(level,row,column)
{
	this.dX=(this.maxX-this.minX)/column;
	this.dY=(this.maxY-this.minY)/level;
	this.dZ=(this.maxZ-this.minZ)/row;
};
ObjectUniformGrid.prototype.addCoordinateIndex=function(index0,x0,y0,z0,x1,y1,z1,x2,y2,z2)
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
	var l=this.level,r=this.row,c=this.column;
	k0=this.clamp(k0,0,l-1);k1=this.clamp(k1,0,l-1);
	i0=this.clamp(i0,0,r-1);i1=this.clamp(i1,0,r-1);
	j0=this.clamp(j0,0,c-1);j1=this.clamp(j1,0,c-1);
	for(var k=k0;k<=k1;k++)
	{
		for(var i=i0;i<=i1;i++)
		{
			for(j=j0;j<=j1;j++)
			{
				var p=k*r*c+i*c+j;
				this.table[p].push(index0);
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
ObjectUniformGrid.prototype.getPrimaryIndicesArray=function()
{
	var Uint32Size=Uint32Array.BYTES_PER_ELEMENT;
	var primaryIndicesArray=new Uint32Array(this.primaryIndexTable);
	primaryIndicesArray.size=Uint32Size*this.primaryIndexTable.length;
	return primaryIndicesArray;
};
ObjectUniformGrid.prototype.getSecondaryIndicesArray=function()
{
	var Uint32Size=Uint32Array.BYTES_PER_ELEMENT;
	var secondaryIndicesArray=new Uint32Array(this.secondaryIndexTable);
	secondaryIndicesArray.size=Uint32Size*this.secondaryIndexTable.length;
	return secondaryIndicesArray;
};
ObjectUniformGrid.prototype.printIndexTables=function()
{
	var l=this.level;
	var r=this.row;
	var c=this.column;
	var s="\nPrimaryIndexTable\n";
	for(var k=0;k<l;k++)
	{
		for(var i=0;i<r;i++)
		{
			for(j=0;j<c;j++)
			{
				var p=k*r*c+i*c+j;
				s+=this.primaryIndexTable[p]+"  ";
			}
			s+="\n";
		}
		s+="\n\n";
	}
	s+="\nSecondaryIndexTable\n";
	for(var i=0;i<this.secondaryIndexTable.length;)
	{
		s+=i+":  "
		var length=this.secondaryIndexTable[i++];
		for(j=0;j<length;j++)
		{
			s+=this.secondaryIndexTable[i++]+"  ";
		}
		s+="\n";
	}
	console.log(s);
};
ObjectUniformGrid.prototype.clamp=function(k,x,y){return k<x?x:k>y?y:k;};
ObjectUniformGrid.prototype.min=function(x,y){return x<=y?x:y;};
ObjectUniformGrid.prototype.max=function(x,y){return x>=y?x:y;};



var MoleculeUniformGrid=function(moleculeDataArray,boundingBox,level,row,column)
{
	this.level=level;
	this.row=row;
	this.column=column;
	this.initTable();
	this.minX=boundingBox[0];
	this.minY=boundingBox[1];
	this.minZ=boundingBox[2];
	this.maxX=boundingBox[3];
	this.maxY=boundingBox[4];
	this.maxZ=boundingBox[5];
	this.getIntervals(level,row,column);
	for(var i=0;i<moleculeDataArray.length;i+=7)
	{
		var x=moleculeDataArray[i+0];
		var y=moleculeDataArray[i+1];
		var z=moleculeDataArray[i+2];
		var r=moleculeDataArray[i+6];
		this.addMoleculeIndex(i,x,y,z,r);
	}
	this.createIndexTables();
};
MoleculeUniformGrid.prototype.initTable=function()
{
	this.table=[];
	var l=this.level;
	var r=this.row;
	var c=this.column;
	for(var k=0;k<l;k++)
	{
		for(var i=0;i<r;i++)
		{
			for(j=0;j<c;j++)
			{
				var p=k*r*c+i*c+j;
				this.table[p]=[];
			}
		}
	}
};
MoleculeUniformGrid.prototype.getIntervals=function(level,row,column)
{
	this.dX=(this.maxX-this.minX)/column;
	this.dY=(this.maxY-this.minY)/level;
	this.dZ=(this.maxZ-this.minZ)/row;
};
MoleculeUniformGrid.prototype.addMoleculeIndex=function(index0,centerX,centerY,centerZ,radius)
{
	var minX=centerX-radius;
	var minY=centerY-radius;
	var minZ=centerZ-radius;
	var maxX=centerX+radius;
	var maxY=centerY+radius;
	var maxZ=centerZ+radius;
	var j0=Math.floor((minX-this.minX)/this.dX);
	var i0=Math.floor((minZ-this.minZ)/this.dZ);
	var k0=Math.floor((minY-this.minY)/this.dY);
	var j1=Math.floor((maxX-this.minX)/this.dX);
	var i1=Math.floor((maxZ-this.minZ)/this.dZ);
	var k1=Math.floor((maxY-this.minY)/this.dY);
	var l=this.level,r=this.row,c=this.column;
	k0=this.clamp(k0,0,l-1);k1=this.clamp(k1,0,l-1);
	i0=this.clamp(i0,0,r-1);i1=this.clamp(i1,0,r-1);
	j0=this.clamp(j0,0,c-1);j1=this.clamp(j1,0,c-1);
	for(var k=k0;k<=k1;k++)
	{
		for(var i=i0;i<=i1;i++)
		{
			for(j=j0;j<=j1;j++)
			{
				var p=k*r*c+i*c+j;
				this.table[p].push(index0);
			}
		}
	}
};
MoleculeUniformGrid.prototype.createIndexTables=function()
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
MoleculeUniformGrid.prototype.getPrimaryIndicesArray=function()
{
	var Uint32Size=Uint32Array.BYTES_PER_ELEMENT;
	var primaryIndicesArray=new Uint32Array(this.primaryIndexTable);
	primaryIndicesArray.size=Uint32Size*this.primaryIndexTable.length;
	return primaryIndicesArray;
};
MoleculeUniformGrid.prototype.getSecondaryIndicesArray=function()
{
	var Uint32Size=Uint32Array.BYTES_PER_ELEMENT;
	var secondaryIndicesArray=new Uint32Array(this.secondaryIndexTable);
	secondaryIndicesArray.size=Uint32Size*this.secondaryIndexTable.length;
	return secondaryIndicesArray;
};
MoleculeUniformGrid.prototype.printIndexTables=function()
{
	var l=this.level;
	var r=this.row;
	var c=this.column;
	var s="\nPrimaryIndexTable\n";
	for(var k=0;k<l;k++)
	{
		for(var i=0;i<r;i++)
		{
			for(j=0;j<c;j++)
			{
				var p=k*r*c+i*c+j;
				s+=this.primaryIndexTable[p]+"  ";
			}
			s+="\n";
		}
		s+="\n\n";
	}
	s+="\nSecondaryIndexTable\n";
	for(var i=0;i<this.secondaryIndexTable.length;)
	{
		s+=i+":  "
		var length=this.secondaryIndexTable[i++];
		for(j=0;j<length;j++)
		{
			s+=this.secondaryIndexTable[i++]+"  ";
		}
		s+="\n";
	}
	console.log(s);
};
MoleculeUniformGrid.prototype.clamp=function(k,x,y){return k<x?x:k>y?y:k;};
MoleculeUniformGrid.prototype.min=function(x,y){return x<=y?x:y;};
MoleculeUniformGrid.prototype.max=function(x,y){return x>=y?x:y;};
