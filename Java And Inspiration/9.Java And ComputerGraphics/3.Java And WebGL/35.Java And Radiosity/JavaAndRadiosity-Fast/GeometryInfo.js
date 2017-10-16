var w=0.1,h=0.1,d=0.0,R=4,C=4,L=1.0,B=0.08;
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
	row:3,
	column:3
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
	row:R,
	column:C
};
