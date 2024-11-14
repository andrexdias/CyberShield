import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<User> users = new ArrayList<>();
        System.out.println("Insira a quantidade de pessoas deseja adcionar:");
        int qua = Integer.parseInt(scanner.next());
        

        System.out.println("Insira o número da pessoa deseja ver:");
        String ver = scanner.next();
        System.out.println(users);
        System.out.println(users[Integer.parseInt(ver)].getLastName());
    }


}