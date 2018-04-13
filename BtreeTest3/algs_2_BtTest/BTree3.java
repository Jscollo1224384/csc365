import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class BTree3 {
    final private int minDegree;  // = 4;  // The minmum number of keys in any node but the root.


    final private int maxKeys;// = (2*minDegree) - 1;  //The maximum number of keys in any node, = (2MinDegree -1)


    public Node root;

    //long nextPostion;

    int offset = 5;

    long currentPos;

    int count = 0;

    public Node allocateNode(Node x){
         Node node;
         node = x;
        count ++;
        x.currentPos = offset * count;
        return node;
    }

    public BTree3(int t) throws IOException {

        //new info

        minDegree = t;
        maxKeys = 2 * minDegree -1;
        root = new Node(0, true,0); // root is a leaf  //put back later
        root = allocateNode(root);
        root.diskWrite(root);	  // write the root to disk
    }// The root of the BTree

    /**
     * Private inner class for a B-tree node.
     */
    public class Node {

        int numOfKeys; //  The number of keys stored in the node.


        Entry[] kvPairs;


        public Node[] children;


        public boolean leaf;

        long currentPos;






        public Node(int numOfKeys, boolean leaf, long currentPos) {
            this.numOfKeys = numOfKeys;
            this.leaf = leaf;
            kvPairs = new Entry[maxKeys];
            if (leaf)
                children = null;
            else
                children = new Node[maxKeys + 1];

            this.currentPos = currentPos;



        }




        public void diskWrite(Node x)
                throws IOException{

            String keyFile = "keyFile.txt";     //Takes the paramater k(which is an index number from yelpData class) and appends it the file path creating the file name.
            //String valFile = "valFile.txt";     //Takes the paramater v(which is an index number from yelpData class) and appends it the file path creating the file name.
            File file = new File(keyFile);                // create the file to store the keys/
            //File file2 = new File(valFile);               // create the file to store values.
            RandomAccessFile fos;                   // create the random access file object for key
           // RandomAccessFile raf;                   // create random accsess file for value
           // raf = new RandomAccessFile(file2, "rw"); // make the RAF's readable and writeable
            fos = new RandomAccessFile(file, "rw");
                                               // creates a means of tracking and maintaining the position of the pointer in file.
            //long currentPos2 = 0;
            // ByteBuffer buffer;
            System.out.println(currentPos);

            FileChannel fileChannel = fos.getChannel();     // Create and open file channels for each RAF.
            //FileChannel valueChannel = raf.getChannel();
            for (int i = 0; i < x.kvPairs.length && x.kvPairs[i] != null; i++) {
                String key = x.kvPairs[i].getKey();
                key = key +",";
                for(int j = key.length();  j < 5; j++ ){

                    key = key + " ";
                    //System.out.println(key);
                }
                byte[] inputBytes = key.getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(inputBytes);
                fos.seek(currentPos);
                fileChannel.write(buffer);
                this.currentPos = fos.getFilePointer();

            }
            fileChannel.close();
            fos.close();


        }
        public String read(long currentPos) throws IOException {
            long pos = currentPos;
            File file = new File("keyFile.txt");
            RandomAccessFile raf = new RandomAccessFile(file,"rw");
            FileChannel channel = raf.getChannel();
            raf.seek(pos);
            ByteBuffer bb = ByteBuffer.allocate(offset);
            channel.read(bb);
            String k = new String(bb.array());
            k = k.replaceAll(" ", "");
            return k;


        }

        public String BTreeSearch(String k) throws IOException { //Might have to change back to string
            int i = 0;
            //children = new Node[numOfKeys];
            //children[i] = root;
            while (i < numOfKeys && k.compareTo(kvPairs[i].getKey()) > 0)
                i++;

            if (i < numOfKeys && kvPairs[i].getKey().compareTo(k) == 0) {
                // System.out.println("searched for key: " + kvPairs[i].getKey() + " and found value: " + kvPairs[i].getValue());
                return kvPairs[i].getValue();  // change back to getValue()
                //return "";

            }// found it

            if (leaf)
                return null; //original: return null;    // no child to search
            else {        // search child i
                children[i].read(children[i].currentPos);
                return  children[i].BTreeSearch(k); //original = return children[i].BTreeSearch(k);
            }
        }

        public void BTreeSplitChild(Node x, int i) throws IOException {
            Node z = new Node(minDegree - 1, leaf,0);

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
            diskWrite(this);
            diskWrite(z);
            diskWrite(x);
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
                //offset = 10;

                diskWrite(this); //x
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








    public void insert(String k, String v) throws IOException {
        Node r;
        r = root;

        Entry e = new Entry(k,v);
        //String current = k;
        //String next = k;



        if(r.numOfKeys == maxKeys) {
            Node s = new Node(0, false,0);
            root = s;
            s = allocateNode(s);
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
    public String setSearch(String k) throws IOException {
        return root.BTreeSearch(k);


    }
    public BTree3 read() throws IOException {
        FileReader fr = new FileReader("keyFile.txt");
        FileReader fr2 = new FileReader("vaLFile.txt");

        BufferedReader br = new BufferedReader(fr);
        BufferedReader br2 = new BufferedReader(fr2);

        String textRead = br.readLine();
        String textRead2 = br2.readLine();

        //System.out.println("File contents: ");

        String in,in2;


        String[] delim, delim2;
        BTree3 tree = new BTree3(5);


        while (textRead != null) {
            textRead = textRead.replaceAll(" ","");
            textRead2 = textRead2.replaceAll("#$%","");
            in = textRead;
            in2 = textRead2;

            delim = in.split(",");
            delim2 = in2.split("!@&");
            for (int i = 0; i < delim.length ; i++) {
                //System.out.println(delim[i]+ "      " + i);
                tree.insert(delim[i],(delim2[i]));
            }
            System.out.println(delim2[1]);
            textRead = br.readLine();
            textRead2 = br2.readLine();
        }

        fr.close();
        fr2.close();

        br.close();
        br2.close();
        return tree;

    }



}
