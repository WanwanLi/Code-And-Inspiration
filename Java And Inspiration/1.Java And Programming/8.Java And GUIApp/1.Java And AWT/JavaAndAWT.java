import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.applet.*;
public class JavaAndAWT
{
	public static void main(String[] args)
	{
		new Frame1();							
	}	
}	
class Frame1 extends Frame implements MouseListener,MouseMotionListener
{
	Font Font1;
	TextArea TextArea1;
	Image Image1;
	FileDialog FileDialog1;
	FileDialog FileDialog2;
	CheckboxGroup CheckboxGroup1;
	Checkbox Checkbox1;
	Checkbox Checkbox2;
	Choice Choice1;
	Clipboard Clipboard1;
	MenuItem MenuItem1;
	MenuShortcut MenuShortcut1;
	CheckboxMenuItem CheckboxMenuItem1;
	Menu Menu1;
	Menu Menu2;
	MenuBar MenuBar1;
	Button Button1;
	Label Label1;
	Panel Panel1;
	Panel Panel2;
	List List1;
	public Frame1()
	{
		Font1=new Font("Î¢ÈíÑÅºÚ",Font.BOLD,30);		
		Image1=Toolkit.getDefaultToolkit().getImage("W.jpg");
		TextArea1=new TextArea("TextArea1");						
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


		FileDialog1=new FileDialog(this,"FileDialog1",FileDialog.LOAD);
		FileDialog2=new FileDialog(this,"FileDialog2",FileDialog.SAVE);
			
		CheckboxGroup1=new CheckboxGroup();
		Checkbox1=new Checkbox("Checkbox1",CheckboxGroup1,true);
		Checkbox2=new Checkbox("Checkbox2",CheckboxGroup1,true);
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
		
		
		Choice1=new Choice();
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
					String string=Choice1.getSelectedItem();
				}				
			}
		});

		
		Clipboard1=Toolkit.getDefaultToolkit().getSystemClipboard();
		
		MenuItem1=new MenuItem("MenuItem  E");
		MenuShortcut1=new MenuShortcut(KeyEvent.VK_E); 
		MenuItem1.setShortcut(MenuShortcut1);
		MenuItem1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("MenuItem1");
				Clipboard1.setContents(new StringSelection(TextArea1.getSelectedText()) ,null);
				TextArea1.replaceRange("Clipboard",TextArea1.getSelectionStart(),TextArea1.getSelectionEnd());
				try 
				{
					TextArea1.append("\n"+Clipboard1.getContents(null).getTransferData(DataFlavor.stringFlavor));
				}
				catch(Exception ex){}
			}
		});		
		CheckboxMenuItem1=new CheckboxMenuItem("CheckboxMenuItem1");		
		CheckboxMenuItem1.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				System.out.println("CheckboxMenuItem1");								
			}
		});
		Menu1=new Menu("Menu1");
		Menu2=new Menu("Menu2");		
		Menu1.add(MenuItem1);
		Menu1.addSeparator();
		Menu1.add(CheckboxMenuItem1);
		MenuBar1=new MenuBar();		
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
		Label1=new Label("Label1");
		List1=new List();
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
						case 0:
						{
							System.out.println("List1.0");
							FileDialog1.setVisible(true);
							try
							{
     	         							AudioClip AudioClip1=Applet.newAudioClip(new File(FileDialog1.getDirectory(),FileDialog1.getFile()).toURI().toURL());
								AudioClip1.play();								
							}
							catch(Exception ex){}
							break;
						}
						case 1:System.out.println("List1.1");break;
						case 2:System.out.println("List1.2");break;
						case 3:System.out.println("List1.3");break;
						case 4:System.out.println("List1.4");break;						
					}
				}				
			}
		});
		Panel1=new Panel(new BorderLayout());
		Panel2=new Panel()
		{
			public void paint(Graphics g)
			{
				g.drawImage(Image1,0,0,this);
			}
		};				
		Panel1.add(Button1,"North");
		Panel1.add(Label1,"South");
		Panel1.add(TextArea1,"East");	
		Panel1.add(List1,"Center");
		Panel2.add(Checkbox1);
		Panel2.add(Checkbox2);	
		
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setVisible(true);
		this.setResizable(false);
		this.add(Panel1,"West");
		this.add(Panel2,"Center");
		this.setMenuBar(MenuBar1);
		this.setFocusable(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	public void mouseExited(MouseEvent e){System.out.println("mouseExited");}
	public void mouseEntered(MouseEvent e){System.out.println("mouseEntered");}
	public void mouseDown(MouseEvent e){System.out.println("mouseDown");}
	public void mouseUp(MouseEvent e){System.out.println("mouseUp");}
	public void mouseReleased(MouseEvent e){System.out.println("mouseReleased");}
	public void mousePressed(MouseEvent e){System.out.println("mousePressed");}
	public void mouseClicked(MouseEvent e){System.out.println("mouseClicked");}
	public void mouseMoved(MouseEvent e){System.out.println("mouseMoved");}
	public void mouseDragged(MouseEvent e){System.out.println("mouseDragged");}
}