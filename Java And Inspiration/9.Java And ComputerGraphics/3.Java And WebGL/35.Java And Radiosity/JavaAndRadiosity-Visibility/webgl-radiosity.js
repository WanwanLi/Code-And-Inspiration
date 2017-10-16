function Radiosity(geometryInfo)
{
	this.coordinates=geometryInfo.coordinates;
	this.reflectances=geometryInfo.reflectances;
	this.normals=geometryInfo.normals;
	this.colors=geometryInfo.colors;
	this.indices=geometryInfo.indices;
	this.length=this.indices.length/3;
	this.globalReflectance=10.0;
	this.renderingTimes=30;
	this.validExitanceBrightness=0.01;
}
Radiosity.prototype.setGlobalReflectance=function(reflectance)
{
	this.globalReflectance=reflectance*10.0;
}
Radiosity.prototype.setRenderingTimes=function(times)
{
	this.renderingTimes=times;
}
Radiosity.prototype.getGeometryInfo=function()
{
	this.createElements();
	this.createRadiosity();
	this.createFormFactors();
	this.updateVertexColors();
	var geometryInfo=
	{
		coordinates:this.coordinates,
		colors:this.colors,
		indices:this.indices
	};
	return geometryInfo;
};
Radiosity.prototype.createElements=function()
{
	if(this.normals==null){this.createNormals();return;}
	var positions=[];
	var normals=[];
	var c=0;
	for(var i=0;i<this.length;i++)
	{
		var i0=this.indices[c++];
		var i1=this.indices[c++];
		var i2=this.indices[c++];
		var p0=this.getVector(this.coordinates,i0);
		var p1=this.getVector(this.coordinates,i1);
		var p2=this.getVector(this.coordinates,i2);
		var n0=this.getVector(this.normals,i0);
		var n1=this.getVector(this.normals,i1);
		var n2=this.getVector(this.normals,i2);
		var p=this.getAverageVector(p0,p1,p2);
		var n=this.getAverageVector(n0,n1,n2);
		positions.push(p);
		normals.push(n.normalize());
	}
	this.positions=positions;
	this.normals=normals;
};
Radiosity.prototype.createNormals=function()
{
	this.positions=[];
	this.normals=[];
	this.areas=[];
	this.triangles=[];
	var c=0;
	for(var i=0;i<this.length;i++)
	{
		var i0=this.indices[c++];
		var i1=this.indices[c++];
		var i2=this.indices[c++];
		var p0=this.getVector(this.coordinates,i0);
		var p1=this.getVector(this.coordinates,i1);
		var p2=this.getVector(this.coordinates,i2);
		var position=this.getAverageVector(p0,p1,p2);
		var normal=this.getNormalVector(p0,p1,p2);
		var area=normal.length();normal.normalize();
		var triangle=this.getTriangle(p0,p1,p2,normal);
		this.positions.push(position);
		this.normals.push(normal);
		this.areas.push(area);
		this.triangles.push(triangle);
	}
};
Radiosity.prototype.getVector=function(array,i)
{
	var elements=
	[
		array[i*3+0],
		array[i*3+1],
		array[i*3+2]
	];
	return new Vector3(elements);
};
Radiosity.prototype.getAverageVector=function(v0,v1,v2)
{
	var v=v0.xyz();
	v.add(v1);v.add(v2);
	v.mul(1.0/3.0);
	return v;
};
Radiosity.prototype.getNormalVector=function(v0,v1,v2)
{
	var p0=v0.xyz();
	var p1=v1.xyz();
	var p2=v2.xyz();
	p2.sub(p1);p1.sub(p0);
	p0.cross(p1,p2);
	return p0;
};
Radiosity.prototype.addVector=function(array,i,vector)
{
	array[i*3+0]+=vector.elements[0];
	array[i*3+1]+=vector.elements[1];
	array[i*3+2]+=vector.elements[2];
};
Radiosity.prototype.mulScaler=function(array,i,scaler)
{
	array[i*3+0]*=scaler;
	array[i*3+1]*=scaler;
	array[i*3+2]*=scaler;
};
Radiosity.prototype.getTriangle=function(p0,p1,p2,normal)
{
	var triangle=
	{
		vertex0:p0,
		vertex1:p1,
		vertex2:p2,
		normal:normal
	};
	return triangle;
};
Radiosity.prototype.formFactor=function(i,j)
{
	var x=this.positions[i].xyz();
	var y=this.positions[j].xyz();
	y.sub(x);
	var rXY=y.length();
	y.normalize();
	var n=this.normals[i];
	var cosX=y.dot(n);
	n=this.normals[j];
	y.mul(-1.0);
	var cosY=y.dot(n);
	if(cosX<=0||cosY<=0)return 0;
	return Fij=cosX*cosY/(Math.PI*rXY*rXY);
};
Radiosity.prototype.getVisibility=function(i,j)
{
	var direction=this.positions[j].subtract(this.positions[i]);
	var distance_ij=direction.length();direction.normalize();
	var shootingList=this.shootingTable[i];
	for(var k=0;k<shootingList.length;k++)
	{
		var triangle=this.triangles[shootingList[k]];
		var distance_ik=this.positions[i].intersectWith(triangle,direction);
		if(distance_ik<0);
		else if(distance_ij>=distance_ik)return -1;
		else if(distance_ij<distance_ik)return k;
	}
	return this.length;
}
Radiosity.prototype.createFormFactors=function()
{
	this.formFactors=[];
	this.shootingTable=[];
	var K=this.globalReflectance;
	for(var i=0;i<this.length;i++)
	{
		this.formFactors[i]=new Float32Array(this.length);
		this.shootingTable[i]=[];
	}
	for(var i=0;i<this.length;i++)
	{
		for(var j=i+1;j<this.length;j++)
		{
			var Ai=this.areas[i],Aj=this.areas[j];
			var Fij=this.formFactor(i,j),Fji=Fij;
			this.formFactors[i][j]=K*Ai*Fij;
			this.formFactors[j][i]=K*Aj*Fji;
		}
	}
};
Radiosity.prototype.createShootingTable=function(i)
{
	if(this.shootingTable[i].length>0)return;
	for(var j=0;j<this.length;j++)
	{
		if(j==i)continue;
		if(this.formFactors[i][j]>=0)
		{
			var visibility=this.getVisibility(i,j);
			if(visibility==this.length)this.shootingTable[i].push(j);
			else if(visibility>=0)this.shootingTable[i][visibility]=j;
			else if(visibility<0);
		}
	}
};
Radiosity.prototype.updateVertexColors=function()
{
	var times=this.renderingTimes;
	for(var i=0;i<times;i++)
	{
		this.getMaxExitanceBrightness();
		if(this.maxExitanceBrightness<this.validExitanceBrightness)break;
		this.createShootingTable(this.maxExitanceIndex);
		this.shootExitance(this.maxExitanceIndex);
	}
	this.getVertexIrradiances();
	for(var i=0;i<this.colors.length;i++)
	{
		this.colors[i]+=this.vertexIrradiances[i];
	}
};
Radiosity.prototype.createRadiosity=function()
{
	var k=0;
	this.exitances=[];
	this.irradiances=[];
	this.reflectance=[];
	this.brightness=[];
	this.maxExitanceIndex=0;
	this.maxExitanceBrightness=0;
	for(var i=0;i<this.length;i++)
	{
		var i0=this.indices[k++];
		var i1=this.indices[k++];
		var i2=this.indices[k++];
		var c0=this.getVector(this.colors,i0);
		var c1=this.getVector(this.colors,i1);
		var c2=this.getVector(this.colors,i2);
		var r0=this.getVector(this.reflectances,i0);
		var r1=this.getVector(this.reflectances,i1);
		var r2=this.getVector(this.reflectances,i2);
		var c=this.getAverageVector(c0,c1,c2);
		var r=this.getAverageVector(r0,r1,r2);
		this.exitances.push(c);
		this.reflectance.push(r);
		this.brightness.push(c.max());
		this.irradiances.push(new Vector3([0,0,0]));
	}
};
Radiosity.prototype.getMaxExitanceBrightness=function()
{
	this.maxExitanceIndex=0;
	this.maxExitanceBrightness=0;
	for(var i=0;i<this.length;i++)
	{
		if(this.brightness[i]>this.maxExitanceBrightness)
		{
			this.maxExitanceBrightness=this.brightness[i];
			this.maxExitanceIndex=i;
		}
	}
};
Radiosity.prototype.shootExitance=function(i)
{
	this.maxExitanceBrightness=0;
	var shootingList=this.shootingTable[i];
	for(var k=0;k<shootingList.length;k++)
	{
		var j=shootingList[k];
		var irradiance=this.exitances[i].xyz();
		var Fij=this.formFactors[i][j];
		irradiance.mul(Fij);
		irradiance.multiply(this.reflectance[j]);
		this.irradiances[j].add(irradiance);
		this.exitances[j].add(irradiance);
		this.brightness[j]=this.exitances[j].max();
	}
	this.exitances[i].set([0,0,0]);
	this.brightness[i]=0;
};
Radiosity.prototype.getVertexIrradiances=function()
{
	var vertexCounter=new Uint16Array(this.length*3);
	this.vertexIrradiances=new Float32Array(this.length*3*3);
	for(var i=0;i<this.length;i++)
	{
		if(this.irradiances[i].max()>0)
		{
			var i0=this.indices[i*3+0];
			var i1=this.indices[i*3+1];
			var i2=this.indices[i*3+2];
			vertexCounter[i0]++;
			vertexCounter[i1]++;
			vertexCounter[i2]++;
			this.irradiances[i].divide(this.reflectance[i]);
		}
	}
	for(var i=0;i<this.length;i++)
	{
		if(this.irradiances[i].max()>0)
		{
			var i0=this.indices[i*3+0];
			var i1=this.indices[i*3+1];
			var i2=this.indices[i*3+2];
			var reflectance0=this.getVector(this.reflectances,i0);
			var reflectance1=this.getVector(this.reflectances,i1);
			var reflectance2=this.getVector(this.reflectances,i2);
			var vertexIrradiance0=this.irradiances[i].xyz();
			var vertexIrradiance1=this.irradiances[i].xyz();
			var vertexIrradiance2=this.irradiances[i].xyz();
			vertexIrradiance0.multiply(reflectance0);
			vertexIrradiance1.multiply(reflectance1);
			vertexIrradiance2.multiply(reflectance2);
			vertexIrradiance0.mul(1.0/vertexCounter[i0]);
			vertexIrradiance1.mul(1.0/vertexCounter[i1]);
			vertexIrradiance2.mul(1.0/vertexCounter[i2]);
			this.addVector(this.vertexIrradiances,i0,vertexIrradiance0);
			this.addVector(this.vertexIrradiances,i1,vertexIrradiance1);
			this.addVector(this.vertexIrradiances,i2,vertexIrradiance2);
		}
	}
};
