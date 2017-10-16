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
public class GLTriangleEqil2D implements IGLObject {

    /**
     * The state of the object to draw
     */
    private TriangleEqilState _triangle;

    /**
     * Constructor
     * 
     * @param triangle The state of the triangle we're drawing
     */
    public GLTriangleEqil2D(TriangleEqilState triangle) {
        this._triangle = triangle;
    }

    /**
     * Draws a rectangle on the specified glDrawable
     * 
     * @param glDrawable The object to draw on
     */
    public void draw(GLDrawable glDrawable) {
        final GL gl = glDrawable.getGL();

        // set color
        gl.glColor3fv(this._triangle.getColor().getColor());

        float x = 0.0f;
        float y = 0.0f;

        float length = this._triangle.getLength();

        float half_length = 0.5f * length;

        // given the length of a side and the center point, we can calculate
        // the end points

        // we'll draw bisecting lines through each corner... we know that the
        // length of one side is 1/2 the length of a side
        float hypotenus = ((float) (half_length / (Math.sin(Math.toRadians(60)))));
        float adjacent = ((float) (half_length / (Math.tan(Math.toRadians(60)))));

        // draw the triangle
        gl.glBegin(GL.GL_POLYGON); // draw it

        // upper right
        gl.glVertex2f(x, hypotenus);

        // upper left
        gl.glVertex2f(x - half_length, y - adjacent);

        // lower left
        gl.glVertex2f(x + half_length, y - adjacent);

        gl.glEnd();
    }

}