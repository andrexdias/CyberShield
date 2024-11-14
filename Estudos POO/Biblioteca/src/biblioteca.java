import java.util.ArrayList;

public class biblioteca {

    private String titulo;
    private ArrayList<String> livros;

    public biblioteca() {
        this.titulo = "";
        this.livros = new ArrayList<>();
    }

    public biblioteca(String titulo) {
        this.titulo = titulo;
        this.livros = new ArrayList<>();
    }

    public String getTitulo(){
        return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }
}






