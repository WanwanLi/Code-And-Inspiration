import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
public class JavaAndSwing
{
	public static void main(String[] args)
	{
		new JFrame1();		
	}
}


class JFrame1 extends JFrame
{
	JButton JButton2;	
	JFrame JFrame2;
	int i=0;
	public JFrame1()
	{
		i=0;
		JPanel JPanel1=new JPanel();
		JPanel1.setLayout(new BorderLayout());
		Image Image1=Toolkit.getDefaultToolkit().getImage("W.jgp");
		JTextArea JTextArea0=new JTextArea("JTextArea0");
		JTextArea JTextArea1=new JTextArea("JTextArea1");
		JTextArea JTextArea2=new JTextArea("JTextArea2");
		JTextArea JTextArea3=new JTextArea("JTextArea3");
		
		JScrollPane JScrollPane1=new JScrollPane(JTextArea0);
		JPanel1.add(JScrollPane1,"South");	
			
		JSplitPane JSplitPane1=new JSplitPane(0,JTextArea1,JTextArea2);
		JSplitPane JSplitPane2=new JSplitPane(1,JSplitPane1,JTextArea3);
		JPanel1.add(JSplitPane2,"West");
		
		JPasswordField JPasswordField1=new JPasswordField("JPasswordField1");
		JPasswordField1.setEchoChar('*');
				
		
		JTextPane JTextPane1=new JTextPane();		
		//JTextPane1.insertIcon(new ImageIcon("W.jpg"));		
		try
		{
			JTextPane1.read(new FileInputStream("T.txt"),null);
		}
		catch(Exception e){}
		
		JPanel JPanel2=new JPanel();
		JPanel2.setLayout(new BorderLayout());
		JPanel2.add(JPasswordField1,"North");
		JPanel2.add(JTextPane1,"Center");

		JButton JButton1=new JButton("JButton1",new ImageIcon(Image1));
		JButton1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Button1");
				JFrame JFrame2=new JFrame();
				JFrame2.setBounds(100,100,400,400);
				String[] Strings={"String0","String1","String2","String3","String4","String5","String6","String7","String8","String9"};
				final JComboBox JComboBox1=new JComboBox(Strings);
				JFrame2.add(JComboBox1,"North");
				JComboBox1.addItemListener(new ItemListener()
				{
					public void itemStateChanged(ItemEvent e)
					{
						
						if(JComboBox1.getSelectedItem().equals("String1"))
						{
							JFileChooser JFileChooser1=new JFileChooser();							
							JFileChooser1.showDialog(null,"Open Text File");
							String s="";
							try
							{
						      		BufferedReader BufferedReader1=new BufferedReader(new FileReader(JFileChooser1.getSelectedFile().getPath()));
								s=BufferedReader1.readLine();
								while(s!=null)
								{
									System.out.println(s);
									s=BufferedReader1.readLine();
								}
								BufferedReader1.close();
							}
							catch(Exception ex){}
						}
						else if(JComboBox1.getSelectedItem().equals("String2"))
						{
							JFileChooser JFileChooser1=new JFileChooser();							
							JFileChooser1.showDialog(null,"Save Text File");
							String s="JFileChooser1.String";
							try
							{
						      		PrintWriter PrintWriter1=new PrintWriter(JFileChooser1.getSelectedFile().getPath());
								PrintWriter1.print(s);
								PrintWriter1.close();
							}
							catch(Exception ex){}
						}
						else if(JComboBox1.getSelectedItem().equals("String3"))
						{
							JFrame JFrame3=new JFrame();
							JFrame3.setBounds(400,400,50,80);
							final JProgressBar JProgressBar1=new JProgressBar();
							JFrame3.add(JProgressBar1);
							Timer Timer1=new Timer(100,new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									i++;
									if(i==100)System.exit(0);
									JProgressBar1.setValue(i);
									JProgressBar1.setString(String.valueOf(i)+"%");
								}
							});
							Timer1.start();
							JFrame3.setVisible(true);
						}
						else if(JComboBox1.getSelectedItem().equals("String4"))
						{
							
							JFrame JFrame4=new JFrame();
							JFrame4.setBounds(400,400,300,300);
							TableModel TableModel1=new AbstractTableModel()
							{
								public int getColumnCount()	
								{
									return 10;
								}
								public int getRowCount()	
								{
									return 30;
								}
								public Object getValueAt(int row,int col)	
								{
									return new Integer(row*col);
								}
							};
							JTable JTable0=new JTable(TableModel1);
							final JTable JTable1=new JTable(10,6);
							int R=JTable1.getRowCount();
							int C=JTable1.getColumnCount();
							for(int r=0;r<R;r++)
								for(int c=0;c<C;c++)
									JTable1.setValueAt(c*r,r,c);
							JButton JButton4=new JButton("JButton4");
							JButton4.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									System.out.println("JTable1.getValueAt(0,0)="+String.valueOf(JTable1.getValueAt(0,0)));
								}
							});
							JScrollPane JScrollPane1=new JScrollPane(JTable1);
							JFrame4.add(JScrollPane1);
							JFrame4.add(JButton4,"South");
							JFrame4.setVisible(true);			
						}
						else if(JComboBox1.getSelectedItem().equals("String5"))
						{
							
							JFrame JFrame5=new JFrame();
							JFrame5.setBounds(400,400,400,600);
							JToolBar JToolBar1=new JToolBar();
							JButton JButton5_0=new JButton(new ImageIcon("Icon\\W0.jpg"));
							JButton JButton5_1=new JButton(new ImageIcon("Icon\\W1.jpg"));
							JButton JButton5_2=new JButton(new ImageIcon("Icon\\W2.jpg"));
							JButton5_0.setToolTipText("JButton5_0");
							JButton5_1.setToolTipText("JButton5_1");
							JButton5_2.setToolTipText("JButton5_2");
							JButton5_0.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									System.out.println("JButton5_0");
								}
							});
							JButton5_1.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									System.out.println("JButton5_1");
								}
							});
							JButton5_2.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									System.out.println("JButton5_2");
								}
							});
							JToolBar1.add(JButton5_0);
							JToolBar1.add(JButton5_1);
							JToolBar1.add(JButton5_2);	
							JFrame5.add(JToolBar1,"South");						
							JMenuBar JMenuBar1=new JMenuBar();
							JMenu JMenu1=new JMenu("JMenu1");
							JMenu JMenu2=new JMenu("JMenu2");
							JMenu JMenu3=new JMenu("JMenu3");
							JMenuItem JMenuItem1_1=new JMenuItem("JMenuItem1_1");
							JMenuItem JMenuItem1_2=new JMenuItem("JMenuItem1_2");
							JMenuItem JMenuItem1_3=new JMenuItem("JMenuItem1_3");
							JMenuItem JMenuItem2_1=new JMenuItem("JMenuItem2_1");
							JMenuItem JMenuItem2_2=new JMenuItem("JMenuItem2_2");
							JMenuItem JMenuItem2_3=new JMenuItem("JMenuItem2_3");
							JMenuItem JMenuItem3_1=new JMenuItem("JMenuItem3_1");
							JMenu1.add(JMenuItem1_1);
							JMenu1.add(JMenuItem1_2);
							JMenu1.add(JMenuItem1_3);
							JMenu2.add(JMenuItem2_1);
							JMenu2.add(JMenuItem2_2);
							JMenu2.add(JMenuItem2_3);
							JMenu3.add(JMenuItem3_1);
							JMenuBar1.add(JMenu1);
							JMenuBar1.add(JMenu2);
							JMenuBar1.add(JMenu3);
							JFrame5.add(JMenuBar1,"North");
							JFrame5.setVisible(true);			
						}		
						else if(JComboBox1.getSelectedItem().equals("String6"))
						{
							
							JFrame JFrame6=new JFrame();
							JFrame6.setBounds(400,400,400,600);
							DefaultMutableTreeNode DefaultMutableTreeNode0=new DefaultMutableTreeNode("DefaultMutableTreeNode0");
							DefaultMutableTreeNode DefaultMutableTreeNode1=new DefaultMutableTreeNode("DefaultMutableTreeNode1");
							DefaultMutableTreeNode DefaultMutableTreeNode1_1=new DefaultMutableTreeNode("DefaultMutableTreeNode1_1");
							DefaultMutableTreeNode DefaultMutableTreeNode1_2=new DefaultMutableTreeNode("DefaultMutableTreeNode1_2");
							DefaultMutableTreeNode DefaultMutableTreeNode1_3=new DefaultMutableTreeNode("DefaultMutableTreeNode1_3");
							DefaultMutableTreeNode DefaultMutableTreeNode1_4=new DefaultMutableTreeNode("DefaultMutableTreeNode1_4");
							DefaultMutableTreeNode1.add(DefaultMutableTreeNode1_1);
							DefaultMutableTreeNode1.add(DefaultMutableTreeNode1_2);
							DefaultMutableTreeNode1.add(DefaultMutableTreeNode1_3);
							DefaultMutableTreeNode1.add(DefaultMutableTreeNode1_4);
							DefaultMutableTreeNode DefaultMutableTreeNode2=new DefaultMutableTreeNode("DefaultMutableTreeNode2");
							DefaultMutableTreeNode DefaultMutableTreeNode2_1=new DefaultMutableTreeNode("DefaultMutableTreeNode2_1");
							DefaultMutableTreeNode DefaultMutableTreeNode2_2=new DefaultMutableTreeNode("DefaultMutableTreeNode2_2");
							DefaultMutableTreeNode DefaultMutableTreeNode2_3=new DefaultMutableTreeNode("DefaultMutableTreeNode2_3");
							DefaultMutableTreeNode DefaultMutableTreeNode3=new DefaultMutableTreeNode("DefaultMutableTreeNode3");
							DefaultMutableTreeNode2.add(DefaultMutableTreeNode2_1);
							DefaultMutableTreeNode2.add(DefaultMutableTreeNode2_2);
							DefaultMutableTreeNode2.add(DefaultMutableTreeNode2_3);
							DefaultMutableTreeNode0.add(DefaultMutableTreeNode1);
							DefaultMutableTreeNode0.add(DefaultMutableTreeNode2);
							DefaultMutableTreeNode0.add(DefaultMutableTreeNode3);
							final JTree JTree1=new JTree(DefaultMutableTreeNode0);
							DefaultTreeCellRenderer DefaultTreeCellRenderer1=new DefaultTreeCellRenderer();
							DefaultTreeCellRenderer1.setOpenIcon(new ImageIcon("Win\\Win0_0.jpg"));
							DefaultTreeCellRenderer1.setClosedIcon(new ImageIcon("Win\\Win0.jpg"));
							JTree1.setCellRenderer(DefaultTreeCellRenderer1);
							JTree1.addTreeSelectionListener(new TreeSelectionListener()
							{
								public void valueChanged(TreeSelectionEvent e)
								{
									DefaultMutableTreeNode LastSelectedPathComponentOfDefaultMutableTreeNode=(DefaultMutableTreeNode)JTree1.getLastSelectedPathComponent();
									if(LastSelectedPathComponentOfDefaultMutableTreeNode.isLeaf())JOptionPane.showMessageDialog(null,LastSelectedPathComponentOfDefaultMutableTreeNode.toString()+" and Parent is:"+LastSelectedPathComponentOfDefaultMutableTreeNode.getParent().toString());
								}
							});
							JScrollPane JScrollPane6_1=new JScrollPane(JTree1);
							JTextArea JTextArea1=new JTextArea("JTextArea1");
							JScrollPane JScrollPane6_2=new JScrollPane(JTextArea1);
							JSplitPane JSplitPane6=new JSplitPane(1,true,JScrollPane6_1,JScrollPane6_2);
							JFrame6.add(JSplitPane6);
							JFrame6.setVisible(true);			
						}
						else if(JComboBox1.getSelectedItem().equals("String7"))
						{
							
							JFrame JFrame7=new JFrame();
							JFrame7.setBounds(400,400,600,800);
							DefaultMutableTreeNode DefaultMutableTreeNode0=new DefaultMutableTreeNode("DefaultMutableTreeNode0");
							DefaultMutableTreeNode DefaultMutableTreeNode1;
							try
							{
								BufferedReader BufferedReader1=new BufferedReader(new FileReader("JTree.txt"));
								String s=BufferedReader1.readLine();
								DefaultMutableTreeNode0=new DefaultMutableTreeNode(s);
								int k=0;String t="";
								s=BufferedReader1.readLine();
								while(s!=null)
								{
									k=0;t="";
									for(;s.charAt(k)!=';';k++)t+=s.charAt(k);
									DefaultMutableTreeNode1=new DefaultMutableTreeNode(t);k++;t="";
									while(k<s.length())
									{
										for(;s.charAt(k)!=';';k++)t+=s.charAt(k);
										DefaultMutableTreeNode1.add(new DefaultMutableTreeNode(t));k++;t="";
									}
									DefaultMutableTreeNode0.add(DefaultMutableTreeNode1);
									s=BufferedReader1.readLine();
								}
								BufferedReader1.close();
							}
							catch(Exception ex){}
							final JTree JTree1=new JTree(DefaultMutableTreeNode0);
							JTree1.addTreeSelectionListener(new TreeSelectionListener()
							{
								public void valueChanged(TreeSelectionEvent e)
								{
									DefaultMutableTreeNode LastSelectedPathComponentOfDefaultMutableTreeNode=(DefaultMutableTreeNode)JTree1.getLastSelectedPathComponent();
									if(LastSelectedPathComponentOfDefaultMutableTreeNode.isLeaf())JOptionPane.showMessageDialog(null,LastSelectedPathComponentOfDefaultMutableTreeNode.toString()+" and Parent is:"+LastSelectedPathComponentOfDefaultMutableTreeNode.getParent().toString());
								}
							});
							JScrollPane JScrollPane7_1=new JScrollPane(JTree1);
							JTextArea JTextArea7=new JTextArea("JTextArea7");
							JScrollPane JScrollPane7_2=new JScrollPane(JTextArea7);
							JSplitPane JSplitPane7=new JSplitPane(1,JScrollPane7_1,JScrollPane7_2);
							JFrame7.add(JSplitPane7);
							JFrame7.setVisible(true);	
						}
						else JOptionPane.showMessageDialog(null,JComboBox1.getSelectedItem());
					}
				});
				JFrame2.setVisible(true);
				JFrame2.setDefaultCloseOperation(3);
			}
		});
		
		JInternalFrame JInternalFrame1=new JInternalFrame("JInternalFrame1");
		JInternalFrame1.setSize(200,200);		 
		JInternalFrame1.add(JButton1);
		
		JInternalFrame1.setVisible(true);
	
		
		JDesktopPane JDesktopPane1=new JDesktopPane();		
		JDesktopPane1.add(JInternalFrame1);	
		
		
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.add(JPanel1,"West");	
		this.add(JPanel2,"East"); 	
		this.add(JDesktopPane1,"Center");
		this.setVisible(true);		
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}

}

class SwingJPanel extends JPanel implements ActionListener,KeyListener
{
	int index=0;
	Image Images[] =new Image[10],SystemBar;
	String[] Strings={"0.jpg","1.jpg","2.jpg","3.jpg","4.jpg","5.jpg","6.jpg","7.jpg","8.jpg","9.jpg"};
	Timer Timer1=new Timer(100,new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			repaint();index++;if(index==10)index=0;			
		}	
	});	
	public void paint(Graphics g)
	{
						
		Image1=Toolkit.getDefaultToolkit().getImage(Strings[index]);
		g.drawImage(Image1,0,0,this);
		g.setColor(Color.white);
		g.setFont(new Font("Î¢ÈíÑÅºÚ",Font.BOLD,100));
		g.drawString("Panel3",50,50);		
	}
	Image Image1;
	JButton JButton1;	
	JButton JButton2;
	JButton JButton3;
	ScrollPane ScrollPane1;
	JComboBox JComboBox1;
	JProgressBar JProgressBar1;
	JTextField JTextField1;
	JTextArea JTextArea1;
	public SwingJPanel()
	{
		super();
		this.addKeyListener(this);
		JButton1.addActionListener(this);
		this.setFocusable(true);
		Timer1.start();
		this.setLayout(null);
		JButton1=new JButton();		
		JButton1.setBounds(1000,50,300,50);	
		JButton1.setText("JButton1");	
		JButton1.addActionListener(this);

		JButton2=new JButton();		
		JButton2.setBounds(100,50,300,50);	
		JButton2.setText("JButton2");
		JButton2.addActionListener(this);

		JButton3=new JButton();		
		JButton3.setBounds(500,50,300,50);	
		JButton3.setText("JButton3");
		JButton3.addActionListener(this);

		this.add(JButton1);
		this.add(JButton2);
		this.add(JButton3);
		ScrollPane1=new ScrollPane();
		ScrollPane1.setBounds(1000,100,300,550);			
		ScrollPane1.add(JTextArea1);		
		this.add(ScrollPane1);	
		JComboBox1=new JComboBox();
		JComboBox1.setBounds(1000,600,150,50);
		JComboBox1.setFont(new Font("Î¢ÈíÑÅºÚ",Font.BOLD,25));
		this.add(JComboBox1);
		JProgressBar1=new JProgressBar();
		JProgressBar1.setValue(100);
		JProgressBar1.setBounds(1000,50,300,50);
		this.add(JProgressBar1);
		JTextField1=new JTextField();
		JTextField1.setText("");	
		this.add(JTextField1);	
		JTextField1.setBounds(1000,150,300,100);
		JTextField1.setFont(new Font("Î¢ÈíÑÅºÚ",Font.BOLD,25));	
		JTextArea1=new JTextArea();
		JTextArea1.setBounds(100,300,300,300);
		this.add(JTextArea1);
	}
	public void actionPerformed(ActionEvent e)
	{		
		if(e.getSource()==JButton1)
		{
			JFileChooser JFileChooser1=new JFileChooser();
			int a=JFileChooser1.showDialog(this,"Open Text File");
			String s="",t="";
			try
			{
		      		BufferedReader BufferedReader1=new BufferedReader(new FileReader(JFileChooser1.getSelectedFile().getPath()));
				t=BufferedReader1.readLine();
				while(t!=null)
				{
					s+=t;s+="\n";
					t=BufferedReader1.readLine();
				}
			}
			catch(Exception ex){}
			JTextArea1.setText(s);		
		}

		if(e.getSource()==JButton2)
		{
			JFileChooser JFileChooser1=new JFileChooser();
			int a=JFileChooser1.showDialog(this,"Save Text File");
			String s=JTextArea1.getText();
			try
			{
				PrintWriter PrintWriter1=new PrintWriter(JFileChooser1.getSelectedFile().getPath());
				PrintWriter1.print(s);
				PrintWriter1.close();
			}
			catch(Exception ex){}				
		}
		if(e.getSource()==JButton3)
		{
			JComboBox1.removeAllItems();
			String[] S=(JTextArea1.getText()).split("\n");
			for(String s : S)JComboBox1.addItem(s);	
			String s=(String)JComboBox1.getSelectedItem();
			JOptionPane.showMessageDialog(null,s);
		}
	}
	public void keyPressed(KeyEvent e)		
	{
		String keyText=KeyEvent.getKeyText(e.getKeyCode());
		System.out.println("Pressed keyText="+keyText+";");
	}
	public void keyReleased(KeyEvent e)
	{
		String keyText=KeyEvent.getKeyText(e.getKeyCode());
		System.out.println("Released keyText="+keyText+";");	
	}
	public void keyTyped(KeyEvent e)
	{
		System.out.println("key Typed Event:"+e.getKeyChar());				
	}
}						
		
		

				

		
	





