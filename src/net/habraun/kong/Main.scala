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
import java.awt.geom._
import javax.swing._

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._
import net.phys2d.math._
import net.phys2d.raw._



object Main {

	val screenSizeX = 800
	val screenSizeY = 600

	val border = 20

	val defaultStroke = new BasicStroke(0)



	def main(args: Array[String]) {
		// Configure the main window.
		val frame = new JFrame("Kong 0.2")
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		frame.setSize(screenSizeX + 12, screenSizeY + 35)

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

		// Initialize paddles
		val paddleInitialY= screenSizeY / 2 - Paddle.radius
		val paddle1 = new Paddle(border, paddleInitialY)
		val paddle2 = new Paddle(screenSizeX - border - Paddle.radius, paddleInitialY)
		val paddles = paddle1::paddle2::Nil

		// Initialize world for physics simulation and add all bodies
		val world = new World(new Vector2f(0, 0), 10)
		paddles.foreach((paddle) => world.add(paddle.body))

		// Initialize graphic objects
		val shape = new Ellipse2D.Double(0, 0, Paddle.radius, Paddle.radius)
		val paddleNodes = paddles.map((paddle) => {
			val node = new PPath(shape)
			node.setPaint(Color.RED)
			node.setStroke(defaultStroke)

			node
		})
		paddleNodes.foreach((node) => canvas.getLayer.addChild(node))

		frame.setVisible(true)

		while (true) {
			// Process input
			for (i <- 0 until paddles.length) {
				if (inputHandler.isUpPressed(i)) {
					paddles(i).movementUp
				}
				else if (inputHandler.isDownPressed(i)) {
					paddles(i).movementDown
				}
			}

			world.step

			// Display game state
			for (i <- 0 until paddles.length) {
				SwingUtilities.invokeLater(new Runnable { def run {
					val position = paddles(i).body.getPosition
					paddleNodes(i).setTransform(AffineTransform.getTranslateInstance(position.getX,
							position.getY))
				}})
			}

			Thread.sleep(17)
		}
	}
		
}
