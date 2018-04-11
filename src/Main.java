import network.Network;
import trainset.TrainSet;

import java.io.File;

public class Main {

    private static int INPUT_NEURONS  = 28*28;
    private static int OUTPUT_NEURONS = 10;

    public static void main(String[] args) {
        /*
        * 784 = 28*28, the number of pixels in the pictures as each is a 28 pixels square
        * 70 ???
        * 35 ???
        * 10, the output size, the data set is an image of a 0-9 so 10 possible outcomes
        * */
        Network network = new Network(INPUT_NEURONS, 70, 35, OUTPUT_NEURONS);

        TrainSet set = createTrainSet(0, 4999);
    }

    public static TrainSet createTrainSet(int start, int end) {
        TrainSet set = new TrainSet(INPUT_NEURONS, OUTPUT_NEURONS);

        try {
            String path = new File().getAbsolutePath();

        }catch () {

        }

        return set;
    }

}
