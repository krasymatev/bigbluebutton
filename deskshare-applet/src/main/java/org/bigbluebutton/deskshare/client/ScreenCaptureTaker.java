/*
 * BigBlueButton - http://www.bigbluebutton.org
 * 
 * Copyright (c) 2008-2009 by respective authors (see below). All rights reserved.
 * 
 * BigBlueButton is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 3 of the License, or (at your option) any later 
 * version. 
 * 
 * BigBlueButton is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along 
 * with BigBlueButton; if not, If not, see <http://www.gnu.org/licenses/>.
 *
 * $Id: $
 */
package org.bigbluebutton.deskshare.client;

import java.awt.image.BufferedImage;

public class ScreenCaptureTaker implements Runnable {
	
	private ScreenCapture capture;
	private int timeBase;
	private int frameCount = 0;
	private IScreenCaptureListener listeners;
	
	private volatile boolean startCapture = false;
		
	public ScreenCaptureTaker(ScreenCapture capture){
		System.out.println("Capture thread constructor.");
		this.capture = capture;
		this.timeBase = 1000 / capture.getProperFrameRate();
	}
	
	public void run(){		
//		while (startCapture){
		for (int i = 0; i < 1000; i++) {
			BufferedImage image = capture.takeSingleSnapshot();
			notifyListeners(image);
			try{
				//Thread.sleep(timeBase);
				Thread.sleep(1000);
			} catch (Exception e){
				System.out.println("Exception sleeping.");
				System.exit(0);
			}
		}
		
		System.out.println("Stopping screen capture.");
		
		listeners.screenCaptureStopped();
	}
	
	private void notifyListeners(BufferedImage image) {
		listeners.onScreenCaptured(image, isKeyFrame());
	}
	
	private boolean isKeyFrame() {
		if (frameCount == 0) {
			System.out.println("Is Key Frame " + frameCount);
			frameCount++;
			
			return true;
		} else {
			System.out.println("Is Not Key Frame " + frameCount);
			if (frameCount < 10) {
				frameCount++;
			} else {
				frameCount = 0;
			}
			
			return false;
		}
	}
	
	public void addListener(IScreenCaptureListener listener) {
//		listeners.add(listener);
		listeners = listener;
	}

	public void removeListener(IScreenCaptureListener listener) {
//		listeners.remove(listener);
	}
	
	public void setCapture(boolean capture) {
		startCapture = capture;
	}
}