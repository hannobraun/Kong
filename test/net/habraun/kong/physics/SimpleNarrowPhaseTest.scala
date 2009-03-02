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



class SimpleNarrowPhaseTest {

	@Test
	def verifyIsNarrowPhase {
		val narrowPhase = new SimpleNarrowPhase
		assertTrue(narrowPhase.isInstanceOf[NarrowPhase])
	}



	@Test
	def inspectTwoNoShapesExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.shape = NoShape
		val b2 = new Body
		b2.shape = NoShape

		assertEquals(None, narrowPhase.inspectCollision(b1, b2))
	}



	@Test
	def inspectTwoCirclesExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(3, 0)
		b2.shape = Circle(1)

		assertEquals(None, narrowPhase.inspectCollision(b1, b2))
	}



	@Test
	def inspectTwoCirclesExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(0.5, 0)
		b2.shape = Circle(1)

		val expectedCollision = Collision(b1, b2, Vec2D(1, 0), Vec2D(-1, 0), Vec2D(0, 0))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(b1, b2))
	}



	@Test
	def inspectCircleAndNoShapeExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(0, 0.5)
		b2.shape = NoShape

		assertEquals(None, narrowPhase.inspectCollision(b1, b2))
	}



	@Test
	def inspectCircleAndLineSegmentExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(0, 2)
		b2.shape = LineSegment(Vec2D(-1, 0), Vec2D(1, 0))

		assertEquals(None, narrowPhase.inspectCollision(b1, b2))
	}



	@Test
	def inspectCircleAndLineSegmentExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(0, 0.5)
		b2.shape = LineSegment(Vec2D(-1, 0), Vec2D(1, 0))

		val expectedCollision = Collision(b1, b2, Vec2D(0, 1), Vec2D(0, -1), Vec2D(0, 0))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(b1, b2))
	}



	@Test
	def inspectLineSegmentAndCirlceExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(0, 0.5)
		b2.shape = LineSegment(Vec2D(-1, 0), Vec2D(1, 0))

		val expectedCollision = Collision(b2, b1, Vec2D(0, -1), Vec2D(0, 1), Vec2D(0, 0))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(b2, b1))
	}



	@Test
	def inspectTwoLineSegmentsExpectNoCollisionEvenIfTheyCollide {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 1)
		b1.shape = LineSegment(Vec2D(0, 0), Vec2D(2, -2))
		val b2 = new Body
		b2.position = Vec2D(0, -1)
		b2.shape = LineSegment(Vec2D(0, 0), Vec2D(2, 2))

		assertEquals(None, narrowPhase.inspectCollision(b1, b2))
	}
}
