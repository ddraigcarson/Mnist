import mnist.MnistImageFile;
import mnist.MnistLabelFile;
import network.Network;
import network.NetworkTools;
import trainset.TrainSet;

import java.io.File;

public class Main {

    private static int INPUT_NEURONS  = 28*28;
    private static int OUTPUT_NEURONS = 10;

    private static int NO_OF_IMAGES = 4999;

    public static void main(String[] args) {
        /*
        * 784 = 28*28, the number of pixels in the pictures as each is a 28 pixels square
        * 70 ???
        * 35 ???
        * 10, the output size, the data set is an image of a 0-9 so 10 possible outcomes
        * */
        Network network = new Network(INPUT_NEURONS, 70, 35, OUTPUT_NEURONS);

        TrainSet set = createTrainSet(0, NO_OF_IMAGES);
        trainData(network, set, 100, 50, 100);

        TrainSet testSet = createTrainSet(5000, 9999);
        testTrainSet(network, testSet, 10);
    }

    public static TrainSet createTrainSet(int start, int end) {
        TrainSet set = new TrainSet(INPUT_NEURONS, OUTPUT_NEURONS);

        try {
            String path = new File("").getAbsolutePath();
            /*
            * IDX is an index file extension commonly used in Windows to speed up the search
            * in a db
            * */
            MnistImageFile m = new MnistImageFile(path + "/res/trainImage.idx3-ubyte", "rw");
            MnistLabelFile l = new MnistLabelFile(path + "/res/trainLabel.idx1-ubyte", "rw");

            /*
            * Loop over all the images, there are 5000 training images
            *
            * FOR EVERY IMAGE IN TRAINING SET
            * - CREATE THE INPUT NEURON SET
            * - CREATE THE OUTPUT NEURON SET
            * - TODO WHAT DOES THE LABEL FILE LOOK LIKE
            * */
            for (int i=start ; i<=end ; i++) {
                if (i % 100 == 0) {
                    System.out.println("prepared: " + i);
                }

                double[] input = new double[INPUT_NEURONS];
                double[] output = new double[OUTPUT_NEURONS];

                output[l.readLabel()] = 1d;
                for (int j=0 ; j<INPUT_NEURONS ; j++) {
                    /*
                    *  TODO ??? WTF is 256
                    *  Maybe something to do with the pixels, colour???
                    * */
                    input[j] = (double)m.read() / (double) 256;
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

    public static void trainData(Network net, TrainSet set, int epochs, int loops, int batch_size) {
        /*
        * Epochs is a period of time in ones life
        * Maybe we repeat the training for increased accuracy
        * */
        for (int e=0 ; e<epochs ; e++) {
            net.train(set, loops, batch_size);
        }
    }

    public static void testTrainSet(Network net, TrainSet set, int printSteps) {
        int correct = 0;

        for (int i=0 ; i<set.size() ; i++) {
            double highest = NetworkTools.indexOfHighestValue(net.calculate((set.getInput(i))));
            double actualHighest = NetworkTools.indexOfHighestValue(set.getOutput(i));

            if(highest == actualHighest) {
                correct++;
            }
            if(i%printSteps == 0) {
                System.out.println(i + ": " + (double)correct / (double) (i + 1));
            }
            System.out.println("Testing finished, RESULT: " + correct + " / " + set.size()+ "  -> " + (double)correct / (double)set.size() +" %");
        }
    }

}
