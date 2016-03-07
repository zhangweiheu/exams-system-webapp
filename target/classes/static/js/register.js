/**
 * Created by zhang on 2016/2/21.
 */
function md5password() {
    if ($("#username").val().textTrim === "") {
        disable_btn("用户名不能为空");
        return false;
    }
    if ($("#password").val().textTrim === "" || !$("#password").val().isEqual($("#password2").val())) {
        disable_btn("两次密码不一致");
        return false;
    }

    $("#password").val($.md5($("#password").val()));
    return true;
}
function disable_btn(s) {
    layer.alert(s);
    $("#submit_btn").attr("disabled", 'disabled');
}
function checkpassword(){
    if ($("#password").val().textTrim === "" || !$("#password").val().isEqual($("#password2").val())) {
        disable_btn("两次密码不一致");
        return false;
    }
}
