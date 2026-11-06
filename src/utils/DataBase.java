package utils;

import java.util.ArrayList;
import java.lang.reflect.Field;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class DataBase {
    public static ArrayList<Field> getALlField(Class<?> clazz){
       ArrayList<Field> fields = new ArrayList<>();
       while(clazz != null && clazz != Object.class){
           Field[] declareds = clazz.getDeclaredFields();
           fields.addAll(Arrays.asList(declareds));
           clazz =clazz.getSuperclass();
       }
       return fields;
    }
    public static String getCSVLine(Object obj) {
        Class<?> clazz = obj.getClass();
        List<Field> fields = getALlField(clazz);
        String line = "";
        for (Field field : fields) {
            field.setAccessible(true);
            Object val = null;
            try {
                val = field.get(obj);
            } catch (IllegalAccessException e) {
                System.err.println(e.getStackTrace());
            }
            String valString = (val != null) ? val.toString() : "";
            line += valString;
            line += ',';
        }
        line.substring(0,line.length() -1);
        return line;
    }
    public static <T> void themVaoFile(T object, String filePath){

    }
}
