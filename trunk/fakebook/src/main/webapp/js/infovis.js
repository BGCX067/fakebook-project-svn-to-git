var Infovis = {

	initLayout: function() {
		var size = Window.getSize();
		var header = $('header'), left = $('left'), infovisContainer = $('infovis');
		var headerOffset = header.getSize().y, leftOffset = left.getSize().x;

		var newStyles = {
			'height': Math.floor((size.y - headerOffset) / 1),
			'width' : Math.floor((size.x - leftOffset) / 1)
		};

		infovisContainer.setProperties(newStyles);
		infovisContainer.setStyles(newStyles);
		infovisContainer.setStyles({
			'position':'absolute',
			'top': headerOffset + 'px',
			'left': leftOffset + 'px'
		});
		left.setStyle('height', newStyles.height);
	}
};

var Log = {
	elem: false,
	write: function(text) {
		if(!this.elem) this.elem = $('log');
		this.elem.set('html', text);
	}
};

jQuery.ajaxSetup({'beforeSend': function(xhr){
    if (xhr.overrideMimeType)
        xhr.overrideMimeType("text/plain");
    }
});

jQuery.fn.scale = function(scale) {
	return this.each(function() {
		$j(this).width($j(this).attr("origWidth") * scale);
		$j(this).height($j(this).attr("origHeight") * scale);
	});
};
