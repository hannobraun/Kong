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



import java.awt.Color

import edu.umd.cs.piccolo.nodes.PText



class Score(centerX: Int, centerY: Int) {

	val node = new PText(":")
	private val scoreNode1 = new PText("0")
	private val scoreNode2 = new PText("0")
	node.setConstrainWidthToTextWidth(true)
	node.setConstrainHeightToTextHeight(true)
	scoreNode1.setConstrainWidthToTextWidth(true)
	scoreNode1.setConstrainHeightToTextHeight(true)
	scoreNode2.setConstrainWidthToTextWidth(true)
	scoreNode2.setConstrainHeightToTextHeight(true)
	node.addChild(scoreNode1)
	node.addChild(scoreNode2)
	node.setTextPaint(Score.paint)
	scoreNode1.setTextPaint(Score.paint)
	scoreNode2.setTextPaint(Score.paint)
	node.setTransparency(0.2f)
	node.setScale(15)
	node.setOffset(centerX - (node.getWidth * node.getScale / 2),
			centerY - (node.getHeight * node.getScale / 2))
	scoreNode1.setOffset(-scoreNode1.getWidth /*- scoreBorder*/, 0)
	scoreNode2.setOffset(Score.border, 0)

	private var score1 = 0
	private var score2 = 0



	def getScore1 = score1
	def getScore2 = score2

	def increaseScore1 = score1 += 1
	def increaseScore2 = score2 += 1



	def update {
		scoreNode1.setText(score1.toString)
		scoreNode2.setText(score2.toString)

		scoreNode1.setOffset(-scoreNode1.getWidth, 0)
		scoreNode2.setOffset(Score.border, 0)
	}
}



object Score {
	val paint = Color.BLACK
	val border = 3.5
}
