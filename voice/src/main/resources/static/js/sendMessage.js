//채팅방 정보 불러오기
var roomId = $("#roomId").val();

function getId(id){
	return document.getElementById(id);
}

var data = {}; // 전송 데이터(JSON)

var socket ;

//user 정보 불러오기
var mid = getId('mid');

var btnLogin = getId('btnLogin'); // 임시제작 로그인 버튼
var btnSend = getId('sendBtn'); // 메세지 전송 버튼

var msg = getId('message-input');

var messageType;

btnLogin.onclick = function(){

    socket = new WebSocket("ws://" + location.host + "/chat");

    socket.onopen = function(event) {
      console.log("WebSocket 연결이 열렸습니다.");

      var mid = getId('mid').value; // 입력한 아이디 가져오기
      var roomId = getId('roomId').value; // 입력한 방번호 가져오기
      messageType = "ENTER";
      var message = "채팅방에 입장하였습니다.";
      var timestamp = getFormattedTimestamp();
      socket.send(JSON.stringify({ messageType:messageType, roomId:roomId, mid: mid,  message: message, timestamp: timestamp }));
    };



    // 받은 채팅을 띄우는 코드
    socket.onmessage = function(msg) {
        var data = JSON.parse(msg.data);
        var message = data.message;
        var timestamp = data.timestamp;
        var midd = data.mid;

        var sendRoomId = data.roomId;

        console.log("받은 채팅: ", msg);

        if(midd != mid.value){
            console.log("data.mid: " + midd);
            console.log("mid.value: " + mid.value);

            var messagesContainer = document.getElementById('chat-messages');

            // 새로운 메시지 컨테이너를 생성합니다.
            var messageDiv = document.createElement('div');
            messageDiv.className = 'message receiver-message';
            messageDiv.textContent = message;


            var timestampDiv = document.createElement('div');
            timestampDiv.className = 'timestamp_receiver';

            timestampDiv.textContent = timestamp;

            // 채팅 메시지를 추가합니다.
            messagesContainer.appendChild(messageDiv);
            messagesContainer.appendChild(timestampDiv);

            adjustMessageWidth(messageDiv);
            scrollToBottom();
        }
    };

    document.getElementById("message-input").addEventListener("keyup", function(event) {
               if (event.keyCode === 13) {
                 event.preventDefault();
                 sendMessage();
                 this.value = "";
               }
         });

          btnSend.onclick = function(){
            sendMessage();
          }

          // 보내는 채팅을 띄우는 코드
          function sendMessage() {
                 var input = document.getElementById('message-input');
                 var message = input.value.trim();

                  if (message !== '') {
                          var mid = getId('mid').value; // 입력한 아이디 가져오기
                          var roomId = getId('roomId').value; // 입력한 방번호 가져오기
                          console.log("roomId: ", roomId);
                          var timestamp = getCurrentDateTime(); // 현재 시각 가져오기
                          messageType = "TALK";
                          socket.send(JSON.stringify({ messageType:messageType, roomId:roomId, mid: mid,  message: message, timestamp: timestamp })); // 메시지와 시각을 JSON 형식으로 전송

                          var messagesContainer = document.getElementById('chat-messages');

                          // 새로운 메시지 컨테이너를 생성합니다.
                          var messageDiv = document.createElement('div');
                          messageDiv.className = 'message sender-message';
                          messageDiv.textContent = message;

                          var timestampDiv = document.createElement('div');
                          timestampDiv.className = 'timestamp_sender';
                          timestampDiv.textContent = timestamp; // ex) 2023. 10. 30. 오전 08:13

                          // 채팅 메시지를 추가합니다.
                          messagesContainer.appendChild(messageDiv);
                          messagesContainer.appendChild(timestampDiv);

                          input.value = '';
                          input.focus();

                          adjustMessageWidth(messageDiv);
                          scrollToBottom();

                          // AJAX 요청 보내기
                         var xhr = new XMLHttpRequest();
                         xhr.open("POST", "/popup/chat/saveChatLog", true);
                         xhr.setRequestHeader("Content-Type", "application/json");
                         xhr.onreadystatechange = function () {
                             if (xhr.readyState === XMLHttpRequest.DONE) {
                                 if (xhr.status === 200) {
                                     // 요청이 성공적으로 처리되었을 때 수행할 작업
                                     console.log("Chat log saved successfully!");
                                 } else {
                                     // 요청이 실패한 경우에 대한 처리
                                     console.error("Failed to save chat log!");
                                 }
                             }
                         };
                         xhr.send(JSON.stringify({message: messageDiv.textContent, timestamp: timestampDiv.textContent}));
                  }
          }

}

    function adjustMessageWidth(messageDiv) {
      // 메시지 너비 조정 로직은 동일하게 유지.
      var messageLength = messageDiv.textContent.length;
      var maxWidth = 300; // 최대 넓이 (예시 값)

      if (messageLength <= 10) {
        // 10글자 이하인 경우 최소 넓이를 가지도록 설정
        messageDiv.style.width = '100px';
      } else if (messageLength > 10 && messageLength <= 20) {
        // 11글자 ~ 20글자 사이인 경우 중간 넓이를 가지도록 설정
        messageDiv.style.width = '200px';
      } else {
        // 21글자 이상인 경우 최대 넓이를 가지도록 설정
        messageDiv.style.width = maxWidth + 'px';
      }
    }

    function scrollToBottom() {
      // 스크롤 조정 로직은 동일하게 유지.
      var chatMessages = document.getElementById("chat-messages");
      chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    // 시각을 가져오는 함수 1 : ex) 2023-09-12 02:55
    function getFormattedTimestamp() {
      var now = new Date();
      var year = now.getFullYear();
      var month = String(now.getMonth() + 1).padStart(2, '0');
      var day = String(now.getDate()).padStart(2, '0');
      var hours = String(now.getHours()).padStart(2, '0');
      var minutes = String(now.getMinutes()).padStart(2, '0');
      var formattedTimestamp = `${year}-${month}-${day} ${hours}:${minutes}`;
      return formattedTimestamp;
    }

    // 시각을 가져오는 함수 2 : ex) 2023. 10. 30. 오전 08:13
    function getCurrentDateTime() {
        const currentDate = new Date();
        const formattedDate = currentDate.toLocaleString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
        return formattedDate;
    }

    // 시각 포맷을 변경해주는 함수 : ex) 2023-09-12 02:55 > 2023. 09. 12. 오전 02:55
    function formatDate(inputDate) {
        // 주어진 문자열을 Date 객체로 변환
        const inputDateTime = new Date(inputDate);

        // 날짜와 시간을 추출
        const year = inputDateTime.getFullYear();
        const month = inputDateTime.getMonth() + 1;
        const day = inputDateTime.getDate();
        let hours = inputDateTime.getHours();
        const minutes = inputDateTime.getMinutes();

        // 오전과 오후 구분
        const amPm = hours >= 12 ? "오후" : "오전";

        // 시간을 12시간 형식으로 변경
        hours = hours % 12;
        hours = hours ? hours : 12; // 0 시간을 12로 표시

        // 원하는 포맷으로 날짜와 시간을 반환
        const formattedDate = `${year}. ${month}. ${day}. ${amPm} ${hours}:${minutes < 10 ? '0' : ''}${minutes}`;

        return formattedDate;
    }

//    var element = document.querySelector('.timestamp_table');
//    if (element) {
//        element.value = '결제시각: ' + getCurrentDateTime();
//    } else {
//        console.log('timestamp_table element not found!');
//    }
//
    var dataScript = document.querySelector('[data-script-save]');

    // 데이터를 읽어옴
    if(dataScript != null ){
        var saveFileName = dataScript.getAttribute('data-script-save');
        var originalFileName = dataScript.getAttribute('data-script-ori');

        console.log("저장명:", saveFileName);
        console.log("원본명:", originalFileName);

        //// 서버로부터 다운로드 URL을 가져옴.
        $("#downloadButton").click(function() {
                // var fileName = "example.txt"; // 실제 파일 이름으로 대체
                var downloadUrl = "/downloadFile/" + saveFileName;

                var link = document.createElement('a');
                link.href = "http://localhost:8080/downloadFile/" + saveFileName;
                link.download = originalFileName;
                link.click();

                console.log('다운 링크: ', link);
        });
    }

    window.onload = function() {
        scrollToBottom();
    }



