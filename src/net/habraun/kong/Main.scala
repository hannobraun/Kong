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



package net.habraun.kong



import java.awt._
import javax.swing._

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._



object Main {

	val screenSizeX = 800
	val screenSizeY = 600

	val defaultStroke = new BasicStroke(0)



	def main(args: Array[String]) {
		// Configure the main window.
		val frame = new JFrame("Kong 0.2")
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		frame.setSize(800, 600)

		// Configure the canvas where the scene graph is painted on.
		val canvas = new PCanvas
		canvas.removeInputEventListener(canvas.getZoomEventHandler)
		canvas.removeInputEventListener(canvas.getPanEventHandler)
		frame.add(canvas)

		// Configure the background color.
		val background = PPath.createRectangle(0, 0, screenSizeX, screenSizeY)
		background.setPaint(Color.ORANGE)
		canvas.getLayer.addChild(background)

		// Configure the middle line.
		val middleLine = PPath.createRectangle(screenSizeX / 2 - 1, 0, 2, screenSizeY)
		middleLine.setPaint(Color.RED)
		middleLine.setStroke(defaultStroke)
		canvas.getLayer.addChild(middleLine)

		// Configure the input handler
		val inputHandler = new InputHandler
		canvas.getRoot.getDefaultInputManager.setKeyboardFocus(inputHandler)

		frame.setVisible(true)

		while (true) {
			Console.println(inputHandler.isUpPressed(0))
			Thread.sleep(50)
		}
	}
		
}
