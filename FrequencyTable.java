/*
Name: Joseph M. Scollo
School: Suny Oswego
Course: CSC-365 Data Structures

Class: FrequencyTable
About Class: This class contains 5 methods and a construcor. The details on their members and how the function is documented
                  through out the code. This class is a custom Hash Table that takes strings as keys
                  and the frequency in which they occur as their values.
*/


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class FrequencyTable{//class

    private double LOADFACTOR;
    private int INITSIZE;
    private FreqBucket[] fTable;
    int elements;
    ArrayList<String> keyList, uKeys; //keysFromFile, valsFromeFile;
    File file;
    String fName;
    RandomAccessFile raf;
    ByteBuffer bb;
    ByteBuffer fb;
    FileChannel channel;
    long startpos, endpos;


    public FrequencyTable()   //Constructor that sets innitializes LOADFACOR, ININTSIZE, fTable, elements, and keyList.
    {//constructor
        //serialVersionUID = 1L;
        LOADFACTOR = .75;
        INITSIZE = 500;
        fTable = new FreqBucket[INITSIZE];
        elements = 0;
        keyList = new ArrayList<String>();
        uKeys = new ArrayList<String>();
       // keysFromFile = new ArrayList<String>();
        //valsFromeFile = new ArrayList<String>();
        fName = "dataStore2.txt";
        file = new File(fName);
    }




    public void addKey(String key) //Method thats adds keys entered into the thable to an array list.
    {//void addKey
        keyList.add(key);
    }

    public ArrayList<String> getKeyList() //Method tht returns the keys from the keyList.
    {//void

        return keyList;
    }

    public void put(String key, int value) {//put
        int index;
        index = Math.abs(key.hashCode()) % fTable.length;
        FreqBucket node;
        //addKey(key);
        if (fTable[index] == null) // Checks is position in the array is empty
        {//if   
            fTable[index] = new FreqBucket(key, value);// creates a new FreqBucket or "node ready to take an element's key and value.       
        } else {//else
            node = fTable[index];

            while (node != null) //while loop that handles collisions. The body of this loop is entered while an index is not null.
            {//begin while
                if (key.equals(node.getKey()))            // checks to see if the current key matches the key in the current index
                {
                    // if the key mathches, increment its value by one.

                    node.setValue(node.getValue() + 1);
                    return;
                } else if (node.getNextNode() == null) //if the next node is null set that node to receive an element with its key and a value.
                {
                    node.setNextNode(new FreqBucket(key, value));
                    elements++;                                           //increment the element count;
                    if (elements >= (fTable.length * LOADFACTOR)) {                                              // check to see if if the table is equal or greator than 75% of the table size.
                        resize();
                    }
                    return;
                } else //the node has an elment get the next node.
                    node = node.getNextNode();
            }//end while
        }

    }

    //*************************************************************************//
    // public method of type integer that returns the associated key values    //
    //*************************************************************************//


    public int get(String key) // public method of type integer that takes a String parameter and returns the associated key values.
    {//get
        int index;

        index = Math.abs(key.hashCode()) % fTable.length; //sets the index = to the hashcode.

        FreqBucket node;
        if (fTable[index] == null) // Checks if position in the array is empty
        {
            return 0; //position is empty return 0.

        } else
            node = fTable[index];

        while (node != null) {
            if (key.equals(node.getKey()))

                return node.getValue();
            else
                node = node.getNextNode();
        }

        return 0;
    }

    private void resize() {
        FreqBucket[] tmp = fTable;                     // Store the current table                   
        FreqBucket next;                               // Store the next element for traversing
        fTable = new FreqBucket[fTable.length * 2];    // Create a new hasfTable and double the size.

        for (FreqBucket node : tmp) {
            next = node;
            while (next != null) {
                // Check if next is null
                if (next == null)
                    break;
                else {
                    // Rehash element
                    this.put(next.getKey(), next.getValue());
                    // Move on to the next element
                    next = next.getNextNode();
                }
            }
        }
    }



    public String[] getKeys() {
        // Create the arrayList
        ArrayList<String> keys = new ArrayList<String>();
        // Store the next element for traversing
        FreqBucket next;

        for (FreqBucket element : fTable) {
            next = element;
            while (next != null) {
                // Check if next is null
                if (next == null) {
                    // Element does not exist, break
                    break;
                } else {
                    // Add the key
                    keys.add(element.getKey());
                    System.out.println(element.getKey());
                    next = next.getNextNode();
                    break;
                }
            }
        }
        // Return the keys
        return keys.toArray(new String[keys.size()]);
    }




    // The following method takes data from a frequency table object and stores it for later use. Keys and values get there own folder and files.
    public void writeFile2(String k, String v)
            throws IOException {
        k = "key_persistance/" + k +".txt";     //Takes the paramater k(which is an index number from yelpData class) and appends it the file path creating the file name.
        v = "val_persistance/" + v +".txt";     //Takes the paramater v(which is an index number from yelpData class) and appends it the file path creating the file name.
        File file = new File(k);                // create the file to store the keys/
        File file2 = new File(v);               // create the file to store values.
        RandomAccessFile fos;                   // create the random access file object for key
        RandomAccessFile raf;                   // create random accsess file for value
        raf = new RandomAccessFile(file2, "rw"); // make the RAF's readable and writeable
        fos = new RandomAccessFile(file, "rw");
        long currentPos = 0;     // creates a means of tracking and maintaining the position of the pointer in file.
        long currentPos2 = 0;
        // ByteBuffer buffer;


        FileChannel fileChannel = fos.getChannel();     // Create and open file channels for each RAF.
        FileChannel valueChannel = raf.getChannel();
        String key = "";
        int val = 0;
        String value = "";
        FreqBucket node;
        for (FreqBucket fb : fTable) {
            node = fb;
            while (node != null) {

                if(node == null)
                    break;
                else {
                    key = node.getKey()+",";
                    val = node.getValue();
                    value = Integer.toString(val)+",";

                    //System.out.println(key);
                    node = node.getNextNode();
                    for(int i = key.length();  i < 51; i++ ){

                        key = key + " ";
                        //System.out.println(key);
                    }
                    for(int i = value.length();  i < 5; i++ ){

                        value = value + " ";
                        //System.out.println(key);
                    }
                    byte[] inputBytes = key.getBytes();
                    byte[] valueBytes = value.getBytes();

                    ByteBuffer buffer = ByteBuffer.wrap(inputBytes);
                    ByteBuffer buf = ByteBuffer.wrap(valueBytes);

                    fos.seek(currentPos);
                    raf.seek(currentPos2);

                    fileChannel.write(buffer);
                    valueChannel.write(buf);
                    currentPos = fos.getFilePointer();
                    currentPos2 = raf.getFilePointer();

                }
            }
            // System.out.println(key);
        }
        fileChannel.close();
        valueChannel.close();

        fos.close();
        raf.close();

    }
    public FrequencyTable readTable(String p1, String p2) throws IOException {
        FileReader fr = new FileReader("key_persistance/"+ p1 + ".txt");
        FileReader fr2 = new FileReader("val_persistance/"+ p2 + ".txt");

        BufferedReader br = new BufferedReader(fr);
        BufferedReader br2 = new BufferedReader(fr2);

        String textRead = br.readLine();
        String textRead2 = br2.readLine();

        //System.out.println("File contents: ");

        String in,in2;


        String[] delim, delim2;
        FrequencyTable table = new FrequencyTable();


        while (textRead != null) {
            textRead = textRead.replaceAll(" ","");
            textRead2 = textRead2.replaceAll(" ","");
            in = textRead;
            in2 = textRead2;

            delim = in.split(",");
            delim2 = in2.split(",");
            for (int i = 0; i < delim.length ; i++) {
                //System.out.println(delim[i]+ "      " + i);
                table.put(delim[i],Integer.parseInt(delim2[i]));
            }

            textRead = br.readLine();
            textRead2 = br2.readLine();
        }

        fr.close();
        fr2.close();

        br.close();
        br2.close();
        return table;

    }

}




















































/*public void resize2()
{//re

   FreqBucket[] temp = fTable;
   FreqBucket node;
   fTable = new FreqBucket[fTable.length * 2];

   for(int i = 0; i < temp.length; i++)
   {

      node = temp[i];

      if(node == null)
         break;
      else
      if(node != null)
      {

         if(node.equals(node.getKey()))
            this.put(node.getKey(),node.getValue());

         node = temp[i].getNextNode();
         //System.out.println("*****");
      }



   }*/
