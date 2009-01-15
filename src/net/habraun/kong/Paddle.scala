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

	val body = new StaticBody(new Circle(Paddle.radius))
	body.setPosition(initialX, initialY)



	def movementUp {
		val newPosition = new Vector2f(body.getPosition)
		newPosition.add(new Vector2f(0, -Paddle.speed))
		body.setPosition(newPosition.x, newPosition.y)
	}



	def movementDown {
		val newPosition = new Vector2f(body.getPosition)
		newPosition.add(new Vector2f(0, Paddle.speed))
		body.setPosition(newPosition.x, newPosition.y)
	}
}



object Paddle {
	val radius = 50
	val speed = 5
}
