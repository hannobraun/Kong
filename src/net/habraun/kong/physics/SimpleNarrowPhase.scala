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

	def inspectCollision(delta: Double, b1: Body, b2: Body) = {
		if (b1.shape.isInstanceOf[Circle] && b2.shape.isInstanceOf[Circle]) {
			// This algorithms does continious collision detection between two moving circles. I got this
			// from "Real-Time Collision Detection" by Christer Ericson, page 223/224.

			// The two (possibly) colliding circles.
			val circle1 = b1.shape.asInstanceOf[Circle]
			val circle2 = b2.shape.asInstanceOf[Circle]

			val s = b2.position - b1.position // vector between sphere centers
			val v = (b2.velocity - b1.velocity) * delta // relative motion between the circles
			val r = circle1.radius + circle2.radius // the sum of both radii

			// The time of impact is given by the smaller solution of the quadratic equation
			// at^2 + 2bt + c = 0.
			val a = v * v //a, b and c from the equation in the comment above
			val b = v * s
			val c = (s * s) - (r * r)
			val d = (b * b) - (a * c) // the discriminant of the solution

			// Check for several corner cases. If none of these occurs, we can compute t after the general
			// formula.
			if (c < 0.0) {
				// Spheres are initially overlapping.
				val normal1 = (b2.position - b1.position).normalize
				val normal2 = (b1.position - b2.position).normalize
				val point = Vec2D(0, 0) // This doesn't really make sense. The point should be the real point
				                        // of impact and t should be negative.
				Some(Collision(0.0, Contact(b1, b2, normal1, normal2, point)))
			}
			else if (a == 0) {
				// Spheres are not moving relative to each other.
				None
			}
			else if (b >= 0.0) {
				// Spheres are not moving towards each other.
				None
			}
			else if (d < 0.0) {
				// Discriminant is negative, no real solution.
				None
			}
			else {
				// None of the edge cases has occured, so we need to compute the time of contact.
				val t = (-b - Math.sqrt(d)) / a
				if (t <= 1.0) {
					val normal1 = (b2.position - b1.position).normalize
					val normal2 = (b1.position - b2.position).normalize
					val point = b1.position + (b1.velocity * delta * t) + (normal1 * circle1.radius)
					Some(Collision(t, Contact(b1, b2, normal1, normal2, point)))
				}
				else {
					None
				}
			}
		}
		else if (b1.shape.isInstanceOf[Circle] && b2.shape.isInstanceOf[LineSegment]) {
			circleLineSegment(b1, b2, b1.shape.asInstanceOf[Circle], b2.shape.asInstanceOf[LineSegment])
		}
		else if (b1.shape.isInstanceOf[LineSegment] && b2.shape.isInstanceOf[Circle]) {
			val possibleCollision = circleLineSegment(b2, b1, b2.shape.asInstanceOf[Circle],
					b1.shape.asInstanceOf[LineSegment])
			if (possibleCollision.isDefined) {
				val contact = possibleCollision.getOrElse({ throw new AssertionError }).contact
				Some(Collision(1.0, Contact(b1, b2, contact.normal2, contact.normal1, Vec2D(0, 0))))
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
					Some(Collision(1.0, Contact(b1, b2, normal1, normal2, Vec2D(0, 0))))
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
