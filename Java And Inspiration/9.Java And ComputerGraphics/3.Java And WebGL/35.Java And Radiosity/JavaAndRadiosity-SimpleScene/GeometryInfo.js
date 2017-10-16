var w=0.1,h=0.1,d=0.0,L=1,B=0.05;
var quad_light=
{
	coordinates:
	[ 	
		-w,h,d,
		-w,-h,d,
		w,-h,d,
		w,h,d
	],
	reflectances:
	[ 
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0
	],
	colors:
	[ 
		L,L,L,
		L,L,L,
		L,L,L,
		L,L,L
	],
	row:10,
	column:10
};
var quad_wall=
{
	coordinates:
	[ 	
		-w,h,d,
		-w,-h,d,
		w,-h,d,
		w,h,d
	],
	reflectances:
	[ 
		B,B,B,
		B,B,B,
		B,B,B,
		B,B,B
	],
	colors:
	[ 
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0
	],
	row:20,
	column:20
};
