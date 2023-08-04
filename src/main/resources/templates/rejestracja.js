
const form = document.getElementById('rejestracja');
form.addEventListener('submit', (event) => {
    event.preventDefault();
    const formData = new FormData(form);
    if(form.haslo.value==form.haslo2.value)
    {
        fetch('http://localhost:8080/User/dodaj', {
            method: 'POST',
            body: JSON.stringify({
                "login":form.login.value,
                "haslo":form.haslo.value,
                "emailAdress":form.emailAdress.value,
            }),
            headers: { 'Content-Type': 'application/json' }
        })
        .then((response) => {
            if(response.status === 200){
                console.log('tak. status to 200');
                document.getElementsByClassName("error_rejestracja")[0].style.display="block";
                document.getElementsByClassName("error_rejestracja")[0].style.color="green";
                document.getElementsByClassName("error_rejestracja")[0].textContent='Pomyślnie zarejestrowano';
                setTimeout(() => {  window.location.href = "http://localhost:8080/login";
                }, 3000);
                
            }
            return response.json();
        })
        .then((data) => {
        
            if(data.message && data.message !== 'undefined'){
                document.getElementsByClassName("error_rejestracja")[0].style.display="block";
                document.getElementsByClassName("error_rejestracja")[0].style.color="crimson";
                document.getElementsByClassName("error_rejestracja")[0].textContent=data.message;
            }
            
        })
        .catch((error) => {
            
            console.error(error);
        });
    }
    else
    {
        document.getElementsByClassName("error_rejestracja")[0].style.display="block";
        document.getElementsByClassName("error_rejestracja")[0].style.color="crimson";
        document.getElementsByClassName("error_rejestracja")[0].textContent='Podane hasła różnią się od siebie';
    }
});




// wypiwanie imion z resposna
// const form = document.getElementById('rejestracja');
// form.addEventListener('submit', (event) => {
//   event.preventDefault();
//   const formData = new FormData(form);
//   fetch('http://localhost:8080/User/dajWszystkich', {
//     method: 'GET',
//   })
//   .then((response) => {
//     return response.json();
//   })
//   .then((data)=>{
//     const names = data.map((user) => {
//         return user.username;
//         });
//         console.log(names);
//     // console.log(emaile);
//     // console.log(JSON.stringify(data,null,2))
//   })
//   .catch((error) => {
//     console.error(error);
//   });
// });


// wypisywanie całego jsona z backendu
// const form = document.getElementById('rejestracja');
// form.addEventListener('submit', (event) => {
//   event.preventDefault();
//   const formData = new FormData(form);
//   fetch('http://localhost:8080/User/dajWszystkich', {
//     method: 'GET',
//   })
//   .then((response) => {
//     return response.json();
//   })
//   .then((data)=>{
//     // const emaile=data.filter((emailAddress) =>{
//     //     return user.role!="ADMIN";
        
//     // })
//     // console.log(emaile);
//     console.log(JSON.stringify(data,null,2))
//   })
//   .catch((error) => {
//     console.error(error);
//   });
// });