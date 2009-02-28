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
 * The broad phase is the first of two phases of collision detection.
 * It is responsible for doing a rough and cheap test, determining what bodies _could_ collide, thus reducing
 * the number of body pairs the second phase (narrow phase) has to test.
 */

trait BroadPhase {

	/**
	 * Determines which of the given bodies could potentially collide.
	 * Returns a list of body pairs that should be further tested by the narrow phase. The list of body pairs
	 * must not contain duplicates.
	 */

	def detectPossibleCollisions(bodies: List[Body]): List[(Body, Body)]
}
