/*
Name: Joseph M. Scollo
School: Suny Oswego
Course: CSC-365 Data Structures

Class: HashBuckut
About Class: This class contains 5 methods and a construcor. The details on their members and how the function is documented
             throughout the code. The class creates hash buckets, set and gets values of nodes keys and values.
*/


public class HashBucket 
{

   String key;         // A private String varriable that stores a key. 
   String value;       // A private Sttring varriable that stores a value, i.e in this case, the word frequency
   HashBucket next;    // the next node
                 
       
   //-------------------------------------------------------------------//
   // constructor that sets the key, the value and sets next to null    //
   //-------------------------------------------------------------------//
      
    public HashBucket(String K, String V)       
    {
      key = K;         // Set the key
      value = V;       // Set the value
      next = null;     // Set the next element
    }
        
    //------------------------------------------------------------//
    // Method that gets a String value and returns it.            //
    //------------------------------------------------------------//

     public String getValue() 
     {
       return value;
     }
        
    //------------------------------------------------------------//
    // Method that sets the String value.                         //
    //------------------------------------------------------------//

     public void setValue(String v) 
     {
       value = v;
     }
        
     //------------------------------------------------------------//
     // Method that gets a key and returns it.                     //
     //------------------------------------------------------------//

     public String getKey() 
     {
       return key;
     }
        
               
     //------------------------------------------------------------//
     // Method that gets the next node.                            //
     //------------------------------------------------------------//

     public HashBucket getNextBucket() 
     {
       return next;
     } //get next bucket
        
     //------------------------------------------------------------//
     // Method that sets the next node.                            //
     //------------------------------------------------------------//

     public void setNextBucket(HashBucket n) 
     {
       next = n;
     }    //setnextbucket      
}          
            
    

