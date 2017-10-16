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
 * Represents a typesafe enum of GLColors.
 * 
 * @author Kevin Conroy
 */
public class GLColor {
    /**
     * Represent type-safe enum instance of Red
     */
    public static final GLColor RED = new GLColor(new float[]{1.0f, 0.0f, 0.0f});

    /**
     * Represent type-safe enum instance of Blue
     */
    public static final GLColor BLUE = new GLColor(
                                                   new float[]{0.0f, 0.0f, 1.0f});

    /**
     * Represent type-safe enum instance of Green
     */
    public static final GLColor GREEN = new GLColor(new float[]{0.0f, 1.0f,
                                                                0.0f});

    /**
     * Represent type-safe enum instance of Black
     */
    public static final GLColor BLACK = new GLColor(new float[]{0.0f, 0.0f,
                                                                0.0f});

    /**
     * Represent type-safe enum instance of Gray
     */
    public static final GLColor GRAY = new GLColor(new float[]{0.5f, 0.5f,
                                                               0.5f, 1.0f});

    /**
     * Private contrustor... type safe enum baby!
     * 
     * @param color The OpenGL color to wrap
     */
    private GLColor(float[] color) {
        this._color = color;
    }

    /**
     * The color that this class represents
     */
    private float[] _color;

    /**
     * Returns an array of floats representing this color in OpenGL format
     * 
     * @return The color in OpenGL format
     */
    public float[] getColor() {
        return this._color;
    }

}