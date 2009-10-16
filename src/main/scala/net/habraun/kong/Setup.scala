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



import game.GameSetup
import game.Score
import input.InputSetup
import ui.BallView
import ui.PaddleView
import ui.ScoreView
import ui.UISetup

import net.habraun.sd.World
import net.habraun.sd.core.Body



class Setup {

	// Set up game.
	val gameSetup = new GameSetup
	val paddles = gameSetup.createPaddles
	val ball = gameSetup.createBall
	val borders = gameSetup.createBorders
	val score = new Score

	// Initialize world for physics simulation and add all bodies
	val world = new World[Body]
	paddles.foreach( world.add( _ ) )
	world.add( ball )
	borders.foreach( world.add( _ ) )

	// Set up the UI.
	val uiSetup = new UISetup
	val frame = uiSetup.createFrame
	val canvas = uiSetup.createCanvas( frame )
	val paddleViews = paddles.map( new PaddleView( _ ) )
	val ballView = new BallView( ball )
	val scoreView = new ScoreView( score, Main.screenSizeX / 2, Main.screenSizeY / 2 )

	// Add views to the canvas.
	paddleViews.foreach( canvas.getLayer.addChild( _ ) )
	canvas.getLayer.addChild( ballView )
	canvas.getLayer.addChild( scoreView.node )

	// Set up the input handling
	val inputSetup = new InputSetup
	val keyHandler = inputSetup.createKeyHandler( canvas )

	// Make UI visible.
	frame.setVisible( true )
	canvas.requestFocusInWindow
}
