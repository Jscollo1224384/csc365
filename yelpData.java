/*
Name: Joseph M. Scollo
School: Suny Oswego
Course: CSC-365 Data Structures

Class: yelpFata 
About Class: This class contains the the algorythims and organization of the yelpData set. This is where the data is past thoroug a
             HashTable, word frequency table, passes through similarity metric vectors and managed in array lists.
*/
import java.util.*;
import java.io.*;

public class yelpData 
{//begin yelp class
     
   String userEntry, output, newLine, newLine2, newLine3, newLine4, bName, bid, rev; 
   HashTable table;
   FrequencyTable freq; 
   BufferedReader br, br2, br3, br4; 
   ArrayList<String> lines, lines2, tmp, tmp2, hybridList, keyFileList, valFileList;
   ArrayList<Integer> vector1, vector2;
   double[] metrics; 
   CosineSimilarity cosine;
   FrequencyTable[] hObj;
   String[] names, ids, delim; 
   int count, count2;
   BTree tree;
   public yelpData()throws IOException        //constructor
   {
      table = new HashTable();
      freq = new FrequencyTable();
      hObj = new FrequencyTable[296];
         br = new BufferedReader(new FileReader("busID_10000.csv"));
      br2 = new BufferedReader(new FileReader("rev2_10000.csv"));
      br3 = new BufferedReader(new FileReader("names_296.csv"));
      br4 = new BufferedReader(new FileReader("ids_296.csv"));
      tmp = new ArrayList<String>();
      tmp2 = new ArrayList<String>();   
      hybridList = new ArrayList<String>();
      vector1 = new ArrayList<Integer>();
      vector2 = new ArrayList<Integer>();
      cosine = new CosineSimilarity();
      lines = new ArrayList<String>();
      lines2 = new ArrayList<String>();
      keyFileList = new ArrayList<String>();
      valFileList = new ArrayList<String>();
      metrics = new double[296];
      names = new String[296];
      ids = new String[296];
      count = 0;
      count2 = 0;
      tree = new BTree(7);

   }
   
   public void setUserEntry(String user) // setter that takes a Srting id from the main class and sets assigns it to userEntry.
   {
      userEntry = user;
   }
   public String getOutput()  // a getter method that get the output value and returns it. The main class grabs this and displays
   {                          // output at runtime.
      return output;
   }
   
   public void run()throws IOException  // method that runs all the algorithyms.
   {//begin run
   
      System.out.println(userEntry);
      while ((newLine = br.readLine()) != null) //lines 65 - 93 use several buffered reader loops to pull data from 4 different
      {                                         //csv files. The files contain business id, names, and reveiw text. the are passed into
         lines.add(newLine);                    //into ArrayLists and and arrays prepared to put into the HashTable. 
      }
      
      br.close(); 
   
      while ((newLine2 = br2.readLine()) != null)    // ArrayList "lines" get the ID's and "lines2" get the reveiws.
      {
         lines2.add(newLine2);
      }
      
      br2.close();                                   
      
      while ((newLine3 = br3.readLine()) != null) 
      {
        names[count] = newLine3;                     // String Array "names" get the busines names and "ids" get the id's
        count++;                                     // a counter keeps track and loads them in 1 at a time.
      }
      
      br3.close();
      
      while ((newLine4 = br4.readLine()) != null) 
      {
         ids[count2] = newLine4;
         count2++;
      }

      br4.close();   
     String hFile = "hashkeys_vals/keys.txt";
     File f = new File(hFile);
     //if(!f.exists()) {
          for (int i = 0; i < lines.size(); i++)   // This for loop gets the id's from "lines and the reveiws from "lines2 and put them in
          {                                       // in the hash table. the id's are the keys and th reveiws the are the values.
              bid = lines.get(i);
              rev = lines2.get(i);
              table.put(bid, rev);
              //tree.insert(bid,rev);
          }
         // table.writeFile2("keys", "values");
     // }
       for (int i = 0; i <ids.length ; i++) {
              tree.insert(ids[i],table.get(ids[i]));

       }
      String kFile = "key_persistance/0.txt";
      File file = new File(kFile);
      if(file.exists()) {

          for (int i = 0; i < ids.length; i++)//this uses the id's from the array and passes them to the hash tables get method.
          {//for                             //the hashtable gives the coresponding reveiws.

              FrequencyTable current = new FrequencyTable();
              current = current.readTable(Integer.toString(i), Integer.toString(i));

              tmp2 = current.getKeyList(); // gets all the word keys that were placed in the frequency table.
              for (String key : tmp2) {
                  if (hybridList.contains(key) != true)
                      hybridList.add(key);  // adds all keys to a master list.
              }

              hObj[i] = current;
              System.out.println(i + "************************************************************************************");
          }
      }
      else {
           System.out.println("before ids for");
           for (int i = 0; i < ids.length; i++)//this uses the id's from the array and passes them to the hash tables get method.
           {//for                             //the hashtable gives the coresponding reveiws.
               //bName = table.get(ids[i]);      //the reviews are assined to bName.
               bName = tree.setSearch(ids[i]);
               delim = bName.split(" ");       //delim takes reveiw from bName with a space as a delimiter and puts each idividual word
               //through a word frequency hash table where the word is the key and it's occurenses are values.
               FrequencyTable current = new FrequencyTable();
               //current.readFile();
               //keyFileList = current.getReadKeys();
               //valFileList = current.getReadVals();


               for (int j = 0; j < delim.length; j++) //This nested for loop takes every hObj object and gets puts words through the freq table.
               {//nFor
                   current.put(delim[j], 1);   //puts keys and value pairs in. default is 1 for value.
                   current.addKey(delim[j]);  // adds key to an arraylist.
                   //hObj[i] = current;

                   if (j == delim.length - 1) {//begin if
                       tmp2 = current.getKeyList(); // gets all the word keys that were placed in the frequency table.

                       for (String key : tmp2) {
                           if (hybridList.contains(key) != true)
                               hybridList.add(key);  // adds all keys to a master list.
                       }

                       hObj[i] = current;
                       System.out.println(i + "###################################################################################################################");
                       hObj[i].writeFile2(Integer.toString(i),Integer.toString(i));


                   }
               }
           }
       }//end of else
       //hObj[0].writeFile();
      // FrequencyTable f = new FrequencyTable();
      //f.readFile();
       String in;
       /*HashTable ta = new HashTable();
       if(f.exists()){
           ta = ta.readTable("keys","values");

           in = ta.get(userEntry);
       }
       else {
           in = table.get(userEntry); //assigns the reveiw to in by taking the userEntry that was assigned from the main class;
       }*/
       //in = table.get(userEntry);
       in = tree.setSearch(userEntry);
      delim = in.split(" ");
          for (int g = 0; g < delim.length; g++) // this for loop puts all the key value pairs from the main input through the word frequency table.
          {
              System.out.println(delim[g]);
              freq.put(delim[g], 1);    // adds word key value pairs.
              freq.addKey(delim[g]);
          }

          tmp = freq.getKeyList();  // gets the keys from the array list and assigns it to temp.


      for(String key:tmp)   
      {
         if(hybridList.contains(key)!= true)
            hybridList.add(key);   // all the user keys are passed to the hybrid list. note: if the hybrid list already takes keys
      }                            // it dosent already contain.
                        
      for(int v = 0; v < names.length; v++)                      // lines 148 - 159 use the master list to extract the values in order
      {//begin for                                              // and place them through the cosine vectors. 
         for(int t = 0; t < hybridList.size(); t++)
         {
             vector1.add(hObj[v].get(hybridList.get(t)));   // gets a word from the master list that was passed through from hObj[v]
             vector2.add(freq.get(hybridList.get(t)));// get method which returns its value to vector 1. vector to gets the word
         }                                                 // values from freq get method. 
            
         metrics[v] = cosine.cosineSimilarity(vector1,vector2); // metrics array getts assigned all cosine values.
         vector1 = new ArrayList<Integer>(); // resets vectors.
         vector2 = new ArrayList<Integer>();
         System.out.println("\n"+ v +": " + " " + metrics[v] + " " + names[v]);   
      
      }//end for
    
      double max = 0.0;
      double max2 = 0.0;
      double largest = 0.0;
      double secondLargest = 0.0;
      int entity = 0;
      int entity2 = 0;
      
      for(int m = 0; m < metrics.length; m++)            //lines 170 to 192 get the top two higest cosine similarities and pair them
      {                                                  // with business name.
         if(max < metrics[m])
         {
            max = metrics[m];
            entity = m;
         }
      }
      
      for(int i = 0; i < metrics.length; i++) 
      {
         if(metrics[i] > largest) 
         {
		      secondLargest = largest;
				largest = metrics[i];
         } 
         else 
         if(metrics[i] > secondLargest) 
         {
				secondLargest = metrics[i];
            entity2 = i;
			}
		}       
       
      output = (names[entity2]); // passes the output to the main.
      System.out.println("\nYour entered business is most similar to: " + names[entity]);   // additonal output for terminal
      System.out.println("It has a cosine similarity of: " + max);
      System.out.println("Your entered business is 2nd most similar to: " + names[entity2]);
      System.out.println("It has a cosine similarity of: " + secondLargest);
     // System.out.print(hObj[0].get("came"));
      //hObj[0].readFile();

   }
}    

