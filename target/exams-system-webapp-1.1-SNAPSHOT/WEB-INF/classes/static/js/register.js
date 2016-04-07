/**
 * Created by zhang on 2016/2/21.
 */
function md5password() {
    var password = $("#password1").val();
    if ($("#username").val().textTrim === "") {
        disable_btn("用户名不能为空");
        return false;
    }
    if (password.textTrim === "" || !password.isEqual($("#password2").val())) {
        disable_btn("两次密码不一致");
        return false;
    }
    $("#password").val($.md5(password));
    return true;
}

$("#password2").blur(function (){
    var password = $("#password");
    var password2 = $("#password2");
    if (password.val().trim() === "" || password2.val() === "") {
        disable_btn("密码不能为空");
    }else if (password.val() != password2.val()) {
        disable_btn("两次密码不一致");
    }
});

function disable_btn(s) {
    $("#submit_btn").attr("disabled", 'disabled');
    layer.alert(s);
}
