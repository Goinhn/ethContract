import $ from 'jquery';
import '../vendor/bootstrap/js/bootstrap.min';
import '../vendor/jquery.cookie/jquery.cookie';
import r from "jsrsasign";


$(function () {
    checkEth();
    userInfo();
    details();
    checkSign();
    loginOut();
});


function getUrlParam(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    let r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}


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


function userInfo() {
    $("#username").html($.cookie("username"));
}


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


function checkSign() {
    let contractId = getUrlParam("contractId");
    console.log(contractId);
    $("signButton").on({
        click: function () {
            $.ajax({
                url: "user/checkSign",
                type: "post",
                data: {
                    "function": "checkSign",
                    "contractId": contractId
                },
                dataType: "json",
                success: function (data) {
                    if (data.flag == true) {
                        try {
                            let signaturePrivate = new r.Signature({
                                alg: "SHA1withRSA",
                                prvkeypem: privateInput
                            });
                            console.log(signaturePrivate);
                            signaturePrivate.updateString("test");
                            let a = signaturePrivate.sign();
                            let signPrivate = r.hextob64(a);
                            console.log("私钥签名： " + signPrivate);

                            let signaturePublic = new r.Signature({
                                alg: "SHA1withRSA",
                                prvkeypem: data.data.publicKey
                            });
                            signaturePublic.updateString("test");
                            let b = signaturePublic.verify(r.b64tohex(signPrivate));
                            console.log("公钥验签： " + b);
                            if (b != true) {
                                alert("公私钥不匹配");
                                return;
                            } else {
                                block();
                            }
                        } catch (e) {
                            console.log(e);
                            alert("请输入正确的RSA公私钥");
                            return;
                        }
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

function block(){

}


function userSigned(contractAddress) {
    let contractId = getUrlParam("contractId");
    console.log(contractId);

    $.ajax({
        url: "user/userSigned",
        type: "post",
        data: {
            "function": "userSigned",
            "contractId": contractId,
            "contractAddress": contractAddress
        },
        dataType: "json",
        success: function (data) {
            if (data.flag == true) {
                alert("签署成功");
                window.location.href = "index.html";
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