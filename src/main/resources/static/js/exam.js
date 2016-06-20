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
                generatePaperView(d.questions, d.time);
            }
        }
    });
    $(document).ready(function(){
        $('input').iCheck({
            checkboxClass: 'icheckbox_flat-blue',
            radioClass: 'iradio_flat-blue'
        });
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
                generatePaperView(d.questions, d.time);
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
//$('#checkAll').on("click", function () {
//    $('input[name="questionTagList"]').iCheck(this.checked);
//});
//
//$("input[name='questionTagList']").on("click", function () {
//    $('#checkAll').iCheck($("input[name='questionTagList']").length == $("input[name='questionTagList']:checked").length ? 'uncheck':'check');
//});


/**试题显示页*/

function generatePaperView(questions, time) {

    if (questions === "" || questions === undefined || questions.length < 1) {
        return;
    }

    $(".selectDom").hide();
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
            single = single + "<div class='font-style' style='position: relative;left: 20px;margin-top: 25px'>" + s + "、" + title + "</div>";
            single = single + "<div style='margin:0 auto;position: relative'>";
            single = single + "<div hidden><input id='" + s + "s' value='" + id + "' hidden/></div>";
            single = single + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + s + "s' type='radio' value='A'" + ("A" === q.currentAnswer ? "checked='checked'" : null) + ">" + options.A + "</label></div>";
            single = single + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + s + "s' type='radio' value='B'" + ("B" === q.currentAnswer ? "checked='checked'" : null) + ">" + options.B + "</label></div>";
            single = single + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + s + "s' type='radio' value='C'" + ("C" === q.currentAnswer ? "checked='checked'" : null) + ">" + options.C + "</label></div>";
            single = single + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + s + "s' type='radio' value='D'" + ("D" === q.currentAnswer ? "checked='checked'" : null) + ">" + options.D + "</label></div>";
            single = single + "</div>";
        }

        if (q.questionType == "MULTI_SELECTION") {
            m++;
            var options = JSON.parse(q.options);
            multi = multi + "<div class='font-style' style='position: relative;left: 20px;margin-top: 25px'>" + m + "、" + title + "</div>"
            multi = multi + "<div style='margin:0 auto;position: relative'>";
            multi = multi + "<div hidden><input id='" + m + "m' value='" + id + "' hidden/></div>";
            multi = multi + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + m + "m' type='checkbox' value='A'" + isTheRadio(q.currentAnswer, "A") + ">" + options.A + "</label></div>";
            multi = multi + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + m + "m' type='checkbox' value='B'" + isTheRadio(q.currentAnswer, "B") + ">" + options.B + "</label></div>";
            multi = multi + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + m + "m' type='checkbox' value='C'" + isTheRadio(q.currentAnswer, "C") + ">" + options.C + "</label></div>";
            multi = multi + "<div style='width:25%;float: left;vertical-align:middle'><label style='font-size: 15px;color: black;right: 30px'><input class='input-radio' name='" + m + "m' type='checkbox' value='D'" + isTheRadio(q.currentAnswer, "D") + ">" + options.D + "</label></div>";
            multi = multi + "</div>";
        }

        if (q.questionType == "PROGRAMMING_QUESTION") {
            p++;
            program = program + "<div class='font-style' style='position: relative;left: 20px;margin-bottom: 20px'>" + p + "、" + title + "</div>"
            program = program + "<div style='margin:0 auto;position: relative'>";
            program = program + "<div><input id='" + p + "p' class='input-radio' value='" + id + "' hidden/></div>";
            program = program + "<div><textarea style='width:80%;height:450px;margin-left: 10%;margin-bottom: 3%' name='" + p + "p' type=''>" +("" == q.currentAnswer || undefined == q.currentAnswer ? "public class demo\{public static void main\(String\[\] args\) \{\}\}" : q.currentAnswer)+"</textarea></div>";
            program = program + "</div>";
        }
    }

    if (single.length > 52) {
        single = single +
            "<div align='right' style='margin-top: 60px;margin-top: -5px'><a id='single-save-btn' class='button button-primary button-pill button-normal' onclick='singlesubmit();'>提交</a></div>";
        $("#s").val(s);
        $("#single").html(single);
    }
    if (multi.length > 52) {
        multi = multi +
            "<div align='right' style='margin-top: 60px;margin-top: -5px'><a id='multi-save-btn' class='button button-primary button-pill button-normal' onclick='multisubmit();'>提交</a></div>";
        $("#m").val(m);
        $("#multi").html(multi);
    }
    if (program.length > 51) {
        program = program +
            "<div align='right' style='margin-top: 60px;margin-top: -5px'><a id='program-save-btn' class='button button-primary button-pill button-normal' onclick='programsubmit();'>提交</a></div>";
        $("#p").val(p);
        $("#program").html(program);
    }

    //设置要倒计时的秒数
    countTime(time);
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
                layer.alert(data.data, {
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

/**结束考试*/
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
                location.reload(true);
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
    return {"answersList": JSON.stringify(d)};
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
    return {"answersList": JSON.stringify(d)};
}

function gatherDataProgram() {
    var pid = $("#pid").val();
    var qid = $("#1p").val();
    var text = $("textarea[name='1p']").val();
    var d = {
        "pid": pid,
        "qid": qid,
        "text": text
    };
    return d;
}

function isTheRadio(currentAnswers, option) {
    var answer = currentAnswers.split(",");
    for (var i = 0; i < answer.length; i++) {
        if (option === answer[i]) {
            return "checked='checked'";
        }
    }
    return null;
}
function countTime(times){
    if(times <= 0)return;
    var timebody = "<div id='CountDownTimer' data-timer='" + times +"' style='width: 300px;height: 100px'></div>";
    $("#countTime").html(timebody);
    $("#CountDownTimer").TimeCircles(
        {
            count_past_zero: false,
            time: {
                Days: {show: false}
            }
        }).addListener(function(total,value,unit){
        if(unit <= 300 && unit >299){
            $("#CountDownTimer").append('<strong style="color: red">后将自动交卷,请提前提交各部分试题</strong>');
        }
        //if(unit <= 20 && unit >19){
        //    singlesubmit();
        //}
        //if(unit <= 10 && unit >9){
        //    $("#CountDownTimer").attr('data-timer',20);
        //    $("#CountDownTimer").TimeCircles().destroy();
        //    $("#CountDownTimer").TimeCircles().restart();
        //}
        if(unit <= 0){
            $("#exam-over-btn").click();
        }
    });
}

