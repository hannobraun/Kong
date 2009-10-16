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



import game.Ball
import game.Border
import game.GameSetup
import game.Paddle
import input.DownKey
import input.InputSetup
import input.PlayerLeft
import input.PlayerRight
import input.UpKey
import ui.UISetup
import util.PiccoUtil.updateSG

import java.awt.BasicStroke
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.geom.AffineTransform
import java.awt.geom.Ellipse2D
import javax.swing.JFrame

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

	val screenSizeX = 800
	val screenSizeY = 600

	val defaultStroke = new BasicStroke( 0 )

	val timeStep = 1.0 / 50.0



	def main(args: Array[String]) {
		// Set up the UI.
		val uiSetup = new UISetup
		val frame = uiSetup.createFrame
		val canvas = uiSetup.createCanvas( frame )

		// Set up the input handling
		val inputSetup = new InputSetup
		val keyHandler = inputSetup.createKeyHandler( canvas )

		// Set up game.
		val gameSetup = new GameSetup
		val paddles = gameSetup.createPaddles
		val ball = gameSetup.createBall
		val borders = gameSetup.createBorders

		// Initialize world for physics simulation and add all bodies
		val world = new World[Body]
		paddles.foreach( world.add( _ ) )
		world.add( ball )
		borders.foreach( world.add( _ ) )

		// Initialize scene graph nodes for paddles
		val paddleShape = new Ellipse2D.Double(0, 0, Paddle.radius * 2, Paddle.radius * 2)
		val paddleNodes = paddles.map((paddle) => {
			val node = new PPath(paddleShape)
			node.setPaint(Color.RED)
			node.setStroke(defaultStroke)

			node
		})
		paddleNodes.foreach((node) => canvas.getLayer.addChild(node))

		// Initialize the scene graph node for the ball
		val ballShape = new Ellipse2D.Double(0, 0, Ball.radius * 2, Ball.radius * 2)
		val ballNode = new PPath(ballShape)
		ballNode.setPaint(Color.RED)
		ballNode.setStroke(defaultStroke)
		canvas.getLayer.addChild(ballNode)

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
			updateSG( () => {
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
			} )

			val delta = System.currentTimeMillis - timeBefore
			val missing = (timeStep * 1000).toLong - delta
			if (missing > 0) {
				Thread.sleep(missing)
			}
		}
	}	
}
