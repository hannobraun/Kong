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



class SimpleNarrowPhase extends NarrowPhase {

	def inspectCollision(b1: Body, b2: Body) = {
		if (b1.shape.isInstanceOf[Circle] && b2.shape.isInstanceOf[Circle]) {
			val circle1 = b1.shape.asInstanceOf[Circle]
			val circle2 = b2.shape.asInstanceOf[Circle]
			val dSquared = (b1.position - b2.position).squaredLength
			if (dSquared <= circle1.radius + circle2.radius) {
				val normal1 = (b2.position - b1.position).normalize
				val normal2 = (b1.position - b2.position).normalize
				Some(Collision(b1, b2, normal1, normal2, Vec2D(0, 0)))
			}
			else {
				None
			}
		}
		else if (b1.shape.isInstanceOf[Circle] && b2.shape.isInstanceOf[LineSegment]) {
			circleLineSegment(b1, b2, b1.shape.asInstanceOf[Circle], b2.shape.asInstanceOf[LineSegment])
		}
		else if (b1.shape.isInstanceOf[LineSegment] && b2.shape.isInstanceOf[Circle]) {
			val possibleCollision = circleLineSegment(b2, b1, b2.shape.asInstanceOf[Circle],
					b1.shape.asInstanceOf[LineSegment])
			if (possibleCollision.isDefined) {
				val collision = possibleCollision.getOrElse { throw new AssertionError }
				Some(Collision(b1, b2, collision.normal2, collision.normal1, Vec2D(0, 0)))
			}
			else {
				possibleCollision
			}
		}
		else {
			None
		}
	}



	private[this] def circleLineSegment(b1: Body, b2: Body, circle: Circle, lineSegment: LineSegment):
			Option[Collision] = {
		val circle = b1.shape.asInstanceOf[Circle]
		val lineSegment = b2.shape.asInstanceOf[LineSegment]

		val p = b2.position + lineSegment.p1
		val d = (lineSegment.p2 - lineSegment.p1).normalize
		val tmax = (lineSegment.p2 - lineSegment.p1).length

		val m = p - b1.position
		val b = m * d
		val c = (m * m) - circle.radius * circle.radius
		val dis = b * b - c
		if (dis >= 0) {
			val t1 = -b + Math.sqrt(dis)
			val t2 = -b - Math.sqrt(dis)
			if (t2 >= 0 && t1 <= tmax) {
				val cp = b1.position
				val v = lineSegment.p2 - lineSegment.p1
				val t = -(((p.x - cp.x) * v.x) + ((p.y - cp.y) * v.y)) / ((d.x * v.x) + (d.y * v.y))
				val normal1 = (p + (d * t)).normalize
				val normal2 = -normal1
					Some(Collision(b1, b2, normal1, normal2, Vec2D(0, 0)))
			}
			else {
				None
			}
		}
		else {
			None
		}
	}
}
