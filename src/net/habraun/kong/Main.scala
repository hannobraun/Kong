/*
	Copyright (c) 2008 Hanno Braun <hanno@habraun.net>

	Permission to use, copy, modify, and/or distribute this software for any
	purpose with or without fee is hereby granted, provided that the above
	copyright notice and this permission notice appear in all copies.

	THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
	WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
	MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
	ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
	WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
	ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
	OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
*/



package net.habraun.kong



import java.awt._
import java.awt.event._
import java.awt.geom._
import javax.swing._

import com.sun.scenario.scenegraph._
import com.sun.scenario.scenegraph.event._



abstract case class Key
case object WKey extends Key
case object SKey extends Key
case object UpKey extends Key
case object DownKey extends Key
case object NoKey extends Key



object Main {

	val screenXBorder = 20

	val screenWidth = 800
	val screenHeight = 600

	val paddleWidth = 30
	val paddleHeight = 150
	val paddleCornerArc = 10
	val paddleSpeed = 0.5

	val ballSize = 15
	val ballMiddleDistance = 50
	val ballSpeed = 0.5

	val initialBall1X = screenWidth / 2 - ballMiddleDistance - ballSize
	val initialBall1Y = screenHeight / 2 - ballSize / 2
	val initialBall2X = screenWidth / 2 + ballMiddleDistance
	val initialBall2Y = screenHeight / 2 - ballSize / 2
	val initialBall1Speed = (ballSpeed, Math.Pi)
	val initialBall2Speed = (ballSpeed, 0.0)



	def main(args: Array[String]) {
		val frame = new JFrame("Kong 0.1")
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		val panel = new JSGPanel
		val scene = new SGGroup

		val background = new SGShape
		background.setShape(new Rectangle(screenWidth, screenHeight))
		background.setFillPaint(Color.ORANGE)
		scene.add(background)

		val middleLine = new SGShape
		middleLine.setShape(new Rectangle(2, screenHeight))
		middleLine.setFillPaint(Color.RED)
		val middleLineTrans = SGTransform.createTranslation(screenWidth / 2 - 1, 0.0, middleLine)
		scene.add(middleLineTrans)

		panel.setScene(scene)
		frame.add(panel)
		frame.pack
		frame.setVisible(true)

		val paddle1 = new SGShape
		paddle1.setShape(new RoundRectangle2D.Double(0.0, 0.0, paddleWidth, paddleHeight, paddleCornerArc,
													 paddleCornerArc))
		paddle1.setFillPaint(Color.RED)
		val paddle1Trans = SGTransform.createTranslation(screenXBorder, screenHeight / 2 - paddleHeight / 2,
														 paddle1)
		scene.add(paddle1Trans)

		val paddle2 = new SGShape
		paddle2.setShape(new RoundRectangle2D.Double(0.0, 0.0, paddleWidth, paddleHeight, paddleCornerArc,
													 paddleCornerArc))
		paddle2.setFillPaint(Color.RED)
		val paddle2Trans = SGTransform.createTranslation(screenWidth - paddleWidth - screenXBorder,
														 screenHeight / 2 - paddleHeight / 2, paddle2)
		scene.add(paddle2Trans)

		val ball1 = new SGShape
		ball1.setShape(new Ellipse2D.Double(0, 0, ballSize, ballSize))
		ball1.setFillPaint(Color.RED)
		val ball1Trans = SGTransform.createTranslation(initialBall1X, initialBall1Y, ball1)
		scene.add(ball1Trans)

		val ball2 = new SGShape
		ball2.setShape(new Ellipse2D.Double(0, 0, ballSize, ballSize))
		ball2.setFillPaint(Color.RED)
		val ball2Trans = SGTransform.createTranslation(initialBall2X, initialBall2Y, ball2)
		scene.add(ball2Trans)

		val keyListener = new SGKeyListener {

			var pressedKey1: Key = NoKey
			var pressedKey2: Key = NoKey

			def keyPressed(event: KeyEvent, node: SGNode) {
				if (event.getKeyCode == KeyEvent.VK_W) {
					pressedKey1 = WKey
				}
				else if (event.getKeyCode == KeyEvent.VK_S) {
					pressedKey1 = SKey
				}
				else if (event.getKeyCode == KeyEvent.VK_UP) {
					pressedKey2 = UpKey
				}
				else if (event.getKeyCode == KeyEvent.VK_DOWN) {
					pressedKey2 = DownKey
				}
			}

			def keyReleased(event: KeyEvent, node: SGNode) {
				if (event.getKeyCode == KeyEvent.VK_W && pressedKey1 == WKey) {
					pressedKey1 = NoKey
				}
				else if (event.getKeyCode == KeyEvent.VK_S && pressedKey1 == SKey) {
					pressedKey1 = NoKey
				}
				else if (event.getKeyCode == KeyEvent.VK_UP && pressedKey2 == UpKey) {
					pressedKey2 = NoKey
				}
				else if (event.getKeyCode == KeyEvent.VK_DOWN && pressedKey2 == DownKey) {
					pressedKey2 = NoKey
				}
			}

			def keyTyped(event: KeyEvent, node: SGNode) {}
		}
		background.addKeyListener(keyListener)

		var lastTime = System.currentTimeMillis
		var ball1Speed = initialBall1Speed
		var ball2Speed = initialBall2Speed
		while (true) {
			val deltaTime = System.currentTimeMillis - lastTime
			lastTime = System.currentTimeMillis

			val speed1 = keyListener.pressedKey1 match {
				case WKey => -paddleSpeed
				case SKey => paddleSpeed
				case NoKey => 0.0
			}
			translatePaddle(paddle1Trans, speed1, deltaTime)

			val speed2 = keyListener.pressedKey2 match {
				case UpKey => -paddleSpeed
				case DownKey => paddleSpeed
				case NoKey => 0.0
			}
			translatePaddle(paddle2Trans, speed2, deltaTime)

			ball1Speed = translateBall(ball1Trans, ball1Speed, deltaTime, paddle1Trans, paddle2Trans)
			ball2Speed = translateBall(ball2Trans, ball2Speed, deltaTime, paddle1Trans, paddle2Trans)

			Thread.sleep(20)
		}
	}



	private def translatePaddle(trans: SGTransform.Translate, speed: Double, deltaTime: Double) {
		trans.setTranslateY(Math.min(screenHeight - paddleHeight,
									 Math.max(0, trans.getTranslateY + (speed * deltaTime))))
	}



	private def translateBall(trans: SGTransform.Translate, speed: (Double, Double), delta: Double, 
			paddle1Trans: SGTransform.Translate, paddle2Trans: SGTransform.Translate): (Double, Double) = {
		val x = trans.getTranslateX
		val y = trans.getTranslateY
		val newX = x + speed._1 * Math.cos(speed._2) * delta
		val newY = Math.min(screenHeight - ballSize, Math.max(0, y - speed._1 * Math.sin(speed._2) * delta))

		if (newX > screenWidth) {
			trans.setTranslation(initialBall1X, initialBall1Y)
			initialBall1Speed
		}
		else if (newX < 0 - ballSize) {
			trans.setTranslation(initialBall2X, initialBall2Y)
			initialBall2Speed
		}
		else if (newY == 0 || newY == screenHeight - ballSize) {
			(speed._1, -speed._2)
		}
		else if (newX + ballSize >= screenWidth - screenXBorder - paddleWidth
				 && newX < screenWidth - screenXBorder) {
			val paddle2CenterDistance = newY + ballSize / 2 - paddle2Trans.getTranslateY - paddleHeight / 2
			if (paddle2CenterDistance < -paddleHeight / 2 || paddle2CenterDistance > paddleHeight / 2) {
				trans.setTranslation(newX, newY)
				speed
			}
			else {
				val yMirroredDirection = Math.Pi - speed._2
				val angleAddend = (paddle2CenterDistance / (paddleHeight / 2)) * 0.25 * Math.Pi
				val divertedDirection = yMirroredDirection + angleAddend
				(speed._1, Math.min(1.25 * Math.Pi, Math.max(0.75 * Math.Pi, divertedDirection)))
			}
		}
		else if (newX < screenXBorder + paddleWidth && newX >= screenXBorder) {
			val paddle1CenterDistance = newY + ballSize / 2 - paddle1Trans.getTranslateY - paddleHeight / 2
			if (paddle1CenterDistance < -paddleHeight / 2 || paddle1CenterDistance > paddleHeight / 2) {
				trans.setTranslation(newX, newY)
				speed
			}
			else {
				val yMirroredDirection = Math.Pi - speed._2
				val angleAddend = (-paddle1CenterDistance / (paddleHeight / 2)) * 0.25 * Math.Pi
				val divertedDirection = yMirroredDirection + angleAddend
				(speed._1, Math.min(0.25 * Math.Pi, Math.max(-0.25 * Math.Pi, divertedDirection)))
			}
		}
		else {
			trans.setTranslation(newX, newY)
			speed
		}
	}
}
