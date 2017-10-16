import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.awt.geom.*;
import java.applet.*;
import javax.swing.*;
import java.sql.*;
public class JavaLanguagePrograming 
{
	public static void main(String[] args)
	{
		final Frame Frame1=new Frame("title1");
		final Font Font1=new Font("Î¢ÈíÑÅºÚ",Font.BOLD,30);
		final TextArea TextArea1=new TextArea("TextArea1");
		final Image Image1=Toolkit.getDefaultToolkit().getImage("D:\\ÎÒµÄÎÄµµ\\My Pictures\\Green Pictures\\Í¼Æ¬ÊÕ²Ø\\Vista_ºÚÒ¹¾«Áé.jpg");						
		TextArea1.setEditable(true);
		TextArea1.addTextListener(new TextListener()
		{
			public void textValueChanged(TextEvent e)
			{
				TextArea1.setBackground(Color.black);
				TextArea1.setForeground(Color.white);
				TextArea1.setFont(Font1);		
			}
		});						
		final TextField TextField1=new TextField();
		TextField1.addTextListener(new TextListener()
		{
			public void textValueChanged(TextEvent e)
			{
				TextField1.setForeground(Color.black);	
				TextField1.setEchoCharacter('*');
			}
		});
		final FileDialog FileDialog1=new FileDialog(Frame1,"FileDialog1",FileDialog.LOAD);
		final FileDialog FileDialog2=new FileDialog(Frame1,"FileDialog2",FileDialog.SAVE);
			
		CheckboxGroup CheckboxGroup1=new CheckboxGroup();
		final Checkbox Checkbox1=new Checkbox("Checkbox1",CheckboxGroup1,true);
		final Checkbox Checkbox2=new Checkbox("Checkbox2",CheckboxGroup1,true);
		CheckboxGroup1.setSelectedCheckbox(Checkbox1);
		Checkbox1.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getItemSelectable()==Checkbox1)
				{
					System.out.println("Checkbox1");
					FileDialog1.setVisible(true);
					File[] Files=(new File(FileDialog1.getDirectory())).listFiles();
					for(int i=0;i<Files.length;i++)TextArea1.append(Files[i].getName()+"\n");
				}
			}
		});
		Checkbox2.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getItemSelectable()==Checkbox2)System.out.println("Checkbox2");
				FileDialog2.setVisible(true);
				try
				{
					File File1=new File(FileDialog2.getDirectory()+FileDialog2.getFile());
					if(File1.exists())File1.delete();
					else File1.createNewFile();
				}
				catch (Exception ex){}				
			}
		});
		
		
		final Choice Choice1=new Choice();
		Choice1.add("Choice1.0");
		Choice1.add("Choice1.1");
		Choice1.add("Choice1.2");
		Choice1.add("Choice1.3");
		Choice1.add("Choice1.4");		
		Choice1.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getItemSelectable()==Choice1)
				{
					switch(Choice1.getSelectedIndex())
					{
						case 0:System.out.println("Choice1.0");break;
						case 1:System.out.println("Choice1.1");break;
						case 2:System.out.println("Choice1.2");break;
						case 3:System.out.println("Choice1.3");break;
						case 4:System.out.println("Choice1.4");break;						
					}
				}				
			}
		});




		
		final Clipboard Clipboard1=Toolkit.getDefaultToolkit().getSystemClipboard();
		
		MenuItem MenuItem1=new MenuItem("MenuItem  E");
		MenuShortcut MenuShortcut1=new MenuShortcut(KeyEvent.VK_E); 
		MenuItem1.setShortcut(MenuShortcut1);
		MenuItem1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("MenuItem1");
				Clipboard1.setContents(new StringSelection(TextArea1.getSelectedText()) ,null);
				TextArea1.replaceRange("Clipboard",TextArea1.getSelectionStart(),TextArea1.getSelectionEnd());
				try {TextArea1.append("\n"+Clipboard1.getContents(null).getTransferData(DataFlavor.stringFlavor));}
				catch(Exception ex){}
			}
		});		
		CheckboxMenuItem CheckboxMenuItem1=new CheckboxMenuItem("CheckboxMenuItem1");		
		CheckboxMenuItem1.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				System.out.println("CheckboxMenuItem1");
								
			}
		});
		Menu Menu1=new Menu("Menu1"),Menu2=new Menu("Menu2");		
		Menu1.add(MenuItem1);
		Menu1.addSeparator();
		Menu1.add(CheckboxMenuItem1);
		MenuBar MenuBar1=new MenuBar();		
		MenuBar1.add(Menu1);
		MenuBar1.add(Menu2);
		
		
		
								
		Button Button1=new Button("Button1");
		Button1.setBackground(Color.WHITE);
		Button1.setBounds(100,100,100,100);
		Button1.addActionListener(new ActionListener()
		{			
			public void actionPerformed(ActionEvent e)
			{
				System.out.print(TextArea1.getText());
				FileDialog1.setVisible(true);
				try
				{
					BufferedReader BufferedReader1=new BufferedReader(new FileReader(FileDialog1.getDirectory()+FileDialog1.getFile()));
					String s=BufferedReader1.readLine();					
					while(s!=null)
					{
						TextArea1.append(s+"\n");
						s=BufferedReader1.readLine();
					}
					BufferedReader1.close();
				}
				catch(Exception ex){ex.printStackTrace();}
				FileDialog2.setVisible(true);
				try
				{
					PrintWriter PrintWriter1=new PrintWriter(FileDialog2.getDirectory()+FileDialog2.getFile());
					PrintWriter1.write(TextArea1.getText());
					PrintWriter1.close();
				}
				catch(Exception ex){ex.printStackTrace();}
				FileDialog1.setVisible(true);
				try
				{
					Runtime.getRuntime().exec(FileDialog1.getDirectory()+FileDialog1.getFile());										
				}
				catch(Exception ex){ex.printStackTrace();}
				
			}
		});
		
		Label Label1=new Label("Label1");
		
		
		
		Panel Panel1=new Panel(new BorderLayout())
		{
			public void paint(Graphics g)
			{
				g.drawString("Panel1",80,50);
			}
		};				
		Panel1.add(Button1,"North");
		Panel1.add(Label1,"South");
		Panel1.add(TextArea1,"East");
		Panel1.add(Checkbox1,"West");	
		Panel1.add(Checkbox2);
	
		
		
		Frame1.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		//Frame1.setVisible(true);
		Frame1.add(Panel1,"West");
		Frame1.setMenuBar(MenuBar1);
		Frame1.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
		
		

		
		final List List1=new List();
		List1.add("List1.0");
		List1.add("List1.1");
		List1.add("List1.2");
		List1.add("List1.3");
		List1.add("List1.4");		
		List1.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getItemSelectable()==List1)
				{
					switch(List1.getSelectedIndex())
					{
						System.out.println(List1.getSelectedItem());
						case 0:
							System.out.println("List1.0");
							FileDialog1.setVisible(true);
							try
							{
								final AudioClip AudioClip1=Applet.newAudioClip(new File(FileDialog1.getDirectory(),FileDialog1.getFile()).toURI().toURL());
								AudioClip1.play();								
							}
							catch(Exception ex){}
							break;
						case 1:
							System.out.println("List1.1");
							
							break;
						case 2:System.out.println("List1.2");break;
						case 3:System.out.println("List1.3");break;
						case 4:System.out.println("List1.4");break;						
					}
				}				
			}
		});
		
		
		
		final Window Window1=new Window(null)
		{									
			public void paint(Graphics g)
			{
				g.drawImage(Image1,0,0,Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height,Frame1);
				g.setFont(Font1);
				g.setColor(Color.white);
				g.drawString("Window1",100,100);
				Graphics2D g2D=((Graphics2D)g);
				g2D.setStroke(new BasicStroke(10));				
				g2D.draw(new Line2D.Double(100,200,300,200)); 
				g2D.draw(new Rectangle2D.Double(100,300,200,200));
				g2D.clearRect(100-1,300-1,200,200);						
			}
		};
			
								
					
		Window1.setBackground(Color.black);
		Window1.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height);														
		Window1.add(Choice1,"North");
		Window1.add(List1,"South");						
		//Window1.setVisible(true);
		Window1.setFocusable(true);
		Window1.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				System.out.println("keyPressed:"+e.getKeyChar());
				int code=e.getKeyCode();
				String t=KeyEvent.getKeyText(e.getKeyCode());
				if(t.equals("Backspace"))System.out.println("Backspace");
				else if(t.equals("Enter"))System.out.println("Enter");
				else if(t.equals("Home"))System.out.println("Home");
				else if(t.equals("Delete"))System.out.println("Delete");
				else if(t.equals("Left"))System.out.println("Page Up");
				else if(t.equals("Right"))System.out.println("Page Down");
				else if(t.equals("Up"))System.out.println("Up");
				else if(t.equals("Down"))System.out.println("Down");
				else if(t.equals("Page Up"))System.out.println("Page Up");
				else if(t.equals("Page Down"))System.out.println("Page Down");
			}
			public void keyTyped(KeyEvent e)
			{
				System.out.println("keyTyped:"+e.getKeyChar());
			}
			public void keyReleased(KeyEvent e)
			{
				System.out.println("keyReleased:"+e.getKeyChar());
			}

		});
		Window1.addMouseListener(new MouseListener()
		{
			public void mousePressed(MouseEvent e)
			{
				System.out.println("mousePressed("+e.getX()+","+e.getY()+")");
			}
			public void mouseClicked(MouseEvent e)
			{
				System.out.println("mouseClicked("+e.getX()+","+e.getY()+")");
			}			
			public void mouseReleased(MouseEvent e)
			{
				System.out.println("mouseReleased("+e.getX()+","+e.getY()+")");
			}
			public void mouseEntered(MouseEvent e)
			{
				System.out.println("mouseEntered("+e.getX()+","+e.getY()+")");
			}
			public void mouseExited(MouseEvent e)
			{
				System.out.println("mouseExited("+e.getX()+","+e.getY()+")");
			}
		});
		
		Window1.addMouseMotionListener(new MouseMotionListener()
		{
			public void mouseDragged(MouseEvent e)
			{
				System.out.println("mouseDragged("+e.getX()+","+e.getY()+")");
			}
			public void mouseMoved(MouseEvent e)
			{
				System.out.println("mouseMoved("+e.getX()+","+e.getY()+")");
			}
		});
		
		
		
		
		JPanel JPanel1=new JPanel();
		JPanel1.setLayout(new BorderLayout());
		
		JTextArea JTextArea0=new JTextArea("JTextArea0");
		JTextArea JTextArea1=new JTextArea("JTextArea1");
		JTextArea JTextArea2=new JTextArea("JTextArea2");
		JTextArea JTextArea3=new JTextArea("JTextArea3");
		
		JScrollPane JScrollPane1=new JScrollPane(JTextArea0);
		JPanel1.add(JScrollPane1,"West");	
			
		JSplitPane JSplitPane1=new JSplitPane(0,JTextArea1,JTextArea2);
		JSplitPane JSplitPane2=new JSplitPane(1,JSplitPane1,JTextArea3);
		JPanel1.add(JSplitPane2,"South");
		
		JPasswordField JPasswordField1=new JPasswordField("JPasswordField1");
		JPasswordField1.setEchoChar('*');
				
		JTextPane JTextPane1=new JTextPane();
		
		JTextPane1.insertIcon(new ImageIcon("W.jpg"));
		try{JTextPane1.read(new FileInputStream("T.txt"),null);}catch(Exception e){}
		
		JPanel JPanel2=new JPanel();
		JPanel2.setLayout(new BorderLayout());
		JPanel2.add(JPasswordField1,"North");
		JPanel2.add(JTextPane1,"South");
		
			
		JButton JButton1=new JButton("JButton1",new ImageIcon(Image1));
		JInternalFrame JInternalFrame1=new JInternalFrame("JInternalFrame1");
		JInternalFrame1.setSize(200,200);		 
		JInternalFrame1.add(JButton1);
		JInternalFrame1.setVisible(true);
	
		
		JDesktopPane JDesktopPane1=new JDesktopPane();		
		JDesktopPane1.add(JInternalFrame1);
		 
		 
		

				
		JFrame JFrame1=new JFrame("JFrame1");
		JFrame1.setSize(Toolkit.getDefaultToolkit().getScreenSize());				
		JFrame1.add(JPanel1,"West");
		JFrame1.add(JPanel2,"North");				 				 
		JFrame1.setVisible(true);
		JFrame1.add(JDesktopPane1,"Center");
	
		
				
		JWindow JWindow1=new JWindow()
		{
			public void paint(Graphics g)
			{
				g.drawImage(Image1,0,0,Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height,Frame1);
				g.setFont(Font1);
				g.setColor(Color.white);
				g.drawString("JWindow1",100,100);									
			}
		};
		JWindow1.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		JWindow1.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				System.out.println("keyPressed:"+e.getKeyChar());
			}
			public void keyTyped(KeyEvent e)
			{
				System.out.println("keyTyped:"+e.getKeyChar());
			}
			public void keyReleased(KeyEvent e)
			{
				System.out.println("keyReleased:"+e.getKeyChar());
			}
		});
		
		JWindow1.addMouseListener(new MouseListener()
		{
			public void mousePressed(MouseEvent e)
			{
				System.out.println("mousePressed("+e.getX()+","+e.getY()+")");
			}
			public void mouseClicked(MouseEvent e)
			{
				System.out.println("mouseClicked("+e.getX()+","+e.getY()+")");
			}			
			public void mouseReleased(MouseEvent e)
			{
				System.out.println("mouseReleased("+e.getX()+","+e.getY()+")");
			}
			public void mouseEntered(MouseEvent e)
			{
				System.out.println("mouseEntered("+e.getX()+","+e.getY()+")");
			}
			public void mouseExited(MouseEvent e)
			{
				System.out.println("mouseExited("+e.getX()+","+e.getY()+")");
			}
		});
		
		JWindow1.addMouseMotionListener(new MouseMotionListener()
		{
			public void mouseDragged(MouseEvent e)
			{
				System.out.println("mouseDragged("+e.getX()+","+e.getY()+")");
			}
			public void mouseMoved(MouseEvent e)
			{
				System.out.println("mouseMoved("+e.getX()+","+e.getY()+")");
			}
		});
		JWindow1.setLayout(null);
		JTextArea1=new JTextArea("JTextArea1");
		JScrollPane1=new JScrollPane(JTextArea1);
		JScrollPane1.setBounds(300,300,100,100);				
		JWindow1.add(JScrollPane1);
	//	JWindow1.setVisible(true);
	
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection Connection1=DriverManager.getConnection("jdbc:odbc:DataSource1");
			
			String StructuredQueryLanguage="Select Key,Data1,Data2 From Table1";System.out.println(StructuredQueryLanguage);			
			ResultSet ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);	
			while(ResultSet1.next())System.out.println("Row:"+ResultSet1.getRow()+" Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select * From Table1";System.out.println(StructuredQueryLanguage);			
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			ResultSetMetaData ResultSetMetaData1=ResultSet1.getMetaData();
			for(int i=1;i<=ResultSetMetaData1.getColumnCount();i++)System.out.println("CollumnClassName:"+ResultSetMetaData1.getColumnClassName(i)+"  ColumnName:"+ResultSetMetaData1.getColumnName(i)+" getColumnTypeName:"+ResultSetMetaData1.getColumnTypeName(i));						
			
			StructuredQueryLanguage="Select Key,Data2,Data2*0.1 As Data4 From Table1";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2")+"  Data4:"+ResultSet1.getString("Data4"));								
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2>=60";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2 Like '5%'";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2 Not Like '5%'";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2 Between 90 And 60";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2 Not In (50,70,90)";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Key>2 And Data2>=60";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2<60 Or Key<2";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Order By Data2";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Insert Into Table1 (Key,Data1,Data2) Values (11,'String11',0)";System.out.println(StructuredQueryLanguage);
			Connection1.createStatement().execute(StructuredQueryLanguage);										
			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1";System.out.println(StructuredQueryLanguage);			
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);										
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Delete * From Table1 Where Data1='String11'";System.out.println(StructuredQueryLanguage);
			Connection1.createStatement().execute(StructuredQueryLanguage);										
			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1";System.out.println(StructuredQueryLanguage);			
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);										
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Update Table1  Set Data2=0 Where Data1='String10'";System.out.println(StructuredQueryLanguage);
			Connection1.createStatement().execute(StructuredQueryLanguage);										
			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1";System.out.println(StructuredQueryLanguage);			
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);										
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
			
			ResultSet1.close();
			Connection1.close();
		}
		catch(Exception e){e.printStackTrace();}		

							
	}	
}

							


