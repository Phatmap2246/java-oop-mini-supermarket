package utils;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class json {
    public static ObjectMapper mapper = getDefaultMapper();
    public static Writer output;
    static {
        try {
            output = getOutPut();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Writer getOutPut() throws IOException {
    return new BufferedWriter(new FileWriter("src/utils/temp.json", true));
    }
    public static ObjectMapper getDefaultMapper() {
        ObjectMapper defaultmapper = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .build();
        return defaultmapper;
    }
    public static void themVaoTemp(ArrayList<String> Jsons) throws IOException {
        output = getOutPut();
        for( String json : Jsons){
            output.append(json + System.lineSeparator());
        }
        output.close();
    }

}
