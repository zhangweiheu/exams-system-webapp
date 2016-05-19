/**
 * Created by zhang on 2016/3/8.
 */
$("#password").on("change", function () {
    $("#password").val($.md5($("#password").val()));
});

$("#save-btn").on("click", function () {
    $.ajax({
        method: "PUT",
        url: "/api/user",
        data: $("#form").serialize(),
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                layer.alert('退出成功', {
                    icon: 1, offset: '150px', end: function () {
                        location.reload(true);
                    }
                });
            } else {
                layer.alert(data.msg, {icon: 11})
            }
        }
    });
});
$("#logout").on("click", function () {
    $.ajax({
        method: "GET",
        url: "/api/logout",
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                layer.msg('退出成功', {shade: 0.8});
                window.setTimeout("location='/login'", 100);
            } else {
                layer.alert(data.msg, {icon: 11})
            }
        }
    });
});
