public class Neuron {

  // stores the results for training
  public double[] lastInput;
  public double lastSum;
  public double lastOutput;

  // weights and bias
  public int weightsCount;
  public double[] weights;
  public Double bias;

  public Neuron(double[] weights, double bias) {
    this.weights = weights;
    this.bias = bias;
    this.weightsCount = weights.length;
  }

  // computes the output of the neuron
  public double compute(
    double[] input,
    ActivationFunctions.FunctionBundle func
  ) {
    lastInput = input;
    lastSum = bias;
    for (int i = 0; i < weightsCount; i++) {
      lastSum += input[i] * weights[i];
    }
    lastOutput = func.activate(lastSum);
    return lastOutput;
  }

  // updates the weights and bias of the neuron
  // with the delta and and the learning rate
  public void updateWeights(double learningRate, double delta) {
    for (int i = 0; i < weightsCount; i++) {
      weights[i] -= learningRate * delta * lastInput[i];
    }
    bias -= learningRate * delta;
  }

  // saves the neuron
  public NeuronData save() {
    return new NeuronData(weights, bias);
  }

  // gets the amount of weights and biases in the neuron
  public int getCount() {
    return weightsCount + (bias != null ? 1 : 0);
  }
}
