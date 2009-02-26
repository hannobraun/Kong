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



class LineSegmentTest {

	@Test
	def createLineSegmentCheckPoint1 {
		val p1 = Vec2D(0, 0)
		val segment = new LineSegment(p1, Vec2D(10, 10))
		assertEquals(p1, segment.p1)
	}



	@Test
	def createLineSegmentCheckPoint2 {
		val p2 = Vec2D(10, 10)
		val segment = new LineSegment(Vec2D(0, 0), p2)
		assertEquals(p2, segment.p2)
	}



	@Test
	def createLineSegmentVerifyIsShape {
		val segment = new LineSegment(Vec2D(0, 0), Vec2D(10, 10))
		assertTrue(segment.isInstanceOf[Shape])
	}
}
