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



var PhotonUniformGrid=function(boundingBox,level,row,column)
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
	this.photonInfoTable=[];
};
PhotonUniformGrid.prototype.addPhotonInfo=function(photonInfo)
{
	for(var i=0;i<photonInfo.positionX.length;i++)
	{
		var x=photonInfo.positionX[i];
		var y=photonInfo.positionY[i];
		var z=photonInfo.positionZ[i];
		var w=photonInfo.positionW[i];
		var l=photonInfo.directionX[i];
		var m=photonInfo.directionY[i];
		var n=photonInfo.directionZ[i];
		var r=photonInfo.colorR[i];
		var g=photonInfo.colorG[i];
		var b=photonInfo.colorB[i];
		if(x<this.minX-10)continue;
		this.addPhotonIndex(x,y,z);
		this.photonInfoTable.push(x);
		this.photonInfoTable.push(y);
		this.photonInfoTable.push(z);
		this.photonInfoTable.push(w);
		this.photonInfoTable.push(l);
		this.photonInfoTable.push(m);
		this.photonInfoTable.push(n);
		this.photonInfoTable.push(r);
		this.photonInfoTable.push(g);
		this.photonInfoTable.push(b);
	}
};
PhotonUniformGrid.prototype.initTable=function()
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
PhotonUniformGrid.prototype.getIntervals=function(level,row,column)
{
	this.dX=(this.maxX-this.minX)/column;
	this.dY=(this.maxY-this.minY)/level;
	this.dZ=(this.maxZ-this.minZ)/row;
};
PhotonUniformGrid.prototype.addPhotonIndex=function(x,y,z)
{
	var j=Math.floor((x-this.minX)/this.dX);
	var i=Math.floor((z-this.minZ)/this.dZ);
	var k=Math.floor((y-this.minY)/this.dY);
	var l=this.level,r=this.row,c=this.column;
	var index=this.photonInfoTable.length/10;
	this.table[k*r*c+i*c+j].push(index);
};
PhotonUniformGrid.prototype.createIndexTables=function()
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
PhotonUniformGrid.prototype.getPhotonInfoArray=function()
{
	var Float32Size=Float32Array.BYTES_PER_ELEMENT;
	var photonInfoArray=new Float32Array(this.photonInfoTable);
	photonInfoArray.size=Float32Size*this.photonInfoTable.length;
	return photonInfoArray;
};
PhotonUniformGrid.prototype.getPrimaryIndicesArray=function()
{
	var Uint32Size=Uint32Array.BYTES_PER_ELEMENT;
	var primaryIndicesArray=new Uint32Array(this.primaryIndexTable);
	primaryIndicesArray.size=Uint32Size*this.primaryIndexTable.length;
	return primaryIndicesArray;
};
PhotonUniformGrid.prototype.getSecondaryIndicesArray=function()
{
	var Uint32Size=Uint32Array.BYTES_PER_ELEMENT;
	var secondaryIndicesArray=new Uint32Array(this.secondaryIndexTable);
	secondaryIndicesArray.size=Uint32Size*this.secondaryIndexTable.length;
	return secondaryIndicesArray;
};
PhotonUniformGrid.prototype.printIndexTables=function()
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
PhotonUniformGrid.prototype.clamp=function(k,x,y){return k<x?x:k>y?y:k;};
PhotonUniformGrid.prototype.min=function(x,y){return x<=y?x:y;};
PhotonUniformGrid.prototype.max=function(x,y){return x>=y?x:y;};
