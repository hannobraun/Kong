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
import game.GameUpdater
import game.Paddle
import input.DownKey
import input.InputProcessor
import input.InputSetup
import input.PlayerLeft
import input.PlayerRight
import input.UpKey
import ui.BallView
import ui.PaddleView
import ui.Renderer
import ui.UISetup

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

	val config = DefaultConfiguration



	def main( args: Array[String] ) {
		// Setup
		val setup = new Setup( config, new GameSetup( config ), new UISetup( config ), new InputSetup )

		// Initialize main loop functions.
		val processInput = new InputProcessor
		val updateGame = new GameUpdater( config )
		val render = new Renderer
		
		val nanoDelta = ( config.dt * 1000000000 ).asInstanceOf[ Long ]

		var accumulator: Long = 0
		var lastUpdate = System.nanoTime

		// Game loop
		while ( true ) {
			while ( accumulator >= nanoDelta ) {
				processInput( setup.keyHandler, setup.paddles )
				updateGame( config.dt, setup.world, setup.ball, setup.score )
				
				accumulator -= nanoDelta
			}

			render( setup.views )

			val currentTime = System.nanoTime
			accumulator += currentTime - lastUpdate
			lastUpdate = currentTime
		}
	}
}
