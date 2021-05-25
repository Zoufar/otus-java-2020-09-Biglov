package hw16W_io.dataprocessor;

import hw16W_io.model.Measurement;

import javax.json.Json;
import javax.json.JsonStructure;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;


public class FileLoader implements Loader {

    private final String inputDataFileName;

    public FileLoader(String fileName) {
        inputDataFileName = fileName;
    }

    @Override
    public List<Measurement> load() {
                List<Measurement> list = new ArrayList<>();
        try (var jsonReader = Json.createReader(FileLoader.class.getClassLoader()
                                                  .getResourceAsStream(inputDataFileName))) {
            JsonStructure jsonFromFile = jsonReader.read();
            for (JsonValue val : jsonFromFile.asJsonArray()) {
                var jsonObject = (JsonObject) val;
                String name = null;
                double value = 0.;
                for (Map.Entry<String, JsonValue> entry : jsonObject.entrySet()) {
                    if (entry.getKey().equals("name")) {
                        name = ((JsonString) jsonObject.get(entry.getKey())).getString();
                    }
                    if (entry.getKey().equals("value")) {
                        value = ((JsonNumber) jsonObject.get(entry.getKey())).doubleValue();
                    }
                }
                list.add(new Measurement(name, value));
            }
        }
        return list;
    }
}
