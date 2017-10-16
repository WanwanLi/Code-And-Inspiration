function Atom(R,Q,M,P,C)
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
function Molecule(G)
{
	this.length=0;
	this.R=G.radius;
	this.M=G.mass;
	this.atomNames=G.atoms;
	this.atoms=[];
	for(var i=0;i<G.atoms.length;i++)this.addAtom(G);
	this.edgeList=[];
	this.edges=[];
	for(var i=0;i<this.length;i++)
	{
		var edge=new Uint16Array(this.length);
		this.edges.push(edge);
	}
	for(var i=0;i<G.edges.length;i+=2)
	{
		var n0=this.getAtomIndex(G.edges[i+0]);
		var n1=this.getAtomIndex(G.edges[i+1]);
		this.edgeList.push(n0);
		this.edgeList.push(n1);
		this.edges[n0][n1]=1;
		this.edges[n1][n0]=1;
	}
	this.object=[];
	this.object.edge=this.getEdgeObject();
	this.object.atom=this.getAtomObject();
}
Molecule.prototype.getAtomIndex=function(E)
{
	for(var i=0;i<this.length;i++)
	{
		if(E==this.atomNames[i])return i;
	}
	return 0;
};
Molecule.prototype.addAtom=function(G)
{
	var i=this.length;
	var R=G.radius[i];
	var Q=0.2,M=0.1;
	var P=this.getRandomPosition(0.2);
	while(this.isConflict(P,R))P=this.getRandomPosition(4*R);
	var C=G.colors[i];
	this.atoms.push(new Atom(R,Q,M,P,C));
	this.length++;
};
Molecule.prototype.getRandomPosition=function(R)
{
	var x=R*(0.5-Math.random());
	var y=R*(0.5-Math.random());
	var z=R*(0.5-Math.random());
	return [x,y,z];
}
Molecule.prototype.isConflict=function(P,R)
{
	var x=P[0],y=P[1],z=P[2];
	for(var i=0;i<this.length;i++)
	{
		var atom=this.atoms[i];
		var dx=atom.x-x;
		var dy=atom.y-y;
		var dz=atom.z-z;
		var dr2=dx*dx+dy*dy+dz*dz;
		var dr=Math.sqrt(dr2);
		if(dr<R)return true;
	}
	return false;
}
Molecule.prototype.getAtomObject=function()
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
Molecule.prototype.getEdgeObject=function()
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
Molecule.prototype.animate=function()
{
	var dt=0.05,error=0.001;
	var S=1.0,E=0.2,Q=0.2;
	for(var i=0;i<this.length;i++)
	{
		var atom=this.atoms[i];
		atom.x+=atom.a[0]*dt;
		atom.y+=atom.a[1]*dt;
		atom.z+=atom.a[2]*dt;
		for(var j=0;j<this.length;j++)
		{
			if(i==j)continue;
			var atom1=this.atoms[j];
			var dx=atom.x-atom1.x;
			var dy=atom.y-atom1.y;
			var dz=atom.z-atom1.z;
			var dr2=dx*dx+dy*dy+dz*dz;
			var dr=Math.sqrt(dr2);
			var F=ElectronicForce(E,Q,Q,dr2);
			if(this.edges[i][j])F-=SpringForce(S,dr);
			var a=F/atom.M;
			atom.a[0]+=a*dx/dr;
			atom.a[1]+=a*dy/dr;
			atom.a[2]+=a*dz/dr;
		}
		atom.a[0]/=(this.length-1);
		atom.a[1]/=(this.length-1);
		atom.a[2]/=(this.length-1);
		if(Math.abs(atom.a[0])<error)
		if(Math.abs(atom.a[1])<error)
		if(Math.abs(atom.a[2])<error)
		{
			atom.a[0]=0;
			atom.a[1]=0;
			atom.a[2]=0;
		}
	}
};
