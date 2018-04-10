/*
Name: Joseph M. Scollo
School: Suny Oswego
Course: CSC-365 Data Structures

Class: Main 
About Class: This class contains the main method and methods that impllement a basic graphical user interface that
                  consists of a choicebox with six busess names to choose from a clickable button that begins the program
                  and a text result.
*/
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;


public class Main extends Application
{
   Stage window;          
   Scene scene;             
   Button button;
   Text t;
   Text t2;             
   Text t3;
   String id = "";
   String output = "";
    
   public static void main(String[] args)throws IOException //main method
   {
     launch(args);
   }

   @Override
   public void start(Stage primaryStage) throws Exception 
   {
      window = primaryStage;      window.setTitle("Yelp Data set Challenge");  //sets the title of the window
      button = new Button("Search");               //creates a new button;
      t = new Text();                              //creates a bew text object
      t2 = new Text();
      t3 = new Text();
      String output = "";
      t.setText("Select a choice from the drop down box"); // lines 54-60 set up text size, color, and font. also sets the text. 
      t.setFont(Font.font ("Verdana", 12));
      t.setFill(Color.BLACK);
      t2.setFont(Font.font ("Verdana",12));
      t2.setFill(Color.BLACK);
      t3.setFont(Font.font ("Veranda",12));
      t3.setFill(Color.BLACK);
      ChoiceBox<String> choiceBox = new ChoiceBox<>(); // creates a new object of choice box
    
      choiceBox.getItems().add("John's Chinese BBQ Restaurant"); //getItems returns the ObservableList object 
      choiceBox.getItems().add("Subway");                        //which you can add items to
      choiceBox.getItems().add("Double Play Sports Bar");
      choiceBox.getItems().add("Dubliner");
      choiceBox.getItems().add("Joey D's Bar & Grille");
      choiceBox.getItems().add("Sams Club Optical");
      choiceBox.setValue("John's Chinese BBQ Restaurant");      //Set a default value

      button.setOnAction(e -> getChoice(choiceBox)); // takes selection from choice box when button is clicked
     
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(t,choiceBox, button,t3,t2);

        scene = new Scene(layout, 300, 250);
        window.setScene(scene);
        window.show();
  
   }
   //To get the value of the selected item
   private void getChoice(ChoiceBox<String> choiceBox)
   {
      String name = choiceBox.getValue();                        //sets name = to the value selected in the choice box.
      t3.setText("Your selection is most closely related to: "); 
         
      if(name == "John's Chinese BBQ Restaurant")              
         id = "--6MefnULPED_I942VcFNA";
      else
      if(name == "Subway")
         id = "-05uZNVbb8DhFweTEOoDVg";
       else
       if(name == "Double Play Sports Bar")                   //lines 88 - 103 are conditional that pass a particular value to id 
         id = "--q7kSBRb0vWC8lSkXFByA";                       //based off choice box selection.
       else
       if(name == "Dubliner")
         id = "-0tgMGl7D9B10YjSN2ujLA";
       else
       if(name == "Joey D's Bar & Grille")
         id = "-40RDS4F54qiGiEQUsZaXA";
       else
         id = "-5gSPcauy5_JCQeHiJgFmw";
       try
       {
         yelpData data = new yelpData();   
         data.setUserEntry(id);       // passes the id to the yelpData class
         data.run();                  // runs algorithyms in yelpData class
         output = data.getOutput();   // pulls the output from the get data class
         t2.setText(output);
       }
       catch(Exception e)
       {
       }
   }
}
