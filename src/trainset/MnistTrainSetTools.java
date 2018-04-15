package trainset;

import mnist.MnistImageFile;
import mnist.MnistLabelFile;

import java.io.File;

import static network.NetworkConstants.*;
import static network.NetworkConstants.INPUT_NEURONS;
import static network.NetworkConstants.OUTPUT_NEURONS;

public class MnistTrainSetTools {
    /**
     * Creates new MNIST database image persistance ready for reading.
     *
     * @param start
     *            Image idx index start
     * @param end
     *            Image idx index end
     * @return TrainSet
     *
     * Loop over all the images
     * FOR EVERY IMAGE IN TRAINING SET
     * - CREATE THE INPUT NEURON SET
     * - CREATE THE OUTPUT NEURON SET
     * - TODO WHAT DOES THE LABEL FILE LOOK LIKE
     * - INITIALISE THE OUTPUT SET - each value is set to 0 (double default) then the output neuron with i=the images number is set to 1
     * -- FOR EACH INPUT NEURON SET IT TO A 0-1 VALUE CORRESPONDING TO THE COLOUR OF THE PIXEL
     */
    public static TrainSet createTrainSet(int start, int end) {
        TrainSet set = new TrainSet(INPUT_NEURONS, OUTPUT_NEURONS);

        try {
            String path = new File("").getAbsolutePath();

            MnistImageFile m = new MnistImageFile(path + IMAGE_FILE_PATH, IMAGE_FILE_ACCESS_MODE);
            MnistLabelFile l = new MnistLabelFile(path + IMAGE_LABEL_PATH, IMAGE_FILE_ACCESS_MODE);

            for (int image=start ; image<=end ; image++) {
                printEveryXIterations("prepared: " + image, 100, image);

                double[] input = new double[INPUT_NEURONS];
                double[] output = new double[OUTPUT_NEURONS];

                output[l.readLabel()] = 1d;
                for (int neuron=0 ; neuron<INPUT_NEURONS ; neuron++) {
                    // Create the pixel colour as a 0-1 value, 0 = white, 1 = black
                    input[neuron] = (double)m.read() / PIXEL_COLOUR_VALUE_LIMIT;
                }

                set.addData(input, output);
                m.next();
                l.next();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return set;
    }

    private static void printEveryXIterations(String message, int iterations, int iteration) {
        if (iteration % iterations == 0) {
            System.out.println(message);
        }
    }

}
