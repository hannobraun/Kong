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



package net.habraun.kong.game



import net.habraun.sd.collision.shape.LineSegment
import net.habraun.sd.core.Body
import net.habraun.sd.math.Vector2



class Border( config: Configuration, thePosition: Vector2 ) extends Body with LineSegment {

	position = thePosition
	mass = Double.PositiveInfinity
	override val direction = Vector2( config.screenSizeX, 0 )
}
