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



abstract case class Key
case object WKey extends Key
case object SKey extends Key
case object UpKey extends Key
case object DownKey extends Key
case object NoKey extends Key



object Main {

	def main(args: Array[String]) {
		Console.println("Kong is temporarily out of order.")
	}
		
}
