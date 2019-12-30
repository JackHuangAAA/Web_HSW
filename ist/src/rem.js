window.onload = function(){
		var width = document.body.clientWidth;
		var html = document.documentElement;
		var rem;
		if(16*(width/1080)<12){
			rem = 12 + 'px';
		}else{
			rem = 16*(width/1080) + 'px';
		}
		html.style.fontSize = rem;
}


window.onresize = function(){
	var width = document.body.clientWidth;
	var html = document.documentElement;
	var rem;
	if(16*(width/1080)<12){
		rem = 12 + 'px';
	}else{
		rem = 16*(width/1080) + 'px';
	}
	html.style.fontSize = rem;
}