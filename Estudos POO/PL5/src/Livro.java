public class Livro {
    private String titulo;
    private String autor;
    private int numeroPaginas;
    private int paginaAtual;

    // Construtor
    public Livro(String titulo, String autor, int numeroPaginas) {
        this.titulo = titulo;
        this.autor = autor;
        this.numeroPaginas = numeroPaginas;
        this.paginaAtual = 0; // Inicializa a página atual como zero
    }

    // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public int getPaginaAtual() {
        return paginaAtual;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public void setPaginaAtual(int paginaAtual) {
        this.paginaAtual = paginaAtual;
    }

    // Função para adicionar páginas lidas
    public String adicionarPaginasLidas(int paginasLidas) {
        if (paginaAtual + paginasLidas > numeroPaginas) {
            return "Erro: O número de páginas lidas excede o total de páginas do livro.";
        } else {
            paginaAtual += paginasLidas;
            return "Você leu " + paginasLidas + " páginas. Página atual: " + paginaAtual;
        }
    }

    // Função para regredir páginas lidas
    public String regredirPaginasLidas(int paginasRegredidas) {
        if (paginaAtual - paginasRegredidas < 0) {
            return "Erro: Não é possível regredir mais páginas do que já foram lidas.";
        } else {
            paginaAtual -= paginasRegredidas;
            return "Você regrediu " + paginasRegredidas + " páginas. Página atual: " + paginaAtual;
        }
    }

    // toString
    @Override
    public String toString() {
        return "Livro{" +
                "título='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", número de páginas=" + numeroPaginas +
                ", página atual=" + paginaAtual +
                '}';
    }
}
