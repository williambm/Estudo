import Curso from "./curso.js";

class Aluno {
    //Classes não aceitam declaram em escopo de classe atributos com var, const ou let.    
    nome;
    nota;
    nivel;
    cursos;

    constructor(nome, nota, nivel){  
        //Exemplo de tratamento de exceção
        if(typeof nome !== "string") {
            throw new AlunoError("O nome deve ser uma string");
        }
        this.nome = nome;
        this.nota = nota;
        this.nivel = nivel;
        this.cursos = new Set();  // Inicializando um conjunto vazio
    }

//Não se utiliza a palavra reservada function para métodos em classes no JS -> function matricular(curso) #ERRADO
    matricular(curso) {
        this.cursos.add(curso);
    }

    quantificarValorGastoEmCursos(){
        let precoFinal = 0; //Inicializa a variavel porque o foreach não inicializa (daria erro)
        this.cursos.forEach((curso)=>{           
            precoFinal = precoFinal + curso.preco;            
        });
        console.log('R$'+precoFinal);
        return precoFinal;
    }

}

export default Aluno; //Exporta a classe de aluno, permitindo que outras classes a importem

// Classe de erro personalizada
class AlunoError extends Error {
    constructor(message) {
        super(message);
        this.name = "AlunoError"; // Nome da classe de erro
    }
}