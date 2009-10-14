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



import net.habraun.piccoinput._
import net.habraun.sd.collision.shape.Circle
import net.habraun.sd.core.Body
import net.habraun.sd.dynamics.PositionConstraint
import net.habraun.sd.math.Vec2D



class Paddle( player: Player, initialX: Float, initialY: Float )
		extends Body with Circle  with PositionConstraint{

	mass = 100
	radius = Paddle.radius
	position = Vec2D( initialX, initialY )
	xConstraint = Some( initialX )



	def getPlayer = player



	def movementUp {
		setSpeed(-Paddle.speed)
	}



	def movementDown {
		setSpeed(Paddle.speed)
	}



	def movementStop {
		setSpeed(0)
	}



	private[this] def setSpeed( nominalSpeed: Double ) {
		val speedDifference = Math.abs( nominalSpeed - velocity.y )
		val undirectedForce = speedDifference / Main.timeStep * mass
		val force = if ( nominalSpeed > velocity.y ) undirectedForce else -undirectedForce
		applyForce( new Vec2D( 0, force ) )
	}
}



object Paddle {
	val radius = 30
	val speed = 500
}
