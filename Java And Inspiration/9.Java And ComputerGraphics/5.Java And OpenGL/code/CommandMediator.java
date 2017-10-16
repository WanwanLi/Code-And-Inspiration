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

import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.GLU;

/**
 * Mediator for commands that can be issued by the JoglEventMediator. The event
 * mediator listens for various GUI events and determines the policy for
 * deciding what user actions constitute what event. The job of this class is to
 * perform the requested actions based on those events.
 * 
 * @author Kevin Conroy
 */
public class CommandMediator {
    /**
     * Stores the state of the triangle
     */
    private TriangleEqilState _triangle;

    /**
     * Stores the state of the rectangle
     */
    private RectangleState _rectangle;

    /**
     * Stores the state of the diamong
     */
    private RectangleState _diamond;

    /**
     * Contructor
     */
    public CommandMediator() {
        this._triangle = new TriangleEqilState(0.0f, 0.0f, 0.4f, 0.0f,
                                               GLColor.GREEN);
        this._rectangle = new RectangleState(0.0f, 0.0f, 0.5f, 0.5f, 0.0f,
                                             GLColor.RED);
        this._diamond = new RectangleState(0.0f, 0.0f, 0.565685f, 0.565685f,
                                           45.0f, GLColor.BLUE);
    }

    /**
     * Draws the objects (triangle, diamond, rectangle) on the specified
     * GLDrawable
     * 
     * @param glDrawable The GLDrawable to draw objects on.
     */
    public void draw(GLDrawable glDrawable) {
        // clear the screen
        clearScreen(glDrawable);

        // make GLObjects to represent the current state of each object
        IGLObject glDiamond = this._diamond.makeGLObject();
        IGLObject glRectangle = this._rectangle.makeGLObject();
        IGLObject glTriangle = this._triangle.makeGLObject();

        // draw the three shapes
        glDiamond.draw(glDrawable);
        glRectangle.draw(glDrawable);
        glTriangle.draw(glDrawable);
    }

    /**
     * Clears the screen
     * 
     * @param glDrawable The GLDrawable to clear
     */
    public void clearScreen(GLDrawable glDrawable) {
        final GL gl = glDrawable.getGL();

        // first clear the screen
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // background is gray
        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // clear the window
    }

    /**
     * Changes the color of the triangle
     * 
     * @param color The color to change the triangle to.
     */
    public void changeTriangleColor(GLColor color) {
        this._triangle.setColor(color);
    }

    /**
     * Changes the color of the rectangle
     * 
     * @param color The color to change the rectangle to.
     */
    public void changeRectangleColor(GLColor color) {
        this._rectangle.setColor(color);
    }

    /**
     * Changes the color of the diamond
     * 
     * @param color The color to change the diamond to.
     */
    public void changeDiamondColor(GLColor color) {
        this._diamond.setColor(color);
    }

    /**
     * Moves the rentangle by the requested amount.
     * 
     * @param x X offset
     * @param y Y offset
     */
    public void moveRectangleBy(float x, float y) {
        moveShapeBy(this._rectangle, x, y);
    }

    /**
     * Moves the diamond by the requested amount.
     * 
     * @param x X offset
     * @param y Y offset
     */
    public void moveDiamondBy(float x, float y) {
        moveShapeBy(this._diamond, x, y);
    }

    /**
     * Moves the triangle by the requested amount.
     * 
     * @param x X offset
     * @param y Y offset
     */
    public void moveTriangleBy(float x, float y) {
        moveShapeBy(this._triangle, x, y);
    }

    /**
     * Moves the shape by the requested amount.
     * 
     * @param shape The shape to move
     * @param x X offset
     * @param y Y offset
     */
    private void moveShapeBy(IState shape, float x, float y) {
        Point2D.Float center = shape.getCenter();

        // calculate the new location
        center.setLocation(center.getX() + x, center.getY() + y);

        shape.setCenter(center);
    }

    /**
     * Moves the rectangle to the specified location
     * 
     * @param x X coord
     * @param y Y coord
     */
    public void moveRectangleTo(float x, float y) {
        moveShapeTo(this._rectangle, x, y);
    }

    /**
     * Moves the diamond to the specified location
     * 
     * @param x X coord
     * @param y Y coord
     */
    public void moveDiamondTo(float x, float y) {
        moveShapeTo(this._diamond, x, y);
    }

    /**
     * Moves the shape to the specified location.
     * 
     * @param shape The shape to move
     * @param x X coord
     * @param y Y coord
     */
    private void moveShapeTo(IState shape, float x, float y) {
        Point2D.Float center = shape.getCenter();

        // calculate the new location
        center.setLocation(x, y);

        shape.setCenter(center);
    }

    /**
     * Rotates the rectangle by the specified number of degrees in the specified
     * direction
     * 
     * @param degrees Number of degrees to rotate
     * @param direction Direction to rotate in
     */
    public void rotateRectangle(float degrees, RotateDirection direction) {
        rotateShape(this._rectangle, degrees, direction);
    }

    /**
     * Rotates the diamond by the specified number of degrees in the specified
     * direction
     * 
     * @param degrees Number of degrees to rotate
     * @param direction Direction to rotate in
     */
    public void rotateDiamond(float degrees, RotateDirection direction) {
        rotateShape(this._diamond, degrees, direction);
    }

    /**
     * Rotates the triangle by the specified number of degrees in the specified
     * direction
     * 
     * @param degrees Number of degrees to rotate
     * @param direction Direction to rotate in
     */
    public void rotateTriangle(float degrees, RotateDirection direction) {
        rotateShape(this._triangle, degrees, direction);
    }

    /**
     * Rotates a shape by the specified number of degrees in the specified
     * direction
     * 
     * @param shape The shape to rotate
     * @param degrees Number of degrees to rotate
     * @param direction Direction to rotate in
     */
    private void rotateShape(IState shape, float degrees,
                             RotateDirection direction) {
        float rotation = direction.calculateRotation(degrees);

        shape.setDegreesRotatedCCW(shape.getDegreesRotatedCCW() + rotation);
    }

    /**
     * Swaps the colors of the objects. NOTE: Synchronized because the JOGL
     * thread as well as the ColorChangeAlert thread will use this function.
     */
    public void swapColors() {
        // get the colors
        GLColor rectColor = this._rectangle.getColor();
        GLColor diamondColor = this._diamond.getColor();

        // change the colors using the commands
        this.changeDiamondColor(rectColor);
        this.changeRectangleColor(diamondColor);
    }

    /**
     * Initalizes the glDrawable
     * 
     * @param glDrawable The GLDrawable to initalize
     */
    public void initalize(GLDrawable glDrawable) {
        final GL gl = glDrawable.getGL();
        final GLU glu = glDrawable.getGLU();

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-1.0f, 1.0f, -1.0f, 1.0f); // drawing square
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     * Called by the drawable during the first repaint after the component has
     * been resized. The client can update the viewport and view volume of the
     * window appropriately, for example by a call to GL.glViewport(int, int,
     * int, int); note that for convenience the component has already called
     * GL.glViewport(int, int, int, int)(x, y, width, height) when this method
     * is called, so the client may not have to do anything in this method.
     * 
     * @param gLDrawable The GLDrawable object.
     * @param x The X Coordinate of the viewport rectangle.
     * @param y The Y coordinate of the viewport rectanble.
     * @param width The new width of the window.
     * @param height The new height of the window.
     */
    public void reshape(GLDrawable gLDrawable, int x, int y, int width,
                        int height) {
        /*
         * System.out.println( "MyReshape called x=" + String.valueOf(x) + " y=" +
         * String.valueOf(y) + " width=" + String.valueOf(width) + " height=" +
         * String.valueOf(height));
         */

        final GL gl = gLDrawable.getGL();
        final GLU glu = gLDrawable.getGLU();

        gl.glViewport(0, 0, width, height); // update the viewport
    }

    /**
     * Exits the program
     */
    public void exit() {
        System.exit(0);
    }
}