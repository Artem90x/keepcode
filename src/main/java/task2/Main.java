package task2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import task1.Parser;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String PRICE_LIST_URL = "https://onlinesim.ru/price-list-data?type=receive";
    private static final String FILE_PATH = "C:/Users/User/IdeaProjects/keepcode//src/main/resources/result-task2.json";

    public static void main(String[] args) throws IOException {

        Parser parser = new Parser();
        Gson gson = new Gson();

        JsonObject input = parser.getRequest(PRICE_LIST_URL);
        JsonObject inputCountries = input.getAsJsonObject("countries");
        JsonObject inputText = input.getAsJsonObject("text");
        JsonObject inputList = input.getAsJsonObject("list");

        ArrayList<String> key = new ArrayList<>(inputCountries.keySet());
        Map<String, Map<String, String>> resultMap = new HashMap<>();

        for (String k : key) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> myMap = gson.fromJson(inputList.get(k), type);
            String countryName = inputText.getAsJsonObject().get("country_" + k).getAsString();
            resultMap.put(countryName, myMap);

        }

        resultMap.entrySet().forEach(System.out::println);
        String json = gson.toJson(resultMap);

        try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
            writer.write(json);
            writer.flush();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
