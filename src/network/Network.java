package network;

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

    public Network(int... NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_SIZE - 1];

        /*
        * TODO output of a neuron ??? thats why it is 2D array
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
}
