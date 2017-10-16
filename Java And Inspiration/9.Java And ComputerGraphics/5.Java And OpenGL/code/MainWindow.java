/*
 * Jogl Tutorial
 * Copyright (C) 2004 
 * Kevin Conroy <kmconroy@cs.umd.edu>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import net.java.games.jogl.GLCanvas;
import net.java.games.jogl.GLCapabilities;
import net.java.games.jogl.GLDrawableFactory;

/**
 * The main window of the program.
 * 
 * @author Kevin Conroy
 */
public class MainWindow {

	/**
	 * Main function which will create a new AWT Frame and add a JOGL Canvas to it
	 * 
	 * @param args Runtime args
	 */
	public static void main(String[] args) {
		// Make the new window
		Frame frame = new Frame("CMSC427 - Project 1");

		// make a new GLCanvas for drawing
		GLCanvas canvas = GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());

		//Animator animator = new Animator(canvas); 
		
		// make a new JOGLEventListener that is tied to this cavnas
		JoglEventMediator listener = new JoglEventMediator(canvas);
		canvas.addGLEventListener(listener);
		canvas.addKeyListener(listener);
		canvas.addMouseListener(listener);
		canvas.addMouseMotionListener(listener);

		Timer timer = new Timer();
		TimerTask task = new ColorChangeAlert(listener);
		// 10 seconds... 10000 ms
		timer.schedule(task, 0, 10000);

		// add GLCanvas to the window
		frame.add(canvas);

		// make it purty
		frame.setSize(400, 400);
		frame.setLocation(0, 0);
		frame.setBackground(Color.white);

		// add an event listener for the "close window" event
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		
		// print out directions to user
		System.out.println("CMSC427, Project 1 (Kevin Conroy)");
		System.out.println("");		
		System.out.println("===============");
		System.out.println("  Directions:   ");
		System.out.println("===============");
		System.out.println(" * Use the ARROW keys to move the rectangle");
		System.out.println(" * Use CTRL + the ARROW keys to move the diamond");
		System.out.println(" * Press d (lower case) to rotate the diamond 5 degrees CCW");
		System.out.println(" * Press D (upper case) to rotate the diamond 5 degrees CW");
		System.out.println(" * Press r (lower case) to rotate the rectangle 5 degrees CCW");
		System.out.println(" * Press R (upper case) to rotate the rectangle 5 degrees CW");
		System.out.println(" * Left click to swap rectangle/diamond color");
		System.out.println(" * Hold down right button to turn triangle black");
		System.out.println(" * Click-and-drag with left button to move rectangle");
		System.out.println(" * Click-and-drag with right button to move rectangle");
		System.out.println("");
		System.out.println("===============");
		System.out.println("  Extensions:   ");
		System.out.println("===============");
		System.out.println(" * Written in Java using JOGL and java.awt");
		System.out.println(" * Use ALT + the ARROW keys to move the triangle");
		System.out.println(" * Press t (lower case) to rotate the triangle 5 degrees CCW");
		System.out.println(" * Press T (upper case) to rotate the triangle 5 degrees CW");
		System.out.println("");
		System.out.println("Type 'q' or 'Q' at anytime to quit.");
		
		// display the window
		frame.setVisible(true);
		frame.show();

		//animator.start();
		
		// let JOGL take over
		canvas.requestFocus();
	}
}
