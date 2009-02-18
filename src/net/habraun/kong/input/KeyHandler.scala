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



import scala.collection.mutable._

import edu.umd.cs.piccolo.event._



/**
 * Handles keyboard input events from the Piccolo2D scene graph and provides a method, that allows you to
 * easily determine which keys are currently pressed. The intended usage is, to set Piccolo's keyboard focus
 * to this class and then query it every frame from the main loop.
 *
 * Example setup:
 * // Configure a KeyMap. Check KeyMap's documentation for information on how to do this.
 * val inputHandler = new InputHandler(keyMap)
 * val canvas = PCanvas
 * canvas.getRoot.getDefaultInputManager.setKeyboardFocus(inputHandler)
 *
 * By default you'll have to click into the canvas before keyboard events will be delivered to the handler.
 * If you'd like to deliver keyboard events by default right after the application has been started, you'll
 * have to do the following:
 * // Make your window visible by calling .setVisible(true)
 * canvas.requestFocusInWindow // This will give you keyboard focus right from the start.
 *
 * Example usage:
 * while (true) {
 *     // Tell the input handler to execute an action if a certain player has pressed a certain key. Use the
 *     // player and key objects you defined when configuring the KeyMap.
 *     doIfPressed(player, key, () => {
 *         // do stuff here
 *     })
 *
 *     // Also works if your stuff returns something.
 *     val result = doIfPressed(player, key, () => {
 *         // do stuff that returns a result; your result will be wrapped in an Option
 *     })
 *
 *     // Do whatever else you need to do in your main loop
 * }
 */

class KeyHandler(keyMap: KeyMap) extends PBasicInputEventHandler {

	val pressedKeys = new HashSet[Int]



	/**
	 * Don't call this. This method is called by Piccolo when a key is pressed.
	 */

	override def keyPressed(event: PInputEvent) {
		pressedKeys.addEntry(event.getKeyCode)
	}



	/**
	 * Don't call this. This method is called by Piccolo when a key is released.
	 */

	override def keyReleased(event: PInputEvent) {
		pressedKeys.removeEntry(event.getKeyCode)
	}



	/**
	 * Returns true if a given key is pressed.
	 */

	def isPressed(player: Player, key: Key): Boolean = {
		val keyCode = keyMap.mappings(player)(key)
		pressedKeys.contains(keyCode)
	}



	/**
	 * Executes an action if a specific player has pressed a specific key.
	 */

	def doIfPressed[R](player: Player, key: Key, action: () => R): Option[R] = {
		val keyCode = keyMap.mappings(player)(key)
		if (pressedKeys.contains(keyCode))
			Some(action())
		else
			None
	}
}
