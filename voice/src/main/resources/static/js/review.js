
function handleStarRating(starIndex) {
  const stars = document.querySelectorAll('.rating input[type="radio"]');
  
  stars.forEach((star, index) => {
    const label = document.querySelector(`label[for="star${index + 1}"]`);
    if (index <= starIndex) {
      label.style.color = 'gold';
    } else {
      label.style.color = '#ccc';
    }
  });
}

document.addEventListener('DOMContentLoaded', function() {
  const stars = document.querySelectorAll('.rating input[type="radio"]');
  stars.forEach((star, index) => {
    star.addEventListener('click', function() {
      handleStarRating(index);
    });
  });
});

