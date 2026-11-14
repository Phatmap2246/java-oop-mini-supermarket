package utils;

import interfaces.TrongFile;
import sanpham.SanPham;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

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
    public static String getCSVHeader(Object obj){
        Class<?> clazz = obj.getClass();
        ArrayList<Field> fields = getALlField(clazz);
        String header = "";
        for(Field field :fields){
           header += field.getName();
           header += ",";
        }
        if (!header.isEmpty()) header = header.substring(0,header.length()-1);
        return header;
    }
    public static String getCSVLine(Object obj) {
        Class<?> clazz = obj.getClass();
        ArrayList<Field> fields = getALlField(clazz);
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
        if (!line.isEmpty()) line = line.substring(0,line.length() -1);
        return line;
    }
    public static Object getDataType(String value,Class<?> targetType){
        if (targetType == String.class) return value;
        if (value == null || value.strip().isEmpty() || value.equalsIgnoreCase("null")){
            return null;
        }
        value = value.strip();
        if(targetType == int.class){
            return Integer.parseInt(value);
        }
        else if(targetType == boolean.class){
            if (value.equalsIgnoreCase("true")) return true;
            if (value.equalsIgnoreCase("false")) return false;
            throw new IllegalArgumentException("Giá trị boolean không hợp lệ: " + value);
        }
        else if(targetType == double.class){
            return Double.parseDouble(value);
        }
        else if(targetType == long.class){
            return Long.parseLong(value);
        }
        else if(targetType == LocalDate.class){
            return LocalDate.parse(value);
        }
        System.err.println("co loi");
        return null;
    }
    public static String getFilePath(Class<?> clazz) {
        if(TrongFile.class.isAssignableFrom(clazz)){
            try {
                TrongFile coFile = (TrongFile) clazz.getDeclaredConstructor().newInstance();
                return coFile.getFilePath();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        System.err.println("không lưu trữ được");
        return null;
    }
    //Class của object cần constuctor rỗng VD public SanPham(){};
    public static <T> T taoObject (Class<T> clazz,String CSVHeader, String CSVLine){
       if (CSVHeader == null || CSVLine == null) {
           System.err.println("Thiếu dữ liệu hoặc tiêu đề bản ghi.");
           return null;
       }
       String[] headers = CSVHeader.split(",", -1);
       String[] values = CSVLine.split("," ,-1);
       if (headers.length != values.length) {
           System.err.println("Số trường dữ liệu không khớp tiêu đề.");
           return null;
       }
       try{
           T inst = clazz.getDeclaredConstructor().newInstance();
           ArrayList<Field> fields = getALlField(clazz);
           for(int i = 0; i < headers.length; i++){
               String header = headers[i].trim();
               String val = values[i].trim();
              for (Field field : fields){
                if(field.getName().equals(header)){
                    field.setAccessible(true);
                    Object value = getDataType(val, field.getType());
                    field.set(inst,value);
                    break;
                }
              }
           }
           return inst;
       } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException | java.time.DateTimeException e) {
           System.err.println(e.getMessage());
           return null;
       }
    }
    public static <T> T taoObjectTuTerminal(Class<T> clazz, Scanner sc){
        System.out.println("nhap cac gia tri cho" + clazz);
        ArrayList<Field> fields = DataBase.getALlField(clazz);
        try{
            T inst = clazz.getDeclaredConstructor().newInstance();
            for(Field field : fields){
                field.setAccessible(true);
                System.out.println("nhap gia tri: " + field.getName());
                String inputString = sc.nextLine().trim();
                Object value = DataBase.getDataType(inputString,field.getType());
                field.set(inst,value);
            }
            System.out.println("Tao "+ clazz + "thanh cong");
            return inst;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static void ghiVaoFile(Object obj, String filePath,boolean ghiDe){
        if (obj == null) {
            System.err.println("Không có đối tượng để ghi file.");
            return;
        }
        String headers = DataBase.getCSVHeader(obj);
        String csvLine = DataBase.getCSVLine(obj);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,!ghiDe))){
            if(ghiDe == true) writer.write(headers);
            writer.newLine();
            writer.write(csvLine);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    public static void ghiVaoFile(ArrayList<Object> objs, String filePath, boolean ghiDe){
        if (objs == null || objs.isEmpty() || objs.getFirst() == null) {
            System.err.println("Danh sách rỗng, không có dữ liệu để ghi.");
            return;
        }
        String headers = DataBase.getCSVHeader(objs.getFirst());
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,!ghiDe))){
            if(ghiDe == true) writer.write(headers);
            for(Object obj : objs){
            String csvLine = DataBase.getCSVLine(obj);
            writer.newLine();
            writer.write(csvLine);
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    public static void ghiVanBan(String filePath, String content, boolean ghiDe){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,!ghiDe))){
          writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> ArrayList<T> docfile( Class<T> clazz, String filePath){
       ArrayList<T> objs = new ArrayList<T>();
       try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
           String headers =reader.readLine();
           if(headers == null || headers.trim().isEmpty()){
               return objs;
           }
           String line;
           while ((line = reader.readLine()) != null) {
               T obj = DataBase.taoObject(clazz, headers, line);
               if (obj != null) objs.add(obj);
           }
           return objs;
       }
       catch(Exception e){
           System.err.println(e.getMessage());
           return objs;
       }
    }
    public static <T> ArrayList<T> docfile( Class<T> clazz){
        ArrayList<T> objs = new ArrayList<T>();
        String filePath = getFilePath(clazz);
        if(filePath == null) return objs;
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String headers =reader.readLine();
            if(headers.trim().isEmpty()){
                return objs;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                objs.add(DataBase.taoObject(clazz, headers, line));
            }
            return objs;
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            return objs;
        }
    }
    public static <T> void suaObjectTrongArrayList(ArrayList<T> objs, int index){
        if (objs == null || index < 0 || index >= objs.size() || objs.get(index) == null) {
            System.out.println("Không tìm thấy đối tượng tại vị trí cần sửa!");
            return;
        }
        T obj = objs.get(index);
        ArrayList<Field> fields = DataBase.getALlField(obj.getClass());
        System.out.println("Cac gia tri co the sua");
        for(Field field : fields){
            field.setAccessible(true);
            System.out.println("STT:" +fields.indexOf(field) + " , " +field.getName());
        }
        //lay input stt tu user
        int stt =1;

        if (stt >= fields.size()) {
            System.out.println("Không có thuộc tính tại vị trí cần sửa!");
            return;
        }
        Field toBeEdit = fields.get(stt);
        //lay input gia tri moi tu user
        try {
            toBeEdit.set(obj,null);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
