import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class BTree3 {
    final private int minDegree;  // = 4;  // The minmum number of keys in any node but the root.


    final private int maxKeys;// = (2*minDegree) - 1;  //The maximum number of keys in any node, = (2MinDegree -1)

    public Node root;         // The root of the BTree

    /**
     * Private inner class for a B-tree node.
     */
    public class Node {

        int numOfKeys; //  The number of keys stored in the node.


        Entry[] kvPairs;


        public Node[] children;


        public boolean leaf;

        

        public Node(int numOfKeys, boolean leaf) {
            this.numOfKeys = numOfKeys;
            this.leaf = leaf;
            kvPairs = new Entry[maxKeys];
            if (leaf)
                children = null;
            else
                children = new Node[maxKeys + 1];
        }

        private int diskRead() {
        }

        private void diskWrite(int p, String k, String v) throws IOException {

                String nodeFile = "data.txt";     //Takes the paramater k(which is an index number from yelpData class) and appends it the file path creating the file name.
                String key = k;
                String value = v;//Takes the paramater v(which is an index number from yelpData class) and appends it the file path creating the file name.
                File file = new File(nodeFile);                // create the file to store the keys/
                //File file2 = new File(v);               // create the file to store values.
               // RandomAccessFile fos;                   // create the random access file object for key
                RandomAccessFile raf;                   // create random accsess file for value
                raf = new RandomAccessFile(file, "rw"); // make the RAF's readable and writeable
                //fos = new RandomAccessFile(file, "rw");
                     // creates a means of tracking and maintaining the position of the pointer in file.
                String point = "";
                point = Integer.toString(p);
                //ByteBuffer buffer;

                FileChannel pointChannel = raf.getChannel();

                FileChannel keyChannel = raf.getChannel();     // Create and open file channels for each RAF.
                FileChannel valChannel = raf.getChannel();

            for (int i = key.length(); i < 21 ; i++) {

                key = key + " ";

            }
            for (int i = value.length(); i < 11 ; i++) {

                value = value + " ";

            }
            for (int i = point.length(); i < 5 ; i++) {

                point = point + " ";

            }
            key = key + ",";
            value = value + ",";
            point = point + ",";

                            byte[] pointerBytes = point.getBytes();
                            byte[] keyBytes = key.getBytes();
                            byte[] valueBytes = value.getBytes();

                            ByteBuffer b = ByteBuffer.wrap(pointerBytes);
                            ByteBuffer buffer = ByteBuffer.wrap(keyBytes);
                            ByteBuffer buf = ByteBuffer.wrap(valueBytes);

                            long current = raf.getFilePointer();
                            System.out.println("current pos in file: " + current);
                            if(current != 0) {

                                raf.seek(current);

                                keyChannel.write(buffer);
                                valChannel.write(buf);
                                pointChannel.write(b);

                            }
                            else{
                                raf.seek(0);
                                keyChannel.write(buffer);
                                valChannel.write(buf);
                                pointChannel.write(b);
                            }




                    // System.out.println(key);

                keyChannel.close();
                valChannel.close();
                pointChannel.close();

               //fos.close();
                raf.close();



        }

        public String BTreeSearch(String k) { //Might have to change back to string
            int i = 0;
            //children = new Node[numOfKeys];
            //children[i] = root;
            while (i < numOfKeys && k.compareTo(kvPairs[i].getKey()) > 0)
                i++;

            if (i < numOfKeys && kvPairs[i].getKey().compareTo(k) == 0) {
                // System.out.println("searched for key: " + kvPairs[i].getKey() + " and found value: " + kvPairs[i].getValue());
                return kvPairs[i].getValue();
                //return "";

            }// found it

            if (leaf)
                return null; //original: return null;    // no child to search
            else {        // search child i
                //children[i].diskRead();
                return  children[i].BTreeSearch(k); //original = return children[i].BTreeSearch(k);
            }
        }

        public void BTreeSplitChild(Node x, int i) {
            Node z = new Node(minDegree - 1, leaf);

            // Copy the t-1 keys in positions t to 2t-2 into z.
            for (int j = 0; j < minDegree - 1; j++) {
                z.kvPairs[j] = kvPairs[j + minDegree];
                kvPairs[j + minDegree] = null; // remove the reference
            }

            // If this node is not a leaf, copy the t children in
            // positions t to 2t-1, too.
            if (!leaf)
                for (int j = 0; j < minDegree; j++) {
                    z.children[j] = children[j + minDegree];
                    children[j + minDegree] = null; // remove the reference
                }

            numOfKeys = minDegree - 1;

            // Move the children in x that are to the right of y by
            // one position to the right.
            for (int j = x.numOfKeys; j >= i + 1; j--)
                x.children[j + 1] = x.children[j];

            // Drop z into x's child i+1.
            x.children[i + 1] = z;

            // Move the keys in x that are to the right of y by one
            // position to the right.
            for (int j = x.numOfKeys - 1; j >= i; j--)
                x.kvPairs[j + 1] = x.kvPairs[j];

            // Move this node's median key into x, and remove the
            // reference to the key in this node.
            x.kvPairs[i] = kvPairs[minDegree - 1];
            kvPairs[minDegree - 1] = null;

            x.numOfKeys++;        // one more key/child in x

            // All done.  Write out the nodes.
            //diskWrite();
           // z.diskWrite();
           // x.diskWrite();
        }

        public void BTreeInsertNonfull(Entry e) throws IOException {
            System.out.println("num of keys: "+ numOfKeys);
            int i = numOfKeys - 1; //-1
            String kKey = e.getKey();
            //x.kvPairs[i] = e;
            //System.out.println(x.kvPairs[i].getKey());
            if (leaf) {

                //boolean notMatch = true;

                // Move all keys greater than k's by one position to
                // the right.


                while (i >= 0 && kvPairs[i].getKey().compareTo(kKey) >= 0) {
                    System.out.println("enter while");
                    //if(kvPairs[i].getKey().compareTo(kKey) == 0) {
                    // kvPairs[i].setValue(kvPairs[i].getValue()+ " " + e.getValue());
                    //System.out.println("i count for match" + " "+ i + " " + kvPairs[i].getKey() + " " + kvPairs[i].getValue());
                    // notMatch = false;
                    //i++;
                    // break;
                    // }
                    kvPairs[i + 1] = kvPairs[i];
                    i--;
                    // System.out.println(i);
                }
                //  }

                // if(notMatch)
                kvPairs[i + 1] = e;
                // kvPairs[i].setValue(kvPairs[i].getValue()+e.getValue());
                // Either i is -1 or key[i] is the rightmost key <=
                // k's key.  In either case, drop k into position i+1.
                //if(kvPairs[i].getKey().compareTo(kKey) != 0)
                // kvPairs[i + 1] = e;

                //System.out.println(e.key);
                //write code to set the value
                // if(notMatch)
                System.out.println(i + 1 + "," + kvPairs[i + 1].getKey());
                //  if(notMatch)
                numOfKeys++;
                diskWrite(i,kvPairs[i+1].getKey(),kvPairs[i+1].getValue()); //x
                //BTreeInsertNonfull(e);


            } else {
                // Find which child we descend into.
                while (i >= 0 && kvPairs[i].getKey().compareTo(kKey) > 0)
                    i--;

                // Either i is -1 or key[i] is the rightmost key <=
                // k's key.  In either case, descend into position
                // i+1.
                i++;
                if(children[i] != null){
//                    children[i].diskRead();

                    if (children[i].numOfKeys == maxKeys) {
                        // That child is full, so split it, and possibly
                        // update i to descend into the new child.
                        children[i].BTreeSplitChild(this, i);
                        if (kvPairs[i].getKey().compareTo(kKey) < 0)
                            i++;
                    }

                    children[i].BTreeInsertNonfull(e);
                }
            }
        }
        public String walk(int depth)
        {
            String result = "";

            for (int i = 0; i < numOfKeys; i++) {
                if (!leaf)
                    result += children[i].walk(depth+1);
                for (int j = 0; j < depth; j++)
                    result += "  ";
                result += "Node at " + this + ", key " + i + ": " +
                        kvPairs[i] + "\n";
            }

            if (!leaf)
                result += children[numOfKeys].walk(depth+1);
            //System.out.println("result");
            return result;
        }
    }






    public BTree3(int t)
    {
        minDegree = t;
        maxKeys = 2 * minDegree -1;
        root = new Node(0, true); // root is a leaf
        //root.diskWrite();	  // write the root to disk
    }

    public void insert(String k, String v) throws IOException {
        Node r;
        r = root;
        Entry e = new Entry(k,v);
        //String current = k;
        //String next = k;



        if(r.numOfKeys == maxKeys) {
            Node s = new Node(0, false);
            root = s;
            s.children[0] = r;
            r.BTreeSplitChild(s, 0);
            s.BTreeInsertNonfull(e);
        }
        else {
            // System.out.println("going to btreenonfull");
            r.BTreeInsertNonfull(e);
        }


    }

    public void setWalk(int d){
        Node r;
        r = root;
        r.walk(d);

    }
    public String setSearch(String k){
        return root.BTreeSearch(k);


    }

}
