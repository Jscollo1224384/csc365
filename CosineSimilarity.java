/*
Name: Joseph M. Scollo
School: Suny Oswego
Course: CSC-365 Data Structures

Class: CosineSimilaity
About Class: This class implements a cosine similarity metric by taking data from 2 vectors and aplying a cosine simulary algorithm 
to the data. 

Source: https://stackoverflow.com/questions/520241/how-do-i-calculate-the-cosine-similarity-of-two-vectors. Code was modiied to work for my purposes.
*/

import java.util.*;

public class CosineSimilarity
{


   public static double cosineSimilarity(ArrayList<Integer> vectorA, ArrayList<Integer> vectorB) 
   {                                                                                                 // takes two parematers of type integer
                                                                                                  // and compares them using cosine vector.
      double dotProduct = 0.0;
      double normA = 0.0;
      double normB = 0.0;
    
      for (int i = 0; i < vectorA.size(); i++) 
      {
         dotProduct += vectorA.get(i) * vectorB.get(i);
         normA += Math.pow(vectorA.get(i), 2);
         normB += Math.pow(vectorB.get(i), 2);
      }   
      
      return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
   }
}
 