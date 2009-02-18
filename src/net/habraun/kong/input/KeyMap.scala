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



/**
 * Used to define player-key mappings. A fully configured instance of this class is needed for KeyHandler to
 * function.
 *
 * This class is supposed to be used like this:
 * // Define your players.
 * object RedPlayer extends Player
 * object BluePlayer extends Player
 *
 * // Define the keys. These are logical keys like "steer left", "steer right" or "fire", not the actual keys
 * // on the keyboard.
 * object LeftKey extends Key
 * object RightKey extends Key
 * object FireKey extends Key
 *
 * // Now that we know our keys and our players we can define the mappings between players, keys and physical
 * // keys.
 * val keyMap = (new KeyMap)
 *         .addMapping(RedPlayer, LeftKey, KeyEvent.VK_A) // the 'a' key steers red player to the left
 *         .addMapping(RedPlayer, RightKey, KeyEvent.VK_D) // the 'd' key steers the red player to the right
 *         .addMapping(RedPlayer, FireKey, KeyEvent.VK_W) // red player fires with 'w' key
 *         .addMapping(BluePlayer, LeftKey, KeyEvent.VK_LEFT) // define left key for blue player
 *         .addMapping(BluePlayer, RightKey, KeyEvent.VK_RIGHT) // ...
 *         .addMapping(BluePlayer, FireKey, KeyEvent.VK_UP)
 *
 * // Initialize the key handler.
 * val keyHandler = new KeyHandler(keyMap)
 *
 * The KeyEvent class mentioned in the example is java.awt.event.KeyEvent from the Java standard library.
 */

case class KeyMap(mappings: Map[Player, Map[Key, Int]]) {

	def this() {
		this(new HashMap[Player, Map[Key, Int]])
	}



	/**
	 * Returns a copy of this key map with the given mapping added.
	 * Attention: Since KeyMap is immutable this will return a new KeyMap, it won't change the one you're
	 * calling this method on.
	 * Please check the description of this class for usage examples.
	 */

	def addMapping(player: Player, key: Key, keyCode: Int): KeyMap = {
		val keysForPlayer = if (mappings.contains(player)) mappings(player) else new HashMap[Key, Int]
		KeyMap(mappings.update(player, keysForPlayer + (key -> keyCode)))
	}
}
