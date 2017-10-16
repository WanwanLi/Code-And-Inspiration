import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.applet.Applet;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndViewerAvatar extends Applet
{
	public void init()
	{
		Canvas3D canvas3D=new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		this.setLayout(new BorderLayout());
		this.add(canvas3D);	
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		ViewerAvatar ViewerAvatar1=new ViewerAvatar();
		ViewerAvatar1.addChild(new LineAxes(0.5f));
		SimpleUniverse1.getViewer().setAvatar(ViewerAvatar1);
		String[] args=new String[0];
		VirtualInputDevice VirtualInputDevice1=new VirtualInputDevice(args);
		VirtualInputDevice1.initialize();
		PhysicalEnvironment PhysicalEnvironment1=SimpleUniverse1.getViewer().getPhysicalEnvironment();
		PhysicalEnvironment1.addInputDevice(VirtualInputDevice1);
		PhysicalEnvironment1.setSensor(0,VirtualInputDevice1.getSensor(0));
		PhysicalEnvironment1.setCoexistenceCenterInPworldPolicy(View.NOMINAL_HEAD);
		View View1=SimpleUniverse1.getViewer().getView();
		View1.setUserHeadToVworldEnable(true);
		View1.setCoexistenceCenteringEnable(false);
		View1.setTrackingEnable(true);
		Screen3D screen3D=canvas3D.getScreen3D();
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0.1,0.1,0.0));
		screen3D.setTrackerBaseToImagePlate(transform3D);
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(Color.orange);
		Vector3f vector3f=new Vector3f(-1f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,0.8f,0f));
		Appearance1.setMaterial(Material1);
		Font3D font3D=new Font3D(new Font("Î¢ÈíÑÅºÚ",50,Font.BOLD),new FontExtrusion());
		Text3D text3D=new Text3D(font3D,"Java3D");
		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(0.5,0.5,0.5));
		transform3D.setTranslation(new Vector3f(-0.3f,0.0f,-0.3f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Shape3D(text3D,Appearance1));
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndViewerAvatar(),400,400);
	}
}
class LineAxes extends Shape3D
{
	public LineAxes(float l)
	{
		LineArray LineArray1=new LineArray(6,LineArray.COORDINATES);
		LineArray1.setCoordinate(0,new Point3f(-l,0f,0f));
		LineArray1.setCoordinate(1,new Point3f(l,0f,0f));
		LineArray1.setCoordinate(2,new Point3f(0f,0f,-l));
		LineArray1.setCoordinate(3,new Point3f(0f,0f,l));
		LineArray1.setCoordinate(4,new Point3f(0f,-l,0f));
		LineArray1.setCoordinate(5,new Point3f(0f,l,0f));
		this.setGeometry(LineArray1);
	}
}
class VirtualInputDevice implements InputDevice {

    private Vector3f position = new Vector3f();
    private Transform3D newTransform = new Transform3D();
    Sensor sensors[] = new Sensor[1];

    // The wheel controls control the view platform orientation
    private RotationControls rotControls;
 
    // The button position controls control the view platform position
    private PositionControls positionControls;

    private Transform3D rotTransX = new Transform3D();
    private Transform3D rotTransY = new Transform3D();
    private Transform3D rotTransZ = new Transform3D();

    private Vector3f initPos = new Vector3f();

    private int processingMode;
    private SensorRead sensorRead = new SensorRead();

    // These are the settable parameters.
    private boolean printvalues;
    private int xscreeninitloc;
    private int yscreeninitloc;
    private int xscreensize;
    private int yscreensize;
    private float xobjinitloc;
    private float yobjinitloc;
    private float zobjinitloc;
    private float xaxisrotinit;
    private float yaxisrotinit;
    private float zaxisrotinit;

    /* 
     * Create a device, and use the string arguments in args to construct
     * the device with user preferences.
     */
    public VirtualInputDevice( String[] args ) {

        // default user-definable values
        printvalues = false;
        xscreeninitloc = 550;
        yscreeninitloc = 0;
        xscreensize = 400;
        yscreensize = 200;
        xobjinitloc = 0.0f;
        yobjinitloc = 0.0f;
        zobjinitloc = 0.0f;
        xaxisrotinit = 0.0f;
        yaxisrotinit = 0.0f;
        zaxisrotinit = 0.0f;


        for(int i=0 ; i<args.length ; i+=2) {
           if(args[i] == null)
                  break;
           else if(args[i] == "printvalues")
                  printvalues =  (Boolean.valueOf(args[i+1])).booleanValue();
           else if(args[i] == "xscreeninitloc")
                  xscreeninitloc =  (Integer.valueOf(args[i+1])).intValue();
           else if(args[i] == "yscreeninitloc")
                  yscreeninitloc =  (Integer.valueOf(args[i+1])).intValue();
           else if(args[i] == "xscreensize")
                  xscreensize =  (Integer.valueOf(args[i+1])).intValue();
           else if(args[i] == "yscreensize")
                  yscreensize =  (Integer.valueOf(args[i+1])).intValue();
           else if(args[i] == "xobjinitloc")
                  xobjinitloc =  (Float.valueOf(args[i+1])).floatValue();
           else if(args[i] == "yobjinitloc")
                  yobjinitloc =  (Float.valueOf(args[i+1])).floatValue();
           else if(args[i] == "zobjinitloc")
                  zobjinitloc =  (Integer.valueOf(args[i+1])).floatValue();
        }

        if(printvalues ==  true) {
           System.out.println("Initial values for VirtualInputDevice:");
           System.out.println("xscreeninitloc = " + xscreeninitloc);
           System.out.println("yscreeninitloc = " + yscreeninitloc);
           System.out.println("xscreeninitsize = " + xscreensize);
           System.out.println("yscreeninitsize = " + yscreensize);
           System.out.println("xobjinitloc = " + xobjinitloc);
           System.out.println("yobjinitloc = " + yobjinitloc);
           System.out.println("zobjinitloc = " + zobjinitloc);
           System.out.println("xaxisrotinit = " + xaxisrotinit);
           System.out.println("yaxisrotinit = " + yaxisrotinit);
           System.out.println("zaxisrotinit = " + zaxisrotinit);
        }


        // initialize the InputDevice GUI
        Frame deviceFrame = new Frame();
        deviceFrame.setSize(xscreensize,yscreensize);
        deviceFrame.setLocation(xscreeninitloc, yscreeninitloc);
        deviceFrame.setTitle("Virtual Input Device");
        ButtonPositionControls positionControls;
        // initialize position with initial x, y, and z position
        positionControls = new ButtonPositionControls( xobjinitloc, 
                                                  yobjinitloc, zobjinitloc);
        WheelControls rotControls;
        // initialize rotations with initial angles in radians)
        rotControls = new WheelControls(xaxisrotinit, yaxisrotinit,
                                                            zaxisrotinit);
        positionControls.setDevice (this);
        Panel devicePanel = new Panel();
        devicePanel.setLayout( new BorderLayout() );
        devicePanel.add("East", positionControls );
        devicePanel.add("West", rotControls );
        deviceFrame.add( devicePanel );
        deviceFrame.pack();
        deviceFrame.setVisible(true);

        initPos.set(xobjinitloc, yobjinitloc, zobjinitloc);

        this.positionControls = positionControls;
        this.rotControls = rotControls;
        
        // default processing mode
        processingMode = InputDevice.DEMAND_DRIVEN;

	sensors[0] = new Sensor(this);
    }

    public void close() {
    }

    public int getProcessingMode() {
        return processingMode;
    }

    public int getSensorCount() {
	return sensors.length;
    }

    public Sensor getSensor( int sensorIndex ) {
	return sensors[sensorIndex];
    }

    public boolean initialize() {
	return true;
    }

    public void pollAndProcessInput() {

        sensorRead.setTime( System.currentTimeMillis() );
 
        rotTransX.rotX(-rotControls.getXAngle());
        rotTransY.rotY(-rotControls.getYAngle());
        rotTransZ.rotZ(-rotControls.getZAngle());
 
        positionControls.getPosition(position);
        newTransform.set(position);
        newTransform.mul( rotTransX );

        newTransform.mul(rotTransY);
        newTransform.mul(rotTransZ);

        sensorRead.set( newTransform );
        sensors[0].setNextSensorRead( sensorRead );
    }


    public void processStreamInput() {
    }


    public void setNominalPositionAndOrientation() {

        sensorRead.setTime( System.currentTimeMillis() );
 
        rotTransX.rotX(xaxisrotinit);
        rotTransY.rotY(yaxisrotinit);
        rotTransZ.rotZ(zaxisrotinit);
 
        position.set(initPos);
        
        newTransform.set( position );
 
        newTransform.mul(rotTransX);  
        newTransform.mul(rotTransY);
        newTransform.mul(rotTransZ);
 
        sensorRead.set( newTransform );
        sensors[0].setNextSensorRead( sensorRead );
        rotControls.reset();
        positionControls.setPosition(initPos);
    }



    public void setProcessingMode( int mode ) {

         // A typical driver might implement only one of these modes, and
         // throw an exception when there is an attempt to switch modes.
         // However, this example allows one to use any processing mode.

         switch(mode) {
            case InputDevice.DEMAND_DRIVEN:
            case InputDevice.NON_BLOCKING:
            case InputDevice.BLOCKING:
                 processingMode = mode;
            break;
            default:
               throw new IllegalArgumentException("Processing mode must " +"be one of DEMAND_DRIVEN, NON_BLOCKING, or BLOCKING");
         }
    }

}

interface RotationControls {

    /**
      * Get the angle of Rotation around the X Axis
      */
    public abstract float getXAngle();

    /**
      * Get the angle or Rotation around the Y Axis
      */
    public abstract float getYAngle();

    /**
      * Get the angle or Rotation around the Z Axis
      */
    public abstract float getZAngle();

    /**
      *  Reset angles to original angle.
      */
    public abstract void reset();
}

interface PositionControls {

    /**
      * Get the position
      */
    public void getPosition( Vector3f pos);

    /**
      * Set the position
      */
    public void setPosition( Vector3f pos);

    /**
      * Increment added to position each time mouse is pressed
      * or if the mouse is held down each time the Sensor is
      * read
      */
    public void setStepRate( float stepRate );
}
class ButtonPositionControls extends Panel implements PositionControls, MouseListener {
    private final static int STILL=0;
    private final static int MOVING_UP=1;
    private final static int MOVING_DOWN=2;
    private final static int MOVING_LEFT=3;
    private final static int MOVING_RIGHT=4;
    private final static int MOVING_FORWARD=5;
    private final static int MOVING_BACK=6;

    // initial mode
    private int mode = STILL;

    Vector3f position = new Vector3f();
    Vector3f orig_position = new Vector3f();

    private Button leftB = new Button("Move Left");
    private Button rightB = new Button("Move Right");
    private Button upB = new Button("Move Up");
    private Button downB = new Button("Move Down");

    private Button forwardB = new Button("Move Forward");
    private Button backwardB = new Button("Move Back");

    private Button reset = new Button("Reset");
    private InputDevice device;

    private float step_rate = 0.00023f;   // movement rate per millisecond
    private long time_last_state_change = System.currentTimeMillis();

    // the constructor arguments are the intitial X, Y, and Z positions
    public ButtonPositionControls( float x, float y, float z ) {

        // up, down, right, and left movement buttons
        Panel panPanel = new Panel();
        panPanel.setLayout( new BorderLayout() );
        panPanel.add("North", upB);
        panPanel.add("East", rightB);
        panPanel.add("South", downB);
        panPanel.add("West", leftB);

        // forward, backward, and reset buttons 
        Panel p = new Panel();
        p.setLayout( new GridLayout(0,1,0,0) );
        p.add(forwardB);
        p.add(backwardB);
        p.add(reset);

        // set the initial position
        position.x = x;
        position.y = y;
        position.z = z;
        orig_position.set(position);

        // add a mouse listener to each button
        upB.addMouseListener(this);
        downB.addMouseListener(this);
        leftB.addMouseListener(this);
        rightB.addMouseListener(this);
        forwardB.addMouseListener(this);
        backwardB.addMouseListener(this);
        reset.addMouseListener(this);

	this.setLayout( new BorderLayout() );
        add("East", p );
	add("West", panPanel );
    }

    public void setDevice ( InputDevice device) {
        this.device = device;
    }

    public void getPosition(Vector3f pos ) {
	calculateMotion();
	pos.set(position);
    }

    public void setPosition(Vector3f pos ) {
	position.set(pos);
    }

    public void setStepRate( float stepRate ) {
	step_rate = stepRate;
    }

    private void calculateMotion() {

        long current_time = System.currentTimeMillis();
        long elapsed_time = current_time - time_last_state_change;

        switch(mode) {
            case STILL:
                break;
            case MOVING_LEFT:
                position.x = orig_position.x - step_rate*elapsed_time;
                break;
            case MOVING_RIGHT:
                position.x = orig_position.x + step_rate*elapsed_time;
                break;
            case MOVING_UP:
                position.y = orig_position.y + step_rate*elapsed_time;
                break;
            case MOVING_DOWN:
                position.y = orig_position.y - step_rate*elapsed_time;
                break;
            case MOVING_FORWARD:
                position.z = orig_position.z - step_rate*elapsed_time;
                break;
            case MOVING_BACK:
                position.z = orig_position.z + step_rate*elapsed_time;
                break;
            default:
                throw( new RuntimeException("Unknown motion"));
        }
    }

    public void mouseClicked( MouseEvent e ) {
    }
 
    public void mouseEntered( MouseEvent e ) {
    }

    public void mouseExited( MouseEvent e ) {
    }

    public void mousePressed( MouseEvent e ) {
        if (e.getSource()==leftB && mode != MOVING_LEFT) {
              time_last_state_change =  System.currentTimeMillis();
              mode = MOVING_LEFT;
              orig_position.set(position);
        } else if (e.getSource()==rightB && mode != MOVING_RIGHT) {
              time_last_state_change =  System.currentTimeMillis();
              mode = MOVING_RIGHT;
              orig_position.set(position);
        } else if (e.getSource()==upB && mode != MOVING_UP) {
              time_last_state_change =  System.currentTimeMillis();
              mode = MOVING_UP;
              orig_position.set(position);
        } else if (e.getSource()==downB && mode != MOVING_DOWN) {
              time_last_state_change =  System.currentTimeMillis();
              mode = MOVING_DOWN;
              orig_position.set(position);
        } else if (e.getSource()==forwardB && mode != MOVING_FORWARD) {
              time_last_state_change =  System.currentTimeMillis();
              mode = MOVING_FORWARD;
              orig_position.set(position);
        } else if (e.getSource()==backwardB && mode != MOVING_BACK) {
              time_last_state_change =  System.currentTimeMillis();
              mode = MOVING_BACK;
              orig_position.set(position);
        } else if (e.getSource()==reset) {
              device.setNominalPositionAndOrientation();
        }
    }

    public void mouseReleased( MouseEvent e ) {
        mode = STILL;
    }
}
class WheelControls extends Canvas implements RotationControls, MouseMotionListener, MouseListener {

    private final static int NONE=0;
    private final static int SLIDE_Y=1;
    private final static int SLIDE_X=2;
    private final static int SLIDE_Z=3;

    private int mode = NONE;

    private Dimension size;
    private int thickness;
    private int diameter;
    private int space;
    private int pipSize;
    private int pipOffset;	// Amount pip is below wheel
    private int margin;		// Margin between edge of Canvas and
				// controls

    private Polygon yPip;
    private Rectangle yBackClip;

    private Polygon xPip;
    private Rectangle xBackClip;

    private Polygon zPip;

    private Rectangle yArea;
    private Rectangle xArea;
    private Rectangle zArea;

    private Point oldMousePos = new Point();

    float yAngle = 0.0f;
    float xAngle = 0.0f;
    float zAngle = 0.0f;

    float yOrigAngle;
    float xOrigAngle;
    float zOrigAngle;

    float angleStep = (float)Math.PI/30.0f;

    public WheelControls() {
	this(0.0f, 0.0f, 0.0f);
    }

    public WheelControls( float rotX, float rotY, float rotZ ) {
	size = new Dimension( 200, 200 );

	xAngle = constrainAngle(rotX);
	yAngle = constrainAngle(rotY);
	zAngle = constrainAngle(rotZ);

        yOrigAngle = yAngle;
        xOrigAngle = xAngle;
        zOrigAngle = zAngle;

	setSizes();

	yPip = new Polygon();
	yPip.addPoint( 0, 0 );
	yPip.addPoint( -pipSize/2, pipSize );
	yPip.addPoint( pipSize/2, pipSize );

	xPip = new Polygon();
	xPip.addPoint(0,0);
	xPip.addPoint( pipSize, -pipSize/2 );
	xPip.addPoint( pipSize, pipSize/2 );

	zPip = new Polygon();
	zPip.addPoint( diameter/2, pipOffset );
	zPip.addPoint( diameter/2-pipSize/2, pipOffset-pipSize );
	zPip.addPoint( diameter/2+pipSize/2, pipOffset-pipSize );

	addMouseListener( this );
	addMouseMotionListener( this );
    }

    private void setSizes() {
	margin = 10;
	int width = size.width - margin*2;
	thickness = width * 7 / 100;
	diameter = width * 70 / 100;
	space = width * 10 / 100;
	pipSize = width * 7 / 100;

	pipOffset = thickness/2;

    }

    public void paint( Graphics g ) {
	Graphics2D g2 = (Graphics2D)g;

	g.drawOval( margin,margin, diameter, diameter );
	zArea = new Rectangle( margin, margin, diameter, diameter );
	drawZPip( g2, zAngle );

	g.drawRect( margin, margin+diameter+space, 
		    diameter, thickness ); // Y Wheel
	yArea = new Rectangle( margin, margin+diameter+space, margin+diameter, 
			       thickness+pipOffset );
	yBackClip = new Rectangle( margin-thickness, 
				   margin+diameter+space+thickness, 
				   margin+diameter+thickness*2, thickness );
	drawYPip( g2, yAngle );

	g.drawRect( margin+diameter+space, margin, 
		    thickness, diameter ); // X Wheel
	xArea = new Rectangle( margin+diameter+space, margin, 
			       thickness+pipOffset, margin+diameter );
	xBackClip = new Rectangle( margin+diameter+space+thickness, 
				   margin-thickness, 
				   thickness, margin+diameter+thickness*2 );
	drawXPip( g2, xAngle );


    }

    public float getXAngle() {
	return xAngle;
    }

    public float getYAngle() {
	return yAngle;
    }

    public float getZAngle() {
	return zAngle;
    }


    public void reset() {
                // Overwrite the old pip
                drawYPip( (Graphics2D)(this.getGraphics()),
                          yAngle );
                yAngle = yOrigAngle;
                // Draw the new Pip
                drawYPip( (Graphics2D)(this.getGraphics()),
                          yAngle );

                // Overwrite the old pip
                drawXPip( (Graphics2D)(this.getGraphics()),
                          xAngle );
                xAngle = xOrigAngle;
                // Draw the new Pip
                drawXPip( (Graphics2D)(this.getGraphics()),
                          xAngle );

                drawZPip( (Graphics2D)(this.getGraphics()),
                          zAngle );
 
                zAngle =  zOrigAngle;

                drawZPip( (Graphics2D)(this.getGraphics()),
                          zAngle );
                oldMousePos.setLocation(0,0);
    }


    private void drawXPip( Graphics2D g2, float angle ) {
	AffineTransform trans = new AffineTransform();
	int y;
	int xOrig = margin+diameter+space;
	int yOrig = margin;
	Color origColor = g2.getColor();

	if (angle <= Math.PI) {
	    y = yOrig + diameter - (int)((Math.abs( angle-Math.PI/2 )/(Math.PI/2)) * diameter/2);
	} else
	    y = yOrig + (int)((Math.abs( (angle-Math.PI*1.5) )/(Math.PI/2)) * diameter/2);

	if (angle<Math.PI/2 || angle > Math.PI*1.5)
	    g2.setColor( Color.red );		// Infront of wheel
	else {
	    g2.setColor( Color.black );		// Behind Wheel
	    g2.setClip( xBackClip );
	}

	g2.setXORMode( getBackground() );
	trans.setToTranslation( xOrig+pipOffset, y );
	g2.setTransform( trans );
	g2.fillPolygon( xPip );

	// Reset graphics context
	trans.setToIdentity();
	g2.setTransform( trans );
	g2.setColor(origColor);
	g2.setPaintMode();
    }

    private void drawYPip( Graphics2D g2, float angle ) {
	AffineTransform trans = new AffineTransform();
	int x;
	int xOrig = margin;
	int yOrig = margin+diameter+space;
	Color origColor = g2.getColor();

	if (angle <= Math.PI) {
	    x = xOrig + diameter - (int)((Math.abs( angle-Math.PI/2 )/(Math.PI/2)) * diameter/2);
	} else
	    x = xOrig + (int)((Math.abs( (angle-Math.PI*1.5) )/(Math.PI/2)) * diameter/2);

	if (angle<Math.PI/2 || angle > Math.PI*1.5)
	    g2.setColor( Color.red );		// Infront on wheel
	else {
	    g2.setColor( Color.black );		// Behind Wheel
	    g2.setClip( yBackClip );
	}

	g2.setXORMode( getBackground() );
	trans.setToTranslation( x, yOrig+pipOffset );
	g2.setTransform( trans );
	g2.fillPolygon( yPip );

	// Reset graphics context
	trans.setToIdentity();
	g2.setTransform( trans );
	g2.setColor(origColor);
	g2.setPaintMode();
    }

    private void drawZPip( Graphics2D g2, float zAngle ) {
	AffineTransform trans = new AffineTransform();
	Color origColor = g2.getColor();

	trans.translate( margin, margin );
	trans.rotate(zAngle, diameter/2, diameter/2 );

	g2.setXORMode( getBackground() );
	g2.setTransform(trans);
	g2.setColor( Color.red );
	g2.fillPolygon( zPip );

	// Reset graphics context
	trans.setToIdentity();
	g2.setTransform( trans );
	g2.setColor( origColor );
	g2.setPaintMode();
    }

    public Dimension getPreferredSize() {
	return size;
    }

    public void setSize( Dimension d ) {
	// Set size to smallest dimension
	if (d.width<d.height)
	    size.width = size.height = d.width;
	else
	    size.width = size.height = d.height;
	setSizes();
    }

    public void mouseClicked( MouseEvent e ) {
    }

    public void mouseEntered( MouseEvent e ) {
    }

    public void mouseExited( MouseEvent e ) {
    }

    public void mousePressed( MouseEvent e ) {
	if ( yArea.contains( e.getPoint() )) {
	    mode = SLIDE_Y;
	    oldMousePos = e.getPoint();
	} else if (xArea.contains( e.getPoint() )) {
	    mode = SLIDE_X;
	    oldMousePos = e.getPoint();
	} else if (zArea.contains( e.getPoint() )) {
	    mode = SLIDE_Z;
	    oldMousePos = e.getPoint();
	}
    }

    public void mouseReleased( MouseEvent e ) {
	mode = NONE;
    }

    public void mouseDragged( MouseEvent e ) {
	Point pos = e.getPoint();

	int diffX = pos.x - oldMousePos.x;
	int diffY = pos.y - oldMousePos.y;

	switch(mode) {
	    case NONE:
		break;
	    case SLIDE_Y:
		// Overwrite the old pip
		drawYPip( (Graphics2D)((Canvas)e.getSource()).getGraphics(),
			  yAngle );
		if (diffX<0)
		    yAngle -= angleStep;
		else if (diffX>0)
		    yAngle += angleStep;

		yAngle = constrainAngle(yAngle);

		// Draw the new Pip
		drawYPip( (Graphics2D)((Canvas)e.getSource()).getGraphics(),
			  yAngle );
	        oldMousePos = pos;
		break;
	    case SLIDE_X:
		// Overwrite the old pip
		drawXPip( (Graphics2D)((Canvas)e.getSource()).getGraphics(),
			  xAngle );
		if (diffY<0)
		    xAngle -= angleStep;
		else if (diffY>0)
		    xAngle += angleStep;

		xAngle = constrainAngle(xAngle);

		// Draw the new Pip
		drawXPip( (Graphics2D)((Canvas)e.getSource()).getGraphics(),
			  xAngle );
	        oldMousePos = pos;
		break;
	    case SLIDE_Z:
		drawZPip( (Graphics2D)((Canvas)e.getSource()).getGraphics(),
			  zAngle );

		if (diffX<0)
		    zAngle -= angleStep;
		else if (diffX>0)
		    zAngle += angleStep;

		zAngle = constrainAngle( zAngle );
		drawZPip( (Graphics2D)((Canvas)e.getSource()).getGraphics(),
			  zAngle );
	        oldMousePos = pos;
		break;
	    default:
		throw( new RuntimeException("Internal Error"));
	}
    }

    public void mouseMoved( MouseEvent e ) {
    }

    /**
      * Constrain angle to be 0<angle<2PI
      */
    private float constrainAngle( float angle ) {
        if ( angle > (float)Math.PI*2 ) return angle-(float)Math.PI*2;
        if ( angle < 0.0f) return angle+(float)Math.PI*2;
	return angle;
    }
}
