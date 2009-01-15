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



package net.habraun.kong



import java.awt.event._
import scala.collection.mutable._

import edu.umd.cs.piccolo.event._



class InputHandler extends PBasicInputEventHandler {
	
	val pressedKeys = new HashSet[Int]
	
	
	
	override def keyPressed(event: PInputEvent) {
		pressedKeys.addEntry(event.getKeyCode)
	}
	
	
	
	override def keyReleased(event: PInputEvent) {
		pressedKeys.removeEntry(event.getKeyCode)
	}
	
	
	
	def isUpPressed(player: Int): Boolean = pressedKeys.contains(InputHandler.playerKeys(player)(0))

	def isDownPressed(player: Int): Boolean = pressedKeys.contains(InputHandler.playerKeys(player)(1))
}



object InputHandler {

	val playerKeys = new Array[Array[Int]](2, 2)

	playerKeys(0)(0) = KeyEvent.VK_W
	playerKeys(0)(1) = KeyEvent.VK_S

	playerKeys(1)(0) = KeyEvent.VK_UP
	playerKeys(1)(1) = KeyEvent.VK_DOWN
}
