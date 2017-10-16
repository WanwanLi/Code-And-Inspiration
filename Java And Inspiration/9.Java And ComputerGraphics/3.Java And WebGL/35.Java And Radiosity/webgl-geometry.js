function Geometry()
{
	this.coordinates=[];
	this.reflectances=[];
	this.colors=[];
	this.indices=[];
	this.transformMatrix=new Matrix4();
	this.transformMatrixStack=[];
	this.normalIsReversed=false;
}
Geometry.prototype.loadIdentity=function()
{
	this.transformMatrix=new Matrix4();
};
Geometry.prototype.translate=function(x,y,z)
{
	var transformMatrix=new Matrix4();
	transformMatrix.setTranslate(x,y,z);
	this.transformMatrix.multiply(transformMatrix);
};
Geometry.prototype.rotate=function(angle,x,y,z)
{
	var transformMatrix=new Matrix4();
	transformMatrix.setRotate(angle*180/Math.PI,x,y,z);
	this.transformMatrix.multiply(transformMatrix);
};
Geometry.prototype.scale=function(x,y,z)
{
	var transformMatrix=new Matrix4();
	transformMatrix.setScale(x,y,z);
	this.transformMatrix.multiply(transformMatrix);
};
Geometry.prototype.pushMatrix=function()
{
	var transformMatrix=new Matrix4(this.transformMatrix);
	this.transformMatrixStack.push(transformMatrix);
};
Geometry.prototype.popMatrix=function()
{
	this.transformMatrix=this.transformMatrixStack.pop();
};
Geometry.prototype.reverseNormal=function()
{
	this.normalIsReversed=!this.normalIsReversed;
};
Geometry.prototype.addQuad=function(quad)
{
	var coordinates=quad.coordinates,reflectances=quad.reflectances,colors=quad.colors;
	var v=0,c=0,r=0,m=quad.row,n=quad.column,length=this.coordinates.length/3;
	var v00=new Vector4([coordinates[v++],coordinates[v++],coordinates[v++],1.0]);
	var v10=new Vector4([coordinates[v++],coordinates[v++],coordinates[v++],1.0]);
	var v11=new Vector4([coordinates[v++],coordinates[v++],coordinates[v++],1.0]);
	var v01=new Vector4([coordinates[v++],coordinates[v++],coordinates[v++],1.0]);
	var r00=new Vector3([reflectances[r++],reflectances[r++],reflectances[r++]]);
	var r10=new Vector3([reflectances[r++],reflectances[r++],reflectances[r++]]);
	var r11=new Vector3([reflectances[r++],reflectances[r++],reflectances[r++]]);
	var r01=new Vector3([reflectances[r++],reflectances[r++],reflectances[r++]]);
	var c00=new Vector3([colors[c++],colors[c++],colors[c++]]);
	var c10=new Vector3([colors[c++],colors[c++],colors[c++]]);
	var c11=new Vector3([colors[c++],colors[c++],colors[c++]]);
	var c01=new Vector3([colors[c++],colors[c++],colors[c++]]);
	var p00=this.transformMatrix.multiplyVector4(v00).xyz();
	var p10=this.transformMatrix.multiplyVector4(v10).xyz();
	var p11=this.transformMatrix.multiplyVector4(v11).xyz();
	var p01=this.transformMatrix.multiplyVector4(v01).xyz();
	for(var i=0;i<m;i++)
	{
		for(var j=0;j<n;j++)
		{
			var coordinate=this.getBilinearInterpolatedVector(p00,p01,p10,p11,i,j,m,n);
			var reflectance=this.getBilinearInterpolatedVector(r00,r01,r10,r11,i,j,m,n);
			var color=this.getBilinearInterpolatedVector(c00,c01,c10,c11,i,j,m,n);
			this.coordinates.push(coordinate.x());
			this.coordinates.push(coordinate.y());
			this.coordinates.push(coordinate.z());
			this.reflectances.push(reflectance.x());
			this.reflectances.push(reflectance.y());
			this.reflectances.push(reflectance.z());
			this.colors.push(color.x());
			this.colors.push(color.y());
			this.colors.push(color.z());
		}
	}
	if(this.normalIsReversed)
	{
		for(var i=0;i<m-1;i++)
		{
			for(var j=0;j<n-1;j++)
			{
				this.indices.push(length+(i+0)*n+(j+0));
				this.indices.push(length+(i+0)*n+(j+1));
				this.indices.push(length+(i+1)*n+(j+1));
	
				this.indices.push(length+(i+1)*n+(j+1));
				this.indices.push(length+(i+1)*n+(j+0));
				this.indices.push(length+(i+0)*n+(j+0));
			}
		}
	}
	else
	{
		for(var i=0;i<m-1;i++)
		{
			for(var j=0;j<n-1;j++)
			{
				this.indices.push(length+(i+0)*n+(j+0));
				this.indices.push(length+(i+1)*n+(j+0));
				this.indices.push(length+(i+1)*n+(j+1));
	
				this.indices.push(length+(i+1)*n+(j+1));
				this.indices.push(length+(i+0)*n+(j+1));
				this.indices.push(length+(i+0)*n+(j+0));
			}
		}
	}
};
Geometry.prototype.getBilinearInterpolatedVector=function(v00,v01,v10,v11,i,j,m,n)
{
	var u=i*1.0/(m-1),v=j*1.0/(n-1);
	var vU0=v00.interpolate(v10,u);
	var vU1=v01.interpolate(v11,u);
	return vU0.interpolate(vU1,v);
};
Geometry.prototype.getGeometryInfo=function()
{
	var geometryInfo=
	{
		coordinates:this.coordinates,
		reflectances:this.reflectances,
		colors:this.colors,
		indices:this.indices
	};
	return geometryInfo;
};
Geometry.prototype.addObject=function(object)
{
	var coordinates=object.coordinates,indices=object.indices;
	var reflectance=object.reflectance,color=object.color;
	var length=this.coordinates.length/3,v=0;
	for(var i=0;i<coordinates.length/3;i++)
	{
		var position=new Vector4([coordinates[v++],coordinates[v++],coordinates[v++],1.0]);
		var coordinate=this.transformMatrix.multiplyVector4(position).xyz();
		this.coordinates.push(coordinate.x());
		this.coordinates.push(coordinate.y());
		this.coordinates.push(coordinate.z());
		this.reflectances.push(reflectance[0]);
		this.reflectances.push(reflectance[1]);
		this.reflectances.push(reflectance[2]);
		this.colors.push(color[0]);
		this.colors.push(color[1]);
		this.colors.push(color[2]);
	}
	for(var i=0;i<indices.length;i++)this.indices.push(length+indices[i]);
};
