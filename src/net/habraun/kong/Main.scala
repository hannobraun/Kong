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



import input._

import java.awt._
import java.awt.event._
import java.awt.geom._
import javax.swing._

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._
import net.phys2d.math._
import net.phys2d.raw._
import net.phys2d.raw.shapes._



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
		object PlayerLeft extends Player
		object PlayerRight extends Player
		object UpKey extends Key
		object DownKey extends Key
		val keyMap = (new KeyMap)
				.addMapping(PlayerLeft, UpKey, KeyEvent.VK_W)
				.addMapping(PlayerLeft, DownKey, KeyEvent.VK_S)
				.addMapping(PlayerRight, UpKey, KeyEvent.VK_UP)
				.addMapping(PlayerRight, DownKey, KeyEvent.VK_DOWN)
		val keyHandler = new KeyHandler(keyMap)
		canvas.getRoot.getDefaultInputManager.setKeyboardFocus(keyHandler.handler)

		// Initialize paddles
		val paddle1 = new Paddle(PlayerLeft, border + Paddle.radius, screenSizeY / 2)
		val paddle2 = new Paddle(PlayerRight, screenSizeX - border - Paddle.radius, screenSizeY / 2)
		val paddles = paddle1::paddle2::Nil

		// Initialize scene graph nodes for paddles
		val paddleShape = new Ellipse2D.Double(0, 0, Paddle.radius * 2, Paddle.radius * 2)
		val paddleNodes = paddles.map((paddle) => {
			val node = new PPath(paddleShape)
			node.setPaint(Color.RED)
			node.setStroke(defaultStroke)

			node
		})
		paddleNodes.foreach((node) => canvas.getLayer.addChild(node))

		// Initialize the ball
		val ball = new Ball(screenSizeX / 2, screenSizeY / 2)
		ball.init

		// Initialize the scene graph node for the ball
		val ballShape = new Ellipse2D.Double(0, 0, Ball.radius * 2, Ball.radius * 2)
		val ballNode = new PPath(ballShape)
		ballNode.setPaint(Color.RED)
		ballNode.setStroke(defaultStroke)
		canvas.getLayer.addChild(ballNode)

		// Initialize the borders
		val borderShape = new Line(screenSizeX, 0)
		val topBorder = new StaticBody(borderShape)
		val bottomBorder = new StaticBody(borderShape)
		topBorder.setPosition(0, 0)
		bottomBorder.setPosition(0, screenSizeY)
		topBorder.setFriction(0)
		bottomBorder.setFriction(0)
		topBorder.setRestitution(1)
		bottomBorder.setRestitution(1)

		// Initialize world for physics simulation and add all bodies
		val world = new World(new Vector2f(0, 0), 1000)
		paddles.foreach((paddle) => world.add(paddle.body))
		world.add(ball.body)
		world.add(topBorder)
		world.add(bottomBorder)

		// Initialize score
		val score = new Score(screenSizeX / 2, screenSizeY / 2)
		canvas.getLayer.addChild(score.node)
		
		frame.setVisible(true)
		canvas.requestFocusInWindow

		// Game loop
		while (true) {
			// Process input
			paddles.foreach((paddle) => {
				if (keyHandler.isPressed(paddle.getPlayer, UpKey))
					paddle.movementUp
				else if (keyHandler.isPressed(paddle.getPlayer, DownKey))
					paddle.movementDown
				else
					paddle.movementStop
			})

			// Step the physics simulation
			world.step

			// Check if the ball left the field and needs to be placed in the middle again
			val  ballX = ball.body.getPosition.getX
			if (ballX > screenSizeX) {
				score.increaseScore1
				ball.init
			}
			if (ballX < 0) {
				score.increaseScore2
				ball.init
			}

			// Display game state
			SwingUtilities.invokeLater(new Runnable { def run {
				for (i <- 0 until paddles.length) {
					val position = paddles(i).body.getPosition
					val x = position.getX - Paddle.radius
					val y = position.getY - Paddle.radius

					paddleNodes(i).setTransform(AffineTransform.getTranslateInstance(x, y))
				}

				val position = ball.body.getPosition
				val x = position.getX - Ball.radius
				val y = position.getY - Ball.radius
				ballNode.setTransform(AffineTransform.getTranslateInstance(x, y))

				score.update
			}})

			Thread.sleep(17)
		}
	}
		
}
