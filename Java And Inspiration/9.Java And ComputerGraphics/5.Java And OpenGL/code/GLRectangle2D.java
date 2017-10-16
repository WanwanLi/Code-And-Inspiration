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
import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;


/**
 * @author Kevin Conroy
 */
public class GLRectangle2D implements IGLObject {

    /**
     * The state of the object to draw
     */
    private RectangleState _rectangle;

    /**
     * Constructor
     * 
     * @param rectangle The state of the rectangle we're drawing
     */
    public GLRectangle2D(RectangleState rectangle) {
        this._rectangle = rectangle;
    }

    /**
     * Draws a rectangle on the specified glDrawable
     * 
     * @param glDrawable The object to draw on
     */
    public void draw(GLDrawable glDrawable) {
        final GL gl = glDrawable.getGL();

        // set color
        gl.glColor3fv(this._rectangle.getColor().getColor());

        float x = 0.0f; //(float)this._rectangle.getCenter().getX();
        float y = 0.0f; //(float)this._rectangle.getCenter().getY();
        float width = this._rectangle.getWidth();
        float height = this._rectangle.getHeight();

        gl.glRectf(x - width, y - height, x + width, y + height);

        /*
         * POLYGON version // draw the rectangle gl.glBegin(GL.GL_POLYGON); //
         * draw it // upper right gl.glVertex2f(x + width, y + height); // upper
         * left gl.glVertex2f(x - width, y + height); // lower left
         * gl.glVertex2f(x - width, y - height); // lower right gl.glVertex2f(x +
         * width, y - height); //gl.glVertex2f(0.0f, 0.8f);
         * //gl.glVertex2f(-0.8f, 0.0f); //gl.glVertex2f(0.0f, -0.8f);
         * gl.glEnd();
         */
    }

}