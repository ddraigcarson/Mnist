package persistance;

public interface Storage {

    void writeWeightsToFile(double[][][] weights);

    String weightsToString(double[][][] arr);

    double[][][] readWeightsFromFile(int[] LAYER_SIZES);

    boolean checkForExistingWeights();

    void writeBiasesToFile(double[][] biases);

    String biasesToString(double[][] arr);

    double[][] readBiasesFromFile(int[] LAYER_SIZES);

    boolean checkForExistingBiases();
}
