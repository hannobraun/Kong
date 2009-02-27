/*
	Copyright (c) 2009 Hanno Braun <hanno@habraun.net>

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



package net.habraun.kong.physics



import scala.collection.mutable._



/**
 * The central class for the physics simulation.
 * World is basically a container for objects, whose attributes it updates every simulation step.
 */

class World {

	private[this] var bodies = new HashSet[Body]



	/**
	 * Adds a body to the world. The body will be simulated until it is removed.
	 */
	
	def add(body: Body) {
		bodies.addEntry(body)
	}



	/**
	 * Removes the body from the world. The body will no longer be simulated.
	 */

	def remove(body: Body) {
		bodies.removeEntry(body)
	}
	


	/**
	 * Steps the physics simulation.
	 * All bodies are moved, according to their velocity and the forces that are applied to them.
	 */
	
	def step(delta: Double) {
		bodies.foreach((body) => {
			// Apply forces.
			body.velocity = body.velocity + body.appliedForce / body.mass * delta
			body.resetForce

			// Solve movement constraints.
			val constrainedXVelocity = if (body.xMovementAllowed) body.velocity.x else 0.0
			val constrainedYVelocity = if (body.yMovementAllowed) body.velocity.y else 0.0
			val constrainedVelocity = Vec2D(constrainedXVelocity, constrainedYVelocity)

			// Move bodies.
			body.position = body.position + (constrainedVelocity * delta)
		})
	}
}
