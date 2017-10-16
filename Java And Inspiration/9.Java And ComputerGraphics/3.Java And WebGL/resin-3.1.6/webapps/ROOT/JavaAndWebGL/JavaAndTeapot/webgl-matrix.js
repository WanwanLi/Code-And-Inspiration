var Matrix4=function(param)
{
	if(param&&typeof param === 'object'&&param.hasOwnProperty('elements'))
	{
		this.elements=new Float32Array(16);
		for(var i=0;i<16;i++)this.elements[i]=param.elements[i];
	}
	else this.elements=new Float32Array([1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1]);
};
Matrix4.prototype.set=function(param)
{
	for(var i=0;i<16;i++)this.elements[i]=param.elements[i];
	return this;
};
Matrix4.prototype.setIdentity=function()
{
	var e=this.elements;
	e[0]=1;   e[4]=0;   e[8] =0;   e[12]=0;
	e[1]=0;   e[5]=1;   e[9] =0;   e[13]=0;
	e[2]=0;   e[6]=0;   e[10]=1;   e[14]=0;
	e[3]=0;   e[7]=0;   e[11]=0;   e[15]=1;
	return this;
};
Matrix4.prototype.multiply=function(param)
{
	var a0j,a1j,a2j,a3j;
	var a=this.elements;
	var b=param.elements;
	var e=new Float32Array(16);
	for(var j=0;j<4;j++)
	{
		a0j=a[0+j]; a1j=a[4+j]; a2j=a[8+j]; a3j=a[12+j];
		e[0+j]=a0j*b[0]+a1j*b[1]+a2j*b[2]+a3j*b[3];
		e[4+j]=a0j*b[4]+a1j*b[5]+a2j*b[6]+a3j*b[7];
		e[8+j]=a0j*b[8]+a1j*b[9]+a2j*b[10]+a3j*b[11];
		e[12+j]=a0j*b[12]+a1j*b[13]+a2j*b[14]+a3j*b[15];
	}
	this.elements=e;
	return this;
};
Matrix4.prototype.concat=Matrix4.prototype.multiply;
Matrix4.prototype.multiplyVector3=function(param)
{
	var e=this.elements;
	var p=param.elements;
	var v=new Vector3();
	v.elements[0]=p[0]*e[0]+p[1]*e[4]+p[2]*e[ 8]+e[11];
	v.elements[1]=p[0]*e[1]+p[1]*e[5]+p[2]*e[ 9]+e[12];
	v.elements[2]=p[0]*e[2]+p[1]*e[6]+p[2]*e[10]+e[13];
	return v;
};
Matrix4.prototype.multiplyVector4=function(param)
{
	var e=this.elements;
	var p=param.elements;
	var v=new Vector4();
	v.elements[0]=p[0]*e[0]+p[1]*e[4]+p[2]*e[ 8]+p[3]*e[12];
	v.elements[1]=p[0]*e[1]+p[1]*e[5]+p[2]*e[ 9]+p[3]*e[13];
	v.elements[2]=p[0]*e[2]+p[1]*e[6]+p[2]*e[10]+p[3]*e[14];
	v.elements[3]=p[0]*e[3]+p[1]*e[7]+p[2]*e[11]+p[3]*e[15];
	return v;
};
Matrix4.prototype.transpose=function()
{
	var e=this.elements,t;
	t=e[ 1];  e[ 1]=e[ 4];  e[ 4]=t;
	t=e[ 2];  e[ 2]=e[ 8];  e[ 8]=t;
	t=e[ 3];  e[ 3]=e[12];  e[12]=t;
	t=e[ 6];  e[ 6]=e[ 9];  e[ 9]=t;
	t=e[ 7];  e[ 7]=e[13];  e[13]=t;
	t=e[11];  e[11]=e[14];  e[14]=t;
	return this;
};
Matrix4.prototype.setInverseOf=function(param)
{
	var s=param.elements;
	var e=new Float32Array(16);

	e[0] =  s[5]*s[10]*s[15]-s[5] *s[11]*s[14]-s[9] *s[6]*s[15] +s[9]*s[7] *s[14]+s[13]*s[6] *s[11]-s[13]*s[7]*s[10];
	e[4] =- s[4]*s[10]*s[15]+s[4] *s[11]*s[14]+s[8] *s[6]*s[15]-s[8]*s[7] *s[14]-s[12]*s[6] *s[11]+s[12]*s[7]*s[10];
	e[8] =  s[4]*s[9] *s[15]-s[4] *s[11]*s[13]-s[8] *s[5]*s[15] +s[8]*s[7] *s[13]+s[12]*s[5] *s[11]-s[12]*s[7]*s[9];
	e[12]=- s[4]*s[9] *s[14]+s[4] *s[10]*s[13]+s[8] *s[5]*s[14]-s[8]*s[6] *s[13]-s[12]*s[5] *s[10]+s[12]*s[6]*s[9];

	e[1] =- s[1]*s[10]*s[15]+s[1] *s[11]*s[14]+s[9] *s[2]*s[15]-s[9]*s[3] *s[14]-s[13]*s[2] *s[11]+s[13]*s[3]*s[10];
	e[5] =  s[0]*s[10]*s[15]-s[0] *s[11]*s[14]-s[8] *s[2]*s[15] +s[8]*s[3] *s[14]+s[12]*s[2] *s[11]-s[12]*s[3]*s[10];
	e[9] =- s[0]*s[9] *s[15]+s[0] *s[11]*s[13]+s[8] *s[1]*s[15]-s[8]*s[3] *s[13]-s[12]*s[1] *s[11]+s[12]*s[3]*s[9];
	e[13]=  s[0]*s[9] *s[14]-s[0] *s[10]*s[13]-s[8] *s[1]*s[14] +s[8]*s[2] *s[13]+s[12]*s[1] *s[10]-s[12]*s[2]*s[9];

	e[2] =  s[1]*s[6]*s[15]-s[1] *s[7]*s[14]-s[5] *s[2]*s[15] +s[5]*s[3]*s[14]+s[13]*s[2]*s[7] -s[13]*s[3]*s[6];
	e[6] =- s[0]*s[6]*s[15]+s[0] *s[7]*s[14]+s[4] *s[2]*s[15]-s[4]*s[3]*s[14]-s[12]*s[2]*s[7]+s[12]*s[3]*s[6];
	e[10]=  s[0]*s[5]*s[15]-s[0] *s[7]*s[13]-s[4] *s[1]*s[15] +s[4]*s[3]*s[13]+s[12]*s[1]*s[7] -s[12]*s[3]*s[5];
	e[14]=- s[0]*s[5]*s[14]+s[0] *s[6]*s[13]+s[4] *s[1]*s[14]-s[4]*s[2]*s[13]-s[12]*s[1]*s[6]+s[12]*s[2]*s[5];

	e[3] =- s[1]*s[6]*s[11]+s[1]*s[7]*s[10]+s[5]*s[2]*s[11]-s[5]*s[3]*s[10]-s[9]*s[2]*s[7]+s[9]*s[3]*s[6];
	e[7] =  s[0]*s[6]*s[11]-s[0]*s[7]*s[10]-s[4]*s[2]*s[11] +s[4]*s[3]*s[10]+s[8]*s[2]*s[7] -s[8]*s[3]*s[6];
	e[11]=- s[0]*s[5]*s[11]+s[0]*s[7]*s[9]+s[4]*s[1]*s[11]-s[4]*s[3]*s[9] -s[8]*s[1]*s[7]+s[8]*s[3]*s[5];
	e[15]=  s[0]*s[5]*s[10]-s[0]*s[6]*s[9] -s[4]*s[1]*s[10] +s[4]*s[2]*s[9]+s[8]*s[1]*s[6] -s[8]*s[2]*s[5];

	var det=s[0]*e[0]+s[1]*e[4]+s[2]*e[8]+s[3]*e[12];
	if(det==0)return this;
	for(var i=0;i<16;i++)this.elements[i]=e[i]/det;
	return this;
};
Matrix4.prototype.invert=function()
{
	return this.setInverseOf(this);
};
Matrix4.prototype.setOrtho=function(left,right,bottom,top,near,far)
{
	if(left === right||bottom===top||near===far)throw 'null frustum';
	var dx=right-left,dy=top-bottom,dz=far-near,e=this.elements;
	e[0] =2/dx;		e[1] =0;			e[2] =0;			e[3] =0;
	e[4] =0;			e[5] =2/dy;		e[6] =0;			e[7] =0;
	e[8] =0;			e[9] =0;			e[10]=-2/dz;		e[11]=0;
	e[12]=-(right+left)/dx;	e[13]=-(top+bottom)/dy;	e[14]=-(far+near)/dz;	e[15]=1;
	return this;
};
Matrix4.prototype.ortho=function(left, right, bottom, top, near, far)
{
	return this.multiply(new Matrix4().setOrtho(left,right,bottom,top,near,far));
};
Matrix4.prototype.setFrustum=function(left, right, bottom, top, near, far)
{
	if (left === right || top === bottom || near === far||near <= 0||far <= 0)throw 'null frustum';
	var dx=right-left,dy=top-bottom,dz=far-near,e=this.elements;
	e[ 0]=2*near/dx;		e[ 1]=0;			e[ 2]=0;			e[ 3]=0;
	e[ 4]=0;			e[ 5]=2*near/dy;		e[ 6]=0;			e[ 7]=0;
	e[ 8]=(right+left)/dx;	e[ 9]=(top+bottom)/dy;	e[10]=-(far+near)/dz;	e[11]=-1;
	e[12]=0;			e[13]=0;			e[14]=-2*near*far/dz;	e[15]=0;
	return this;
};
Matrix4.prototype.frustum=function(left, right, bottom, top, near, far)
{
	return this.multiply(new Matrix4().setFrustum(left, right, bottom, top, near, far));
};
Matrix4.prototype.setPerspective=function(fovy, aspect, near, far)
{
	if (near == far || aspect == 0 || near <= 0||far <= 0)throw 'null frustum';
	fovy=Math.PI*fovy/180/2;
	var sinY=Math.sin(fovy);
	if(sinY==0)throw 'null frustum';
	var dz=far-near,cotY=Math.cos(fovy)/sinY,e=this.elements;
	e[0]=cotY/aspect;	e[1] =0;		e[2] =0;			e[3] =0;
	e[4] =0;		e[5] =cotY;	e[6] =0;			e[7] =0;
	e[8] =0;		e[9] =0;		e[10]=-(far+near)/dz;	e[11]=-1;
	e[12]=0;		e[13]=0;		e[14]=-2*near*far/dz;	e[15]=0;
	return this;
};
Matrix4.prototype.perspective=function(fovy, aspect, near, far)
{
	return this.multiply(new Matrix4().setPerspective(fovy, aspect, near, far));
};
Matrix4.prototype.setScale=function(x,y,z)
{
	var e=this.elements;
	e[0]=x;  e[4]=0;  e[8] =0;  e[12]=0;
	e[1]=0;  e[5]=y;  e[9] =0;  e[13]=0;
	e[2]=0;  e[6]=0;  e[10]=z;  e[14]=0;
	e[3]=0;  e[7]=0;  e[11]=0;  e[15]=1;
	return this;
};
Matrix4.prototype.scale=function(x, y, z)
{
	return this.multiply(new Matrix4().setScale(x, y, z));
};
Matrix4.prototype.setTranslate=function(x, y, z)
{
	var e=this.elements;
	e[0]=1;  e[4]=0;  e[8] =0;  e[12]=x;
	e[1]=0;  e[5]=1;  e[9] =0;  e[13]=y;
	e[2]=0;  e[6]=0;  e[10]=1;  e[14]=z;
	e[3]=0;  e[7]=0;  e[11]=0;  e[15]=1;
	return this;
};
Matrix4.prototype.translate=function(x, y, z)
{
	return this.multiply(new Matrix4().setTranslate(x, y, z));
};
Matrix4.prototype.setRotate=function(angle, x, y, z)
{
	var e=this.elements, angle=Math.PI*angle / 180;
	var s=Math.sin(angle),c=Math.cos(angle);
	if (x!=0 && y==0 && z==0)
	{
		if(x<0)s=-s;
		e[0]=1;  e[4]=0;  e[ 8]=0;  e[12]=0;
		e[1]=0;  e[5]=c;  e[ 9] =-s;  e[13]=0;
		e[2]=0;  e[6]=s;  e[10]=c;  e[14]=0;
		e[3]=0;  e[7]=0;  e[11]=0;  e[15]=1;
	}
	else if(x==0 && y!=0 && z==0)
	{
		if(y<0)s=-s;
		e[0]=c;  e[4]=0;  e[ 8]=s;  e[12]=0;
		e[1]=0;  e[5]=1;  e[ 9]=0;  e[13]=0;
		e[2] =-s;  e[6]=0;  e[10]=c;  e[14]=0;
		e[3]=0;  e[7]=0;  e[11]=0;  e[15]=1;
	}
	else if(x==0 && y==0 && z!=0)
	{
		if (z < 0)s=-s;
		e[0]=c;  e[4] =-s;  e[ 8]=0;  e[12]=0;
		e[1]=s;  e[5]=c;  e[ 9]=0;  e[13]=0;
		e[2]=0;  e[6]=0;  e[10]=1;  e[14]=0;
		e[3]=0;  e[7]=0;  e[11]=0;  e[15]=1;
	}
	else
	{
		var len=Math.sqrt(x*x+y*y+z*z);
		if (len != 1) 
		{
			x /= len;
			y /= len;
			z /= len;
		}
		var nc=1-c,xy=x*y,yz=y*z,zx=z*x,xs=x*s,ys=y*s,zs=z*s;
		e[ 0]=x*x*nc+ c;	e[ 1]=xy *nc+zs;	e[ 2]=zx *nc-ys;	e[ 3]=0;
		e[ 4]=xy *nc-zs;	e[ 5]=y*y*nc+ c;	e[ 6]=yz *nc+xs;	e[ 7]=0;
		e[ 8]=zx *nc+ys;	e[ 9]=yz *nc-xs;	e[10]=z*z*nc+ c;	e[11]=0;
		e[12]=0;		e[13]=0;		e[14]=0;		e[15]=1;
	}
	return this;
};
Matrix4.prototype.rotate=function(angle,x,y,z)
{
	return this.multiply(new Matrix4().setRotate(angle, x, y, z));
};
Matrix4.prototype.setLookAt=function(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
{
	var dx=centerX-eyeX,dy=centerY-eyeY,dz=centerZ-eyeZ;
	var dl=Math.sqrt(dx*dx+dy*dy+dz*dz);
	dx /= dl;dy /= dl;dz /= dl;

	var sx=dy*upZ-dz*upY,sy=dz*upX-dx*upZ,sz=dx*upY-dy*upX;
	dl=Math.sqrt(sx*sx+sy*sy+sz*sz);
	sx /= dl;sy /= dl;sz /= dl;

	var ux=sy*dz-sz*dy,uy=sz*dx-sx*dz,uz=sx*dy-sy*dx;

	e=this.elements;
 	e[0]=sx; 	e[1]=ux; 	e[2]=-dx; 	e[3]=0;
 	e[4]=sy; 	e[5]=uy; 	e[6]=-dy; 	e[7]=0;
 	e[8]=sz; 	e[9]=uz; 	e[10]=-dz;e[11]=0;
 	e[12]=0; 	e[13]=0; 	e[14]=0; 	e[15]=1;
	return this.translate(-eyeX, -eyeY, -eyeZ);
};
Matrix4.prototype.lookAt=function(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
{
	return this.multiply(new Matrix4().setLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ));
};
var Vector3=function(param)
{
	var v=new Float32Array(3);
	if (param && typeof param === 'object')
	{
		v[0]=param[0]; v[1]=param[1]; v[2]=param[2];
	} 
	this.elements=v;
}
Vector3.prototype.normalize=function()
{
	var v=this.elements;
	var x=v[0], y=v[1], z=v[2];
	var l=Math.sqrt(x*x+y*y+z*z);
	if(l!=0){v[0]/=l;v[1]/=l;v[2]/=l;}
	return this;
};
var Vector4=function(param)
{
	var v=new Float32Array(4);
	if (param && typeof param === 'object')
	{
		v[0]=param[0]; v[1]=param[1]; v[2]=param[2]; v[3]=param[3];
	} 
	this.elements=v;
}
Vector3.prototype.cross=function(param0,param1)
{
	var v=this.elements;
	var v0=param0.elements;
	var v1=param1.elements;
	var x0=v0[0], y0=v0[1], z0=v0[2];
	var x1=v1[0], y1=v1[1], z1=v1[2];
	var x=y0*z1-y1*z0;
	var y=z0*x1-z1*x0;
	var z=x0*y1-x1*y0;
	v[0]=x;v[1]=y;v[2]=z;
};
Vector3.prototype.x=function()
{
	return this.elements[0];
};
Vector3.prototype.y=function()
{
	return this.elements[1];
};
Vector3.prototype.z=function()
{
	return this.elements[2];
};
Vector3.prototype.z=function()
{
	return this.elements[2];
};
Vector3.prototype.set=function(param)
{
	for(var i=0;i<3;i++)this.elements[i]=param[i];
};
Vector3.prototype.rotate=function(axis,angle)
{
	var e=axis.elements;
	var mat=new Matrix4();
	mat.setRotate(angle,e[0],e[1],e[2]);
	var v=mat.multiplyVector3(this);
	this.elements=v.elements;
};
