/**
 * Created by zhang on 2016/4/7.
 */

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
        url: "/api/analysis/list",
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
                            var po = curPageSize - i -1;
                            var elem = data.data.data[po];
                            var mc = data.data.totalCount - (data.data.page - 1) * data.data.pageSize - curPageSize + i +1;
                            tbody += "<tr>";
                            tbody += "<td style='border-left:1px solid #C1DAD7'>" + mc + "</td>";
                            tbody += "<td>" + elem.username + "</td>";
                            tbody += "<td>" + elem.totalScore + "</td>";
                            tbody += "</tr>";
                        } else {
                            //超出部分
                            tbody += "<tr></tr>";
                        }
                    }
                    $("#system-rank-tbody").html(tbody)
                    ;
                    buildPager(data.data.totalCount, data.data.page, data.data.pageSize);
                }
            } else {
                layer.alert('加载失败', {icon: 8});
            }
        }
    });
}


$(function () {
    var page = $("#page").val();
    var pageSize = $("#pageSize").val();

    buildTable(page, pageSize);


    $('#tmpl-select').on('change', function () {
        var page = 1;
        var pageSize = $("#pageSize").val();

        buildTable(page, pageSize);
    });
});



