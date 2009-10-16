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



package net.habraun.kong.ui



import game.Ball
import game.Paddle
import util.PiccoUtil.updateSG

import java.awt.geom.AffineTransform



class Renderer extends Function3[ List[ PaddleView ], BallView, ScoreView, Unit ] {

	def apply( paddleViews: List[ PaddleView ], ballView: BallView, scoreView: ScoreView ) {
		// Display game state
		updateSG( () => {
			for ( paddleView <- paddleViews ) {
				paddleView.update
			}

			ballView.update

			scoreView.update
		} )
	}
}
