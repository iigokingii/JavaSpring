//full api function for sigin signup
function JwtCookie(cname, cvalue, exdays){
    let jwtToken = checkCookie();
    if(jwtToken===''){
        setCookie(cname, cvalue, exdays);
        console.log('Jwt setted in cookie:"jwt".');
        jwtToken = getCookie(cname);
    }
    console.log('Getted Jwt from cookie.');
    return jwtToken;
}

//удаление jwt по завершении сессии
document.getElementById("logout").addEventListener("click", function() {
    deleteCookie('jwt');
    console.log('jwt cookie deleted');
});

function deleteCookie(cookieName) {
    document.cookie = cookieName + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}
///////////////////////////////////

//setting jwttoken in cookie
function setCookie(cname, cvalue, exdays) {
    const d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    let expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

//getting jwttoken from cookieString
function getCookie(cname) {
    let name = cname + "=";
    let ca = document.cookie.split(';');
    for(let i = 0; i < ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) === ' ') {
        c = c.substring(1);
      }
      if (c.indexOf(name) === 0) {
        return c.substring(name.length, c.length);
      }
    }
    return "";
}

//check if the jwt token is setted 
function checkCookie() {
    let jwtToken = getCookie("jwt");
    if (jwtToken !== "") {
        return jwtToken
    } else {
      return '';
    }
  }