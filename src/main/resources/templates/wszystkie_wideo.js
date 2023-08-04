document.addEventListener("DOMContentLoaded", function(event){
    
    var urlParams = new URLSearchParams(window.location.search);
    var Search="";
    if (urlParams.has('Search')) {
        Search = urlParams.get('Search');
    } else {
        var Search="";
    }
    if (urlParams.has('Page')) {
        var page = urlParams.get('Page');
    } else {
        var page=1;
    }

    fetch('http://localhost:8080/wideos/page='+page+'/Szukane='+Search, {
        method: 'GET',
    })
    .then((response) => {
        return response.json();
    })
    .then((data)=>{

        let content = data.content;
        // console.log(data.totalPages);
        
        for(let i=0; i<content.length; i++){

            const template = document.querySelector('.template_jeden_film');
            const newTemplate = template.cloneNode(true);
            newTemplate.querySelector('a').style.display="flex";
        
        
            const id = newTemplate.querySelector('.id');
            id.innerHTML = content[i][0];
            const tytul = newTemplate.querySelector('.tytul_propozycji');
            tytul.innerHTML = content[i][1];
            const datatemp = newTemplate.querySelector('.data_propozycji');
            let date = new Date(content[i][2]);
            let formattedDate = new Intl.DateTimeFormat('pl-PL', { year: 
                'numeric', month: '2-digit', day: '2-digit' }).format(date);
            datatemp.innerHTML = formattedDate;
            const nazwa = newTemplate.querySelector('.autor_wszystkie_wideo');
            nazwa.innerHTML = content[i][3];

            const zdjecie = newTemplate.querySelector('.photo_mini');
            zdjecie.src="data:image/png;base64," + content[i][4];
        
            const link = newTemplate.querySelector("a[href='http://localhost:8080/wideosite.html']");
            link.href +="?id_wideo="+content[i][0];

        
            const parentElement = document.querySelector('.wszystkie_filmy');
            parentElement.appendChild(newTemplate);
        }

        

        

        for(let j=1; j<=data.totalPages; j++)
        {
            const template = document.querySelector('a.change');
            const newTemplate = template.cloneNode(true);
            newTemplate.style.display="flex";

            if(j==page)
            {
                newTemplate.className = "disabled";
                newTemplate.innerHTML=j
                newTemplate.href +="?Page="+j+"&Search="+Search;
            }
            else{
                newTemplate.className = "active";
                newTemplate.innerHTML=j;
                newTemplate.href +="?Page="+j+"&Search="+Search;
            }
          
            const parentElement = document.querySelector('.paginacja');
            parentElement.appendChild(newTemplate);
        }
        

    })















})
