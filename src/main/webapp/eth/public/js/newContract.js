import $ from 'jquery';
import Web3 from 'web3';
import '../vendor/bootstrap/js/bootstrap.min';
import '../vendor/fileInput/js/fileinput.min';
import '../vendor/jquery.cookie/jquery.cookie';
import BMF from 'browser-md5-file';
import r from 'jsrsasign';


$(function () {
    checkEth();
    userInfo();
    fileInput();
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


function fileInput() {
    $('#fileInput').fileinput({
        language: "en", //配置语言
        showUpload: true, //显示整体上传的按钮
        showRemove: true, //显示整体删除的按钮
        uploadAsync: true, //默认异步上传
        uploadLabel: "上传", //设置整体上传按钮的汉字
        removeLabel: "移除", //设置整体删除按钮的汉字
        uploadClass: "btn btn-primary", //设置上传按钮样式
        showCaption: true, //是否显示标题
        dropZoneEnabled: true, //是否显示拖拽区域
        maxFileSize: 102400, //文件大小限制
        maxFileCount: 1, //允许最大上传数，可以多个，
        enctype: 'multipart/form-data',
        allowedFileExtensions: ["jpg", "png", "gif", "docx", "zip", "xlsx", "txt", "pdf"],
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        showBrowse: true,
        browseOnZoneClick: true,
        uploadUrl: 'newContract/upload',
        uploadExtraData: function () {//向后台传递参数
            return {
                "function": "upload"
            };
        },
        slugCallback: function (filename) {
            return filename.replace('(', '_').replace(']', '_');
        }
    }).on('change', function (event) {
        console.log("change");
    }).on('filepreupload', function (event, data, previewId, index) {
        let partyA = $("#partyA").val();
        let partyB = $("#partyB").val();
        let privateKey = $("#privateKey").val();

        if (partyA == '' || partyB == '' || privateKey == '') {
            $("fileInput").fileinput('clear');
            alert("请先填写交易信息");
            event.stop();
        }

        let file = data.files[0];
        let fileName = data.files[0].name;
        console.log(fileName);
        let fileHash = fileToHash(file);
        backPublicKey(partyA, partyB, privateKey, fileHash, fileName, event);

    }).on('fileerror', function (event, data, msg) {
        console.log('文件上传失败！' + msg);
    });
}


function fileToHash(file) {
    const bmf = new BMF();
    let fileHash = '';
    bmf.md5(
        file,
        (err, md5) => {
            console.log('err:', err);
            console.log('md5 string:', md5);
            fileHash = md5;
            $("#fileHash").html(md5);
        },
        progress => {
            console.log('progress number:', progress);
        },
    );

    return fileHash;
}


function backPublicKey(partyA, partyB, privateInput, fileHash, fileName, event) {
    $.ajax({
        url: "newContract/publicKey",
        data: {
            "function": "publicKey"
        },
        dataType: "json",
        success: function (data) {
            if (data.flag == true) {
                let publicInput = data.data.publicKey;
                if (publicInput != '') {
                    try {
                        let signaturePrivate = new r.Signature({
                            alg: "SHA1withRSA",
                            prvkeypem: privateInput
                        });
                        console.log(signaturePrivate);
                        signaturePrivate.updateString(fileHash);
                        let a = signaturePrivate.sign();
                        let signPrivate = r.hextob64(a);
                        console.log("私钥签名： " + signPrivate);

                        let signaturePublic = new r.Signature({
                            alg: "SHA1withRSA",
                            prvkeypem: publicInput
                        });
                        signaturePublic.updateString(fileHash);
                        let b = signaturePublic.verify(r.b64tohex(signPrivate));
                        console.log("公钥验签： " + b);
                        if (b != true) {
                            alert("公私钥不匹配");
                            event.stop();
                            return;
                        } else {
                            checkSigned(paryA, partyB, fileHash, fileName, event);
                        }
                    } catch (e) {
                        console.log(e);
                        event.stop();
                        alert("请输入正确的RSA公私钥");
                        return;
                    }
                } else {
                    event.stop();
                    alert("尚未绑定签名，先前去绑定签名");
                }

            } else {
                event.stop();
                alert(data.errorMsg);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            event.stop();
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


function checkSigned(partyA, partyB, fileHash, fileName, event) {
    $.ajax({
        url: "newContract/checkSigned",
        data: {
            "function": "checkSigned",
            "fileHash": fileHash
        },
        dataType: "json",
        success: function (data) {
            if (data.flag == true) {
                block(partyA, partyB, fileHash, fileName, event);
            } else {
                event.stop();
                alert(data.errorMsg);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            event.stop();
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

function block(partyA, partyB, fileHash, fileName, event) {
    if (window.ethereum) {
        window.web3 = new Web3(ethereum);
        try {
            ethereum.enable();
        } catch (error) {
            alert("发生异常！");
        }
        console.log(web3.eth.accounts);
        let address = web3.eth.accounts[0];
        console.log("地址：" + address);
    } else if (window.web3) {
        window.web3 = new Web3(web3.currentProvider);
        let address = getUserAccount();
        console.log("地址：" + address);
    } else {
        alert('请首先安装metamask');
    }
}


function submitInfo(partyA, partyB, fileName, fileHash, contractAddress, event) {
    $.ajax({
        url: "newContract/submitInfo",
        data: {
            "function": "submitInfo",
            "partyA": partyA,
            "partyB": partyB,
            "fileHash": fileHash,
            "fileName": fileName,
            "contractAddress": contractAddress
        },
        dataType: "json",
        success: function (data) {
            if (data.flag == true) {
                alert("签署成功");
            } else {
                event.stop();
                alert(data.errorMsg);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            event.stop();
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