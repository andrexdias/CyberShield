import java.util.ArrayList;

public class Colecao {
    private String nomeProprietario;
    private ArrayList<Livro> livros;
    private ArrayList<Coletanea> coletaneas;

    // Construtor
    public Colecao(String nomeProprietario) {
        this.nomeProprietario = nomeProprietario;
        this.livros = new ArrayList<>();
        this.coletaneas = new ArrayList<>();
    }

    // Getters e Setters
    public String getNomeProprietario() {
        return nomeProprietario;
    }

    public void setNomeProprietario(String nomeProprietario) {
        this.nomeProprietario = nomeProprietario;
    }

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public void setLivros(ArrayList<Livro> livros) {
        this.livros = livros;
    }

    public ArrayList<Coletanea> getColetaneas() {
        return coletaneas;
    }

    public void setColetaneas(ArrayList<Coletanea> coletaneas) {
        this.coletaneas = coletaneas;
    }

    // Métodos para adicionar elementos
    public void adicionarLivro(Livro livro) {
        livros.add(livro);
    }

    public void adicionarColetanea(Coletanea coletanea) {
        coletaneas.add(coletanea);
    }

    // Método para editar propriedades de Livros
    public void editarLivro(String titulo, String novoTitulo, String novoAutor, int novoNumeroPaginas) {
        for (Livro livro : livros) {
            if (livro.getTitulo().equalsIgnoreCase(titulo)) {
                livro.setTitulo(novoTitulo);
                livro.setAutor(novoAutor);
                livro.setNumeroPaginas(novoNumeroPaginas);
                break;
            }
        }
    }

    // toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Colecao{nomeProprietario='").append(nomeProprietario).append("', livros=[");
        for (Livro livro : livros) {
            sb.append("\n  ").append(livro.toString());
        }
        sb.append("\n], coletaneas=[");
        for (Coletanea coletanea : coletaneas) {
            sb.append("\n  ").append(coletanea.toString());
        }
        sb.append("\n]}");
        return sb.toString();
    }
}
