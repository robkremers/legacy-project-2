//Scroll to item
$('.scrollTo').click(function(){
  $('html, body').animate({
    scrollTop: $( $(this).attr('href') ).offset().top -225
  }, 800);
  return false;
});


//Make a whole table row clickable
$(document).ready(function($) {
  $(".clickable-row-href").click(function() {
    window.location = $(this).data("href");
  });
});

//Make a whole table row clickable
$(document).ready(function($) {
  $(".clickable-row-target").click(function() {
    window.location = $(this).data("target");
  });
});

//Get the button
var mybutton = document.getElementById("myBtn");

// When the user scrolls down 20px from the top of the document, show the button
window.onscroll = function() {scrollFunction()};

function scrollFunction() {
  if (mybutton) {
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
      mybutton.style.display = "block";
    } else {
      mybutton.style.display = "none";
    }
  }
}

// When the user clicks on the button, scroll to the top of the document
function topFunction() {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}


//hide show side bar
function closeNav() {
  document.getElementById("mySidenav").style.marginLeft = "-25%";
  document.getElementById("main").style.width = "99%";
  document.getElementById("close").style.display = "none";
  document.getElementById("open").style.display ="block";
}


function openNav() {
  document.getElementById("mySidenav").style.marginLeft = "0px";
  document.getElementById("main").style.width = "75%";
  document.getElementById("close").style.display = "block";
  document.getElementById("open").style.display ="none";

}
