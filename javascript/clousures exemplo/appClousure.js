function contador(){
    let valor = 1;
    //para uma clousure funcionar é necessário que a função retorne uma função!!
    return function(){
        console.log(valor++);
    }
}

/**
 * representa um contador que pode ser incrementado.
 */
let contador1 = contador();
let contador2 = contador();

//Explicação: através de clousures, cada contador tem seu valor, mesmo que sejam chamados no mesmo escopo.
//isso ocorre porque cada chamada de contador() cria um novo escopo, e o valor é armazenado dentro desse escopo.
//clousures são funções que retornam outras funções, e que mantém o valor de variáveis dentro do escopo da função.
contador1();
contador1();
contador2();
contador1();