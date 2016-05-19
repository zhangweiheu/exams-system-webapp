/**
 * Created by zhang on 2016/2/21.
 */
$("#submit_btn").on("click", function () {
    if ($("#username").val() === "") {
        layer.alert("用户名不能为空");
        return;
    }
    if ($("#password").val() === "" || !($("#password").val() === $("#password2").val())) {
        layer.alert("两次密码不一致");
        return;
    }
    $("#password").val($.md5($("#password").val()));
    $("#password2").val($.md5($("#password2").val()));
    $.ajax({
        url: "/api/register",
        method: "POST",
        data: $("form").serialize(),
        dataType: "json",
        success: function (data) {
            var code = data.code;
            if (code == 0) {
                layer.msg('注册成功！正跳转到登陆页面......', {shade: 0.8});
                window.setTimeout("location='/login'", 100);
            } else {
                layer.alert(data.msg, {icon: 11});
            }
        }
    });
});

$("#password2").blur(function () {
    var password = $("#password");
    var password2 = $("#password2");
    if (password.val().trim() === "" || password2.val() === "") {
        layer.alert("密码不能为空");
    } else if (password.val() != password2.val()) {
        layer.alert("两次密码不一致");
    }
});

$("#username").blur(function () {
    var username = $("#username").val();
    if (username.textTrim === "") {
        layer.alert("用户名不能为空");
        return;
    }
    $.ajax({
        url: "/api/common/check/username",
        method: "POST",
        async: true,
        data: {"username": username},
        dataType: "json",
        success: function (data) {
            var code = data.code;
            if (code != 0) {
                layer.alert("用户名已经存在")
            }
        }
    });
});

function msg(msg){
    layer.alert(msg,{icon: 11});
}
