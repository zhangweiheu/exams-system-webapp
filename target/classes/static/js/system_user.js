/**
 * Created by zhang on 2016/3/7.
 */

function buildPager(_totalCnt, _currentPage, pageSize) {
    $("#pageArea").pagination({
        items: _totalCnt,
        itemsOnPage: pageSize,
        currentPage: _currentPage,
        cssStyle: 'compact-theme',
        prevText: '上一页',
        nextText: '下一页',
        onPageClick: function (page, event) {
            buildTable(page, pageSize);
        }
    });
    $("#pageArea").append("<li class='page-link next' style='margin-left:30px;margin-top: 3px;font-size: 15px;'>共 "+_totalCnt+" 条</li>");
}

function buildTable(page, pageSize) {
    var pageSize = Math.floor(window.innerHeight / 37) - 6;
    if($('#page').val() == ""){
        page = 1;
    }
    $('#page').val(page);
    $.ajax({
        method: "GET",
        url: "/api/user/list",
        async: true,
        data: {"page": page, "pageSize": pageSize},
        dataType: "json",
        success: function (data) {
            var code = data.code;
            if (code == 0) {
                var curPageSize = data.data.data.length;
                if (curPageSize > 0) {
                    var tbody = "";
                    for (var i = 0; i < pageSize; i++) {
                        if (i < curPageSize) {
                            var elem = data.data.data[i];
                            tbody += "<tr>";
                            tbody += "<td class='fixWid'>" + elem.id + "</td>";
                            tbody += "<td class='fixWid'>" + elem.username + "</td>";
                            tbody += "<td>" + elem.email + "</td>";
                            tbody += "<td>" + elem.properties.createTime + "</td>";
                            if (elem.isAdmin) {
                                tbody += "<td class='fixWid'>管理员</td>";
                            } else {
                                tbody += "<td class='fixWid'>普通用户</td>";
                            }
                            tbody += "<td class='fixWid'><a btn-type=\"edit\" uid=\"" + elem.id + "\" href=\"#\">编辑</a></td>";
                            tbody += "<td class='fixWid'><a  onclick=\"deleteRecord('"+ elem.id +"')\"   btn-type=\"delete\" uid=\"" + elem.id + "\" href=\"#\">删除</a></td>";
                            tbody += "</tr>";
                        } else {
                            //超出部分
                            tbody += "<tr></tr>";
                            //var elem = {id: "", name: "", email: "",  createAt: "", isAdmin: "",isDeleted: ""};
                        }

                    }
                    $("#system-user-tbody").html(tbody)
                    ;         buildPager(data.data.totalCount, data.data.page, data.data.pageSize);
                }
            } else {
                layer.alert('加载失败', {icon: 8});
            }
        }
    });
}

function edit_tmpl(uid) {
    layer.open({
        type: 2,
        title: '编辑用户',
        shadeClose: true,
        shade: 0.5,
        content: '/user/edit/' + uid,
        area: ['70%', '80%'],
        end: function () {
            buildTable($('#page').val(), $('#pageSize').val());
        }
    });
}

function add_tmpl() {
    buildCommonLayer('新增用户','/user/edit/0');
}

$(function () {
    var page = $("#page").val();
    var pageSize = $("#pageSize").val();

    buildTable(page, pageSize);

    $(document).delegate("a[btn-type='edit']", "click", function () {
        var uid = $(this)[0].getAttribute("uid");
        edit_tmpl(uid);
    });

    $(document).delegate("a[btn-type='add']", "click", function () {
        add_tmpl();
    });

    $('#tmpl-select').on('change', function () {
        var page = 1;
        var pageSize = $("#pageSize").val();

        buildTable(page, pageSize);
    });
});
function deleteRecord(id){
    layer.confirm('确认删除？', {
        icon: 4, offset:'150px', yes: function () {
            remove(id);
        }
    });
}
function remove(id) {
    $.ajax({
        method: "DELETE",
        url: "/api/user/" + id,
        async: true,
        success: function (data) {
            if (data.code == 0) {
                layer.alert('删除成功', {
                    icon: 1, offset:'150px', end: function () {
                        location.reload(true);
                    }
                });
            } else {
                layer.alert(data.msg, {icon: 11, offset: '150px'})
            }
        }
    });
}

function gatherData() {
    var id = $("#id").val();
    var name = $("#name").val().trim();
    var email = $("#email").val().trim();
    var password = !$("#old_password").length || $("#password").val().trim() != $("#old_password").val().trim() ? $.md5($("#password").val().trim()) : $("#password").val().trim();
    var isAdmin = $('input[name=isAdmin]:checked').val();
    if (parseInt(isAdmin) == 1) {
        isAdmin = true;
    }else{
        isAdmin = false;
    }

    var d = {
        "id":id,
        "name": name,
        "email": email,
        "password": password,
        "isAdmin": isAdmin
    };

    return d;
}

$("#save-btn").on('click', function () {
    var d = gatherData();
    if(!checkInputData(d))return;
    if (d.id != undefined && d.id != "") {
        //alert(d.isAdmin);
        $.ajax({
            method: "PUT",
            url: "/api/user/",
            async: true,
            data: d,
            success: function (data) {
                if (data.code == 0) {
                    layer.alert('更新成功', {
                        icon: 9, offset: '150px', end: function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        }
                    });
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
                            parent.layer.close(index);
                        }
                    });
                } else {
                    layer.alert(data.msg, {icon: 11})
                }
            }
        });
    }
});


/** 参数校验 */
function checkInputData(data) {
    for (key in data) {
        if(key === "id" || key === "isAdmin") continue;
        if (!data[key]) {
            layer.alert(key + '没填');
            return false;
        }
    }
    return true;
}


