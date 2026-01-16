import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

// displays the number as an image and draws the labels for the output and target
public class Display extends Actor {

  public static int pixelMultiplier = 6;
  public static int borderSize = 4;
  public static int textSize = 22;

  private GreenfootImage image;
  private Label[] output_labels = new Label[10];
  private Label[] target_labels = new Label[10];
  private double[] output = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

  public Display() {
    image =
      new GreenfootImage(
        DatasetImage.dimX * pixelMultiplier,
        DatasetImage.dimY * pixelMultiplier
      );
    setImage(image);
  }

  public void addedToWorld(World world) {
    clear();
  }

  public void clear() {
    // clear display and draw black background with light_grey border
    image.clear();
    image.setColor(Color.BLACK);
    image.fillRect(0, 0, image.getWidth(), image.getHeight());

    for (int i = 0; i < 10; i++) {
      getWorld().removeObject(target_labels[i]);
      target_labels[i] = new Label(String.valueOf(i) + ":", textSize);
      getWorld()
        .addObject(target_labels[i], image.getWidth() + 60, 55 + 22 * i);

      getWorld().removeObject(output_labels[i]);
      output_labels[i] = new Label(String.valueOf(i) + ":", textSize);
      getWorld()
        .addObject(output_labels[i], image.getWidth() + 120, 55 + 22 * i);
    }
    setImage(image);
  }

  public void update(DatasetImage imageObj) {
    // clear display
    clear();
    // creates an Image from the pixel values
    setImage(imageObj.toImage(pixelMultiplier));

    for (int i = 0; i < 10; i++) {
      getWorld().removeObject(target_labels[i]);
      if (i == imageObj.label) {
        target_labels[i] = new Label(String.valueOf(i) + ": 1", 22);
      } else {
        target_labels[i] = new Label(String.valueOf(i) + ": 0", 22);
      }
      getWorld()
        .addObject(target_labels[i], image.getWidth() + 60, 55 + 22 * i);
    }
  }

  public void setOutput(double[] output_data, int label) {
    output = output_data;
    double outputValue = output_data[label];
    for (int i = 0; i < 10; i++) {
      getWorld().removeObject(output_labels[i]);

      String str = String.valueOf(i) + ": " + String.format("%.2f", output[i]);
      Color color = label == i
        ? Color.MAGENTA
        : output_data[i] - outputValue > 0.1 ? Color.ORANGE : Color.BLACK;
      output_labels[i] = new Label(str, textSize, color);

      getWorld()
        .addObject(output_labels[i], image.getWidth() + 120, 55 + 22 * i);
    }
  }
}
