public class Network {

  private int[] structure;
  private double learningRate;
  private ActivationFunctions.FunctionBundle func;
  private Layer[] layers;
  public int layersCount;

  public String model;
  public double loss;

  public Network(
    int[] structure,
    ActivationFunctions.FunctionBundle func,
    double learningRate
  ) {
    this.structure = structure;
    this.learningRate = learningRate;
    this.func = func;
    this.layersCount = structure.length;
    this.layers = new Layer[layersCount];
    for (int i = 0; i < layersCount; i++) {
      this.layers[i] = new Layer();
    }
  }

  // computes the output of the network given an DatasetImage as input
  // it iterates through the layers and computes the output of each layer
  // and returns the output of the last layer
  // it skips the first layer because it is the input layer
  public double[] compute(DatasetImage imageObj) {
    double[] input = imageObj.data;
    for (int i = 1; i < layersCount; i++) {
      input = layers[i].compute(input, func);
    }
    return input;
  }

  private void learn(double[] target) {
    // calculates the deltas of the last layer
    Neuron[] neurons = this.layers[layersCount - 1].neurons;
    int neuronsLen = neurons.length;
    double[] deltas = new double[neuronsLen];
    for (int i = 0; i < neuronsLen; i++) {
      Neuron neuron = neurons[i];
      // uses the derivative of the activation function to calculate how much the output of the neuron should change
      deltas[i] =
        (neuron.lastOutput - target[i]) * func.derivative(neuron.lastSum);
    }
    // propagates the deltas backwards through the network and updates the weights accordingly
    for (int i = layersCount - 1; i > 0; i--) {
      deltas = this.layers[i].propagate(deltas, learningRate, func);
    }
  }

  public void train(DatasetImage[] data, int epochs) {
    double[] actualOutput;
    int dataLen = data.length;
    // epochs is the number of times the network will be trained on the same data
    for (int epoch = 0; epoch < epochs; epoch++) {
      double totalLoss = 0;
      // use the whole dataset for each epoch (inefficient but simple)
      // it is also possible to use a portion of the data for each epoch
      for (int i = 0; i < dataLen; i++) {
        // compute the output of the network and get the actual output
        // compute the mean squared error of the output and the actual output afterwards
        // output of the network is not actually used because all nessary data for learning is stored in each neuron
        actualOutput = data[i].target;
        totalLoss += mse(this.compute(data[i]), actualOutput);
        // compute the loss and backpropagate the error
        learn(actualOutput);
      }
      // compute the average loss of the epoch and logs the current epoch and the loss
      loss = totalLoss / dataLen;
      System.out.print("Epoch " + epoch + ", Average Loss: " + loss + "\n");
    }
  }

  // generates a new network with the given structure
  public void generate() {
    for (int i = 0; i < structure.length; i++) {
      layers[i].generate(func, structure[i], i == 0 ? -1 : structure[i - 1]);
    }
  }

  // returns the total amount of possible modifieable values in the network
  // not actually used :-)
  public int getCount() {
    int sum = 0;
    for (int i = 1; i < layersCount; i++) {
      sum += layers[i].getCount();
    }
    return sum;
  }

  public void estimate(DatasetImage[] data) {
    int dataLen = data.length;
    double[] output;
    double[] target;
    int failedCount = 0;
    for (int i = 0; i < dataLen; i++) {
      output = this.compute(data[i]);
      target = data[i].target;
      for (int j = 0; j < output.length; j++) {
        if (Math.abs(output[j] - target[j]) > 0.3) {
          failedCount++;
          break;
        }
      }
    }
    System.out.println(
      "Avg. Failed: " + String.format("%.2f", (double) failedCount / dataLen)
    );
  }

  // loads a network from a file and reconstructs it from it
  public void load(String path) {
    NetworkData data = NetworkData.load(path);
    model = data.model;
    loss = data.loss;

    int len = data.layers.length;
    layers = new Layer[len];
    for (int i = 0; i < len; i++) {
      layers[i] = new Layer();
      this.layers[i].load(data.layers[i]);
    }
  }

  public void save(String path) {
    NeuronData[][] neuronsData = new NeuronData[layersCount][];
    for (int i = 0; i < layersCount; i++) {
      neuronsData[i] = layers[i].save();
    }
    NetworkData data = new NetworkData(model, loss, structure, neuronsData);
    NetworkData.save(path, data);
  }

  private static double mse(double[] predicted, double[] actual) {
    int len = actual.length;
    double sum = 0;
    double a, p;
    for (int i = 0; i < len; i++) {
      a = actual[i];
      p = predicted[i];
      sum += (p - a) * (p - a);
    }
    return sum / len;
  }
}
