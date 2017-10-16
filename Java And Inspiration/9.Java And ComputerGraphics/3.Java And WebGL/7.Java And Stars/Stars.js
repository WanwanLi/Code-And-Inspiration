var maxDistance=3.0,rotDecay=0.06,rotRatio=4.0;
var spinRatio=20,starRadius=0.5,starShiness=0.15;
function Star(startingDistance,rotationSpeed)
{
        this.angle=0;
        this.distance=startingDistance;
        this.rotSpeed=rotationSpeed;
        this.getColor();
}
Star.prototype.draw=function()
{
	glPushMatrix();
	glRotate(this.angle,0,0,1);
	glTranslate(this.distance,0,0);
	glRotate(this.angle*spinRatio,0,0,1);
	glScale(starRadius,starRadius,starRadius);
	glDrawStar(this.r,this.g,this.b);
	glPopMatrix();
};
Star.prototype.animate=function()
{
	this.angle+=this.rotSpeed;
	this.distance-=rotDecay;
	if(this.distance<0.0)
	{
		this.distance+=maxDistance;
		this.getColor();
	}
};
Star.prototype.getColor=function()
{
        this.r=(1.0+starShiness)*Math.random();
        this.g=(1.0+starShiness)*Math.random();
        this.b=(1.0+starShiness)*Math.random();
};
var stars = [];
var starsNumber=50;
function glInitStars()
{
	timer=new Date();
	startTime=timer.getTime();
	for(var i=0;i <starsNumber;i++)
	{
		var startingDistance=maxDistance*i/(starsNumber-1);
		var rotationSpeed=rotRatio*(i+0.0)/(starsNumber-1);
		stars.push(new Star(startingDistance,rotationSpeed));

	}
}
function glDrawStars()
{
	for(var i in stars)
	{
		stars[i].draw();
		stars[i].animate();
	}
}

