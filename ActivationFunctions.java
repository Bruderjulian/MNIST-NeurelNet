
/**
 * Provides activation functions commonly used in neural networks.
 * Contains implementations for Sigmoid and ReLU (Rectified Linear Unit)
 * activation functions.
 */
public class ActivationFunctions {
  public static FunctionBundle sigmoid = new Sigmoid();
  public static FunctionBundle relu = new ReLU();

  /**
   * Interface defining activation function operations.
   * Provides methods to compute the activation value and its derivative.
   */
  public interface FunctionBundle {
    public double activate(double x);

    public double derivative(double x);
  }

  /**
   * ReLU activation function implementation.
   * Returns 0 for negative inputs and the input value for non-negative inputs.
   */
  private static final class ReLU implements FunctionBundle {
    public double activate(double x) {
      return Math.max(0, x);
    }

    public double derivative(double x) {
      return x > 0 ? 1 : 0;
    }
  }

  /**
   * Sigmoid activation function implementation.
   * Transforms input to a value smoothly between 0 and 1.
   */
  private static final class Sigmoid implements FunctionBundle {
    public double activate(double x) {
      return 1 / (1 + Math.exp(-x));
    }

    public double derivative(double x) {
      double sx = 1 / (1 + Math.exp(-x));
      return sx * (1 - sx);
    }
  }
}
