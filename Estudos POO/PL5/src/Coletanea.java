import java.util.Scanner;
public class Coletanea {
    private String titulo;
    private Livro[] livros;

    // Construtor
    public Coletanea(String titulo, int tamanho) {
        this.titulo = titulo;
        this.livros = new Livro[tamanho];
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < tamanho; i++) {
            System.out.print("Título do livro " + (i + 1) + ": ");
            String livroTitulo = scanner.nextLine();
            System.out.print("Autor do livro " + (i + 1) + ": ");
            String livroAutor = scanner.nextLine();
            System.out.print("Número de páginas do livro " + (i + 1) + ": ");
            int livroPaginas = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha
            livros[i] = new Livro(livroTitulo, livroAutor, livroPaginas);
        }
    }

    // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Livro[] getLivros() {
        return livros;
    }

    public void setLivros(Livro[] livros) {
        this.livros = livros;
    }

    // toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Coletanea{título='").append(titulo).append("', livros=[");
        for (Livro livro : livros) {
            sb.append("\n  ").append(livro.toString());
        }
        sb.append("\n]}");
        return sb.toString();
    }
}
