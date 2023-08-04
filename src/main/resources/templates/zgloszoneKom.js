var role = getCookie("rola");
if(role=="ADMIN")
document.getElementById('zgloszoneLink').style.display="flex";
else
console.log("test");


fetch('http://localhost:8080/Opinion/dajZgloszone', {
    method: 'GET',
    })
    .then((response) => {
        return response.json();
    })
    .then((data)=>{

        for(let i=0; i<data.length; i++){

            const template = document.querySelector('.templatka_zgloszony');
            const newTemplate = template.cloneNode(true);
            newTemplate.style.display="flex";

            newTemplate.id=data[i][0];



            var war =newTemplate.querySelector('.nazwaIdata');
            war.querySelector('.nazwa_uzytkownika').innerHTML=data[i][6];

            let date = new Date(data[i][1]);
            let formattedDate = new Intl.DateTimeFormat('pl-PL', { year: 
                'numeric', month: '2-digit', day: '2-digit' }).format(date);
            war.querySelector('.data_komentarza').innerHTML = formattedDate;



            newTemplate.querySelector('.komentarz_tresc').querySelector('.tresc_komentarza_tekst').innerHTML=data[i][2];

            // newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.liczba_like').innerHTML=data[i][3];
            // newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_like').onclick=function(){
            //     {dajLike(data[i][0]);};};

            newTemplate.querySelector('.opcje_pod_zgloszonym_komentarzem').querySelector('.przycisk_akceptuj_zgloszony_komentarz').onclick=function(){
                {akceptuj_komentarz(data[i][0]);};};

            newTemplate.querySelector('.opcje_pod_zgloszonym_komentarzem').querySelector('.przycisk_usun_zgloszony_komentarz').onclick=function(){
                {usun_komentarz(data[i][0]);};};


            

            const parentElement = document.querySelector('.content_right');
            parentElement.appendChild(newTemplate);
        }



    })

    function akceptuj_komentarz(par){
        fetch('http://localhost:8080/Opinion/akceptuj_komentarz/id='+par, {
            method: 'POST',
            })
            .then((response) => {
                if(response.status==200)
                {
                    var kom = document.getElementById(par);
                    kom.style.display="none";
                }
            })
    }

    function usun_komentarz(par){
        fetch('http://localhost:8080/Opinion/UsunOpinie/id='+par, {
            method: 'DELETE',
            })
            .then((response) => {
                if(response.status==200)
                {
                    var kom = document.getElementById(par);
                    kom.style.display="none";
                }
            })
    }