/**
 * Created by zhang on 2016/3/14.
 */
$("#edit_save_btn").on('click', function () {
    var d = gatherData();
    if (!checkInputData(d))return;
    if (d.id != undefined && d.id != "") {
        $.ajax({
            method: "PUT",
            url: "/api/user",
            async: true,
            data: d,
            success: function (data) {
                if (data.code == 0) {
                    layer.alert('更新成功', {
                        icon: 9, offset: '150px', end: function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            location.reload(true);
                            parent.layer.close(index);
                        }
                    });
                    dealwithTagList();
                    location.reload(true);
                } else {
                    layer.alert(data.msg, {icon: 11})
                }
            }
        });
    } else {
        $.ajax({
            method: "POST",
            url: "/api/user",
            async: true,
            data: d,
            success: function (data) {
                if (data.code == 0) {
                    layer.alert('创建成功', {
                        icon: 9, offset: '150px', end: function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            location.reload(true);
                            parent.layer.close(index);
                        }
                    });
                    location.reload(true);
                } else {
                    layer.alert(data.msg, {icon: 11})
                }
            }
        });
    }
});


function gatherData() {
    var id = $("#id").val();
    var username = $("#username").val().trim();
    var password = $("#password").val().trim();
    var phone = $("#phone").val().trim();
    var email = $("#email").val().trim();
    var wechat = $("#wechat").val().trim();
    var intro = $("#intro").val().trim();
    var type = $("#type").val().trim();
    var status = $("#status").val().trim();
    var tag_array = new Array();
    $('input[name="tag"]:checked').each(function () {
        tag_array.push($(this).val());
    });
    var tagList = tag_array.join();
    $("#tagList").val(tagList);
    var d = {
        "id": id,
        "username": username,
        "password": password,
        "email": email,
        "phone": phone,
        "wechat": wechat,
        "intro": intro,
        "status": status,
        "type": type,
        "tagList": tagList
    };
    return d;
}

/** 参数校验 */
function checkInputData(data) {
    for (key in data) {
        if (key === "id" || key === "wechat" || key === "intro" || key === "isAdmin" || key === "tagList") continue;
        if (!data[key]) {
            layer.alert(key + '没填');
            return false;
        }
    }
    return true;
}


$(function(){
    dealwithTagList();
});

function dealwithTagList() {
    var taglist = $("#tagList").val();
    if (taglist === undefined || taglist === "") {
    }
    else {
        var tag_array = taglist.split(",");
        for (var i = 0; i < tag_array.length; i++) {
            var a = tag_array[i];
            if (a == "JAVA") {
                $("#java").attr('checked', true);
            }
            if (a == "HTML") {
                $("#html").attr('checked', true);
            }
            if (a == "SYSTEM") {
                $("#system").attr('checked', true);
            }
            if (a == "NETWORK") {
                $("#network").attr('checked', true);
            }
            if (a == "CSS") {
                $("#css").attr('checked', true);
            }
            if (a == "SPRING") {
                $("#spring").attr('checked', true);
            }
            if (a == "HIBERNATE") {
                $("#hibernate").attr('checked', true);
            }
            if (a == "MYSQL") {
                $("#mysql").attr('checked', true);
            }
        }
    }
}
$("#password").on("change", function () {
    $("#password").val($.md5($("#password").val()));
});
