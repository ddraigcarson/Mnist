package network;

import java.util.ArrayList;
import java.util.List;

import static network.NetworkConstants.*;

public class NetworkBuilder {

    private List<Integer> layers;
    private double[][][] weights;
    private double[][] bias;

    private int NO_OF_LAYERS;

    public Network build() {
        Network network = new Network(getLayers());
        network.weights = weights;
        network.bias = bias;
        return network;
    }

    public int[] getLayers(){
        int[] arr = new int[layers.size()];
        for (int i=0 ; i<layers.size() ; i++) {
            arr[i] = layers.get(i);
        }
        return arr;
    }

    public NetworkBuilder newNetwork() {
        layers = new ArrayList<Integer>();
        return this;
    }

    public NetworkBuilder addInputLayer(int neurons) {
        layers.add(neurons);
        return this;
    }

    public NetworkBuilder addHiddenLayer(int neurons) {
        layers.add(neurons);
        return this;
    }

    public NetworkBuilder addOutputLayer(int neurons) {
        layers.add(neurons);
        NO_OF_LAYERS = layers.size();
        return this;
    }

    public NetworkBuilder withRandomWeights() {
        for (int i = 1; i< NO_OF_LAYERS; i++) {
            weights[i] = NetworkTools.createRandomArray(
                    layers.get(i),
                    layers.get(i-1),
                    WEIGHTS_LOWER_BOUND,
                    WEIGHTS_UPPER_BOUND
            );
        }
        return this;
    }

    public NetworkBuilder withRandomBiases() {
        for (int i = 0; i< NO_OF_LAYERS; i++) {
            bias[i] = NetworkTools.createRandomArray(
                    layers.get(i),
                    BIAS_LOWER_BOUND,
                    BIAS_UPPER_BOUND
            );
        }
        return this;
    }

    public NetworkBuilder withWeights(double[][][] weights) {
        this.weights = weights;
        return this;
    }

    public NetworkBuilder withBiases(double[][] bias) {
        this.bias = bias;
        return this;
    }

}
