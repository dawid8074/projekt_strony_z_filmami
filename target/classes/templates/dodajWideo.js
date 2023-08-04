var role = getCookie("rola");
if(role=="ADMIN")
document.getElementById('zgloszoneLink').style.display="flex";
else
console.log("test");

document.querySelector("input[type='file']").onchange = function() {
    var file = this.files[0];
    var maxFileSize = 9685760; 
    if (file.size > maxFileSize) {
      alert("Plik jest zbyt duży. Maksymalna dopuszczalna wielkość to " + maxFileSize + " bajtów.");
      this.value = "";
    }
  };
  
  function dodaj_wideo(){
    var id_zalogowanego = getCookie("id_zalogowany");
    var file = document.querySelector("input[name=file]").files[0];
    var name = document.querySelector(".tytul_nowego_wideo").value;
    var opis = document.querySelector(".miejsce_edycji_komentarza").value;
    var id_autora = "id_zalogowanego_uzytkownika";
    var url = `http://localhost:8080/wideos/name=${name}/opis=${opis}/id_autora=${id_zalogowanego}`;
    var formData = new FormData();
    formData.append("file", file);
    fetch(url, {
        method: "PUT",
        body: formData
    }).then(response => {
        if(response.status === 200) {
            console.log("wyslano do bazy");
        }
        return response.json();
    })
    .then((data2)=>{
        console.log(data2);
        if(data2 && Object.keys(data2).length > 0)
        {
            window.location.href = "http://localhost:8080/wideosite.html?id_wideo="+data2;
        }
    });
  }





