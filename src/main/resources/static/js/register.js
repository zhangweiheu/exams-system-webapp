/**
 * Created by zhang on 2016/2/21.
 */
$("#submit_btn").on("click", function () {
    if ($("#username").val() === "") {
        layer.alert("用户名不能为空");
        return false;
    }
    if ($("#password").val() === "" || !($("#password").val() === $("#password2").val())) {
        layer.alert("两次密码不一致");
        return false;
    }
    $("#password").val($.md5($("#password").val()));
    $("#password2").val($.md5($("#password2").val()));
    $.ajax({
        url: "/api/register",
        method: "POST",
        async: true,
        data: $("form").serialize(),
        dataType: "json",
        success: function (data) {
            var code = data.code;
            if (code == 0) {
                layer.alert("注册成功", {icon: 11});
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
