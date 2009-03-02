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
 * Models a collision between two bodies.
 * A collision has the following attributes:
 * * b1 and b2 are the two bodies that collide.
 * * normal1 is the collision normal for b1. This is the unit vector, that is a surface normal for b1 at the
 *   point of impact (pointing away from b1, towards b2). normal2 is the collision normal for b2 and is
 *   always the inverse of normal1.
 * * impactPoint is a position vector pointing at the point of impact. Determining the point of impact is not
 *   yet implemented and impactPoint is always Vec2D(0, 0).
 */

case class Collision(b1: Body, b2: Body, normal1: Vec2D, normal2: Vec2D, impactPoint: Vec2D) {
	if (normal1 != -normal2)
		throw new IllegalArgumentException("Both collision normals must be inverse to each other.")
	if (normal1 * normal1 != 1)
		throw new IllegalArgumentException("Normals must be unit vectors.")
}
