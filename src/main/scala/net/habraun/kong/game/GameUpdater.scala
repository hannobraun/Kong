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



import net.habraun.sd.World



class GameUpdater( config: Configuration ) extends Function4[ Double, World[ _ ], Ball, Score, Unit ] {

	def apply( dt: Double, world: World[ _ ], ball: Ball, score: Score ) {
		// Step the physics simulation
		world.step( dt )

		// Check if the ball left the field and needs to be placed in the middle again
		val  ballX = ball.position.x
		if ( ballX > config.screenSizeX ) {
			score.increaseScore1
			ball.init
		}
		if (ballX < 0) {
			score.increaseScore2
			ball.init
		}
	}
}
