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
	this.createFormFactors();
	this.createRadiosity();
	this.updateVertexColors();
	var geometryInfo=
	{
		coordinates:this.coordinates,
		colors:this.colors,
		indices:this.indices
	};
	return geometryInfo;
};
Radiosity.prototype.getRGBvalues=function(pixel)
{
	var red=(pixel>>16)&0xff;
	var green=(pixel>>8)&0xff;
	var blue=pixel&0xff;
	var r=(red+0.0)/255.0;
	var g=(green+0.0)/255.0;
	var b=(blue+0.0)/255.0;
	return [r,g,b];
};
Radiosity.prototype.getPixelValue=function(color)
{
	var r=color[0];
	var g=color[1];
	var b=color[2];
	return (r<<16)|(g<<8)|b;
};
Radiosity.prototype.getFormFactorsGeometryInfo=function()
{
	var newCoordinates=[],newColors=[],c=0;
	this.maxInteger=(1<<24)-1;
	for(var i=0;i<this.length;i++)
	{
		var i0=this.indices[c++];
		var i1=this.indices[c++];
		var i2=this.indices[c++];
		newCoordinates.push(this.coordinates[i0*3+0]);
		newCoordinates.push(this.coordinates[i0*3+1]);
		newCoordinates.push(this.coordinates[i0*3+2]);
		newCoordinates.push(this.coordinates[i1*3+0]);
		newCoordinates.push(this.coordinates[i1*3+1]);
		newCoordinates.push(this.coordinates[i1*3+2]);
		newCoordinates.push(this.coordinates[i2*3+0]);
		newCoordinates.push(this.coordinates[i2*3+1]);
		newCoordinates.push(this.coordinates[i2*3+2]);
		var color=this.getRGBvalues(i);
		newColors.push(color[0]);
		newColors.push(color[1]);
		newColors.push(color[2]);
		newColors.push(color[0]);
		newColors.push(color[1]);
		newColors.push(color[2]);
		newColors.push(color[0]);
		newColors.push(color[1]);
		newColors.push(color[2]);
	}
	var geometryInfo=
	{
		coordinates:newCoordinates,
		colors:newColors
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
	var c=0;
	for(var i=0;i<this.length;i++)
	{
		var i0=this.indices[c++];
		var i1=this.indices[c++];
		var i2=this.indices[c++];
		var p0=this.getVector(this.coordinates,i0);
		var p1=this.getVector(this.coordinates,i1);
		var p2=this.getVector(this.coordinates,i2);
		var p=this.getAverageVector(p0,p1,p2);
		p2.sub(p1);p1.sub(p0);
		p0.cross(p1,p2);
		this.positions.push(p);
		this.areas.push(p0.length());
		this.normals.push(p0.normalize());
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
Radiosity.prototype.createFormFactors=function()
{
	this.formFactors=[];
	this.shootingTable=[];
	for(var i=0;i<this.length;i++)
	{
		this.formFactors[i]=null;
		this.shootingTable[i]=[];
	}
};
Radiosity.prototype.createCameras=function(i)
{
	this.cameras=[];
	var normal=this.normals[i];
	var tangent=normal.vertical();
	var bitangent=normal.crossProduct(tangent);
	this.cameras[0]=
	{
		eye:this.positions[i],
		center:this.positions[i].sum(normal),
		up:tangent
	};
	this.cameras[1]=
	{
		eye:this.positions[i],
		center:this.positions[i].sum(tangent),
		up:normal
	};
	this.cameras[2]=
	{
		eye:this.positions[i],
		center:this.positions[i].sum(bitangent),
		up:normal
	};
	this.cameras[3]=
	{
		eye:this.positions[i],
		center:this.positions[i].minus(tangent),
		up:normal
	};
	this.cameras[4]=
	{
		eye:this.positions[i],
		center:this.positions[i].minus(bitangent),
		up:normal
	};
};
Radiosity.prototype.setCameraInfo=function(n)
{
	this.framebuffer.eyeX=this.cameras[n].eye.x();
	this.framebuffer.eyeY=this.cameras[n].eye.y();
	this.framebuffer.eyeZ=this.cameras[n].eye.z();
	this.framebuffer.centerX=this.cameras[n].center.x();
	this.framebuffer.centerY=this.cameras[n].center.y();
	this.framebuffer.centerZ=this.cameras[n].center.z();
	this.framebuffer.upX=this.cameras[n].up.x();
	this.framebuffer.upY=this.cameras[n].up.y();
	this.framebuffer.upZ=this.cameras[n].up.z();
};
Radiosity.prototype.countFormFactors=function(i)
{
	var pixels=this.framebuffer.pixels;
	var height=this.framebuffer.height;
	var width=this.framebuffer.width;
	for(var h=0;h<height;h++)
	{
		for(var w=0;w<width;w++)
		{
			var r=pixels[(h*width+w)*4+0];
			var g=pixels[(h*width+w)*4+1];
			var b=pixels[(h*width+w)*4+2];
			var pixel=this.getPixelValue([r,g,b]);
			var k=pixel;if(k>=this.length)k=this.length-1;
			this.formFactors[i][k]++;
		}
	}
}
Radiosity.prototype.getFormFactors=function(i)
{
	if(this.formFactors[i]!=null)return;
	var K=this.globalReflectance,Ai=this.areas[i];
	this.formFactors[i]=new Float32Array(this.length);
	this.createCameras(i);
	for(var n=0;n<5;n++)
	{
		this.setCameraInfo(n);
		this.renderFramebuffer();
		this.countFormFactors(i);
	}
	for(var j=0;j<this.length;j++)
	{
		if(i==j)continue;
		var Fij=this.formFactor(i,j);
		this.formFactors[i][j]*=K*Ai*Fij;
		if(this.formFactors[i][j]>0.0)this.shootingTable[i].push(j);
	}
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
	return cosX*cosY/(Math.PI*rXY*rXY);
};
Radiosity.prototype.updateVertexColors=function()
{
	for(var i=0;i<this.renderingTimes;i++)
	{
		this.getMaxExitanceBrightness();
		if(this.maxExitanceBrightness<this.validExitanceBrightness)break;
		this.getFormFactors(this.maxExitanceIndex);
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
