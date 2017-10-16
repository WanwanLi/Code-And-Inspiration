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
 * @author Kevin Conroy Stores the state of a Rectangle
 */
public class GenericState implements IState {

    /**
     * The center point of the triangle
     */
    private Point2D.Float _center;

    /**
     * The number of degrees that this triangle has been rotated counter-clock
     * wise (CCW)
     */
    private float _degreesRotatedCCW;

    /**
     * The color of this object
     */
    private GLColor _color;

    /**
     * Constructor
     */
    public GenericState() {
        // use default values
        this(0.0f, 0.0f, 0, GLColor.BLACK);
    }

    /**
     * Constructor
     * 
     * @param x The x position
     * @param y The y posotion
     * @param degreesRotatedCCW Number of degress to rotate
     * @param color Color to draw with
     */
    public GenericState(float x, float y, float degreesRotatedCCW, GLColor color) {
        this._center = new Point2D.Float(x, y);
        this._degreesRotatedCCW = degreesRotatedCCW;
        this._color = color;
    }

    /**
     * @param center The new center point
     */
    public void setCenter(Point2D.Float center) {
        this._center = center;
    }

    /**
     * @return Returns center point of this triangle
     */
    public Point2D.Float getCenter() {
        return this._center;
    }

    /**
     * @return Returns number of rotated in a CCW direction
     */
    public float getDegreesRotatedCCW() {
        return _degreesRotatedCCW;
    }

    /**
     * @param degreesRotated The number of rotated in a CCW direction
     */
    public void setDegreesRotatedCCW(float degreesRotated) {
        this._degreesRotatedCCW = degreesRotated;
    }

    /**
     * Set the color of this object
     * 
     * @param color The color of the object.
     */
    public void setColor(GLColor color) {
        this._color = color;
    }

    /**
     * Get the color of this object
     * 
     * @return The color of the object.
     */
    public GLColor getColor() {
        return this._color;
    }

    /**
     * Create the GLObject to use for drawing
     * @return Null GLObject
     */
    public IGLObject makeGLObject() {
        return new EmptyGLObject();
    }

}

