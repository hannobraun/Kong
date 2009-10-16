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
import ui.BallView
import ui.PaddleView
import ui.Score
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
		// Setup
		val setup = new Setup

		// Game loop
		while (true) {
			val timeBefore = System.currentTimeMillis

			// Process input
			setup.paddles.foreach((paddle) => {
				if (setup.keyHandler.isPressed(paddle.getPlayer, UpKey))
					paddle.movementUp
				else if (setup.keyHandler.isPressed(paddle.getPlayer, DownKey))
					paddle.movementDown
				else
					paddle.movementStop
			})

			// Step the physics simulation
			setup.world.step(timeStep)

			// Check if the ball left the field and needs to be placed in the middle again
			val  ballX = setup.ball.position.x
			if (ballX > screenSizeX) {
				setup.scoreView.increaseScore1
				setup.ball.init
			}
			if (ballX < 0) {
				setup.scoreView.increaseScore2
				setup.ball.init
			}

			// Display game state
			updateSG( () => {
				for (i <- 0 until setup.paddles.length) {
					val position = setup.paddles( i ).position
					val x = position.x - Paddle.radius
					val y = position.y - Paddle.radius

					setup.paddleViews(i).setTransform(AffineTransform.getTranslateInstance(x, y))
				}

				val position = setup.ball.position
				val x = position.x - Ball.radius
				val y = position.y - Ball.radius
				setup.ballView.setTransform(AffineTransform.getTranslateInstance(x, y))

				setup.scoreView.update
			} )

			val delta = System.currentTimeMillis - timeBefore
			val missing = (timeStep * 1000).toLong - delta
			if (missing > 0) {
				Thread.sleep(missing)
			}
		}
	}	
}
