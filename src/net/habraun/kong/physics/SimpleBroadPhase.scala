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



class SimpleBroadPhase extends BroadPhase {

	def detectPossibleCollisions(bodies: List[Body]) = {
		def buildPairs(list: List[Body], pairs: List[(Body, Body)]): List[(Body, Body)] = {
			if (list.isEmpty)
				pairs
			else
				buildPairs(list.tail, pairs:::list.tail.map((list.head, _)))
		}

		buildPairs(bodies, Nil)
	}
}