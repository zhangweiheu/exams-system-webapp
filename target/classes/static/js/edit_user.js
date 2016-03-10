/**
 * Created by zhang on 2016/3/10.
 */
$("#submit_btn").on("click",function(){

});
function gatherData(){
    var id = $("#id").val();
    var username = $("#username").val().trim();
    var password = $.md5($("#password").val().trim());
    var phone = $("#phone").val().trim();
    var email = $("#email").val().trim();
    var wechat = $("#wechat").val().trim();
    var intro = $("#intro").val().trim();

}
