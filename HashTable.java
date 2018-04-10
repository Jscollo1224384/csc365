/*
Name: Joseph M. Scollo
School: Suny Oswego
Course: CSC-365 Data Structures

Class: HashTable 
About Class: This class contains  a constructor and 3 methods: put, get and resize. The table maintains business ID's from the yelp
             and there corresponding reviews from the yelp data set challenge.
             consists of a choicebox with six busess names to choose from a clickable button that begins the program
             and a text result.
*/

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

public class HashTable 
{//class
 
    private double LOADFACTOR; 
    private  int INITSIZE; 
    private HashBucket[] hTable;
    int elements; 
   
    public HashTable()
    {//constructor
      LOADFACTOR = .75;
      INITSIZE = 500;
      hTable = new HashBucket[INITSIZE];
      elements = 0;
    }
      
    
    
   public void put(String key, String value)
   {//put
      int index;
      index = Math.abs(key.hashCode()) % hTable.length;
      HashBucket node;
      
      if(hTable[index] == null) // Checks is position in the array is empty
      {//if   
         hTable[index] = new HashBucket(key, value);// creates a new hashbucket or "node ready to take an element's key and value.       
      }
      
      else
      {//else
         node = hTable[index];
     
         while(node != null) //while loop that handles collisions. The body of this loop is entered while an index is not null.
         {//begin while
            if(key.equals(node.getKey()))            // checks to see if the current key matches the key in the current index
            {  
                                                        // if the key mathches, increment its value by one.
               if(value != node.getValue())
               node.setValue(node.getValue() + value);
               return;
            }
            else
            if(node.getNextBucket() == null) //if the next node is null set that node to receive an element with its key and a value.
            {  
               node.setNextBucket(new HashBucket(key,value));
               elements ++;                                           //increment the element count;
               if(elements >= (hTable.length * LOADFACTOR))
               {                                              // check to see if if the table is equal or greator than 75% of the table size.
                  resize();
               }
               return;
            }
             
            else //the node has an elment get the next node.
            node = node.getNextBucket(); 
         }//end while
      }
                 
   }
   public String get(String key)
   {//get
      int index;
      index = Math.abs(key.hashCode()) % hTable.length;
      HashBucket node;
      
      if(hTable[index] == null)        // Checks if position in the array is empty
         return null;                   //position is empty return null.
      else
         node = hTable[index];
      
      while(node != null) 
      {
         if(key.equals(node.getKey()))        //If key is found return it's corresponding value.
           return node.getValue();
         else 
           node = node.getNextBucket();         //If no key is found traverse and get the next node.
      }
      
      return null;
   }

  
   private void resize() 
   {
      HashBucket[] tmp = hTable;                     // Store the current table                   
      HashBucket next;                               // Store the next element for traversing
      hTable = new HashBucket[hTable.length * 2];    // Create a new hashTable and double the size.

      for(HashBucket node : tmp) 
      {
         next = node;
         while(next != null) 
         {
                                     // Check if next is null
            if(next == null) 
               break;
            else 
            {
                                                          // Rehash element
               this.put(next.getKey(), next.getValue());
                                                          // Move on to the next element
               next = next.getNextBucket();
            }
         }
       }
    }
   public void writeFile2(String k, String v)
           throws IOException {
      k = "hashkeys_vals/" + k +".txt";     //Takes the paramater k(which is an index number from yelpData class) and appends it the file path creating the file name.
      v = "hashkeys_vals/" + v +".txt";     //Takes the paramater v(which is an index number from yelpData class) and appends it the file path creating the file name.
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
      String val = "";
      String value = "";
      HashBucket node;
      for (HashBucket hb : hTable) {
         node = hb;
         while (node != null) {

            if(node == null)
               break;
            else {
               key = node.getKey()+",";
               val = node.getValue()+"!@&";
               //value = Integer.toString(val)+",";

               //System.out.println(key);
               node = node.getNextBucket();
               for(int i = key.length();  i < 31; i++ ){

                  key = key + " ";
                  //System.out.println(key);
               }
               for(int i = value.length();  i < 5001; i++ ){

                  val = val + "#$%";
                  //System.out.println(key);
               }
               byte[] inputBytes = key.getBytes();
               byte[] valueBytes = val.getBytes();

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
   public HashTable readTable(String p1, String p2) throws IOException {
      FileReader fr = new FileReader("hashkeys_vals/"+ p1 + ".txt");
      FileReader fr2 = new FileReader("hashkeys_vals/"+ p2 + ".txt");

      BufferedReader br = new BufferedReader(fr);
      BufferedReader br2 = new BufferedReader(fr2);

      String textRead = br.readLine();
      String textRead2 = br2.readLine();

      //System.out.println("File contents: ");

      String in,in2;


      String[] delim, delim2;
      HashTable table = new HashTable();


      while (textRead != null) {
         textRead = textRead.replaceAll(" ","");
         textRead2 = textRead2.replaceAll("#$%","");
         in = textRead;
         in2 = textRead2;

         delim = in.split(",");
         delim2 = in2.split("!@&");
         for (int i = 0; i < delim.length ; i++) {
            //System.out.println(delim[i]+ "      " + i);
            table.put(delim[i],(delim2[i]));
         }
         System.out.println(delim2[1]);
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
   
                              
                              
                              
                           