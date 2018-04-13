import mnist.MnistImageFile;
import mnist.MnistLabelFile;
import network.Network;
import network.NetworkConstants;
import network.NetworkTools;
import trainset.TrainSet;

import java.io.File;

import static network.NetworkConstants.*;
import static trainset.MnistTrainSetTools.createTrainSet;

public class Main {



    public static void main(String[] args) {
        Network network = new Network(
                INPUT_NEURONS, HIDDEN_LAYER_1_NEURONS, HIDDEN_LAYER_2_NEURONS, OUTPUT_NEURONS);

        TrainSet set = createTrainSet(TRAINING_IMAGES_START, TRAINING_IMAGES_END);
        trainData(network, set, 100, 50, TRAINING_BATCH_SIZE);

        TrainSet testSet = createTrainSet(TEST_IMAGES_START, TEST_IMAGES_END);
        testTrainSet(network, testSet, 10);
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
