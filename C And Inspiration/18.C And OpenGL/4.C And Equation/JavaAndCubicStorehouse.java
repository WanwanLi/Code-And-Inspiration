import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.color.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.image.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.sql.*;
public class JavaAndCubicStorehouse
{
	public static void main(String[] args)
	{
		new CubicStorehouse();
	}

}
class CubicStorehouse extends Frame implements ActionListener,ItemListener
{
	private Canvas3D storehouseCanvas3D;
	private Panel panel_StorehouseCanvas3D,panel_StorehouseScript,panel_StorehouseInfo,panel_MerchandiseInfo,panel_MerchandiseLocationInfo,panel_CarrierRouteInfo;
	private MenuBar menuBar;
	private Menu menu_StorehouseScript,menu_Storehouse,menu_Merchandise,menu_Transaction;
	private MenuItem menuItem_NewScriptFile,menuItem_OpenScriptFile,menuItem_EditScriptFile,menuItem_SaveScriptFile,menuItem_RunScriptFile;
	private MenuItem menuItem_DisplayStorehouse3D,menuItem_HideStorehouse3D,menuItem_SetStorehouseInfo;
	private MenuItem menuItem_SetMerchandiseInfo,menuItem_SetMerchandiseLocation,menuItem_getMerchandiseInfo,menuItem_getCarrierRoute;
	private CheckboxMenuItem CheckboxMenuItem1;
	private int screenWidth,screenHeight,regionCounter=0;
	private int storehouseWidth,storehouseHeight,storehouseRow,storehouseColumn,shelfColumn,shelfLevel,intervalWidth,intervalHeight,shelfWidth,shelfHeight,shelfGridWidth;
	private int startRow=0,startColumn=0;
	private MenuShortcut menuShortcut_N,menuShortcut_O,menuShortcut_E,menuShortcut_S,menuShortcut_R,menuShortcut_D,menuShortcut_H;
	private Button button_SetStorehouseInfo,button_SetStorehouseRegionInfo,button_ShowStorehouseRegions,button_HideStorehouseRegions,button_ResetStorehouseRegions;
	private Button button_InsertMerchandiseInfo,button_DeleteMerchandiseInfo,button_UpdateMerchandiseInfo,button_SelectMerchandiseInfo,button_SelectSQLMerchandiseInfo;
	private Button button_selectMerchandiseLocationID,button_setMerchandiseLocationByMouse,button_setMerchandiseLocationInfo;
	private Button button_getCarrierRoute,button_hideCarrierRoute,button_setSartLocationByMouse,button_setStartLocation;
	private TextField textField_storehouseRow,textField_storehouseColumn,textField_shelfColumn,textField_shelfLevel,textField_storehouseRegion;
	private TextField textField_merchandiseID,textField_merchandiseName,textField_merchandiseStocks,textField_merchandiseClassification,textField_merchandisePrice,textField_merchandiseDescription;
	private TextField textField_merchandiseLocationName,textField_merchandiseLocation_storehouseRow,textField_merchandiseLocation_storehouseColumn,textField_merchandiseLocation_shelfColumn,textField_merchandiseLocation_shelfLevel;
	private TextField textField_startRow,textField_startColumn;
	private TextArea textArea_MerchandiseOrderList,textArea_CarrierRoute;
	private Choice choice_merchandiseLocationID;
	private MessageBox messageBox;
	private MySQLprocessor mySQLprocessor;
	private StorehouseRegionPanel storehouseRegionPanel_StorehouseInfo,storehouseRegionPanel_MerchandiseLocationInfo,storehouseRegionPanel_CarrierRouteInfo;
	private MerchandiseInfoPanel merchandiseInfoPanel;
	private ShelfMerchandiseInfoPanel shelfMerchandiseInfoPanel_MerchandiseLocationInfo,shelfMerchandiseInfoPanel_CarrierRouteInfo;
	private StorehouseScriptPanel storehouseScriptPanel;
	private Storehouse3DPanel storehouse3DPanel;
	public CubicStorehouse()
	{
		this.messageBox=new MessageBox("Welcome To Use The Cubic Storehouse System!",600,200,"password","112358");
		this.screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		this.screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.storehouseWidth=3*screenWidth/4;
		this.storehouseHeight=screenHeight;
		this.setLayout(null);
		this.setSize(screenWidth,screenHeight);
		this.setTitle("Cubic Storehouse System:made by LiWanwan");
		this.setResizable(false);
		this.setVisible(true);
		this.messageBox.start();
		this.mySQLprocessor=new MySQLprocessor("CubicStorehouse");
		this.getStorehouseInfo();
		this.addStorehouseCanvas3DPanel();
		this.addStorehouseScriptPanel();
		this.addStorehouseInfoPanel();
		this.addMerchandiseInfoPanel();
		this.addMerchandiseLocationInfoPanel();
		this.addCarrierRouteInfoPanel();
		this.addMenu();
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	private void addMenu()
	{
		this.menuItem_NewScriptFile=new MenuItem("New Script File");
		this.menuItem_NewScriptFile.addActionListener(this);
		this.menuShortcut_N=new MenuShortcut(KeyEvent.VK_N); 
		this.menuItem_NewScriptFile.setShortcut(menuShortcut_N);
		this.menuItem_OpenScriptFile=new MenuItem("Open Script File");
		this.menuItem_OpenScriptFile.addActionListener(this);
		this.menuShortcut_O=new MenuShortcut(KeyEvent.VK_O); 
		this.menuItem_OpenScriptFile.setShortcut(menuShortcut_O);
		this.menuItem_EditScriptFile=new MenuItem("Edit Script File");
		this.menuItem_EditScriptFile.addActionListener(this);
		this.menuShortcut_E=new MenuShortcut(KeyEvent.VK_E); 
		this.menuItem_EditScriptFile.setShortcut(menuShortcut_E);
		this.menuItem_SaveScriptFile=new MenuItem("Save Script File");
		this.menuItem_SaveScriptFile.addActionListener(this);
		this.menuShortcut_S=new MenuShortcut(KeyEvent.VK_S); 
		this.menuItem_SaveScriptFile.setShortcut(menuShortcut_S);
		this.menuItem_RunScriptFile=new MenuItem("Run Script File");
		this.menuItem_RunScriptFile.addActionListener(this);
		this.menuShortcut_R=new MenuShortcut(KeyEvent.VK_R); 
		this.menuItem_RunScriptFile.setShortcut(menuShortcut_R);
		this.menuItem_DisplayStorehouse3D=new MenuItem("Display Storehouse3D");
		this.menuShortcut_D=new MenuShortcut(KeyEvent.VK_D); 
		this.menuItem_DisplayStorehouse3D.setShortcut(menuShortcut_D);
		this.menuItem_DisplayStorehouse3D.addActionListener(this);
		this.menuItem_HideStorehouse3D=new MenuItem("Hide Storehouse3D");
		this.menuShortcut_H=new MenuShortcut(KeyEvent.VK_H); 
		this.menuItem_HideStorehouse3D.setShortcut(menuShortcut_H);
		this.menuItem_HideStorehouse3D.addActionListener(this);	
		this.menuItem_SetStorehouseInfo=new MenuItem("Set Storehouse Info");
		this.menuItem_SetStorehouseInfo.addActionListener(this);	
		this.CheckboxMenuItem1=new CheckboxMenuItem("CheckboxMenuItem1");		
		this.CheckboxMenuItem1.addItemListener(this);
		this.menuItem_SetMerchandiseInfo=new MenuItem("Set Merchandise Info");
		this.menuItem_SetMerchandiseInfo.addActionListener(this);
		this.menuItem_SetMerchandiseLocation=new MenuItem("Set Merchandise Location");
		this.menuItem_SetMerchandiseLocation.addActionListener(this);
		this.menuItem_getCarrierRoute=new MenuItem("Get Carrier's Route ");
		this.menuItem_getCarrierRoute.addActionListener(this);
		this.menu_StorehouseScript=new Menu("StorehouseScript");
		this.menu_StorehouseScript.add(menuItem_NewScriptFile);
		this.menu_StorehouseScript.add(menuItem_OpenScriptFile);
		this.menu_StorehouseScript.add(menuItem_SaveScriptFile);
		this.menu_StorehouseScript.add(menuItem_EditScriptFile);
		this.menu_StorehouseScript.add(menuItem_RunScriptFile);
		this.menu_Storehouse=new Menu("Storehouse");
		this.menu_Storehouse.add(menuItem_DisplayStorehouse3D);
		this.menu_Storehouse.add(menuItem_HideStorehouse3D);
		this.menu_Storehouse.addSeparator();
		this.menu_Storehouse.add(menuItem_SetStorehouseInfo);
		this.menu_Storehouse.add(CheckboxMenuItem1);
		this.menu_Merchandise=new Menu("Merchandise");
		this.menu_Merchandise.add(menuItem_SetMerchandiseInfo);
		this.menu_Merchandise.addSeparator();
		this.menu_Merchandise.add(menuItem_SetMerchandiseLocation);
		this.menu_Transaction=new Menu("Transaction");
		this.menu_Transaction.add(menuItem_getCarrierRoute);
		this.menuBar=new MenuBar();
		this.menuBar.add(menu_StorehouseScript);		
		this.menuBar.add(menu_Storehouse);
		this.menuBar.add(menu_Merchandise);
		this.menuBar.add(menu_Transaction);
		this.setMenuBar(menuBar);
	}
	private void addStorehouseCanvas3DPanel()
	{
		GraphicsConfiguration graphicsConfiguration1=SimpleUniverse.getPreferredConfiguration();
		this.storehouseCanvas3D=new Canvas3D(graphicsConfiguration1);
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(storehouseCanvas3D);
		SimpleUniverse1.addBranchGraph(this.getBranchGraph());
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		this.panel_StorehouseCanvas3D=new Panel();
		this.panel_StorehouseCanvas3D.setLayout(new BorderLayout());
		this.panel_StorehouseCanvas3D.add(storehouseCanvas3D,BorderLayout.CENTER);
		panel_StorehouseCanvas3D.setBounds(0,0,screenWidth,screenHeight);
		this.panel_StorehouseCanvas3D.setVisible(false);
		this.add(panel_StorehouseCanvas3D);
	}
	private void addStorehouseScriptPanel()
	{
		int x0=screenWidth/60;
		int y0=screenHeight/20;
		int scriptWidth=screenWidth/5;
		this.panel_StorehouseScript=new Panel();
		this.panel_StorehouseScript.setLayout(null);
		this.panel_StorehouseScript.setBounds(0,0,screenWidth,screenHeight);
		this.storehouseScriptPanel=new StorehouseScriptPanel(x0,y0,scriptWidth,screenHeight-y0*2,mySQLprocessor);
		this.storehouse3DPanel=new Storehouse3DPanel(x0+scriptWidth+1,y0,screenWidth-scriptWidth-2*x0,screenHeight-y0*2,mySQLprocessor);
		this.storehouseScriptPanel.setStorehouse3DPanel(storehouse3DPanel);
		this.panel_StorehouseScript.add(storehouseScriptPanel);
		this.panel_StorehouseScript.add(this.storehouse3DPanel);
		this.add(panel_StorehouseScript);
	}
	private void addStorehouseInfoPanel()
	{
		int c=0;
		int controlWidth=screenWidth/5;
		int controlHeight=screenHeight/35;
		int startX=screenWidth/20;
		int startY=screenHeight/20;
		Label Label1=new Label("How many Rows of Shelves in Storehouse:");
		Label1.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_storehouseRow=new TextField(storehouseRow+"");
		this.textField_storehouseRow.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		Label Label2=new Label("How many Columns of Shelves in Storehouse:");
		Label2.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_storehouseColumn=new TextField(storehouseColumn+"");
		this.textField_storehouseColumn.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		Label Label3=new Label("How many Columns of each Shelf:");
		Label3.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_shelfColumn=new TextField(shelfColumn+"");
		this.textField_shelfColumn.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		Label Label4=new Label("How many Levels of each Shelf:");
		Label4.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_shelfLevel=new TextField(shelfLevel+"");
		this.textField_shelfLevel.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_SetStorehouseInfo=new Button("Set Storehouse Info");
		this.button_SetStorehouseInfo.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_SetStorehouseInfo.addActionListener(this);
		Label Label5=new Label("Create Regions for the Storehouse:");
		Label5.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_storehouseRegion=new TextField("Region"+regionCounter);
		this.textField_storehouseRegion.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_SetStorehouseRegionInfo=new Button("Create Region for Storehouse");
		this.button_SetStorehouseRegionInfo.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_SetStorehouseRegionInfo.addActionListener(this);
		this.button_ShowStorehouseRegions=new Button("Show The Storehouse Regions");
		this.button_ShowStorehouseRegions.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_ShowStorehouseRegions.addActionListener(this);
		this.button_HideStorehouseRegions=new Button("Hide The Storehouse Regions");
		this.button_HideStorehouseRegions.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_HideStorehouseRegions.addActionListener(this);
		this.button_ResetStorehouseRegions=new Button("Reset The Storehouse Regions");
		this.button_ResetStorehouseRegions.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_ResetStorehouseRegions.addActionListener(this);
		int x0=2*startX+controlWidth,x1=screenWidth-startX,y0=startY,y1=screenHeight-startY;
		this.storehouseRegionPanel_StorehouseInfo=new StorehouseRegionPanel(x1-x0,y1-y0,storehouseRow,storehouseColumn,shelfColumn);
		this.storehouseRegionPanel_StorehouseInfo.setBounds(x0,y0,x1-x0,y1-y0);
		this.panel_StorehouseInfo=new Panel();
		this.panel_StorehouseInfo.setLayout(null);
		this.panel_StorehouseInfo.setBounds(0,0,screenWidth,screenHeight);
		this.panel_StorehouseInfo.setVisible(false);
		this.panel_StorehouseInfo.add(Label1);
		this.panel_StorehouseInfo.add(textField_storehouseRow);
		this.panel_StorehouseInfo.add(Label2);
		this.panel_StorehouseInfo.add(textField_storehouseColumn);
		this.panel_StorehouseInfo.add(Label3);
		this.panel_StorehouseInfo.add(textField_shelfColumn);
		this.panel_StorehouseInfo.add(Label4);
		this.panel_StorehouseInfo.add(textField_shelfLevel);
		this.panel_StorehouseInfo.add(button_SetStorehouseRegionInfo);
		this.panel_StorehouseInfo.add(Label5);
		this.panel_StorehouseInfo.add(textField_storehouseRegion);
		this.panel_StorehouseInfo.add(button_ShowStorehouseRegions);
		this.panel_StorehouseInfo.add(button_HideStorehouseRegions);
		this.panel_StorehouseInfo.add(button_ResetStorehouseRegions);
		this.panel_StorehouseInfo.add(button_SetStorehouseInfo);
		this.panel_StorehouseInfo.add(storehouseRegionPanel_StorehouseInfo);
		this.add(panel_StorehouseInfo);
	}
	private void addMerchandiseInfoPanel()
	{
		int c=0;
		int controlWidth=screenWidth/6;
		int controlHeight=screenHeight/32;
		int startX=screenWidth/30;
		int startY=screenHeight/20;
		Label Label1=new Label("Please Input Merchandise Name:");
		Label1.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_merchandiseName=new TextField();
		this.textField_merchandiseName.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		Label Label2=new Label("Please Input Merchandise Stocks:");
		Label2.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_merchandiseStocks=new TextField();
		this.textField_merchandiseStocks.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		Label Label3=new Label("Please Input Merchandise Classification:");
		Label3.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_merchandiseClassification=new TextField();
		this.textField_merchandiseClassification.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		Label Label4=new Label("Please Input Merchandise Price:");
		Label4.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_merchandisePrice=new TextField();
		this.textField_merchandisePrice.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		Label Label5=new Label("Please Input Merchandise Description:");
		Label5.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_merchandiseDescription=new TextField();
		this.textField_merchandiseDescription.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_InsertMerchandiseInfo=new Button("Insert Merchandise Info");
		this.button_InsertMerchandiseInfo.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_InsertMerchandiseInfo.addActionListener(this);
		this.button_DeleteMerchandiseInfo=new Button("Delete Merchandise Info");
		this.button_DeleteMerchandiseInfo.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_DeleteMerchandiseInfo.addActionListener(this);
		this.button_UpdateMerchandiseInfo=new Button("Update Merchandise Info");
		this.button_UpdateMerchandiseInfo.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_UpdateMerchandiseInfo.addActionListener(this);
		this.button_SelectMerchandiseInfo=new Button("Select Merchandise Info");
		this.button_SelectMerchandiseInfo.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_SelectMerchandiseInfo.addActionListener(this);
		this.button_SelectSQLMerchandiseInfo=new Button("Select Merchandise Info With SQL");
		this.button_SelectSQLMerchandiseInfo.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_SelectSQLMerchandiseInfo.addActionListener(this);
		int x0=2*startX+controlWidth,x1=screenWidth-startX,y0=startY,y1=screenHeight-startY;
		this.merchandiseInfoPanel=new MerchandiseInfoPanel(x1-x0,y1-y0,mySQLprocessor);
		this.merchandiseInfoPanel.setBounds(x0,y0,x1-x0,y1-y0);
		this.panel_MerchandiseInfo=new Panel();
		this.panel_MerchandiseInfo.setLayout(null);
		this.panel_MerchandiseInfo.setBounds(0,0,screenWidth,screenHeight);
		this.panel_MerchandiseInfo.setVisible(false);
		this.panel_MerchandiseInfo.add(Label1);
		this.panel_MerchandiseInfo.add(textField_merchandiseName);
		this.panel_MerchandiseInfo.add(Label2);
		this.panel_MerchandiseInfo.add(textField_merchandiseStocks);
		this.panel_MerchandiseInfo.add(Label3);
		this.panel_MerchandiseInfo.add(textField_merchandiseClassification);
		this.panel_MerchandiseInfo.add(Label4);
		this.panel_MerchandiseInfo.add(textField_merchandisePrice);
		this.panel_MerchandiseInfo.add(Label5);
		this.panel_MerchandiseInfo.add(textField_merchandiseDescription);
		this.panel_MerchandiseInfo.add(button_InsertMerchandiseInfo);
		this.panel_MerchandiseInfo.add(button_DeleteMerchandiseInfo);
		this.panel_MerchandiseInfo.add(button_UpdateMerchandiseInfo);
		this.panel_MerchandiseInfo.add(button_SelectMerchandiseInfo);
		this.panel_MerchandiseInfo.add(button_SelectSQLMerchandiseInfo);
		this.panel_MerchandiseInfo.add(merchandiseInfoPanel);
		this.add(panel_MerchandiseInfo);
	}
	private void addMerchandiseLocationInfoPanel()
	{
		int c=0;
		int controlWidth=screenWidth/4;
		int controlHeight=screenHeight/40;
		int startX=screenWidth/30;
		int startY=screenHeight/20;
		Label Label1=new Label("Please Input Merchandise Name:");
		Label1.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_merchandiseLocationName=new TextField();
		this.textField_merchandiseLocationName.setBounds(startX,startY+c*2*controlHeight,controlWidth/2,controlHeight);
		this.button_selectMerchandiseLocationID=new Button("Select Merchandise ID ");
		this.button_selectMerchandiseLocationID.setBounds(startX+controlWidth/2,startY+(c++)*2*controlHeight,controlWidth/2,controlHeight);
		this.button_selectMerchandiseLocationID.addActionListener(this);
		Label Label2=new Label("Merchandise ID:");
		Label2.setBounds(startX,startY+c*2*controlHeight,controlWidth/2,controlHeight);
		this.choice_merchandiseLocationID=new Choice();
		this.choice_merchandiseLocationID.setBounds(startX+controlWidth/2,startY+(c++)*2*controlHeight,controlWidth/2,controlHeight);
		this.choice_merchandiseLocationID.addItemListener(this);
		Label Label3=new Label("Merchandise Location Info:");
		Label3.setBounds(startX,startY+c*2*controlHeight,controlWidth/2,controlHeight);
		this.button_setMerchandiseLocationByMouse=new Button("Set  By  Mouse");
		this.button_setMerchandiseLocationByMouse.setBounds(startX+controlWidth/2,startY+(c++)*2*controlHeight,controlWidth/2,controlHeight);
		this.button_setMerchandiseLocationByMouse.addActionListener(this);
		Label Label4=new Label("storehouseRow                              storehouseColumn");
		Label4.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_merchandiseLocation_storehouseRow=new TextField();
		this.textField_merchandiseLocation_storehouseRow.setBounds(startX,startY+c*2*controlHeight,controlWidth/2,controlHeight);
		this.textField_merchandiseLocation_storehouseRow.setEditable(false);
		this.textField_merchandiseLocation_storehouseRow.setBackground(Color.white);
		this.textField_merchandiseLocation_storehouseColumn=new TextField();
		this.textField_merchandiseLocation_storehouseColumn.setBounds(startX+controlWidth/2,startY+(c++)*2*controlHeight,controlWidth/2,controlHeight);
		this.textField_merchandiseLocation_storehouseColumn.setEditable(false);
		this.textField_merchandiseLocation_storehouseColumn.setBackground(Color.white);
		Label Label5=new Label("shelfColumn                                  shelfLevel");
		Label5.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_merchandiseLocation_shelfColumn=new TextField();
		this.textField_merchandiseLocation_shelfColumn.setBounds(startX,startY+c*2*controlHeight,controlWidth/2,controlHeight);
		this.textField_merchandiseLocation_shelfColumn.setEditable(false);
		this.textField_merchandiseLocation_shelfColumn.setBackground(Color.white);
		this.textField_merchandiseLocation_shelfLevel=new TextField();
		this.textField_merchandiseLocation_shelfLevel.setBounds(startX+controlWidth/2,startY+(c++)*2*controlHeight,controlWidth/2,controlHeight);
		this.textField_merchandiseLocation_shelfLevel.setEditable(false);
		this.textField_merchandiseLocation_shelfLevel.setBackground(Color.white);
		int x0=2*startX+controlWidth,x1=screenWidth-startX,y0=startY,y1=screenHeight-startY;
		this.storehouseRegionPanel_MerchandiseLocationInfo=new StorehouseRegionPanel(x1-x0,y1-y0,storehouseRow,storehouseColumn,shelfColumn);
		this.storehouseRegionPanel_MerchandiseLocationInfo.setBounds(x0,y0,x1-x0,y1-y0);
		this.shelfMerchandiseInfoPanel_MerchandiseLocationInfo=new ShelfMerchandiseInfoPanel(controlWidth,19*controlHeight,shelfColumn,shelfLevel,mySQLprocessor);
		this.shelfMerchandiseInfoPanel_MerchandiseLocationInfo.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,19*controlHeight);
		this.storehouseRegionPanel_MerchandiseLocationInfo.setShelfMerchandiseInfoPanel(shelfMerchandiseInfoPanel_MerchandiseLocationInfo);
		this.button_setMerchandiseLocationInfo=new Button("Set Merchandise Location Info");
		this.button_setMerchandiseLocationInfo.setBounds(startX,startY+(c*2+18)*controlHeight-controlHeight/2,controlWidth,controlHeight);
		this.button_setMerchandiseLocationInfo.addActionListener(this);
		this.panel_MerchandiseLocationInfo=new Panel();
		this.panel_MerchandiseLocationInfo.setLayout(null);
		this.panel_MerchandiseLocationInfo.setBounds(0,0,screenWidth,screenHeight);
		this.panel_MerchandiseLocationInfo.setVisible(false);
		this.panel_MerchandiseLocationInfo.add(Label1);
		this.panel_MerchandiseLocationInfo.add(textField_merchandiseLocationName);
		this.panel_MerchandiseLocationInfo.add(button_selectMerchandiseLocationID);
		this.panel_MerchandiseLocationInfo.add(Label2);
		this.panel_MerchandiseLocationInfo.add(choice_merchandiseLocationID);
		this.panel_MerchandiseLocationInfo.add(Label3);
		this.panel_MerchandiseLocationInfo.add(button_setMerchandiseLocationByMouse);
		this.panel_MerchandiseLocationInfo.add(Label4);
		this.panel_MerchandiseLocationInfo.add(textField_merchandiseLocation_storehouseRow);
		this.panel_MerchandiseLocationInfo.add(textField_merchandiseLocation_storehouseColumn);
		this.panel_MerchandiseLocationInfo.add(Label5);
		this.panel_MerchandiseLocationInfo.add(textField_merchandiseLocation_shelfColumn);
		this.panel_MerchandiseLocationInfo.add(textField_merchandiseLocation_shelfLevel);
		this.panel_MerchandiseLocationInfo.add(storehouseRegionPanel_MerchandiseLocationInfo);
		this.panel_MerchandiseLocationInfo.add(button_setMerchandiseLocationInfo);
		this.panel_MerchandiseLocationInfo.add(shelfMerchandiseInfoPanel_MerchandiseLocationInfo);
		this.add(panel_MerchandiseLocationInfo);
	}
	private void addCarrierRouteInfoPanel()
	{
		int c=0;
		int controlWidth=screenWidth/4;
		int controlHeight=screenHeight/40;
		int startX=screenWidth/30;
		int startY=screenHeight/20;
		Label Label1=new Label("Please Input The List Of Merchandise Names");
		Label1.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textArea_MerchandiseOrderList=new TextArea();
		this.textArea_MerchandiseOrderList.setBounds(startX,startY+c*2*controlHeight,controlWidth/4,10*controlHeight);
		this.textArea_CarrierRoute=new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		this.textArea_CarrierRoute.setEditable(false);
		this.textArea_CarrierRoute.setBackground(Color.white);
		this.textArea_CarrierRoute.setBounds(startX+controlWidth/4,startY+c*2*controlHeight,3*controlWidth/4,10*controlHeight);
		c+=6;
		Label Label2=new Label("Please Input Carrier's Start Location Of the Storehouse:");
		Label2.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		Label Label3=new Label("Start Row:              Start Column:");
		Label3.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.textField_startRow=new TextField();
		this.textField_startRow.setBounds(startX,startY+c*2*controlHeight,controlWidth/4,controlHeight);
		this.textField_startColumn=new TextField();
		this.textField_startColumn.setBounds(startX+controlWidth/4,startY+c*2*controlHeight,controlWidth/4,controlHeight);
		this.button_setSartLocationByMouse=new Button("Set By Mouse");
		this.button_setSartLocationByMouse.setBounds(startX+controlWidth/2,startY+(c++)*2*controlHeight,controlWidth/2,controlHeight);
		this.button_setSartLocationByMouse.addActionListener(this);
		this.button_setStartLocation=new Button("Set Carrier's Start Location");
		this.button_setStartLocation.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_setStartLocation.addActionListener(this);
		this.button_getCarrierRoute=new Button("Get And Show Carrier's Route");
		this.button_getCarrierRoute.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_getCarrierRoute.addActionListener(this);
		this.button_hideCarrierRoute=new Button("Hide Carrier's Route");
		this.button_hideCarrierRoute.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,controlHeight);
		this.button_hideCarrierRoute.addActionListener(this);
		int x0=2*startX+controlWidth,x1=screenWidth-startX,y0=startY,y1=screenHeight-startY;
		this.storehouseRegionPanel_CarrierRouteInfo=new StorehouseRegionPanel(x1-x0,y1-y0,storehouseRow,storehouseColumn,shelfColumn);
		this.storehouseRegionPanel_CarrierRouteInfo.setBounds(x0,y0,x1-x0,y1-y0);
		this.shelfMerchandiseInfoPanel_CarrierRouteInfo=new ShelfMerchandiseInfoPanel(controlWidth,10*controlHeight,shelfColumn,shelfLevel,mySQLprocessor);
		this.shelfMerchandiseInfoPanel_CarrierRouteInfo.setBounds(startX,startY+(c++)*2*controlHeight,controlWidth,10*controlHeight);
		this.storehouseRegionPanel_CarrierRouteInfo.setShelfMerchandiseInfoPanel(shelfMerchandiseInfoPanel_CarrierRouteInfo);
		this.panel_CarrierRouteInfo=new Panel();
		this.panel_CarrierRouteInfo.setLayout(null);
		this.panel_CarrierRouteInfo.setBounds(0,0,screenWidth,screenHeight);
		this.panel_CarrierRouteInfo.setVisible(false);
		this.panel_CarrierRouteInfo.add(Label1);
		this.panel_CarrierRouteInfo.add(textArea_MerchandiseOrderList);
		this.panel_CarrierRouteInfo.add(textArea_CarrierRoute);
		this.panel_CarrierRouteInfo.add(Label2);
		this.panel_CarrierRouteInfo.add(Label3);
		this.panel_CarrierRouteInfo.add(textField_startRow);
		this.panel_CarrierRouteInfo.add(textField_startColumn);
		this.panel_CarrierRouteInfo.add(button_setSartLocationByMouse);
		this.panel_CarrierRouteInfo.add(button_setStartLocation);
		this.panel_CarrierRouteInfo.add(button_getCarrierRoute);
		this.panel_CarrierRouteInfo.add(button_hideCarrierRoute);
		this.panel_CarrierRouteInfo.add(storehouseRegionPanel_CarrierRouteInfo);
		this.panel_CarrierRouteInfo.add(shelfMerchandiseInfoPanel_CarrierRouteInfo);
		this.add(panel_CarrierRouteInfo);
	}
	public void actionPerformed(ActionEvent e)
	{
		Object eSource=e.getSource();
		if(eSource.equals(menuItem_NewScriptFile))
		{
			this.panel_StorehouseScript.setVisible(true);
			this.panel_StorehouseCanvas3D.setVisible(false);
			this.storehouseScriptPanel.newScriptFile();
		}
		if(eSource.equals(menuItem_OpenScriptFile))
		{
			this.panel_StorehouseScript.setVisible(true);
			this.panel_StorehouseCanvas3D.setVisible(false);
			this.storehouseScriptPanel.openScriptFile();
		}
		if(eSource.equals(menuItem_SaveScriptFile))
		{
			this.panel_StorehouseScript.setVisible(true);
			this.panel_StorehouseCanvas3D.setVisible(false);
			this.storehouseScriptPanel.saveScriptFile();
		}
		if(eSource.equals(menuItem_EditScriptFile))
		{
			this.panel_StorehouseScript.setVisible(true);
			this.panel_StorehouseCanvas3D.setVisible(false);
		}
		if(eSource.equals(menuItem_RunScriptFile))
		{
			this.panel_StorehouseScript.setVisible(true);
			this.panel_StorehouseCanvas3D.setVisible(false);
			this.storehouseScriptPanel.runScriptFile();
		}
		if(eSource.equals(menuItem_HideStorehouse3D))
		{
			this.panel_StorehouseCanvas3D.setVisible(false);
		}
		else if(eSource.equals(menuItem_DisplayStorehouse3D))
		{
			this.panel_StorehouseCanvas3D.setVisible(true);
		}
		else if(eSource.equals(menuItem_SetStorehouseInfo))
		{
			this.panel_StorehouseInfo.setVisible(true);
			this.panel_StorehouseCanvas3D.setVisible(false);
			this.panel_StorehouseScript.setVisible(false);
		}
		else if(eSource.equals(menuItem_SetMerchandiseInfo))
		{
			this.panel_MerchandiseInfo.setVisible(true);
			this.panel_StorehouseInfo.setVisible(false);
			this.panel_StorehouseCanvas3D.setVisible(false);
			this.panel_StorehouseScript.setVisible(false);
		}
		else if(eSource.equals(menuItem_SetMerchandiseLocation))
		{
			this.panel_MerchandiseLocationInfo.setVisible(true);
			this.panel_MerchandiseInfo.setVisible(false);
			this.panel_StorehouseInfo.setVisible(false);
			this.panel_StorehouseCanvas3D.setVisible(false);
			this.panel_StorehouseScript.setVisible(false);
		}
		else if(eSource.equals(menuItem_getCarrierRoute))
		{
			this.panel_CarrierRouteInfo.setVisible(true);
			this.panel_MerchandiseLocationInfo.setVisible(false);
			this.panel_MerchandiseInfo.setVisible(false);
			this.panel_StorehouseInfo.setVisible(false);
			this.panel_StorehouseCanvas3D.setVisible(false);
			this.panel_StorehouseScript.setVisible(false);
		}
		else if(eSource.equals(button_SetStorehouseInfo))
		{
			int storehouseRow=5,storehouseColumn=8,shelfColumn=8,shelfLevel=7;
			if(isInteger(textField_storehouseRow.getText()))storehouseRow=Integer.parseInt(textField_storehouseRow.getText());
			else this.textField_storehouseRow.setText(storehouseRow+"");
			if(isInteger(textField_storehouseColumn.getText()))storehouseColumn=Integer.parseInt(textField_storehouseColumn.getText());
			else this.textField_storehouseColumn.setText(storehouseColumn+"");
			if(isInteger(textField_shelfColumn.getText()))shelfColumn=Integer.parseInt(textField_shelfColumn.getText());
			else this.textField_shelfColumn.setText(shelfColumn+"");
			if(isInteger(textField_shelfLevel.getText()))shelfLevel=Integer.parseInt(textField_shelfLevel.getText());
			else this.textField_shelfLevel.setText(shelfLevel+"");
			this.setStorehouseInfo(storehouseRow,storehouseColumn,shelfColumn,shelfLevel);
			this.messageBox=new MessageBox("System should be started again to adopt the modification",700,200,null,null);
			this.messageBox.start();
		}
		else if(eSource.equals(button_SetStorehouseRegionInfo))
		{
			this.regionCounter++;
			String storehouseRegionText=textField_storehouseRegion.getText();
			this.textField_storehouseRegion.setText("Region"+regionCounter);
			int[] rowColumnInfo=storehouseRegionPanel_StorehouseInfo.getRowColumnInfo();
			if(rowColumnInfo[0]==rowColumnInfo[2]&&rowColumnInfo[1]==rowColumnInfo[3])
			{
				this.messageBox=new MessageBox("Please Draw a New Region By Your Mouse!",500,150,null,null);
				this.messageBox.start();
			}
			this.mySQLprocessor.executeUpdate(new String[]{"Delete From StorehouseRegionInfo Where regionName='"+storehouseRegionText+"'","Insert Into StorehouseRegionInfo Values('"+storehouseRegionText+"',"+rowColumnInfo[0]+","+rowColumnInfo[1]+","+rowColumnInfo[2]+","+rowColumnInfo[3]+")"});
			this.storehouseRegionPanel_StorehouseInfo.setStorehouseRegionInfo(mySQLprocessor.executeQuery("Select * From StorehouseRegionInfo"));
			this.storehouseRegionPanel_StorehouseInfo.setDrawStorehouseRegionsEnable(true);
			this.storehouseRegionPanel_StorehouseInfo.clearXY();
		}
		else if(eSource.equals(button_ShowStorehouseRegions))
		{
			this.storehouseRegionPanel_StorehouseInfo.setStorehouseRegionInfo(mySQLprocessor.executeQuery("Select * From StorehouseRegionInfo"));
			this.storehouseRegionPanel_StorehouseInfo.setDrawStorehouseRegionsEnable(true);
			this.storehouseRegionPanel_StorehouseInfo.clearXY();
		}
		else if(eSource.equals(button_HideStorehouseRegions))
		{
			this.storehouseRegionPanel_StorehouseInfo.setDrawStorehouseRegionsEnable(false);
			this.storehouseRegionPanel_StorehouseInfo.clearXY();
		}
		else if(eSource.equals(button_ResetStorehouseRegions))
		{
			this.regionCounter=0;
			this.storehouseRegionPanel_StorehouseInfo.setDrawStorehouseRegionsEnable(false);
			this.storehouseRegionPanel_StorehouseInfo.clearXY();
			this.mySQLprocessor.executeUpdate("Delete From StorehouseRegionInfo");
		}
		else if(eSource.equals(button_InsertMerchandiseInfo))
		{
			String message="";
			String merchandiseName=this.textField_merchandiseName.getText();
			String merchandiseStocks=this.textField_merchandiseStocks.getText();
			if(!isInteger(merchandiseStocks))
			{
				merchandiseStocks="";
				this.textField_merchandiseStocks.setText("");
			}
			String merchandiseClassification=this.textField_merchandiseClassification.getText();
			String merchandisePrice=this.textField_merchandisePrice.getText();
			if(!isFloat(merchandisePrice))
			{
				merchandisePrice="";
				this.textField_merchandisePrice.setText("");
			}
			String merchandiseDescription=this.textField_merchandiseDescription.getText();
			if(merchandiseName.equals(""))message="Merchandise Name ";
			if(merchandiseStocks.equals(""))message="Merchandise Stocks ";
			if(merchandiseClassification.equals(""))message="Merchandise Classification ";
			if(merchandisePrice.equals(""))message="Merchandise Price ";
			if(merchandiseDescription.equals(""))message="Merchandise Description ";
			if(message.equals(""))
			{
				this.mySQLprocessor.executeUpdate("Insert Into MerchandiseInfo (merchandiseName,merchandiseStocks,merchandiseClassification,merchandisePrice,merchandiseDescription) Values('"+merchandiseName+"',"+merchandiseStocks+",'"+merchandiseClassification+"',"+merchandisePrice+",'"+merchandiseDescription+"')");
				this.merchandiseInfoPanel.repaint();
			}
			else 
			{
				this.messageBox=new MessageBox(message+" Should Not Be Empty!",600,100,null,null);
				this.messageBox.start();
			}
		}
		else if(eSource.equals(button_DeleteMerchandiseInfo))
		{
			String message="";
			String merchandiseName=this.textField_merchandiseName.getText();
			if(merchandiseName.equals(""))
			{
				this.messageBox=new MessageBox("Merchandise Name Should Not Be Empty!",600,100,null,null);
				this.messageBox.start();
			}
			else 
			{
				this.mySQLprocessor.executeUpdate("Delete From MerchandiseInfo Where merchandiseName='"+merchandiseName+"'");
				this.merchandiseInfoPanel.repaint();
			}
		}
		else if(eSource.equals(button_DeleteMerchandiseInfo))
		{
			String message="";
			String merchandiseName=this.textField_merchandiseName.getText();
			if(merchandiseName.equals(""))
			{
				this.messageBox=new MessageBox("Merchandise Name Should Not Be Empty!",600,100,null,null);
				this.messageBox.start();
			}
			else 
			{
				this.mySQLprocessor.executeUpdate("Delete From MerchandiseInfo Where merchandiseName='"+merchandiseName+"'");
				this.merchandiseInfoPanel.repaint();
			}
		}
		else if(eSource.equals(button_UpdateMerchandiseInfo))
		{
			String merchandiseName=this.textField_merchandiseName.getText();
			if(merchandiseName.equals(""))
			{
				this.messageBox=new MessageBox("Merchandise Name Should Not Be Empty!",600,100,null,null);
				this.messageBox.start();
				return;
			}
			String merchandiseStocks=this.textField_merchandiseStocks.getText();
			if(!isInteger(merchandiseStocks))
			{
				merchandiseStocks="";
				this.textField_merchandiseStocks.setText("");
			}
			String merchandiseClassification=this.textField_merchandiseClassification.getText();
			String merchandisePrice=this.textField_merchandisePrice.getText();
			if(!isFloat(merchandisePrice))
			{
				merchandisePrice="";
				this.textField_merchandisePrice.setText("");
			}
			String merchandiseDescription=this.textField_merchandiseDescription.getText();
			if(!merchandiseStocks.equals(""))
			{
				this.mySQLprocessor.executeUpdate("Update MerchandiseInfo Set merchandiseStocks="+merchandiseStocks+" Where merchandiseName='"+merchandiseName+"'");
				this.merchandiseInfoPanel.repaint();
			}
			if(!merchandiseClassification.equals(""))
			{
				this.mySQLprocessor.executeUpdate("Update MerchandiseInfo Set merchandiseClassification='"+merchandiseClassification+"' Where merchandiseName='"+merchandiseName+"'");
				this.merchandiseInfoPanel.repaint();
			}
			if(!merchandisePrice.equals(""))
			{
				this.mySQLprocessor.executeUpdate("Update MerchandiseInfo Set merchandisePrice="+merchandisePrice+" Where merchandiseName='"+merchandiseName+"'");
				this.merchandiseInfoPanel.repaint();
			}
			if(!merchandiseDescription.equals(""))
			{
				this.mySQLprocessor.executeUpdate("Update MerchandiseInfo Set merchandiseDescription='"+merchandiseDescription+"' Where merchandiseName='"+merchandiseName+"'");
				this.merchandiseInfoPanel.repaint();
			}
		}
		else if(eSource.equals(button_SelectMerchandiseInfo))
		{
			String whereClause="";
			this.mySQLprocessor.setCondition("");
			String merchandiseName=this.textField_merchandiseName.getText();
			String merchandiseStocks=this.textField_merchandiseStocks.getText();
			if(!isInteger(merchandiseStocks))
			{
				merchandiseStocks="";
				this.textField_merchandiseStocks.setText("");
			}
			String merchandiseClassification=this.textField_merchandiseClassification.getText();
			String merchandisePrice=this.textField_merchandisePrice.getText();
			if(!isFloat(merchandisePrice))
			{
				merchandisePrice="";
				this.textField_merchandisePrice.setText("");
			}
			String merchandiseDescription=this.textField_merchandiseDescription.getText();
			if(!merchandiseName.equals(""))whereClause+=" merchandiseName='"+merchandiseName+"'";
			if(!merchandiseStocks.equals(""))
			{
				if(!whereClause.equals(""))whereClause+=" And";
				whereClause+=" merchandiseStocks="+merchandiseStocks;
			}
			if(!merchandiseClassification.equals(""))
			{
				if(!whereClause.equals(""))whereClause+=" And";
				whereClause+=" merchandiseClassification='"+merchandiseClassification+"'";
			}
			if(!merchandisePrice.equals(""))
			{
				if(!whereClause.equals(""))whereClause+=" And";
				whereClause+=" merchandisePrice="+merchandisePrice;
			}
			if(!merchandiseDescription.equals(""))
			{
				if(!whereClause.equals(""))whereClause+=" And";
				whereClause+=" merchandiseDescription='"+merchandiseDescription+"'";
			}
			this.mySQLprocessor.setCondition(whereClause);
			this.merchandiseInfoPanel.repaint();
		}
		else if(eSource.equals(button_SelectSQLMerchandiseInfo))
		{
			String whereClause="";
			this.mySQLprocessor.setCondition("");
			String merchandiseName=this.textField_merchandiseName.getText();
			String merchandiseStocks=this.textField_merchandiseStocks.getText();
			String merchandiseClassification=this.textField_merchandiseClassification.getText();
			String merchandisePrice=this.textField_merchandisePrice.getText();
			String merchandiseDescription=this.textField_merchandiseDescription.getText();
			if(!merchandiseName.equals(""))whereClause+=" merchandiseName"+merchandiseName;
			if(!merchandiseStocks.equals(""))
			{
				if(!whereClause.equals(""))whereClause+=" And";
				whereClause+=" merchandiseStocks"+merchandiseStocks;
			}
			if(!merchandiseClassification.equals(""))
			{
				if(!whereClause.equals(""))whereClause+=" And";
				whereClause+=" merchandiseClassification"+merchandiseClassification;
			}
			if(!merchandisePrice.equals(""))
			{
				if(!whereClause.equals(""))whereClause+=" And";
				whereClause+=" merchandisePrice"+merchandisePrice;
			}
			if(!merchandiseDescription.equals(""))
			{
				if(!whereClause.equals(""))whereClause+=" And";
				whereClause+=" merchandiseDescription"+merchandiseDescription;
			}
			this.mySQLprocessor.setCondition(whereClause);
			this.merchandiseInfoPanel.repaint();
		}
		else if(eSource.equals(button_selectMerchandiseLocationID))
		{
			String merchandiseName=this.textField_merchandiseLocationName.getText();
			if(merchandiseName.equals(""))
			{
				this.messageBox=new MessageBox("Merchandise Name Should Not Be Empty!",600,100,null,null);
				this.messageBox.start();
				return;
			}
			else if(merchandiseName.equals("null"))
			{
				this.choice_merchandiseLocationID.removeAll();
				this.choice_merchandiseLocationID.add("-1");
				return;
			}
			this.choice_merchandiseLocationID.removeAll();
			ResultSet ResultSet1=mySQLprocessor.executeQuery("Select merchandiseID From MerchandiseInfo Where  merchandiseName='"+merchandiseName+"'");
			try
			{
				while(ResultSet1.next())this.choice_merchandiseLocationID.add(ResultSet1.getString("merchandiseID"));
			}
			catch(Exception ex){ex.printStackTrace();}
		}
		else if(eSource.equals(button_setMerchandiseLocationByMouse))
		{
			this.textField_merchandiseLocation_storehouseRow.setText(storehouseRegionPanel_MerchandiseLocationInfo.getCurrentRow()+"");
			this.textField_merchandiseLocation_storehouseColumn.setText(storehouseRegionPanel_MerchandiseLocationInfo.getCurrentColumn()+"");
			this.textField_merchandiseLocation_shelfColumn.setText(shelfMerchandiseInfoPanel_MerchandiseLocationInfo.getCurrentColumn()+"");
			this.textField_merchandiseLocation_shelfLevel.setText(shelfMerchandiseInfoPanel_MerchandiseLocationInfo.getCurrentLevel()+"");
		}
		else if(eSource.equals(button_setMerchandiseLocationInfo))
		{
			String message="";
			String merchandiseID=this.choice_merchandiseLocationID.getSelectedItem();
			String storehouseRow=this.textField_merchandiseLocation_storehouseRow.getText();
			String storehouseColumn=this.textField_merchandiseLocation_storehouseColumn.getText();
			String shelfColumn=this.textField_merchandiseLocation_shelfColumn.getText();
			String shelfLevel=this.textField_merchandiseLocation_shelfLevel.getText();
			if(storehouseRow.equals("-1")||!isInteger(storehouseRow))this.textField_merchandiseLocation_storehouseRow.setText("");
			if(storehouseColumn.equals("-1")||!isInteger(storehouseColumn))this.textField_merchandiseLocation_storehouseColumn.setText("");
			if(shelfColumn.equals("-1")||!isInteger(shelfColumn))this.textField_merchandiseLocation_shelfColumn.setText("");
			if(shelfLevel.equals("-1")||!isInteger(shelfLevel))this.textField_merchandiseLocation_shelfLevel.setText("");
			storehouseRow=this.textField_merchandiseLocation_storehouseRow.getText();
			storehouseColumn=this.textField_merchandiseLocation_storehouseColumn.getText();
			shelfColumn=this.textField_merchandiseLocation_shelfColumn.getText();
			shelfLevel=this.textField_merchandiseLocation_shelfLevel.getText();
			if(merchandiseID==null)message="Merchandise ID";
			else if(storehouseRow.equals(""))message="Storehouse Row";
			else if(storehouseColumn.equals(""))message="Storehouse Column";
			else if(shelfColumn.equals(""))message="Shelf Column";
			else if(shelfLevel.equals(""))message="Shelf Level";
			if(!message.equals(""))
			{
				this.messageBox=new MessageBox(message+" Should Not Be Empty!",600,100,null,null);
				this.messageBox.start();
				return;
			}
			else
			{
				String structuredQueryLanguage;
				if(merchandiseID.equals("-1"))mySQLprocessor.executeUpdate("Delete From MerchandiseLocationInfo Where storehouseRow="+storehouseRow+" And storehouseColumn="+storehouseColumn+" And shelfColumn="+shelfColumn+" And  shelfLevel="+shelfLevel);
				else mySQLprocessor.executeUpdate(new String[]
				{
					"Delete From MerchandiseLocationInfo Where merchandiseID="+merchandiseID,
					"Delete From MerchandiseLocationInfo Where storehouseRow="+storehouseRow+" And storehouseColumn="+storehouseColumn+" And shelfColumn="+shelfColumn+" And shelfLevel="+shelfLevel,
					"Insert Into MerchandiseLocationInfo Values ("+merchandiseID+","+storehouseRow+","+storehouseColumn+","+shelfColumn+","+shelfLevel+")"
				});
				this.storehouseRegionPanel_MerchandiseLocationInfo.repaintShelfMerchandiseInfoPanel();
			}
		}
		else if(eSource.equals(button_setSartLocationByMouse))
		{
			this.textField_startRow.setText(storehouseRegionPanel_CarrierRouteInfo.getCurrentRow()+"");
			this.textField_startColumn.setText(storehouseRegionPanel_CarrierRouteInfo.getCurrentColumn()+"");
		}
		else if(eSource.equals(button_setStartLocation))
		{
			String message="";
			String startRow=this.textField_startRow.getText();
			String startColumn=this.textField_startColumn.getText();
			if(startRow.equals("-1")||!isInteger(startRow)){this.textField_startRow.setText("");startRow="";}
			if(startColumn.equals("-1")||!isInteger(startColumn)){this.textField_startColumn.setText("");startColumn="";}
			if(startRow.equals(""))message="Start Row";
			if(startColumn.equals(""))message="Start Column";
			if(message.equals(""))
			{
				this.startRow=Integer.parseInt(startRow);
				this.startColumn=Integer.parseInt(startColumn);
			}
		}
		else if(eSource.equals(button_getCarrierRoute))
		{
			String routes="";
			String[] merchandiseOrderList=this.getStringArray(this.textArea_MerchandiseOrderList.getText()+"\r\n");
			int[][] merchandiseLocationInfo=this.getMerchandiseLocationInfo(merchandiseOrderList);
			int[][] carrierRoute=this.getCarrierRoute(merchandiseLocationInfo);
			if(carrierRoute==null)return;
			int l=carrierRoute.length;
			this.storehouseRegionPanel_CarrierRouteInfo.setCarrierRoute(carrierRoute);
			for(int i=0;i<l;i++)routes+="Merchandise()\r\n{\r\n  merchandiseID="+carrierRoute[i][0]+";\r\n  storehouseRow="+carrierRoute[i][1]+";\r\n  storehouseColumn="+carrierRoute[i][2]+";\r\n  shelfColumn="+carrierRoute[i][3]+";\r\n  shelfLevel="+carrierRoute[i][4]+";\r\n  Sum="+carrierRoute[i][5]+";\r\n}\r\n";
			this.textArea_CarrierRoute.setText(routes);
		}
		else if(eSource.equals(button_hideCarrierRoute))
		{
			this.storehouseRegionPanel_CarrierRouteInfo.setDrawCarrierRouteEnable(false);
		}
	}
	private int[][] getMerchandiseLocationInfo(String[] merchandiseOrderList)
	{
		int c=6;
		int l=merchandiseOrderList.length;
		StringQueue queue=new StringQueue();
		for(int i=0;i<l;i++)
		{
			String[] orderList=this.getSeparatedStrings(merchandiseOrderList[i],'*');
			if(orderList!=null&&isInteger(orderList[1]))
			{
				int totalSum=Integer.parseInt(orderList[1]);
				int[][] singleMerchandiseLocationInfo=this.getSingleMerchandiseLocationInfo(orderList[0],totalSum);
				if(singleMerchandiseLocationInfo==null)return null;
				int r=singleMerchandiseLocationInfo.length;
				for(int m=0;m<r&&singleMerchandiseLocationInfo[m][0]!=-1;m++)
				{
					for(int n=0;n<c;n++)queue.enQueue(singleMerchandiseLocationInfo[m][n]+"");
				}
			}
			else 
			{
				this.messageBox=new MessageBox("Merchandise Sum Should Must Be An Integer!",600,100,null,null);
				this.messageBox.start();
				return null;
			}
		}
		l=queue.length();
		int[][] merchandiseLocationInfo=new int[l/6+1][6];
		merchandiseLocationInfo[0]=new int[]{-1,startRow,startColumn,0,0,0};
		for(int i=0;i<l/6;i++)
		{
			for(int j=0;j<6;j++)
			{
				merchandiseLocationInfo[i+1][j]=Integer.parseInt(queue.deQueue());
			}
		}
		return merchandiseLocationInfo;
	}
	private int[][] getSingleMerchandiseLocationInfo(String name,int totalSum)
	{
		final int INF=11235813;
		String[] columns=new String[]{"merchandiseID","storehouseRow","storehouseColumn","shelfColumn","shelfLevel","merchandiseStocks"};
		String sql="Select MerchandiseLocationInfo.merchandiseID,storehouseRow,storehouseColumn,shelfColumn,shelfLevel,merchandiseStocks From MerchandiseLocationInfo,MerchandiseInfo Where MerchandiseLocationInfo.merchandiseID=MerchandiseInfo.merchandiseID And merchandiseName='"+name+"'";
		int[][] result=mySQLprocessor.executeQuery(columns,sql);
		if(result==null||result.length==0)
		{
			this.messageBox=new MessageBox("Merchandise Does Not Exist!",400,100,null,null);
			this.messageBox.start();
			return null;
		}
		int r=result.length;
		int c=result[0].length;
		int[][] merchandiseIDs=new int[r][c];
		int p=0;
		while(totalSum>0)
		{
			int minI=0;
			int minDistance=INF;
			for(int i=0;i<r;i++)
			{
				int distance=abs(startRow-result[i][1])+abs(startColumn-result[i][2]);
				if(result[i][0]!=-1&&distance<minDistance)
				{
					minI=i;
					minDistance=distance;
				}
			}
			if(minDistance==INF)
			{
				this.messageBox=new MessageBox("Merchandise Sum Should Must Be Over Flow!",600,100,null,null);
				this.messageBox.start();
				return null;
			}
			for(int j=0;j<c-1;j++)merchandiseIDs[p][j]=result[minI][j];
			if(totalSum>result[minI][c-1])
			{
				merchandiseIDs[p][c-1]=result[minI][c-1];
				totalSum-=result[minI][c-1];
			}
			else
			{
				merchandiseIDs[p][c-1]=totalSum;
				totalSum=0;
			}
			result[minI][0]=-1;
			p++;
		}
		if(p<r)merchandiseIDs[p][0]=-1;
		return merchandiseIDs;
	}
	public void itemStateChanged(ItemEvent e)
	{
		if(e.getItemSelectable()==choice_merchandiseLocationID)
		{
			String merchandiseID=this.choice_merchandiseLocationID.getSelectedItem();
			ResultSet ResultSet1=mySQLprocessor.executeQuery("Select * From MerchandiseLocationInfo Where merchandiseID="+merchandiseID);
			try
			{
				if(ResultSet1.next())
				{
					this.storehouseRegionPanel_MerchandiseLocationInfo.setCurrentRowColumn(ResultSet1.getInt("storehouseRow"),ResultSet1.getInt("storehouseColumn"));
					this.shelfMerchandiseInfoPanel_MerchandiseLocationInfo.setCurrentColumnLevel(ResultSet1.getInt("shelfColumn"),ResultSet1.getInt("shelfLevel"));
					this.storehouseRegionPanel_MerchandiseLocationInfo.repaint();
					this.shelfMerchandiseInfoPanel_MerchandiseLocationInfo.repaint();
				}
			}
			catch(Exception ex){ex.printStackTrace();}
		}
	}
	private BranchGroup getBranchGraph()
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),3000);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f lightDirection=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,lightDirection);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		TransformGroup1.addChild(new Storehouse3D(storehouseRow,storehouseColumn,shelfColumn,shelfLevel,mySQLprocessor));
		BranchGroup1.compile();
		return BranchGroup1;
	}
	private void setStorehouseInfo(int storehouseRow,int storehouseColumn,int shelfColumn,int shelfLevel)
	{
		this.mySQLprocessor.executeUpdate("Update StorehouseInfo Set storehouseRow="+storehouseRow+",storehouseColumn="+storehouseColumn+",shelfColumn="+shelfColumn+",shelfLevel="+shelfLevel);
	}
	private void getStorehouseInfo()
	{
		this.storehouseRow=8;
		this.storehouseColumn=5;
		this.shelfColumn=5;
		this.shelfLevel=7;
		ResultSet ResultSet1=mySQLprocessor.executeQuery("Select * From StorehouseInfo");
		try
		{
			if(ResultSet1.next())
			{
				this.storehouseRow=ResultSet1.getInt("storehouseRow");
				this.storehouseColumn=ResultSet1.getInt("storehouseColumn");
				this.shelfColumn=ResultSet1.getInt("shelfColumn");
				this.shelfLevel=ResultSet1.getInt("shelfLevel");
			}
		}
		catch(Exception e){e.printStackTrace();}
		this.intervalWidth=storehouseWidth/(3*storehouseColumn);
		this.intervalHeight=storehouseHeight/(3*storehouseRow);
		this.shelfWidth=storehouseWidth/storehouseColumn-intervalWidth;
		this.shelfHeight=storehouseHeight/storehouseRow-intervalHeight;
		this.shelfGridWidth=shelfWidth/shelfColumn;
	}
	private boolean isInteger(String s)
	{
		int length=s.length();
		if(length==0)return false;
		for(int i=0;i<length;i++)
		{
			char c=s.charAt(i);
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
	private int abs(int x)
	{
		return x<0?-x:x;
	}
	private int min(int x,int y)
	{
		return x<=y?x:y;
	}
	private int[][] getCarrierRoute(int[][] location)
	{
		if(location==null)return null;
		final int INF=Integer.MAX_VALUE;
		int l=location.length;
		int[][] route=new int[l][6];
		int[][] distance=new int[l][l];
		for(int i=0;i<l;i++)
		{
			for(int j=0;j<l;j++)
			{
				if(i==j)distance[i][j]=INF;
				else distance[i][j]=this.distance(location[i][1],location[i][2],location[i][3],location[j][1],location[j][2],location[j][3]);
			}
		}
		int[] select=new int[l];
		int current=0;
		int closest=0;
		select[0]=1;
		for(int j=0;j<6;j++)route[0][j]=location[current][j];
		for(int i=1;i<l;i++)
		{
			int min=INF;
			for(int j=1;j<l;j++)
			{
				if(select[j]==0&&distance[current][j]<min)
				{
					closest=j;
					min=distance[current][j];
				}
			}
			current=closest;
			select[current]=1;
			for(int j=0;j<6;j++)route[i][j]=location[current][j];
		}
		return route;
	}
	private int distance(int i0,int j0,int k0,int i1,int j1,int k1)
	{
		int dx=0,dy=0;	
		if(i1!=i0)
		{
			dy=abs(i1-i0)*(shelfHeight+intervalHeight);
			if(j0==j1)dx=min(k0+k1,shelfColumn-1-k0+shelfColumn-1-k1)*shelfGridWidth;
			else if(j0>j1)dx=(j0-j1)*(shelfWidth+intervalWidth)+(k0+shelfColumn-1-k1)*shelfGridWidth;
			else dx=(j1-j0)*(shelfWidth+intervalWidth)+(k1+shelfColumn-1-k0)*shelfGridWidth;
			
		}
		else
		{
			if(j0==j1)dx=abs(k1-k0)*shelfGridWidth;
			else if(j0>j1)dx=(j0-j1)*(shelfWidth+intervalWidth)+(k0+shelfColumn-1-k1)*shelfGridWidth;
			else dx=(j1-j0)*(shelfWidth+intervalWidth)+(k1+shelfColumn-1-k0)*shelfGridWidth;
		}
		return dx+dy;
	}
	private String[] getStringArray(String text)
	{
		int l=text.length();
		int k=0;
		char c;
		for(int i=0;i<l;i++)if((c=text.charAt(i))=='\n')k++;
		String[] stringArray=new String[k];
		String s="";
		int i=0;
		k=0;
		while(i<l)
		{
			c=text.charAt(i++);
			if(c=='\r')c=text.charAt(i++);
			if(c!='\n')s+=c;
			else
			{
				stringArray[k++]=s;
				s="";
			}
		}
		return stringArray;
	}
	private static String[] getSeparatedStrings(String string,char separator)
	{
		int i=0;
		int l=string.length();
		if(i>=l)return null;
		String[] separatedStrings=new String[2];
		separatedStrings[0]=separatedStrings[1]="";
		char Char=string.charAt(i++);
		while(Char!=separator)
		{
			separatedStrings[0]+=Char;
			if(i>=l)break;
			Char=string.charAt(i++);
		}
		while(i<l)
		{
			Char=string.charAt(i++);
			separatedStrings[1]+=Char;
		}
		return separatedStrings;
	}
}
class StorehouseScriptPanel extends Panel implements TextListener
{
	private TextWindow textWindow_InputHint;
	private TextArea textArea_Input,textArea_Lines,textArea_Error;
	private int textInterval=12;
	private int charInterval=6;
	private int numberInterval=20;
	private int x0,y0;
	private int beginIndex=0;
	private int startRow,startColumn,row,column;
	private String errorMessage;
	private MySQLprocessor mySQLprocessor;
	private File recentFile;
	private Storehouse3DPanel storehouse3DPanel;
	public StorehouseScriptPanel(int x0,int y0,int width,int height,MySQLprocessor mySQLprocessor)
	{
		this.x0=x0;
		this.y0=y0;
		this.mySQLprocessor=mySQLprocessor;
		this.textArea_Input=new TextArea("",0,0,textArea_Input.SCROLLBARS_NONE);
		this.textArea_Input.setBounds(numberInterval,0,width-numberInterval-1,4*height/5);
		this.textArea_Input.setBackground(Color.white);
		this.textArea_Input.addTextListener(this);
		this.textArea_Lines=new TextArea(this.getLines(100),0,0,textArea_Input.SCROLLBARS_NONE);
		this.textArea_Lines.setBounds(0,0,numberInterval,4*height/5);
		this.textArea_Lines.setBackground(Color.white);
		this.textArea_Lines.setEditable(false);
		this.textArea_Error=new TextArea("",0,0,textArea_Input.SCROLLBARS_VERTICAL_ONLY);
		this.textArea_Error.setBounds(0,4*height/5,width-1,height/5);
		this.textArea_Error.setBackground(Color.white);
		this.textArea_Error.setEditable(false);
		this.setLayout(null);
		this.add(textArea_Input);
		this.add(textArea_Lines);
		this.add(textArea_Error);
		this.setBounds(x0,y0,width,height);
		this.textWindow_InputHint=new TextWindow();
		this.setFocusable(true);
	}
	public void newScriptFile()
	{
		FileDialog FileDialog1=new FileDialog(new Frame(),"New Storehouse Script File",FileDialog.SAVE);
		FileDialog1.setVisible(true);
		File file=new File(FileDialog1.getDirectory()+FileDialog1.getFile()+".StorehouseScript");
		try
		{
			file.createNewFile();
		}
		catch(Exception e){e.printStackTrace();}
		this.recentFile=file;
	}
	public void openScriptFile()
	{
		FileDialog FileDialog1=new FileDialog(new Frame(),"Open Storehouse Script File",FileDialog.LOAD);
		FileDialog1.setVisible(true);
		File file=new File(FileDialog1.getDirectory()+FileDialog1.getFile());
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(file));
			String text=BufferedReader1.readLine();
			while(text!=null)
			{
				this.textArea_Input.append(text+"\r\n");
				text=BufferedReader1.readLine();
			}
			BufferedReader1.close();
		}
		catch(Exception e){e.printStackTrace();}
		this.recentFile=file;
	}
	public void saveScriptFile()
	{
		if(recentFile==null||!recentFile.exists())
		{
			FileDialog FileDialog1=new FileDialog(new Frame(),"Save Storehouse Script File",FileDialog.SAVE);
			FileDialog1.setVisible(true);
			this.recentFile=new File(FileDialog1.getDirectory()+FileDialog1.getFile());
		}
		try
		{
			PrintWriter PrintWriter1=new PrintWriter(recentFile);
			PrintWriter1.println(this.textArea_Input.getText());
			PrintWriter1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void runScriptFile()
	{
		this.textArea_Error.setText("");
		String[] SQLcommand=this.getSQLcommand();
		if(SQLcommand!=null)
		{
			this.mySQLprocessor.executeUpdate(SQLcommand);
			this.textArea_Error.setText("Script File Has Run Successfully...");
			if(this.storehouse3DPanel!=null)
			{
				this.storehouse3DPanel.getMerchandiseLocationInfo();
				this.storehouse3DPanel.repaint();
			}
		}
		else this.textArea_Error.append("Script File Has Not Run Successfully...");
	}
	private String getLines(int endLine)
	{
		String lines="";
		for(int i=1;i<=endLine;i++)lines+=i+"\r\n";
		return lines;
	}
	public void textValueChanged(TextEvent e)
	{
		String[] texts=this.getStringArray(this.textArea_Input.getText());
		int length=texts.length;
		int startX=x0+texts[length-1].length()*charInterval+numberInterval;
		int startY=y0+length*textInterval;
		String inputText=texts[length-1];
		this.textWindow_InputHint.setPositionAndTexts(startX,startY+25,this.getHintTexts(inputText));
	}
	String[] getHintTexts(String inputText)
	{
		if(inputText==null)return null;
		int l=inputText.length();
		if(l==0)return null;
		if(inputText.charAt(l-1)==';')return null;
		String[] hintTexts=null;
		String[] allHintTexts=this.getAllHintTexts();
		int length=allHintTexts.length;
		StringQueue StringQueue1=new StringQueue();
		for(int i=0;i<length;i++)
		{
			if(allHintTexts[i].startsWith(inputText))StringQueue1.enQueue(allHintTexts[i]);
			if(("this."+allHintTexts[i]).startsWith(inputText)&&inputText.length()>4)StringQueue1.enQueue(allHintTexts[i]);
		}
		String merchandiseName=this.getMidstring(inputText,'=','.');
		if(merchandiseName!=null&&mySQLprocessor!=null)
		{

			ResultSet ResultSet1=mySQLprocessor.executeQuery("Select merchandiseID From MerchandiseInfo Where merchandiseName='"+merchandiseName+"'");
			try
			{
				while(ResultSet1.next())StringQueue1.enQueue(ResultSet1.getString("merchandiseID"));
			}
			catch(Exception e){e.printStackTrace();}
		}
		hintTexts=StringQueue1.getStrings();
		return hintTexts;
	}
	public String[] getSQLcommand()
	{
		String[] texts=this.getStringArray(this.textArea_Input.getText());
		this.startRow=0;
		this.startColumn=0;
		this.row=0;
		this.column=0;
		int length=texts.length;
		String errors="";
		this.errorMessage="";
		StringQueue SQLcommand=new StringQueue();
		for(int i=0;i<length;i++)
		{
			String[] sql=this.convertToSQL(texts[i]);
			if(sql==null||sql.equals(""))
			{
				if(this.errorMessage.equals(""))continue;
				else errors+="There is an error at line:"+(i+1)+";"+this.errorMessage+"\r\n";
			}
			else SQLcommand.enQueue(sql);
		}
		if(!errors.equals(""))
		{
			this.textArea_Error.setText(errors);
			return null;
		}
		return SQLcommand.getStrings();
	}
	private String[] getAllHintTexts()
	{
		return new String[]
		{
			"box[column][level]=merchandiseID;",
			"row=storehouseRow;",
			"column=storehouseColumn;",
			"setShelf(shelfColumn,shelfLevel);",
			"setBox(storehouseRow,storehouseColumn,shelfColumn,shelfLevel,merchandiseID);"
		};
	}
	private String[] getStringArray(String text)
	{
		int l=text.length();
		int k=0;
		char c;
		for(int i=0;i<l;i++)if((c=text.charAt(i))=='\n')k++;
		String[] stringArray=new String[k+1];
		String s="";
		for(int i=0;i<=k;i++)stringArray[i]=s;
		int i=0;
		k=0;
		while(i<l)
		{
			c=text.charAt(i++);
			if(c=='\r')c=text.charAt(i++);
			if(c!='\n')s+=c;
			else
			{
				stringArray[k++]=s;
				s="";
			}
		}
		if(!s.equals(""))stringArray[k++]=s;
		return stringArray;
	}
	private String[] convertToSQL(String text)
	{
		String[] SQL=null;
		this.errorMessage="";
		this.beginIndex=0;
		String substring=this.getSubstring(text,new char[]{'.'});
		if(!substring.equals("this"))this.beginIndex=0;
		substring=this.getSubstring(text,new char[]{'[','(','='});
		if(substring.equals("box"))
		{
			String shelfColumn=this.getSubstring(text,new char[]{']'});
			this.getSubstring(text,new char[]{'['});
			String shelfLevel=this.getSubstring(text,new char[]{']'});
			this.getSubstring(text,new char[]{'='});
			if(!isInteger(shelfColumn)){this.errorMessage="shelfColumn:"+shelfColumn+" isn't an integer!";return null;}
			else if(!isInteger(shelfLevel)){this.errorMessage="shelfLevel:"+shelfLevel+" isn't an integer!";return null;}
			String merchandiseID=this.getSubstring(text,new char[]{';'});
			if(merchandiseID.equals("null"))return new String[]{"Delete From MerchandiseLocationInfo Where storehouseRow="+this.row+" And storehouseColumn="+this.column+" And shelfColumn="+shelfColumn+" And shelfLevel="+shelfLevel};
			if(!isInteger(merchandiseID))merchandiseID=this.getMidstring(merchandiseID+";",'.',';');
			if(!isInteger(merchandiseID)){this.errorMessage="merchandiseID:"+merchandiseID+" isn't an integer!";return null;}
			if(!existMerchandiseID(merchandiseID)){this.errorMessage="merchandiseID:"+merchandiseID+" dose not exist!";return null;}
			SQL=new String[]
			{
				"Delete From MerchandiseLocationInfo Where merchandiseID="+merchandiseID,
				"Delete From MerchandiseLocationInfo Where storehouseRow="+this.row+" And storehouseColumn="+this.column+" And shelfColumn="+shelfColumn+" And shelfLevel="+shelfLevel,
				"Insert Into MerchandiseLocationInfo Values ("+merchandiseID+","+this.row+","+this.column+","+shelfColumn+","+shelfLevel+")"
			};
		}
		else if(substring.equals("row"))
		{
			String row=this.getSubstring(text,new char[]{';'});
			if(!isInteger(row))this.errorMessage="Row:"+row+" isn't an integer!";
			else this.row=Integer.parseInt(row);
		}
		else if(substring.equals("column"))
		{
			String column=this.getSubstring(text,new char[]{';'});
			if(!isInteger(column))this.errorMessage="Column:"+column+" isn't an integer!";
			else this.column=Integer.parseInt(column);
		}
		else if(substring.equals("setShelf"))
		{
			String row=this.getSubstring(text,new char[]{','});
			String column=this.getSubstring(text,new char[]{')'});
			if(!isInteger(row))this.errorMessage="Row isn't an integer!";
			else if(!isInteger(column))this.errorMessage+="Column isn't an integer!";
			else
			{
				this.row=Integer.parseInt(row);
				this.column=Integer.parseInt(column);
			}
		}
		else if(substring.equals("setBox"))
		{
			String storehouseRow=this.getSubstring(text,new char[]{','});
			String storehouseColumn=this.getSubstring(text,new char[]{','});
			String shelfColumn=this.getSubstring(text,new char[]{','});
			String shelfLevel=this.getSubstring(text,new char[]{','});
			String merchandiseID=this.getSubstring(text,new char[]{')'});
			if(!isInteger(storehouseRow)){this.errorMessage="storehouseRow:"+storehouseRow+" isn't an integer!";return null;}
			else if(!isInteger(storehouseColumn)){this.errorMessage="storehouseColumn:"+storehouseColumn+" isn't an integer!";return null;}
			else if(!isInteger(shelfColumn)){this.errorMessage="shelfColumn:"+shelfColumn+" isn't an integer!";return null;}
			else if(!isInteger(shelfLevel)){this.errorMessage="shelfLevel:"+shelfLevel+" isn't an integer!";return null;}
			if(merchandiseID.equals("null"))return new String[]{"Delete From MerchandiseLocationInfo Where storehouseRow="+storehouseRow+" And storehouseColumn="+storehouseColumn+" And shelfColumn="+shelfColumn+" And  shelfLevel="+shelfLevel};
			if(!isInteger(merchandiseID)){this.errorMessage="merchandiseID:"+merchandiseID+" isn't an integer!";return null;}
			if(!existMerchandiseID(merchandiseID)){this.errorMessage="merchandiseID:"+merchandiseID+" dose not exist!";return null;}
			SQL=new String[]
			{
				"Delete From MerchandiseLocationInfo Where merchandiseID="+merchandiseID,
				"Delete From MerchandiseLocationInfo Where storehouseRow="+storehouseRow+" And storehouseColumn="+storehouseColumn+" And shelfColumn="+shelfColumn+" And shelfLevel="+shelfLevel,
				"Insert Into MerchandiseLocationInfo Values ("+merchandiseID+","+storehouseRow+","+storehouseColumn+","+shelfColumn+","+shelfLevel+")"
			};
		}
		else this.errorMessage=text+" Is Not Valid Command!";
		return SQL;
	}
	private boolean existMerchandiseID(String merchandiseID)
	{
		if(mySQLprocessor==null)return false;
		try
		{
			ResultSet ResultSet1=mySQLprocessor.executeQuery("Select merchandiseID From MerchandiseInfo Where merchandiseID="+merchandiseID);
			return (ResultSet1.next());
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public void setStorehouse3DPanel(Storehouse3DPanel storehouse3DPanel)
	{
		this.storehouse3DPanel=storehouse3DPanel;
	}
	private boolean isInteger(String s)
	{
		if(s==null)return false;
		int length=s.length();
		if(length==0)return false;
		for(int i=0;i<length;i++)
		{
			char c=s.charAt(i);
			if(c<'0'||c>'9')return false;
		}
		return true;
	}
	private static String getMidstring(String string,char beginChar,char endChar)
	{
		if(string==null)return null;
		int i=0;
		int l=string.length();
		if(i==l)return null;
		char Char=string.charAt(i++);
		while(Char!=beginChar&&i<l)Char=string.charAt(i++);
		if(i==l)return null;
		Char=string.charAt(i++);
		String midstring="";
		while(Char!=endChar&&i<l)
		{
			midstring+=Char;
			Char=string.charAt(i++);
		}
		if(Char!=endChar)return null;
		return midstring;
	}
	private String getSubstring(String string,char[] endChar)
	{
		int i=beginIndex;
		int l=string.length();
		if(i>=l)return "";
		String substring="";
		char Char=string.charAt(i++);
		while(Char==' '&&i<l)Char=string.charAt(i++);
		while(!isIn(Char,endChar))
		{
			substring+=Char;
			if(i>=l)break;
			Char=string.charAt(i++);
		}
		this.beginIndex=i;
		return substring;
	}
	private boolean isIn(char c,char[] a)
	{
		int l=a.length;
		for(int i=0;i<l;i++)if(a[i]==c)return true;
		return false;
	}
}
class Storehouse3DPanel extends Panel implements MouseMotionListener
{
	private int width,height,depth,storehouseRow,storehouseColumn,shelfColumn,shelfLevel,intervalWidth,intervalHeight,shelfWidth,shelfHeight,shelfIntervalWidth,shelfIntervalHeight,shelfGridWidth,shelfGridHeight;
	private int x0,y0,startX,startY,endX,endY,currentX,currentY;
	private String[] storehouseRegionInfo;
	private boolean drawStorehouseRegionsEnable=false,drawCarrierRouteEnable=false;
	private int currentRow=-1,currentColumn=-1;
	private ShelfMerchandiseInfoPanel shelfMerchandiseInfoPanel;
	private int[][] carrierRoute;
	private String[] merchandiseLocationInfo;
	private MySQLprocessor mySQLprocessor;
	private TextWindow textWindow;
	public Storehouse3DPanel(int x0,int y0,int width,int height,MySQLprocessor mySQLprocessor)
	{
		this.x0=x0;
		this.y0=y0;
		this.width=width-1;
		this.height=height-1;
		this.setBounds(x0,y0,width,height);
		this.mySQLprocessor=mySQLprocessor;
		this.addMouseMotionListener(this);
		this.getStorehouseInfo();
		this.getMerchandiseLocationInfo();
		this.textWindow=new TextWindow();
	}
	public void paint(Graphics g)
	{
		this.drawShelves(g);
		this.drawMerchandiseIDs(g);
	}
	boolean isValidPosition(int storehouseRow,int storehouseColumn,int shelfColumn,int shelfLevel)
	{
		if(storehouseRow==-1||storehouseColumn==-1||shelfColumn==-1||shelfLevel==-1)return false;
		else return true;
	}
	String[] getMerchandiseInfo(int storehouseRow,int storehouseColumn,int shelfColumn,int shelfLevel)
	{
		if(merchandiseLocationInfo!=null)
		{
			int l=merchandiseLocationInfo.length;
			int i=0;
			while(i<l)
			{
				String merchandiseID=merchandiseLocationInfo[i++];
				int StorehouseRow=Integer.parseInt(merchandiseLocationInfo[i++]);
				int StorehouseColumn=Integer.parseInt(merchandiseLocationInfo[i++]);
				int ShelfColumn=Integer.parseInt(merchandiseLocationInfo[i++]);
				int ShelfLevel=Integer.parseInt(merchandiseLocationInfo[i++]);
				if(storehouseRow==StorehouseRow&&storehouseColumn==StorehouseColumn&&shelfColumn==ShelfColumn&&shelfLevel==ShelfLevel)
				{
					ResultSet ResultSet1=mySQLprocessor.executeQuery("Select * From MerchandiseInfo Where merchandiseID="+merchandiseID);
					try
					{
						if(ResultSet1.next())
						{
							String merchandiseName=ResultSet1.getString("merchandiseName");
							String merchandiseStocks=ResultSet1.getString("merchandiseStocks");
							String merchandiseClassification=ResultSet1.getString("merchandiseClassification");
							String merchandisePrice=ResultSet1.getString("merchandisePrice");
							String merchandiseDescription=ResultSet1.getString("merchandiseDescription");
							return new String[]
							{
								"merchandiseID is:"+merchandiseID,
								"merchandiseName is:"+merchandiseName,
								"merchandiseStocks is:"+merchandiseStocks,
								"merchandiseClassification is:"+merchandiseClassification,
								"merchandisePriceis:"+merchandisePrice,
								"merchandiseDescription is:"+merchandiseDescription,
								"storehouseRow="+storehouseRow,
								"storehouseColumn="+storehouseColumn,
								"shelfColumn="+shelfColumn,
								"shelfLevel="+shelfLevel
							};
						}
					}
					catch(Exception e){e.printStackTrace();}
				}
			}
		}
		return new String[]
		{
			"storehouseRow="+storehouseRow,
			"storehouseColumn="+storehouseColumn,
			"shelfColumn="+shelfColumn,
			"shelfLevel="+shelfLevel
		};
	}
	public void mouseMoved(MouseEvent e)
	{
		int x=e.getX();
		int y=e.getY();
		int storehouseRow=(y-intervalHeight/2)/(shelfHeight+intervalHeight);
		if(y<intervalHeight/2||y>(intervalHeight/2+(shelfHeight+intervalHeight)*storehouseRow+shelfHeight))storehouseRow=-1;
		int storehouseColumn=(x-intervalWidth/2)/(shelfWidth+intervalWidth);
		if(x<intervalWidth/2||x>(intervalWidth/2+(shelfWidth+intervalWidth)*storehouseColumn+shelfWidth))storehouseColumn=-1;
		int shelfColumn=(x-intervalWidth/2-(shelfWidth+intervalWidth)*storehouseColumn)/(shelfGridWidth+shelfIntervalWidth);
		int shelfLevel=this.shelfLevel-1-(y-intervalHeight/2-(shelfHeight+intervalHeight)*storehouseRow)/(shelfGridHeight+shelfIntervalHeight);
		if(isValidPosition(storehouseRow,storehouseColumn,shelfColumn,shelfLevel))
		{
			String[] text=this.getMerchandiseInfo(storehouseRow,storehouseColumn,shelfColumn,shelfLevel);
			this.textWindow.setPositionAndTexts(x+x0+shelfGridWidth,y+y0+shelfGridHeight+25,text);
			this.textWindow.setVisible(true);
		}
		else this.textWindow.setVisible(false);
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}
	private void drawFrame(Graphics g,int x0,int y0,int width,int height)
	{
		g.setColor(new Color(200,200,220));
		g.drawRect(x0,y0,width,height);
		g.setColor(new Color(120,120,140));
		g.drawLine(x0,y0,x0+width,y0);
	}
	private void drawFrame3D(Graphics g,int x0,int y0,int width,int height,int depth)
	{
		int xd=x0+depth;
		int x1=x0+width;
		int xD=x1+depth;
		int yd=y0-depth;
		int y1=y0+height;
		int yD=y1-depth;
		g.setColor(new Color(120,120,140));
		g.drawRect(x0,y0,width,height);
		g.drawLine(xd,yd,xD,yd);
		g.drawLine(xD,yd,xD,yD);
		g.drawLine(x0,y0,xd,yd);
		g.drawLine(x1,y0,xD,yd);
		g.drawLine(x1,y1,xD,yD);
		g.setColor(new Color(200,200,220));
		g.drawLine(x0,y1,xd,yD);
		g.drawLine(xd,yD,xd,yd);
		g.drawLine(xd,yD,xD,yD);
	}
	private void drawShelves(Graphics g)
	{
		this.drawFrame(g,0,0,width,height);
		for(int i=0;i<storehouseRow;i++)
		{
			for(int j=0;j<storehouseColumn;j++)
			{
				int x0=intervalWidth/2+j*(shelfWidth+intervalWidth);
				int y0=intervalHeight/2+i*(shelfHeight+intervalHeight);
				this.drawFrame3D(g,x0,y0,shelfWidth,shelfHeight,depth);
				for(int k=0;k<shelfColumn;k++)
				{
					for(int l=0;l<shelfLevel;l++)
					{
						int x1=x0+k*(shelfGridWidth+shelfIntervalWidth)+shelfIntervalWidth/2;
						int y1=y0+l*(shelfGridHeight+shelfIntervalHeight)+shelfIntervalHeight;
						this.drawFrame(g,x1,y1,shelfGridWidth,shelfGridHeight);
					}
				}
			}
		}
	}
	private void drawMerchandiseIDs(Graphics g)
	{
		if(merchandiseLocationInfo!=null)
		{
			g.setColor(Color.black);
			int l=merchandiseLocationInfo.length;
			int i=0;
			while(i<l)
			{
				String merchandiseID=merchandiseLocationInfo[i++];
				int storehouseRow=Integer.parseInt(merchandiseLocationInfo[i++]);
				int storehouseColumn=Integer.parseInt(merchandiseLocationInfo[i++]);
				int shelfColumn=Integer.parseInt(merchandiseLocationInfo[i++]);
				int shelfLevel=Integer.parseInt(merchandiseLocationInfo[i++]);
				int x=intervalWidth/2+(shelfWidth+intervalWidth)*storehouseColumn+(shelfGridWidth+shelfIntervalWidth)*shelfColumn;
				int y=intervalHeight/2+(shelfHeight+intervalHeight)*storehouseRow+(shelfGridHeight+shelfIntervalHeight)*(this.shelfLevel-shelfLevel);
				g.drawString(merchandiseID,x,y);
			}
		}
	}
	public void getMerchandiseLocationInfo()
	{
		StringQueue queue_merchandiseLocationInfo=new StringQueue();
		try
		{
			ResultSet  ResultSet1=mySQLprocessor.executeQuery("Select * From MerchandiseLocationInfo");
			while(ResultSet1.next())
			{
				queue_merchandiseLocationInfo.enQueue(ResultSet1.getInt("merchandiseID")+"");
				queue_merchandiseLocationInfo.enQueue(ResultSet1.getInt("storehouseRow")+"");
				queue_merchandiseLocationInfo.enQueue(ResultSet1.getInt("storehouseColumn")+"");
				queue_merchandiseLocationInfo.enQueue(ResultSet1.getInt("shelfColumn")+"");
				queue_merchandiseLocationInfo.enQueue(ResultSet1.getInt("shelfLevel")+"");
			}
		}
		catch(Exception e){e.printStackTrace();}
		this.merchandiseLocationInfo=queue_merchandiseLocationInfo.getStrings();
	}
	private void getStorehouseInfo()
	{
		this.storehouseRow=8;
		this.storehouseColumn=5;
		this.shelfColumn=5;
		this.shelfLevel=7;
		ResultSet ResultSet1=mySQLprocessor.executeQuery("Select * From StorehouseInfo");
		try
		{
			if(ResultSet1.next())
			{
				this.storehouseRow=ResultSet1.getInt("storehouseRow");
				this.storehouseColumn=ResultSet1.getInt("storehouseColumn");
				this.shelfColumn=ResultSet1.getInt("shelfColumn");
				this.shelfLevel=ResultSet1.getInt("shelfLevel");
			}
		}
		catch(Exception e){e.printStackTrace();}
		this.intervalWidth=width/(3*storehouseColumn);
		this.intervalHeight=height/(3*storehouseRow);
		this.shelfWidth=width/storehouseColumn-intervalWidth;
		this.shelfHeight=height/storehouseRow-intervalHeight;
		this.shelfIntervalWidth=shelfWidth/(3*shelfColumn);
		this.shelfIntervalHeight=shelfHeight/(3*shelfLevel);
		this.shelfGridWidth=shelfWidth/shelfColumn-shelfIntervalWidth;
		this.shelfGridHeight=shelfHeight/shelfLevel-shelfIntervalHeight;
		this.depth=intervalHeight/4;
	}
}
class StorehouseRegionPanel extends Panel implements MouseListener,MouseMotionListener
{
	private int width,height,storehouseRow,storehouseColumn,shelfColumn,intervalWidth,intervalHeight,shelfWidth,shelfHeight,shelfIntervalWidth,shelfIntervalHeight,shelfGridWidth,shelfGridHeight;
	private int startX,startY,endX,endY,currentX,currentY;
	private String[] storehouseRegionInfo;
	private boolean drawStorehouseRegionsEnable=false,drawCarrierRouteEnable=false;
	private int currentRow=-1,currentColumn=-1;
	private ShelfMerchandiseInfoPanel shelfMerchandiseInfoPanel;
	private int[][] carrierRoute;
	public StorehouseRegionPanel(int width,int height,int storehouseRow,int storehouseColumn,int shelfColumn)
	{
		this.width=width-1;
		this.height=height-1;
		this.storehouseRow=storehouseRow;
		this.storehouseColumn=storehouseColumn;
		this.shelfColumn=shelfColumn;
		this.intervalWidth=width/(3*storehouseColumn);
		this.intervalHeight=height/(3*storehouseRow);
		this.shelfWidth=width/storehouseColumn-intervalWidth;
		this.shelfHeight=height/storehouseRow-intervalHeight;
		this.shelfIntervalWidth=shelfWidth/(3*shelfColumn);
		this.shelfIntervalHeight=shelfHeight/3;
		this.shelfGridWidth=shelfWidth/shelfColumn-shelfIntervalWidth;
		this.shelfGridHeight=shelfHeight-shelfIntervalHeight;
		this.setFocusable(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	public void paint(Graphics g)
	{
		this.drawStorehouseRegions(g);
		this.drawClickedShelfFrame(g);
		this.drawMouseFrame(g);
		this.drawShelves(g);
		this.drawSimpleCarrierRoute(g);
		this.drawElaborateCarrierRoute(g);
	}
	private void drawStorehouseRegions(Graphics g)
	{
		if(!drawStorehouseRegionsEnable)return;
		if(storehouseRegionInfo==null)return;
		int l=storehouseRegionInfo.length;
		int i=0;
		while(i<l)
		{			
			String regionName=storehouseRegionInfo[i++];
			int startRow=Integer.parseInt(storehouseRegionInfo[i++]);
			int startColumn=Integer.parseInt(storehouseRegionInfo[i++]);
			int endRow=Integer.parseInt(storehouseRegionInfo[i++]);
			int endColumn=Integer.parseInt(storehouseRegionInfo[i++]);
			int red=Integer.parseInt(storehouseRegionInfo[i++]);
			int green=Integer.parseInt(storehouseRegionInfo[i++]);
			int blue=Integer.parseInt(storehouseRegionInfo[i++]);
			int x0=intervalWidth/2+startColumn*(shelfWidth+intervalWidth);
			int y0=intervalHeight/2+startRow*(shelfHeight+intervalHeight);
			int dx=(endColumn-startColumn+1)*(shelfWidth+intervalWidth)-intervalWidth;
			int dy=(endRow-startRow+1)*(shelfHeight+intervalHeight)-intervalHeight;
			g.setColor(new Color(red,green,blue));
			g.fillRect(x0,y0,dx,dy);
			g.setColor(Color.BLACK);
			g.drawString(regionName,x0+dx/2,y0+dy/2);
		}
	}
	private void drawClickedShelfFrame(Graphics g)
	{
		if(currentRow==-1||currentColumn==-1)return;
		int x0=intervalWidth/2+currentColumn*(shelfWidth+intervalWidth);
		int y0=intervalHeight/2+currentRow*(shelfHeight+intervalHeight);
		for(int i=0;i<shelfHeight;i++)
		{
			for(int j=0;j<shelfWidth;j++)
			{
				int grey=(int)((shelfHeight-i)*30/shelfHeight)+225;
				g.setColor(new Color(grey,grey,255));
				g.drawLine(x0+j,y0+i,x0+j,y0+i);
			}
		}
		
	}
	private void drawMouseFrame(Graphics g)
	{
		int x0=startX;
		int y0=startY;
		int dX=endX-startX;
		int dY=endY-startY;
		if(dX<0)
		{
			dX=-dX;
			x0=endX;
		}
		if(dY<0)
		{
			dY=-dY;
			y0=endY;
		}
		g.setColor(new Color(100,100,250));
		g.drawRect(x0,y0,dX,dY);
		g.setColor(new Color(245,245,250));
		g.fillRect(x0+1,y0+1,dX-1,dY-1);
	}
	private void drawShelves(Graphics g)
	{
		this.drawFrame(g,0,0,width,height);
		for(int i=0;i<storehouseRow;i++)
		{
			for(int j=0;j<storehouseColumn;j++)
			{
				int x0=intervalWidth/2+j*(shelfWidth+intervalWidth);
				int y0=intervalHeight/2+i*(shelfHeight+intervalHeight);
				this.drawFrame(g,x0,y0,shelfWidth,shelfHeight);
				for(int k=0;k<shelfColumn;k++)
				{
					int x1=x0+shelfIntervalWidth/2+k*(shelfGridWidth+shelfIntervalWidth);
					int y1=y0+shelfIntervalHeight/2;
					this.drawFrame(g,x1,y1,shelfGridWidth,shelfGridHeight);
				}
			}
		}
	}
	private void drawSimpleCarrierRoute(Graphics g)
	{
		if(!drawCarrierRouteEnable)return;
		if(carrierRoute==null)return;
		int l=carrierRoute.length;
		for(int c=0;c<l-1;c++)
		{
			int i0=carrierRoute[c][1];
			int j0=carrierRoute[c][2];
			int k0=carrierRoute[c][3];
			int i1=carrierRoute[c+1][1];
			int j1=carrierRoute[c+1][2];
			int k1=carrierRoute[c+1][3];
			int x0=intervalWidth/2+j0*(shelfWidth+intervalWidth)+shelfIntervalWidth/2+k0*(shelfGridWidth+shelfIntervalWidth)+shelfGridWidth/2;
			int y0=intervalHeight/2+i0*(shelfHeight+intervalHeight)+shelfHeight/2;
			int x1=intervalWidth/2+j1*(shelfWidth+intervalWidth)+shelfIntervalWidth/2+k1*(shelfGridWidth+shelfIntervalWidth)+shelfGridWidth/2;
			int y1=intervalHeight/2+i1*(shelfHeight+intervalHeight)+shelfHeight/2;
			g.drawLine(x0,y0,x1,y1);
		}
	}
	private void drawElaborateCarrierRoute(Graphics g)
	{
		if(!drawCarrierRouteEnable)return;
		if(carrierRoute==null)return;
		int l=carrierRoute.length;
		if(l<2)return;
		int i0=carrierRoute[0][1];
		int j0=carrierRoute[0][2];
		int k0=carrierRoute[0][3];
		int i1=carrierRoute[1][1];
		int j1=carrierRoute[1][2];
		int k1=carrierRoute[1][3];
		char d0=((i1>=i0)?'D':'U');
		for(int c=1;c<l-1;c++)
		{

			int x0=intervalWidth/2+j0*(shelfWidth+intervalWidth)+shelfIntervalWidth/2+k0*(shelfGridWidth+shelfIntervalWidth)+shelfGridWidth/2;
			int y0=intervalHeight/2+i0*(shelfHeight+intervalHeight);
			if(d0=='D')y0+=shelfHeight;
			String[] pathInfo=this.getPathInfo(d0,i0,j0,k0,i1,j1,k1);
			this.drawPath(g,x0,y0,pathInfo);
			d0=pathInfo[0].charAt(pathInfo[0].length()-1);
			d0=((d0=='D')?'U':'D');
			i0=carrierRoute[c][1];
			j0=carrierRoute[c][2];
			k0=carrierRoute[c][3];
			i1=carrierRoute[c+1][1];
			j1=carrierRoute[c+1][2];
			k1=carrierRoute[c+1][3];
		}
		int x0=intervalWidth/2+j0*(shelfWidth+intervalWidth)+shelfIntervalWidth/2+k0*(shelfGridWidth+shelfIntervalWidth)+shelfGridWidth/2;
		int y0=intervalHeight/2+i0*(shelfHeight+intervalHeight);
		if(d0=='D')y0+=shelfHeight;
		String[] pathInfo=this.getPathInfo(d0,i0,j0,k0,i1,j1,k1);
		this.drawPath(g,x0,y0,pathInfo);
	}
	private int abs(int x)
	{
		return ((x>=0)?x:-x);
	}
	private String[] getPathInfo(char d0,int i0,int j0,int k0,int i1,int j1,int k1)
	{
		String[] pathInfo=null;
		String pathDirection=d0+"";
		String pathLength0="0";
		String pathLength1="0";
		String pathLength2="0";
		String pathLength3="0";
		String pathLength4="0";
		int iW=intervalWidth/2;
		int iH=intervalHeight/2;
		int dH=shelfHeight+intervalHeight;
		int dW=shelfWidth+intervalWidth;
		int dSW=shelfGridWidth+shelfIntervalWidth;
		if(d0=='D')
		{
			if(i1==i0||i1==i0+1)
			{
				pathDirection=((i1==i0)?"DRU":"DRD");
				pathLength0=iH+"";
				pathLength1=((k1-k0)*dSW+(j1-j0)*dW)+"";
				pathLength2=iH+"";
				pathInfo=new String[]{pathDirection,pathLength0,pathLength1,pathLength2};
			}
			else if(i1>i0)
			{
				if(j1==j0)
				{
					boolean b=((k0+k1)<=(shelfColumn-1-k0+shelfColumn-1-k1));
					pathDirection=(b?"DLDRD":"DRDLD");
					pathLength0=iH+"";
					pathLength1=((b?k0:(shelfColumn-1-k0))*dSW+iW)+"";
					pathLength2=(i1-i0-1)*dH+"";
					pathLength3=((b?k1:(shelfColumn-1-k1))*dSW+iW)+"";
					pathLength4=iH+"";
					pathInfo=new String[]{pathDirection,pathLength0,pathLength1,pathLength2,pathLength3,pathLength4};
				}
				else
				{
					boolean b=(j1>j0);
					pathDirection=(b?"DRDRD":"DLDLD");
					pathLength0=iH+"";
					pathLength1=((b?(shelfColumn-k0):k0)*dSW+iW+(abs(j1-j0)-1)*dW)+"";
					pathLength2=(i1-i0-1)*dH+"";
					pathLength3=((b?k1:(shelfColumn-k1))*dSW+iW)+"";
					pathLength4=iH+"";
					pathInfo=new String[]{pathDirection,pathLength0,pathLength1,pathLength2,pathLength3,pathLength4};
				}
			}
			else
			{
				if(j1==j0)
				{
					boolean b=((k0+k1)<=(shelfColumn-1-k0+shelfColumn-1-k1));
					pathDirection=(b?"DLURU":"DRULU");
					pathLength0=iH+"";
					pathLength1=((b?k0:(shelfColumn-1-k0))*dSW+iW)+"";
					pathLength2=(i0-i1)*dH+"";
					pathLength3=((b?k1:(shelfColumn-1-k1))*dSW+iW)+"";
					pathLength4=iH+"";
					pathInfo=new String[]{pathDirection,pathLength0,pathLength1,pathLength2,pathLength3,pathLength4};
				}
				else
				{
					boolean b=(j1>j0);
					pathDirection=(b?"DRURU":"DLULU");
					pathLength0=iH+"";
					pathLength1=((b?(shelfColumn-k0):k0)*dSW+iW+(abs(j1-j0)-1)*dW)+"";
					pathLength2=(i0-i1)*dH+"";
					pathLength3=((b?k1:(shelfColumn-k1))*dSW+iW)+"";
					pathLength4=iH+"";
					pathInfo=new String[]{pathDirection,pathLength0,pathLength1,pathLength2,pathLength3,pathLength4};
				}
			}
		}
		else
		{
			if(i1==i0||i1==i0-1)
			{
				pathDirection=((i1==i0)?"URD":"URU");
				pathLength0=iH+"";
				pathLength1=((k1-k0)*dSW+(j1-j0)*dW)+"";
				pathLength2=iH+"";
				pathInfo=new String[]{pathDirection,pathLength0,pathLength1,pathLength2};
			}
			else if(i1<i0)
			{
				if(j1==j0)
				{
					boolean b=((k0+k1)<=(shelfColumn-1-k0+shelfColumn-1-k1));
					pathDirection=(b?"ULURU":"URULU");
					pathLength0=iH+"";
					pathLength1=((b?k0:(shelfColumn-1-k0))*dSW+iW)+"";
					pathLength2=(i0-i1-1)*dH+"";
					pathLength3=((b?k1:(shelfColumn-1-k1))*dSW+iW)+"";
					pathLength4=iH+"";
					pathInfo=new String[]{pathDirection,pathLength0,pathLength1,pathLength2,pathLength3,pathLength4};
				}
				else
				{
					boolean b=(j1>j0);
					pathDirection=(b?"URURU":"ULULU");
					pathLength0=iH+"";
					pathLength1=((b?(shelfColumn-k0):k0)*dSW+iW+(abs(j1-j0)-1)*dW)+"";
					pathLength2=(i0-i1-1)*dH+"";
					pathLength3=((b?k1:(shelfColumn-k1))*dSW+iW)+"";
					pathLength4=iH+"";
					pathInfo=new String[]{pathDirection,pathLength0,pathLength1,pathLength2,pathLength3,pathLength4};
				}
			}
			else
			{
				if(j1==j0)
				{
					boolean b=((k0+k1)<=(shelfColumn-1-k0+shelfColumn-1-k1));
					pathDirection=(b?"ULDRD":"URDLD");
					pathLength0=iH+"";
					pathLength1=((b?k0:(shelfColumn-1-k0))*dSW+iW)+"";
					pathLength2=(i1-i0)*dH+"";
					pathLength3=((b?k1:(shelfColumn-1-k1))*dSW+iW)+"";
					pathLength4=iH+"";
					pathInfo=new String[]{pathDirection,pathLength0,pathLength1,pathLength2,pathLength3,pathLength4};
				}
				else
				{
					boolean b=(j1>j0);
					pathDirection=(b?"URDRD":"ULDLD");
					pathLength0=iH+"";
					pathLength1=((b?(shelfColumn-k0):k0)*dSW+iW+(abs(j1-j0)-1)*dW)+"";
					pathLength2=(i1-i0)*dH+"";
					pathLength3=((b?k1:(shelfColumn-k1))*dSW+iW)+"";
					pathLength4=iH+"";
					pathInfo=new String[]{pathDirection,pathLength0,pathLength1,pathLength2,pathLength3,pathLength4};
				}
			}
		}
		return pathInfo;
	}
	private void drawPath(Graphics g,int x0,int y0,String[] pathInfo)
	{
		if(pathInfo==null)return;
		int l=pathInfo.length;
		if(l<1)return;
		String  pathDirection=pathInfo[0];
		l=pathDirection.length();
		int[] pathLength=new int[l];
		for(int i=0;i<l;i++)pathLength[i]=Integer.parseInt(pathInfo[i+1]);
		int x1=x0;
		int y1=y0;
		for(int i=0;i<l;i++)
		{
			char c=pathDirection.charAt(i);
			switch(c)
			{
				case 'U':y1=y0-pathLength[i];break;
				case 'D':y1=y0+pathLength[i];break;
				case 'L':x1=x0-pathLength[i];break;
				case 'R':x1=x0+pathLength[i];break;
			}
			g.drawLine(x0,y0,x1,y1);
			x0=x1;
			y0=y1;
		}
	}
	public int[] getRowColumnInfo()
	{
		int startX=this.startX;
		int startY=this.startY;
		int endX=this.endX;
		int endY=this.endY;
		if(endX<startX)
		{
			startX=this.endX;
			endX=this.startX;
		}
		if(endY<startY)
		{
			startY=this.endY;
			endY=this.startY;
		}
		int[] rowColumnInfo=new int[4];
		int startRow=(startY+intervalHeight/2)/(shelfHeight+intervalHeight);
		int startColumn=(startX+intervalWidth/2)/(shelfWidth+intervalWidth);
		int endRow=(endY-intervalHeight/2)/(shelfHeight+intervalHeight);
		int endColumn=(endX-intervalWidth/2)/(shelfWidth+intervalWidth);
		rowColumnInfo[0]=startRow;
		rowColumnInfo[1]=startColumn;
		rowColumnInfo[2]=endRow;
		rowColumnInfo[3]=endColumn;
		return rowColumnInfo;
	}
	private void getCurrentRowColumn()
	{
		int rowY=currentY-intervalHeight/2;
		int columnX=currentX-intervalWidth/2;
		if(rowY<0||columnX<0)
		{
			this.currentRow=-1;
			this.currentColumn=-1;
			return;
		}
		this.currentRow=rowY/(shelfHeight+intervalHeight);
		this.currentColumn=columnX/(shelfWidth+intervalWidth);
		if(rowY>currentRow*(shelfHeight+intervalHeight)+shelfHeight){currentRow=-1;return;}
		if(columnX>currentColumn*(shelfWidth+intervalWidth)+shelfWidth)currentColumn=-1;
	}
	public void setCurrentRowColumn(int currentRow,int currentColumn)
	{
		this.currentRow=currentRow;
		this.currentColumn=currentColumn;
	}
	public void setStorehouseRegionInfo(ResultSet storehouseRegionResultSet)
	{
		StringQueue StringQueue1=new StringQueue();
		ResultSet ResultSet1=storehouseRegionResultSet;
		try
		{
			while(ResultSet1.next())
			{
				StringQueue1.enQueue(ResultSet1.getString("regionName"));
				StringQueue1.enQueue(ResultSet1.getInt("startRow")+"");
				StringQueue1.enQueue(ResultSet1.getInt("startColumn")+"");
				StringQueue1.enQueue(ResultSet1.getInt("endRow")+"");
				StringQueue1.enQueue(ResultSet1.getInt("endColumn")+"");
				StringQueue1.enQueue(220+(int)(20*Math.random())+"");
				StringQueue1.enQueue(220+(int)(20*Math.random())+"");
				StringQueue1.enQueue(220+(int)(5*Math.random())+"");
			}
		}
		catch(Exception e){}
		this.storehouseRegionInfo=StringQueue1.getStrings();
	}
	public void setCarrierRoute(int[][] carrierRoute)
	{
		this.carrierRoute=carrierRoute;
		this.setDrawCarrierRouteEnable(true);
	}
	private void drawFrame(Graphics g,int x0,int y0,int width,int height)
	{
		g.setColor(new Color(200,200,220));
		g.drawRect(x0,y0,width,height);
		g.setColor(new Color(120,120,140));
		g.drawLine(x0,y0,x0+width,y0);
	}
	public void setDrawStorehouseRegionsEnable(boolean b)
	{
		this.drawStorehouseRegionsEnable=b;
		this.repaint();
	}
	public void setDrawCarrierRouteEnable(boolean b)
	{
		this.drawCarrierRouteEnable=b;
		this.repaint();
	}
	public void clearXY()
	{
		this.startX=0;
		this.startY=0;
		this.endX=0;
		this.endY=0;
		this.repaint();
	}
	public void repaintShelfMerchandiseInfoPanel()
	{
		if(shelfMerchandiseInfoPanel!=null)
		{
			this.shelfMerchandiseInfoPanel.clearColumnLevel();
			this.shelfMerchandiseInfoPanel.setStorehouseRowColumn(currentRow,currentColumn);
		}
	}
	public void mousePressed(MouseEvent e)
	{
		this.startX=e.getX();
		this.startY=e.getY();
	}
	public void mouseDragged(MouseEvent e)
	{
		this.endX=e.getX();
		this.endY=e.getY();
		this.currentRow=-1;
		this.repaint();
	}
	public void mouseClicked(MouseEvent e)
	{
		this.clearXY();
		this.currentX=e.getX();
		this.currentY=e.getY();
		this.getCurrentRowColumn();
		if(shelfMerchandiseInfoPanel!=null)
		{
			this.shelfMerchandiseInfoPanel.setStorehouseRowColumn(currentRow,currentColumn);
		}
	}
	public void mouseReleased(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseDown(MouseEvent e){}
	public void mouseUp(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public int getCurrentRow(){return currentRow;}
	public int getCurrentColumn(){return currentColumn;}
	public void setShelfMerchandiseInfoPanel(ShelfMerchandiseInfoPanel shelfMerchandiseInfoPanel)
	{
		this.shelfMerchandiseInfoPanel=shelfMerchandiseInfoPanel;
	}
}
class ShelfMerchandiseInfoPanel extends Panel implements MouseListener
{
	private int length,height,level,column,intervalLength,intervalHeight,gridLength,gridHeight;
	private int currentX,currentY;
	private String[] shelfMerchandiseInfo;
	private int currentLevel=-1,currentColumn=-1;
	private MySQLprocessor mySQLprocessor;
	public ShelfMerchandiseInfoPanel(int length,int height,int column,int level,MySQLprocessor mySQLprocessor)
	{
		this.length=length-1;
		this.height=height-1;
		this.column=column;
		this.level=level;
		this.intervalLength=length/(5*column);
		this.intervalHeight=height/(5*level);
		this.gridLength=length/column-intervalLength;
		this.gridHeight=height/level-intervalHeight;
		this.setFocusable(true);
		this.addMouseListener(this);
		this.mySQLprocessor=mySQLprocessor;
	}
	public void paint(Graphics g)
	{
		this.drawShelfMerchandises(g);
		this.drawClickedGridFrame(g);
		this.drawShelfGrids(g);
	}
	private void drawShelfMerchandises(Graphics g)
	{
		if(shelfMerchandiseInfo==null)return;
		int l=shelfMerchandiseInfo.length;
		int i=0;
		while(i<l)
		{
			String merchandiseName=shelfMerchandiseInfo[i++];
			String merchandiseID=shelfMerchandiseInfo[i++];
			int shelfColumn=Integer.parseInt(shelfMerchandiseInfo[i++]);
			int shelfLevel=Integer.parseInt(shelfMerchandiseInfo[i++]);
			int red=Integer.parseInt(shelfMerchandiseInfo[i++]);
			int green=Integer.parseInt(shelfMerchandiseInfo[i++]);
			int blue=Integer.parseInt(shelfMerchandiseInfo[i++]);
			int x0=intervalLength/2+shelfColumn*(gridLength+intervalLength);
			int y0=intervalHeight/2+(level-1-shelfLevel)*(gridHeight+intervalHeight);
			g.setColor(new Color(red,green,blue));
			g.fillRect(x0,y0,gridLength,gridHeight);
			g.setColor(Color.BLACK);
			g.drawString(merchandiseName,x0,y0+gridHeight);
			g.drawString(merchandiseID,x0+gridLength/2,y0+gridHeight/2);
		}
	}
	private void drawClickedGridFrame(Graphics g)
	{
		if(currentLevel==-1||currentColumn==-1)return;
		int x0=intervalLength/2+currentColumn*(gridLength+intervalLength);
		int y0=intervalHeight/2+currentLevel*(gridHeight+intervalHeight);
		for(int i=0;i<gridHeight;i++)
		{
			for(int j=0;j<gridLength;j++)
			{
				int grey=(int)((gridHeight-i)*40/gridHeight)+215;
				g.setColor(new Color(grey,grey,255));
				g.drawLine(x0+j,y0+i,x0+j,y0+i);
			}
		}
	}
	private void drawShelfGrids(Graphics g)
	{
		this.drawFrame(g,0,0,length-1,height-1);
		for(int i=0;i<level;i++)
		{
			for(int j=0;j<column;j++)
			{
				int x0=intervalLength/2+j*(gridLength+intervalLength);
				int y0=intervalHeight/2+i*(gridHeight+intervalHeight);
				this.drawFrame(g,x0,y0,gridLength,gridHeight);
			}
		}
	}
	private void getCurrentLevelColumn()
	{
		int levelY=currentY-intervalHeight/2;
		int columnX=currentX-intervalLength/2;
		if(levelY<0||columnX<0)
		{
			this.currentLevel=-1;
			this.currentColumn=-1;
			return;
		}
		this.currentLevel=levelY/(gridHeight+intervalHeight);
		this.currentColumn=columnX/(gridLength+intervalLength);
		if(levelY>currentLevel*(gridHeight+intervalHeight)+gridHeight){currentLevel=-1;return;}
		if(columnX>currentColumn*(gridLength+intervalLength)+gridLength)currentColumn=-1;
	}
	public void setCurrentColumnLevel(int currentColumn,int currentLevel)
	{
		this.currentColumn=currentColumn;
		this.currentLevel=level-1-currentLevel;
	}
	public void setStorehouseRowColumn(int storehouseRow,int storehouseColumn)
	{
		String structuredQueryLanguage="Select merchandiseName,MerchandiseLocationInfo.merchandiseID,shelfColumn,shelfLevel From MerchandiseLocationInfo,MerchandiseInfo  Where MerchandiseLocationInfo.merchandiseID=MerchandiseInfo.merchandiseID And storehouseRow="+storehouseRow+" And storehouseColumn="+storehouseColumn;
		ResultSet shelfMerchandiseResultSet=mySQLprocessor.executeQuery(structuredQueryLanguage);
		this.setShelfMerchandiseInfo(shelfMerchandiseResultSet);
		this.repaint();
	}
	private void setShelfMerchandiseInfo(ResultSet shelfMerchandiseResultSet)
	{
		StringQueue StringQueue1=new StringQueue();
		ResultSet ResultSet1=shelfMerchandiseResultSet;
		try
		{
			while(ResultSet1.next())
			{
				StringQueue1.enQueue(ResultSet1.getString("merchandiseName"));
				StringQueue1.enQueue(ResultSet1.getInt("merchandiseID")+"");
				StringQueue1.enQueue(ResultSet1.getInt("shelfColumn")+"");
				StringQueue1.enQueue(ResultSet1.getInt("shelfLevel")+"");
				StringQueue1.enQueue(220+(int)(20*Math.random())+"");
				StringQueue1.enQueue(220+(int)(20*Math.random())+"");
				StringQueue1.enQueue(220+(int)(5*Math.random())+"");
			}
		}
		catch(Exception e){}
		this.shelfMerchandiseInfo=StringQueue1.getStrings();
	}
	private void drawFrame(Graphics g,int x0,int y0,int width,int height)
	{
		g.setColor(new Color(200,200,220));
		g.drawRect(x0,y0,width,height);
		g.setColor(new Color(120,120,140));
		g.drawLine(x0,y0,x0+width,y0);
	}
	public void mouseClicked(MouseEvent e)
	{
		this.currentX=e.getX();
		this.currentY=e.getY();
		this.getCurrentLevelColumn();
		this.repaint();
	}
	public void clearColumnLevel()
	{
		this.currentColumn=-1;
		this.currentLevel=-1;
	}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public int getCurrentLevel(){return level-1-currentLevel;}
	public int getCurrentColumn(){return currentColumn;}
}
class MerchandiseInfoPanel extends Panel  implements MouseListener,MouseMotionListener
{
	private MySQLprocessor mySQLprocessor;
	private int startY;
	private int y0,y1;
	public MerchandiseInfoPanel(int width,int height,MySQLprocessor mySQLprocessor)
	{
		this.mySQLprocessor=mySQLprocessor;
		String[] fields=new String[]{"merchandiseID","merchandiseName","merchandiseStocks","merchandiseClassification","merchandisePrice","merchandiseDescription"};
		String table="MerchandiseInfo";
		String fontName="Adobe Fan Heiti Std";
		int fontSize=12;
		this.startY=0;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		mySQLprocessor.createTable(width,height,fields,table,fontName,fontSize);
	}
	public void paint(Graphics g)
	{
		mySQLprocessor.drawTable(g,startY);
	}
	public void mousePressed(MouseEvent e)
	{
		this.y0=e.getY();
	}
	public void mouseDragged(MouseEvent e)
	{
		this.y1=e.getY();
		this.startY+=y1-y0;
		this.y0=y1;
		this.repaint();
	}
	public void mouseReleased(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseDown(MouseEvent e){}
	public void mouseUp(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
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
}
class MessageBox extends Thread
{
	private String message;
	private Font font;
	private int width,height,startX,startY;
	private String inputRemind,inputAnswer;
	public MessageBox(String message,int width,int height,String inputRemind,String inputAnswer)
	{
		super();
		this.message=message;
		this.width=width;
		this.height=height;
		this.font=new Font("Microsoft Tai Le",Font.BOLD,20);
		int screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.startX=(screenWidth-this.width)/2;
		this.startY=(screenHeight-this.height)/2;
		this.inputRemind=inputRemind;
		this.inputAnswer=inputAnswer;
	}
	public void run()
	{
		final Frame Frame1=new Frame(message);
		Frame1.setVisible(true);
		Frame1.setAlwaysOnTop(true);
		Frame1.setTitle(message);
		Frame1.setBounds(startX,startY,width,height);
		Frame1.setResizable(false);
		Frame1.setLayout(null);
		Label Label1=new Label(message);
		if(inputRemind!=null)
		{
			Label1.setBounds(width/10,height/5,4*width/5,height/5);
			final TextField TextField1=new TextField(inputRemind);
			TextField1.setFont(font);
			TextField1.setBounds(width/10,height/2,3*width/5,height/5);
			TextField1.setForeground(Color.gray);
			TextField1.addTextListener(new TextListener()
			{
				public void textValueChanged(TextEvent e)
				{
					TextField1.setForeground(Color.black);	
					TextField1.setEchoCharacter('*');
				}
			});
			Frame1.add(TextField1);
			Button Button1=new Button("OK");
			Button1.setFont(font);
			Button1.setBounds(7*width/10,height/2,width/5,height/5);
			Button1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String inputResult=TextField1.getText();
					if(!inputResult.equals(inputAnswer))System.exit(0);
					else Frame1.setVisible(false);
				}
			});
			Frame1.add(Label1);
			Frame1.add(Button1);
		}
		else Label1.setBounds(width/10,height/10,4*width/5,4*height/5);
		Label1.setFont(font);
		Frame1.add(Label1);
		Frame1.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if(inputRemind==null)Frame1.setVisible(false);
			}
		});
	}
}
class TextWindow extends Window
{
	int textLength;
	int textInterval=11;
	int charInterval=8;
	String[] texts;
	int width;
	int height;
	public TextWindow()
	{
		super(null);
		this.setBackground(Color.white);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	public void setPositionAndTexts(int startX,int startY,String[] texts)
	{
		if(texts==null){this.reshape(startX,startY,0,0);return;}
		this.texts=texts;
		this.textLength=texts.length;
		this.width=this.maxLength(texts)*charInterval;
		this.height=(this.textLength+1)*textInterval;
		this.reshape(startX,startY,width,height);
		this.repaint();
	}
	private int maxLength(String[] texts)
	{
		int maxlen=0;
		for(int i=0;i<textLength;i++)
		{
			int len=texts[i].length();
			if(len>maxlen)maxlen=len;
		}
		return maxlen;
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.black);
		g.drawRect(0,0,width-1,height-1);
		for(int i=0;i<textLength;i++)g.drawString(texts[i],0,(i+1)*textInterval);
	}
}
class Storehouse3D extends TransformGroup
{
	private float wallLength=2.0f;
	private float wallWidth=1.5f;
	private float wallHeight=1.0f;
	private float intervalLength;
	private float intervalWidth;
	private float shelfLength;
	private float shelfWidth;
	private float shelfHeight;
	private String dir="images\\";
	private String[] wallImages=new String[]{dir+"wall0.jpg",dir+"wall1.jpg",dir+"wall2.jpg",dir+"wall3.jpg",dir+"wall4.jpg",dir+"wall5.jpg"};
	private String shelfImage=dir+"shelf.jpg";
	public Storehouse3D(int storehouseRow,int storehouseColumn,int shelfColumn,int shelfLevel,MySQLprocessor mySQLprocessor)
	{
		this.intervalLength=wallLength/(3*storehouseColumn);
		this.intervalWidth=wallWidth/(1.3f*storehouseRow);
		this.shelfWidth=wallWidth/storehouseRow-intervalWidth;
		this.shelfLength=wallLength/storehouseColumn-intervalLength;
		this.shelfHeight=(shelfWidth<shelfLength/shelfColumn?shelfWidth:shelfLength/shelfColumn)*shelfLevel;
		if(this.shelfHeight>3*wallHeight/4)this.shelfHeight=3*wallHeight/4;
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.addChild(new ImageWalls3D(wallLength,wallWidth,wallHeight,wallImages));
		this.addChild(TransformGroup1);
		SharedGroup SharedGroup1=new SharedGroup();
		SharedGroup1.addChild(new Shelf3D(shelfLength,shelfWidth,shelfHeight,shelfColumn,shelfLevel,shelfImage));
		for(int i=0;i<storehouseRow;i++)
		{
			for(int j=0;j<storehouseColumn;j++)
			{
				float x=-wallLength/2+3*intervalLength/2+j*(shelfLength+intervalLength);
				float y=(shelfHeight-wallHeight)/2;
				float z=-wallWidth/2+intervalWidth+i*(shelfWidth+intervalWidth);
				Transform3D transform3D=new Transform3D();
				transform3D.setTranslation(new Vector3f(x,y,z));
				TransformGroup TransformGroup2=new TransformGroup(transform3D);
				TransformGroup2.addChild(new Link(SharedGroup1));
				this.addChild(TransformGroup2);
			}
		}
		float boxLength=9*(shelfLength/shelfColumn)/10;
		float boxHeight=9*(shelfHeight/shelfLevel)/10;
		float boxWidth=4*(shelfWidth)/5;
		int boxRow=1;
		int boxColumn=3;
		int boxLevel=3;
		double k=0.3;
		Font3D font3D=new Font3D(new Font("Microsoft Tai Le",Font.BOLD,1),null);
		Transform3D transform3D_Text3D=new Transform3D();
		transform3D_Text3D.setScale(new Vector3d(boxLength*k,boxHeight*k,0));
		transform3D_Text3D.setTranslation(new Vector3d(-boxLength/2,-boxHeight/4,shelfWidth/2));
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,1f,1f));
		Appearance1.setMaterial(Material1);
		SharedGroup SharedGroup2=new SharedGroup();
		SharedGroup2.addChild(new Box3D(boxLength,boxWidth,boxHeight,boxRow,boxColumn,boxLevel,"images\\texture0.jpg"));
		SharedGroup SharedGroup3=new SharedGroup();
		SharedGroup3.addChild(new Box3D(boxLength,boxWidth,boxHeight,boxRow,boxColumn,boxLevel,"images\\texture1.jpg"));
		try
		{
			ResultSet  ResultSet1=mySQLprocessor.executeQuery("Select * From MerchandiseLocationInfo,MerchandiseInfo Where MerchandiseLocationInfo.MerchandiseID=MerchandiseInfo.MerchandiseID");
			while(ResultSet1.next())
			{
				String merchandiseIDOfBox=ResultSet1.getInt("merchandiseID")+"";
				String merchandiseNameOfBox=ResultSet1.getString("merchandiseName");
				int storehouseRowOfBox=ResultSet1.getInt("storehouseRow");
				int storehouseColumnOfBox=ResultSet1.getInt("storehouseColumn");
				int shelfColumnOfBox=ResultSet1.getInt("shelfColumn");
				int shelfLevelOfBox=ResultSet1.getInt("shelfLevel");
				float x=-wallLength/2+3*intervalLength/2-shelfLength/2+storehouseColumnOfBox*(shelfLength+intervalLength)+shelfColumnOfBox*(shelfLength/shelfColumn)+(shelfLength/shelfColumn)/2;
				float y=-wallHeight/2+boxLevel*(shelfHeight/shelfLevel)+(shelfHeight/shelfLevel)/2;
				float z=-wallWidth/2+intervalWidth+storehouseRowOfBox*(shelfWidth+intervalWidth);
				Transform3D transform3D=new Transform3D();
				transform3D.setTranslation(new Vector3f(x,y,z));
				TransformGroup TransformGroup2=new TransformGroup(transform3D);
				TransformGroup2.addChild(new Link((Math.random()<0.5)?SharedGroup2:SharedGroup3));
				TransformGroup TransformGroup3=new TransformGroup(transform3D);
				TransformGroup TransformGroup4=new TransformGroup(transform3D_Text3D);
				TransformGroup3.addChild(TransformGroup4);
				TransformGroup4.addChild(new Shape3D(new Text3D(font3D,merchandiseIDOfBox+" "+merchandiseNameOfBox),Appearance1));
				this.addChild(TransformGroup2);
				this.addChild(TransformGroup3);
			}
		}
		catch(Exception e){e.printStackTrace();}
	}
}
class Image3D extends Shape3D
{
	public Image3D(float length,float width,String imageName)
	{
		Point3f p00=new Point3f(-length/2,-width/2,0);
		Point3f p01=new Point3f(-length/2,+width/2,0);
		Point3f p11=new Point3f(+length/2,+width/2,0);
		Point3f p10=new Point3f(+length/2,-width/2,0);
		TexCoord2f t00=new TexCoord2f(0f,0f);
		TexCoord2f t01=new TexCoord2f(0f,1f);
		TexCoord2f t11=new TexCoord2f(1f,1f);
		TexCoord2f t10=new TexCoord2f(1f,0f);
		Vector3f normal=new Vector3f(0f,0f,1f);
		QuadArray QuadArray1=new QuadArray(4,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.TEXTURE_COORDINATE_2);
		QuadArray1.setCoordinate(3,p00);
		QuadArray1.setCoordinate(2,p01);
		QuadArray1.setCoordinate(1,p11);
		QuadArray1.setCoordinate(0,p10);
		QuadArray1.setTextureCoordinate(0,3,t00);
		QuadArray1.setTextureCoordinate(0,2,t01);
		QuadArray1.setTextureCoordinate(0,1,t11);
		QuadArray1.setTextureCoordinate(0,0,t10);
		QuadArray1.setNormal(3,normal);
		QuadArray1.setNormal(2,normal);
		QuadArray1.setNormal(1,normal);
		QuadArray1.setNormal(0,normal);
		this.setGeometry(QuadArray1);
		this.setAppearance(this.getImageComponent2DAppearance(imageName,false));
	}
	public static Appearance getImageComponent2DAppearance(String imageName,boolean hasMaterial)
	{
		Appearance imageComponent2DAppearance=new Appearance();
		int imageWidth=1024,imageHeight=1024;
		BufferedImage BufferedImage1=new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_ARGB);
		BufferedImage BufferedImage2=null;
		try{BufferedImage2=ImageIO.read(new File(imageName));}catch(Exception e){}
		double scaleX=(imageWidth+0.0)/BufferedImage2.getWidth();
		double scaleY=(imageHeight+0.0)/BufferedImage2.getHeight();
		AffineTransform AffineTransform1=new AffineTransform();
		AffineTransform1.setToScale(scaleX,scaleY);
		AffineTransformOp AffineTransformOp1=new AffineTransformOp(AffineTransform1,AffineTransformOp.TYPE_BILINEAR);
		BufferedImage BufferedImage3=AffineTransformOp1.filter(BufferedImage2,null);
		ImageComponent2D imageComponent2D=new ImageComponent2D(ImageComponent2D.FORMAT_RGBA,BufferedImage3);
		Texture2D texture2D=new Texture2D(Texture.BASE_LEVEL,Texture.RGBA,imageComponent2D.getWidth(),imageComponent2D.getHeight());
		texture2D.setImage(0,imageComponent2D);
		texture2D.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		imageComponent2DAppearance.setTexture(texture2D);
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		imageComponent2DAppearance.setTextureAttributes(TextureAttributes1);
		if(hasMaterial)imageComponent2DAppearance.setMaterial(new Material());
		return imageComponent2DAppearance;
	}
}
class ImageWalls3D extends TransformGroup
{
	public ImageWalls3D(float length,float width,float height,String[] images)
	{
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0,0,-width/2.0));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(length,height,images[0]));
		this.addChild(TransformGroup1);
		transform3D.rotY(Math.PI);
		transform3D.setTranslation(new Vector3d(0,0,width/2.0));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(length,height,images[1]));
		this.addChild(TransformGroup1);
		transform3D.rotY(Math.PI/2);
		transform3D.setTranslation(new Vector3d(-length/2.0,0,0));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(width,height,images[2]));
		this.addChild(TransformGroup1);
		transform3D.rotY(3*Math.PI/2);
		transform3D.setTranslation(new Vector3d(length/2.0,0,0));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(width,height,images[3]));
		this.addChild(TransformGroup1);
		transform3D.rotX(-Math.PI/2);
		transform3D.setTranslation(new Vector3d(0,-height/2.0,0));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(length,width,images[4]));
		this.addChild(TransformGroup1);
		transform3D.rotX(Math.PI/2);
		transform3D.setTranslation(new Vector3d(0,height/2.0,0));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(length,width,images[5]));
		this.addChild(TransformGroup1);
	}
}
class Box3D extends Shape3D
{
	public Box3D(float length,float width,float height,int row,int column,int level,String textureImage)
	{
		float l=length/column;
		float w=width/row;
		float h=height/level;
		int QuadArrayNumber=2*level*column*4+2*level*row*4+2*row*column*4;
		QuadArray QuadArray1=new QuadArray(QuadArrayNumber,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.TEXTURE_COORDINATE_2);
		int c=0,t=0,n=0;
		TexCoord2f t00=new TexCoord2f(0f,0f);
		TexCoord2f t01=new TexCoord2f(0f,1f);
		TexCoord2f t11=new TexCoord2f(1f,1f);
		TexCoord2f t10=new TexCoord2f(1f,0f);
		for(int i=0;i<level;i++)
		{
			for(int j=0;j<column;j++)
			{
				float x0=-length/2+j*l;
				float x1=-length/2+(j+1)*l;
				float y0=height/2-i*h;
				float y1=height/2-(i+1)*h;
				float z=-width/2;
				QuadArray1.setCoordinate(c++,new Point3f(x0,y0,z));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y0,z));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y1,z));
				QuadArray1.setCoordinate(c++,new Point3f(x0,y1,z));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			}
		}
		for(int i=0;i<level;i++)
		{
			for(int j=0;j<column;j++)
			{
				float x0=-length/2+j*l;
				float x1=-length/2+(j+1)*l;
				float y0=height/2-i*h;
				float y1=height/2-(i+1)*h;
				float z=width/2;
				QuadArray1.setCoordinate(c++,new Point3f(x0,y0,z));
				QuadArray1.setCoordinate(c++,new Point3f(x0,y1,z));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y1,z));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y0,z));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			}
		}
		for(int i=0;i<level;i++)
		{
			for(int j=0;j<row;j++)
			{
				float x=-length/2;
				float y0=height/2-i*h;
				float y1=height/2-(i+1)*h;
				float z0=width/2-j*w;
				float z1=width/2-(j+1)*w;
				QuadArray1.setCoordinate(c++,new Point3f(x,y0,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x,y0,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x,y1,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x,y1,z0));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
			}
		}
		for(int i=0;i<level;i++)
		{
			for(int j=0;j<row;j++)
			{
				float x=length/2;
				float y0=height/2-i*h;
				float y1=height/2-(i+1)*h;
				float z0=width/2-j*w;
				float z1=width/2-(j+1)*w;
				QuadArray1.setCoordinate(c++,new Point3f(x,y0,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x,y1,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x,y1,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x,y0,z1));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
			}
		}
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				float y=-height/2;
				float x0=-length/2+j*l;
				float x1=-length/2+(j+1)*l;
				float y1=height/2-(i+1)*h;
				float z0=-width/2+i*w;
				float z1=-width/2+(i+1)*w;
				QuadArray1.setCoordinate(c++,new Point3f(x0,y,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x0,y,z1));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
			}
		}
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				float y=height/2;
				float x0=-length/2+j*l;
				float x1=-length/2+(j+1)*l;
				float y1=height/2-(i+1)*h;
				float z0=-width/2+i*w;
				float z1=-width/2+(i+1)*w;
				QuadArray1.setCoordinate(c++,new Point3f(x0,y,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x0,y,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y,z0));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
			}
		}
		this.setGeometry(QuadArray1);
		this.setAppearance(Image3D.getImageComponent2DAppearance(textureImage,false));
	}
}
class Shelf3D extends Shape3D
{
	public Shelf3D(float length,float width,float height,int column,int level,String textureImage)
	{
		float l=length/column;
		float h=height/level;
		int k=10;
		float tl=l/k;
		float th=h/k;
		int QuadArrayNumber=4*(level+1)*4+4*(column+1)*4;
		QuadArray QuadArray1=new QuadArray(QuadArrayNumber,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.TEXTURE_COORDINATE_2);
		int c=0,t=0,n=0;
		TexCoord2f t00=new TexCoord2f(0f,0f);
		TexCoord2f t01=new TexCoord2f(0f,1f);
		TexCoord2f t11=new TexCoord2f(1f,1f);
		TexCoord2f t10=new TexCoord2f(1f,0f);
		for(int i=0;i<=level;i++)
		{
			Point3f p00=new Point3f(-length/2,height/2-i*h+th,-width/2);
			Point3f p01=new Point3f(-length/2,height/2-i*h+th,+width/2);
			Point3f p11=new Point3f(+length/2+tl,height/2-i*h+th,+width/2);
			Point3f p10=new Point3f(+length/2+tl,height/2-i*h+th,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
		}
		for(int i=0;i<=level;i++)
		{
			Point3f p00=new Point3f(-length/2,height/2-i*h,-width/2);
			Point3f p01=new Point3f(-length/2,height/2-i*h,+width/2);
			Point3f p11=new Point3f(+length/2+tl,height/2-i*h,+width/2);
			Point3f p10=new Point3f(+length/2+tl,height/2-i*h,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
		}
		for(int i=0;i<=level;i++)
		{
			Point3f p00=new Point3f(-length/2,height/2-i*h+th,width/2);
			Point3f p01=new Point3f(-length/2,height/2-i*h,width/2);
			Point3f p11=new Point3f(+length/2+tl,height/2-i*h,width/2);
			Point3f p10=new Point3f(+length/2+tl,height/2-i*h+th,width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
		}
		for(int i=0;i<=level;i++)
		{
			Point3f p00=new Point3f(-length/2,height/2-i*h+th,-width/2);
			Point3f p01=new Point3f(-length/2,height/2-i*h,-width/2);
			Point3f p11=new Point3f(+length/2+tl,height/2-i*h,-width/2);
			Point3f p10=new Point3f(+length/2+tl,height/2-i*h+th,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
		}
		for(int j=0;j<=column;j++)
		{
			Point3f p00=new Point3f(-length/2+j*l,-height/2,-width/2);
			Point3f p01=new Point3f(-length/2+j*l,-height/2,+width/2);
			Point3f p11=new Point3f(-length/2+j*l,+height/2+th,+width/2);
			Point3f p10=new Point3f(-length/2+j*l,+height/2+th,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
		}
		for(int j=0;j<=column;j++)
		{
			Point3f p00=new Point3f(-length/2+j*l+tl,-height/2,-width/2);
			Point3f p01=new Point3f(-length/2+j*l+tl,-height/2,+width/2);
			Point3f p11=new Point3f(-length/2+j*l+tl,+height/2+th,+width/2);
			Point3f p10=new Point3f(-length/2+j*l+tl,+height/2+th,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
		}
		for(int j=0;j<=column;j++)
		{
			Point3f p00=new Point3f(-length/2+j*l+0,-height/2+th,-width/2);
			Point3f p01=new Point3f(-length/2+j*l+0,+height/2+th,-width/2);
			Point3f p11=new Point3f(-length/2+j*l+tl,+height/2+th,-width/2);
			Point3f p10=new Point3f(-length/2+j*l+tl,-height/2+th,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
		}
		for(int j=0;j<=column;j++)
		{
			Point3f p00=new Point3f(-length/2+j*l+0,-height/2+th,width/2);
			Point3f p01=new Point3f(-length/2+j*l+0,+height/2+th,width/2);
			Point3f p11=new Point3f(-length/2+j*l+tl,+height/2+th,width/2);
			Point3f p10=new Point3f(-length/2+j*l+tl,-height/2+th,width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
		}
		this.setGeometry(QuadArray1);
		this.setAppearance(Image3D.getImageComponent2DAppearance(textureImage,false));
	}
}
class StringQueue
{
	private String stringQueue;
	public StringQueue()
	{
		this.stringQueue="";
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