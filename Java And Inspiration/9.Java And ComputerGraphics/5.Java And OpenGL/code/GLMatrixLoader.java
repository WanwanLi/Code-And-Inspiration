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
 * @author Kevin Conroy Copies the current matrix onto the stack before we apply
 *         a new transformation. Cleans up after the wrapped object is done.
 */
public class GLMatrixLoader implements IGLObject {

    /**
     * The IGLObject to wrap
     */
    private IGLObject _glObject;

    /**
     * Contructor
     * 
     * @param glObject The IGLobject which we're wrapping
     */
    public GLMatrixLoader(IGLObject glObject) {
        this._glObject = glObject;
    }

    /**
     * Pushs a copy of the matrix before drawing the wrapped IGLObject. Then it
     * pops the matrix after the wrapped IGLObject has drawn itself.
     * 
     * @param glDrawable
     */
    public void draw(GLDrawable glDrawable) {
        final GL gl = glDrawable.getGL();

        // copy existing state
        gl.glPushMatrix();

        // decorator!
        this._glObject.draw(glDrawable);

        // remove rotation
        gl.glPopMatrix();
    }

}