import $ from 'jquery';
import '../vendor/bootstrap/js/bootstrap.min';
import '../vendor/formValidator/js/formValidation.min';
import '../vendor/formValidator/js/bootstrap';
import '../vendor/formValidator/js/zh_CN';
import '../vendor/jquery.cookie/jquery.cookie'

$(function () {
    loginCheck();
});

function loginCheck() {
    $("#loginForm").formValidation({
        message: "值输入错误",
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            nameMail: {
                selector: "#nameMail",
                verbose: false,
                validators: {
                    notEmpty: {
                        message: "用户名/邮箱不能为空"
                    }
                }
            },
            password: {
                selector: "#password",
                validators: {
                    notEmpty: {
                        message: "密码不能为空"
                    }
                }
            }
        },
        onSuccess: function () {
            $("#loginButton").on({
                click: function () {
                    $.ajax({
                        url: "user/login",
                        type: "post",
                        data: {
                            "function": "login",
                            "nameMail": $("#nameMail").val(),
                            "password": $("#password").val(),
                            "remember": $("#remember").val()
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.flag == true) {
                                let userId = data.data.userId;
                                if ($("#remember") == "on") {
                                    $.cookie("userId", userId, {expires: 7, path: '/'});
                                }
                                let url = "index.html?userId=" + userId;
                                window.location.href = url;

                            } else {
                                alert(data.errorMsg);
                            }
                            $("#loginForm").data('formValidation').resetForm();
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
                    })
                }
            });
        }
    });
}

