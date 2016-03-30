var second = 0;
var intervalid = 0;
var rex = /^1[3-8]+\d{9}$/;
var authkey = '';
var sms = function() {
	if (second > 0) {
		return;
	}
	second = 60;
	var p = $("#myModal input[name='phone']").val();
	if (p && p != '' && p != null && rex.test(p)) {
		intervalid = self.setInterval("clock()", 1000);
		$.ajax({
			type : "post",
			//async : false,
			url : "sendSmsAdmin.do?phone=" + p, //发送请求地址,
			success : function(r) {
				if (r.flag){
					authkey = r.authkey;
				}else{
					second = 0;
					window.clearInterval(intervalid);
					$('#tips').html(r.msg);
				}
			}
		});
	} else {
		second = 0;
		alert('请输入正确的手机号');
	}
};

var clock = function() {
	second--;
	$('#tips').html('<font color="red">' + second + '</font>秒后可重新获取');
	if (second <= 0) {
		window.clearInterval(intervalid);
		$('#tips').html('');
	}
};

var findpass = function(){
	var validcode = $("#myModal input[name='validcode']").val();
	if (validcode && validcode.length != 6){
		alert('请填写正确的验证码');
		return;
	}
	$('#tips1').html('');
	if (authkey && authkey != ''){
		$.ajax({
			type : "post",
			//async : false,
			url : "checkValidcodeAdmin.do?authkey=" + authkey + '&validcode=' + validcode, //发送请求地址,
			success : function(r) {
				if (r.flag){
					$('#myModal').modal('hide');
					window.location.href = 'resetpassAdmin.do?authkey=' + authkey;
				}else{
					$('#tips1').html(r.msg);
				}
			}
		});
	}
};
