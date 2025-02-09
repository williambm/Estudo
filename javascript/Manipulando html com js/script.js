//Captura o paragrafoo pelo id
var paragrafo = document.getElementById("paragrafo");
//rescreve o paragrafo todo ( ou seja todo o elemento da tag p id paragrafo)
paragrafo.innerHTML = "Nova frase!!!";
//Coloca dados no final do elemento
paragrafo.append("123 colocado ao final do elemento");

document.getElementById("botao").onclick = ()=>{
    //Cria um elemento link quando apertar o botão
    var linkNovo = document.createElement("a");
    //atribui url ao link
    linkNovo.href = "https://www.google.com";
    //atribui texto ao link
    linkNovo.innerText = "Link para o Google";
    //apresenta no final do body
    document.body.appendChild(linkNovo);
}
