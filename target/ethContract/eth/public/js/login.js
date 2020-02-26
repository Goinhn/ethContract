import '../../app';

function checkNameMail() {
    let nameMail = $("#nameMail").val();
    let reg = /^.{4,30}$/;
    let flag = reg.test(nameMail);
    if (flag) {
        $("#nameMail").css("border", "");
    } else {
        alert('error');
        $("#nameMail").css("border", "1px solid red");
    }
    return flag;
}

function checkPassword() {
    let password = $("#password").val();
    let reg = /^\w{4,20}$/;
    let flag = reg.test(password);
    if (flag) {
        $("#password").css("border", "");
    } else {
        alert('error');
        $("#password").css("border", "1px solid red");
    }
    return flag;
}

$(function () {
    $("#loginButton").on({
        click: () => {
            if (checkNameMail() && checkPassword()) {
                $.post("user/login", $("#loginForm").serialize(), function (data) {
                    if (data.flag) {
                        console.log(data);
                    } else {
                        alert(data["errorMsg"]);
                    }
                });
            }
            return false;
        }
    });

    $("#nameMail").on({
        blur: checkNameMail
    });
    $("#password").on({
        blur: checkPassword
    });
});
