   M S B A M L   `   `   `  ����N  JDX WPF Application1, Version=0.0.0.0, Culture=neutral, PublicKeyToken=null3clr-namespace:DXWpfApplicationDXWpfApplication  5   	   !    DXWpfApplication.MainWindow   5      \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   0localclr-namespace:DXWpfApplication     5   	   a ]DevExpress.Xpf.Core.v10.1, Version=10.1.7.0, Culture=neutral, PublicKeyToken=b88d1754d700e49a;dx2http://schemas.devexpress.com/winfx/2008/xaml/core  5   	   <dxb2http://schemas.devexpress.com/winfx/2008/xaml/bars  5      d `DevExpress.Xpf.Docking.v10.1, Version=10.1.7.0, Culture=neutral, PublicKeyToken=b88d1754d700e49a?dxd5http://schemas.devexpress.com/winfx/2008/xaml/docking  5      ?dxe5http://schemas.devexpress.com/winfx/2008/xaml/editors  5        � Title$  DXWpfApplication��5
   	   $	��600��6"   $	��800��6/   (  "DevExpress.Xpf.Themes.ThemeManager   	ThemeName$ Office2007Blue��5   	   .��5      $  DevExpress.Xpf.Bars.BarManager  -   5        $��
barManager��5         CreateStandardLayout .    
Categories 5   
   ,  &DevExpress.Xpf.Bars.BarManagerCategory  5         Name$
 File��6%     5      $
 Edit��6%     5      $
 Help��6%     5      $ Build-in Menus��6%      Items 5   
   '  !DevExpress.Xpf.Bars.BarButtonItem  5      -   5         �Name$
 bNew��5       !  DevExpress.Xpf.Bars.BarItem   CategoryName$
 File��6,      Content)  #DevExpress.Xpf.Core.ObjectConverter$	 New 6@   	   Glyph$ 	 Images/Icons/new-16x16.png��6N   
   
KeyGesture$
 Ctrl+N��6q      Hint$ New Blank Document 6�     5      -   5        $ bOpen��5       $
 File��6-   $
 Open 6A   $!	 Images/Icons/open-16x16.png��6P   $
 Ctrl+O��6t   $
 Open 6�     5      -   5        $ bClose��5       $
 File��6.   $ Close 6B   $"	 Images/Icons/close-16x16.png��6R   $ Close 6w     5      -   5        $ bSave��5       $
 File��6-   $
 Save 6A   $!	 Images/Icons/save-16x16.png��6P   $
 Ctrl+S��6t   $ Save Document 6�     5      -   5        $ bSaveAs��5       $
 File��6/   $ 
Save As... 6C   $$	 Images/Icons/save-as-16x16.png��6X   $ Save Document As... 6     5      -   5        $ bPrint��5       $
 File��6.   $ Print 6B   $"	 Images/Icons/print-16x16.png��6R   $
 Ctrl+P��6w     5      -   5        $ bExit��5       $
 File��6-   $
 Exit 6A   $
 Ctrl+X��6P     5      -	   5        $ bHome��5       $
 Help��6-   $" Developer Express on the Web 6A     5      -
   5        $ bAbout��5       $
 Help��6.   $ About 6B   $  DevExpress.Xpf.Bars.BarSubItem  5       -   5        $ smFile��5       $ Build-in Menus��6+   $
 File 6I      	ItemLinks 5!      +  %DevExpress.Xpf.Bars.BarButtonItemLink  5"      %	  DevExpress.Xpf.Bars.BarItemLink 	  BarItemName$
 bNew��6,     5#      $ bOpen��6,     5$      $ bClose��6,   .
  (DevExpress.Xpf.Bars.BarItemLinkSeparator
  5%        5&      $ bSave��6,     5'      $ bSaveAs��6,   
  5(        5)      $ bPrint��6,   
  5*        5+      $ bExit��6,   5-        5.      -   5        $ smHelp��5.      $ Build-in Menus��6+   $
 Help 6I    5/        50      $ bHome��6,   
  51        52      $ bAbout��6,   54         Bars 56   
     DevExpress.Xpf.Bars.Bar  57      -   5          Name$ MainMenu��57   )      Caption$ MainMenu��6      
IsMainMenu .    UseWholeRow .    DockInfo 58      %  DevExpress.Xpf.Bars.BarDockInfo  59         ContainerType$	 Top=�6&   /  )DevExpress.Xpf.Bars.BarItemLinkHolderBase   	ItemLinks 5;      (  "DevExpress.Xpf.Bars.BarSubItemLink  5<      $ smFile��6)     5=      $ smHelp��6)   5?      ._�5A   
   .  (DevExpress.Xpf.Docking.DockLayoutManager     
LayoutRoot. 5B      (  "DevExpress.Xpf.Docking.LayoutGroup     Items. 5C           Orientation$ Vertical=�6"   +  %DevExpress.Xpf.Docking.BaseLayoutItem   Width$ 3* �6:   . 5D      *  $DevExpress.Xpf.Docking.DocumentGroup     Height$ 3* �6(   . 5E      *  $DevExpress.Xpf.Docking.DocumentPanel     Caption$ 	Document1��6,   $ 3* �6@      CaptionImage$ Images/Icons/MainView.png��6L   (  "DevExpress.Xpf.Docking.LayoutPanel   Content. 5F      �� $��0q�6&   5G        5H      $ 	Document2��6,   $ 3* �6@   $ Images/Icons/MainView.png��6L   . 5I      �� $��0q�6&   5J      5K        5L      $ 
DetailView��6&   $ * �6;   $! Images/Icons/DetailView.png��6G   . 5M      �� $��0q�6"   5N      5O      (  "DevExpress.Xpf.Docking.TabbedGroup  5P      $ * �6"   . 5Q        $ 
Properties��6&     5R      $ Explorer��6&   5S      5T         AutoHideGroups 5U      *  $DevExpress.Xpf.Docking.AutoHideGroup  5V      . 5W        $ 
Navigation��6&   . 5X      �� $��0q�6"   5Y        5Z      $ Tools��6&   5[      5]      5^      5`      