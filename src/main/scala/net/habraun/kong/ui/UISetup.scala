/*
	Copyright (c) 2008, 2009 Hanno Braun <hanno@habraun.net>

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/



package net.habraun.kong.ui



import java.awt.BasicStroke
import java.awt.Color
import javax.swing.JFrame

import edu.umd.cs.piccolo.PCanvas
import edu.umd.cs.piccolo.nodes.PPath



class UISetup {

	def createFrame = {
		// Configure the main window.
		val frame = new JFrame( "Kong 0.4" )
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )
		frame.setSize( Main.screenSizeX + 12, Main.screenSizeY + 35 )

		frame
	}



	def createCanvas( frame: JFrame ) = {
		// Configure the canvas where the scene graph is painted on.
		val canvas = new PCanvas
		canvas.removeInputEventListener( canvas.getZoomEventHandler )
		canvas.removeInputEventListener( canvas.getPanEventHandler )
		frame.add( canvas )

		// Configure the background color.
		val background = PPath.createRectangle( 0, 0, Main.screenSizeX, Main.screenSizeY )
		background.setPaint( Color.ORANGE )
		canvas.getLayer.addChild( background )

		// Configure the middle line.
		val middleLine = PPath.createRectangle( Main.screenSizeX / 2 - 1, 0, 2, Main.screenSizeY )
		middleLine.setPaint( Color.RED )
		middleLine.setStroke( Main.defaultStroke )
		canvas.getLayer.addChild( middleLine )

		canvas
	}
}
