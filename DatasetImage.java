//import greenfoot.Color;
//import greenfoot.GreenfootImage;
import greenfoot.Color;
import greenfoot.GreenfootImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

// represents an image from the MNIST dataset
// each image is 28x28 pixels and stored as an array of doubles (64bit floating point numbers)
// the values in the array are in the range 0.0 to 255.0
// A label is also stored with the image, indicating the number it represents
public class DatasetImage {

  // index of the image in the dataset (used for finded the specific image)
  public int idx;
  // represented number of the image (0-9)
  public int label;
  // pixel data of the image (784 values as 64bit floating point numbers)
  public double[] data;
  public double[] target;

  // dimensions of the images
  public static int dimX = 28;
  public static int dimY = 28;

  // images used for training
  public static DatasetImage[] trainingData;
  // images used for testing
  public static DatasetImage[] testData;

  public DatasetImage(int label, int idx, double[] data) {
    this.label = label;
    this.idx = idx;
    this.data = data;
    target = new double[10];
    target[label] = 1;
  }

  // convert the dataset image to a greenfoot image
  public GreenfootImage toImage(int pixelMultiplier) {
    GreenfootImage image = new GreenfootImage(
      dimX * pixelMultiplier,
      dimY * pixelMultiplier
    );
    // draw number on number Display
    for (int i = 0; i < DatasetImage.dimY; i++) {
      for (int j = 0; j < DatasetImage.dimX; j++) {
        // get pixel value from array
        int value = (int) Math.round(data[i * dimY + j]);
        // assume each color has the same color proportions
        image.setColor(new Color(value, value, value));
        // draw a rectangle at the correct position
        image.fillRect(
          j * pixelMultiplier,
          i * pixelMultiplier,
          pixelMultiplier,
          pixelMultiplier
        );
      }
    }
    return image;
  }

  // loading the dataset from a .csv (comma-seperated) file
  public static void loadDataset(String setType, String path, int amount) {
    // create empty array to store all images
    HashSet<DatasetImage> setData = new HashSet<DatasetImage>(amount);
    try {
      // create a file Scanner
      BufferedReader reader = new BufferedReader(new FileReader(path));
      // line counter and line string
      int idx = 0;
      String line;

      // Ignore first line
      reader.readLine();

      // read to the specified amount of lines if possible
      while ((line = reader.readLine()) != null && idx < amount) {
        // read the next line and parse it to a DatasetImage Object
        // containing all the data

        // split the line by commas
        String[] split = line.split(",");
        // get the label of the image
        int label = Integer.parseInt(split[0]);
        // create empty array for all 28x28 pixel values
        double[] data = new double[28 * 28];

        // fill the array with the pixel values
        // ignore the first value, as it is the label of the image
        for (int i = 1; i < split.length; i++) {
          // convert the string to a double
          data[i - 1] = Double.parseDouble(split[i]);
        }
        // save the dataset image with the label, index and the pixel data
        setData.add(new DatasetImage(label, idx, data));
        idx++;
      }

      System.out.println("Loaded " + idx + " images");

      // close scanner to save resscources
      reader.close();
      // save the dataset
      if (setType.equals("training")) {
        trainingData = setData.toArray(new DatasetImage[0]);
      } else {
        testData = setData.toArray(new DatasetImage[0]);
      }
    } catch (Exception e) {
      // print out the error/exception if one occurs
      System.out.println("Failed to load Dataset: " + e.getCause());
      e.printStackTrace();
    }
  }
}
