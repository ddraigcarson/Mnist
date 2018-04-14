package file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileIO {

    public static void writeToFile(String fileName, String str) {

        try {
            byte[] strToByte = str.getBytes();

            String path = new File("").getAbsolutePath() + "/output/" + fileName;
            Path weights = Paths.get(path);
            Files.write(weights, strToByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static double[][][] readWeightsFromFile(String fileName) {
        double[][][] arr = null;

        try {
            int NO_OF_LAYERS = 4;
            int[] LAYER_SIZES = {28*28, 70, 35, 10};

            Path path = Paths.get(new File("").getAbsolutePath() + "/output/" + fileName);
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

            System.out.println("finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

}
