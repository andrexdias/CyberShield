import java.util.ArrayList;
import java.util.Scanner;

public class ColecaoDemo {
    public static void main(String[] args) {
        ArrayList<Colecao> colecoes = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Menu:");
            System.out.println("1. Adicionar coleção");
            System.out.println("2. Remover coleção");
            System.out.println("3. Consultar coleções");
            System.out.println("4. Adicionar livro a uma coleção");
            System.out.println("5. Adicionar coletânea a uma coleção");
            System.out.println("6. Editar livro em uma coleção");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    System.out.print("Nome do proprietário da coleção: ");
                    String nomeProprietario = scanner.nextLine();
                    colecoes.add(new Colecao(nomeProprietario));
                    System.out.println("Coleção adicionada com sucesso!");
                    break;
                case 2:
                    System.out.print("Nome do proprietário da coleção a remover: ");
                    String nomeRemover = scanner.nextLine();
                    colecoes.removeIf(colecao -> colecao.getNomeProprietario().equalsIgnoreCase(nomeRemover));
                    System.out.println("Coleção removida com sucesso!");
                    break;
                case 3:
                    System.out.println("Coleções registradas:");
                    for (Colecao colecao : colecoes) {
                        System.out.println(colecao.getNomeProprietario());
                    }
                    System.out.print("Digite o nome do proprietário da coleção para ver detalhes: ");
                    String nomeConsultar = scanner.nextLine();
                    for (Colecao colecao : colecoes) {
                        if (colecao.getNomeProprietario().equalsIgnoreCase(nomeConsultar)) {
                            System.out.println(colecao);
                        }
                    }
                    break;
                case 4:
                    System.out.print("Nome do proprietário da coleção: ");
                    String nomeColecaoLivro = scanner.nextLine();
                    for (Colecao colecao : colecoes) {
                        if (colecao.getNomeProprietario().equalsIgnoreCase(nomeColecaoLivro)) {
                            System.out.print("Título do livro: ");
                            String tituloLivro = scanner.nextLine();
                            System.out.print("Autor do livro: ");
                            String autorLivro = scanner.nextLine();
                            System.out.print("Número de páginas do livro: ");
                            int paginasLivro = scanner.nextInt();
                            scanner.nextLine(); // Consumir a nova linha
                            colecao.adicionarLivro(new Livro(tituloLivro, autorLivro, paginasLivro));
                            System.out.println("Livro adicionado à coleção com sucesso!");
                        }
                    }
                    break;
                case 5:
                    System.out.print("Nome do proprietário da coleção: ");
                    String nomeColecaoColetanea = scanner.nextLine();
                    for (Colecao colecao : colecoes) {
                        if (colecao.getNomeProprietario().equalsIgnoreCase(nomeColecaoColetanea)) {
                            System.out.print("Título da coletânea: ");
                            String tituloColetanea = scanner.nextLine();
                            System.out.print("Número de livros na coletânea: ");
                            int tamanhoColetanea = scanner.nextInt();
                            scanner.nextLine(); // Consumir a nova linha
                            colecao.adicionarColetanea(new Coletanea(tituloColetanea, tamanhoColetanea));
                            System.out.println("Coletânea adicionada à coleção com sucesso!");
                        }
                    }
                    break;
                case 6:
                    System.out.print("Nome do proprietário da coleção: ");
                    String nomeColecaoEditar = scanner.nextLine();
                    for (Colecao colecao : colecoes) {
                        if (colecao.getNomeProprietario().equalsIgnoreCase(nomeColecaoEditar)) {
                            System.out.print("Título do livro a editar: ");
                            String tituloEditar = scanner.nextLine();
                            System.out.print("Novo título: ");
                            String novoTitulo = scanner.nextLine();
                            System.out.print("Novo autor: ");
                            String novoAutor = scanner.nextLine();
                            System.out.print("Novo número de páginas: ");
                            int novoNumeroPaginas = scanner.nextInt();
                            scanner.nextLine(); // Consumir a nova linha
                            colecao.editarLivro(tituloEditar, novoTitulo, novoAutor, novoNumeroPaginas);
                            System.out.println("Livro editado com sucesso!");
                        }
                    }
                    break;
                case 7:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 7);

        scanner.close();
    }
}
