function ArrayObject(gl,object)
{
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
function QuadObject(R)
{
	var coordinateArray=
	[
		-R, -R,  0.0,
		R, -R,  0.0,
		R,  R,  0.0,
		-R,  R,  0.0

	];
	var texCoordArray=
	[
		0.0, 1.0,
		1.0, 1.0,
		1.0, 0.0,
		0.0, 0.0,
	];
	var normalArray=
	[
		0.0, 0.0, 0.1, 
		0.0, 0.0, 0.1, 
		0.0, 0.0, 0.1, 
		0.0, 0.0, 0.1
	];
	var indexArray=[0,1,2,2,3,0];
	var object=[];
	object["coordinates"]=coordinateArray;
	object["colors"]=[];
	object["normals"]=normalArray;
	object["texCoords"]=texCoordArray
	object["indices"]=indexArray;
	return new ArrayObject(gl,object);
}
