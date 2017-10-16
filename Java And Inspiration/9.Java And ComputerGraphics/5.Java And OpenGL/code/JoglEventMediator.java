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
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import net.java.games.jogl.GLCanvas;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.GLEventListener;

/**
 * A class which subscribes to JOGL events and processes events by either handling them directly or
 * by passing the event onto subscribing objects.
 * 
 * @author Kevin Conroy
 */
public class JoglEventMediator
	implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

	/**
	 * The canvas we're listening to
	 */
	private GLCanvas _canvas = null;

	/**
	 * The command mediator which processes commands that we capture from the GUI
	 */
	private CommandMediator _commandMediator;

	/**
	 * the state of the last mouse pressed/released event
	 */
	private int _button;

	/**
	 * @param canvas
	 *            The canvas we're listening to
	 */
	public JoglEventMediator(GLCanvas canvas) {
		this._canvas = canvas;
		this._commandMediator = new CommandMediator();
	}

	
	/**
	 * Helper function for debug info
	 * @param toPrint Debug print info
	 */
	private void debug(String toPrint) {
		//System.out.println(toPrint);
	}
	
	/**
	 * Called by the drawable to initiate OpenGL rendering by the client. After all
	 * GLEventListeners have been notified of a display event, the drawable will swap its buffers
	 * if necessary.
	 * 
	 * @param gLDrawable
	 *            The GLDrawable object.
	 */
	public void display(GLDrawable gLDrawable) {
		debug("display called");

		this._commandMediator.draw(gLDrawable);
	}

	/**
	 * Refreshs the display by telling the canvas to display itself again
	 */
	private void refreshDisplay() {

		this._canvas.display();
	}

	/**
	 * Draws a rectangle
	 * 
	 * @param gl
	 */
	/*
	 * 
	 * private void drawRectangle(final GL gl) { // draw the rectangle if (colorIsReversed) //
	 * select the drawing color gl.glColor3fv(GLColor.RED.getColor()); else
	 * gl.glColor3fv(GLColor.BLUE.getColor()); // draw it gl.glRectf(-0.50f, -0.50f, +0.50f,
	 * +0.50f); }
	 */

	/**
	 * Called when the display mode has been changed. <B>!! CURRENTLY UNIMPLEMENTED IN JOGL !!
	 * </B>
	 * 
	 * @param gLDrawable
	 *            The GLDrawable object.
	 * @param modeChanged
	 *            Indicates if the video mode has changed.
	 * @param deviceChanged
	 *            Indicates if the video device has changed.
	 */
	public void displayChanged(GLDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {
		// do nothing
	}

	/**
	 * Called by the drawable immediately after the OpenGL context is initialized for the first
	 * time. Can be used to perform one-time OpenGL initialization such as setup of lights and
	 * display lists.
	 * 
	 * @param gLDrawable
	 *            The GLDrawable object.
	 */
	public void init(GLDrawable gLDrawable) {
		this._commandMediator.initalize(gLDrawable);
	}

	/**
	 * Called by the drawable during the first repaint after the component has been resized. The
	 * client can update the viewport and view volume of the window appropriately, for example by a
	 * call to GL.glViewport(int, int, int, int); note that for convenience the component has
	 * already called GL.glViewport(int, int, int, int)(x, y, width, height) when this method is
	 * called, so the client may not have to do anything in this method.
	 * 
	 * @param gLDrawable
	 *            The GLDrawable object.
	 * @param x
	 *            The X Coordinate of the viewport rectangle.
	 * @param y
	 *            The Y coordinate of the viewport rectanble.
	 * @param width
	 *            The new width of the window.
	 * @param height
	 *            The new height of the window.
	 */
	public void reshape(GLDrawable gLDrawable, int x, int y, int width, int height) {
		this._commandMediator.reshape(gLDrawable, x, y, width, height);
	}

	/**
	 * Invoked when a key has been pressed. See the class description for {@link KeyEvent}for a
	 * definition of a key pressed event.
	 * 
	 * @param e
	 *            The KeyEvent.
	 */
	public void keyPressed(KeyEvent e) {
		debug("Arrow?");

		if (e.isControlDown()) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				this._commandMediator.moveDiamondBy(0.0f, 0.02f);
				this.refreshDisplay();
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				this._commandMediator.moveDiamondBy(0.0f, -0.02f);
				this.refreshDisplay();
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				this._commandMediator.moveDiamondBy(-0.02f, 0.0f);
				this.refreshDisplay();
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				this._commandMediator.moveDiamondBy(0.02f, 0.0f);
				this.refreshDisplay();
			}
		} else {
			if (e.isAltDown()) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					this._commandMediator.moveTriangleBy(0.0f, 0.02f);
					this.refreshDisplay();
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					this._commandMediator.moveTriangleBy(0.0f, -0.02f);
					this.refreshDisplay();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					this._commandMediator.moveTriangleBy(-0.02f, 0.0f);
					this.refreshDisplay();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					this._commandMediator.moveTriangleBy(0.02f, 0.0f);
					this.refreshDisplay();
				}
			} else {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					this._commandMediator.moveRectangleBy(0.0f, 0.02f);
					this.refreshDisplay();
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					this._commandMediator.moveRectangleBy(0.0f, -0.02f);
					this.refreshDisplay();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					this._commandMediator.moveRectangleBy(-0.02f, 0.0f);
					this.refreshDisplay();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					this._commandMediator.moveRectangleBy(0.02f, 0.0f);
					this.refreshDisplay();
				}
			}
		}

	}

	/**
	 * Invoked when a key has been released. See the class description for {@link KeyEvent}for a
	 * definition of a key released event.
	 * 
	 * @param e
	 *            The KeyEvent.
	 */
	public void keyReleased(KeyEvent e) {
		// do nothing
	}

	/**
	 * Invoked when a key has been typed. See the class description for {@link KeyEvent}for a
	 * definition of a key typed event.
	 * 
	 * @param e
	 *            The KeyEvent.
	 */
	public void keyTyped(KeyEvent e) {
		debug("Key pressed; code = " + String.valueOf(e.getKeyCode()));

		// if we press Q|q, exit the program
		if (e.getKeyChar() == 'Q' || e.getKeyChar() == 'q') {
			this._commandMediator.exit();
		}
		if (e.getKeyChar() == 'r') {
			this._commandMediator.rotateRectangle(5, RotateDirection.CCW);
			this.refreshDisplay();
		}
		if (e.getKeyChar() == 'R') {
			this._commandMediator.rotateRectangle(5, RotateDirection.CW);
			this.refreshDisplay();
		}
		if (e.getKeyChar() == 'd') {
			this._commandMediator.rotateDiamond(5, RotateDirection.CCW);
			this.refreshDisplay();
		}
		if (e.getKeyChar() == 'D') {
			this._commandMediator.rotateDiamond(5, RotateDirection.CW);
			this.refreshDisplay();
		}
		if (e.getKeyChar() == 't') {
			this._commandMediator.rotateTriangle(5, RotateDirection.CCW);
			this.refreshDisplay();
		}
		if (e.getKeyChar() == 'T') {
			this._commandMediator.rotateTriangle(5, RotateDirection.CW);
			this.refreshDisplay();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent mouse) {
		// do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent mouse) {
		// do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent mouse) {
		// do nothing

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent mouse) {
		this.setButton(mouse.getButton());

		debug(
			"Mouse click detected at coordinates x="
				+ String.valueOf(mouse.getX())
				+ " and y="
				+ String.valueOf(mouse.getY()));

		if (mouse.getButton() == MouseEvent.BUTTON1) {
			debug("Left mouse click.  Reversing colors.");
			this._commandMediator.swapColors();
			this.refreshDisplay();
		}

		if (mouse.getButton() == MouseEvent.BUTTON3) {
			this._commandMediator.changeTriangleColor(GLColor.BLACK);
			this.refreshDisplay();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent mouse) {
		this.setButton(0);

		if (mouse.getButton() == MouseEvent.BUTTON3) {
			this._commandMediator.changeTriangleColor(GLColor.GREEN);
			this.refreshDisplay();
		}
	}

	/**
	 * Saves the state of the last mouse pressed/released event
	 * 
	 * @param button
	 */
	private void setButton(int button) {
		debug("Setting button to " + button);
		this._button = button;
	}

	/**
	 * Swaps the colors of the objects. NOTE: Synchronized because the JOGL thread as well as the
	 * ColorChangeAlert thread will use this function.
	 */
	public synchronized void swapColors() {
		this._commandMediator.swapColors();
		this.refreshDisplay();
	}

	/**
	 * Listens for a mouse drag (at which point we should move the rectangle or diamond to the
	 * location of the mouse cursor.
	 * 
	 * @param mouse
	 *            Mouse event args
	 *  
	 */
	public void mouseDragged(MouseEvent mouse) {
		debug("Mouse drag event detected");

		Point2D.Float world = screenToJOGLWorld(mouse);

		if (this.getButton() == MouseEvent.BUTTON1) {
			debug("Mouse drag event + left click");

			// move the rectangle!
			this._commandMediator.moveRectangleTo(world.x, world.y);
			this.refreshDisplay();
		}
		if (this.getButton() == MouseEvent.BUTTON3) {
			debug("Mouse drag event + right click");

			// move the diamond!
			this._commandMediator.moveDiamondTo(world.x, world.y);
			this.refreshDisplay();
		}
	}

	/**
	 * Converts a mouse position to a JOGL world position
	 * 
	 * @param mouse
	 *            Mouse event which we want to translate
	 * @return The point in JOGL world coordinate space
	 */
	private Point2D.Float screenToJOGLWorld(MouseEvent mouse) {
		Point2D.Float world = new Point2D.Float();

		Rectangle bounds = this._canvas.getBounds();
		float x = mouse.getX();
		float y = mouse.getY();

		world.x = ((x / (float) bounds.getMaxX()) * 2.0f) - 1.0f;
		world.y = ((((float) bounds.getMaxY() - y) / (float) bounds.getMaxY()) * 2.0f) - 1.0f;

		debug(
			"("
				+ mouse.getX()
				+ ", "
				+ mouse.getY()
				+ ") => ("
				+ world.getX()
				+ ", "
				+ world.getY()
				+ ")");

		return world;
	}

	/**
	 * @return the state of the last mouse pressed/released event
	 */
	private int getButton() {
		return this._button;
	}

	/**
	 * General mouse motion
	 * 
	 * @param mouse
	 *            Mouse event args
	 */
	public void mouseMoved(MouseEvent mouse) {
		// ignore
	}
}
