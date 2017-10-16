function getObjectData(fileName)
{
	var request=new XMLHttpRequest();
	request.open("GET",fileName+".obj",false);
	request.send(null);
	var text=request.responseText;

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
	var objectData=[];
	objectData["coordinates"]=coordinateArray;
	objectData["colors"]=[];
	objectData["normals"]=normalArray;
	objectData["texCoords"]=texCoordArray
	objectData["indices"]=indexArray;
	return objectData;
}
