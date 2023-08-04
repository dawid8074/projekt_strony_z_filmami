var przycisk = document.getElementById("przyciskWyszukaj");
var input = document.getElementById("wyszukiwarkaInput");
przycisk.addEventListener("click", function() {
    var wyszukane = input.value;
    if(wyszukane){
        var url = "http://localhost:8080/wszystkiewideo.html?Page=1&Search=" + wyszukane;
        window.location.href = url;
    }
});