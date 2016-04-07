/**
 * Created by zhang on 2016/3/8.
 */
$("#password").on("change",function(){
    $("#password").val($.md5($("#password").val()));
});

$("#save-btn").on("click",function(){
    $.ajax({
        method: "PUT",
        url: "/api/user/",
        data: $("#form").serialize(),
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                layer.alert('修改成功', {
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
