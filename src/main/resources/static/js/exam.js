/**
 * Created by zhang on 2016/3/17.
 */
$("#show").hide();

$(function () {
    $.ajax({
        method: "POST",
        url: "/api/system/exam/generate/" + $("#uid").val(),
        data: d,
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                var d = data.data;
                $("#uid").val(d.uid);
                $("#pid").val(d.pid);
                generatePaperView(d.questions);
            } else {
                layer.alert(data.msg, {icon: 11})
            }
        }
    });
});


/**试题类型选择页*/
$("#submit-btn").on('click', function () {
    var d = gatherData();
    if (!checkInputData(d))return;
    $.ajax({
        method: "POST",
        url: "/api/system/exam/generate/" + $("#uid").val(),
        data: d,
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                var d = data.data;
                $("#uid").val(d.uid);
                $("#pid").val(d.pid);
                generatePaperView(d.questions);
            } else {
                layer.alert(data.msg, {icon: 11})
            }
        }
    });
});

function gatherData() {
    /**试题类型*/
    var questionTagList = new Array();
    $('input[name="questionTagList"]:checked').each(function () {
        questionTagList.push($(this).val());
    });
    var questionTagList = questionTagList.join();

    /**试卷类型*/
    var paperType = $('input[name = "paperType"]:checked').val();

    var d = {
        "questionTagList": questionTagList,
        "paperType": paperType
    }
    return d;
}

function checkInputData(data) {
    for (key in data) {
        if (!data[key]) {
            layer.alert(key + '没选择！');
            return false;
        }
    }
    return true;
}

/**全选/全不选*/
$(function () {
    $("#checkAll").click(function () {
        $('input[name="subBox"]').attr("checked", this.checked);
    });
    var $subBox = $("input[name='questionTagList']");
    $subBox.click(function () {
        $("#checkAll").attr("checked", $subBox.length == $("input[name='questionTagList']:checked").length ? true : false);
    });
});

/**试题显示页*/

function generatePaperView(questions) {

    if (questions === "" || questions === undefined || questions.length < 1) {
        return;
    }

    $("#select").hide();
    $("#show").show();

    var s = 0;
    var m = 0;
    var p = 0;
    var single = "<table><thead>单项选择</thead><tbody>";
    var multi = "<table><thead>多项选择</thead><tbody>";
    var program = "<table><thead>编程题</thead><tbody>";

    for (var i = 0; i < questions.length; i++) {

        var q = questions[i];
        var options = JSON.parse(q.options);
        var title = q.title;
        var id = q.id;

        if (q.questionType == "SINGLE_SELECTION") {
            s++;
            single = single + "<br/><tr>" + title + "</tr><br/>"
            single += "<tr>";
            single = single + "<input id=\"" + s + "s\" value=\"" + id + "\" hidden/>";
            single = single + "<input name=\"" + s + "s\" type=\"radio\" value=\"A\">" + options.A;
            single = single + "<input name=\"" + s + "s\" type=\"radio\" value=\"B\">" + options.B;
            single = single + "<input name=\"" + s + "s\" type=\"radio\" value=\"C\">" + options.C;
            single = single + "<input name=\"" + s + "s\" type=\"radio\" value=\"D\">" + options.D;
            single += "</tr>";
        }

        if (q.questionType == "MULTI_SELECTION") {
            m++;
            multi = multi + "<br/><tr>" + title + "</tr><br/>"
            multi += "<tr>";
            multi = multi + "<input id=\"" + m + "m\" value=\"" + id + "\" hidden/>";
            multi = multi + "<input name=\"" + m + "m\" type=\"checkbox\" value=\"A\">" + options.A;
            multi = multi + "<input name=\"" + m + "m\" type=\"checkbox\" value=\"B\">" + options.B;
            multi = multi + "<input name=\"" + m + "m\" type=\"checkbox\" value=\"C\">" + options.C;
            multi = multi + "<input name=\"" + m + "m\" type=\"checkbox\" value=\"D\">" + options.D;
            multi += "</tr>";
        }

        if (q.questionType == "PROGRAMMING_QUESTION") {
            p++;
            program = program + "<br/><tr>" + title + "</tr><br/>"
            program += "<tr>";
            program = program + "<input id=\"" + p + "p\" value=\"" + id + "\" hidden/>";
            program = program + "<input style=\"width:800px;height:600px\" name=\"" + p + "p\" " + "type=\"text\">";
            program += "</tr>";
        }
    }

    if (single.length > 33) {
        single = single +
            "</tbody>" +
            "<div><input id=\"single-save-btn\" type=\"submit\" " + "value=\"提交\"/></div>" +
            "</table>";
        $("#s").val(s);
        $("#single").html(single);
    }
    if (multi.length > 33) {
        multi = multi +
            "</tbody>" +
            "<div><input " + "id=\"multi-save-btn\" " + "type=\"submit\" " + "value=\"提交\"/></div>" +
            "</table>";
        $("#m").val(m);
        $("#multi").html(multi);
    }
    if (program.length > 33) {
        program = program +
            "</tbody>" +
            "<div><input " + "id=\"program-save-btn\" " + "type=\"submit\" " + "value=\"提交\"/></div>" +
            "</table>";
        $("#p").val(p);
        $("#program").html(program);
    }
}


/**保存btn监听*/
$("#single-save-btn").on('click', function () {
    var d = gatherDataSingle();
    $.ajax({
        method: "PUT",
        url: "/api/system/exam/do/" + $("#uid").val(),
        data: d,
        success: function (data) {
            if (data.code == 0) {
                layer.alert('提交成功', {
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
});

$("#multi-save-btn").on('click', function () {
    var d = gatherDataMulti();
    $.ajax({
        method: "PUT",
        url: "/api/system/exam/do/" + $("#uid").val(),
        data: d,
        success: function (data) {
            if (data.code == 0) {
                layer.alert('提交成功', {
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
});

$("#program-save-btn").on('click', function () {
    var d = gatherDataProgram();
    $.ajax({
        method: "PUT",
        url: "/api/system/exam/programing/",
        data: d,
        success: function (data) {
            if (data.code == 0) {
                layer.alert('提交成功', {
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
});
$("#exam-over-btn").on('click', function () {
    $.ajax({
        method: "PUT",
        url: "/api/system/exam/close/" + $("#uid").val() + "/" + $("#pid").val(),
        success: function (data) {
            if (data.code == 0) {
                layer.alert('考试结束', {
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
});


/**收集数据*/
function gatherDataSingle() {
    var s = $("#s").val();
    if (0 == s) {
        return undefined;
    }
    var d = {};

    for (var i = 1; i <= s; i++) {
        var ids = $("#" + i + "s").val();
        var valuecheck = $('input[name = ' + i + 's]:checked').val();
        var c = ids + ":" + valuecheck;
        d.add(c);
    }
    return d;
}
function gatherDataMulti() {
    var m = $("#m").val();
    if (0 == m) {
        return undefined;
    }
    var d = {};

    for (var i = 1; i <= m; i++) {
        var ids = $("#" + i + "m").val();
        var tag_array = new Array();
        var inputName = "input[name = " + i + "m]:checked";
        $(inputName.valueOf()).each(function () {
            tag_array.push($(this).val());
        });
        var answers = tag_array.join();
        var c = ids + ":" + answers;
        d.add(c);
    }
    return d;
}
function gatherDataProgram() {
    var pid = $("#pid").val();
    var qid = $("#1p").val();
    var text = $("input[name='1p']").val();
    var d = {
        "pid":pid,
        "qid":qid,
        "text":text
    }
    return d;
}
