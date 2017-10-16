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

import java.util.TimerTask;

/**
 * Simple class which tells the JOGLEventListener to swap the colors of the
 * shapes. Should be triggered every 10 seconds.
 * 
 * @author Kevin Conroy
 */
public class ColorChangeAlert extends TimerTask {

    /**
     * The event listener to trigger a timer on
     */
    private JoglEventMediator _listener;

    /**
     * @param listener The event listener to trigger a timer on
     */
    public ColorChangeAlert(JoglEventMediator listener) {
        this._listener = listener;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        //System.out.println("Timer just went off. Reversing colors.");
        this._listener.swapColors();
    }

}
