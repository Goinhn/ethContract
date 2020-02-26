import $ from 'jquery';
import Web3 from 'web3';
import '../vendor/bootstrap/js/bootstrap.min';
import '../vendor/jquery.cookie/jquery.cookie';
import '../vendor/dataTables/js/jquery.dataTables.min';
import '../vendor/dataTables/js/dataTables.bootstrap4.min';


const language = {
    "sProcessing": "处理中...",
    "sLengthMenu": "显示 _MENU_ 项结果",
    "sZeroRecords": "没有匹配结果",
    "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
    "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
    "sInfoPostFix": "",
    "sSearch": "搜索:",
    "sUrl": "",
    "sEmptyTable": "表中数据为空",
    "sLoadingRecords": "载入中...",
    "sInfoThousands": ",",
    "oPaginate": {
        "sFirst": "首页",
        "sPrevious": "上页",
        "sNext": "下页",
        "sLast": "末页"
    },
    "oAria": {
        "sSortAscending": ": 以升序排列此列",
        "sSortDescending": ": 以降序排列此列"
    }
};

const testData = [
    {
        "contractId": "12345678",
        "contractName": "healer",
        "contractHash": "fhkahk4jh5jk34kj324jk32h4j23h43jk4k",
        "partyA": "A",
        "partyB": "B"
    },
    {
        "contractId": "12345678",
        "contractName": "healer",
        "contractHash": "fhkahk4jh5jk34kj324jk32h4j23h43jk4k",
        "partyA": "A",
        "partyB": "B"
    },
    {
        "contractId": "12345678",
        "contractName": "healer",
        "contractHash": "fhkahk4jh5jk34kj324jk32h4j23h43jk4k",
        "partyA": "A",
        "partyB": "B"
    }
];


const accountAddress = '0x0c54FcCd2e384b4BB6f2E405Bf5Cbc15a017AaFb';
const value = '0xde0b6b3a7640000';
const desiredNetwork = '1';


$(function () {
    userInfo();
    checkEth();
    // contractCount();
    forceDetails();
    notSignedDetails();
    loginOut();
});


function checkEth() {
    window.addEventListener('load', function () {
        // if (window.ethereum) {
        //     window.web3 = new Web3(window.ethereum);
        //     try {
        //         ethereum.enable();
        //     } catch (error) {
        //         console.log(error);
        //     }
        //     console.log(web3.eth.accounts);
        //     let address = web3.eth.accounts[0];
        //     $("#accountAddress").html(address);
        // } else if (window.web3) {
        //     window.web3 = new Web3(web3.currentProvider);
        //     // let address = getUserAccount();
        // } else {
        //     alert('请首先安装metamask');
        // }


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

                    $.ajax({
                        url: "index/userSaveAccountAddress",
                        type: "post",
                        data: {
                            "function": "userSaveAccountAddress",
                            "accountAddress": account
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.flag == true) {
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
                });

            ethereum.on('accountsChanged', function (accounts) {
                console.log(accounts[0])
            });
            console.log(ethereum.networkVersion);
            console.log(ethereum.selectedAddress);
        }
    });
}

function userInfo() {
    $.ajax({
        url: "index/userInfo",
        type: "post",
        data: {
            "function": "userInfo"
        },
        dataType: "json",
        success: function (data) {
            if (data.flag == true) {
                $.cookie("userId", data.data.username, {expires: 7, path: '/'});
                $("#username").html(data.data.username);
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


function contractCount(data) {
    $.ajax({
        url: "index/contractCount",
        type: "post",
        data: {
            "function": "contractCount",
            "userId": data.data.userId
        },
        dataType: "json",
        success: function (data) {
            if (data.flag == true) {
                $("#forceContract").html(data.data.forceContract);
                $("#notSignedContract").html(data.data.notSignedContract);
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
    });
}


function forceDetails() {
    $("#forceTable").DataTable({
        language: language,
        data: testData,
        // ajax: {
        //     url: "index/forceContract",
        //     type: "post",
        //     data: {
        //         "function": "forceContract"
        //     },
        //     dataSrc: "data",
        // },
        columns: [
            {data: 'contractName'},
            {data: 'contractHash'},
            {data: 'partyA'},
            {data: 'partyB'},
            {
                data: 'contractId',
                render: function (data, type, row) {
                    return '<a href=contractDetails.html?contractId="' + data + '" class="btn btn-secondary btn-sm btn-icon-split"><span class="text">查看</span></a>';
                }
            },
        ]
    });
}


function notSignedDetails() {
    $("#notSignedTable").DataTable({
        language: language,
        data: testData,
        // ajax: {
        //     url: "index/notSignedContract",
        //     type: "post",
        //     data: {
        //         "function": "notSignedContract"
        //     },
        //     dataSrc: "data",
        // },
        columns: [
            {data: 'contractName'},
            {data: 'contractHash'},
            {data: 'partyA'},
            {data: 'partyB'},
            {
                data: 'contractId',
                render: function (data, type, row) {
                    return '<a href=contractSign.html?contractId="' + data + '" class="btn btn-secondary btn-sm btn-icon-split"><span class="text">签署</span></a>';
                }
            },
        ]
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
