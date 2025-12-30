var loginForm = document.forms["login-form"];

loginForm.onsubmit = async function(event) {

    event.preventDefault();

    var username = loginForm.elements["lg_username"].value;
    var password = loginForm.elements["lg_password"].value;

    try {
        var data = await $.ajax({
            url: '/board/ct/check-Login-endpoint',
            type: 'GET',
            data: {
                username: username,
                password: password
            }
        });

        if (data.message === 'login-true') {
            alert('로그인 성공.');

            var hiddenInput = document.createElement("input");
            hiddenInput.setAttribute("type", "hidden");
            hiddenInput.setAttribute("value", username);
            loginForm.appendChild(hiddenInput);
            loginForm.submit();
        } else if (data.message === 'login-false') {
            alert('아이디 또는 비밀번호를 잘못 입력했습니다.\n' + '입력하신 내용을 다시 확인해주세요.');
            return;
        }
    } catch (error) {
        console.error('로그인 에러:', error);
        return;
    }
}
