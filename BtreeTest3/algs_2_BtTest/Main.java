import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException {
        BTree3 bTree = new BTree3(5);
        String k1;
        k1 = "B";
        String k2;
        k2 = "a";
        String K3 = "C";
        //File file = new File("keyFile.txt");
        //BTree3 tree;


       //(file.exists()){

           //tree = bTree.read();
           //System.out.println(tree.setSearch(k1));
       // }

        bTree.insert("B","h");
        bTree.insert("A","g");
        bTree.insert("Z","h");
        bTree.insert("Y","g");
        bTree.insert("W","h");
        bTree.insert("X","g");
        bTree.insert("S","h");
        bTree.insert("U","g");
        bTree.insert("R","h");
        bTree.insert("V","V");
        bTree.insert("L","h");
        bTree.insert("H","g");
        bTree.insert("D","h");
        bTree.insert("P","g");
        bTree.insert("J","h");
        bTree.insert("Q","g");

System.out.println(bTree.setSearch("X"));
        // bTree.insert(k1,"hello");


    }
}
