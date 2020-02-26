import $ from 'jquery';
import '../vendor/bootstrap/js/bootstrap.min';

$(function () {
    activeNow();
});


//获取url中的属性值
function getUrlParam(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    let r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}


//激活账户
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
                case 403:
                    alert("系统拒绝：您没有访问权限。");
                    break;
                case 404:
                    alert("您访问的资源不存在。");
                    // window.location.href = "404.html";
                    break;
                case 500:
                    alert("服务器内部错误");
                    break;
                case 503:
                    alert("服务不可用");
                    break;
            }
        }
    });

}