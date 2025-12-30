// 페이지 로딩 후 실행
document.addEventListener("DOMContentLoaded", function() {
    var audioTabBtn = document.getElementById("audio-tab-btn");
    var tabNum = document.getElementById("tabNum").textContent;

     scrollToBottomOfGallery();

    if (tabNum == 2) { // 음성샘플, 영상샘플 중 어느 탭을 보일 지 판단
        audioTabBtn.click(); // 보여져야하는 탭을 클릭
    }
});

// "gallery" 클래스를 가진 요소의 스크롤을 최하단으로 이동시키는 함수
function scrollToBottomOfGallery() {
    var gallery = document.querySelector('.gallery');
    gallery.scrollTop = gallery.scrollHeight;
}

function changeTab(evt, tabName) {
    var i, tabContent, tabButton;
    tabContent = document.getElementsByClassName("gallery");
    for (i = 0; i < tabContent.length; i++) {
      tabContent[i].style.display = "none";
    }
    tabButton = document.getElementsByClassName("tab-button");
    for (i = 0; i < tabButton.length; i++) {
      tabButton[i].className = tabButton[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "flex";
    evt.currentTarget.className += " active";
  }


document.addEventListener("DOMContentLoaded", function() { // 음성샘플 - 재생이미지 클릭 시 샘플 재생
    const imageContainers = document.querySelectorAll('.imageDiv');

    imageContainers.forEach(container => {
        const image = container.querySelector('.myImage');
        const audio = container.querySelector('.audio');

        image.addEventListener('click', () => {
            if (audio.paused) {
                image.src = "/img/stop.jpg";
                audio.play();
            } else {
                image.src = "/img/play.jpg";
                audio.pause();
            }
        });
    });
    
});
