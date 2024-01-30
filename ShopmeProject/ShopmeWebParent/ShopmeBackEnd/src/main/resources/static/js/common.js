$(document).ready(function(){
    $("#logoutLink").on("click", function(e){
        e.preventDefault();
        document.logoutForm.submit();
    });
    customizeDropDownMenu()
});
 function customizeDropDownMenu(){
     $(".navbar .dropdown").hover(
         function(){ //for slidedown
             $(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
         },
         function(){ //for slideUp
             $(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
         }
     )

     $(".dropdown > a").click(function(){
         location.href=this.href;
     });
 }