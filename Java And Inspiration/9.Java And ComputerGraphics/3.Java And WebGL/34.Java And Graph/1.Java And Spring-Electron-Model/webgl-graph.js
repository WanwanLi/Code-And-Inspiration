function Vertex(R,Q,M,P,C)
{
	this.R=R;
	this.Q=Q;	
	this.M=M;
	this.x=P[0];
	this.y=P[1];
	this.z=P[2];
	this.C=C;
	this.a=[0,0,0];
	this.v=[0,0,0];
}
function Graph(G)
{
	this.isStatic=true;
	this.length=0;
	this.R=G.radius;
	this.M=G.mass;
	this.vertices=[];
	for(var i=0;i<G.length;i++)this.addVertex();
	this.edgeList=G.edges;
	this.edges=[];
	for(var i=0;i<G.length;i++)
	{
		var edge=new Uint16Array(G.length);
		this.edges.push(edge);
	}
	for(var i=0;i<G.edges.length;i+=2)
	{
		var n0=G.edges[i+0];
		var n1=G.edges[i+1];
		this.edges[n0][n1]=1;
		this.edges[n1][n0]=1;
	}
	this.object=[];
	this.object.edge=this.getEdgeObject();
	this.object.vertex=this.getVertexObject();
}
Graph.prototype.addVertex=function()
{
	var R=this.R*(1+Math.random()),Q=0.2,M=this.M;
	var P=this.getRandomPosition(10*R);
	while(this.isConflict(P,R))P=this.getRandomPosition(10*R);
	var C=this.getRandomColor();
	this.vertices.push(new Vertex(R,Q,M,P,C));
	this.length++;
};
Graph.prototype.getRandomPosition=function(R)
{
	var x=R*(0.5-Math.random());
	var y=R*(0.5-Math.random());
	var z=R*(0.5-Math.random());
	return [x,y,z];
}
Graph.prototype.isConflict=function(P,R)
{
	var x=P[0],y=P[1],z=P[2];
	for(var i=0;i<this.length;i++)
	{
		var vertex=this.vertices[i];
		var dx=vertex.x-x;
		var dy=vertex.y-y;
		var dz=vertex.z-z;
		var dr2=dx*dx+dy*dy+dz*dz;
		var dr=Math.sqrt(dr2);
		if(dr<0.01*R)return true;
	}
	return false;
}
Graph.prototype.getRandomColor=function()
{
	var r=Math.random();
	var g=Math.random();
	var b=Math.random();
	return [r,g,b];
}
Graph.prototype.getVertexObject=function()
{
	var m=15,n=30,R=1;
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
Graph.prototype.getEdgeObject=function()
{
	var m=2,n=10,h=1.0,r=0.002;
	var a=2*Math.PI/(n-1),x,z,y;
	var coordinates=new Float32Array(m*n*3);
	var colors=new Float32Array(m*n*3);
	var normals=new Float32Array(m*n*3);
	var texCoords=new Float32Array(m*n*2);
	for(var i=0;i<m;i++)
	{
		y=i*h;
		for(var j=0;j<n;j++)
		{
			x=r*Math.cos(a*j);
			z=r*Math.sin(a*j);

			coordinates[(i*n+j)*3+0]=x;
			coordinates[(i*n+j)*3+1]=y;
			coordinates[(i*n+j)*3+2]=z;

			normals[(i*n+j)*3+0]=x/r;
			normals[(i*n+j)*3+1]=0;
			normals[(i*n+j)*3+2]=z/r;

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
function SpringForce(K,dr)
{
	return K*dr;
}
function ElectronicForce(K,Q1,Q2,r2)
{
	return r2==0?0:K*Q1*Q2/r2;
}
Graph.prototype.animate=function()
{
	var dt=0.1,error=0.001;
	var S=1.5,E=0.1,Q=0.4;
	for(var i=0;i<this.length;i++)
	{
		var vertex=this.vertices[i];
		vertex.x+=vertex.a[0]*dt;
		vertex.y+=vertex.a[1]*dt;
		vertex.z+=vertex.a[2]*dt;
		for(var j=0;j<this.length;j++)
		{
			if(i==j)continue;
			var vertex1=this.vertices[j];
			var dx=vertex.x-vertex1.x;
			var dy=vertex.y-vertex1.y;
			var dz=vertex.z-vertex1.z;
			var dr2=dx*dx+dy*dy+dz*dz;
			var dr=Math.sqrt(dr2);
			var F=ElectronicForce(E,Q,Q,dr2);
			if(this.edges[i][j])F-=SpringForce(S,dr);
			var a=F/vertex.M;
			vertex.a[0]+=a*dx/dr;
			vertex.a[1]+=a*dy/dr;
			vertex.a[2]+=a*dz/dr;
		}
		vertex.a[0]/=(this.length-1);
		vertex.a[1]/=(this.length-1);
		vertex.a[2]/=(this.length-1);
		if(Math.abs(vertex.a[0])<error)
		if(Math.abs(vertex.a[1])<error)
		if(Math.abs(vertex.a[2])<error)
		{
			vertex.a[0]=0;
			vertex.a[1]=0;
			vertex.a[2]=0;
		}
	}
};
Graph.prototype.animate_dynamic=function()
{
	var dt=0.01;
	var S=0.5,E=0.01,Q=0.4;
	for(var i=0;i<this.length;i++)
	{
		var vertex=this.vertices[i];
		vertex.v[0]+=vertex.a[0]*dt;
		vertex.v[1]+=vertex.a[1]*dt;
		vertex.v[2]+=vertex.a[2]*dt;
		vertex.x+=vertex.v[0]*dt;
		vertex.y+=vertex.v[1]*dt;
		vertex.z+=vertex.v[2]*dt;
		for(var j=0;j<this.length;j++)
		{
			if(i==j)continue;
			var vertex1=this.vertices[j];
			var dx=vertex.x-vertex1.x;
			var dy=vertex.y-vertex1.y;
			var dz=vertex.z-vertex1.z;
			var dr2=dx*dx+dy*dy+dz*dz;
			var dr=Math.sqrt(dr2);
			var F=ElectronicForce(E,Q,Q,dr2)-SpringForce(S,dr);
			var a=F/vertex.M;
			vertex.a[0]+=a*dx/dr;
			vertex.a[1]+=a*dy/dr;
			vertex.a[2]+=a*dz/dr;
		}
		var vx=vertex.v[0];
		var vy=vertex.v[1];
		var vz=vertex.v[2];
		var v2=vx*vx+vy*vy+vz*vz;
		var dv=Math.sqrt(v2);
	}
};