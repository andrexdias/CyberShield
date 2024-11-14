import java.util.ArrayList;
import java.util.Scanner;


public class LivroDemo {
        public static void main(String[] args) {
            ArrayList<Livro> livros = new ArrayList<>();
            Scanner scanner = new Scanner(System.in);
            int opcao;

            do {
                System.out.println("Menu:");
                System.out.println("1. Adicionar livro");
                System.out.println("2. Remover livro");
                System.out.println("3. Consultar livros");
                System.out.println("4. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha

                switch (opcao) {
                    case 1:
                        System.out.print("Título: ");
                        String titulo = scanner.nextLine();
                        System.out.print("Autor: ");
                        String autor = scanner.nextLine();
                        System.out.print("Número de páginas: ");
                        int numeroPaginas = scanner.nextInt();
                        scanner.nextLine(); // Consumir a nova linha
                        livros.add(new Livro(titulo, autor, numeroPaginas));
                        System.out.println("Livro adicionado com sucesso!");
                        break;
                    case 2:
                        System.out.print("Título do livro a remover: ");
                        String tituloRemover = scanner.nextLine();
                        livros.removeIf(livro -> livro.getTitulo().equalsIgnoreCase(tituloRemover));
                        System.out.println("Livro removido com sucesso!");
                        break;
                    case 3:
                        System.out.println("Livros registrados:");
                        for (Livro livro : livros) {
                            System.out.println(livro.getTitulo());
                        }
                        System.out.print("Digite o título do livro para ver detalhes: ");
                        String tituloConsultar = scanner.nextLine();
                        for (Livro livro : livros) {
                            if (livro.getTitulo().equalsIgnoreCase(tituloConsultar)) {
                                System.out.println(livro);
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } while (opcao != 4);

            scanner.close();
        }
    }

