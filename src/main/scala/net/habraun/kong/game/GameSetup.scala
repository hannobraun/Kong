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



class GameSetup {

	val border = 20



	def createPaddles = {
		// Initialize paddles
		val paddle1 = new Paddle( PlayerLeft, border + Paddle.radius, Main.screenSizeY / 2 )
		val paddle2 = new Paddle( PlayerRight, Main.screenSizeX - border - Paddle.radius,
				Main.screenSizeY / 2)

		List( paddle1, paddle2 )
	}



	def createBall = {
		// Initialize the ball
		val ball = new Ball( Main.screenSizeX / 2, Main.screenSizeY / 2 )
		ball.init

		ball
	}
}
