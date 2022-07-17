package task1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        Map<String, ArrayList<String>> result = new Parser().getMap();
        result.entrySet().forEach(System.out::println);
        Recording.createJsonFile(result);
    }
}
