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



import com.hannobraun.kong.util.PiccoUtil.updateSG



class Renderer extends Function1[ List[ EntityView ], Unit ] {

	def apply( views: List[ EntityView ] ) {
		// Display game state
		updateSG( () => {
			for ( view <- views ) {
				view.update
			}
		} )
	}
}
