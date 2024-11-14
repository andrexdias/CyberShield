public class User {
    private String fristName;
    private String lastName;

    public User(String fristName, String lastName) {
        this.fristName = fristName;
        this.lastName = lastName;
    }

    public static void add(User actual) {

    }

    public void setFristName(String fristName) {
        this.fristName = fristName;
    }

    public void setLastName(String lastname){
        this.lastName = lastname;
    }

    public String getFristName(){
        return fristName;
    }

    public String getLastName() {
        return lastName;
    }

}
