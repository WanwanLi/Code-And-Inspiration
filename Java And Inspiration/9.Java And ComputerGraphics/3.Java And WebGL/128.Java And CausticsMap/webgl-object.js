function ArrayObject(gl,coordinates,colors,normals,texCoords,indices)
{
	var object=[];
	object["coordinates"]=coordinates;
	object["colors"]=colors;
	object["normals"]=normals;
	object["texCoords"]=texCoords;
	object["indices"]=indices;
	var objectBuffer=createObjectBuffer(gl,object);
	this.coordinatesBuffer=objectBuffer["coordinates"];
	this.colorsBuffer=objectBuffer["colors"];
	this.normalsBuffer=objectBuffer["normals"];
	this.texCoordsBuffer=objectBuffer["texCoords"];
	this.indicesBuffer=objectBuffer["indices"];
}
function FileObject(gl,fileName,text)
{
	var object=createObjectFromFile(fileName,text);
	var objectBuffer=createObjectBuffer(gl,object);
	this.coordinatesBuffer=objectBuffer["coordinates"];
	this.colorsBuffer=objectBuffer["colors"];
	this.normalsBuffer=objectBuffer["normals"];
	this.texCoordsBuffer=objectBuffer["texCoords"];
	this.indicesBuffer=objectBuffer["indices"];
}
function createObjectBuffer(gl,object)
{
	var coordinatesBuffer=gl.createBuffer();
	var colorsBuffer=gl.createBuffer();
	var normalsBuffer=gl.createBuffer();
	var texCoordsBuffer=gl.createBuffer();
	var indicesBuffer=gl.createBuffer();
	var coordinatesData=new Float32Array(object["coordinates"]);
	var colorsData=new Float32Array(object["colors"]);
	var normalsData=new Float32Array(object["normals"]);
	var texCoordsData=new Float32Array(object["texCoords"]);
	var indicesData=new Uint16Array(object["indices"]);
	bindBufferData(gl,gl.ARRAY_BUFFER,coordinatesBuffer,coordinatesData,3);
	bindBufferData(gl,gl.ARRAY_BUFFER,colorsBuffer,colorsData,3);
	bindBufferData(gl,gl.ARRAY_BUFFER,normalsBuffer,normalsData,3);
	bindBufferData(gl,gl.ARRAY_BUFFER,texCoordsBuffer,texCoordsData,2);
	bindBufferData(gl,gl.ELEMENT_ARRAY_BUFFER,indicesBuffer,indicesData,1);
	var objectBuffer=[];
	objectBuffer["coordinates"]=coordinatesBuffer;
	objectBuffer["colors"]=colorsBuffer;
	objectBuffer["normals"]=normalsBuffer;
	objectBuffer["texCoords"]=texCoordsBuffer
	objectBuffer["indices"]=indicesBuffer;
	return objectBuffer;
}
function bindBufferData(gl,type,buffer,data,itemSize)
{
	gl.bindBuffer(type,buffer);
	gl.bufferData(type,data,gl.STATIC_DRAW);
	buffer.itemSize=itemSize;
	buffer.numItems=data.length/itemSize;
}
function createObjectFromFile(fileName,text)
{
	var coordinateArray=[ ];
	var normalArray=[ ];
	var texCoordArray=[ ];
	var indexArray=[ ];

	var vertex=[ ];
	var normal=[ ];
	var texture=[ ];
	var facemap={ };
	var index=0;
	var lines=text.split("\n");
	for (var lineIndex in lines)
	{
		var line=lines[lineIndex].replace(/[ \t]+/g, " ").replace(/\s\s*$/, "");
		if (line[0] == "#")continue;
		var array=line.split(" ");
		if (array[0] == "v")
		{
			vertex.push(parseFloat(array[1]));
			vertex.push(parseFloat(array[2]));
			vertex.push(parseFloat(array[3]));
		}
		else if (array[0] == "vt")
		{
			texture.push(parseFloat(array[1]));
			texture.push(parseFloat(array[2]));
		}
		else if (array[0] == "vn")
		{
			normal.push(parseFloat(array[1]));
			normal.push(parseFloat(array[2]));
			normal.push(parseFloat(array[3]));
		}
		else if (array[0] == "f")
		{
			if (array.length != 4)continue;
			for (var i=1; i < 4; ++i)
			{
				if (!(array[i] in facemap)) 
				{
					var f=array[i].split("/");
					var vtx, nor, tex;

					if (f.length == 1)
					{
						vtx=parseInt(f[0]) - 1;
						nor=vtx;
						tex=vtx;
					}
					else if (f.length=3) 
					{
						vtx=parseInt(f[0]) - 1;
						tex=parseInt(f[1]) - 1;
						nor=parseInt(f[2]) - 1;
					}
					else return null;

					var x=0,y=0,z=0;
					if (vtx * 3 + 2 < vertex.length)
					{
						x=vertex[vtx*3+0];
						y=vertex[vtx*3+1];
						z=vertex[vtx*3+2];
					}
					coordinateArray.push(x);
					coordinateArray.push(y);
					coordinateArray.push(z);
	
					x=0;y=0;
					if (tex * 2 + 1 < texture.length)
					{
						x=texture[tex*2+0];
						y=texture[tex*2+1];
					}
					texCoordArray.push(x);
					texCoordArray.push(y);

					x=0;y=0;z=1;
					if (nor * 3 + 2 < normal.length)
					{
						x=normal[nor*3+0];
						y=normal[nor*3+1];
						z=normal[nor*3+2];
					}
					normalArray.push(x);
					normalArray.push(y);
					normalArray.push(z);
					facemap[array[i]]=index++;
				}
				indexArray.push(facemap[array[i]]);
			}
		}
	}
	var object=[];
	object["coordinates"]=coordinateArray;
	object["colors"]=[];
	object["normals"]=normalArray;
	object["texCoords"]=texCoordArray
	object["indices"]=indexArray;
	return object;
}
function SphereObject(R)
{
	var m=50,n=80;
	var u0=0,u1=Math.PI,v0=0,v1=2*Math.PI;
	var du=(u1-u0)/(m-1),dv=(v1-v0)/(n-1),d=0.0001;
	var coordinates=new Float32Array(m*n*3);
	var colors=new Float32Array(m*n*3);
	var normals=new Float32Array(m*n*3);
	var texCoords=new Float32Array(m*n*2);
	for(var i=0;i<m;i++)
	{
		var u=u0+i*du;
		for(var j=0;j<n;j++)
		{
			var v=v0+j*dv;
			var x=R*Math.sin(u)*Math.cos(v);
			var y=R*Math.cos(u);
			var z=R*Math.sin(u)*Math.sin(v);

			coordinates[(i*n+j)*3+0]=x;
			coordinates[(i*n+j)*3+1]=y;
			coordinates[(i*n+j)*3+2]=z;

			colors[(i*n+j)*3+0]=x/R;
			colors[(i*n+j)*3+1]=y/R;
			colors[(i*n+j)*3+2]=z/R;

			var dxu=R*Math.sin(u+d)*Math.cos(v)-x;
			var dyu=R*Math.cos(u+d)-y;
			var dzu=R*Math.sin(u+d)*Math.sin(v)-z;

			var dxv=R*Math.sin(u)*Math.cos(v+d)-x;
			var dyv=0;
			var dzv=R*Math.sin(u)*Math.sin(v+d)-z;

			var v1=new Vector3([dxu,dyu,dzu]);
			var v2=new Vector3([dxv,dyv,dzv]);
			var normal=new Vector3();
			if(i==0)normal.set([0,1,0]);
			else if(i==m-1)normal.set([0,-1,0]);
			else normal.cross(v2,v1);

			normals[(i*n+j)*3+0]=normal.x();
			normals[(i*n+j)*3+1]=normal.y();
			normals[(i*n+j)*3+2]=normal.z();

			texCoords[(i*n+j)*2+0]=1.0*i/(m-1);
			texCoords[(i*n+j)*2+1]=1.0*j/(n-1);
		}
	}
	var indices=new Uint16Array((m-1)*(n-1)*6),c=0;
	for(var i=0;i<m-1;i++)
	{
		for(var j=0;j<n-1;j++)
		{
			indices[c++]=(i+0)*n+(j+0);
			indices[c++]=(i+1)*n+(j+0);
			indices[c++]=(i+1)*n+(j+1);

			indices[c++]=(i+1)*n+(j+1);
			indices[c++]=(i+0)*n+(j+1);
			indices[c++]=(i+0)*n+(j+0);
		}
	}
	return new ArrayObject(gl,coordinates,colors,normals,texCoords,indices);
}
function PlaneObject(width)
{
	var r=width/2;
	var coordinates=
	[
		-r,0,-r,
		-r,0, r,
		 r,0, r,
		 r,0,-r
	];
	var colors=[];
	var normals=[0,1,0,0,1,0,0,1,0,0,1,0];
	var texCoords=
	[
		0,0,
		1,0,
		1,1,
		0,1
	];
	var indices=[0,1,2,2,3,0];
	return new ArrayObject(gl,coordinates,colors,normals,texCoords,indices);
}
function CubeObject(width)
{
	var r=width/2;
	var coordinates=
	[
		-r,-r,-r,
		-r,-r, r,
		-r, r, r,
		-r, r,-r,
/*
		-r,-r,-r,
		 r,-r,-r,
		 r,-r, r,
		-r,-r, r,
*/
		0,0,0,
		0,0,0,
		0,0,0,
		0,0,0,

		-r,-r,-r,
		-r, r,-r,
		 r, r,-r,
		 r,-r,-r,

		 r,-r,-r,
		 r, r,-r,
		 r, r, r,
		 r,-r, r,

		 -r, r,-r,
		 -r, r, r,
		  r, r, r,
		  r, r,-r,

		 -r,-r, r,
		  r,-r, r,
		  r, r, r,
		 -r, r, r

	];
	var indices=[];
	for(var i=0;i<6;i++)
	{
		indices.push(i*4+0);
		indices.push(i*4+1);
		indices.push(i*4+2);

		indices.push(i*4+2);
		indices.push(i*4+3);
		indices.push(i*4+0);
	}
	var normals=
	[
		-1,0,0,
		-1,0,0,
		-1,0,0,
		-1,0,0,

		0,-1,0,
		0,-1,0,
		0,-1,0,
		0,-1,0,

		0,0,-1,
		0,0,-1,
		0,0,-1,
		0,0,-1,

		1,0,0,
		1,0,0,
		1,0,0,
		1,0,0,

		0,1,0,
		0,1,0,
		0,1,0,
		0,1,0,

		0,0,1,
		0,0,1,
		0,0,1,
		0,0,1
	];
	var colors=new Float32Array(coordinates.length);
	var texCoords=new Float32Array(coordinates.length/3*2);
	return new ArrayObject(gl,coordinates,colors,normals,texCoords,indices);
}
function SceneCubeObject(centerVector,width)
{
	var v=centerVector.elements;
	this.centerX=v[0];
	this.centerY=v[1];
	this.centerZ=v[2];
	this.width=width;
};
