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



class CollisionTest {

	@Test
	def verifyHasAttributes {
		val b1 = new Body
		val b2 = new Body
		val normal1 = Vec2D(0, 1)
		val normal2 = Vec2D(0, -1)
		val impactPoint = Vec2D(10, 10)
		val collision = Collision(b1, b2, normal1, normal2, impactPoint)
		assertEquals(b1, collision.b1)
		assertEquals(b2, collision.b2)
		assertEquals(normal1, collision.normal1)
		assertEquals(normal2, collision.normal2)
		assertEquals(impactPoint, collision.impactPoint)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createCollisionWithNonInverseNormalsExpectException {
		Collision(new Body, new Body, Vec2D(1, 0), Vec2D(0, -1), Vec2D(0, 0))
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createCollisionWithNonUnitNormalsExpectException {
		Collision(new Body, new Body, Vec2D(1, 1), Vec2D(-1, -1), Vec2D(0, 0))
	}



	@Test
	def createCollisionWithSlightlyOffNonUnitNormalsExpectTolerance {
		Collision(new Body, new Body, Vec2D(1.02, 0), Vec2D(-1.02, 0), Vec2D(0, 0))
		Collision(new Body, new Body, Vec2D(0.98, 0), Vec2D(-0.98, 0), Vec2D(0, 0))
	}
}
