package network;

import trainset.TrainSet;

public class Network {

    private double[][] output;
    public double[][][] weights;
    public double[][] bias;

    private double[][] error_signal;
    private double[][] output_derivative; // TODO ???

    public final int[] NETWORK_LAYER_SIZES;
    public final int   INPUT_SIZE;
    public final int   NO_OF_LAYERS;
    public final int   OUTPUT_SIZE;
    public final int   OUTPUT_LAYER;

    /*
    * Output is a 2D array as it is the output of each neuron in each layer. not the whole network.
    * Weights is a 3d array, x = layer, y = neurons in layer, z = neurons in previous layer
    *   x starts at 1 not 0 as there is no -1 layer
    * Bias is a 2d array, x = layer, y = neurons in layer
    * TODO where do these bounds come from ??
    * */
    public Network(int[] NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;

        this.NO_OF_LAYERS = NETWORK_LAYER_SIZES.length;
        this.OUTPUT_LAYER = NO_OF_LAYERS -1;

        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[OUTPUT_LAYER];

        this.output = new double[NO_OF_LAYERS][];
        this.weights = new double[NO_OF_LAYERS][][];
        this.bias = new double[NO_OF_LAYERS][];

        this.error_signal = new double[NO_OF_LAYERS][];
        this.output_derivative = new double[NO_OF_LAYERS][];

        for (int i = 0; i< NO_OF_LAYERS; i++) {
            this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.error_signal[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.output_derivative[i] = new double[NETWORK_LAYER_SIZES[i]];
        }

    }

    public void train(TrainSet set, int loops, int batch_size) {
        if (set.INPUT_SIZE != INPUT_SIZE || set.OUTPUT_SIZE != OUTPUT_SIZE) {
            return;
        }

        for (int i=0 ; i<loops ; i++) {
            TrainSet batch = set.extractBatch(batch_size);
            for (int b=0 ; b<batch_size ; b++) {
                this.train(batch.getInput(b), batch.getOutput(b), 0.3);
            }
            System.out.println(MSE(batch));
        }
    }

    public void train(double[] input, double[] target, double eta) {
        if (input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) {
            return;
        }
        calculate(input);
        backpropError(target);
        updateWeights(eta);
    }

    /*
    * MSE Mean of Squared Errors
    * */
    public double MSE(TrainSet set) {
      double v=0;
      for (int i=0 ; i<set.size(); i++) {
          v += MSE(set.getInput(i), set.getOutput(i));
      }
      return v/set.size();
    }

    public double MSE(double[] input, double[] target) {
        if (input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) {
            return 0;
        }
        calculate(input);
        double v = 0;
        for (int i=0 ; i<target.length ; i++) {
            double actualValue = target[i];
            double expectedNeuron = output[NO_OF_LAYERS-1][i];
            v += (actualValue - expectedNeuron) * (actualValue - expectedNeuron);
        }
        return v / (2d * target.length);
    }

    public double[] calculate(double... input) {
        if (input.length != this.INPUT_SIZE) {
            return null;
        }

        this.output[0] = input;

        for(int layer = 1; layer < NO_OF_LAYERS; layer ++) {
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron ++) {

                double neuronBias = bias[layer][neuron];
                double neuronOutput = 0;

                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer-1]; prevNeuron ++) {
                    double prevNeuronOutput = output[layer-1][prevNeuron];
                    double prevNeuronWeight = weights[layer][neuron][prevNeuron];
                    neuronOutput += prevNeuronOutput * prevNeuronWeight;
                }
                neuronOutput += neuronBias;

                output[layer][neuron] = sigmoid(neuronOutput);
                output_derivative[layer][neuron] = sigmoidDerivative(output[layer][neuron]);
            }
        }
        return output[NO_OF_LAYERS-1];
    }

    private double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

    private double sigmoidDerivative(double x) {
        return x * (1-x);
    }

    public void backpropError(double[] target) {
        for (int neuron=0 ; neuron<NETWORK_LAYER_SIZES[OUTPUT_LAYER] ; neuron++) {

            double predictedValue = output[OUTPUT_LAYER][neuron];
            double predictedValueDerivative = output_derivative[OUTPUT_LAYER][neuron];
            double actualValue = target[neuron];

            error_signal[NO_OF_LAYERS-1][neuron] = (predictedValue - actualValue)
                    * predictedValueDerivative;
        }
        for (int layer = NO_OF_LAYERS-2; layer>0 ; layer--) {
            for (int neuron=0 ; neuron<NETWORK_LAYER_SIZES[layer] ; neuron++) {

                double sum = 0;
                for (int nextNeuron=0 ; nextNeuron<NETWORK_LAYER_SIZES[layer+1] ; nextNeuron++) {
                    double nextNeuronErrorSignal = error_signal[layer + 1][nextNeuron];
                    double nextNeuronWeight = weights[layer + 1][nextNeuron][neuron];
                    sum += nextNeuronWeight * nextNeuronErrorSignal;
                }
                this.error_signal[layer][neuron] = sum*output_derivative[layer][neuron];
            }
        }
    }

    public void updateWeights(double eta) {
        for (int layer = 1; layer< NO_OF_LAYERS; layer++) {
            for (int neuron=0 ; neuron<NETWORK_LAYER_SIZES[layer] ; neuron++) {
                double delta = -eta*error_signal[layer][neuron];
                bias[layer][neuron] += delta;

                for (int prevNeuron=0 ; prevNeuron<NETWORK_LAYER_SIZES[layer-1] ; prevNeuron++) {
                    double prevNeuronOutput = output[layer-1][prevNeuron];
                    weights[layer][neuron][prevNeuron] += delta*prevNeuronOutput;
                }
            }
        }
    }
}
