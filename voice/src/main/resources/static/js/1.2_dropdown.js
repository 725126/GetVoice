// 카테고리, 키워드, 정렬 용 드롭박스
function toggleDropdown() {
    var dropdown = document.getElementById("myDropdown");
    dropdown.classList.toggle("show");
  }
  
  function toggleCheckbox(event) {
    var checkbox = event.target.querySelector('input[type="checkbox"]');
    checkbox.checked = !checkbox.checked;
  
    var link = event.target;
    link.classList.toggle("selected");
  }
  
  window.onclick = function(event) {
    if (!event.target.matches('.dropbtn')) {
      var dropdowns = document.getElementsByClassName("dropdown-content");
      for (var i = 0; i < dropdowns.length; i++) {
        var openDropdown = dropdowns[i];
        if (openDropdown.classList.contains('show')) {
          openDropdown.classList.remove('show');
        }
      }
    }
  }
  
  function selectCategory(event, category) {
    event.preventDefault();
    const categories = document.querySelectorAll('.dropdown-content a');
    
    event.target.classList.add('selected');
    console.log('Selected Category:', category);
  }
