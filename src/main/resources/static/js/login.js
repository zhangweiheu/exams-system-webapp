/**
 * Created by zhang on 2016/2/21.
 */

$("#submit_btn").on("click", function () {

    if ($("#username").val() === "") {
        layer.alert("用户名不能为空");
        return;
    }
    if ($("#password").val() === "") {
        layer.alert("两次密码不一致");
        return;
    }
    $("#password").val($.md5($("#password").val()));
    $.ajax({
        url: "/api/login",
        method: "POST",
        async: true,
        data: $("form").serialize(),
        dataType: "json",
        success: function (data) {
            var code = data.code;
            if (code == 0) {
                window.setTimeout("location='/index'", 100);
            } else {
                layer.alert(data.msg, {icon: 11})
            }
        }
    });
});
