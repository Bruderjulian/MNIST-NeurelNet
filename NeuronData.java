// stores the weights and bias of a neuron
// used for saving the network
public class NeuronData {

  public double[] weights;
  public double bias;

  public NeuronData(double[] weights, double bias) {
    this.weights = weights;
    this.bias = bias;
  }
}
