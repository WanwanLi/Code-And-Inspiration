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
 * A Decorator Pattern which apply GL rotations before rendering the
 * encapsulated object.
 * 
 * @author Kevin Conroy
 */
public class GLRotate2D implements IGLObject {

    /**
     * Number of degrees to rotate
     */
    private float _degreesToRotateCCW;

    /**
     * IGLObject we're decorating
     */
    private IGLObject _glObject;

    /**
     * Contructor
     * 
     * @param degreesToRotateCCW The number of degrees to rotate
     */
    public GLRotate2D(float degreesToRotateCCW) {
        this(degreesToRotateCCW, new EmptyGLObject());
    }

    /**
     * @param degreesToRotateCCW The number of degrees to rotate
     * @param glObject GLObject we're wrapping (decorator)
     */
    public GLRotate2D(float degreesToRotateCCW, IGLObject glObject) {
        this._degreesToRotateCCW = degreesToRotateCCW;
        this._glObject = glObject;
    }

    /**
     * Applys rotation and then attempts to draw encapsulated GLObject, if any
     * exists
     * 
     * @param glDrawable GLDrawable to use for drawing
     */
    public void draw(GLDrawable glDrawable) {
        final GL gl = glDrawable.getGL();

        // apply rotation
        gl.glRotatef(this._degreesToRotateCCW, 0.0f, 0.0f, 1.0f);

        // decorator!
        this._glObject.draw(glDrawable);
    }

}