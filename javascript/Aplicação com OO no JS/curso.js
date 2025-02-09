class Curso {
  nome;
  preco;

  constructor(nome, preco) {
    this.nome = nome;
    //Exemplo de tratamento de exceção.
    if (preco < 0) {
      throw new CursoError("O preço não pode ser negativo");
    } else {
      this.preco = preco;
    }
  }
}

export default Curso; //Exporta a classe de Curso, permitindo que outras classes a importem

class CursoError extends Error {
  constructor(message) {
    super(message);
    this.name = "CursoError"; // Nome da classe de erro
  }
}
