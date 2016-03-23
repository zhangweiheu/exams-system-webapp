/**
 * Created by zhang on 2016/3/17.
 */
$("#show").hide();
$("#exam-over-btn").hide();

$(function () {
    $.ajax({
        method: "GET",
        url: "/api/system/exam/check/" + $("#uid").val(),
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                var d = data.data;
                $("#uid").val(d.uid);
                $("#pid").val(d.pid);
                generatePaperView(d.questions);
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
$("#checkAll").on("click", function () {
    $('input[name="questionTagList"]').attr("checked", this.checked);
});

$("input[name='questionTagList']").on("click", function () {
    $("#checkAll").attr("checked", $("input[name='questionTagList']").length == $("input[name='questionTagList']:checked").length);
});


/**试题显示页*/

function generatePaperView(questions) {

    if (questions === "" || questions === undefined || questions.length < 1) {
        return;
    }

    $("#select").hide();
    $("#show").show();
    $("#exam-over-btn").show();

    var s = 0;
    var m = 0;
    var p = 0;
    var single = "<div style='color: black;font-size: 25px'>单项选择</div>";
    var multi = "<div style='color: black;font-size: 25px'>多项选择</div>";
    var program = "<div style='color: black;font-size: 25px'>编程题</div>";

    for (var i = 0; i < questions.length; i++) {

        var q = questions[i];

        var title = q.title;
        var id = q.id;

        if (q.questionType == "SINGLE_SELECTION") {
            s++;
            var options = JSON.parse(q.options);
            single = single + "<div class='font-style' style='position: relative;left: 20px;margin-top: 25px'>"+ s + "、" + title + "</div>";
            single = single + "<div style='margin:0 auto;position: relative'>";
                single = single + "<div hidden><input id='" + s + "s' value='" + id + "' hidden/></div>";
                single = single + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + s + "s' type='radio' value='A'>" + options.A + "</label></div>";
                single = single + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + s + "s' type='radio' value='B'>" + options.B + "</label></div>";
                single = single + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + s + "s' type='radio' value='C'>" + options.C + "</label></div>";
                single = single + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + s + "s' type='radio' value='D'>" + options.D + "</label></div>";
            single = single + "</div>";
        }

        if (q.questionType == "MULTI_SELECTION") {
            m++;
            var options = JSON.parse(q.options);
            multi = multi + "<div class='font-style' style='position: relative;left: 20px;margin-top: 25px'>"+ m + "、" + title + "</div>"
            multi = multi + "<div style='margin:0 auto;position: relative'>";
                multi = multi + "<div hidden><input id='" + m + "m' value='" + id + "' hidden/></div>";
                multi = multi + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + m + "m' type='checkbox' value='A'>" + options.A + "</label></div>";
                multi = multi + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + m + "m' type='checkbox' value='B'>" + options.B + "</label></div>";
                multi = multi + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + m + "m' type='checkbox' value='C'>" + options.C + "</label></div>";
                multi = multi + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + m + "m' type='checkbox' value='D'>" + options.D + "</label></div>";
            multi = multi + "</div>";
        }

        if (q.questionType == "PROGRAMMING_QUESTION") {
            p++;
            program = program + "<div class='font-style' style='position: relative;left: 20px;margin-bottom: 20px'>"+ p + "、" + title + "</div>"
            program = program + "<div style='margin:0 auto;position: relative'>";
            program = program + "<div><input id='" + p + "p' class='input-radio' value='" + id + "' hidden/></div>";
            program = program + "<div><input style='width:80%;height:450px;margin-left: 10%;margin-bottom: 3%' name='" + p + "p' type='text'></div>";
            program = program + "</div>";
        }
    }

    if (single.length > 52) {
        single = single +
            "<div align='center' style='margin-top: 60px;margin-top: -5px'><input id='single-save-btn' type='submit' class='input-btn' value='提交' onclick='singlesubmit();'></div>";
        $("#s").val(s);
        $("#single").html(single);
    }
    if (multi.length > 52) {
        multi = multi +
            "<div align='center' style='margin-top: 60px;margin-top: -5px'><input id='multi-save-btn' type='submit' class='input-btn' value='提交' onclick='multisubmit();'></div>";
        $("#m").val(m);
        $("#multi").html(multi);
    }
    if (program.length > 51) {
        program = program +
            "<div align='center' style='margin-top: 60px;margin-top: -5px'><input id='program-save-btn' type='submit' class='input-btn' value='提交' onclick='programsubmit();'></div>";
        $("#p").val(p);
        $("#program").html(program);
    }
}

function singlesubmit() {
    var d = gatherDataSingle();
    $.ajax({
        method: "PUT",
        url: "/api/system/exam/do/" + $("#pid").val(),
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
}

function multisubmit() {
    var d = gatherDataMulti();
    $.ajax({
        method: "PUT",
        url: "/api/system/exam/do/" + $("#pid").val(),
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
}

function programsubmit() {
    var d = gatherDataProgram();
    $.ajax({
        method: "PUT",
        url: "/api/system/exam/programing",
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
}


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
        d[ids] = valuecheck;
    }
    return {"answersList":JSON.stringify(d)};
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
        d[ids] = answers;
    }
    return {"answersList":JSON.stringify(d)};
}

function gatherDataProgram() {
    var pid = $("#pid").val();
    var qid = $("#1p").val();
    var text = $("input[name='1p']").val();
    var d = {
        "pid": pid,
        "qid": qid,
        "text": text
    }
    return d;
}
