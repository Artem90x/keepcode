package task1;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Recording extends Parser {
    private static final String FILE_PATH = "C:/Users/User/IdeaProjects/keepcode//src/main/resources/result-task1.json";

    public static void createJsonFile(Map input) {

        Gson gson = new Gson();
        String json = gson.toJson(input);

        try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
            writer.write(json);
            writer.flush();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
