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



import java.awt.BasicStroke
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.geom.AffineTransform
import java.awt.geom.Ellipse2D
import javax.swing.JFrame
import javax.swing.SwingUtilities

import edu.umd.cs.piccolo.PCanvas
import edu.umd.cs.piccolo.nodes.PPath
import net.habraun.piccoinput.Key
import net.habraun.piccoinput.KeyHandler
import net.habraun.piccoinput.KeyMap
import net.habraun.piccoinput.Player
import net.habraun.sd.World
import net.habraun.sd.collision.shape.LineSegment
import net.habraun.sd.core.Body
import net.habraun.sd.math.Vec2D



object Main {

	val timeStep = 1.0 / 50.0

	val screenSizeX = 800
	val screenSizeY = 600

	val border = 20

	val defaultStroke = new BasicStroke(0)



	def main(args: Array[String]) {
		// Configure the main window.
		val frame = new JFrame( "Kong 0.4" )
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
		val p = Vec2D( 0, 0 )
		val d = Vec2D( screenSizeX, 0 )
		val topBorder = new Body with LineSegment {}
		val bottomBorder = new Body with LineSegment {}
		topBorder.mass = Double.PositiveInfinity
		bottomBorder.mass = Double.PositiveInfinity
		topBorder.p = p
		bottomBorder.p = p
		topBorder.d = d
		bottomBorder.d = d
		topBorder.position = Vec2D( 0, 0 )
		bottomBorder.position = Vec2D( 0, screenSizeY )

		// Initialize world for physics simulation and add all bodies
		val world = new World[Body]
		paddles.foreach( world.add( _ ) )
		world.add( ball )
		world.add(topBorder)
		world.add(bottomBorder)

		// Initialize score
		val score = new Score(screenSizeX / 2, screenSizeY / 2)
		canvas.getLayer.addChild(score.node)
		
		frame.setVisible(true)
		canvas.requestFocusInWindow

		// Game loop
		while (true) {
			val timeBefore = System.currentTimeMillis

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
			world.step(timeStep)

			// Check if the ball left the field and needs to be placed in the middle again
			val  ballX = ball.position.x
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
					val position = paddles( i ).position
					val x = position.x - Paddle.radius
					val y = position.y - Paddle.radius

					paddleNodes(i).setTransform(AffineTransform.getTranslateInstance(x, y))
				}

				val position = ball.position
				val x = position.x - Ball.radius
				val y = position.y - Ball.radius
				ballNode.setTransform(AffineTransform.getTranslateInstance(x, y))

				score.update
			}})

			val delta = System.currentTimeMillis - timeBefore
			val missing = (timeStep * 1000).toLong - delta
			if (missing > 0) {
				Thread.sleep(missing)
			}
		}
	}	
}
