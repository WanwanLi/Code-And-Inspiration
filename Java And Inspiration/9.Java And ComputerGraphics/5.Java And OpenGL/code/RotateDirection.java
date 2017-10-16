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
 * @author Kevin Conroy
 *
 * Type-safe enum of rotation directions.
 */
public class RotateDirection {
	
	/**
	 * The clockwise direction
	 */
	public static final RotateDirection CW = new RotateDirection(-1.00f);
	
	/**
	 * The counter-clockwise direction 
	 */
	public static final RotateDirection CCW = new RotateDirection(1.00f);
	
	/**
	 * The amount to multiple the degrees as to get the desired rotation
	 */	
	private float _multipler;
	
	/**
	 * Constructor
	 * @param multipler The amount to multiple the degrees by to get the right rotation
	 */
	public RotateDirection(float multipler) {
		this._multipler = multipler;
	}

	/**
	 * Calculates the correct rotation based on the direction of the rotation and the
	 * specifed number of degrees. (Positive means CCW, negative CW)
	 * @param degrees Number of degrees to rotate for this direction
	 * @return Number of degrees to rotate in CCW direction
	 */
	public float calculateRotation(float degrees) {
		return degrees * _multipler;
	}
}
