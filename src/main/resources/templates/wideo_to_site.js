var urlParams = new URLSearchParams(window.location.search);
    var id_wideo;
    var id_autora_filmu;
    var list_like=[];
    var list_dislike=[];
    var czy_zalogowany=true;
    if (urlParams.has('id_wideo') ) {
        id_wideo = urlParams.get('id_wideo');
    } else {
        window.location.href = "http://localhost:8080/wszystkiewideo.html";
    }
    var role = getCookie("rola");
    var id_zalogowanego = getCookie("id_zalogowany");
    var nazwa_zalogowanego=getCookie("nazwa_zalogowanego");
    var czy_admin;
    var autor_wideo;
    if(role=="ADMIN")
    czy_admin=true;
    else
    czy_admin=false;
    console.log(id_zalogowanego);
    if(typeof id_zalogowanego=="undefined")
        {
            czy_zalogowany=false;
        }
    console.log(czy_zalogowany);


    //pobranie wideo
    fetch('http://localhost:8080/wideos/'+id_wideo) 
    .then(response => {
        if (response.ok) {
            return response.blob();
        }
        throw new Error('Nie znaleziono pliku wideo');
        })
        .then(blob => {
        // Pobieranie elementu video z HTML
        const video = document.getElementById('video');
        const source = document.getElementsByClassName('odtwarzacz')[0];
    
        // Tworzenie URL dla pliku wideo z blob
        const url = URL.createObjectURL(blob);
    
        // Ustawianie URL jako źródło dla elementu video
        source.src = url;
        video.load();
        })
        .catch(error => {
        console.error(error);
        });


    //Pobranie szczegolow wideo
    fetch('http://localhost:8080/wideos/NameAndOpisAndIdAndNameAndDate/Id='+id_wideo, {
        method: 'GET',
    })
    .then((response) => {
        return response.json();
    })
    .then((data)=>{
        const element2 = document.getElementsByClassName('nazwa_wideo')[0];
        element2.textContent = data[0];
        const element3 = document.getElementsByClassName('tresc_opisu_filmu')[0];
        element3.textContent = data[1];
        const element4 = document.getElementsByClassName('autor_wideo')[0];
        element4.textContent = data[2];
        autor_wideo=data[2];
        const element5 = document.getElementsByClassName('id_autora')[0];
        element5.textContent = data[3];
        id_autora_filmu=data[3];

        //sprawdzenie czy można wyswietlic przycisk edytuj wideo
        if(id_autora_filmu==id_zalogowanego || czy_admin)
        {
            const template = document.querySelector('.link_wideo_edit');
            template.style.display="flex";
            template.href+=id_wideo;
        }
        const element6 = document.getElementsByClassName('data_wideo')[0];
        let date2 = new Date(data[4]);
        let formattedDate = new Intl.DateTimeFormat('pl-PL', { year: 
            'numeric', month: '2-digit', day: '2-digit' }).format(date2);
        element6.textContent = formattedDate;

    })
    
    
    //pobranie porpozycji
    fetch('http://localhost:8080/wideos/Propozycje/'+id_wideo, {
        method: 'GET',
    })
    .then((response) => {
        return response.json();
    })
    .then((data)=>{
        for(let i=0; i<data.length; i++){

            const template = document.querySelector('.template_proponowane_jedno_wideo');
            const newTemplate = template.cloneNode(true);
            newTemplate.querySelector('a').style.display="flex";


            const link = newTemplate.querySelector('.popozycja_link');
            link.href +=data[i][0];

            const tytul = newTemplate.querySelector('.tytul_propozycji');
            tytul.innerHTML = data[i][1];

            const datatemp = newTemplate.querySelector('.data_propozycji');
            let date = new Date(data[i][2]);
            let formattedDate = new Intl.DateTimeFormat('pl-PL', { year: 
                'numeric', month: '2-digit', day: '2-digit' }).format(date);
            datatemp.innerHTML = formattedDate;


            const zdjecie = newTemplate.querySelector('.miniaturka2');
            zdjecie.src="data:image/png;base64," + data[i][3];
    

            const parentElement = document.querySelector('.proponowane_wideo');
            parentElement.appendChild(newTemplate);

        }

    })




    //czekanie na id autora wideo i obsluga komentarzy
    function waitForIdAutoraFilmu(){
        if(typeof id_autora_filmu != "undefined"){

            //edycja filmu



        }
        else{
            setTimeout(waitForIdAutoraFilmu, 30);
        }
    }
    waitForIdAutoraFilmu();

    
    var pobrano_komentarze="nie";
    let list_id_komentarze=[];

    fetch('http://localhost:8080/Opinion/dajOpinie/id='+id_wideo, {
        method: 'GET',
        })
        .then((response) => {
            return response.json();
        })
        .then((data)=>{
            for(let i=0; i<data.length; i++){

                if(data[i][8]!="zgloszono"){

                    const template = document.querySelector('.templatka_komentarz');
                    const newTemplate = template.cloneNode(true);
                    newTemplate.style.display="flex";
                    
                    newTemplate.id=data[i][0];
                    list_id_komentarze.push(data[i][0]);
                    
                    var war =newTemplate.querySelector('.nazwaIdata');
                    war.querySelector('.nazwa_uzytkownika').innerHTML=data[i][6];

                    let date = new Date(data[i][1]);
                    let formattedDate = new Intl.DateTimeFormat('pl-PL', { year: 
                        'numeric', month: '2-digit', day: '2-digit' }).format(date);
                    war.querySelector('.data_komentarza').innerHTML = formattedDate;

                    newTemplate.querySelector('.komentarz_tresc').querySelector('.tresc_komentarza_tekst').innerHTML=data[i][2];

                    var id_autora_opinii=data[i][7];
                    if(czy_admin || id_zalogowanego==id_autora_opinii)
                    {
                        var war2=newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.przyciski')
                        war2.style.visibility="visible";
                        war2.querySelector(".Przycisk_edycji_komentarza").onclick= function(){
                            {edytuj_komentarz(data[i][0]);};};
                        war2.querySelector(".Przycisk_usuniecia_komentarza").onclick=function(){
                            {usun_komentarz(data[i][0]);};};
                        newTemplate.querySelector('.edycja_komentarza').querySelector('.Przycisk_zapisania_edycji_komentarza').onclick=function(){
                            {zapisz_edytowany_komentarz(data[i][0]);};};
        
                    }

                    newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.liczba_like').innerHTML=data[i][3];
                    newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_like').onclick=function(){
                        {dajLike(data[i][0]);};};
 

                    newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.liczba_dislike').innerHTML=data[i][4];
                    newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_dislike').onclick=function(){
                        {dajDisLike(data[i][0]);};};

                    //dopisać obsługę przycisku

                    newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_zglos').onclick=function(){
                        {zglos_komentarz(data[i][0]);};};
                    if(data[i][8]=="zweryfikowano"){
                        newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_zglos').style.display="none";
                    } 

                    const parentElement = document.querySelector('.komentarze');
                    parentElement.appendChild(newTemplate);
                }

            }
            pobrano_komentarze="tak";
    })

    function waitForkomentarze(){
        if(pobrano_komentarze=="tak"){
            list_like=new Array(list_id_komentarze.length);
            list_dislike=new Array(list_id_komentarze.length);
                for(let list_id_komentarz of list_id_komentarze )
                {
                    
                    // id komentarza id zalogowanego
                    fetch('http://localhost:8080/Opinion/dajOpinieLike/id_opinion='+list_id_komentarz+'/id_user='+id_zalogowanego, {
                        method: 'GET',
                    })
                    .then((response2)=>{
                        if (response2.status === 200) {
                            return response2.json();
                        }
                    })
                    .then((data2)=>{
                        let id_do_listy=list_id_komentarze.indexOf(list_id_komentarz);
                        if(data2 && Object.keys(data2).length > 0)
                        {
                            if(data2['like'])
                            {
                                list_like[id_do_listy]=true;
                                var komObj=document.getElementById(list_id_komentarz);
                                komObj.querySelector('.opcje_pod_komentarzem').querySelector('.liczba_like').style.color="#b042f0";
                                komObj.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_like').style.backgroundColor="#b042f0";
                            } else{
                                list_like[id_do_listy]=false;
                            }
                            if(data2['dislike'])
                            {
                                list_dislike[id_do_listy]=true;
                                var komObj=document.getElementById(list_id_komentarz);
                                komObj.querySelector('.opcje_pod_komentarzem').querySelector('.liczba_dislike').style.color="#b042f0";
                                komObj.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_dislike').style.backgroundColor="#b042f0";
                            } else{
                                list_dislike[id_do_listy]=false;
                            }
                        }
                        else{
                            list_like[id_do_listy]=false;
                            list_dislike[id_do_listy]=false;

                        }
                    })
                }

        }
        else{
            setTimeout(waitForkomentarze, 80);
        }
    }
    waitForkomentarze();
    



    function dodajKomentarz(){
        
        if(czy_zalogowany)
        {
            console.log(id_zalogowanego);
        var tresc_nowego_komentarza=document.querySelector('.nowy_komentarz_tresc').value;
        document.querySelector('.nowy_komentarz_tresc').value="";
        fetch('http://localhost:8080/Opinion/dodajOpinie', {
            method: 'POST',
            body: JSON.stringify({
                "id_uzytkownik": id_zalogowanego,
                "id_wideo": id_wideo,
                "tekst":tresc_nowego_komentarza,
            }),
            headers: { 'Content-Type': 'application/json' }
            })
            .then((response) => {
                return response.json();
            })
            .then((data)=>{
                list_id_komentarze.push(data);
                list_dislike.push(false);
                list_like.push(false);

                    const template = document.querySelector('.templatka_komentarz');
                    const newTemplate = template.cloneNode(true);
                    newTemplate.style.display="flex";
                    newTemplate.id=data;
                    
                    var war =newTemplate.querySelector('.nazwaIdata');
                    war.querySelector('.nazwa_uzytkownika').innerHTML=nazwa_zalogowanego;

                    let date = new Date();
                    let dateString = date.toLocaleDateString("pl-PL", {
                        day: "2-digit",
                        month: "2-digit",
                        year: "numeric"
                    });
                    war.querySelector('.data_komentarza').innerHTML = dateString;

                    newTemplate.querySelector('.komentarz_tresc').querySelector('.tresc_komentarza_tekst').innerHTML=tresc_nowego_komentarza;

                    var war2=newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.przyciski')
                    war2.style.visibility="visible";
                    war2.querySelector(".Przycisk_edycji_komentarza").onclick= function(){
                        {edytuj_komentarz(data);};};
                    war2.querySelector(".Przycisk_usuniecia_komentarza").onclick=function(){
                        {usun_komentarz(data);};};
                    newTemplate.querySelector('.edycja_komentarza').querySelector('.Przycisk_zapisania_edycji_komentarza').onclick=function(){
                        {zapisz_edytowany_komentarz(data);};};
    
                

                    newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.liczba_like').innerHTML="0";
                    newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_like').onclick=function(){
                        {dajLike(data);};};
 

                    newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.liczba_dislike').innerHTML="0";
                    newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_dislike').onclick=function(){
                        {dajDisLike(data);};};

                    //dopisać obsługę przycisku
                    newTemplate.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_zglos').onclick=function(){
                        {zglos_komentarz(data);};};

                    const parentElement = document.querySelector('.komentarze');
                    parentElement.prepend(newTemplate);



            })
        }


    }


    function edytuj_komentarz(par) {
        var doc=document.getElementById(par);
        var wartosc=doc.querySelector('.komentarz_tresc').querySelector('.tresc_komentarza_tekst').innerHTML;
        doc.querySelector('.komentarz_tresc').style.display="none";
        doc.querySelector('.edycja_komentarza').style.display="flex";
        doc.querySelector('.edycja_komentarza').querySelector('.miejsce_edycji_komentarza').innerHTML=wartosc;

        
    }
    function zapisz_edytowany_komentarz(par){
        var doc=document.getElementById(par);
        var wartosc=doc.querySelector('.edycja_komentarza').querySelector('.miejsce_edycji_komentarza').value;



        fetch('http://localhost:8080/Opinion/edytujTekstOpinii', {
        method: 'PUT',
                body: JSON.stringify({
            "id_opinion":par,
            "tekst":wartosc
        }),
        headers: { 'Content-Type': 'application/json' }
        })
        .then((response) => {
            if(response.status === 200){
                doc.querySelector('.komentarz_tresc').style.display="flex";
                doc.querySelector('.edycja_komentarza').style.display="none";
                doc.querySelector('.komentarz_tresc').querySelector('.tresc_komentarza_tekst').innerHTML=wartosc;
                doc.querySelector('.opcje_pod_komentarzem').querySelector('.przycisk_zglos').style.display="flex";
                doc.querySelector('.komunikat_edycji').style.display="flex";
                setTimeout(() => {  doc.querySelector('.komunikat_edycji').style.display="none";
            }, 3000);
            }
        })
    }

    function usun_komentarz(par) {
        if (confirm("Czy na pewno chcesz usunąć?")) {
            
            fetch('http://localhost:8080/Opinion/UsunOpinie/id='+par, {
                method: 'DELETE',
            })
            .then((response) => {
                if(response.status === 200){
                    var doc2=document.getElementById(par);
                    doc2.style.display="none";
                }
            })
            } 
    }
    function zglos_komentarz(par)
    {
        if(czy_zalogowany)
        {
        fetch('http://localhost:8080/Opinion/zglosOpinie/id='+par, {
            method: 'POST',
            })
            .then((response) => {
                if(response.status==200)
                {

                    var doc=document.getElementById(par);
                    doc.querySelector(".opcje_pod_komentarzem").querySelector('.zgloszono_komunikat').style.display="flex";
                    doc.querySelector(".opcje_pod_komentarzem").querySelector('.przycisk_zglos').style.display="none";
                    setTimeout(() => {  
                        
                        
                        doc.querySelector(".opcje_pod_komentarzem").querySelector('.zgloszono_komunikat').style.display="none";
                    }, 3000);
                    doc.querySelector('.zgloszono_komunikat')

                }
                
            })
        }

    }

    function dajLike(par){
        if(czy_zalogowany)
        {
        var indexOpinii= list_id_komentarze.indexOf(par);
        if(list_dislike[indexOpinii])
        {
            //funZabierzDislike(par);
            list_dislike[indexOpinii]=false;
            var doc=document.getElementById(par);
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.przycisk_dislike').style.backgroundColor="#9571ce";
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_dislike').style.color="#9571ce";
            var przedKlik=doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_dislike').innerHTML;
            przedKlik=parseInt(przedKlik)-1;
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_dislike').innerHTML=przedKlik; 
        }

        if(list_like[indexOpinii])
        {
            //zabierz like
            funZabierzLike(par);
            list_like[indexOpinii]=false;

            
        }else{
            //daj nowego like
            funDodajLike(par);
            list_like[indexOpinii]=true;
            

        }
        }
    }
    function dajDisLike(par){
        if(czy_zalogowany)
        {
        var indexOpinii= list_id_komentarze.indexOf(par);
        if(list_like[indexOpinii])
        {
            //funZabierzLike(par);
            list_like[indexOpinii]=false;
            var doc=document.getElementById(par);
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.przycisk_like').style.backgroundColor="#9571ce";
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_like').style.color="#9571ce";
            var przedKlik=doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_like').innerHTML;
            przedKlik=parseInt(przedKlik)-1;
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_like').innerHTML=przedKlik; 

        }

        if(list_dislike[indexOpinii])
        {
            //zabierz dislike
            funZabierzDislike(par);
            list_dislike[indexOpinii]=false;

            
        }else{
            //daj nowego dislike
            funDodajDislike(par);
            list_dislike[indexOpinii]=true;
            

        }
        }


    }



    function funZabierzLike(par2){
        var doc=document.getElementById(par2);
        doc.querySelector(".opcje_pod_komentarzem").querySelector('.przycisk_like').style.backgroundColor="#9571ce";
        doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_like').style.color="#9571ce";
        var przedKlik=doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_like').innerHTML;
        przedKlik=parseInt(przedKlik)-1;
        doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_like').innerHTML=przedKlik; 

        fetch('http://localhost:8080/Opinion/zabierzLikeOpinni/opinion='+par2+'/user='+id_zalogowanego, {
            method: 'POST',
            })
            .then((response) => {
                if(response.status=200)
                    console.log("poszło");
            })

    }

    function funDodajLike(par2){
        var doc=document.getElementById(par2);
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.przycisk_like').style.backgroundColor="#b042f0";
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_like').style.color="#b042f0";
            var przedKlik=doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_like').innerHTML;
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_like').innerHTML=(parseInt(przedKlik)+1);


            fetch('http://localhost:8080/Opinion/dodajlikeOpinni/opinion='+par2+'/user='+id_zalogowanego, {
                method: 'POST',
                })
                .then((response) => {
                    if(response.status=200)
                        console.log("poszło");
                })
    }

    function funZabierzDislike(par2){
        var doc=document.getElementById(par2);
        doc.querySelector(".opcje_pod_komentarzem").querySelector('.przycisk_dislike').style.backgroundColor="#9571ce";
        doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_dislike').style.color="#9571ce";
        var przedKlik=doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_dislike').innerHTML;
        przedKlik=parseInt(przedKlik)-1;
        doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_dislike').innerHTML=przedKlik; 

        fetch('http://localhost:8080/Opinion/zabierzDislikeOpinni/opinion='+par2+'/user='+id_zalogowanego, {
            method: 'POST',
            })
            .then((response) => {
                if(response.status=200)
                    console.log("poszło");
            })
    }
    
    function funDodajDislike(par2){
        var doc=document.getElementById(par2);
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.przycisk_dislike').style.backgroundColor="#b042f0";
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_dislike').style.color="#b042f0";
            var przedKlik=doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_dislike').innerHTML;
            doc.querySelector(".opcje_pod_komentarzem").querySelector('.liczba_dislike').innerHTML=(parseInt(przedKlik)+1);


            fetch('http://localhost:8080/Opinion/dodajdislikeOpinni/opinion='+par2+'/user='+id_zalogowanego, {
                method: 'POST',
                })
                .then((response) => {
                    if(response.status=200)
                        console.log("poszło");
                })
    }