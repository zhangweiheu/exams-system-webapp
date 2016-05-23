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
    $("#pageArea").append("<li class='page-link next' style='margin-left:30px;margin-top: 3px;font-size: 15px;'>共 " + _totalCnt + " 条</li>");
}

function buildTable(page, pageSize) {
    var pageSize = Math.floor(window.innerHeight / 45) - 6;
    if ($('#page').val() == "") {
        page = 1;
    }
    $('#page').val(page);
    $.ajax({
        method: "GET",
        url: "/api/system/paper/list",
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
                            tbody += "<tr style='border-left: 1px solid #C1DAD7'>";
                            tbody += "<td width='50px'>"+ elem.id + "</td>";
                            tbody += "<td width='50px'>"+ elem.userId + "</td>";
                            tbody += "<td width='50px'>"+ elem.mongoPaperId + "</td>";
                            if (elem.exam) {
                                tbody += "<td width='60px'>考试卷</td>";
                            } else {
                                tbody += "<td width='60px'>练习卷</td>";
                            }
                            tbody += "<td width='60px'>"+ elem.title + "</td>";
                            if ("SINGLE_SELECTION" === elem.paperType) {
                                tbody += "<td>单选</td>";
                            } else if ("MULTI_SELECTION" === elem.paperType) {
                                tbody += "<td>多选</td>";
                            } else if ("PROGRAMMING_QUESTION" === elem.paperType) {
                                tbody += "<td>编程</td>";
                            } else if ("SINGLE_AND_MULTI" === elem.paperType) {
                                tbody += "<td>单选、多选</td>";
                            } else if ("SINGLE_AND_PROGRAMMING" === elem.paperType) {
                                tbody += "<td>单选、编程</td>";
                            } else if ("MULTI_AND_PROGRAMMING" === elem.paperType) {
                                tbody += "<td>多选、编程</td>";
                            } else if ("SINGLE_AND_MULTI_PROGRAMMING" === elem.paperType) {
                                tbody += "<td>单选、多选、编程</td>";
                            } else {
                                tbody += "<td></td>";
                            }
                            tbody += "<td width='50px'>" + elem.difficulty + "</td>";
                            tbody += "<td width='50px'>" + elem.totalPoints + "</td>";
                            tbody += "<td width='50px'>" + elem.score + "</td>";
                            if("NORMAL" === elem.status){
                                tbody += "<td width='95px'>正常</td>";
                            }else if("WRONG" === elem.status){
                                tbody += "<td width='95px'>有误</td>";
                            }else if("CLOSE" === elem.status){
                                tbody += "<td width='95px'>已关闭</td>";
                            }else if("DELETE" === elem.status){
                                tbody += "<td width='95px'>已删除</td>";
                            }else{
                                tbody += "<td width='95px'></td>";
                            }
                            tbody += "<td>" + elem.tagList + "</td>";
                            tbody += "<td width='50px'>" + elem.totalRight + "</td>";
                            tbody += "<td>" + elem.properties.createTime + "</td>";
                            tbody += "<td width='50px'><a btn-type=\"edit\" pid=\"" + elem.id + "\" href=\"#\">切换状态</a></td>";
                            tbody += "<td width='50px'><a  onclick=\"deleteRecord('" + elem.id + "')\"   btn-type=\"delete\" pid=\"" + elem.id + "\" href=\"#\">删除</a></td>";
                            tbody += "</tr>";
                        } else {
                            //超出部分
                            tbody += "<tr></tr>";
                        }
                    }
                    $("#system-paper-tbody").html(tbody);
                    buildPager(data.data.totalCount, data.data.page, data.data.pageSize);
                }
            } else {
                layer.alert('加载失败', {icon: 8});
            }
        }
    });
}

function edit_tmpl(pid) {
    $.ajax({
        method: "PUT",
        url: "/api/system/paper/0/" + pid,
        async: true,
        success: function (data) {
            if (data.code == 0) {
                layer.alert(data.data, {
                    icon: 1, offset: '150px', end: function () {
                        location.reload(true);
                    }
                });
            } else {
                layer.alert(data.msg, {icon: 11, offset: '150px'})
            }
        }
    });

}

$(function () {
    var page = $("#page").val();
    var pageSize = $("#pageSize").val();

    buildTable(page, pageSize);

    $(document).delegate("a[btn-type='edit']", "click", function () {
        var pid = $(this)[0].getAttribute("pid");
        edit_tmpl(pid);
    });

    $('#tmpl-select').on('change', function () {
        var page = 1;
        var pageSize = $("#pageSize").val();

        buildTable(page, pageSize);
    });
});
function deleteRecord(id) {
    layer.confirm('确认删除？', {
        icon: 4, offset: '150px', yes: function () {
            remove(id);
        }
    });
}
function remove(id) {
    $.ajax({
        method: "DELETE",
        url: "/api/system/paper/" + id,
        async: true,
        success: function (data) {
            if (data.code == 0) {
                layer.alert('删除成功', {
                    icon: 1, offset: '150px', end: function () {
                        location.reload(true);
                    }
                });
            } else {
                layer.alert(data.msg, {icon: 11, offset: '150px'})
            }
        }
    });
}
$('#switch').on('switch-change', function (e, data) {
    var $el = $(data.el)
        , value = data.value;
    console.log(e, $el, value);
});
