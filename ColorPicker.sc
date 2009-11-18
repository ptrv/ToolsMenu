ColorPicker {

	*new {
		var w,r,g,b,update,color;
		w = Window.new("Color Picker",Rect(100,100,230,68),false);
		r = EZSlider(w, Rect(2,0,150,20),"R",labelWidth:20)
		.action_({ |v| update.(); });
		g = EZSlider(w, Rect(2,22,150,20),"G",labelWidth:20)
		.action_({ |v| update.(); });

		b = EZSlider(w, Rect(2,44,150,20),"B",labelWidth:20)
		.action_({ |v| update.(); });
		
		[r,g,b].do({|item| item.sliderView.canFocus_(false) });

		color = SCUserView(w,Rect(160,0,64,64))
		.background_(Color.black)
		.enabled_(true)
		.mouseDownAction_({ color.background.postln;})
		.beginDragAction_({color.background})
		.canFocus_(false);

		
		update = { color.background_(Color(r.value,g.value,b.value)) }; 

		w.front;

	}
}
	 