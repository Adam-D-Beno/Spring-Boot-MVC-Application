import java.util.ArrayList;
import java.util.List;

public class Tets {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a1");
        list.add("a2");
        list.add("a3");
        Person person = new Person(
                "Adam",
                new ArrayList<>()
        );

        System.out.println(person);
    }
}
