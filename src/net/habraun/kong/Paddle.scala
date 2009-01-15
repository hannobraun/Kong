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


import net.phys2d.math._
import net.phys2d.raw._
import net.phys2d.raw.shapes._



class Paddle(initialX: Float, initialY: Float) {

	val body = new Body(new Circle(Paddle.radius), 100)
	body.setPosition(initialX, initialY)
	body.setMaxVelocity(0, 1000000)
	body.setDamping(2)



	def movementUp {
		body.setForce(0, -Paddle.speed)
	}



	def movementDown {
		body.setForce(0, Paddle.speed)
	}



	def movementStop {
		body.setForce(0, 0)
	}
}



object Paddle {
	val radius = 30
	val speed = 100000
}
