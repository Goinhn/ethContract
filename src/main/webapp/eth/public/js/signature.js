import $ from 'jquery';
import Web3 from 'web3';
import r from 'jsrsasign';
import '../vendor/bootstrap/js/bootstrap.min';
import '../vendor/jquery.cookie/jquery.cookie';


// const privateInput = '-----BEGIN PRIVATE KEY-----\n' +
//     'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOoGv7y0wJCy0/cz\n' +
//     'F9PeuvaRySuP4e3fldxomq4rYVYgzu2mC7jKgpnROSBER17AuqXxH0D5W+vmeIZd\n' +
//     'WVqBujjZFlkDHK8tqJgkIfop+/LqUypcezAVmRS55S/Fy9j4OdP+qAg6R0Ym2Kbx\n' +
//     'Op6f6JfgjYuNJ/O8dGBAK1NxgjztAgMBAAECgYAE9Neiu4FIj0EF/0MF7bWv+y5z\n' +
//     'ibyTOSUHYf/EqI6LMG6xgc7FvyQ89b1r/YY1b4eEdDLhQWa8/EiZL+jwjcbP2wrC\n' +
//     'KTC5+75VT0j/bAEkzqA2638zKu6UHyzKwgF6nfHy/SsbKke5xt3cWc8uMfSbqIpj\n' +
//     'ipQGSGvBrU7zC5Ox0QJBAPfqlKEeKQaI7VVPy6XD4FnJJ6HFK+uY6Pvw3i9Gulwj\n' +
//     'V5NIXKeW8upfUynSYgxpQ3BY7CEK0TtqTiXbqzwLXI8CQQDxqDmx4G24ZwjxEd3q\n' +
//     'qYlinCtb40P0j3glEjXGbiJnml8SdQDlhYZml7gskM/qxds8L3GA2dcr0lo0wTSn\n' +
//     'T4TDAkEA6h7LkYPr9lHk30Xl6XsbKW8/UBPlsC3NB++lwzulzlFJ6LrTVSjF6fNv\n' +
//     'k/UrxtC5cSVoiKOwh82SU0opYSDRnQJAXfX1CO6ketDFL0atLLLi0k66pS3GnG60\n' +
//     'XmWoNxFXm0TwtnJ7+MJkMbvIRru/vB9WUR55WYpaGZXesSh2Wp+WlQJBAKMLvDc9\n' +
//     'nn7Jd9++YREBSc5yqeKPivszWjjVgc0+ZzYCryA3Y3uNk+3veRZRdogRpx9pfha9\n' +
//     'MN6aX0jVMN1Dix0=\n' +
//     '-----END PRIVATE KEY-----\n';
//
//
// const publicInput = '-----BEGIN PUBLIC KEY-----\n' +
//     'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDqBr+8tMCQstP3MxfT3rr2kckr\n' +
//     'j+Ht35XcaJquK2FWIM7tpgu4yoKZ0TkgREdewLql8R9A+Vvr5niGXVlagbo42RZZ\n' +
//     'AxyvLaiYJCH6Kfvy6lMqXHswFZkUueUvxcvY+DnT/qgIOkdGJtim8Tqen+iX4I2L\n' +
//     'jSfzvHRgQCtTcYI87QIDAQAB\n' +
//     '-----END PUBLIC KEY-----\n';


$(function () {
    checkEth();
    userInfo();
    signedPublicAndPrivate();
    loginOut();
});

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
                    // if (ethereum.networkVersion !== desiredNetwork) {
                    //     alert('This application requires the main network, please switch it in your MetaMask UI.')
                    // }
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


function signedPublicAndPrivate() {
    $("#signatureButton").on({
        "click": function () {
            let privateInput = $("#private").val();
            let publicInput = $('#public').val();
            console.log(privateInput);
            console.log(publicInput);
            let personAddress = $('#personAddress').val();

            try {
                let signaturePrivate = new r.Signature({
                    alg: "SHA1withRSA",
                    prvkeypem: privateInput
                });
                console.log(signaturePrivate);
                signaturePrivate.updateString(personAddress);
                let a = signaturePrivate.sign();
                let signPrivate = r.hextob64(a);
                console.log("私钥签名： " + signPrivate);

                let signaturePublic = new r.Signature({
                    alg: "SHA1withRSA",
                    prvkeypem: publicInput
                });
                signaturePublic.updateString(personAddress);
                let b = signaturePublic.verify(r.b64tohex(signPrivate));
                console.log("公钥验签： " + b);
                if (b != true) {
                    alert("公私钥不匹配");
                    return;
                }
            } catch (e) {
                console.log(e);
                alert("请输入正确的RSA公私钥");
                return;
            }

            $.ajax({
                url: "signature/saveSign",
                data: {
                    "function": "saveSign",
                    "accountAddress": personAddress,
                    "publicKey": publicInput
                },
                dataType: "json",
                success: function (data) {
                    if (data.flag == true) {
                        alert("签名保存成功")
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
    })
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
