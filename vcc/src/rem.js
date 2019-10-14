window.onload = function(){
		var width = document.body.clientWidth
		var html = document.documentElement;
		var rem;
		if(16*(width/800)<12){
			rem = 12 + 'px';
		}else{
		rem = 16*(width/800) + 'px';
		}
		html.style.fontSize = rem;
		console.log(rem)
}


window.onresize = function(){
	var width = document.body.clientWidth
	var html = document.documentElement;
	var rem;
	if(16*(width/800)<12){
		rem = 12 + 'px';
	}else{
	rem = 16*(width/800) + 'px';
	}
	html.style.fontSize = rem;
}