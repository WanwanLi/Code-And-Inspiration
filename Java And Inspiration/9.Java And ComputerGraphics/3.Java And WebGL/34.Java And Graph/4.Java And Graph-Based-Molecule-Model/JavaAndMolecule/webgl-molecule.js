function Atom(R,Q,M,P,C)
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
	this.object=new ArrayObject(gl,coordinates,colors,normals,texCoords,indices);
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
var dt=0.001,B=0.01;
function Molecule(K)
{
	this.K=K;
	this.length=0;
	this.atoms=[];
}
Molecule.prototype.addAtom=function(R,Q,M,P,C)
{
	this.atoms.push(new Atom(R,Q,M,P,C));
	this.length++;
};
function ElectronicForce(K,Q1,Q2,r2)
{
	return r2==0?0:K*Q1*Q2/r2;
}
Molecule.prototype.animate=function()
{
	for(var i=1;i<this.length;i++)
	{
		var atom=this.atoms[i];
		atom.v[0]+=atom.a[0]*dt;
		atom.v[1]+=atom.a[1]*dt;
		atom.v[2]+=atom.a[2]*dt;
		atom.x+=atom.v[0]*dt;
		atom.y+=atom.v[1]*dt;
		atom.z+=atom.v[2]*dt;
		for(var j=0;j<this.length;j++)
		{
			if(i==j)continue;
			var atom1=this.atoms[j];
			var dx=atom.x-atom1.x;
			var dy=atom.y-atom1.y;
			var dz=atom.z-atom1.z;
			var dr2=dx*dx+dy*dy+dz*dz;
			var dr=Math.sqrt(dr2);
			if(dr<=atom1.R+atom.R)
			{
				atom.v[0]=-atom.v[0];
				atom.v[1]=-atom.v[1];
				atom.v[2]=-atom.v[2];
			}
			var F=ElectronicForce(this.K,atom.Q,atom1.Q,dr2);
			var a=F/atom.M;
			atom.a[0]+=a*dx/dr;
			atom.a[1]+=a*dy/dr;
			atom.a[2]+=a*dz/dr;
		}
		var vx=atom.v[0];
		var vy=atom.v[1];
		var vz=atom.v[2];

		var v2=vx*vx+vy*vy+vz*vz;
		var dv=Math.sqrt(v2);
		if(dv>0)
		{
		atom.a[0]-=B*v2*vx/dv/atom.M;

		atom.a[1]-=B*v2*vy/dv/atom.M;

		atom.a[2]-=B*v2*vz/dv/atom.M;
		}
	}
};
