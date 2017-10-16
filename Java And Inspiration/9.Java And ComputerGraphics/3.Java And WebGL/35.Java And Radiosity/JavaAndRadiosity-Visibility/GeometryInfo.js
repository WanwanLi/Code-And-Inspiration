var r=0.2,w=r,h=r,d=0;
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
		1.0, 1.0, 0.8,
		1.0, 1.0, 0.8,
		1.0, 1.0, 0.8,
		1.0, 1.0, 0.8
	],
	row:2,
	column:2
};
var quad_front=
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
		2.0, 2.0, 1.8,
		2.0, 2.0, 1.8,
		2.0, 2.0, 1.8,
		2.0, 2.0, 1.8
	],
	colors:
	[ 
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0
	],
	row:3,
	column:3
};
var quad_left=
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
		0.0, 1.0, 0.0,
		0.0, 1.0, 0.0,
		0.0, 1.0, 0.0,
		0.0, 1.0, 0.0
	],
	colors:
	[ 
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0
	],
	row:3,
	column:3
};
var quad_right=
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
		0.0, 0.0, 1.0,
		0.0, 0.0, 1.0,
		0.0, 0.0, 1.0,
		0.0, 0.0, 1.0
	],
	colors:
	[ 
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0
	],
	row:3,
	column:3
};
var quad_ceiling=
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
		0.3, 0.0, 0.0,
		0.3, 0.0, 0.0,
		0.3, 0.0, 0.0,
		0.3, 0.0, 0.0
	],
	colors:
	[ 
		0.1, 0.0, 0.0,
		0.1, 0.0, 0.0,
		0.1, 0.0, 0.0,
		0.1, 0.0, 0.0
	],
	row:2,
	column:2
};

var quad_floor=
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
		2.0, 2.0, 1.8,
		2.0, 2.0, 1.8,
		2.0, 2.0, 1.8,
		2.0, 2.0, 1.8
	],
	colors:
	[ 
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0
	],
	row:10,
	column:10
};
