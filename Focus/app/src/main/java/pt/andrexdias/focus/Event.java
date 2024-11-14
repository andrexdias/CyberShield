package pt.andrexdias.focus;

public class Event {
    private String id;
    private String name;
    private String local;
    private String date;
    private String time;

    public Event() {
        // Construtor vazio necessário para o Firebase
    }

    public Event(String id, String name, String local, String date, String time) {
        this.id = id;
        this.name = name;
        this.local = local;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}