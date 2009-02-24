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



class Vec2DTest {

	@Test
	def addition {
		val vec1 = Vec2D(1, 0)
		val vec2 = Vec2D(0, 1)
		assertEquals(Vec2D(1, 1), vec1 + vec2)
	}



	@Test
	def scalarMultiplication {
		val vec = Vec2D(1, 1)
		assertEquals(Vec2D(2, 2), vec * 2)
	}



	@Test
	def dotProduct {
		val vec1 = Vec2D(1, 2)
		val vec2 = Vec2D(2, 1)
		assertEquals(4.0, vec1 * vec2, 0.0)
	}
}
