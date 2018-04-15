package persistance;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileIO implements Storage {

    @Override
    public void writeWeightsToFile(double[][][] weights) {
        try {
            byte[] strToByte = weightsToString(weights).getBytes();
            Path path = Paths.get(new File("").getAbsolutePath() + "/output/weights.csv");
            Files.write(path, strToByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String weightsToString(double[][][] arr) {

        String output = "";

        for (int i=0 ; i<arr.length ; i++) {
            if (arr[i] == null) {
                continue;
            }
            for (int j=0 ; j<arr[i].length ; j++) {
                if (arr[i][j] == null) {
                    continue;
                }
                for (int k=0 ; k<arr[i][j].length ; k++) {
                    output += i + "," + j + "," + k + "," + arr[i][j][k] + ",\n";
                }
            }
        }
        return output;
    }

    @Override
    public double[][][] readWeightsFromFile(int[] LAYER_SIZES) {
        double[][][] arr = null;

        try {
            int NO_OF_LAYERS = LAYER_SIZES.length;

            Path path = Paths.get(new File("").getAbsolutePath() + "/output/weights.csv");
            List<String> output = Files.readAllLines(path);

            arr = new double[NO_OF_LAYERS][][];
            for (int layer=1 ; layer<NO_OF_LAYERS ; layer++) {
                arr[layer] = new double[LAYER_SIZES[layer]][LAYER_SIZES[layer-1]];
            }

            for (String str : output) {
                String[] split = str.split(",");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                int z = Integer.parseInt(split[2]);
                Double value = Double.parseDouble(split[3]);
                arr[x][y][z] = value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    @Override
    public boolean checkForExistingWeights() {
        Path path = Paths.get(new File("").getAbsolutePath() + "/output/weights.csv");
        return Files.exists(path);
    }

    @Override
    public void writeBiasesToFile(double[][] biases) {
        try {
            byte[] strToByte = biasesToString(biases).getBytes();
            Path path = Paths.get(new File("").getAbsolutePath() + "/output/biases.csv");
            Files.write(path, strToByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String biasesToString(double[][] arr) {
        String output = "";
        for (int i=0 ; i<arr.length ;i++) {
            if (arr[i] == null) {
                continue;
            }
            for (int j=0 ; j<arr[i].length ;j++) {
                output += i + "," + j + "," + arr[i][j] +",\n";
            }
        }
        return output;
    }

    @Override
    public double[][] readBiasesFromFile(int[] LAYER_SIZES) {
        double[][] arr = null;

        try {
            int NO_OF_LAYERS = LAYER_SIZES.length;

            Path path = Paths.get(new File("").getAbsolutePath() + "/output/biases.csv");
            List<String> output = Files.readAllLines(path);

            arr = new double[NO_OF_LAYERS][];

            for (int layer=0 ; layer<NO_OF_LAYERS ; layer++) {
                arr[layer] = new double[LAYER_SIZES[layer]];
            }

            for (String str : output) {
                String[] split = str.split(",");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                Double value = Double.parseDouble(split[2]);
                arr[x][y] = value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    @Override
    public boolean checkForExistingBiases() {
        Path path = Paths.get(new File("").getAbsolutePath() + "/output/biases.csv");
        return Files.exists(path);
    }
}
