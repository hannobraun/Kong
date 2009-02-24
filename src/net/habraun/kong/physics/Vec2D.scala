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



/**
 * A 2D vector.
 * Important: Vec2D is immutable. This means, all operations that modify the vector return a new vector,
 * leaving the old one unchanged.
 */

case class Vec2D(x: Double, y: Double) {

	/**
	 * Adds another vector.
	 */

	def + (vector: Vec2D) = Vec2D(x + vector.x, y + vector.y)



	/**
	 * Multiplies the vector with a scalar.
	 */

	def * (scalar: Double) = Vec2D(x * scalar, y * scalar)



	/**
	 * Computes the dot product of this vector and another vector.
	 */

	def * (vector: Vec2D) = (x * vector.x) + (y * vector.y)
}
