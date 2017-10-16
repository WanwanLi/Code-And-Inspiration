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

import java.awt.geom.Point2D;

/**
 * @author Kevin Conroy
 *
 * Stores the state of a Rectangle
 */
public class RectangleState implements IState  {
	
	/**
	 * Stores general state information
	 */
	private IState _state;
	
	/**
	 * The width from the center
	 */
	private float _width;

	/**
	 * The height from the center
	 */
	private float _height;
	
	/**
	 * Constructor
	 */
	public RectangleState() {
		this._state = new GenericState();
		this._width = 0;
		this._height = 0;
	}

	/**
	 * Constructor
	 * 
	 * @param x
	 *            The x position
	 * @param y
	 *            The y posotion
	 * @param height
	 *            Height of rectangle
	 * @param width
	 *            Width of rectangle
	 * @param degreesRotatedCCW
	 *            Number of degress to rotate
	 * @param color
	 *            Color to draw with
	 */
	public RectangleState(
		float x,
		float y,
		float height,
		float width,
		float degreesRotatedCCW,
		GLColor color) {
		this._state = new GenericState(x, y, degreesRotatedCCW, color);
		this._width = width;
		this._height = height;
	}


	/**
	 * @param center The new center point
	 */
	public void setCenter(Point2D.Float center) {
		this._state.setCenter(center);
	}
	
	/**
	 * @return Returns center point of this triangle
	 */
	public Point2D.Float getCenter(){
		return this._state.getCenter();
	}
	
	/**
	 * @return Returns number of rotated in a CCW direction
	 */
	public float getDegreesRotatedCCW() {
		return this._state.getDegreesRotatedCCW();
	}

	/**
	 * @param degreesRotated The number of rotated in a CCW direction
	 */
	public void setDegreesRotatedCCW(float degreesRotated) {
		this._state.setDegreesRotatedCCW(degreesRotated);
	}

	/**
	 * @return Returns the width from the center.
	 */
	public float getWidth() {
		return this._width;
	}

	/**
	 * @param width The width from the center
	 */
	public void setWidth(float width) {
		this._width = width;
	}

	/**
	 * @return Returns the width from the center.
	 */
	public float getHeight() {
		return this._height;
	}

	/**
	 * @param height The width from the center
	 */
	public void setHeight(float height) {
		this._height = height;
	}
	
	
	/**
	 * Set the color of this object
	 * @param color The color of the object.
	 */
	public void setColor(GLColor color) {
		this._state.setColor(color);
	}
	
	/**
	 * Get the color of this object
	 * @return The color of the object.
	 */
	public GLColor getColor() {
		return this._state.getColor();
	}

	/**
	 * Factory method that creates a GLObject capable of rendering this rectangles current state 
	 * @return The GLObject 
	 */
	public IGLObject makeGLObject() {
		IGLObject glObject = new GLRectangle2D(this);
		glObject = new GLRotate2D(this.getDegreesRotatedCCW(), glObject);
		glObject = new GLTranslate2D((float)this.getCenter().getX(), (float)this.getCenter().getY(), glObject);
		glObject = new GLMatrixLoader(glObject);
		
		return glObject;
	}
}

