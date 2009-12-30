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



import scala.util.Random

import net.habraun.sd.collision.shape.Circle
import net.habraun.sd.core.Body
import net.habraun.sd.dynamics.VelocityConstraint
import net.habraun.sd.math.Vector2



class Ball( startingX: Int, startingY: Int ) extends Body with Circle with VelocityConstraint {
	
	private val r = new Random

	override val radius = Ball.radius
	mass = Ball.mass
	maxVelocity = 550



	def init {
		val mod = (r: Random) => (r.nextInt(2) + 1) * 2 - 3 // result: -1 or +1
		val vel = (r: Random, min: Int, factor: Float) => (r.nextInt(101) + min) * factor

		val xMod = mod(r)
		val yMod = mod(r)
		val xVel = xMod * vel(r, 100, 3)
		val yVel = yMod * vel(r, 0, 2)

		velocity = Vector2( xVel, yVel )
		position = Vector2( startingX, startingY )
	}
}



object Ball {
	val radius = 5.0
	val mass = 1
}
