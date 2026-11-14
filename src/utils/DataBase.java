package utils;

import interfaces.TrongFile;
import person.DiaChi;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class DataBase {

    private static boolean isSerializableField(Field field) {
        int mod = field.getModifiers();
        if (Modifier.isStatic(mod) || Modifier.isTransient(mod)) {
            return false;
        }
        Class<?> type = field.getType();
        if (Collection.class.isAssignableFrom(type) || type.isArray()) {
            return false;
        }
        return type.isPrimitive()
                || type == String.class
                || type == Integer.class
                || type == Long.class
                || type == Double.class
                || type == Boolean.class
                || type == LocalDate.class
                || type == LocalDateTime.class
                || type == DiaChi.class;
    }

    public static ArrayList<Field> getALlField(Class<?> clazz) {
        ArrayList<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            Field[] declareds = clazz.getDeclaredFields();
            for (Field f : declareds) {
                if (isSerializableField(f)) {
                    fields.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

private static String escapeCSV(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n") || val.contains("\r")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }

    private static String[] parseCSVLine(String line) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    tokens.add(current.toString());
                    current.setLength(0);
                } else {
                    current.append(c);
                }
            }
        }
        tokens.add(current.toString());
        return tokens.toArray(new String[0]);
    }

    public static String getCSVHeader(Object obj) {
        Class<?> clazz = obj.getClass();
        ArrayList<Field> fields = getALlField(clazz);
        StringBuilder header = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) header.append(",");
            header.append(fields.get(i).getName());
        }
        return header.toString();
    }

    public static String getCSVLine(Object obj) {
        Class<?> clazz = obj.getClass();
        ArrayList<Field> fields = getALlField(clazz);
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) line.append(",");
            Field field = fields.get(i);
            field.setAccessible(true);
            Object val = null;
            try {
                val = field.get(obj);
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
            }
            String valString = "";
            if (val instanceof DiaChi dc) {
                valString = String.join(";",
                        dc.getSoNha(), dc.getDuong(), dc.getPhuongXa(),
                        dc.getQuanHuyen(), dc.getTinhThanhPho());
            } else if (val != null) {
                valString = val.toString();
            }
            line.append(escapeCSV(valString));
        }
        return line.toString();
    }

    public static Object getDataType(String value, Class<?> targetType) {
        if (targetType == String.class) return value == null ? "" : value;
        if (value == null || value.strip().isEmpty() || value.equalsIgnoreCase("null")) {
            if (targetType == DiaChi.class) return new DiaChi();
            return null;
        }
        value = value.strip();
        if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            if (value.equalsIgnoreCase("true")) return true;
            if (value.equalsIgnoreCase("false")) return false;
            throw new IllegalArgumentException("Giá trị boolean không hợp lệ: " + value);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        } else if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value);
        } else if (targetType == LocalDate.class) {
            LocalDate parsed = DateUtil.parseDate(value);
            return parsed != null ? parsed : LocalDate.parse(value);
        } else if (targetType == LocalDateTime.class) {
            return LocalDateTime.parse(value);
        } else if (targetType == DiaChi.class) {
            String[] parts = value.split(";", -1);
            if (parts.length >= 5) {
                return new DiaChi(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim());
            }
            return new DiaChi(value, "", "", "", "");
        }
        return null;
    }

    public static String getFilePath(Class<?> clazz) {
        if (TrongFile.class.isAssignableFrom(clazz)) {
            try {
                TrongFile coFile = (TrongFile) clazz.getDeclaredConstructor().newInstance();
                return coFile.getFilePath();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        System.err.println("Không lưu trữ được");
        return null;
    }

    private static Method findSetter(Class<?> clazz, String fieldName, Class<?> paramType) {
        String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        Class<?> curr = clazz;
        while (curr != null && curr != Object.class) {
            try {
                return curr.getDeclaredMethod(setterName, paramType);
            } catch (NoSuchMethodException ignored) {
            }
            curr = curr.getSuperclass();
        }
        return null;
    }

    public static <T> T taoObject(Class<T> clazz, String CSVHeader, String CSVLine) {
        if (CSVHeader == null || CSVLine == null || CSVLine.isBlank()) {
            return null;
        }
        String[] headers = parseCSVLine(CSVHeader);
        String[] values = parseCSVLine(CSVLine);
        if (headers.length != values.length) {
            System.err.println("Số trường dữ liệu không khớp tiêu đề.");
            return null;
        }
        try {
            T inst = clazz.getDeclaredConstructor().newInstance();
            ArrayList<Field> fields = getALlField(clazz);
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].trim();
                String val = values[i].trim();
                for (Field field : fields) {
                    if (field.getName().equals(header)) {
                        field.setAccessible(true);
                        Object value = getDataType(val, field.getType());
                        if (value == null && field.getType().isPrimitive()) {
                            throw new IllegalArgumentException("Không thể gán null cho trường nguyên thủy: " + field.getName());
                        }
                        Method setter = findSetter(clazz, field.getName(), field.getType());
                        boolean shouldInvokeSetter = setter != null && value != null
                                && (!(value instanceof String s) || !s.isEmpty());
                        if (shouldInvokeSetter) {
                            setter.setAccessible(true);
                            setter.invoke(inst, value);
                        } else if (value != null || !field.getType().isPrimitive()) {
                            field.set(inst, value);
                        }
                        break;
                    }
                }
            }
            return inst;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            System.err.println("Dữ liệu CSV không hợp lệ: " + cause.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static <T> T taoObjectTuTerminal(Class<T> clazz, Scanner sc) {
        ArrayList<Field> fields = DataBase.getALlField(clazz);
        try {
            T inst = clazz.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                System.out.println("Nhập giá trị: " + field.getName());
                String inputString = sc.nextLine().trim();
                Object value = DataBase.getDataType(inputString, field.getType());
                if (value != null || !field.getType().isPrimitive()) {
                    field.set(inst, value);
                }
            }
            return inst;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static void ensureParentDir(String filePath) {
        File f = new File(filePath);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    public static void ghiVaoFile(Object obj, String filePath, boolean ghiDe) {
        if (obj == null || filePath == null) return;
        ensureParentDir(filePath);
        File file = new File(filePath);
        boolean canHeader = ghiDe || !file.exists() || file.length() == 0;
        String headers = DataBase.getCSVHeader(obj);
        String csvLine = DataBase.getCSVLine(obj);
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, !ghiDe), StandardCharsets.UTF_8))) {
            if (canHeader) {
                writer.write(headers);
                writer.newLine();
            }
            writer.write(csvLine);
            writer.newLine();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void ghiVaoFile(ArrayList<Object> objs, String filePath, boolean ghiDe) {
        if (objs == null || objs.isEmpty() || objs.getFirst() == null || filePath == null) return;
        ensureParentDir(filePath);
        File file = new File(filePath);
        boolean canHeader = ghiDe || !file.exists() || file.length() == 0;
        String headers = DataBase.getCSVHeader(objs.getFirst());
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, !ghiDe), StandardCharsets.UTF_8))) {
            if (canHeader) {
                writer.write(headers);
                writer.newLine();
            }
            for (Object obj : objs) {
                if (obj == null) continue;
                writer.write(DataBase.getCSVLine(obj));
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void ghiVanBan(String filePath, String content, boolean ghiDe) {
        ensureParentDir(filePath);
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, !ghiDe), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> ArrayList<T> docfile(Class<T> clazz, String filePath) {
        ArrayList<T> objs = new ArrayList<>();
        if (filePath == null) return objs;
        File f = new File(filePath);
        if (!f.exists() || f.length() == 0) return objs;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String headers = reader.readLine();
            while (headers != null && headers.trim().startsWith("#")) {
                headers = reader.readLine();
            }
            if (headers == null || headers.trim().isEmpty()) {
                return objs;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
                T obj = DataBase.taoObject(clazz, headers, line);
                if (obj != null) objs.add(obj);
            }
            return objs;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return objs;
        }
    }

    public static <T> ArrayList<T> docfile(Class<T> clazz) {
        String filePath = getFilePath(clazz);
        if (filePath == null) return new ArrayList<>();
        return docfile(clazz, filePath);
    }
}
