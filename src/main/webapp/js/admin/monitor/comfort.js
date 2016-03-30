$(function() {
	$.ajax({
		type : "post", 
		url : "getWeather.do", 
		success : function(data) {
			console.log(data);
			$("#city_time").html(data.city + "&nbsp;&nbsp;&nbsp;&nbsp;" + data.ptime);
			$("#w1").attr("style","background: url(../../../img/weather/" + data.day1.icon + ".png);background-size:70px 70px;");
			$("#w2").attr("style","background: url(../../../img/weather/" + data.day2.icon + ".png);background-size:50px 50px;");
			$("#w3").attr("style","background: url(../../../img/weather/" + data.day2.icon + ".png);background-size:50px 50px;");
			$("#temp1").html((data.day1.temp1 == '' ? '' : (data.day1.temp1 + " ~ ")) + data.day1.temp2 + "<sup>℃</sup>");
			if (data.day1.weather1 == data.day1.weather2){
				$("#weather1").html(data.day1.weather1);
			}else{
				$("#weather1").html((data.day1.weather1 == '' ? data.day1.weather2 : (data.day1.weather1 + "转") + data.day1.weather2));	
			}
			
			if (data.day1.winddirection1 == '无持续风向'){
				data.day1.winddirection1 = '';
			}
			if (data.day1.winddirection2 == '无持续风向'){
				data.day1.winddirection2 = '';
			}
			if (data.day2.winddirection1 == '无持续风向'){
				data.day2.winddirection1 = '';
			}
			if (data.day2.winddirection2 == '无持续风向'){
				data.day2.winddirection2 = '';
			}
			if (data.day3.winddirection1 == '无持续风向'){
				data.day3.winddirection1 = '';
			}
			if (data.day3.winddirection2 == '无持续风向'){
				data.day3.winddirection2 = '';
			}
			
			$("#wind1").html(data.day1.winddirection1 == '' ? (data.day1.winddirection2 + data.day1.windstrength2) : 
				(data.day1.winddirection1 + data.day1.windstrength1));
			
			$("#temp2").html(data.day2.temp1 + " ~ " + data.day2.temp2 + "℃");
			if (data.day2.weather1 == data.day2.weather2){
				$("#weather2").html(data.day2.weather1);
			}else{
				$("#weather2").html(data.day2.weather1 + "转" + data.day2.weather2);
			}
			$("#wind2").html(data.day2.winddirection1 + data.day2.windstrength1 + ((data.day2.winddirection1 == data.day2.winddirection2 && 
					data.day2.windstrength1 == data.day2.windstrength2) ? '' : ("转" + data.day2.winddirection2 + data.day2.windstrength2)));
			
			$("#temp3").html(data.day3.temp1 + " ~ " + data.day3.temp2 + "℃");
			if (data.day3.weather1 == data.day3.weather2){
				$("#weather3").html(data.day3.weather1);
			}else{
				$("#weather3").html(data.day3.weather1 + "转" + data.day3.weather2);
			}
			
			$("#wind3").html(data.day3.winddirection1 + data.day3.windstrength1 + ((data.day3.winddirection1 == data.day3.winddirection2 && 
					data.day3.windstrength1 == data.day3.windstrength2) ? '' : ("转" + data.day3.winddirection2 + data.day3.windstrength2)));
		}

	});
});