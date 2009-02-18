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



import java.awt.event._

import edu.umd.cs.piccolo.event._
import org.junit._
import org.junit.Assert._



class KeyHandlerTest {
	
	object TestPlayer extends Player
	object TestKey extends Key
	


	var keyMap: KeyMap = null
	var keyHandler: KeyHandler = null
	var testEvent: PInputEvent = null



	@Before
	def setup {
		keyMap = (new KeyMap).addMapping(TestPlayer, TestKey, 0)
		keyHandler = new KeyHandler(keyMap)
		testEvent = new PInputEvent(null, null) { override def getKeyCode = 0 }
	}



	@Test
	def doNothingCheckIfPressed {
		assertFalse(keyHandler.isPressed(TestPlayer, TestKey))
	}



	@Test
	def pressKeyCheckIfPressed {
		keyHandler.handler.keyPressed(testEvent)
		assertTrue(keyHandler.isPressed(TestPlayer, TestKey))
	}



	@Test
	def pressAndReleaseKeyCheckIfPressed {
		keyHandler.handler.keyPressed(testEvent)
		keyHandler.handler.keyReleased(testEvent)
		assertFalse(keyHandler.isPressed(TestPlayer, TestKey))
	}



	@Test
	def doNothingCheckDoIfPressed {
		val result = keyHandler.doIfPressed(TestPlayer, TestKey, () => 5)
		assertEquals(None, result)
	}



	@Test
	def pressKeyCheckDoIfPressed {
		keyHandler.handler.keyPressed(testEvent)
		val result = keyHandler.doIfPressed(TestPlayer, TestKey, () => 5)
		assertEquals(Some(5), result)
	}



	@Test
	def pressAndreleaseKeyCheckDoIfPressed {
		keyHandler.handler.keyPressed(testEvent)
		keyHandler.handler.keyReleased(testEvent)
		val result = keyHandler.doIfPressed(TestPlayer, TestKey, () => 5)
		assertEquals(None, result)
	}
}
