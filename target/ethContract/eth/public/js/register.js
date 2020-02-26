import $ from 'jquery';
import '../vendor/bootstrap/js/bootstrap.min';
import '../vendor/formValidator/js/formValidation.min';
import '../vendor/formValidator/js/bootstrap';
import '../vendor/formValidator/js/zh_CN';

$(function () {
    checkNow();
});

function checkNow() {
    function randomNumber(min, max) {
        return Math.floor(Math.random() * (max - min + 1) + min);
    }

    $('#codeLook').html([randomNumber(1, 100), '+', randomNumber(1, 200), '='].join(' '));

    $("#registerForm").formValidation({
        message: "值输入错误",
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                selector: "#username",
                verbose: false,
                validators: {
                    notEmpty: {
                        message: "用户名不能为空"
                    },
                    stringLength: {
                        min: 4,
                        max: 30,
                        message: "用户名长度必须在4到30之间"
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: "用户名中有非法字符"
                    }
                }
            },
            mail: {
                selector: "#mail",
                validators: {
                    emailAddress: {
                        message: "email格式错误"
                    }
                }
            },

            password: {
                selector: "#password",
                validators: {
                    notEmpty: {
                        message: "密码不能为空"
                    },
                    different: {
                        field: "username",
                        message: "密码不能与用户名相同"
                    },
                    callback: {
                        message: "密码输入错误",
                        callback: function (value, validator, $field) {
                            if (value === '') {
                                return true;
                            }

                            if (value.length < 8) {
                                return {
                                    valid: false,
                                    message: "密码长度不能小于8位"
                                };
                            }

                            if (value === value.toLowerCase()) {
                                return {
                                    valid: false,
                                    message: '密码至少包含一个大写字母'
                                }
                            }

                            if (value === value.toUpperCase()) {
                                return {
                                    valid: false,
                                    message: '密码至少包含一个小写字母'
                                }
                            }

                            if (value.search(/[0-9]/) < 0) {
                                return {
                                    valid: false,
                                    message: '它必须至少包含一个数字'
                                }
                            }

                            return true;
                        }
                    }
                }
            },

            rePassword: {
                selector: "#rePassword",
                validators: {
                    notEmpty: {
                        message: "密码不能为空"
                    },
                    callback: {
                        message: "两次密码不一致",
                        callback: function (value, validator, $field) {
                            if (value === '') {
                                return true;
                            }
                            if (value != $("#password").val()) {
                                return {
                                    valid: false,
                                    message: "两次密码不一致"
                                };
                            }
                            return true;
                        }
                    }
                }
            },

            code: {
                selector: "#code",
                validators: {
                    callback: {
                        message: "验证码输入错误",
                        callback: function (value, validator, $field) {
                            let items = $("#codeLook").html().split(' ');
                            let sum = parseInt(items[0]) + parseInt(items[2]);
                            return value == sum;
                        }
                    }
                }
            }
        },
        onSuccess: function () {
            $("#registerButton").on({
                click: function () {
                    $.ajax({
                        url: "user/register",
                        type: "post",
                        data: {
                            "function": "register",
                            "username": $("#username").val(),
                            "mail": $("#mail").val(),
                            "password": $("#password").val()
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.flag == true) {
                                alert("注册成功，请通过邮箱激活账号！");
                            } else {
                                alert(data.errorMsg);
                            }
                            $("#registerForm").data('formValidation').resetForm();
                        },
                        error: function () {
                            window.location.href = "404.html";
                            $("#registerForm").data('formValidation').resetForm();
                        }
                    })
                }
            });
        }
    });
}
