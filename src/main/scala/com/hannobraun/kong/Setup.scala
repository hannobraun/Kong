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



package com.hannobraun.kong



import com.hannobraun.kong.game.GameSetup
import com.hannobraun.kong.game.Score
import com.hannobraun.kong.input.InputSetup
import com.hannobraun.kong.ui.BallView
import com.hannobraun.kong.ui.PaddleView
import com.hannobraun.kong.ui.ScoreView
import com.hannobraun.kong.ui.UISetup

import com.hannobraun.sd.World
import com.hannobraun.sd.core.Body



class Setup( config: Configuration, gameSetup: GameSetup, uiSetup: UISetup, inputSetup: InputSetup ) {

	// Set up game.
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
	val frame = uiSetup.createFrame
	val canvas = uiSetup.createCanvas( frame )
	val views = {
		val paddleViews = paddles.map( new PaddleView( config, _ ) )
		val ballView = new BallView( config, ball )
		val scoreView = new ScoreView( score, config.screenSizeX / 2, config.screenSizeY / 2 )

		paddleViews:::List( ballView, scoreView )
	}

	// Add views to the canvas.
	views.foreach( canvas.getLayer.addChild( _ ) )

	// Set up the input handling
	val keyHandler = inputSetup.createKeyHandler( canvas )

	// Make UI visible.
	frame.setVisible( true )
	canvas.requestFocusInWindow
}
