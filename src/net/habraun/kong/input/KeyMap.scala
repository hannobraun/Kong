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



package net.habraun.kong.input



import scala.collection.immutable._



abstract class Player
abstract class Key



case class KeyMap(mappings: Map[Player, Map[Key, Int]]) {

	def this() {
		this(new HashMap[Player, Map[Key, Int]])
	}



	def addMapping(player: Player, key: Key, keyCode: Int): KeyMap = {
		val keysForPlayer = if (mappings.contains(player)) mappings(player) else new HashMap[Key, Int]
		KeyMap(mappings.update(player, keysForPlayer + (key -> keyCode)))
	}
}
