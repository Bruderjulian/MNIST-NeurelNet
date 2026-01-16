import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

// stores the data for a neural network
// can be saved to a file and loaded from a file

// way to overcomplicated, but it works!

public class NetworkData {

  // model name
  public String model;
  // loss of the model (calculated in the training)
  public double loss;
  // structure of the network
  public int[] structure;
  // data for the neurons of each layers in the network
  public NeuronData[][] layers;

  // parse the strings into a more suitable format
  public NetworkData(
    String model,
    String loss,
    String structure,
    NeuronData[][] layers
  ) {
    this.model = model;
    this.loss = Double.parseDouble(loss);
    this.layers = layers;
    String[] temp = structure.split("-");
    this.structure = new int[temp.length];
    try {
      for (int i = 0; i < temp.length; i++) {
        this.structure[i] = Integer.parseInt(temp[i]);
      }
    } catch (NumberFormatException e) {
      System.out.println("Failed to parse structure");
      e.printStackTrace();
    }
  }

  // or just store it directly if is already in the right format
  public NetworkData(
    String model,
    double loss,
    int[] structure,
    NeuronData[][] layers
  ) {
    this.model = model;
    this.loss = loss;
    this.layers = layers;
    this.structure = structure;
  }

  // loads the data for the network from a file specified by path
  public static NetworkData load(String path) {
    ArrayList<NeuronData> neuronData = new ArrayList<NeuronData>();
    ArrayList<NeuronData[]> layerData = new ArrayList<NeuronData[]>();
    String line;
    String[] metaData_parts = { "failed", "-1", "1-1-1" };
    try {
      BufferedReader reader = new BufferedReader(new FileReader(path));
      metaData_parts = reader.readLine().split(",");
      if (metaData_parts.length < 3) {
        reader.close();
        throw new Exception("Invalid file format");
      }

      while ((line = reader.readLine()) != null) {
        if (line.equals("")) {
          if (neuronData.size() > 0) {
            layerData.add(neuronData.toArray(NeuronData[]::new));
            neuronData.clear();
          }
          continue;
        }
        String[] parts = line.split(",");
        double bias = Double.parseDouble(parts[0]);
        double[] weights = new double[parts.length - 1];
        for (int i = 0; i < weights.length; i++) {
          if (parts.length <= i) {
            weights[i] = 1;
          } else {
            weights[i] = Double.parseDouble(parts[i + 1]);
          }
        }
        neuronData.add(new NeuronData(weights, bias));
      }
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new NetworkData(
      metaData_parts[0],
      metaData_parts[1],
      metaData_parts[2],
      layerData.toArray(NeuronData[][]::new)
    );
  }

  public static void save(String path, NetworkData data) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(path));
      writer.write(
        data.model + "," + data.loss + "," + join(data.structure, "-")
      );
      for (NeuronData[] layer : data.layers) {
        writer.newLine();
        for (NeuronData neuron : layer) {
          writer.write(
            neuron.bias +
            "," +
            Arrays
              .toString(neuron.weights)
              .replaceAll("\\[", "")
              .replaceAll("\\]", "")
          );
          writer.newLine();
        }
      }
      writer.newLine();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // joins the elements of an array into a string
  // mostly unused - you should use better alternatives
  private static String join(int[] arr, String sep) {
    String str = "";
    for (int i = 0; i < arr.length; i++) {
      str += arr[i];
      if (i < arr.length - 1) {
        str += sep;
      }
    }
    return str;
  }
}
