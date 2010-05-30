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



package com.hannobraun.kong.ui



import com.hannobraun.kong.Configuration
import com.hannobraun.kong.game.Paddle

import java.awt.Color
import java.awt.geom.AffineTransform
import java.awt.geom.Ellipse2D

import edu.umd.cs.piccolo.nodes.PPath



class PaddleView( config: Configuration, val paddle: Paddle )
		extends PPath( PaddleView.shape ) with EntityView{

	setPaint( Color.RED )
	setStroke( config.defaultStroke )



	def update {
		val x = paddle.position.x - Paddle.radius
		val y = paddle.position.y - Paddle.radius
		setTransform( AffineTransform.getTranslateInstance( x, y ) )
	}
}




object PaddleView {
	val shape = new Ellipse2D.Double( 0, 0, Paddle.radius * 2, Paddle.radius * 2 )
}
