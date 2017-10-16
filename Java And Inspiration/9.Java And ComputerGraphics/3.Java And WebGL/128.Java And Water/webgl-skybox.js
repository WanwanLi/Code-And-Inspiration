function SkyBox(gl,width,textureName,imageFormat)
{
	var r=width/2;
	var coordinates=
	[
		-r,-r,-r,
		-r,-r, r,
		-r, r, r,
		-r, r,-r
	];
	var colors=[];
	var normals=[0,0,0,0,0,0,0,0,0,0,0,0];
	var texCoords=
	[
		0,0,
		1,0,
		1,1,
		0,1
	];
	var indices=[0,1,2,2,3,0];
	this.negX=
	{
		obj:new ArrayObject(gl,coordinates,colors,normals,texCoords,indices),
		img:createImageTexture(gl,textureName+0+imageFormat)
	};
	coordinates=
	[
		-r,-r,-r,
		 r,-r,-r,
		 r,-r, r,
		-r,-r, r
	];
	texCoords=
	[
		0,0,
		1,0,
		1,1,
		0,1
	];
	this.negY=
	{
		obj:new ArrayObject(gl,coordinates,colors,normals,texCoords,indices),
		img:createImageTexture(gl,textureName+4+imageFormat)
	};
	coordinates=
	[
		-r,-r,-r,
		 r,-r,-r,
		 r, r,-r,
		-r, r,-r
	];
	texCoords=
	[
		1,0,
		0,0,
		0,1,
		1,1
	];
	this.negZ=
	{
		obj:new ArrayObject(gl,coordinates,colors,normals,texCoords,indices),
		img:createImageTexture(gl,textureName+2+imageFormat)
	};
	coordinates=
	[
		 r,-r,-r,
		 r,-r, r,
		 r, r, r,
		 r, r,-r
	];
	texCoords=
	[
		1,0,
		0,0,
		0,1,
		1,1
	];
	this.posX=
	{
		obj:new ArrayObject(gl,coordinates,colors,normals,texCoords,indices),
		img:createImageTexture(gl,textureName+3+imageFormat)
	};
	coordinates=
	[
		 -r, r,-r,
		  r, r,-r,
		  r, r, r,
		 -r, r, r
	];
	texCoords=
	[
		0,1,
		1,1,
		1,0,
		0,0
	];
	this.posY=
	{
		obj:new ArrayObject(gl,coordinates,colors,normals,texCoords,indices),
		img:createImageTexture(gl,textureName+1+imageFormat)
	};
	coordinates=
	[
		 -r,-r, r,
		  r,-r, r,
		  r, r, r,
		 -r, r, r
	];
	texCoords=
	[
		0,0,
		1,0,
		1,1,
		0,1

	];
	this.posZ=
	{
		obj:new ArrayObject(gl,coordinates,colors,normals,texCoords,indices),
		img:createImageTexture(gl,textureName+5+imageFormat)
	};
}
function createImageTexture(gl,imageName)
{
	var texture=gl.createTexture();
	texture.image=new Image();
	texture.image.onload=function(){loadImageTexture(gl,texture);}
	texture.image.src=imageName;
	return texture;
}
function loadImageTexture(gl,texture)
{
	gl.bindTexture(gl.TEXTURE_2D,texture);
	gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);
	gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, texture.image);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
	gl.generateMipmap(gl.TEXTURE_2D);
	gl.bindTexture(gl.TEXTURE_2D, null);
}
