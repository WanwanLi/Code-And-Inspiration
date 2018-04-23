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
function toIntArray(array)
{
	var newArray=[];
	for(var i in array)
	{
		newArray.push(parseInt(array[i])-1);
	}
	return newArray;
}
function getVectorXd(vector, index, dim)
{
	var vectorXd=[];
	for(var i=0; i<dim; i++)
	{
		vectorXd.push(vector[index*dim+i]);
	}
	return vectorXd;
}
function getTriangleObject(vertices, texCoords, normals, triangle, index)
{
	var vertexArray=[];
	var normalArray=[];
	var texCoordArray=[];
	var indexArray=[];
	for(var i=0; i<3; i++)
	{
		var vertex=getVectorXd(vertices, triangle[i][0], 3);
		var texCoord=getVectorXd(texCoords, triangle[i][1], 2);
		var normal=getVectorXd(normals, triangle[i][2], 3);
		vertexArray=vertexArray.concat(vertex);
		texCoordArray=texCoordArray.concat(texCoord);
		normalArray=normalArray.concat(normal);
		indexArray.push(index+i);
	}
	var object=[];
	object["coordinates"]=vertexArray;
	object["normals"]=normalArray;
	object["texCoords"]=texCoordArray
	object["indices"]=indexArray;
	return object;
}
function createObjectFromFaces(vertices, texCoords, normals, faces)
{
	var vertexArray=[];
	var normalArray=[];
	var texCoordArray=[];
	var indexArray=[];
	var index=0;
	for(var f in faces)
	{
		var triangles=[];
		if(faces[f].length==3)
		triangles=[faces[f].slice(0, 3)];
		else if(faces[f].length==4)
		{
			var triangle1=
			[
				faces[f][2], faces[f][3], faces[f][0]
			];
			triangles=[faces[f].slice(0, 3), triangle1];
		}
		for(var i in triangles)
		{
			var triangle=getTriangleObject(vertices, texCoords, normals, triangles[i], index);
			vertexArray=vertexArray.concat(triangle["coordinates"]);
			texCoordArray=texCoordArray.concat(triangle["texCoords"]);
			normalArray=normalArray.concat(triangle["normals"]);
			indexArray=indexArray.concat(triangle["indices"]); index+=3;
		}
	}
	var object=[];
	object["coordinates"]=vertexArray;
	object["colors"]=[];
	object["normals"]=normalArray;
	object["texCoords"]=texCoordArray
	object["indices"]=indexArray;
	return object;
}
function createObjectFromFile(fileName,text)
{
	var vertices=[ ];
	var normals=[ ];
	var texCoords=[ ];
	var faces=[];
	var lines=text.split("\n");
	for(var i in lines)
	{
		var line=lines[i].replace(/[ \t]+/g, " ");
		line=line.replace(/\s\s*$/, "");
		if(line[0] == "#")continue;
		var array=line.split(" ");
		if (array[0] == "v")
		{
			vertices.push(parseFloat(array[1]));
			vertices.push(parseFloat(array[2]));
			vertices.push(parseFloat(array[3]));
		}
		else if (array[0] == "vt")
		{
			texCoords.push(parseFloat(array[1]));
			texCoords.push(parseFloat(array[2]));
		}
		else if (array[0] == "vn")
		{
			normals.push(parseFloat(array[1]));
			normals.push(parseFloat(array[2]));
			normals.push(parseFloat(array[3]));
		}
		else if (array[0] == "f")
		{
			if(array.length<4)continue;  var face=[];
			for (var i=1; i<array.length; i++)
			face.push(toIntArray(array[i].split("/")));
			faces.push(face);
		}
	}
	return createObjectFromFaces(vertices, texCoords, normals, faces);
}
