import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

public class MyWorld extends World {

  private Display display;
  private Network network;
  private Label timeLabel;
  private Label correctLabel;
  private int totalCount = 1;

  private ArrayList<Integer> failed = new ArrayList<Integer>();

  public MyWorld() {
    super(600, 600, 1);
    display = new Display();
    addObject(display, 110, 130);
    addObject(new Label("Target:", 21), 220, 25);
    addObject(new Label("Output:", 21), 290, 25);
    addObject(new Button(80, 35, "Next", "nextImage", 20), 65, 240);
    addObject(new Button(80, 35, "Clear", "clearImage", 20), 154, 240);
    addObject(new Button(80, 35, "See Failed", "showFailedImage", 20), 65, 290);
    addObject(
      new Button(100, 35, "Estimate Failed", "estimateFailed", 20),
      170,
      290
    );
    addObject(new Button(100, 35, "Train", "train", 20), 65, 340);

    // load datasets create a new network and load the model
    DatasetImage.loadDataset("test", "./data/mnist_test.csv", 1000);
    //DatasetImage.loadDataset("training", "./data/mnist_train.csv", 1000);
    // the network expects a structure as an integer array, the activation function
    // and the learning rate
    // for create a new network use the .generate() method
    network =
      new Network(
        new int[] { 748, 24, 16, 10 },
        ActivationFunctions.sigmoid,
        0.01
      );
    // loads the current model
    //network.generate();
    network.load("./models/mnist6.csv");

    // add all the labels
    addObject(
      new Label("Model: " + String.valueOf(network.model), 24),
      500,
      80
    );
    addObject(new Label("Loss: " + String.valueOf(network.loss), 24), 500, 120);
    timeLabel = new Label("Time: -1", 24);
    addObject(timeLabel, 500, 160);
    correctLabel = new Label("Avg Failed: 0.00", 24);
    addObject(correctLabel, 500, 200);
  }

  // returns the Display
  public Display getDisplay() {
    return display;
  }

  // returns the network
  public Network getNetwork() {
    return network;
  }

  // returns the time label
  public Label getTimeLabel() {
    return timeLabel;
  }
  
  // resets display and counters
  public void clear() {
      failed.clear();
      totalCount = 0;
      display.clear();
  }

  // retrieve a new image from the dataset
  // updates the network (computes the output) for the image
  // and updates the display
  public void updateNetwork() {
    // get the next image from the dataset
    DatasetImage imageObj = getNextImage();
    long time = System.currentTimeMillis();
    // compute the output for the image
    double[] result = network.compute(imageObj);

    String timeStr = String.valueOf(
      Math.round(System.currentTimeMillis() - time)
    );
    timeLabel.set("Time: " + timeStr + "ms");
    // checks the result (ui stuff)
    // also stores the images if it's a failed image
    checkResult(result, imageObj);
    // updates the display
    display.setOutput(result, imageObj.label);
  }

  // shows one of the failed images
  public void showFailedImage() {
    if (failed.size() == 0) return;
    // get a random image from the failed images and update the display with it
    int idx = Greenfoot.getRandomNumber(failed.size());
    DatasetImage imageObj = DatasetImage.testData[failed.get(idx)];
    display.update(imageObj);

    // computes the output for the failed image
    long time = System.currentTimeMillis();
    double[] result = network.compute(imageObj);

    // updates the display with the results
    String timeStr = String.valueOf(
      Math.round(System.currentTimeMillis() - time)
    );
    timeLabel.set("Time: " + timeStr + "ms");

    display.setOutput(result, imageObj.label);
  }

  // estimates the precentage of failed images in the dataset
  public void estimateFailed() {
    clear();
    long time = System.currentTimeMillis();

    // goes through the dataset and computes the output for each image
    // and checks if it's a failed image
    int len = DatasetImage.testData.length;
    for (int i = 0; i < len; i++) {
      DatasetImage imageObj = DatasetImage.testData[i];
      double[] result = network.compute(imageObj);
      checkResult(result, imageObj);
    }

    // updates the label
    String timeStr = String.valueOf(
      Math.round(System.currentTimeMillis() - time)
    );
    timeLabel.set("Time: " + timeStr + "ms");
    correctLabel.set(
      "Avg. Failed: " +
      String.format("%.2f", (double) failed.size() / totalCount)
    );
  }

  // trains the network on the dataset
  public void train() {
    network.train(DatasetImage.testData, 100);
    // network.save("./data/mnist2.csv");
  }

  // gets the next image from the dataset
  public DatasetImage getNextImage() {
    int idx = Greenfoot.getRandomNumber(DatasetImage.testData.length);
    DatasetImage imageObj = DatasetImage.testData[idx];
    display.update(imageObj);
    return imageObj;
  }

  // checks if the result is correct and updates the failed list if it's a failed image
  // a image has failed if one result is bigger than the correct result by 0.1
  public void checkResult(double[] result, DatasetImage imageObj) {
    if (failed.contains(imageObj.idx)) {
      return;
    }
    double correctResult = result[imageObj.label];
    for (int i = 0; i < result.length; i++) {
      if ((result[i] - correctResult) > 0.1 && i != imageObj.label) {
        failed.add(imageObj.idx);
        break;
      }
    }
    totalCount++;
  }
}
