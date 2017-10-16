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

import net.java.games.jogl.GLCanvas;
import net.java.games.jogl.GLCapabilities;
import net.java.games.jogl.GLDrawableFactory;

/**
 * Based on NeHe Production Lesson 01 ported by Kevin Duling
 * (jattier@hotmail.com)
 * 
 * @author Kevin Conroy (kmconroy@cs.umd.edu)
 */
public class HelloWorldWindow {
    /**
     * Main function which will create a new AWT Frame and add a JOGL Canvas to
     * it
     * 
     * @param args Runtime args
     */
    public static void main(String[] args) {
        Frame frame = new Frame("Hello World!");

        GLCanvas canvas = GLDrawableFactory.getFactory()
                        .createGLCanvas(new GLCapabilities());
        frame.add(canvas);

        frame.setSize(300, 300);
        frame.setBackground(Color.white);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.show();
    }
}
