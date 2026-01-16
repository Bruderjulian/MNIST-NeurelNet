public class Main {

  public static void main(String[] args) {
    //DatasetImage.loadDataset("test", "./data/mnist_test.csv", 10000);
    DatasetImage.loadDataset("training", "./data/mnist_train.csv", 10000);
    Network network = new Network(
      new int[] { 784, 24, 16, 10 },
      ActivationFunctions.sigmoid,
      0.0002
    );
    //network.load("./models/mnist7.csv");

    //network.estimate(DatasetImage.testData);
    network.generate();
    long time = System.currentTimeMillis();
    for (int i = 0; i < 1; i++) {
      network.train(DatasetImage.trainingData, 100);
      network.save("./models/mnist7.csv");
    }
    long duration = (System.currentTimeMillis() - time) / 1000;
    System.out.println(
      "Time: " + String.format("%02d:%02d", (duration / 60) % 60, duration % 60)
    );
  }
}
