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



package com.hannobraun.kong.game



import com.hannobraun.kong.Configuration

import com.hannobraun.piccoinput.Player
import com.hannobraun.sd.collision.shape.Circle
import com.hannobraun.sd.core.Body
import com.hannobraun.sd.dynamics.PositionConstraint
import com.hannobraun.sd.math.Vector2



class Paddle( config: Configuration, player: Player, initialX: Double, initialY: Double )
		extends Body with Circle  with PositionConstraint {

	mass = Double.PositiveInfinity
	override val radius = Paddle.radius
	position = Vector2( initialX, initialY )
	minX = Some( initialX )
	maxX = Some( initialX )
	minY = Some( Paddle.radius )
	maxY = Some( config.screenSizeY - Paddle.radius )



	def getPlayer = player



	def movementUp {
		setSpeed( -Paddle.speed )
	}



	def movementDown {
		setSpeed( Paddle.speed )
	}



	def movementStop {
		setSpeed( 0 )
	}



	private[ this ] def setSpeed( nominalSpeed: Double ) {
		velocity = Vector2( 0, nominalSpeed )
	}
}



object Paddle {
	val radius = 30.0
	val speed = 500
}
