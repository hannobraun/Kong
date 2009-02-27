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



import org.junit._
import org.junit.Assert._



class WorldTest {

	@Test
	def addBodyStepExpectBodyMoved {
		val world = new World
		val body = new Body
		body.velocity = Vec2D(1, 0)
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(2, 0), body.position)
	}



	@Test
	def addAndRemoveBodyExpectBodyNotMoved {
		val world = new World
		val body = new Body
		body.velocity = Vec2D(1, 0)
		world.add(body)
		world.remove(body)
		world.step(2.0)
		assertEquals(Vec2D(0, 0), body.position)
	}



	@Test
	def addBodyApplyForceCheckVelocity {
		val world = new World
		val body = new Body
		body.mass = 5
		body.applyForce(Vec2D(5, 0))
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(2, 0), body.velocity)
	}



	@Test
	def addBodyApplyForceStepTwiceCheckVelocity {
		val world = new World
		val body = new Body
		body.mass = 5
		body.applyForce(Vec2D(5, 0))
		world.add(body)
		world.step(2.0)
		world.step(2.0)
		assertEquals(Vec2D(2, 0), body.velocity)
	}



	@Test
	def addBodyApplyForceStepCheckPosition {
		val world = new World
		val body = new Body
		body.mass = 5
		body.applyForce(Vec2D(5, 0))
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(4, 0), body.position)
	}



	@Test
	def addBodyDisallowXMovementStepCheckPosition {
		val world = new World
		val body = new Body
		body.allowXMovement(false)
		body.applyForce(Vec2D(1, 1))
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(0, 4), body.position)
	}



	@Test
	def addBodyDisallowYMovementStepCheckPosition {
		val world = new World
		val body = new Body
		body.allowYMovement(false)
		body.applyForce(Vec2D(1, 1))
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(4, 0), body.position)
	}



	@Test
	def createWorldVerifyInitialBroadPhase {
		val world = new World
		assertTrue(world.broadPhase.isInstanceOf[SimpleBroadPhase])
	}



	@Test
	def createWorldVerifyInitialNarrowPhase {
		val world = new World
		assertEquals(NoNarrowPhase, world.narrowPhase)
	}
}
