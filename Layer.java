public class Layer {

  public Neuron[] neurons;
  public int amount;

  public Layer() {}

  // computes the output of the layer
  // it iterates through the neurons and computes the output of each neuron
  public double[] compute(
    double[] input,
    ActivationFunctions.FunctionBundle func
  ) {
    double[] output = new double[amount];
    for (int i = 0; i < amount; i++) {
      output[i] = neurons[i].compute(input, func);
    }
    return output;
  }

  /**
   * Performs backpropagation for the current layer during neural network
   * training.
   *
   * Calculates deltas for the previous layer by propagating error gradients
   * backwards,
   * updates neuron weights using the learning rate and computed deltas, and
   * prepares delta values for the previous layer.
   */
  public double[] propagate(
    double[] nextLayerDeltas,
    double learningRate,
    ActivationFunctions.FunctionBundle func
  ) {
    double[] previousLayerDeltas = new double[neurons[0].weightsCount];
    for (int i = 0; i < amount; i++) {
      Neuron neuron = neurons[i];
      double delta = func.derivative(neuron.lastSum) * nextLayerDeltas[i];
      neuron.updateWeights(learningRate, delta);

      for (int j = 0; j < neuron.weightsCount; j++) {
        previousLayerDeltas[j] += neuron.weights[j] * delta;
      }
    }
    return previousLayerDeltas;
  }

  // generates the random (dumb) neurons for the layer
  // based on the amount of neurons of this and the previous layer
  public void generate(
    ActivationFunctions.FunctionBundle func,
    int amount,
    int prev_amount
  ) {
    this.amount = amount;
    neurons = new Neuron[amount];
    for (int i = 0; i < amount; i++) {
      // when it is the input layer, the neurons are just a placeholder
      // and don't have any weights or biases
      if (prev_amount == -1) {
        neurons[i] = new Neuron(new double[] {}, 0);
        continue;
      }
      // generate the random weights and bias for the neuron
      double[] weights = new double[prev_amount];
      for (int j = 0; j < prev_amount; j++) {
        weights[j] = Layer.genNum();
      }
      neurons[i] = new Neuron(weights, Layer.genNum());
    }
  }

  // loads the data neurons into the layer
  public void load(NeuronData[] data) {
    amount = data.length;
    this.neurons = new Neuron[amount];
    for (int i = 0; i < amount; i++) {
      this.neurons[i] = new Neuron(data[i].weights, data[i].bias);
    }
  }

  // save the data from the neurons in the layer
  public NeuronData[] save() {
    NeuronData[] data = new NeuronData[amount];
    for (int i = 0; i < amount; i++) {
      data[i] = neurons[i].save();
    }
    return data;
  }

  // get the amount of weights and biases in the layer
  public int getCount() {
    int output = 0;
    for (Neuron neuron : this.neurons) {
      output += neuron.getCount();
    }
    return output;
  }

  // generate a random number between -1 and 1
  private static double genNum() {
    return Math.random() * 2 - 1;
  }
}
