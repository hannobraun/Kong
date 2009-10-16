/*
	Copyright (c) 2008, 2009 Hanno Braun <hanno@habraun.net>

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



import java.awt.event.KeyEvent

import edu.umd.cs.piccolo.PCanvas
import net.habraun.piccoinput.Key
import net.habraun.piccoinput.KeyHandler
import net.habraun.piccoinput.KeyMap
import net.habraun.piccoinput.Player



object PlayerLeft extends Player
object PlayerRight extends Player
object UpKey extends Key
object DownKey extends Key



class InputSetup {

	def createKeyHandler( canvas: PCanvas ) = {
		// Create the key mappings.
		val keyMap = ( new KeyMap )
				.addMapping( PlayerLeft, UpKey, KeyEvent.VK_W )
				.addMapping( PlayerLeft, DownKey, KeyEvent.VK_S )
				.addMapping( PlayerRight, UpKey, KeyEvent.VK_UP )
				.addMapping( PlayerRight, DownKey, KeyEvent.VK_DOWN )

		// Create the key handler.
		val keyHandler = new KeyHandler( keyMap )
		
		// Set the handler as the input handler for Piccolo key events.
		canvas.getRoot.getDefaultInputManager.setKeyboardFocus( keyHandler.handler )

		keyHandler
	}
}
