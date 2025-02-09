import Aluno from "./aluno.js";
import Curso from "./curso.js";

try {
  //Como esse arquivo não é uma classe eu devo utilizar palçavras chave como var, let ou const para declarar variáveis!
  let aluno1 = new Aluno("bianca", 10, "avançado");
  let aluno2 = new Aluno("Mariana", 7, "avançado");

  let matematica = new Curso("Matemática", 200.23);
  let direito = new Curso("Direito", 300.73);
  let administração = new Curso("Administração", 300.73);
  //Tentativa de colocar curso com preço negativo - teste de catch
  //let historia = new Curso("Administração", -300.73);

  aluno1.matricular(direito);
  aluno1.matricular(administração);
  aluno2.matricular(matematica);

  console.log(aluno1);
  aluno1.quantificarValorGastoEmCursos();
} catch (error) {
  //No js não existem multiplos catch, apenas um catch para todos os erros !!!
  switch (error.name) {
    case "AlunoError":
      console.log("Erro no aluno: " + error.message);
      break;
    case "CursoError":
      console.log("Erro no curso: " + error.message);
      break;
    default:
      console.log("Erro desconhecido: " + error.message);
      break;
  }
} finally {
  console.log("Finalizando o programa");
}
