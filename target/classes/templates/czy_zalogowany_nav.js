function getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
  }
  
  if (getCookie("rola")) {
    document.getElementsByClassName("wylogowany")[0].style.display="none";
    document.getElementsByClassName("zalogowany")[0].style.display="";
  } else {
    document.getElementsByClassName("zalogowany")[0].style.display="none";
    document.getElementsByClassName("wylogowany")[0].style.display="";
  }