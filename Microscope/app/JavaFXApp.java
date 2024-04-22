import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.*;

import javafx.concurrent.*;

import javafx.beans.*;
import javafx.beans.value.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.*;
import javafx.concurrent.*;
import javafx.beans.*;
import javafx.beans.value.*;
import java.nio.ByteBuffer;
import javafx.scene.Group;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.scene.canvas.GraphicsContext;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

import cam.Frames;

import java.io.*;
import javax.imageio.*;

///ZEBY ZAPISAC TRZEBA NACISNAC 2 RAZY!!!!!

public class JavaFXApp extends Application
 {
    private static final int FRAME_WIDTH  = 640;
    private static final int FRAME_HEIGHT = 480;


    GraphicsContext gc;
    Canvas canvas;
    byte buffer[];
    PixelWriter pixelWriter;
    PixelFormat<ByteBuffer> pixelFormat;

    Frames frames;
    Stage stage;

    int RGB_pixels[];
  public BufferedImage bi;

      FileChooser fileChooser;

    Cam cam;

  public static void main(String[] args) {
            launch(args);
	        }

@Override
  public void start(Stage primaryStage) {
  primaryStage.setTitle("JavaFX App");

  int result;

      Timeline timeline;

      frames = new Frames();

      result = frames.open_shm("/frames");

        canvas     = new Canvas(FRAME_WIDTH + 100, FRAME_HEIGHT + 100);
        gc         = canvas.getGraphicsContext2D();

        timeline = new Timeline(new KeyFrame(Duration.millis(130), e->disp_frame()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

      primaryStage.setTitle("Camera");

      Group root = new Group();
      canvas     = new Canvas(650, 490);
      gc         = canvas.getGraphicsContext2D();
    RGB_pixels = new int[FRAME_WIDTH*FRAME_HEIGHT];

  stage = primaryStage;

  Menu menu1 = new Menu("File");

    MenuItem menuItem1 = new MenuItem("Save RGB");
    MenuItem menuItem2 = new MenuItem("Save binary");
    MenuItem menuItem3 = new MenuItem("Save negative");
    MenuItem menuItem4 = new MenuItem("Save greyscale");
    MenuItem menuItem5 = new MenuItem("Exit");

      menu1.getItems().addAll(menuItem1);
      fileChooser = new FileChooser();
      fileChooser.setTitle("Save");
      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));


    menuItem2.setOnAction(n -> {
                                System.out.println("Save greyscale");
                              greyscale(menuItem2);
                             });

    menuItem4.setOnAction(n -> {
                                System.out.println("Save binary image");
                              binaryzacja(menuItem4);
                             });
    menuItem3.setOnAction(n -> {
                                System.out.println("Save negative");
                              cos_dialog(menuItem3);
                             });

    menuItem5.setOnAction(e -> {
                              System.out.println("Exit Selected");

                              exit_dialog();

                             });
    menuItem1.setOnAction(s -> {
                              System.out.println("Save RGB image");

                             Save_dialog(menuItem1);

                             });

  menu1.getItems().add(menuItem1);
  menu1.getItems().add(menuItem2);
  menu1.getItems().add(menuItem3);
  menu1.getItems().add(menuItem4);
  menu1.getItems().add(menuItem5);


  MenuBar menuBar = new MenuBar();

  menuBar.getMenus().add(menu1);

  VBox vBox = new VBox(menuBar);

  vBox.getChildren().add(canvas);
  Scene scene = new Scene(vBox, 960, 600);

  primaryStage.setScene(scene);

  primaryStage.setOnCloseRequest(e -> {
                                       e.consume();
                                       exit_dialog();
                                      });

  primaryStage.show();

 }

 public void item_1()
  {
   System.out.println("Save");
  }

public void Save_dialog(MenuItem item)
    {
   FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save");
      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
      item.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent event) {
            File selectedfile = fileChooser.showSaveDialog(stage);
            String filePath = selectedfile.getPath();
            System.out.println(filePath);

     System.out.println("Save image");

     //zapis do pliku filePath
     try {

        int i, j;

        j = 0;
        for(i = 0; i < RGB_pixels.length; i++)
        {
        RGB_pixels[i] = (int) (buffer[j] << 16) + (buffer[j+1]<< 8) + buffer[j+2];
        j+=3;
        }

        bi = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);

        bi.setRGB(0, 0, FRAME_WIDTH, FRAME_HEIGHT, RGB_pixels, 0, FRAME_WIDTH);


        ImageIO.write(bi, "png", selectedfile);
     } catch (IOException e) {

     }
         }
      });
  }


 public void exit_dialog()
  {
   System.out.println("exit dialog");


   Alert alert = new Alert(AlertType.CONFIRMATION,
                           "Do you really want to exit the program?.",
 			    ButtonType.YES, ButtonType.NO);

   alert.setResizable(true);
   alert.onShownProperty().addListener(e -> {
                                             Platform.runLater(() -> alert.setResizable(false));
                                            });

  Optional<ButtonType> result = alert.showAndWait();
  if (result.get() == ButtonType.YES)
   {
    Platform.exit();
   }
  else
   {
   }

  }


   public void cos_dialog(MenuItem item)
  {
  FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save");
      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
      //Adding action on the menu item
      item.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent event) {
            //Opening a dialog box
            File selectedfile = fileChooser.showSaveDialog(stage);
            String filePath = selectedfile.getPath();
            //String fileName = selectedfile.getName();
            System.out.println(filePath);
            //System.out.println(fileName);

     System.out.println("Save image in negative");

 try {
        int i, j;

        j = 0;
        for(i = 0; i < RGB_pixels.length; i++)
        {
        RGB_pixels[i] = (int) (buffer[j] << 16) + (buffer[j+1]<< 8) + buffer[j+2];
        j+=3;
        }

        bi = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);

        bi.setRGB(0, 0, FRAME_WIDTH, FRAME_HEIGHT, RGB_pixels, 0, FRAME_WIDTH);

        for (int y = 0; y < FRAME_HEIGHT; y++) {
            for (int x = 0; x < FRAME_WIDTH; x++) {
                int p = bi.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // subtract RGB from 255
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                // set new RGB value
                p = (a << 24) | (r << 16) | (g << 8) | b;
                bi.setRGB(x, y, p);
            }
        }

        ImageIO.write(bi, "png", selectedfile);
     }catch (IOException e) {

     }
         }
      });
}

 public void binaryzacja(MenuItem item)
  {
  FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save");
      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
      //Adding action on the menu item
      item.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent event) {
            //Opening a dialog box
            File selectedfile = fileChooser.showSaveDialog(stage);
            String filePath = selectedfile.getPath();
            //String fileName = selectedfile.getName();
            System.out.println(filePath);
            //System.out.println(fileName);

     System.out.println("Save image in grey");

 try {
        int i, j;

        j = 0;
        for(i = 0; i < RGB_pixels.length; i++)
        {
        RGB_pixels[i] = (int) (buffer[j] << 16) + (buffer[j+1]<< 8) + buffer[j+2];
        j+=3;
        }


        bi = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);

        bi.setRGB(0, 0, FRAME_WIDTH, FRAME_HEIGHT, RGB_pixels, 0, FRAME_WIDTH);

        int[] pixels = bi.getRGB(0, 0, FRAME_WIDTH, FRAME_HEIGHT, null, 0, FRAME_WIDTH);

        for (int l = 0; l < pixels.length; l++) {

            // Here i denotes the index of array of pixels
            // for modifying the pixel value.
            int p = pixels[l];

            int a = (p >> 24) & 0xff;
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            int avg = (r + g + b) / 3;

            // replace RGB value with avg
            p = (a << 24) | (avg << 16) | (avg << 8) | avg;

            pixels[l] = p;
        }
    bi.setRGB(0, 0, FRAME_WIDTH, FRAME_HEIGHT, pixels, 0, FRAME_WIDTH);


        ImageIO.write(bi, "png", selectedfile);
     }catch (IOException e) {

     }
         }
      });
}


public void greyscale(MenuItem item)
  {
  FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save");
      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
      //Adding action on the menu item
      item.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent event) {
            //Opening a dialog box
            File selectedfile = fileChooser.showSaveDialog(stage);
            String filePath = selectedfile.getPath();
            //String fileName = selectedfile.getName();
            System.out.println(filePath);
            //System.out.println(fileName);

     System.out.println("Save image in grey");

 try {
        int i, j;

        j = 0;
        for(i = 0; i < RGB_pixels.length; i++)
        {
        RGB_pixels[i] = (int) (buffer[j] << 16) + (buffer[j+1]<< 8) + buffer[j+2];
        j+=3;
        }


        bi = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);

        bi.setRGB(0, 0, FRAME_WIDTH, FRAME_HEIGHT, RGB_pixels, 0, FRAME_WIDTH);

//         int[] pixels = bi.getRGB(0, 0, FRAME_WIDTH, FRAME_HEIGHT, null, 0, FRAME_WIDTH);

        int h=FRAME_HEIGHT;
        int w=FRAME_WIDTH;
              BufferedImage bufferedImage = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
       if (bi == null) {
             System.out.println("No image loaded");
        }
       else {
           for(int k=0;k<w;k++)
           {
               for(int l=0;l<h;l++)
               {


                    //Get RGB Value
                    int val = bi.getRGB(k, l);
                    //Convert to three separate channels
                    int r = (0x00ff0000 & val) >> 16;
                    int g = (0x0000ff00 & val) >> 8;
                    int b = (0x000000ff & val);
                    int m=(r+g+b);
                    //(255+255+255)/2 =283 middle of dark and light
                    if(m>=383)
                    {
                        // for light color it set white
                        bufferedImage.setRGB(k, l, Color.WHITE.getRGB());
                    }
                    else{
                        // for dark color it will set black
                        bufferedImage.setRGB(k, l, 0);
                    }
                }
            }
        }

        ImageIO.write(bufferedImage, "png", selectedfile);
     }catch (IOException e) {

     }
         }
      });
}



    private void disp_frame()
     {

      pixelWriter = gc.getPixelWriter();
      pixelFormat = PixelFormat.getByteRgbInstance();


      buffer = frames.get_frame();
      pixelWriter.setPixels(5, 5, FRAME_WIDTH, FRAME_HEIGHT, pixelFormat, buffer, 0, FRAME_WIDTH*3);

     }
}
