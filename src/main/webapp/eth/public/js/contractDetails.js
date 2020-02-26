import $ from 'jquery';
import '../vendor/bootstrap/js/bootstrap.min';
import '../vendor/jquery.cookie/jquery.cookie';


$(function () {
    checkEth();
    userInfo();
    details();
    loginOut();
});

//获取url的属性值
function getUrlParam(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    let r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}


//检查是否接入钱包
function checkEth() {
    window.addEventListener('load', function () {
        if (typeof window.ethereum === 'undefined') {
            alert('请首先安装metamask')
        } else {
            ethereum.enable()
                .catch(function (reason) {
                    if (reason === 'User rejected provider access') {
                    } else {
                        alert('当前钱包账户未登录');
                    }
                })
                .then(function (accounts) {
                    console.log(accounts);
                    let account = accounts[0];
                    console.log(account);
                    $("#accountAddress").html(account);
                })
        }
    });
}


//获取用户的昵称
function userInfo() {
    $("#username").html($.cookie("username"));
}


//获取合同的详细信息
function details() {
    let contractId = getUrlParam("contractId");
    console.log(contractId);

    $.ajax({
        url: "user/contractDetails",
        type: "post",
        data: {
            "function": "details",
            "contractId": contractId
        },
        dataType: "json",
        success: function (data) {
            if (data.flag == true) {
                $("#contractName").html(data.data.contractName);
                $("#contractHash").html(data.data.contractHash);
                $("#contractAddress").html(data.data.contractAddress);
                $("#partyA").html(data.data.partyA);
                $("#partyB").html(data.data.partyB);
            } else {
                alert(data.errorMsg);
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


//账户退出
function loginOut() {
    $("#loginOut").on({
        click: function () {
            $.ajax({
                url: "user/loginOut",
                type: "post",
                data: {
                    "function": "loginOut",
                },
                dataType: "json",
                success: function (data) {
                    if (data.flag == true) {
                        window.location.href = "login.html";
                    } else {
                        alert(data.errorMsg);
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
    });
}