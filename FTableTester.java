public class FTableTester {
    public static void main(String[]args){
        FrequencyTable table = new FrequencyTable();

        table.put("hello",1);
        table.put("hello",1);
        table.put("hello",1);
        table.put("hello",1);
        table.put("hi",1);

        System.out.println(table.get("hello"));
        System.out.println(table.get("hi"));


    }
}
