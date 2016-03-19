/**
 * Created by zhang on 2016/3/16.
 */
$(
    function test(d) {
        var d = {
            act: "do_vote",
            code: undefined,
            v_areaid: "3",
            v_id: "90",
            v_parentid: "260",
            flag: "0"
        }
        for (var i = 0; i < 100; i++) {
            t(d);
        }
    }
)
function t(d) {
    $.ajax({
        method: "POST",
        url: "http://31.5219.net/ajaxout.php",
        data: d,
        success: function (data) {
            if (data.msg == "success") {
                layer.alert("成功");
            }
        }
    })
}
