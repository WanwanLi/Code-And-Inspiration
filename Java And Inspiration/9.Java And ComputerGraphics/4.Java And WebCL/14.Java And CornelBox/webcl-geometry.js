function Geometry()
{
	this.coordinates=[];
	this.normals=[];
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
	var coordinates=quad.coordinates,normals=quad.normals,colors=quad.colors;
	var v=0,c=0,r=0,m=quad.row,n=quad.column,length=this.coordinates.length/3;
	var v00=new Vector4([coordinates[v++],coordinates[v++],coordinates[v++],1.0]);
	var v10=new Vector4([coordinates[v++],coordinates[v++],coordinates[v++],1.0]);
	var v11=new Vector4([coordinates[v++],coordinates[v++],coordinates[v++],1.0]);
	var v01=new Vector4([coordinates[v++],coordinates[v++],coordinates[v++],1.0]);
	var n00=new Vector3([normals[r++],normals[r++],normals[r++]]);
	var n10=new Vector3([normals[r++],normals[r++],normals[r++]]);
	var n11=new Vector3([normals[r++],normals[r++],normals[r++]]);
	var n01=new Vector3([normals[r++],normals[r++],normals[r++]]);
	var c00=new Vector3([colors[c++],colors[c++],colors[c++]]);
	var c10=new Vector3([colors[c++],colors[c++],colors[c++]]);
	var c11=new Vector3([colors[c++],colors[c++],colors[c++]]);
	var c01=new Vector3([colors[c++],colors[c++],colors[c++]]);
	var p00=this.transformMatrix.multiplyVector4(v00).xyz();
	var p10=this.transformMatrix.multiplyVector4(v10).xyz();
	var p11=this.transformMatrix.multiplyVector4(v11).xyz();
	var p01=this.transformMatrix.multiplyVector4(v01).xyz();
	if(this.normalIsReversed)
	{
		n00.reverse();
		n01.reverse();
		n11.reverse();
		n10.reverse();
	}
	for(var i=0;i<m;i++)
	{
		for(var j=0;j<n;j++)
		{
			var coordinate=this.getBilinearInterpolatedVector(p00,p01,p10,p11,i,j,m,n);
			var normal=this.getBilinearInterpolatedVector(n00,n01,n10,n11,i,j,m,n);
			var color=this.getBilinearInterpolatedVector(c00,c01,c10,c11,i,j,m,n);
			this.coordinates.push(coordinate.x());
			this.coordinates.push(coordinate.y());
			this.coordinates.push(coordinate.z());
			this.normals.push(normal.x());
			this.normals.push(normal.y());
			this.normals.push(normal.z());
			this.colors.push(color.x());
			this.colors.push(color.y());
			this.colors.push(color.z());
		}
	}
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
		normals:this.normals,
		colors:this.colors,
		indices:this.indices
	};
	return geometryInfo;
};
Geometry.prototype.addObject=function(object)
{
	var coordinates=object.coordinates,indices=object.indices;
	var normals=object.normals;this.colors=object.colors;
	var length=this.coordinates.length/3,v=0,n=0;
	for(var i=0;i<coordinates.length/3;i++)
	{
		var coordinate4fv=new Vector4([coordinates[v++],coordinates[v++],coordinates[v++],1.0]);
		var normal4fv=new Vector4([normals[n++],normals[n++],normals[n++],0.0]);
		var coordinate=this.transformMatrix.multiplyVector4(coordinate4fv).xyz();
		var normal=this.transformMatrix.multiplyVector4(normal4fv).xyz();
		this.coordinates.push(coordinate.x());
		this.coordinates.push(coordinate.y());
		this.coordinates.push(coordinate.z());
		this.normals.push(normal.x());
		this.normals.push(normal.y());
		this.normals.push(normal.z());
	}
	for(var i=0;i<indices.length;i++)this.indices.push(length+indices[i]);
};
Geometry.prototype.getCylinder=function(radius,height,slices)
{
	var coordinates=[],normals=[],indices=[];
	var a=2*Math.PI/(slices-1);
	var x,y1=height/2,y0=-y1,z;
	for(var i=0;i<slices;i++)
	{
		x=radius*Math.cos(a*i);
		z=radius*Math.sin(a*i);
		coordinates.push(x,y0,z);
		normals.push(x/radius,0,z/radius);
	}
	for(var i=0;i<slices;i++)
	{
		x=radius*Math.cos(a*i);
		z=radius*Math.sin(a*i);
		coordinates.push(x,y1,z);
		normals.push(x/radius,0,z/radius);
	}
	for(var j=0;j<slices-1;j++)
	{
		indices.push(0*slices+j+0);
		indices.push(0*slices+j+1);
		indices.push(1*slices+j+1);

		indices.push(1*slices+j+1);
		indices.push(1*slices+j+0);
		indices.push(0*slices+j+0);
	}
	var i0=coordinates.length/3;
	coordinates.push(0,y0,0);
	normals.push(0,-1,0);
	for(var i=0;i<slices;i++)
	{
		x=radius*Math.cos(a*i);
		z=radius*Math.sin(a*i);
		coordinates.push(x,y0,z);
		normals.push(0,-1,0);
	}
	for(var j=0;j<slices-1;j++)
	{
		indices.push(i0+1+j+0);
		indices.push(i0+1+j+1);
		indices.push(i0);
	}
	var i1=coordinates.length/3;
	coordinates.push(0,y1,0);
	normals.push(0,1,0);
	for(var i=0;i<slices;i++)
	{
		x=radius*Math.cos(a*i);
		z=radius*Math.sin(a*i);
		coordinates.push(x,y1,z);
		normals.push(0,1,0);
	}
	for(var j=0;j<slices-1;j++)
	{
		indices.push(i1+1+j+0);
		indices.push(i1+1+j+1);
		indices.push(i1);
	}
	var geometryInfo=
	{
		coordinates:coordinates,
		normals:normals,
		color:[1,1,1],
		indices:indices
	};
	return geometryInfo;
}
