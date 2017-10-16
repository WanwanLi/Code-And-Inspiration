import java.io.*;
import java.net.*;
import java.sql.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
class JavaAndRemoteDesktopSystem
{
	public static void main(String[] args)
	{
		new RemoteDesktopSystemFrame();
	}
}
class RemoteDesktopSystemFrame extends JFrame implements ActionListener
{
	private Timer timer;
	RemoteDesktopSystemPanel remoteDesktopSystemPanel;
	public RemoteDesktopSystemFrame()
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		int screenWidth=(int)toolkit.getScreenSize().getWidth();
		int screenHeight=(int)toolkit.getScreenSize().getHeight();
		int taskbarHeight=30;
		Image iconImage=toolkit.getImage("images\\icons\\icon.JPG");
		this.timer=new Timer(100,this);
		this.timer.start();
		RemoteDesktopSystemPanel remoteDesktopSystemPanel=new RemoteDesktopSystemPanel(this,screenWidth,screenHeight,timer);
		this.setBackground(Color.black);
		this.setIconImage(iconImage);
		this.setBounds(0,-taskbarHeight+5,screenWidth,screenHeight+taskbarHeight);
		this.setResizable(false);
		this.setLayout(null);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.setVisible(true);
	}
	public void actionPerformed(ActionEvent e)
	{
System.out.println("p=");
		if(remoteDesktopSystemPanel!=null)this.remoteDesktopSystemPanel.repaint();
	}
}
class RemoteDesktopSystemPanel extends JPanel implements KeyListener,MouseListener,MouseMotionListener,TextListener,Runnable
{
	private String databaseName="DesktopDatabase";
	private String tableName="InformationTable";
	private String password="11235813";
	private String inputPassword="";
	private int state;
	private int screenWidth,screenHeight;
	private int mouseX,mouseY,mouseX0,mouseY0,windowX,windowY,X0,Y0,L=200,dX=L/4,dY=L/2,filePositionX=0,filePositionX0=0;
	private int frameX=20,frameY=45,insertframeX=30,insertframeY=40,updateframeX=80,updateframeY=153,selectframeX=29,selectframeY=29,alterframeX=29,alterframeY=40;
	private int messageboxX=184,messageboxY=301;
	private int filePosition_I=-1,filePosition_J=-1,filePosition_maxJ=0,itemPosition=-1,itemPositionY=0,itemPositionY0=0,catalogItemPositionY=0,catalogItemPositionY0=0,listPosition=-1;
	private int classPosition=-1,currentClassPosition=-1,attributePosition=-1,currentAttributePosition=-1,currentCatalogPosition=-1,newCatalogPosition=-1;
	private int catalogPosition=-1,choicePosition=-1,catalogItemPosition=-1,choiceItemPosition=-1;
	private int beginCatalogLine=0,beginDiskLine=0,beginAttributeField=0,beginClassPosition=0,beginAttributePosition=0,beginCatalogPosition=0;
	private final int REGISTER=-1;
	private final int BEGIN=0;
	private final int START=1;
	private final int MENU=2;
	private final int FRAME=3;
	private final int CLOSE_FRAME=4;
	private final int WINDOW=5;
	private final int WINDOW_INSERT=6;
	private final int INSERT=7;
	private final int CLOSE_INSERT=8;
	private final int ON_INSERT_BUTTON=9;
	private final int MESSAGE=10;
	private final int ON_MESSAGE_BUTTON=11;
	private final int ON_EXIT_BUTTON=12;
	private final int ON_START_BUTTON=13;
	private final int ON_FRAME_BUTTON=14;
	private final int UPDATE_FRAME=15;
	private final int ON_UPDATE_BUTTON=16;
	private final int CLOSE_UPDATE_FRAME=17;
	private final int WINDOW_SELECT=18;
	private final int SELECT=19;
	private final int CLOSE_SELECT=20;
	private final int SELECT_LIST=21;
	private final int WINDOW_ALTER=22;
	private final int ALTER=23;
	private final int CLOSE_ALTER=24;
	private final int ON_FRAME_BLUE_BUTTON=25;
	private final int REMOTE=26;
	private final int FILE_IMG=3,FILE_TXT=11,FILE_AUD=12;
	private Color Color_darkblue=new Color(60,100,150);
	private Color Color_lightgray=new Color(100,100,100);
	private Color Color_transparentblue=new Color(168f/255,202f/255,236f/255,0.3f);
	private Color Color_transparentwhite=new Color(1f,1f,1f,0.5f);
	private Color Color_transparentblack=new Color(0f,0f,0f,0.5f);
	private Color Color_lightgreen=new Color(113,223,141);
	private Color Color_whitegreen=new Color(199,240,214);
	private Font bigFont,middleFont,smallFont,littleFont;
	private Image registerbackgroundImage,registerbuttonImage,registerbutton_shineImage,symbolImage;
	private Image backgroundImage,smallbackgroundImage,background3DImage,fileImage,smallfileImage,file3DImage,classbuttonImage,shiningclassbuttonImage,desktopbuttonImage,desktopshiningbuttonImage;
	private Image frameImage,framebuttonImage,closeframeImage,insertframeImage,closeinsertframeImage,iconImage,startImage,menuImage;
	private Image updateframeImage,closeupdateframeImage,selectframeImage,alterframeImage,smallbluebuttonImage,catalogfieldImage;
	private Image windowImage,insertImage,selectImage,alterImage,intImage,textImage,attributeImage,diskImage,messageboxImage,bluebuttonImage;
	private Image backfieldImage,bigfieldImage,smallfieldImage,selectbuttonImage,listframeImage,listbuttonImage,attributefieldImage,smallfileiconImage;
	private Image windowbuttonImage,insertbuttonImage,simplebuttonImage,shinebuttonImage,exitbuttonImage,updatebuttonImage,crystalbuttonImage,darkbuttonImage;
	private Image computerImage,notepadImage,pictureImage,recycle_emptyImage,recycle_fullImage,startbuttonImage,catalogitembuttonImage,shieldImage;
	private Image[] itemsImages,catalogitemsImages;
	private int startImageHeight=40;
	private int fileIndex;
	private int[] IDs,newIDs;
	private String nameField;
	private int classField;
	private int itemsImagesLength,catalogitemsImagesLength;
	private String[] names,newNames;
	private String[] items;
	private String database;
	private String[] itemTable;
	private String[] settingTable;
	private String[] catalogFields;
	private String[] attributeFields;
	private String[] classFieldStrings;
	private Choice[] catalogChoices;
	private String[][] catalogChoicesTable;
	private boolean[] catalogStateTable;
	private boolean[][] catalogChoicesStateTable;
	private TextField[] attributeTextFields;
	private String selectText="";
	private TextField selectTextField,attributeTextField,catalogTextField,catalogContentTextField,attributeFieldNameTextField,attributeFieldTypeTextField;
	private TextArea updateTextArea;
	private String attributeFieldName,attributeFieldType;
	private String listText="";
	private MySQLprocessor mySQLprocessor;
	private String messageString="message!",messagebuttonString="Ok";
	private Frame screenFrame;
	private String settingtableFile="database\\setting.table";
	private String catalogfieldFile="database\\catalog.field";
	private String attributefieldFile="database\\attribute.field";
	private int isOnShiningClassButton=0,isOnCrystalButton=0;
	private int criticalAttributePosition,criticalCatalogPosition;
	private boolean isOnRegisterButton=false;
	private String[] remoteInfo;
	private String[] remoteFiles;
	private File remoteFileDir;
	private int remotePosition,remotePositionY,remotePositionY0;
	private int remoteFilePosition,remoteFilePositionY,remoteFilePositionY0;
	private Socket socket;
	private String IP="127.0.0.1";
	//private String IP="172.17.211.196";
	private int port=8080;
	private int progressBarLength=0,fileLength=0;
	private Timer timer;
	private String remoteFileName;
	public RemoteDesktopSystemPanel(Frame screenFrame,int screenWidth,int screenHeight,Timer timer)
	{
		this.timer=timer;
		this.remoteFileDir=new File("file");
		this.remoteFiles=remoteFileDir.list();
		this.settingTable=this.readStringsFromFile(settingtableFile);
		this.catalogFields=this.readStringsFromFile(catalogfieldFile);
		this.attributeFields=this.readStringsFromFile(attributefieldFile);
		this.nameField=this.attributeFields[Integer.parseInt(settingTable[0])];
		this.classField=Integer.parseInt(settingTable[1]);
		this.classFieldStrings=this.getChoiceStrings(catalogFields[classField*2+1]);
		this.itemsImagesLength=Integer.parseInt(settingTable[2]);
		this.catalogitemsImagesLength=Integer.parseInt(settingTable[3]);
		this.criticalAttributePosition=Integer.parseInt(settingTable[4]);
		this.criticalCatalogPosition=Integer.parseInt(settingTable[5]);;
		this.screenFrame=screenFrame;
		this.screenWidth=screenWidth;
		this.screenHeight=screenHeight;
		this.mySQLprocessor=new MySQLprocessor(databaseName);
		try{this.socket=new Socket(IP,port);}catch(Exception e){System.err.println(e);}
		this.updateIDsAndNames();
		//this.state=REGISTER;
		this.state=BEGIN;
		this.setBounds(-3,-6,screenWidth,screenHeight);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setFocusable(true);
		this.setVisible(true);
		this.setLayout(null);
		this.initializeFonts();
		this.initializeImages();
		this.initializeTextFields();
		this.initializeTextAreas();
		this.screenFrame.add(this);
	}
	private void initializeFonts()
	{
		this.bigFont=new Font(null,Font.BOLD,30);
		this.middleFont=new Font(null,Font.BOLD,20);
		this.smallFont=new Font(null,Font.BOLD,13);
		this.littleFont=new Font(null,Font.PLAIN,10);
	}
	private void initializeImages()
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		this.registerbackgroundImage=toolkit.getImage("images\\register\\registerbackground.JPG");
		this.registerbuttonImage=toolkit.getImage("images\\register\\registerbutton.JPG");
		this.registerbutton_shineImage=toolkit.getImage("images\\register\\registerbutton_shine.JPG");
		this.symbolImage=toolkit.getImage("images\\register\\symbol.GIF");
		this.iconImage=toolkit.getImage("images\\icons\\icon.JPG");
		this.startImage=toolkit.getImage("images\\start.jpg");
		this.menuImage=toolkit.getImage("images\\menu.jpg");
		this.fileImage=toolkit.getImage("images\\file.gif");
		this.smallfileImage=toolkit.getImage("images\\smallfile.gif");
		this.file3DImage=toolkit.getImage("images\\file3D.gif");
		this.classbuttonImage=toolkit.getImage("images\\classbutton.jpg");
		this.shiningclassbuttonImage=toolkit.getImage("images\\shiningclassbutton.jpg");
		this.desktopbuttonImage=toolkit.getImage("images\\desktopbutton.jpg");
		this.desktopshiningbuttonImage=toolkit.getImage("images\\desktopshiningbutton.jpg");
		this.backgroundImage=toolkit.getImage("images\\background.jpg");
		this.smallbackgroundImage=toolkit.getImage("images\\smallbackground.jpg");
		this.background3DImage=toolkit.getImage("images\\background3D.gif");
		this.frameImage=toolkit.getImage("images\\frame.jpg");
		this.framebuttonImage=toolkit.getImage("images\\framebutton.jpg");
		this.bluebuttonImage=toolkit.getImage("images\\bluebutton.jpg");
		this.insertframeImage=toolkit.getImage("images\\insertframe.jpg");
		this.selectframeImage=toolkit.getImage("images\\selectframe.gif");
		this.alterframeImage=toolkit.getImage("images\\alterframe.jpg");
		this.updateframeImage=toolkit.getImage("images\\updateframe.gif");
		this.updatebuttonImage=toolkit.getImage("images\\updatebutton.jpg");
		this.closeinsertframeImage=toolkit.getImage("images\\closeinsertframe.jpg");
		this.closeframeImage=toolkit.getImage("images\\closeframe.jpg");
		this.closeupdateframeImage=toolkit.getImage("images\\closeupdateframe.gif");
		this.windowImage=toolkit.getImage("images\\window.jpg");
		this.insertImage=toolkit.getImage("images\\icons\\insert.gif");
		this.selectImage=toolkit.getImage("images\\icons\\select.gif");
		this.alterImage=toolkit.getImage("images\\icons\\alter.gif");
		this.insertbuttonImage=toolkit.getImage("images\\icons\\insertbutton.jpg");
		this.windowbuttonImage=toolkit.getImage("images\\icons\\windowbutton.jpg");
		this.simplebuttonImage=toolkit.getImage("images\\icons\\simplebutton.gif");
		this.shinebuttonImage=toolkit.getImage("images\\icons\\shinebutton.gif");
		this.exitbuttonImage=toolkit.getImage("images\\icons\\exitbutton.jpg");
		this.intImage=toolkit.getImage("images\\icons\\int.jpg");
		this.textImage=toolkit.getImage("images\\icons\\text.jpg");
		this.attributeImage=toolkit.getImage("images\\icons\\attribute.gif");
		this.attributefieldImage=toolkit.getImage("images\\icons\\attributefield.gif");
		this.catalogfieldImage=toolkit.getImage("images\\icons\\catalogfield.gif");
		this.smallfileiconImage=toolkit.getImage("images\\icons\\smallfileicon.gif");
		this.backfieldImage=toolkit.getImage("images\\icons\\backfield.gif");
		this.bigfieldImage=toolkit.getImage("images\\icons\\bigfield.jpg");
		this.smallfieldImage=toolkit.getImage("images\\icons\\smallfield.jpg");
		this.selectbuttonImage=toolkit.getImage("images\\icons\\selectbutton.jpg");
		this.crystalbuttonImage=toolkit.getImage("images\\icons\\crystalbutton.jpg");
		this.darkbuttonImage=toolkit.getImage("images\\icons\\darkbutton.jpg");
		this.listframeImage=toolkit.getImage("images\\icons\\listframe.jpg");
		this.listbuttonImage=toolkit.getImage("images\\icons\\listbutton.jpg");
		this.catalogitembuttonImage=toolkit.getImage("images\\icons\\catalogitembutton.jpg");
		this.smallbluebuttonImage=toolkit.getImage("images\\icons\\smallbluebutton.jpg");
		this.diskImage=toolkit.getImage("images\\disk.gif");
		this.messageboxImage=toolkit.getImage("images\\messagebox.gif");
		this.computerImage=toolkit.getImage("images\\start\\computer.gif");
		this.notepadImage=toolkit.getImage("images\\start\\notepad.gif");
		this.pictureImage=toolkit.getImage("images\\start\\picture.gif");
		this.recycle_emptyImage=toolkit.getImage("images\\start\\recycle_empty.gif");
		this.recycle_fullImage=toolkit.getImage("images\\start\\recycle_full.gif");
		this.startbuttonImage=toolkit.getImage("images\\start\\startbutton.jpg");
		this.shieldImage=toolkit.getImage("images\\icons\\shield.gif");
		this.itemsImages=new Image[itemsImagesLength];
		this.catalogitemsImages=new Image[catalogitemsImagesLength];
		for(int i=0;i<itemsImagesLength;i++)this.itemsImages[i]=toolkit.getImage("images\\icons\\items\\item"+i+".gif");
		for(int i=0;i<catalogitemsImagesLength;i++)this.catalogitemsImages[i]=toolkit.getImage("images\\icons\\catalogitems\\catalogitem"+i+".gif");
	}
	private void initializeTextFields()
	{
		this.attributeTextFields=new TextField[attributeFields.length/2];
		int x0=insertframeX+260,y0=insertframeY+250,width=370,height=20,interval=5;
		for(int i=0;i<attributeTextFields.length;i++)
		{
			this.attributeTextFields[i]=new TextField();
			this.attributeTextFields[i].setText(attributeFields[i*2]);
			this.attributeTextFields[i].setBounds(x0,y0+(height+interval)*i,width,height);
			this.attributeTextFields[i].setFont(smallFont);
			this.attributeTextFields[i].setVisible(false);
			this.add(attributeTextFields[i]);
		}
		this.initializeChoices();
		if(selectTextField!=null)return;
		this.selectTextField=new TextField();
		this.selectTextField.setForeground(Color.black);
		this.selectTextField.setBounds(selectframeX+81,selectframeY+38,674,23);
		this.selectTextField.setFont(middleFont);
		this.selectTextField.setVisible(false);
		this.selectTextField.addTextListener(this);
		this.add(selectTextField);
		this.attributeTextField=new TextField();
		this.attributeTextField.setBackground(Color.black);
		this.attributeTextField.setForeground(Color.white);
		this.attributeTextField.setFont(smallFont);
		this.attributeTextField.setVisible(false);
		this.add(attributeTextField);
		this.catalogTextField=new TextField();
		this.catalogTextField.setBackground(Color.black);
		this.catalogTextField.setForeground(Color.white);
		this.catalogTextField.setFont(smallFont);
		this.catalogTextField.setVisible(false);
		this.add(catalogTextField);
		this.catalogContentTextField=new TextField();
		this.catalogContentTextField.setBackground(Color.black);
		this.catalogContentTextField.setForeground(Color.white);
		this.catalogContentTextField.setFont(smallFont);
		this.catalogContentTextField.setVisible(false);
		this.add(catalogContentTextField);
		this.attributeFieldNameTextField=new TextField();
		this.attributeFieldNameTextField.setBackground(Color.white);
		this.attributeFieldNameTextField.setForeground(Color.black);
		this.attributeFieldNameTextField.setFont(middleFont);
		this.attributeFieldNameTextField.setBounds(alterframeX+382,alterframeY+239,136,28);
		this.attributeFieldNameTextField.setVisible(false);
		this.add(attributeFieldNameTextField);
		this.attributeFieldTypeTextField=new TextField();
		this.attributeFieldTypeTextField.setBackground(Color.white);
		this.attributeFieldTypeTextField.setForeground(Color.black);
		this.attributeFieldTypeTextField.setFont(middleFont);
		this.attributeFieldTypeTextField.setBounds(alterframeX+382,alterframeY+296,136,28);
		this.attributeFieldTypeTextField.setVisible(false);
		this.add(attributeFieldTypeTextField);
	}
	private void initializeTextAreas()
	{
		this.updateTextArea=new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		this.updateTextArea.setBounds(updateframeX+7,updateframeY+119,776,313);
		this.updateTextArea.setFont(middleFont);
		this.updateTextArea.setVisible(false);
		this.screenFrame.add(updateTextArea);
	}
	private void initializeChoices()
	{
		this.catalogStateTable=new boolean[catalogFields.length/2];
		this.catalogChoices=new Choice[catalogFields.length/2];
		this.catalogChoicesStateTable=new boolean[catalogFields.length/2][];
		this.catalogChoicesTable=new String[catalogFields.length/2][];
		int x0=insertframeX+40,y0=insertframeY+145,width=140,height=20,interval=30;
		for(int i=0;i<catalogChoices.length;i++)
		{
			this.catalogStateTable[i]=false;
			this.catalogChoices[i]=new Choice();
			String[] choiceStrings=this.getChoiceStrings(catalogFields[i*2+1]);
			this.catalogChoicesTable[i]=choiceStrings;
			this.catalogChoicesStateTable[i]=new boolean[choiceStrings.length];
			for(int j=0;j<choiceStrings.length;j++)
			{
				this.catalogChoicesStateTable[i][j]=false;
				this.catalogChoices[i].add(choiceStrings[j]);
			}
			this.catalogChoices[i].setBounds(x0,y0+(height+interval)*i,width,height);
			this.catalogChoices[i].setFont(smallFont);
			this.catalogChoices[i].setVisible(false);
			this.add(catalogChoices[i]);
		}
	}
	private String[] getChoiceStrings(String string)
	{
		int l=string.length(),k=0,j=0;
		for(int i=0;i<l;i++)if(string.charAt(i)=='|')k++;
		String[] strings=new String[k];
		for(int i=0;i<k;i++)
		{
			strings[i]="";
			char c=string.charAt(j++);
			while(c!='|')
			{
				strings[i]+=c;
				c=string.charAt(j++);
			}
		}
		return strings;
	}
	private void updateIDsAndNames()
	{
		this.IDs=mySQLprocessor.executeQuery("ID",tableName);
		this.names=mySQLprocessor.executeQuery(nameField,IDs,tableName);
	}
	private void updateNewIDsAndNewNames()
	{
		if(classPosition==classFieldStrings.length)this.newIDs=mySQLprocessor.executeQuery("ID",tableName);
		else
		{
			String whereCluase=" Where "+catalogFields[classField*2+0]+"=\'"+classFieldStrings[classPosition]+"\'";
			this.newIDs=mySQLprocessor.executeQuery("ID",tableName+whereCluase);
		}
		this.newNames=mySQLprocessor.executeQuery(nameField,newIDs,tableName);
	}
	public void paint(Graphics g)
	{
		if(state==REGISTER){this.drawRegisterFrame(g);return;}
		g.drawImage(backgroundImage,0,0,this);
		this.drawFiles(g);
		if(isOnShiningClassButton==1)this.drawBackground3D(g);
		this.drawClassButtons(g);
		if(state==START)g.drawImage(startImage,0,screenHeight-startImageHeight-10,this);
		else if(state==MENU||state==ON_START_BUTTON)this.drawMenu(g);
		else if(state==FRAME||state==ON_FRAME_BUTTON||state==ON_FRAME_BLUE_BUTTON)this.drawFrame(g);
		else if(state==CLOSE_FRAME)
		{
			this.drawFrame(g);
			g.drawImage(closeframeImage,frameX+820,frameY+4,this);
		}
		else if(state==UPDATE_FRAME)
		{
			this.drawFrame(g);
			this.drawUpdateFrame(g);
		}
		else if(state==CLOSE_UPDATE_FRAME)
		{
			this.drawFrame(g);
			this.drawUpdateFrame(g);
			g.drawImage(closeupdateframeImage,updateframeX+730,updateframeY+2,this);
		}
		else if(state==ON_UPDATE_BUTTON)
		{
			this.drawFrame(g);
			this.drawUpdateFrame(g);
			this.drawUpdateButton(g);
		}
		else if(state==WINDOW)this.drawWindow(g);
		else if(state==WINDOW_INSERT)this.drawWindow(g);
		else if(state==WINDOW_SELECT)this.drawWindow(g);
		else if(state==WINDOW_ALTER)this.drawWindow(g);
		else if(state==INSERT)
		{
			this.drawInsertFrame(g);
		}
		else if(state==SELECT)
		{
			this.drawSelectFrame(g);
		}
		else if(state==REMOTE)
		{
			this.drawRemote(g);
		}
		else if(state==ALTER)
		{
			this.drawAlterFrame(g);
		}
		else if(state==SELECT_LIST)
		{
			this.drawSelectFrame(g);
			this.drawSelectList(g);
		}
		else if(state==CLOSE_INSERT)
		{
			this.drawInsertFrame(g);
			g.drawImage(closeinsertframeImage,insertframeX+759,insertframeY+6,this);
		}
		else if(state==CLOSE_SELECT)
		{
			this.drawSelectFrame(g);
			g.drawImage(closeframeImage,selectframeX+854,selectframeY+5,this);
		}
		else if(state==CLOSE_ALTER)
		{
			this.drawAlterFrame(g);
			g.drawImage(closeframeImage,alterframeX+855,alterframeY+4,this);
		}
		else if(state==ON_INSERT_BUTTON)
		{
			this.drawInsertFrame(g);
			this.drawInsertButton(g);
		}
		else if(state==MESSAGE||state==ON_MESSAGE_BUTTON)
		{
			this.drawMessageBox(g);
		}
		else if(state==ON_EXIT_BUTTON)
		{
			g.drawImage(startImage,0,screenHeight-startImageHeight-10,this);
			this.drawMenu(g);
			this.drawExitButton(g);
		}
	}
	public void mouseClicked(MouseEvent e)
	{
		int state0=state;
		if(state==REGISTER)
		{
			if(isOnRegisterButton)
			{
				if(inputPassword.equals(password))this.state=BEGIN;
				else this.inputPassword="";
				this.repaint();
			}
		}
		else if(state==BEGIN)
		{
			this.fileIndex=this.getFileIndex();
			if(isOnShiningClassButton==1)
			{
				this.IDs=newIDs;
				this.names=newNames;
				this.isOnShiningClassButton=0;
				this.filePositionX=0;
				this.filePositionX0=0;
				this.repaint();
			}
			else if(fileIndex>=0)
			{
				this.itemTable=mySQLprocessor.executeQuery(IDs[fileIndex],getFieldNames(catalogFields),getFieldNames(attributeFields),tableName);
				this.state=FRAME;
			}
			else 
			{
				if(mouseY<600)
				{
					this.windowX=mouseX;
					this.windowY=mouseY;
					this.state=WINDOW;
				}
			}
		}
		else if(state==START)this.state=MENU;
		else if(state==ON_START_BUTTON)
		{
			if(this.getStartButton()==0)
			{
				this.send("begin\0");
				this.receiveRemoteInfo();
				this.remotePositionY=0;
				this.remotePositionY0=0;
				this.remoteFiles=remoteFileDir.list();
				this.state=REMOTE;
		
			}
		}
		else if(state==MENU)
		{
			if(!mouseIsInBounds(0,startImageHeight,409,690))this.state=BEGIN;
		}
		else if(state==FRAME)
		{
			if(catalogPosition>=0)this.state=ON_FRAME_BLUE_BUTTON;
		}
		else if(state==ON_FRAME_BLUE_BUTTON)
		{
			if(catalogPosition<0||choicePosition<0)return;
			this.updateItemForDatabase(false);
			this.catalogPosition=-1;
			this.choicePosition=-1;
			this.state=FRAME;
		}
		else if(state==ON_FRAME_BUTTON)
		{
			this.state=UPDATE_FRAME;
			this.showUpdateTextArea();
		}
		else if(state==CLOSE_FRAME)this.state=BEGIN;
		else if(state==CLOSE_UPDATE_FRAME)
		{
			this.state=FRAME;
			this.updateTextArea.setFocusable(false);
			this.updateTextArea.setVisible(false);
		}
		else if(state==ON_UPDATE_BUTTON)
		{
			this.updateItemForDatabase(true);
			this.state=FRAME;
			this.updateTextArea.setFocusable(false);
			this.updateTextArea.setVisible(false);
		}
		else if(state==WINDOW)
		{
			if(closeWindow())this.state=BEGIN;
		}
		else if(state==WINDOW_INSERT)
		{
			this.state=INSERT;
			this.setTextFieldsVisible(true);
		}
		else if(state==WINDOW_SELECT)
		{
			this.selectItemsFromDatabase();
			this.state=SELECT;
		}
		else if(state==WINDOW_ALTER)
		{
			this.attributeFieldNameTextField.setVisible(true);
			this.attributeFieldTypeTextField.setVisible(true);
			this.state=ALTER;
		}
		else if(state==SELECT_LIST)
		{
			this.selectItemsFromDatabase();
			this.state=SELECT;
		}
		else if(state==CLOSE_INSERT)
		{
			this.state=BEGIN;
			this.setTextFieldsVisible(false);
		}
		else if(state==SELECT)
		{
			if(mouseIsInBounds(selectframeX+81,selectframeY+38,674,23))
			{
				this.selectTextField.setText(selectText);
				this.selectTextField.setVisible(true);
			}
			else
			{
				this.selectText=selectTextField.getText();
				this.selectTextField.setVisible(false);
			}
			if(mouseIsInBounds(selectframeX+761,selectframeY+38,143,21))
			{
				this.state=SELECT_LIST;
			}
			else if(itemPosition>=0)
			{
				int C=attributeFields.length/2+1;
				int id=Integer.parseInt(items[itemPosition*C]);
				for(int i=0;i<IDs.length;i++)if(id==IDs[i]){this.fileIndex=i;break;}
				this.itemTable=mySQLprocessor.executeQuery(id,getFieldNames(catalogFields),getFieldNames(attributeFields),tableName);
				this.state=FRAME;
			}
			else if(catalogItemPosition>=0)
			{
				int l=this.catalogChoicesStateTable[catalogItemPosition].length;
				for(int j=0;j<l;j++)this.catalogChoicesStateTable[catalogItemPosition][j]=false;
				if(choiceItemPosition>=0)this.catalogChoicesStateTable[catalogItemPosition][choiceItemPosition]=true;
				else this.catalogStateTable[catalogItemPosition]=!catalogStateTable[catalogItemPosition];
				this.selectItemsFromDatabase();
				this.repaint();
			}
		}
		else if(state==REMOTE)
		{
			int x=selectframeX+854,y=selectframeY+5,w=56,h=21;
			int x0=selectframeX+12,y0=selectframeY+34,w0=30,h0=30;
			if(mouseIsInBounds(x,y,w,h)){this.state=BEGIN;this.send("end\0");}
			else if(mouseIsInBounds(x0,y0,w0,h0))
			{
				this.remotePositionY=0;
				this.remotePositionY0=0;
				this.send("cmd\0cd\0..\0");
				this.receiveRemoteInfo();
				this.repaint();
			}
			else if(remotePosition>=0)
			{
				this.remotePositionY=0;
				this.remotePositionY0=0;
				this.updateRemoteInfo();
				this.repaint();
			}
			else if(remoteFilePosition>=0)
			{
				try
				{
					Runtime Runtime1=Runtime.getRuntime();
					String fileName=remoteFiles[remoteFilePosition];
					int fileType=this.getFileType(this.getPostfixName(fileName));
					if(fileType==FILE_TXT)Runtime1.exec("notepad file/"+fileName);
					else if(fileType==FILE_IMG)Runtime1.exec("java ImageBox file/"+fileName);
					else if(fileType==FILE_AUD)Runtime1.exec("java AudioPlayer file/"+fileName);
				}
				catch(Exception ex){ex.printStackTrace();}
			}
		}
		else if(state==ALTER)
		{
			if(currentAttributePosition>=0)
			{
				this.alterTableChangeAttributeField();
				this.attributeTextField.setFocusable(false);
				this.attributeTextField.setVisible(false);
				this.currentAttributePosition=-1;
				this.repaint();
			}
			if(newCatalogPosition>=0)
			{
				this.alterTableChangeCatalogField();
				this.catalogTextField.setFocusable(false);
				this.catalogTextField.setVisible(false);
				this.catalogContentTextField.setFocusable(false);
				this.catalogContentTextField.setVisible(false);
				this.newCatalogPosition=-1;
				this.repaint();
			}
			if(attributePosition>=0)
			{
				int x0=alterframeX+130,y0=alterframeY+480,x=x0,dx=150;
				this.attributeTextField.setText(attributeFields[attributePosition*2+0]);
				this.attributeTextField.setBounds(x0+(attributePosition-beginAttributePosition)*dx-4,y0+77,80,20);
				this.attributeTextField.setFocusable(true);
				this.attributeTextField.setVisible(true);
				this.currentAttributePosition=attributePosition;
			}
			if(currentCatalogPosition>=0)
			{
				int x0=alterframeX+130,y0=alterframeY+80,x=x0,dx=150;
				this.catalogTextField.setText(catalogFields[currentCatalogPosition*2+0]);
				this.catalogTextField.setBounds(x0+(currentCatalogPosition-beginCatalogPosition)*dx-5,y0+78,82,20);
				this.catalogTextField.setFocusable(true);
				this.catalogTextField.setVisible(true);
				this.catalogContentTextField.setText(catalogFields[currentCatalogPosition*2+1]);
				this.catalogContentTextField.setBounds(x0+(currentCatalogPosition-beginCatalogPosition)*dx-5,y0+99,82,16);
				this.catalogContentTextField.setFocusable(true);
				this.catalogContentTextField.setVisible(true);
				this.newCatalogPosition=currentCatalogPosition;
			}
			if(isOnCrystalButton==1)
			{
				this.alterTableAddAttributeField();
				this.repaint();
			}
		}
		else if(state==CLOSE_SELECT)
		{
			this.state=BEGIN;
			this.selectTextField.setVisible(false);
		}
		else if(state==CLOSE_ALTER)
		{
			this.state=BEGIN;
			this.attributeFieldNameTextField.setVisible(false);
			this.attributeFieldTypeTextField.setVisible(false);
		}
		else if(state==ON_INSERT_BUTTON)
		{
			this.insertItemIntoDatabase();
			this.setTextFieldsVisible(false);
			this.state=MESSAGE;
		}
		else if(state==ON_MESSAGE_BUTTON)
		{
			this.state=BEGIN;
		}
		else if(state==ON_EXIT_BUTTON)
		{
			System.exit(0);
		}
		if(state!=state0)this.repaint();
	}
	public void mouseMoved(MouseEvent e)
	{
		int state0=state;
		this.mouseX=e.getX();
		this.mouseY=e.getY();
		if(state==REGISTER)
		{
			boolean isOnButton=isOnRegisterButton;
			if(mouseIsInBounds(625,500,177,38))this.isOnRegisterButton=true;
			else this.isOnRegisterButton=false;
			if(isOnRegisterButton!=isOnButton)this.repaint();
		}
		else if(state==BEGIN)
		{
			if(mouseX<startImageHeight&&mouseY>screenHeight-startImageHeight)this.state=START;
			else 
			{
				this.positionFile();
				this.positionClass();
			}
		}
		else if(state==START)
		{
			if(!(mouseX<startImageHeight&&mouseY>screenHeight-startImageHeight))this.state=BEGIN;
		}
		else if(state==MENU)
		{
			if(onExitButton())this.state=ON_EXIT_BUTTON;
			else if((this.getStartButton())!=-1)this.state=ON_START_BUTTON;
		}
		else if(state==ON_EXIT_BUTTON)
		{
			if(!onExitButton())this.state=MENU;
		}
		else if(state==ON_START_BUTTON)
		{
			if((this.getStartButton())==-1)this.state=MENU;
		}
		else if(state==FRAME||state==ON_FRAME_BLUE_BUTTON)
		{
			if(closeFrame())this.state=CLOSE_FRAME;
			else if((this.getFrameButton())!=-1)this.state=ON_FRAME_BUTTON;
			else 
			{
				this.positionCatalog();
				if(catalogPosition<0)this.state=FRAME;
			}
		}
		else if(state==CLOSE_FRAME)
		{
			if(!closeFrame())this.state=FRAME;
		}
		else if(state==ON_FRAME_BUTTON)
		{
			if((this.getFrameButton())==-1)this.state=FRAME;
		}
		else if(state==UPDATE_FRAME)
		{
			if(closeUpdateFrame())this.state=CLOSE_UPDATE_FRAME;
			else if(onUpdateButton())this.state=ON_UPDATE_BUTTON;
		}
		else if(state==CLOSE_UPDATE_FRAME)
		{
			if(!closeUpdateFrame())this.state=UPDATE_FRAME;
		}
		else if(state==ON_UPDATE_BUTTON)
		{
			if(!onUpdateButton())this.state=UPDATE_FRAME;
		}
		else if(state==WINDOW)
		{
			int x=windowX+10,y=windowY+70,w=380,h=66;
			if(mouseIsInBounds(x,y,w,h))this.state=WINDOW_INSERT;
			else if(mouseIsInBounds(x,y+h,w,h))this.state=WINDOW_SELECT;
			else if(mouseIsInBounds(x,y+2*h,w,h))this.state=WINDOW_ALTER;
		}
		else if(state==WINDOW_INSERT)
		{
			int x=windowX+10,y=windowY+70,w=380,h=66;
			if(!mouseIsInBounds(x,y,w,h))this.state=WINDOW;
		}
		else if(state==WINDOW_SELECT)
		{
			int x=windowX+10,y=windowY+70,w=380,h=66;
			if(!mouseIsInBounds(x,y+h,w,h))this.state=WINDOW;
		}
		else if(state==WINDOW_ALTER)
		{
			int x=windowX+10,y=windowY+70,w=380,h=66;
			if(!mouseIsInBounds(x,y+2*h,w,h))this.state=WINDOW;
		}
		else if(state==INSERT)
		{
			if(closeInsertFrame())this.state=CLOSE_INSERT;
			else if(onInsertButton())this.state=ON_INSERT_BUTTON;
		}
		else if(state==CLOSE_INSERT)
		{
			if(!closeInsertFrame())this.state=INSERT;
		}
		else if(state==ON_INSERT_BUTTON)
		{
			 if(!onInsertButton())this.state=INSERT;
		}
		else if(state==SELECT)
		{
			if(closeSelectFrame())this.state=CLOSE_SELECT;
			else
			{
				this.positionItem();
				this.positionCatalogItem();
			}
		}
		else if(state==REMOTE)
		{
			this.positionRemote();
			this.positionRemoteFile();
		}
		else if(state==CLOSE_SELECT)
		{
			if(!closeSelectFrame())this.state=SELECT;
		}
		else if(state==SELECT_LIST)
		{
			this.positionList();
		}
		else if(state==ALTER)
		{

			if(mouseIsInBounds(alterframeX+372,alterframeY+229,166,58))this.attributeFieldNameTextField.setFocusable(true);
			else this.attributeFieldNameTextField.setFocusable(false);
			if(mouseIsInBounds(alterframeX+372,alterframeY+286,166,58))this.attributeFieldTypeTextField.setFocusable(true);
			else this.attributeFieldTypeTextField.setFocusable(false);
			if(closeAlterFrame())this.state=CLOSE_ALTER;
			else 
			{
				this.checkCrystalButton();
				this.positionAttributeField();
				this.positionCatalogField();
			}
		}
		else if(state==CLOSE_ALTER)
		{
			if(!closeAlterFrame())this.state=ALTER;
		}
		else if(state==MESSAGE)
		{
			 if(onMessageButton())this.state=ON_MESSAGE_BUTTON;
		}
		else if(state==ON_MESSAGE_BUTTON)
		{
			if(!onMessageButton())this.state=MESSAGE;
		}
		else this.state=BEGIN;
		if(state!=state0)this.repaint();
	}
	public void mousePressed(MouseEvent e)
	{
		this.mouseX0=e.getX();
		this.mouseY0=e.getY();
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseDragged(MouseEvent e)
	{
		
		if(state==BEGIN)
		{
			int mouseX1=e.getX();
			this.filePositionX=filePositionX0+mouseX1-mouseX0;
			if(filePositionX>0)this.filePositionX=0;
			this.repaint();
		}
		else if(state==SELECT)
		{
			if(mouseX>selectframeX&&mouseX<selectframeX+185)
			{
				int mouseY1=e.getY();
				this.catalogItemPositionY=catalogItemPositionY0+mouseY1-mouseY0;
				if(catalogItemPositionY>0)this.catalogItemPositionY=0;
				this.repaint();
			}
			else if(mouseX>selectframeX+185&&mouseX<selectframeX+908)
			{
				int mouseY1=e.getY();
				this.itemPositionY=itemPositionY0+mouseY1-mouseY0;
				if(itemPositionY>0)this.itemPositionY=0;
				int C=attributeFields.length/2+1,L=419/36,l=items.length/C;
				if(l<=L)this.itemPositionY=0;
				else
				{
					int min_dY=-(l-L)*36;
					if(itemPositionY<min_dY)this.itemPositionY=min_dY;
				}
				this.repaint();
			}
		}
		else if(state==REMOTE)
		{
			if(mouseX>selectframeX&&mouseX<selectframeX+185)
			{
				int mouseY1=e.getY();
				this.remoteFilePositionY=remoteFilePositionY0+mouseY1-mouseY0;
				if(remoteFilePositionY>0)this.remoteFilePositionY=0;
				this.repaint();
			}
			if(mouseX>selectframeX+185&&mouseX<selectframeX+908)
			{
				int mouseY1=e.getY();
				this.remotePositionY=remotePositionY0+mouseY1-mouseY0;
				if(remotePositionY>0)this.remotePositionY=0;
				int L=419/36,l=remoteInfo.length/2;
				if(l<=L)this.remotePositionY=0;
				else
				{
					int min_dY=-(l-L)*36;
					if(remotePositionY<min_dY)this.remotePositionY=min_dY;
				}
				this.repaint();
			}
		}
	}
	public void mouseReleased(MouseEvent e)
	{
		if(state==BEGIN)
		{
			this.filePositionX0=filePositionX;
		}
		else if(state==SELECT)
		{
			this.catalogItemPositionY0=catalogItemPositionY;
			this.itemPositionY0=itemPositionY;
		}
		else if(state==REMOTE)
		{
			//this.catalogItemPositionY0=catalogItemPositionY;
			this.remotePositionY0=remotePositionY;
		}
	}
	public void keyReleased(KeyEvent e)
	{
		int code=e.getKeyCode();
		String t=KeyEvent.getKeyText(e.getKeyCode());
		int state0=state;
		if(state==REGISTER)
		{
			if(t.equals("Backspace"))
			{
				int length=inputPassword.length();
				if(length>0)this.inputPassword=inputPassword.substring(0,length-1);
			}
			else if(t.equals("Enter"))
			{
				if(inputPassword.equals(password))this.state=BEGIN;
				else inputPassword="";
			}
			else if(t.length()==1)
			{
				char c=e.getKeyChar();
				this.inputPassword+=c;
			}
			this.repaint();
		}
		else if(state==BEGIN)
		{
			if(t.equals("Home"))
			{
				this.filePositionX=0;
				this.filePositionX0=0;
				this.repaint();
			}
			else if(t.equals("Delete"))
			{
				int index=this.getFileIndex();
				if(index>=0)
				{
					this.deleteAndRecycle(IDs[index]);
					this.state=MESSAGE;
				}
			}
		}
		else if(state==FRAME)
		{
			if(t.equals("Home"))
			{
				this.beginDiskLine=0;
				this.repaint();
			}
			else if(t.equals("Up"))
			{	
				if(beginDiskLine>0)this.beginDiskLine--;
				this.repaint();
			}
			else if(t.equals("Down"))
			{
				int C=attributeFields.length/2;
				int L=C/3;
				if(C%3!=0)L++;
				if(beginDiskLine<L-3)this.beginDiskLine++;
				this.repaint();
			}
			else if(t.equals("Page Up"))
			{
				if(beginCatalogLine<0)this.beginCatalogLine++;
				this.repaint();
			}
			else if(t.equals("Page Down"))
			{	
				this.beginCatalogLine--;
				this.repaint();
			}


		}
		else if(state==SELECT)
		{
			if(t.equals("Home"))
			{
				this.itemPositionY=0;
				this.itemPositionY0=0;
				this.repaint();
			}
			else if(t.equals("Delete"))
			{
				if(itemPosition>=0)
				{
					int C=attributeFields.length/2+1;
					int id=Integer.parseInt(items[itemPosition*C]);
					this.deleteAndRecycle(id);
					this.state=MESSAGE;
				}
			}
			else if(t.equals("Left"))
			{	
				if(beginAttributeField>0)this.beginAttributeField--;
				this.repaint();
			}
			else if(t.equals("Right"))
			{
				int C=attributeFields.length/2;
				if(beginAttributeField<C-6)this.beginAttributeField++;
				this.repaint();
			}
		}
		else if(state==ALTER)
		{
			if(t.equals("Home"))
			{
				this.beginAttributePosition=0;
				this.beginCatalogPosition=0;
				this.repaint();
			}
			else if(t.equals("End"))
			{
				int C=attributeFields.length/2;
				if(beginAttributePosition<C-5)this.beginAttributePosition=C-5;
				C=catalogFields.length/2;
				if(beginCatalogPosition<C-5)this.beginCatalogPosition=C-5;
				this.repaint();
			}
			else if(t.equals("Delete"))
			{
				if(attributePosition>=0)
				{
					this.alterTableDropAttributeField();
					this.repaint();
				}
				else if(currentCatalogPosition>=0)
				{
					this.alterTableDropCatalogField();
					this.repaint();
				}
			}
			else if(t.equals("Left"))
			{	
				if(beginAttributePosition>0)this.beginAttributePosition--;
				this.repaint();
			}
			else if(t.equals("Right"))
			{
				int C=attributeFields.length/2;
				if(beginAttributePosition<C-5)this.beginAttributePosition++;
				this.repaint();
			}
		}
		else if(state==REMOTE)
		{
			if(t.equals("Delete")&&remoteFilePosition>=0)
			{
				try
				{
					String fileName=remoteFiles[remoteFilePosition];
					File file=new File("file/"+fileName);
					file.delete();
					this.remoteFiles=remoteFileDir.list();
					this.repaint();
				}
				catch(Exception ex){ex.printStackTrace();}
			}
			else if(t.equals("Enter")&&remotePosition>=0)
			{
				String fileName=remoteInfo[remotePosition*2+0].replaceAll("\0","");
				System.out.println("open\0"+fileName+"\0");
				this.send("open\0"+fileName+"\0");
			}
		}
		if(state!=state0)this.repaint();
	}
	public void keyPressed(KeyEvent e){}
	public void  keyTyped(KeyEvent e){}
	public void textValueChanged(TextEvent e)
	{
		this.selectItemsFromDatabase();
	}
	public void run()
	{
		this.receiveRemoteFile(remoteFileName);
	}
	private void deleteAndRecycle(int id)
	{
		String item=id+";";
		this.itemTable=mySQLprocessor.executeQuery(id,getFieldNames(catalogFields),getFieldNames(attributeFields),tableName);
		for(int i=0;i<itemTable.length;i++)item+=itemTable[i]+";";
		String fileName="database\\recycle.table",file="";
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName));
			String s=BufferedReader1.readLine();
			while(s!=null)
			{
				file+=s+"\r\n";
				s=BufferedReader1.readLine();
			}
			BufferedReader1.close();
			PrintWriter PrintWriter1=new PrintWriter(fileName);
			PrintWriter1.print(file);
			PrintWriter1.println(item);
			PrintWriter1.close();
		}
		catch(Exception ex){System.err.println(ex);}
		this.mySQLprocessor.executeUpdate("Delete From "+tableName+" Where ID="+id);
		this.updateIDsAndNames();
	}
	private void drawRegisterFrame(Graphics g)
	{
		g.drawImage(registerbackgroundImage,0,0,this);
		g.drawImage(symbolImage,630,250,this);
		g.setFont(middleFont);
		String title="DesktopDatabase";
		this.drawTitle(g,title,608,408);
		int x=575,y=445,w=220,h=20;
		this.drawPasswordTextField(g,x,y,w,h);
		if(isOnRegisterButton)g.drawImage(registerbutton_shineImage,625,500,this);
		else g.drawImage(registerbuttonImage,625,500,this);
		this.drawTitle(g,"Enter",653,520);
	}
	private void drawTitle(Graphics g,String title,int x,int y)
	{
		g.setColor(Color.gray);
		g.drawString(title,x+2,y+2);
		g.setColor(Color.white);
		g.drawString(title,x,y);
	}
	private void drawPasswordTextField(Graphics g,int x,int y,int w,int h)
	{
		g.setColor(Color.white);
		g.fillRect(x,y,w,h);
		g.setColor(Color.black);
		g.drawRect(x,y,w,h);
		int dx=h/3,dy=(h-dx)/2;
		for(int i=0;i<inputPassword.length();i++)
		{
			g.fillOval(x+(i+1)*dx,y+dy,dx,dx);
		}
	}
	private void drawMenu(Graphics g)
	{
		g.drawImage(startImage,0,screenHeight-startImageHeight-10,this);
		g.drawImage(menuImage,0,startImageHeight,this);
		g.setColor(Color.white);
		g.setFont(smallFont);
		g.drawString("Exit",290,702);
		int x=30,y=70,h=150,i=0,dx=40,dy=20;
		if(state==ON_START_BUTTON)g.drawImage(startbuttonImage,11,y+h*this.getStartButton(),this);
		g.setColor(Color_darkblue);
		g.setFont(middleFont);
		g.drawImage(computerImage,x,y+h*(i++),this);
		g.drawString("Computer",x+dx-10,y+h*i);
		g.drawImage(notepadImage,x,y+h*(i++),this);
		g.drawString("Notepad",x+dx,y+h*i-dy);
		g.drawImage(pictureImage,x,y+h*(i++),this);
		g.drawString("Picture",x+dx,y+h*i-dy);
		g.drawImage(recycle_emptyImage,x,y+h*(i++),this);
		g.drawString("Recycle",x+dx,y+h*i-dy);
	}
	private int getStartButton()
	{
		if(!mouseIsInBounds(11,75,246,600))return -1;
		int k=(mouseY-75)/150;
		int h=(mouseY-75)%150;
		if(h>140||h<10)return -1;
		return k;
	}
	private void positionFile()
	{
		int i0=this.filePosition_I;
		int j0=this.filePosition_J;
		this.filePosition_I=(mouseY-dY)/L;
		this.filePosition_J=(mouseX-dX-filePositionX)/L;
		if(filePosition_I>2)this.filePosition_I=-1;
		if(filePosition_J>filePosition_maxJ)this.filePosition_J=-1;
		if((mouseY-dY)%L>2*L/3||(mouseX-dX-filePositionX)%L>L/2)this.filePosition_I=this.filePosition_J=-1;
		if(i0!=filePosition_I||j0!=filePosition_J)this.repaint();
	}
	private int getFileIndex()
	{
		if(filePosition_I==-1||filePosition_J==-1)return -1;
		int index=filePosition_J*3+filePosition_I;
		if(index<IDs.length)return index;
		else return -1;
	}
	private boolean closeFrame()
	{
		int x=frameX+824;
		int y=frameY;
		int w=46;
		int h=20;
		if(mouseIsInBounds(x,y,w,h))return true;
		return false;
	}
	private boolean closeInsertFrame()
	{
		int x=insertframeX+759;
		int y=insertframeY;
		int w=47;
		int h=20;
		if(mouseIsInBounds(x,y,w,h))return true;
		return false;
	}
	private boolean closeSelectFrame()
	{
		int x=selectframeX+854;
		int y=selectframeY+5;
		int w=56;
		int h=21;
		if(mouseIsInBounds(x,y,w,h))return true;
		return false;
	}
	private boolean closeAlterFrame()
	{
		int x=alterframeX+854;
		int y=alterframeY+5;
		int w=56;
		int h=21;
		if(mouseIsInBounds(x,y,w,h))return true;
		return false;
	}
	private boolean closeWindow()
	{
		int x=windowX;
		int y=windowY;
		int w=417;
		int h=465;
		if(mouseIsInBounds(x,y,w,h))return false;
		return true;
	}
	private void drawFiles(Graphics g)
	{
		int L=200;
		int l=this.IDs.length;
		int r=3;
		int c=l/r;
		int k=0;
		int j=0;
		while(k<l)
		{
			for(int i=0;i<3;i++)
			{
				int x=j*L+dX+filePositionX,y=i*L+dY;
				if(i==filePosition_I&&j==filePosition_J)
				{
					g.setColor(Color_transparentblue);
					g.fillRect(x,y,93,154);
					g.setColor(Color_transparentwhite);
					g.drawRect(x,y,93,154);
				}
				g.drawImage(fileImage,x,y,this);
				g.setColor(Color.white);
				g.setFont(smallFont);
				g.drawString(this.getStringLines(""+names[k++],6)[0],x+28,y+143);
				if(k>=l)
				{
					this.filePosition_maxJ=j;
					return;
				}
			}
			j++;
		}
	}
	private void drawSmallFiles(Graphics g,int dx,int dy,boolean is3D)
	{
		int L=30;
		if(is3D)L=120;
		int l=this.newIDs.length,k=0,j=0;
		g.setColor(Color.white);
		g.setFont(littleFont);
		while(k<l)
		{
			for(int i=0;i<3;i++)
			{
				int x=j*L+dx,y=i*L+dy;
				if(is3D)
				{
					g.drawImage(file3DImage,x+i*2*(2-j)-j*50,y-(i-1)*j*4-j*3,this);
					g.drawString(""+newNames[k],5+x+i*2*(2-j)-j*50,75+y-(i-1)*j*4-j*3);
				}
				else g.drawImage(smallfileImage,x,y,this);
				k++;
				if(k>=l)return;
			}
			j++;
			if(j>5)return;
		}
	}
	private void drawClassButtons(Graphics g)
	{
		int x=100,y=727,w=62;
		int l=classFieldStrings.length;
		for(int i=0;i<l;i++)
		{
			if(i==classPosition&&isOnShiningClassButton==1)
			{
				g.drawImage(shiningclassbuttonImage,x,y,this);
				g.setColor(Color_transparentblue);
				int width=230,height=163,dx=(width-w)/2;
				g.fillRect(x-dx,y-height-3,width,height);
				g.setColor(Color_transparentwhite);
				g.drawRect(x-dx,y-height-3,width,height);
				g.drawRect(x-dx+10,y-height+25,width-25,height-40);
				g.drawImage(smallbackgroundImage,x-dx+15,y-height+32,this);
				this.drawSmallFiles(g,x-dx+23,y-height+45,false);
				g.drawImage(smallfileiconImage,x-dx+10,y-height+6,this);
				g.setFont(smallFont);
				g.setColor(Color.white);
				g.drawString(classFieldStrings[i],x-dx+34,y-height+18);
			}
			else g.drawImage(classbuttonImage,x,y,this);
			x+=w;
		}
		if(classPosition==l)g.drawImage(desktopshiningbuttonImage,screenWidth-15,y-2,this);
		else g.drawImage(desktopbuttonImage,screenWidth-15,y-2,this);
	}
	private void drawBackground3D(Graphics g)
	{
		int w=62,x=100+classPosition*w,y=727,width=236,height=169,dx=(width-w)/2,dy;
		if(mouseIsInBounds(x-dx,y-height-3,width,height))
		{
			g.setColor(Color_transparentblack);
			g.fillRect(0,0,screenWidth,screenHeight-40);
			x=20;y=40;dx=65;dy=10;
			for(int i=0;i<7;i++)
			{
				x+=dx;y+=i*dy;
				g.drawImage(background3DImage,x,y,this);
			}
		}
		this.drawSmallFiles(g,x+50,y+40,true);
	}
	private void drawFrame(Graphics g)
	{
		g.drawImage(frameImage,frameX,frameY,this);
		g.setColor(Color.black);
		g.setFont(smallFont);
		int index=filePosition_J*3+filePosition_I;
		int x0=frameX+20,x1=x0+20,y0=frameY+120,y1=frameY+580,y=y0+beginCatalogLine*40;
		int l=catalogFields.length/2;
		for(int i=0;i<l;i++)
		{
			String fieldName=catalogFields[i*2+0];
			String dataType=catalogFields[i*2+1];
			if(y>=y0)
			{
				if(i==classField)g.drawImage(intImage,x0,y,this);
				else g.drawImage(textImage,x0,y,this);
				g.drawString(fieldName,x0+50,y+25);
			}
			y+=40;
			if(y>y1)break;
			if(y>=y0)
			{
				if(choicePosition<0&&i==catalogPosition)g.drawImage(bluebuttonImage,frameX+15,y,this);
				g.drawImage(attributeImage,x1,y+2,this);
				g.drawString(""+itemTable[i],x0+50,y+20);
			}
			y+=40;
			if(y>y1)break;
			if(state==ON_FRAME_BLUE_BUTTON&&i==catalogPosition)
			{
				String[] choiceStrings=this.catalogChoicesTable[i];
				for(int j=0;j<choiceStrings.length;j++)
				{
					if(y>=y0)
					{
						if(j==choicePosition)g.drawImage(bluebuttonImage,frameX+15,y,this);
						g.drawImage(smallfileiconImage,x1+20,y+6,this);
						g.drawString(choiceStrings[j],x1+50,y+20);
					}
					y+=40;
					if(y>y1)break;
				}
			}
		}
		int l1=attributeFields.length/2;
		y=frameY+130;
		int w=190,h=150,k=beginDiskLine*3,k0=this.getFrameButton(),length=15;
		while(k<l1)
		{
			for(int j=0;j<3;j++)
			{
				int x=j*w+frameX+280;
				if(k==k0)g.drawImage(framebuttonImage,x-5,y,this);
				g.drawImage(diskImage,x,y,this);
				String s=attributeFields[2*k]+":"+itemTable[l+(k++)];
				String[] strings=this.getStringLines(s,15);
				int sx=x+30,sy=y+110;
				for(int i=0;i<strings.length&&i<2;i++)
				{
					g.drawString(strings[i],sx,sy);
					sy+=20;
				}
				if(k>=l1)return;
			}
			y+=h;
			if(y>frameY+568)return;
		}
	}
	private void send(String s)
	{
		try
		{
System.out.println(s);
			this.socket=new Socket(IP,port);
			PrintWriter PrintWriter1=new PrintWriter(socket.getOutputStream(),true);
			PrintWriter1.println(s);
			this.socket.shutdownOutput();
		}
		catch(Exception e){System.err.println(e);}
	}
	private void updateRemoteInfo()
	{
		String s="";
		String name=remoteInfo[remotePosition*2+0];
		String type=remoteInfo[remotePosition*2+1];
		boolean receiveFile=false;
		name=name.replaceAll("\0","");
		if(type.equals("DISK"))s="cmd\0"+name;
		else if(type.equals("DIR"))s="cmd\0cd\0"+name;
		else
		{
			s="cp\0"+name;
			receiveFile=true;
			this.fileLength=Integer.parseInt(type)/1024;
		}
		this.send(s+"\0");
		if(receiveFile)
		{
			this.remoteFileName=name;
			Thread thread=new Thread(this);
			thread.start();
		}
		else this.receiveRemoteInfo();
	}
	private void receiveRemoteInfo()
	{
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String t="";
			String s=BufferedReader1.readLine();
			while(s!=null)
			{
				t+=s+";";
				s=BufferedReader1.readLine();
			}
			StringQueue q=new StringQueue(t);
			this.remoteInfo=q.getStrings();
			this.repaint();
		}
		catch(Exception e){System.err.println(e);this.remoteInfo=new String[0];this.repaint();}
	}
	private void receiveRemoteFile(String name)
	{
		//this.timer.start();
		try
		{

			this.progressBarLength=0;
     			InputStream InputStream1=socket.getInputStream();
     			FileOutputStream FileOutputStream1=new FileOutputStream("file/"+name);
			byte[] bytes=new byte[1024];			
			int n=InputStream1.read(bytes);
			while(n!=-1)
			{
				FileOutputStream1.write(bytes,0,n);
				n=InputStream1.read(bytes);
				this.progressBarLength++;
this.repaint();
			}
			this.progressBarLength=0;
this.repaint();
			InputStream1.close();
			FileOutputStream1.close();
		}
		catch(Exception e){e.printStackTrace();}
	//	this.timer.stop();
		this.remoteFiles=remoteFileDir.list();
	}
	private void drawRemote(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(selectframeX+5,selectframeY+100,188,500);
		this.drawRemoteFiles(g);
		g.setColor(Color.white);
		g.fillRect(selectframeX+180,selectframeY+100,730,500);
		int x0=selectframeX+219-33,y0=selectframeY+160-33,x=x0,y=y0,dx=10,dy=15;
		int l=remoteInfo.length/2;
		g.setColor(Color_lightgray);
		g.setFont(smallFont);
		y=y0+25+remotePositionY;
		for(int i=0;i<l;i++)
		{
			x=x0;
			if(y<y0-100){y+=35;continue;}
			if(y>y0+450)break;
			if(i==remotePosition)g.drawImage(selectbuttonImage,x,y+6,this);
			String infoName=remoteInfo[i*2+0];
			String infoType=remoteInfo[i*2+1];
			if(infoType.equals("DIR"))g.drawImage(itemsImages[0],x,y,this);
			else if(infoType.equals("FILE"))g.drawImage(itemsImages[2],x,y,this);
			else if(infoType.equals("DISK"))g.drawImage(itemsImages[4],x,y,this);
			else
			{
				g.drawImage(itemsImages[3],x,y,this);
				int fileSize=Integer.parseInt(infoType)/1024;
				infoType=fileSize+"KB";
			}
			g.drawString(infoName,x+dx+25,y+30);
			g.drawString(infoType,x+dx+593,y+30);
			y+=35;
		}
		y=y0;
		x=x0;
		g.drawImage(backfieldImage,x-5,y,this);
		g.drawImage(bigfieldImage,x,y,this);
		g.drawString("Name",x+dx,y+dy);
		x+=593;
		g.drawImage(smallfieldImage,x,y,this);
		g.drawString("Type",x+dx,y+dy);
		g.drawImage(selectframeImage,selectframeX,selectframeY,this);
		if(progressBarLength!=0&&fileLength!=0)this.drawProgressBar(g);
	}
	private void drawProgressBar(Graphics g)
	{
		int x0=selectframeX+82,y0=selectframeY+41;
		int h0=5,h1=13,w=(int)(644.0*progressBarLength/fileLength);
System.out.println("w="+w);
		g.setColor(Color_whitegreen);
		g.fillRect(x0,y0,w,h0);
		g.setColor(Color_lightgreen);
		g.fillRect(x0,y0+h0,w,h1);
	}
	private void drawRemoteFiles(Graphics g)
	{
		int l=remoteFiles.length;
		int x0=selectframeX+33,y0=selectframeY+140,x=x0,y=y0,dx=10,dy=15,minY=y0-100,maxY=y0+450;
		g.setColor(Color_lightgray);
		g.setFont(smallFont);
		y=y0+remoteFilePositionY;
		for(int i=0;i<l;i++)
		{
			if(y>minY)
			{
				String fileName=this.getStringLines(remoteFiles[i],15)[0];
				String postName=this.getPostfixName(fileName);
				int fileType=this.getFileType(postName);
				if(i==remoteFilePosition)g.drawImage(catalogitembuttonImage,x-20,y-5,this);
				g.drawString(fileName,x+6,y+14);
				g.drawImage(catalogitemsImages[fileType],x-16,y,this);
			}
			y+=25;
			if(y>maxY)return;
		}
	}
	private String getPostfixName(String fileName)
	{
		int l=fileName.length();
		String p="";
		for(int i=l-1;i>0;i--)
		{
			char c=fileName.charAt(i);
			if(c=='.')break;
			p=c+p;
		}
		return p;
	}
	private int getFileType(String postfix)
	{
		postfix=postfix.toLowerCase();
		if(postfix.equals("txt"))return FILE_TXT;
		else if(postfix.equals("java"))return FILE_TXT;
		else if(postfix.equals("c"))return FILE_TXT;
		else if(postfix.equals("cpp"))return FILE_TXT;
		else if(postfix.equals("cs"))return FILE_TXT;
		else if(postfix.equals("jpg"))return FILE_IMG;
		else if(postfix.equals("gif"))return FILE_IMG;
		else if(postfix.equals("bmp"))return FILE_IMG;
		else if(postfix.equals("png"))return FILE_IMG;
		else if(postfix.equals("jpg"))return FILE_IMG;
		else if(postfix.equals("jpg"))return FILE_IMG;
		else if(postfix.equals("mp3"))return FILE_AUD;
		else if(postfix.equals("wav"))return FILE_AUD;
		else if(postfix.equals("wma"))return FILE_AUD;
		else return 1;
	}
	private void positionCatalog()
	{
		int i0=this.catalogPosition;
		int j0=this.choicePosition;
		int x0=frameX,y0=frameY+120+beginCatalogLine*40;
		if(mouseX<x0)this.catalogPosition=-1;
		else if(mouseY<y0)this.catalogPosition=-1;
		else if(mouseX>frameX+190)this.catalogPosition=-1;
		else if(state==ON_FRAME_BLUE_BUTTON&&i0!=-1)
		{
			int k=(mouseY-y0)/40;
			k-=(i0+1)*2;
			int choiceStringsLength=this.catalogChoicesTable[i0].length;
			if(k>=-1&&k<choiceStringsLength)this.choicePosition=k;
			else
			{
				this.choicePosition=-1;
				k-=choiceStringsLength;
				if(k%2==0)this.catalogPosition=-1;
				else
				{
					k/=2;
					int C=catalogFields.length/2;
					if(k+i0<C)this.catalogPosition=k+i0;
					else this.catalogPosition=-1;
				}

			}
		}
		else 
		{
			int k=(mouseY-y0)/40;
			if(k%2==0)this.catalogPosition=-1;
			else
			{
				k/=2;
				int C=catalogFields.length/2;
				if(k<C)this.catalogPosition=k;
				else this.catalogPosition=-1;
			}
		}
		if(i0!=catalogPosition||j0!=choicePosition)this.repaint();
	}
	private void showUpdateTextArea()
	{
		int l=catalogFields.length/2;
		int k=this.getFrameButton();
		String s=itemTable[l+k];
		this.attributeFieldName=attributeFields[k*2];
		this.attributeFieldType=attributeFields[k*2+1];
		this.updateTextArea.setText(s);
		this.updateTextArea.setFocusable(true);
		this.updateTextArea.setVisible(true);
	}
	private void drawUpdateFrame(Graphics g)
	{
		g.drawImage(updateframeImage,updateframeX,updateframeY,this);
		g.setColor(Color.black);
		g.setFont(smallFont);
		g.drawString("Field Name is:"+attributeFieldName,updateframeX+96,updateframeY+48);
		g.drawString("Field Type is:"+attributeFieldType,updateframeX+657,updateframeY+48);
	}
	private void updateItemForDatabase(boolean isAttribute)
	{
		if(isAttribute)this.mySQLprocessor.executeUpdate(attributeFieldName,attributeFieldType,updateTextArea.getText(),tableName,IDs[fileIndex]);
		else 
		{
			String catalogFieldName=catalogFields[catalogPosition*2+0];
			String[] choiceStrings=this.getChoiceStrings(catalogFields[catalogPosition*2+1]);
			this.mySQLprocessor.executeUpdate(catalogFieldName,"text",choiceStrings[choicePosition],tableName,IDs[fileIndex]);
		}
		this.itemTable=mySQLprocessor.executeQuery(IDs[fileIndex],getFieldNames(catalogFields),getFieldNames(attributeFields),tableName);
		this.updateIDsAndNames();
	}
	private int getFrameButton()
	{
		int y0=frameY+130;
		int x0=frameX+280;
		if(!mouseIsInBounds(x0,y0,885-x0,652-y0))return -1;
		int w=190,h=150;
		int j=(mouseX-x0)/w;
		int m=(mouseX-x0)%w;
		if(m<20||m>170)return -1;
		int i=(mouseY-y0)/h;
		m=(mouseY-y0)%h;
		if(m<20||m>130)return -1;
		int k=(i+beginDiskLine)*3+j;
		if(k>=attributeFields.length/2)return -1;
		return k;
	}
	private void drawInsertFrame(Graphics g)
	{
		g.drawImage(insertframeImage,insertframeX,insertframeY,this);
		g.setFont(bigFont);
		g.setColor(Color_darkblue);
		g.drawString("Insert an Item into Database:",insertframeX+260,insertframeY+120);
		int x=insertframeX+50,y=insertframeY+135,width=130,height=20,interval=50;
		int l=catalogFields.length/2;
		g.setFont(smallFont);
		for(int i=0;i<l;i++)
		{
			g.drawImage(shieldImage,x-30,y-12,this);
			g.drawString(catalogFields[i*2+0],x-8,y);
			y+=interval;
		}
	}
	private void drawSelectFrame(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(selectframeX+5,selectframeY+100,905,500);
		this.drawItems(g);
		this.drawCatalogItems(g);
		g.drawImage(selectframeImage,selectframeX,selectframeY,this);
		g.setFont(smallFont);
		g.setColor(Color.black);
		g.drawString(selectText,selectframeX+108,selectframeY+54);
		String listText;
		if(listPosition>=0)listText=attributeFields[listPosition*2+0]+":"+attributeFields[listPosition*2+1];
		else 
		{
			int K=Integer.parseInt(settingTable[0])/2;
			listText=attributeFields[K*2+0]+":"+attributeFields[K*2+1];
		}
		g.drawString(listText,selectframeX+768,selectframeY+54);
	}
	private void drawItems(Graphics g)
	{
		int C=attributeFields.length/2+1;
		int l=items.length/C;
		int x0=selectframeX+219-33,y0=selectframeY+160-33,x=x0,y=y0,dx=10,dy=15;
		int K=Integer.parseInt(settingTable[0])/2;
		g.setColor(Color_lightgray);
		g.setFont(smallFont);
		y=y0+25+itemPositionY;
		for(int i=0;i<l;i++)
		{
			x=x0;
			if(y<y0-100){y+=35;continue;}
			if(y>y0+450)break;
			if(i==itemPosition)g.drawImage(selectbuttonImage,x,y+6,this);
			g.drawImage(itemsImages[(i+(int)(Math.random()*100))%itemsImagesLength],x,y,this);
			g.drawString(items[i*C+0],x+dx+25,y+dy+15);x+=98;
			for(int j=beginAttributeField+1;j<C;j++)
			{
				g.drawString(items[i*C+j],x+dx,y+dy+15);
				x+=(j-1==K)?193:98;
				if(x>selectframeX+850)break;
			}
			y+=35;
		}
		y=y0;
		x=x0;
		g.drawImage(backfieldImage,x-5,y,this);
		g.drawImage(smallfieldImage,x,y,this);
		g.drawString("ID",x+dx,y+dy);
		x+=98;
		for(int i=beginAttributeField;i<C-1;i++)
		{
			String field=attributeFields[i*2+0];
			if(i==K)
			{
				g.drawImage(bigfieldImage,x,y,this);
				g.drawString(field,x+dx,y+dy);
				x+=193;
			}
			else
			{
				g.drawImage(smallfieldImage,x,y,this);
				g.drawString(field,x+dx,y+dy);
				x+=98;
			}
			if(x>selectframeX+850)break;
		}
	}
	private void positionCatalogItem()
	{
		int i0=this.catalogItemPosition;
		int j0=this.choiceItemPosition;
		int x0=selectframeX+33,y0=selectframeY+140;
		if(mouseX<x0)this.catalogItemPosition=-1;
		else if(mouseY<y0)this.catalogItemPosition=-1;
		else if(mouseX>selectframeX+200)this.catalogItemPosition=-1;
		else 
		{
			y0+=catalogItemPositionY+6;
			int k=(mouseY-y0)/25,m=-1,i=0,j=-1;
			for(i=0;i<catalogStateTable.length;i++)
			{
				j=-1;
				if(++m==k)break;
				if(catalogStateTable[i])
				{
					for(j=0;j<catalogChoicesTable[i].length;j++)
					{
						if(++m==k)break;
					}
					if(m==k)break;
				}
			}
			if(i>=0&&i<catalogStateTable.length)
			{
				this.catalogItemPosition=i;
				if(j>=0&&j<catalogChoicesTable[i].length)this.choiceItemPosition=j;
				else this.choiceItemPosition=-1;
			}
			else this.catalogItemPosition=-1;
		}
		if(i0!=catalogItemPosition||j0!=choiceItemPosition)this.repaint();
	}
	private void drawCatalogItems(Graphics g)
	{
		int l=catalogFields.length/2;
		int x0=selectframeX+33,y0=selectframeY+140,x=x0,y=y0,dx=10,dy=15,minY=y0-100,maxY=y0+450;
		int K=Integer.parseInt(settingTable[0])/2;
		g.setColor(Color_lightgray);
		g.setFont(smallFont);
		y=y0+catalogItemPositionY;
		for(int i=0;i<l;i++)
		{
			String fieldName=catalogFields[i*2+0];
			if(y>minY)
			{
				if(choiceItemPosition<0&&i==catalogItemPosition)g.drawImage(catalogitembuttonImage,x-20,y-5,this);
				g.drawImage(catalogitemsImages[(i+(int)(Math.random()*100))%catalogitemsImagesLength],x,y,this);
				g.drawString(fieldName,x+22,y+14);
			}
			y+=25;
			if(y>maxY)return;
			if(catalogStateTable[i])
			{
				String[] choiceStrings=this.catalogChoicesTable[i];
				for(int j=0;j<choiceStrings.length;j++)
				{
					String choice=choiceStrings[j];
					if(y>minY)
					{
						if(catalogChoicesStateTable[i][j])g.drawImage(smallbluebuttonImage,x-20,y-2,this);
						else if(i==catalogItemPosition&&j==choiceItemPosition)g.drawImage(catalogitembuttonImage,x-20,y-5,this);
						g.drawImage(catalogitemsImages[(i+(int)(Math.random()*100))%catalogitemsImagesLength],x+20,y,this);
						g.drawString(choice,x+22+20,y+14);
					}
					y+=25;
					if(y>maxY)return;
				}
			}
		}
	}
	private void drawSelectList(Graphics g)
	{
		g.drawImage(listframeImage,selectframeX+762,selectframeY+63,this);
		int x=selectframeX+784,y=selectframeY+121,w=126,h=485;
		int C=attributeFields.length/2;
		g.setColor(Color.black);
		g.setFont(middleFont);
		if(listPosition>=0)
		{
			String type=attributeFields[listPosition*2+1];
			g.drawString("Type:"+type,x-2,y-10);
		}
		for(int i=0;i<C;i++)
		{
			if(i==listPosition)g.drawImage(listbuttonImage,x-13,y,this);
			String field=attributeFields[i*2+0];
			g.drawString(field,x+5,y+20);
			y+=25;
		}
	}
	private void positionItem()
	{
		int i0=this.itemPosition;
		int x0=selectframeX+219-33,y0=selectframeY+160-33+25;
		if(mouseX<x0)this.itemPosition=-1;
		else if(mouseY<y0)this.itemPosition=-1;
		else if(mouseX>selectframeX+800)this.itemPosition=-1;
		else 
		{
			y0+=itemPositionY+6;
			this.itemPosition=(mouseY-y0)/35;
			int C=attributeFields.length/2+1;
			int l=items.length/C;
			if(itemPosition>=l)this.itemPosition=-1;
		}
		if(i0!=itemPosition)this.repaint();
	}
	private void positionRemote()
	{
		int i0=this.remotePosition;
		int x0=selectframeX+219-33,y0=selectframeY+160-33+25;
		if(mouseX<x0)this.remotePosition=-1;
		else if(mouseY<y0)this.remotePosition=-1;
		else if(mouseX>selectframeX+800)this.remotePosition=-1;
		else
		{
			y0+=remotePositionY+6;
			this.remotePosition=(mouseY-y0)/35;
			int l=remoteInfo.length/2;
			if(remotePosition>=l)this.remotePosition=-1;
		}
		if(i0!=remotePosition)this.repaint();
	}
	private void positionRemoteFile()
	{
		int i0=this.remoteFilePosition;
		int x0=selectframeX+33,y0=selectframeY+140;
		if(mouseX<x0)this.remoteFilePosition=-1;
		else if(mouseY<y0)this.remoteFilePosition=-1;
		else if(mouseX>selectframeX+200)this.remoteFilePosition=-1;
		else 
		{
			y0+=remoteFilePositionY;
			this.remoteFilePosition=(mouseY-y0)/25;
		}
		if(i0!=remoteFilePosition)this.repaint();
	}
	private void positionList()
	{
		int i0=this.listPosition;
		int x0=selectframeX+784,y0=selectframeY+121,w=126,h=485;
		if(mouseIsInBounds(x0,y0,w,h))
		{
			this.listPosition=(mouseY-y0)/31;
			int C=attributeFields.length/2;
			if(listPosition>C)this.listPosition=-1;
		}
		else this.listPosition=-1;
		if(i0!=listPosition)this.repaint();
	}
	private void checkCrystalButton()
	{
		int s=isOnCrystalButton;
		int x=alterframeX+381;
		int y=alterframeY+407;
		int w=140;
		int h=30;
		if(mouseIsInBounds(x,y,w,h))this.isOnCrystalButton=1;
		else this.isOnCrystalButton=0;
		if(isOnCrystalButton!=s)this.repaint();
	}
	private void positionClass()
	{
		int i0=this.classPosition;
		if(i0>=0)
		{
			int w=62,x=100+i0*w,y=727,width=236,height=169;
			if(mouseIsInBounds(x-(width-w)/2,y-height,width,height))
			{
				this.classPosition=i0;
				this.repaint();
				return;
			}
		}
		int x0=100,y0=727,dx=62;
		if(mouseX<x0)this.classPosition=-1;
		else if(mouseY<y0)this.classPosition=-1;
		else if(mouseX>screenWidth-17)this.classPosition=classFieldStrings.length;
		else 
		{
			this.classPosition=(mouseX-x0)/dx+beginClassPosition;
			if((mouseX-x0)%dx<10)this.classPosition=-1;
			if((mouseX-x0)%dx>50)this.classPosition=-1;
			if(classPosition>=classFieldStrings.length)this.classPosition=-1;
		}
		if(i0!=classPosition)
		{
			if(classPosition>=0)
			{
				this.updateNewIDsAndNewNames();
				this.isOnShiningClassButton=1;
			}
			else this.isOnShiningClassButton=0;
			this.repaint();
		}
	}
	private void positionAttributeField()
	{
		int i0=this.attributePosition;
		int x0=alterframeX+130,y0=alterframeY+490,x=x0,dx=150;
		if(mouseX<x0)this.attributePosition=-1;
		else if(mouseY<y0)this.attributePosition=-1;
		else if(mouseY>y0+86)this.attributePosition=-1;
		else if(mouseX>alterframeX+900)this.attributePosition=-1;
		else 
		{
			this.attributePosition=(mouseX-x0)/dx+beginAttributePosition;
			if((mouseX-x0)%dx>68)this.attributePosition=-1;
			int c=attributeFields.length/2;
			if(attributePosition>=c)this.attributePosition=-1;
		}
		if(i0!=attributePosition)this.repaint();
	}
	private void positionCatalogField()
	{
		int i0=this.currentCatalogPosition;
		int x0=alterframeX+130,y0=alterframeY+80,x=x0,dx=150;
		if(mouseX<x0)this.currentCatalogPosition=-1;
		else if(mouseY<y0)this.currentCatalogPosition=-1;
		else if(mouseY>y0+110)this.currentCatalogPosition=-1;
		else if(mouseX>alterframeX+900)this.currentCatalogPosition=-1;
		else 
		{
			this.currentCatalogPosition=(mouseX-x0)/dx+beginCatalogPosition;
			if((mouseX-x0)%dx>68)this.currentCatalogPosition=-1;
			int c=catalogFields.length/2;
			if(currentCatalogPosition>=c)this.currentCatalogPosition=-1;
		}
		if(i0!=currentCatalogPosition)this.repaint();
	}
	private void drawAlterFrame(Graphics g)
	{
		g.drawImage(alterframeImage,alterframeX,alterframeY,this);
		if(isOnCrystalButton==1)g.drawImage(crystalbuttonImage,alterframeX+381,alterframeY+407,this);
		else g.drawImage(darkbuttonImage,alterframeX+381,alterframeY+407,this);
		g.setColor(Color.white);
		g.setFont(middleFont);
		g.drawString("Add a Field",alterframeX+388,alterframeY+427);
		int l=attributeFields.length/2;
		int x0=alterframeX+130,y0=alterframeY+480,x=x0,dx=150;
		g.setColor(Color.white);
		g.setFont(smallFont);
		for(int i=beginAttributePosition;i<l;i++)
		{
			if(i==attributePosition)
			{
				g.setColor(Color_transparentblue);
				g.fillRect(x-5,y0-5,80,80);
				g.setColor(Color_transparentwhite);
				g.drawRect(x-5,y0-5,80,80);
				g.setColor(Color.white);
			}
			g.drawImage(attributefieldImage,x,y0,this);
			g.drawString(attributeFields[i*2+0],x,y0+90);
			g.drawString(attributeFields[i*2+1],x,y0+20);
			x+=dx;
			if(x>alterframeX+832)break;
		}
		l=catalogFields.length/2;
		x=x0;y0=alterframeY+80;
		for(int i=beginCatalogPosition;i<l;i++)
		{
			if(i==currentCatalogPosition)
			{
				g.setColor(Color_transparentblue);
				g.fillRect(x-5,y0-8,80,80);
				g.setColor(Color_transparentwhite);
				g.drawRect(x-5,y0-8,80,80);
				g.setColor(Color.white);
			}
			g.drawImage(catalogfieldImage,x,y0,this);
			g.drawString(catalogFields[i*2+0],x,y0+90);
			g.setColor(Color.white);
			g.drawRect(x-5,y0+99,80,14);
			g.drawString(this.getStringLines(catalogFields[i*2+1],10)[0],x,y0+110);
			x+=dx;
			if(x>alterframeX+832)break;
		}
	}
	private String[] getStringLines(String string,int length)
	{
		string.replaceAll("\n","");
		string.replaceAll("\r","");
		int l=string.length()/length;
		int m=string.length()%length;
		int c=0;
		if(m!=0)l++;
		String[] lines=new String[l];
		for(int i=0;i<l-1;i++,c+=length)lines[i]=string.substring(c,c+length);
		lines[l-1]=string.substring(c,string.length());
		return lines;
	}
	private boolean onInsertButton()
	{
		int x=insertframeX+258;
		int y=insertframeY+163;
		int w=64;
		int h=66;
		if(mouseIsInBounds(x,y,w,h))return true;
		return false;
	}
	private void drawInsertButton(Graphics g)
	{
		g.drawImage(insertbuttonImage,insertframeX+257,insertframeY+158,this);
		g.setColor(Color.blue);
		g.setFont(smallFont);
		g.drawString("Insert",insertframeX+266,insertframeY+156);
	}
	private boolean onExitButton()
	{
		int x=268;
		int y=687;
		int w=90;
		int h=22;
		if(mouseIsInBounds(x,y,w,h))return true;
		return false;
	}
	private void drawExitButton(Graphics g)
	{
		int x=267,y=688;
		g.setColor(Color.white);
		g.setFont(smallFont);
		g.drawImage(exitbuttonImage,x,y,this);
		g.drawString("Exit",290,702);
	}	
	private boolean onUpdateButton()
	{
		int x=updateframeX+12;
		int y=updateframeY+457;
		int w=57;
		int h=32;
		if(mouseIsInBounds(x,y,w,h))return true;
		return false;
	}
	private void drawUpdateButton(Graphics g)
	{
		int x=updateframeX+12;
		int y=updateframeY+437;
		g.setColor(Color_darkblue);
		g.setFont(middleFont);
		g.drawImage(updatebuttonImage,x,y,this);
		g.drawString("Update The Item",x+70,y+30);
	}
	private boolean closeUpdateFrame()
	{
		int x=updateframeX+740;
		int y=updateframeY;
		int w=56;
		int h=21;
		if(mouseIsInBounds(x,y,w,h))return true;
		return false;
	}
	private void drawWindow(Graphics g)
	{
		g.setColor(Color_darkblue);
		g.setFont(middleFont);
		g.drawImage(windowImage,windowX,windowY,this);
		int x0=windowX+10,y0=windowY+70,x=x0,y=y0;
		if(state==WINDOW_INSERT)g.drawImage(windowbuttonImage,x,y,this);
		x+=20;y+=10;
		g.drawImage(insertImage,x,y,this);
		x+=70;y+=30;
		g.drawString("Insert a new item",x,y);
		x=x0;y+=20;
		if(state==WINDOW_SELECT)g.drawImage(windowbuttonImage,x,y,this);
		x+=20;y+=10;
		g.drawImage(selectImage,x,y,this);
		x+=70;y+=30;
		g.drawString("Select some items",x,y);
		x=x0;y+=20;
		if(state==WINDOW_ALTER)g.drawImage(windowbuttonImage,x,y,this);
		x+=20;y+=10;
		g.drawImage(alterImage,x,y,this);
		x+=70;y+=30;
		g.drawString("Alter some Fields",x,y);
	}
	private void setTextFieldsVisible(boolean bool)
	{
		for(int i=0;i<attributeTextFields.length;i++)
		{
			this.attributeTextFields[i].setText(attributeFields[i*2]);
			this.attributeTextFields[i].setFocusable(bool);
			this.attributeTextFields[i].setVisible(bool);
		}
		for(int i=0;i<catalogChoices.length;i++)
		{
			this.catalogChoices[i].setFocusable(bool);
			this.catalogChoices[i].setVisible(bool);
		}
	}
	private void insertItemIntoDatabase()
	{
		int l1=attributeTextFields.length;
		int l2=catalogChoices.length;
		String[] fields1=this.getFieldNames(attributeFields);
		String[] fields2=this.getFieldNames(catalogFields);
		String[] values1=new String[l1];
		String[] values2=new String[l2];
		for(int i=0;i<l1;i++)values1[i]=attributeTextFields[i].getText();
		for(int i=0;i<l2;i++)values2[i]=catalogChoices[i].getSelectedItem();
		String[] types1=this.getFieldTypes(attributeFields);
		String[] types2=this.getFieldTypes(catalogFields);
		this.mySQLprocessor.executeInsert(fields1,fields2,values1,values2,types1,types2,tableName);
		this.updateIDsAndNames();
		this.repaint();
	}
	private void selectItemsFromDatabase()
	{
		String selectCondition="";
		String text=selectTextField.getText();
		int K=Integer.parseInt(settingTable[0])/2;
		String field=attributeFields[K*2+0];
		String type=attributeFields[K*2+1];
		if(listPosition>=0)
		{
			field=attributeFields[listPosition*2+0];
			type=attributeFields[listPosition*2+1];
		}
		if(text.equals("")||field.equals(""))selectCondition="";
		else 
		{
			if(type.equals("int"))
			{
				if(isInteger(text))selectCondition=field+"="+text;
				else this.selectTextField.setText("");
			}
			else if(type.equals("float"))
			{
				if(isFloat(text))selectCondition=field+"="+text;
				else this.selectTextField.setText("");
			}
			else selectCondition=field+"='"+text+"'";
		}
		boolean and=selectCondition.equals("")?false:true;
		for(int i=0;i<catalogChoicesStateTable.length;i++)
		{
			boolean[] choicesState=catalogChoicesStateTable[i];
			for(int j=0;j<choicesState.length;j++)
			{
				if(choicesState[j])
				{
					if(and)selectCondition+=" And ";else and=true;
					field=catalogFields[i*2+0];
					text=catalogChoicesTable[i][j];
					selectCondition+=field+"='"+text+"'";
				}
			}
		}
		String[] fieldNames=this.getFieldNames(attributeFields);
		this.items=this.mySQLprocessor.executeQuery(tableName,fieldNames,selectCondition);
		this.itemPositionY=0;
		this.itemPositionY0=0;
		this.repaint();
	}
	private void alterTableChangeAttributeField()
	{
		if(currentAttributePosition<=criticalAttributePosition)return;
		String newFieldName=this.attributeTextField.getText().replaceAll(" ","");
		this.attributeTextField.setText(newFieldName);
		if(newFieldName.equals(""))return;
		if(isNumber(newFieldName.charAt(0)))return;
		if(!isEnglish(newFieldName))return;
		if(isIn(newFieldName,attributeFields))return;
		String fieldName=attributeFields[currentAttributePosition*2+0];
		String fieldType=attributeFields[currentAttributePosition*2+1];
		this.mySQLprocessor.alterTableChange(tableName,fieldName,fieldType,newFieldName);
		this.attributeFields[currentAttributePosition*2+0]=newFieldName;
		this.writeStringsIntoFile(attributeFields,attributefieldFile);
		this.nameField=this.attributeFields[Integer.parseInt(settingTable[0])];
		this.initializeTextFields();
	}
	private void alterTableChangeCatalogField()
	{
		if(newCatalogPosition<=criticalCatalogPosition)return;
		this.alterCatalogField();
		String newFieldName=this.catalogTextField.getText().replaceAll(" ","");
		this.catalogTextField.setText(newFieldName);
		if(newFieldName.equals(""))return;
		if(isNumber(newFieldName.charAt(0)))return;
		if(!isEnglish(newFieldName))return;
		if(isIn(newFieldName,catalogFields))return;
		String fieldName=catalogFields[newCatalogPosition*2+0];
		this.mySQLprocessor.alterTableChange(tableName,fieldName,"text",newFieldName);
		this.catalogFields[newCatalogPosition*2+0]=newFieldName;
		this.writeStringsIntoFile(catalogFields,catalogfieldFile);
		this.initializeTextFields();
	}
	private void alterCatalogField()
	{
		String newContent=this.catalogContentTextField.getText().replaceAll(" ","");
		if(newContent.equals(""))return;
		if(newContent.charAt(newContent.length()-1)!='|')newContent+="|";
		this.catalogContentTextField.setText(newContent);
		this.catalogFields[newCatalogPosition*2+1]=newContent;
		this.writeStringsIntoFile(catalogFields,catalogfieldFile);
		this.initializeTextFields();
		this.classFieldStrings=this.getChoiceStrings(catalogFields[classField*2+1]);
	}
	private boolean isNumber(char c)
	{
		return c>='0'&&c<='9';
	}
	private boolean isLetter(char c)
	{
		return (c>='a'&&c<='z')||(c>='A'&&c<='Z');
	}
	private void alterTableAddAttributeField()
	{
		String fieldName=attributeFieldNameTextField.getText().replaceAll(" ","");
		if(fieldName.equals(""))return;
		if(isNumber(fieldName.charAt(0))){attributeFieldNameTextField.setText("");return;}
		if(!isEnglish(fieldName)){attributeFieldNameTextField.setText("");return;}
		if(isIn(fieldName,attributeFields)){attributeFieldNameTextField.setText("");return;}
		if(isIn(fieldName,catalogFields)){attributeFieldNameTextField.setText("");return;}
		String fieldType=attributeFieldTypeTextField.getText();
		if(fieldType.equals(""))fieldType="text";
		fieldType=fieldType.toLowerCase();
		if(!(fieldType.equals("int")||fieldType.equals("float")||fieldType.equals("text"))){alterTableAddCatalogField(fieldName,fieldType);return;}
		this.attributeFieldTypeTextField.setText(fieldType);
		this.mySQLprocessor.alterTableAdd(tableName,fieldName,fieldType);
		int length=attributeFields.length;
		String[] newAttributeFields=new String[length+2];
		for(int i=0;i<length;i++)newAttributeFields[i]=attributeFields[i];
		newAttributeFields[length+0]=fieldName;
		newAttributeFields[length+1]=fieldType;
		this.attributeFields=newAttributeFields;
		this.writeStringsIntoFile(attributeFields,attributefieldFile);
		this.initializeTextFields();
	}
	private void alterTableAddCatalogField(String fieldName,String fieldContent)
	{
		if(fieldContent.charAt(fieldContent.length()-1)!='|')fieldContent+="|";
		this.attributeFieldTypeTextField.setText(fieldContent);
		this.mySQLprocessor.alterTableAdd(tableName,fieldName,"text");
		int length=catalogFields.length;
		String[] newCatalogFields=new String[length+2];
		for(int i=0;i<length;i++)newCatalogFields[i]=catalogFields[i];
		newCatalogFields[length+0]=fieldName;
		newCatalogFields[length+1]=fieldContent;
		this.catalogFields=newCatalogFields;
		this.writeStringsIntoFile(catalogFields,catalogfieldFile);
		this.initializeTextFields();
	}
	private void alterTableDropAttributeField()
	{
		if(attributePosition<=criticalAttributePosition)return;
		String fieldName=attributeFields[attributePosition*2+0];
		this.mySQLprocessor.alterTableDrop(tableName,fieldName);
		int length=attributeFields.length/2,j=0;
		String[] newAttributeFields=new String[(length-1)*2];
		for(int i=0;i<length;i++)
		{
			if(i==attributePosition)continue;
			newAttributeFields[j++]=attributeFields[i*2+0];
			newAttributeFields[j++]=attributeFields[i*2+1];
		}
		this.attributeFields=newAttributeFields;
		this.writeStringsIntoFile(attributeFields,attributefieldFile);
		this.initializeTextFields();
	}
	private void alterTableDropCatalogField()
	{
		if(currentCatalogPosition<=criticalCatalogPosition)return;
		String fieldName=catalogFields[currentCatalogPosition*2+0];
		this.mySQLprocessor.alterTableDrop(tableName,fieldName);
		int length=catalogFields.length/2,j=0;
		String[] newCatalogFields=new String[(length-1)*2];
		for(int i=0;i<length;i++)
		{
			if(i==currentCatalogPosition)continue;
			newCatalogFields[j++]=catalogFields[i*2+0];
			newCatalogFields[j++]=catalogFields[i*2+1];
		}
		this.catalogFields=newCatalogFields;
		this.writeStringsIntoFile(catalogFields,catalogfieldFile);
		this.initializeTextFields();
	}
	private boolean isEnglish(String text)
	{
		int length=text.length();
		for(int i=0;i<length;i++)
		{
			char c=text.charAt(i);
			if(!isLetter(c)&&!isNumber(c))return false;
		}
		return true;
	}
	private void drawMessageBox(Graphics g)
	{
		g.setColor(new Color(0f,0f,0f,0.8f));
		g.fillRect(messageboxX+5,messageboxY+5,725,330);
		g.drawImage(messageboxImage,messageboxX,messageboxY,this);
		int simplebuttonX=messageboxX+300,simplebuttonY=messageboxY+250;
		if(state==MESSAGE)g.drawImage(simplebuttonImage,simplebuttonX,simplebuttonY,this);
		else g.drawImage(shinebuttonImage,simplebuttonX,simplebuttonY,this);
		g.setFont(smallFont);
		g.setColor(Color.white);
		g.drawString(messageString,messageboxX+40,messageboxY+60);
		g.drawString(messagebuttonString,simplebuttonX+60,simplebuttonY+23);
	}
	private boolean onMessageButton()
	{
		int simplebuttonX0=messageboxX+300,simplebuttonY0=messageboxY+250,simplebuttonW=147,simplebuttonH=39;
		return mouseIsInBounds(simplebuttonX0,simplebuttonY0,simplebuttonW,simplebuttonH);
	}
	private String[] readStringsFromFile(String fileName)
	{
		StringQueue queue=new StringQueue();
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName));
			String s=BufferedReader1.readLine();
			while(s!=null)
			{
				queue.enQueue(s);
				s=BufferedReader1.readLine();
			}
			BufferedReader1.close();
		}
		catch(Exception e){System.err.println(e);}
		return queue.getStrings();
	}
	private void writeStringsIntoFile(String[] strings,String fileName)
	{
		StringQueue queue=new StringQueue();
		try
		{
			PrintWriter PrintWriter1=new PrintWriter(fileName);
			for(int i=0;i<strings.length;i++)PrintWriter1.println(strings[i]);
			PrintWriter1.close();
		}
		catch(Exception e){System.err.println(e);}
	}
	private void runProcess(String cmd)
	{
		try
		{
			Runtime Runtime1=Runtime.getRuntime();
			Runtime1.exec(cmd);
		}
		catch(Exception e){System.err.println(e);}
	}
	private String[] getFieldNames(String[] table)
	{
		int l=table.length/2;
		String[] fieldNames=new String[l];
		for(int i=0;i<l;i++)fieldNames[i]=table[i*2+0];
		return fieldNames;
	}
	private String[] getFieldTypes(String[] table)
	{
		int l=table.length/2;
		String[] fieldTypes=new String[l];
		for(int i=0;i<l;i++)fieldTypes[i]=table[i*2+1];
		return fieldTypes;
	}
	private boolean mouseIsInBounds(int x,int y,int width,int height)
	{
		if(mouseX>x&&mouseY>y&&mouseX<x+width&&mouseY<y+height)return true;
		else return false;
	}
	private boolean isIn(String string,String[] strings)
	{
		for(int i=0;i<strings.length;i++)
		{
			if(string.toLowerCase().equals(strings[i].toLowerCase()))return true;
		}
		return false;
	}
	private boolean isInteger(String s)
	{
		int length=s.length();
		if(length==0)return false;
		int i=0;
		char c=s.charAt(i);
		if(c=='+'||c=='-')if(length>1)i=1;
		for(;i<length;i++)
		{
			c=s.charAt(i);
			if(c<'0'||c>'9')return false;
		}
		return true;
	}
	private boolean isFloat(String s)
	{
		int length=s.length();
		if(length==0)return false;
		String s0="",s1="";
		int i=0;
		for(;i<length;i++)
		{
			char c=s.charAt(i);
			if(c=='.')break;
			s0+=c;
		}
		if(i==length)return isInteger(s0);
		i++;
		for(;i<length;i++)
		{
			char c=s.charAt(i);
			if(c<'0'||c>'9')return false;
			s1+=c;
		}
		return isInteger(s0)&&isInteger(s1);
	}
}
class MySQLprocessor
{
	private Connection connection;
	private ResultSet tableResultSet;
	private String[] tableFields;
	private String tableName;
	private int tableWidth;
	private int tableHeight;
	private int tableColumn;
	private int tableRow;
	private Font tableFont;
	private int gridWidth;
	private int gridHeight;
	private int intervalWidth;
	private int intervalHeight;
	private String structuredQueryLanguage;
	public MySQLprocessor(String dataBase)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"+dataBase,"root","11235813");
		}
		catch(Exception e){e.printStackTrace();}
	}
	public ResultSet executeQuery(String structuredQueryLanguage)
	{
		ResultSet ResultSet1=null;
		try
		{
			ResultSet1=this.connection.createStatement().executeQuery(structuredQueryLanguage);
		}
		catch(Exception e){e.printStackTrace();}
		return ResultSet1;
	}
	public int[] executeQuery(String fieldName,String tableName)
	{
		try
		{
			String structuredQueryLanguage="Select "+fieldName+" From "+tableName;
System.out.println(structuredQueryLanguage);
			ResultSet ResultSet1=this.connection.createStatement().executeQuery(structuredQueryLanguage);
			int r=0;
			while(ResultSet1.next())r++;
			int[] result=new int[r];
			ResultSet1.first();
			for(int i=0;i<r;i++)
			{
				result[i]=ResultSet1.getInt(fieldName);
				ResultSet1.next();
			}
			return result;
		}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	public String[] executeQuery(String fieldName,int[] IDs,String tableName)
	{
		try
		{
			int l=IDs.length;
			String[] result=new String[l];
			for(int i=0;i<l;i++)
			{
				String structuredQueryLanguage="Select "+fieldName+" From "+tableName+" Where "+tableName+".ID="+IDs[i];
				ResultSet ResultSet1=this.connection.createStatement().executeQuery(structuredQueryLanguage);
				ResultSet1.first();
				result[i]=ResultSet1.getString(fieldName);
			}
			return result;
		}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	public String[] executeQuery(int ID,String[] fieldNames1,String[] fieldNames2,String tableName)
	{
		try
		{
			String structuredQueryLanguage="Select * From "+tableName+" Where ID="+ID;
			ResultSet ResultSet1=this.connection.createStatement().executeQuery(structuredQueryLanguage);
			ResultSet1.first();
			int k=0,l1=fieldNames1.length,l2=fieldNames2.length;
			String[] result=new String[l1+l2];
			for(int i=0;i<l1;i++)result[k++]=ResultSet1.getString(fieldNames1[i]);
			for(int i=0;i<l2;i++)result[k++]=ResultSet1.getString(fieldNames2[i]);
			return result;
		}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	public String[] executeQuery(String tableName,String[] fieldNames,String condition)
	{
		try
		{
			StringQueue resultQueue=new StringQueue();
			String structuredQueryLanguage="Select * From "+tableName;
			if(condition!=null&&!condition.equals(""))structuredQueryLanguage+=" Where "+condition;
			ResultSet ResultSet1=this.connection.createStatement().executeQuery(structuredQueryLanguage);
			while(ResultSet1.next())
			{
				resultQueue.enQueue(ResultSet1.getString("ID"));
				for(int i=0;i<fieldNames.length;i++)resultQueue.enQueue(ResultSet1.getString(fieldNames[i]));
			}
			return resultQueue.getStrings();
		}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	public int[][] executeQuery(String[] columns,String structuredQueryLanguage)
	{
		try
		{
			ResultSet ResultSet1=this.connection.createStatement().executeQuery(structuredQueryLanguage);
			int r=0;
			while(ResultSet1.next())r++;
			int c=columns.length;
			int[][] result=new int[r][c];
			ResultSet1.first();
			for(int i=0;i<r;i++)
			{
				for(int j=0;j<c;j++)result[i][j]=ResultSet1.getInt(columns[j]);
				ResultSet1.next();
			}
			return result;
		}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	public void executeInsert(String[] fields1,String[] fields2,String[] values1,String[] values2,String[] types1,String[] types2,String table)
	{
		int l1=fields1.length;
		int l2=fields2.length;
		String field=" (",value=" (";
		for(int i=0;i<l1;i++)
		{
			field+=fields1[i];
			if(types1[i].equals("text"))value+="\'"+values1[i]+"\'";
			else
			{	
				if(types1[i].equals("int")&&isInteger(values1[i]))value+=values1[i];
				else if(types1[i].equals("float")&&isFloat(values1[i]))value+=values1[i];
				else value+="-1";
			}
			field+=",";
			value+=",";
		}
		for(int i=0;i<l2;i++)
		{
			field+=fields2[i];
			value+="\'"+values2[i]+"\'";
			if(i==l2-1){field+=") ";value+=") ";}
			else {field+=",";value+=",";}
		}
		String structuredQueryLanguage="Insert Into "+table+field+"Values"+value;
System.out.println(structuredQueryLanguage);
		this.executeUpdate(structuredQueryLanguage);
	}
	public void executeUpdate(String field,String type,String value,String table,int ID)
	{
		String newValue;
		if(type.equals("text"))newValue="\'"+value+"\'";
		else
		{	
			if(type.equals("int")&&isInteger(value))newValue=value;
			else if(type.equals("float")&&isFloat(value))newValue=value;
			else newValue="-1";
		}
		String structuredQueryLanguage="Update "+table+" Set "+field+" = "+newValue+" Where ID="+ID;
System.out.println(structuredQueryLanguage);
		this.executeUpdate(structuredQueryLanguage);
	}
	public void executeUpdate(String structuredQueryLanguage)
	{
		try
		{
			this.connection.createStatement().executeUpdate(structuredQueryLanguage);
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void executeUpdate(String[] structuredQueryLanguages)
	{
		int n=structuredQueryLanguages.length;
		try
		{
			for(int i=0;i<n;i++)this.connection.createStatement().executeUpdate(structuredQueryLanguages[i]);
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void createTable(int width,int height,String[] fields,String table,String fontName,int fontSize)
	{
		this.tableFields=fields;
		this.tableName=table;
		this.tableWidth=width;
		this.tableHeight=height;
		this.tableColumn=fields.length;
		this.tableRow=height/(2*fontSize);
		this.gridWidth=width/tableColumn;
		this.gridHeight=height/tableRow;
		this.intervalWidth=gridWidth/50;
		this.intervalHeight=gridHeight/5;
		this.structuredQueryLanguage="Select "+fields[0];
		for(int j=1;j<tableColumn;j++)this.structuredQueryLanguage+=","+fields[j];
		this.structuredQueryLanguage+=" From "+table;
		this.tableFont=new Font(fontName,Font.PLAIN,fontSize);
	}
	public void alterTableChange(String tableName,String fieldName,String fieldType,String newFieldName)
	{
		String structuredQueryLanguage="Alter Table "+tableName+" Change "+fieldName+" "+newFieldName+" "+fieldType;
		try
		{
			this.connection.createStatement().execute(structuredQueryLanguage);
		}
		catch(Exception e){System.err.println(e);}
	}
	public void alterTableAdd(String tableName,String fieldName,String fieldType)
	{
		String structuredQueryLanguage="Alter Table "+tableName+" Add "+fieldName+" "+fieldType;
System.out.println(structuredQueryLanguage);
		try
		{
			this.connection.createStatement().execute(structuredQueryLanguage);
		}
		catch(Exception e){System.err.println(e);}
	}
	public void alterTableDrop(String tableName,String fieldName)
	{
		String structuredQueryLanguage="Alter Table "+tableName+" Drop "+fieldName;
		try
		{
			this.connection.createStatement().execute(structuredQueryLanguage);
		}
		catch(Exception e){System.err.println(e);}
	}
	public void setCondition(String whereClause)
	{
		if(whereClause.equals(""))
		{
			this.structuredQueryLanguage="Select "+tableFields[0];
			for(int j=1;j<tableColumn;j++)this.structuredQueryLanguage+=","+tableFields[j];
			this.structuredQueryLanguage+=" From "+tableName;
		}
		else this.structuredQueryLanguage+=" Where "+whereClause;
	}
	public void drawTable(Graphics g,int startY)
	{
		this.drawFrame(g,0,0,tableWidth-1,tableHeight-1);
		try
		{
			this.tableResultSet=this.connection.createStatement().executeQuery(this.structuredQueryLanguage);
			if(tableResultSet==null)return;
			for(int i=1;tableResultSet.next();i++)
			{
				for(int j=0;j<tableColumn;j++)
				{
					this.drawGrid(g,i,j,tableResultSet.getString(tableFields[j]),startY);
				}
			}
		}
		catch(Exception e){e.printStackTrace();}
		g.setColor(Color.white);
		g.fillRect(0,0,tableWidth,gridHeight);
		for(int j=0;j<tableColumn;j++)this.drawGrid(g,0,j,tableFields[j],0);
	}
	private void drawGrid(Graphics g,int i,int j,String text,int startY)
	{
		int x0=j*gridWidth+intervalWidth;
		int y0=i*gridHeight+intervalHeight-startY;
		int width=gridWidth-2*intervalWidth;
		int height=gridHeight-2*intervalHeight;
		this.drawFrame(g,x0,y0,width,height);
		x0=j*gridWidth;
		y0=i*gridHeight-startY;
		width=gridWidth;
		height=gridHeight;
		this.drawFrame(g,x0,y0,width,height);
		g.setColor(Color.BLACK);
		g.setFont(tableFont);
		g.drawString(text,x0+2*intervalWidth,y0+gridHeight-2*intervalHeight);
	}
	private void drawFrame(Graphics g,int x0,int y0,int width,int height)
	{
		g.setColor(new Color(210,210,230));
		g.drawRect(x0,y0,width,height);
		g.setColor(new Color(140,140,160));
		g.drawLine(x0,y0,x0+width,y0);
	}
	private boolean isInteger(String s)
	{
		int length=s.length();
		if(length==0)return false;
		int i=0;
		char c=s.charAt(i);
		if(c=='+'||c=='-')if(length>1)i=1;
		for(;i<length;i++)
		{
			c=s.charAt(i);
			if(c<'0'||c>'9')return false;
		}
		return true;
	}
	private boolean isFloat(String s)
	{
		int length=s.length();
		if(length==0)return false;
		String s0="",s1="";
		int i=0;
		for(;i<length;i++)
		{
			char c=s.charAt(i);
			if(c=='.')break;
			s0+=c;
		}
		if(i==length)return isInteger(s0);
		i++;
		for(;i<length;i++)
		{
			char c=s.charAt(i);
			s1+=c;
		}
		return isInteger(s0)&&isInteger(s1);
	}
}
/*
class StringQueue
{
	private String stringQueue;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public StringQueue(String queue)
	{
		this.stringQueue=queue;
		this.length=this.getLength();
		if(length==0)
		{
			this.stringQueue+=";";
			this.length=1;
		}
	}
	public void enQueue(String string)
	{
		this.stringQueue+=string+";";
	}
	public void enQueue(String[] string)
	{
		int l=string.length;
		for(int i=0;i<l;i++)this.stringQueue+=string[i]+";";
	}
	public String deQueue()
	{
		String string="";
		if(stringQueue.length()==0)return string;
		int n=0;
		char c=stringQueue.charAt(n++);
		while(c!=';')
		{
			string+=c;
			c=stringQueue.charAt(n++);
		}
		this.stringQueue=stringQueue.substring(n,stringQueue.length());
		return string;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public String[] getStrings()
	{
		int l=this.length();
		String[] strings=new String[l];
		int n=0,i=0;
		String s="";
		char c;
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			strings[n++]=s;
			s="";
		}
		return strings;
	}
	public int length()
	{
		int l=0;
		for(int i=0;i<stringQueue.length();i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
		}
		return l;
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}
*/
class StringQueue
{
	private String stringQueue;
	private int length;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public StringQueue(String queue)
	{
		this.stringQueue=queue;
		this.length=this.getLength();
		if(length==0)
		{
			this.stringQueue+=";";
			this.length=1;
		}
	}
	public void enQueue(String string)
	{
		this.stringQueue+=string+";";
		this.length++;
	}
	public String deQueue()
	{
		String string="";
		if(stringQueue.length()==0)return string;
		int n=0;
		char c=stringQueue.charAt(n++);
		while(c!=';')
		{
			string+=c;
			c=stringQueue.charAt(n++);
		}
		this.stringQueue=stringQueue.substring(n,stringQueue.length());
		this.length--;
		return string;
	}
	private int getLength()
	{
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
		}
		return l;
	}
	public int length()
	{
		return this.length;
	}
	public String[] getStrings()
	{
		int l=this.length();
		String[] strings=new String[l];
		int n=0,i=0;
		String s="";
		char c;
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			strings[n++]=s;
			s="";
		}
		return strings;
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}