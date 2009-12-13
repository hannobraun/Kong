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



package net.habraun.kong.game



import input.PlayerLeft
import input.PlayerRight

import net.habraun.sd.math.Vector2



class GameSetup( config: Configuration ) {

	val border = 20



	def createPaddles = {
		// Initialize paddles
		val paddle1 = new Paddle( config, PlayerLeft, border + Paddle.radius, config.screenSizeY / 2 )
		val paddle2 = new Paddle( config, PlayerRight, config.screenSizeX - border - Paddle.radius,
				config.screenSizeY / 2)

		List( paddle1, paddle2 )
	}



	def createBall = {
		// Initialize the ball
		val ball = new Ball( config.screenSizeX / 2, config.screenSizeY / 2 )
		ball.init

		ball
	}



	def createBorders = {
		// Initialize the borders
		val topBorder = new Border( config, Vector2( 0, 0 ) )
		val bottomBorder = new Border( config, Vector2( 0, config.screenSizeY ) )

		List( topBorder, bottomBorder )
	}
}
