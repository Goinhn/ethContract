import echarts from 'echarts';
import $ from "jquery";
import '../vendor/jquery.cookie/jquery.cookie';


$(function () {
    checkEth();
    userInfo();
    dataLook();
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


function dataLook() {
    let myChart = echarts.init(document.getElementById('contractData'));

    let option = {
        backgroundColor: "#0B1837",
        color: ["#EAEA26", "#906BF9", "#FE5656", "#01E17E", "#3DD1F9", "#FFAD05"],
        title: {
            text: '合同统计',
            left: '60',
            top: 0,
            textAlign: 'center',
            textStyle: {
                color: '#fff',
                fontSize: 14,
                fontWeight: 0
            }
        },
        grid: {
            left: -100,
            top: 50,
            bottom: 10,
            right: 10,
            containLabel: true
        },
        tooltip: {
            trigger: 'item',
            formatter: "{b} : {c} ({d}%)"
        },
        legend: {
            type: "scroll",
            orient: "vartical",
            x: "right",
            top: "center",
            right: "15",
            bottom: "0%",
            itemWidth: 16,
            itemHeight: 8,
            itemGap: 16,
            textStyle: {
                color: '#A3E2F4',
                fontSize: 12,
                fontWeight: 0
            },
            data: ['已签署合同', '未生效合同']
        },
        polar: {},
        angleAxis: {
            interval: 1,
            type: 'category',
            data: [],
            z: 10,
            axisLine: {
                show: false,
                lineStyle: {
                    color: "#0B4A6B",
                    width: 1,
                    type: "solid"
                },
            },
            axisLabel: {
                interval: 0,
                show: true,
                color: "#0B4A6B",
                margin: 8,
                fontSize: 16
            },
        },
        radiusAxis: {
            min: 40,
            max: 120,
            interval: 20,
            axisLine: {
                show: false,
                lineStyle: {
                    color: "#0B3E5E",
                    width: 1,
                    type: "solid"
                },
            },
            axisLabel: {
                formatter: '{value} %',
                show: false,
                padding: [0, 0, 20, 0],
                color: "#0B3E5E",
                fontSize: 16
            },
            splitLine: {
                lineStyle: {
                    color: "#0B3E5E",
                    width: 2,
                    type: "solid"
                }
            }
        },
        calculable: true,
        series: [{
            type: 'pie',
            radius: ["5%", "10%"],
            hoverAnimation: false,
            labelLine: {
                normal: {
                    show: false,
                    length: 30,
                    length2: 55
                },
                emphasis: {
                    show: false
                }
            },
            data: [{
                name: '',
                value: 0,
                itemStyle: {
                    normal: {
                        color: "#0B4A6B"
                    }
                }
            }]
        }, {
            type: 'pie',
            radius: ["90%", "95%"],
            hoverAnimation: false,
            labelLine: {
                normal: {
                    show: false,
                    length: 30,
                    length2: 55
                },
                emphasis: {
                    show: false
                }
            },
            name: "",
            data: [{
                name: '',
                value: 0,
                itemStyle: {
                    normal: {
                        color: "#0B4A6B"
                    }
                }
            }]
        }, {
            stack: 'a',
            type: 'pie',
            radius: ['20%', '80%'],
            roseType: 'area',
            zlevel: 10,
            label: {
                normal: {
                    show: true,
                    formatter: "{c}",
                    textStyle: {
                        fontSize: 12,
                    },
                    position: 'outside'
                },
                emphasis: {
                    show: true
                }
            },
            labelLine: {
                normal: {
                    show: true,
                    length: 20,
                    length2: 55
                },
                emphasis: {
                    show: false
                }
            },
            data: [
                {
                    value: 10,
                    name: '已签署合同'
                },
                {
                    value: 5,
                    name: '未生效合同'
                },

            ]
        },]
    };

    myChart.setOption(option);

}