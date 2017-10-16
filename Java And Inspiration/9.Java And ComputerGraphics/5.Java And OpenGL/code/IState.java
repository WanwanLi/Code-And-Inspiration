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
/**
 * Generic interface that all state objects should have
 * @author Kevin Conroy
 */
public interface IState {	
	/**
	 * Sets the center point of this object
	 * @param center The new center point
	 */
	public abstract void setCenter(java.awt.geom.Point2D.Float center);
	
	/**
	 * Gets the center point of this object
	 * @return Returns center point of this object
	 */
	public abstract java.awt.geom.Point2D.Float getCenter();
	
	/**
	 * Gets the number of degrees rotated in a CCW direction
	 * @return Returns number of rotated in a CCW direction
	 */
	public abstract float getDegreesRotatedCCW();
	/**
	 * Sets the number of degrees rotated in a CCW direction
	 * @param degreesRotated The number of degrees rotated in a CCW direction
	 */
	public abstract void setDegreesRotatedCCW(float degreesRotated);
	
	/**
	 * Set the color of this object
	 * @param color The color of the object.
	 */
	public abstract void setColor(GLColor color);
	
	/**
	 * Get the color of this object
	 * @return The color of the object.
	 */
	public abstract GLColor getColor();	
	
	
	/**
	 * Makes a IGLObject which can render the object based on the current state
	 * @return IGLObject to use for drawing
	 */
	public abstract IGLObject makeGLObject();
}