import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException {
        BTree3 bTree = new BTree3(5);
        String k1;
        k1 = "B";
        String k2;
        k2 = "a";
        String K3 = "C";

        bTree.insert(k1,"hello");
        bTree.insert(k2,"goodBye");
       // bTree.insert(k1,"hello");


    }
}
