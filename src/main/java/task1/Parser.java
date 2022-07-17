package task1;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private static final String COUNTRY_LIST_URL = "https://onlinesim.ru/api/getFreeCountryList";
    private static final String PHONE_LIST_URL = "https://onlinesim.ru/api/getFreePhoneList?country=";

    public Map<String, ArrayList<String>> getMap() throws IOException {

        Map<String, ArrayList<String>> numbersMap = new HashMap<>();
        JsonObject objectCountry = getRequest(COUNTRY_LIST_URL);
        JsonArray countriesArray = objectCountry.getAsJsonArray("countries");

        for (int i = 0; i < countriesArray.size(); i++) {
            if (!countriesArray.get(i).getAsJsonObject().get("country").isJsonNull()) {
                int code = countriesArray.get(i).getAsJsonObject().get("country").getAsInt();
                String country = countriesArray.get(i).getAsJsonObject().get("country_text").getAsString();

                JsonObject objectPhone = getRequest(PHONE_LIST_URL + code);
                JsonArray numbersArray = objectPhone.getAsJsonArray("numbers");
                ArrayList<String> numberList = new ArrayList<>();

                for (int j = 0; j < numbersArray.size(); j++) {
                    if (!numbersArray.get(j).getAsJsonObject().get("full_number").isJsonNull()) {
                        String number = numbersArray.get(j).getAsJsonObject().get("full_number").getAsString();
                        numberList.add(number);
                    }
                }
                numbersMap.put(country, numberList);
            }
        }
        return numbersMap;
    }

    public JsonObject getRequest(String inputUrl) throws IOException {

        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        URL url = new URL(inputUrl);
        String response;

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

        while ((response = bufferedReader.readLine()) != null) {
            jsonObject = gson.fromJson(response, JsonObject.class);
        }
        bufferedReader.close();
        return jsonObject;
    }
}
