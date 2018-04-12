package network;

import trainset.TrainSet;

public class Network {

    private double[][] output;
    private double[][][] weights;
    private double[][] bias;

    private double[][] error_signal;
    private double[][] output_derivative; // TODO ???

    public final int[] NETWORK_LAYER_SIZES;
    public final int   INPUT_SIZE;
    public final int   NETWORK_SIZE;
    public final int   OUTPUT_SIZE;
    public final int   OUTPUT_LAYER;

    public Network(int... NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_SIZE - 1];
        this.OUTPUT_LAYER = NETWORK_SIZE-1;

        /*
        * TODO output of a neuron ??? that is why is it a 2D array?
        * The output of each layer? that's why it is NETWORK_SIZE long
        * TODO why is weights a 3D array ???
        * */
        this.output = new double[NETWORK_SIZE][];
        this.weights = new double[NETWORK_SIZE][][];
        this.bias = new double[NETWORK_SIZE][];

        this.error_signal = new double[NETWORK_SIZE][];
        this.output_derivative = new double[NETWORK_SIZE][];

        for (int i=0 ; i<NETWORK_SIZE ; i++) {
            /*
            * 784, 70, 35, 10
            * */
            this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.error_signal[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.output_derivative[i] = new double[NETWORK_LAYER_SIZES[i]];

            /* TODO where do these bounds come from */
            this.bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], -0.5, 0.7);

            if (i > 0) {
                weights[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], NETWORK_LAYER_SIZES[i-1], -1, 1);
            }
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
            v += (target[i] - output[NETWORK_SIZE-1][i]) * (target[i] - output[NETWORK_SIZE-1][i]);
        }
        return v / (2d * target.length);
    }

    /*
    * Takes its best guess as to what the digit is
    * - FOR EACH LAYER EXCLUDING INPUT
    * -- FOR EACH NEURON
    * -- GET THAT NEURONS BIAS
    * --- FOR EACH NEURON IN THE PREVIOUS LAYER
    * --- ADD THE OUTPUT MULTIPLIED BY THE WEIGHTING WHICH IS IN THE CONNECTION BETWEEN EACH NEURON
    * --- TODO APPLY THE SIGMOID FUNCTION ??? AND PUT IT INTO THE OUTPUT OF THAT LAYER/NEURON
    * --- TODO OUTPUT DERIVITIVE???
    * - RETURN THE FINAL LAYER WHICH IS THE OUTPUT
    * */
    public double[] calculate(double... input) {
        if( input.length != INPUT_SIZE) {
            return null;
        }
        this.output[0] = input;

        for (int layer=1 ; layer<NETWORK_SIZE ; layer++) {
            for (int neuron=0 ; neuron<NETWORK_LAYER_SIZES[layer] ; neuron++) {

                double sum = bias[layer][neuron];
                for (int prevNeuron=0 ; prevNeuron<NETWORK_LAYER_SIZES[layer-1] ; prevNeuron++) {
                    sum += output[layer-1][prevNeuron]*weights[layer][neuron][prevNeuron];
                }
                output[layer][neuron] = sigmoid(sum);
                output_derivative[layer][neuron] = output[layer][neuron] * (1 - output[layer][neuron]);
            }
        }
        return output[NETWORK_SIZE-1];
    }

    private double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

    /*
    * - FOR EACH NEURON IN THE OUTPUT LAYER
    * -- THE ERROR SIGNAL OF THAT NEURON IS the best guess - the actual answer * the output derivitive TODO ???
    * - FOR EACH LAYER WORKING BACKWARDS
    * -- FOR EACH NEURON
    * --- FOR EACH NEURON IN THE PREVIOUS LAYER
    * --- SUM THEIR WEIGHT IN CONNECTION TO THIS LAYERS NEURON MULTIPLIED BY THE ERROR SIGNAL OF THE PREV LAYER NEURON
    * -- THIS LAYERS NEURONS ERROR SIGNAL IS THAT SUM MULTIPLIED BY ITS OUTPUT DERIVITIVE TODO ???
    * */
    public void backpropError(double[] target) {
        for (int neuron=0 ; neuron<NETWORK_LAYER_SIZES[OUTPUT_LAYER] ; neuron++) {
            error_signal[NETWORK_SIZE-1][neuron] = (output[OUTPUT_LAYER][neuron] - target[neuron])
                    * output_derivative[OUTPUT_LAYER][neuron];
        }
        for (int layer=NETWORK_SIZE-2 ; layer>0 ; layer--) {
            for (int neuron=0 ; neuron<NETWORK_LAYER_SIZES[layer] ; neuron++) {
                double sum = 0;
                for (int nextNeuron=0 ; nextNeuron<NETWORK_LAYER_SIZES[layer+1] ; nextNeuron++) {
                    sum += weights[layer + 1][nextNeuron][neuron] * error_signal[layer + 1][nextNeuron];
                }
                this.error_signal[layer][neuron] = sum*output_derivative[layer][neuron];
            }
        }
    }

    /*
    * - FOR EACH LAYER
    * -- FOR EACH NEURON
    * -- TODO WHAT IS eta???
    * -- UPDATE THE BIAS FOR EACH NEURON BASED ON THE ERROR SIGNAL
    * --- FOR EACH WEIGHT BETWEEN THE CURRENT NEURON AND EACH NEURON IN THE PREVIOUS LAYER
    * --- UPDATE THE WEIGHT BASED ON THE DELTA TODO WHY THE OUTPUT???
    *
    * */
    public void updateWeights(double eta) {
        for (int layer=1 ; layer<NETWORK_SIZE ; layer++) {
            for (int neuron=0 ; neuron<NETWORK_LAYER_SIZES[layer] ; neuron++) {

                double delta = -eta*error_signal[layer][neuron];
                bias[layer][neuron] += delta;

                for (int prevNeuron=0 ; prevNeuron<NETWORK_LAYER_SIZES[layer-1] ; prevNeuron++) {
                    weights[layer][neuron][prevNeuron] += delta*output[layer-1][prevNeuron];
                }
            }
        }
    }
}
