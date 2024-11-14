package pt.andrexdias.focus;

public class Task {
    private String id;
    private String description;
    private String priority;
    private boolean completed;

    public Task() {
        // Construtor vazio necessário para o Firebase
    }

    public Task(String description, String priority) {
        this.description = description;
        this.priority = priority;
        this.completed = false;
    }

    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}