public class Entry {
    String key;
    String value;

    public Entry(String k, String v){
        key = k;
        value = v;
    }
    public void setValue(String v){
        value = v;
    }
    public String getValue(){
        return value;
    }
    public String getKey(){
        return key;
    }
}
