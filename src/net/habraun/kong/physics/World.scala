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

	private[this] val bodies = new HashSet[Body]

	

	/**
	 * The broad phase is used for detecting which bodys can possible collide.
	 * This is done to cut down the time spent on doing detailed collision checks.
	 */

	private[this] var _broadPhase: BroadPhase = new SimpleBroadPhase

	def broadPhase = _broadPhase

	def broadPhase_=(bp: BroadPhase) = {
		if (bp == null) throw new NullPointerException
		_broadPhase = bp
	}



	/**
	 * The narrow phase is used to perform detailed (and possibly expensive) collision testing on body pairs
	 * that made it through the broad phase.
	 */

	private[this] var _narrowPhase: NarrowPhase = new SimpleNarrowPhase

	def narrowPhase = _narrowPhase

	def narrowPhase_=(np: NarrowPhase) = {
		if (np == null) throw new NullPointerException
		_narrowPhase = np
	}



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
		// Collision detection
		val collisionPairs = broadPhase.detectPossibleCollisions(bodies.toList)
		val collisions = collisionPairs.map((pair) => narrowPhase.inspectCollision(pair._1, pair._2))

		// Compute collision effects.
		// This is a tricky construction. The "possibleCollision <- collision" part is like an outer for loop
		// that iterates through all collisions. Collisions is a list of Option[Collision], this means,
		// during each iteration possibleCollision is either Some(collision) or None.
		// The "collision <- possibleCollision" part is technically an inner loop that iterates through the
		// Option[Collision]. This works because because Option is a collection that contains either one (if
		// if is an instance of Some) or no (if it is None) elements.
		// Despite the long explanation, what this does is actually pretty simple: We loop through the list
		// of possible collisions. We execute the yield stuff only for actual collisions, not for None.
		for ( possibleCollision <- collisions; collision <- possibleCollision ) yield {
			// Split the velocity vectors of the colliding bodies into a part that is orthogonal to the
			// collision normal and one that is parallel to the collision normal.
			val b1SplitVelocity = collision.b1.velocity.split(collision.normal1)
			val b1OrthogonalVelocity = b1SplitVelocity._1
			val b1ParallelVelocity = b1SplitVelocity._2
			val b2SplitVelocity = collision.b2.velocity.split(collision.normal2)
			val b2OrthogonalVelocity = b2SplitVelocity._1
			val b2ParallelVelocity = b2SplitVelocity._2

			// The parallel parts of the velocity are affected by the collision. Compute how they change.
			// Let's make some shorter variable names first, so I won't become crazy.
			val v1 = b1ParallelVelocity
			val v2 = b2ParallelVelocity
			val m1 = collision.b1.mass
			val m2 = collision.b2.mass
			val b1ChangedVelocity = {
				if (m1 == Double.PositiveInfinity)
					v1
				else if (m2 == Double.PositiveInfinity)
					v1.inverse + (v2 * 2)
				else
					(v1 * (m1 - m2) + (v2 * m2 * 2)) / (m1 + m2)
			}
			val b2ChangedVelocity = {
				if (m2 == Double.PositiveInfinity)
					v2
				else if (m1 == Double.PositiveInfinity)
					v2.inverse + (v1 * 2)
				else
					(v2 * (m2 - m1) + (v1 * m1 * 2)) / (m1 + m2)
			}

			// The part of the velocity that is orthogonal to the normal remains unchanged. To get the new
			// velocity we have to add the orthogonal, unchanged part to the parallel, changed part.
			collision.b1.velocity = b1OrthogonalVelocity + b1ChangedVelocity
			collision.b2.velocity = b2OrthogonalVelocity + b2ChangedVelocity
		}
		
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
