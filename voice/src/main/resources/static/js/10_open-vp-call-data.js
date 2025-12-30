window.onload = function() {
    var params = new URLSearchParams(window.location.search);
    var pid = params.get('pid');

    if(pid != null){
        // AJAX 요청을 보낼 URL 설정
        var url = "/popup/voiceport/open-vp-call-data?pid=" + pid;

        // AJAX 요청 보내기
        $.ajax({
            type: "GET",
            url: url,
            success: function (response) {
                // 성공적으로 데이터를 받았을 때의 동작
                console.log("게시물 데이터를 성공적으로 받았습니다");
                 if (response) {

                         var vpCtrlDTO = response.vpCtrlDTO;
                         console.log("vpCtrlDTO:", vpCtrlDTO);

                         $("#title").text(vpCtrlDTO.title);
                         $("#title").attr("title", vpCtrlDTO.title);

                         $("#nickname").text(vpCtrlDTO.nickname);
                         $("#sample-count").text(vpCtrlDTO.sampleCount);

                         $("#introduce").text(vpCtrlDTO.introduceContent);
                         $("#modify").text(vpCtrlDTO.modifyContent);
                         $("#refund").text(vpCtrlDTO.refundContent);

                         $("#for-1").text(formatCurrency(vpCtrlDTO.forProfitPrice) + '원');
                         $("#for-1000").text(formatCurrency(vpCtrlDTO.forProfitPrice*1000) + '원');
                         $("#non-1").text(formatCurrency(vpCtrlDTO.nonProfitPrice) + '원');
                         $("#non-1000").text(formatCurrency(vpCtrlDTO.nonProfitPrice*1000) + '원');

                         $(document).ready(function() {
                             $(".more-button").click(function() {
                                 showpopupchkw2(pid);
                             });
                         });

                         var voiceTypes = vpCtrlDTO.voiceTypes;

                         // 키워드 (음성유형)
                         var tableRow = document.getElementById('keywordsTable').getElementsByTagName('tr')[0];

                         for (var i = 0; i < voiceTypes.length && i < 8; i++) {
                             var keywordItem = document.createElement('div');
                             keywordItem.className = 'keyword-item';
                             keywordItem.textContent = voiceTypes[i];

                             var tableData = document.createElement('td');
                             tableData.appendChild(keywordItem);
                             tableRow.appendChild(tableData);
                         }

                         if (voiceTypes.length > 8) {
                             var moreItem = document.createElement('td');
                             moreItem.className = 'more';
                             moreItem.textContent = '…';
                             tableRow.appendChild(moreItem);
                         }

                         // 현재 구직 여부
                         var availability = vpCtrlDTO.availability;

                         // 동적으로 HTML 생성
                         var availabilityContainer = document.getElementById('availabilityContainer');

                         var availabilityDiv = document.createElement('div');
                         availabilityDiv.className = availability === 'possible' ? 'price-info-ok' : 'price-info-no';

                         var iconElement = document.createElement('i');
                         iconElement.className = availability === 'possible' ? 'fas fa-circle' : 'fas fa-square square-icon';

                         var textElement = document.createTextNode(availability === 'possible' ? ' 의뢰가능' : ' 의뢰불가');

                         availabilityDiv.appendChild(iconElement);
                         availabilityDiv.appendChild(textElement);

                         availabilityContainer.appendChild(availabilityDiv);

                         // profileDTO
                         var profileDTO = response.profileDTO;
                         console.log("profileDTO:", profileDTO);

                         $("#profile-img").attr("src", profileDTO.fileDownloadUri);

                         // pdfDTO
                         var pdfDTO = response.pdfDTO;
                         console.log("pdfDTO:", pdfDTO);

                         $("#downloadButton").click(function() {
                             var link = document.createElement('a');
                             link.href = pdfDTO.fileDownloadUri;
                             link.download = pdfDTO.originalFileName;
                             link.click();
                         });

                         // audioDTOList
                         var audioDTOList = response.audioDTOList;
                         console.log("audioDTOList:", audioDTOList);

                         var galleryContainer = $('#gallery2');
                         audioDTOList.forEach(function(audioDTO) {
                             var audioGalleryItem = createAudioGalleryItem(audioDTO);
                             galleryContainer.append(audioGalleryItem);
                         });

                         // urlDTOList
                         var urlDTOList = response.urlDTOList;
                         console.log("urlDTOList:", urlDTOList);

                         var galleryContainer = $('#gallery1');
                         urlDTOList.forEach(function(urlDTO) {
                             var galleryItem = createGalleryItem(urlDTO);
                             galleryContainer.append(galleryItem);
                         });

                         if(urlDTOList.length > audioDTOList.length){
                             $("#tabNum").text(1);
                         } else {
                            $("#tabNum").text(2);
                         }

                 } else {
                    console.error("서버에서 받은 데이터가 유효하지 않습니다.");
                 }
            },
            error: function (error) {
                // 오류 처리 로직
                console.error("데이터를 불러오는 중 오류가 발생했습니다:", error);
            }
        });
    }
}

function formatCurrency(number) {
    // 숫자가 1000 미만인 경우에는 그대로 반환
    if (number < 1000) {
        return number.toString();
    }

    // 숫자를 문자열로 변환하고 소수점을 기준으로 나눔
    var parts = number.toString().split(".");

    // 정수 부분에 콤마를 추가
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");

    // 다시 합치고 반환
    return parts.join(".");
}

// 영상샘플을 동적으로 생성하고 HTML에 추가하는 함수
function createGalleryItem(urlDTO) {
    var galleryItem = $('<div class="gallery-item"></div>');

    var link = $('<a></a>').attr('href', 'javascript:showYoutubePopup(\'' + urlDTO.youtubeUrl + '\')');
    var image = $('<img>').attr('src', urlDTO.thumbnailUrl).attr('alt', '이미지');
    link.append(image);

    var title = $('<h3></h3>').text(urlDTO.title).attr('title', urlDTO.title);

    var keywordList = $('<ul></ul>');
    urlDTO.selectedKeywords.forEach(function(keyword) {
        var keywordItem = $('<li></li>').text(keyword).attr('title', keyword);
        keywordList.append(keywordItem);
    });

    galleryItem.append(link, title, keywordList);

    return galleryItem;
}

// 음성샘플을 동적으로 생성하고 HTML에 추가하는 함수
function createAudioGalleryItem(audioDTO) {
    var galleryItem = $('<div class="gallery-item"></div>');

    var imageContainer = $('<div class="imageContainer"></div>');
    var imageDiv = $('<div class="imageDiv"></div>');

    var playImage = $('<img class="myImage">').attr('src', '/img/play.jpg').attr('alt', '이미지 5');

    var audio = $('<audio class="audio" id="audio"></audio>');
    var audioSource = $('<source>').attr('src', audioDTO.fileDownloadUri).attr('type', 'audio/mpeg');
    audio.append(audioSource);

    imageDiv.append(playImage, audio);

    var title = $('<h3></h3>').text(audioDTO.title).attr('title', audioDTO.title);

    var keywordList = $('<ul></ul>');
    audioDTO.selectedKeywords.forEach(function(keyword) {
        var keywordItem = $('<li></li>').text(keyword).attr('title', keyword);
        keywordList.append(keywordItem);
    });

    imageContainer.append(imageDiv, title, keywordList);
    galleryItem.append(imageContainer);

    return galleryItem;
}

// 음성샘플 - 재생이미지 클릭 시 샘플을 재생하는 함수
document.addEventListener("DOMContentLoaded", function() {
    $(document).on('click', '.myImage', function() {
        const image = $(this);
        const audio = image.siblings('.audio')[0];

        if (audio.paused) {
            image.attr('src', '/img/stop.jpg');
            audio.play();
        } else {
            image.attr('src', '/img/play.jpg');
            audio.pause();
        }
    });
});

// 보이스포트 더보기 클릭 시 실행 함수
 function showpopupchkw2(pid) {
     var popupUrl = "10.1_GV-F-KWDtl-10.1?pid=" + pid;
     window.open(popupUrl, "_blank", "width=540, height=820, left=600, top=50");
 }
