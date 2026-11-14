# Nhập xuất từ file
## Các class có thể nhập xuất từ file
Địa chỉ, hóa đơn, hóa đơn bán hàng, chi tiết hóa đơn,
thanh toán chuyển khoản, thanh toán tiền mặt, đối tác,
khuyến mãi, các class con khuyến mãi
## Lấy các field từ class
reflection là một cách để java tạo object, lấy, sửa 
các biến từ class khi chạy, giúp làm gọn, tái sử dụng
code cực kỳ hiệu quả.
Các biến được declare trong class có loại là Field.
## Lấy tất cả fields từ một class
Vì các class có kế thừa nên cần một method để lấy
cả các Field của class cha
```java
public static ArrayList<Field> getALlField(Class<?> clazz){
    ArrayList<Field> fields = new ArrayList<>();
    while(clazz != null && clazz != Object.class){
        Field[] declareds = clazz.getDeclaredFields();
        fields.addAll(Arrays.asList(declareds));
        clazz =clazz.getSuperclass();
    }
    return fields;
}
```
tạo arraylist fields, lấy hết các fields
của class hiện tại bằng
`clazz.getDeclaredFields()`,
thêm vào arraylist rồi di chuyển lên lớp cha,
vòng lặp dừng khi lớp cha rỗng hoặc là 
`Object.class`
vì đây là class cuối cùng của mọi class.
Bằng cách này có thể lấy mọi field từ class 
dù kế thừa nhiều
## Lấy value từ các field của class
```java
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
        line = line.substring(0,line.length() -1);
        return line;
    }
```
Tất cả các giá trị trong object sẽ được trả về
với dạng một String CSV.  
Đầu tiên lấy các field của object và class
của object đó,
duyệt từng phần tử trong arraylist, dòng 
` field.setAccessible(true);` để có thể đọc
và ghi các field private, không có sẽ phát sinh
lỗi `IllegalAccessException` 

Sau đó lấy giá trị của field này của object
được truyền vào, chuyển đổi giá trị thành String,
thêm vào String line và dấu phẩy.  
Dòng `line = line.substring(0,line.length() -1);`  
để loại bỏ dấu phẩy thừa cuối cùng.

## Lấy string giá trị, kiểu dữ liệu và trả về giá trị theo kiểu dữ liệu đó
```java
    public static Object getDataType(String value,Class<?> targetType){
        if (targetType == String.class) return value;
        if (value == null || value.strip() == "" || value.equalsIgnoreCase("null")){
            return null;
        }
        value = value.strip();
        if(targetType == int.class){
            return Integer.parseInt(value);
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
```
Lý do cần method này vì Java không tự đoán
type dữ liệu và cho method tạo object
## Tạo Object từ String CSVheader và CSVlines
CSVheader là String CSV của các tên biến
của class.  
CSVlines là các giá trị trong object.  
Lưu ý, để sử dụng method này, class cần có
constructor rỗng
```java
    public static <T> T taoObject (Class<T> clazz,String CSVHeader, String CSVLine){
    String[] headers = CSVHeader.split(",", -1);
    String[] values = CSVLine.split("," ,-1);
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
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
        System.err.println(e.getMessage());
        return null;
    }
}
```
T và \<T> để chỉ method này sử dụng generics
nghĩa là method này sẽ sử dụng được cho kiểu dữ liệu
T bất kỳ mà không cần cast, khác với khi sử dụng Object
method này sẽ lấy headers, values, rồi tất cả các field trong
của class, dòng
`T inst = clazz.getDeclaredConstructor().newInstance();`  
có nghĩa tạo Object loại T từ class của nó, lấy
constructor và tạo bằng newInstance, sau đó
match tên field với header rồi gán giá trị với
2 vòng lặp for, cuối cùng trả về Object inst
## Ghi vào file csv
cách sử dụng, cho object cần ghi, Path tới file
lưu trữ, có ghi đè hay không
```java
    public static void ghiVaoFile(ArrayList<Object> objs, String filePath, boolean ghiDe){
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
```

## Đọc từ file csv
cách sử dụng, truyền class muốn đọc, Path tới file,
trả về ArrayList của Object đó
```java
    public static <T> ArrayList<T> docfile( Class<T> clazz, String filePath){
    ArrayList<T> objs = new ArrayList<T>();
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
```
