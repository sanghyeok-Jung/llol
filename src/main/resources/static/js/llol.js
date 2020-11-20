function find_user() {
    var form = document.headerForm;
    if(form.name.value == "" ) {
        alert("닉네임을 입력하세요");
        form.name.focus();
    } else {
        form.action = "find";
        form.method = "get";
        form.submit();
    }
}

function go_search() {
    var form = document.headerForm;
    form.name.value = "";
    form.name.focus();
}

function login() {
    // 요청한 폼 정보 : 이후 action, method, submit()을 사용하기 위해 정의함
    var form = document.loginForm;

    // 요청한 폼에서 가져온 데이터 (id, password)
    // $("#") 은 name이 아니라 id 속성값을 검색해서 가져옴
    var id = $("#InputId").val();
    var password = $("#Password").val();

    // 아이디 값이 비어있지 않으면 실행
    if(id != "") {
        // ajax 통신 요청
        $.ajax({
            // 요청할 URL에 id 값을 매개변수로 전달한다.
            url: 'checkMember?id=' + id,
            // 요청 method 설정
            type: 'get',

            // 요청이 성공되었을 경우 data = 전달받은 값
            success: function (data) {
                // 아이디가 없는 경우
                if (data < 1) {
                    // 안내 텍스트를 출력할 영역들에 대한 텍스트 내용 초기화
                    $("#checkResultId").text("존재하지 않는 아이디입니다.");
                    $("#checkResultPassword").text("");
                } else {
                    // 비밀번호가 비어있는지 확인한다.
                    if(password == "") {
                        // 안내 텍스트를 출력할 영역들에 대한 텍스트 내용 초기화
                        $("#checkResultId").text("");
                        $("#checkResultPassword").text("비밀번호를 입력하세요.");
                    } else {
                        // 아이디가 있는 경우
                        // 다시 ajax 호출로 DB에 저장된 비밀번호를 가져옴
                        $.ajax({
                            url: 'getMemberInfo',
                            type: 'post',
                            data: 'id=' + id,

                            // 데이터 받아오기 성공
                            success: function (data) {
                                // 로그인 폼에 입력된 비밀번호와 일치하는지 확인
                                // 일치하면 로그인 요청 시도
                                if(password == data) {
                                    form.method = "post";
                                    form.action = "login";
                                    form.submit();
                                } else {
                                    // 안내 텍스트를 출력할 영역들에 대한 텍스트 내용 초기화
                                    $("#checkResultId").text("");
                                    $("#checkResultPassword").text("비밀번호가 틀렸습니다.");
                                }
                            }
                        });
                    }
                }
            }
        });
    } else {
        // 아이디 값이 비어있으면 실행되는 부분
        // id = checkResultId의 텍스트 값을 다음과 같이 변경한다.
        $("#checkResultId").text("아이디를 입력히세요.");
    }
}

function signUp() {
    // 요청한 폼 정보 : 이후 action, method, submit()을 사용하기 위해 정의함
    var form = document.signUpForm;

    // 요청한 폼에서 가져온 데이터 (id, password1, password2, passoword)
    // $("#") 은 name이 아니라 id 속성값을 검색해서 가져옴
    var id = $("#InputId").val();
    var password1 = $("#Password1").val();
    var password2 = $("#Password2").val();
    var name = $("#InputName").val();

    // 아이디 값이 비어있지 않으면 실행
    if(id != "") {
        // ajax 통신 요청
        $.ajax({
            // 요청할 URL에 id 값을 매개변수로 전달한다.
            url: 'checkMember?id=' + id,
            // 요청 method 설정
            type: 'get',

            // 요청이 성공되었을 경우 data = 전달받은 값
            success: function (data) {
                // 메소드에서 처리한 결과 값이 1이 이상인 경우
                // 아이디가 있는 경우
                if (data > 1) {
                    // 안내 텍스트를 출력할 영역들에 대한 텍스트 내용 초기화
                    $("#checkResultId").text("이미 사용중인 아이디입니다.");
                    $("#checkResultPassword1").text("");
                    $("#checkResultPassword2").text("");
                    $("#checkResultName").text("");
                } else {
                    // 아이디가 존재하지 않으면, 나머지 부분들의 입력에 대해 확인하고 submit한다.

                    // 비밀번호가 비어있는지, 비밀번호 확인과 일치하는지, 닉네임이 비어있는지 확인한다.
                    if(password1 == "") {
                        $("#checkResultId").text("");
                        $("#checkResultPassword1").text("비밀번호를 입력하세요.");
                        $("#checkResultPassword2").text("");
                        $("#checkResultName").text("");
                    } else if(password2 == "") {
                        $("#checkResultId").text("");
                        $("#checkResultPassword1").text("");
                        $("#checkResultPassword2").text("비밀번호를 입력하세요.");
                        $("#checkResultName").text("");
                    } else if(password1 != password2) {
                        $("#checkResultId").text("");
                        $("#checkResultPassword1").text("");
                        $("#checkResultPassword2").text("비밀번호가 서로 다릅니다.");
                        $("#checkResultName").text("");
                    } else if(name == "") {
                        $("#checkResultId").text("");
                        $("#checkResultPassword1").text("");
                        $("#checkResultPassword2").text("");
                        $("#checkResultName").text("닉네임을 입력하세요.");
                    } else {
                        // 중복 처리도 성공했고, 모든 텍스트 입력란에 대한 조건이 확인된 경우 처리하는 영역
                        // 회원가입 성공
                        form.method = "post";
                        form.action = "signUp";
                        form.submit();
                    }
                }
            }
        })
    } else {
        // 아이디 값이 비어있으면 실행되는 부분
        // id = checkResultId의 텍스트 값을 다음과 같이 변경한다.
        $("#checkResultId").text("아이디를 입력히세요.");
    }
}

function updateMember() {
    var form = document.updateMemberForm;

    var password1 = $("#Password1").val();
    var password2 = $("#Password2").val();
    var name = $("#InputName").val();

    if(password1 == password2) {
        if(name != "") {
            form.action = "updateMember";
            form.method = "post";
            form.submit();
        } else {
            $("#checkResultPassword1").text("");
            $("#checkResultPassword2").text("");
            $("#checkResultName").text("닉네임을 입력해주세요.");
        }
    } else {
        $("#checkResultPassword1").text("");
        $("#checkResultPassword2").text("비밀번호를 다시 확인해주세요.");
        $("#checkResultName").text("");
    }
}

function insertBoard() {
    var form = document.insertBoardForm;
    var title = $("#InputTitle").val();
    var content = $("#Content").val();

    if(title != "") {
        if(content != "") {
            form.action = "insertBoard"
            form.method = "post";
            form.submit();
        } else {
            $("#checkResultTitle").text("");
            $("#checkResultContent").text("내용을 입력하세요.");
        }
    } else {
        $("#checkResultTitle").text("제목을 입력하세요.");
        $("#checkResultContent").text("");
    }
}

function deleteBoard() {
    var form = document.boardForm;
    var boardnum = form.boardnum.value;
    var boardwriter = form.boardwriter.value;

    if(confirm("정말 삭제하시겠습니까?") == true) {
        form.method = "post";
        form.setAttribute("boardnum", boardnum);
        form.setAttribute("boardwriter", boardwriter);
        form.action = "deleteBoard";
        form.submit();
    }
}

function updateBoardView() {
    var form = document.boardForm;
    var boardnum = form.boardnum.value;
    var boardwriter = from.boardwriter.value;

    form.method = "post";
    form.setAttribute("boardnum", boardnum);
    form.setAttribute("boardwriter", boardwriter);
    form.action = "updateBoardView";
    form.submit();
}