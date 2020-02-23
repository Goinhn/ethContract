import $ from 'jquery';
import '../vendor/bootstrap/js/bootstrap.min';

$(function () {
    activeNow();
});

function getUrlParam(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    let r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}

function activeNow() {
    let code = getUrlParam("code");
    $.ajax({
        url: "user/active",
        type: "post",
        data: {
            "function": "active",
            "code": code
        },
        dataType: "json",
        success: function (data) {
            if (data.flag == true) {
                let url = "<a href='login.html'>点击跳转登录</a>";
                $("#tip").val("账号激活成功，" + url);
            } else {
                $("#tip").val("账号激活失败");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status == "undefined") {
                return;
            }
            switch (jqXHR.status) {
                case 400:
                    $("#tip").val("400：错误的语法请求");
                    break;
                case 401:
                    $("#tip").val("401：需要进行身份验证");
                    break;
                case 403:
                    $("#tip").val("系统拒绝：您没有访问权限。");
                    break;
                case 404:
                    alert("您访问的资源不存在。");
                    window.location.href = "404.html";
                    break;
                case 406:
                    $("#tip").val("方法禁用");
                    break;
                case 500:
                    $("#tip").val("服务器内部错误");
                    break;
                case 503:
                    $("#tip").val("服务不可用");
                    break;
                case 504:
                    $("#tip").val("网关超时");
                    break;
            }
        }
    });

}