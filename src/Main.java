import network.NetworkBuilder;
import persistance.FileIO;
import network.Network;
import network.NetworkTools;
import persistance.Storage;
import trainset.TrainSet;

import static network.NetworkConstants.*;
import static trainset.MnistTrainSetTools.createTrainSet;

public class Main {

    public static void main(String[] args) {
        Storage storage = new FileIO();
        boolean existingNetwork = storage.checkForExistingWeights() && storage.checkForExistingBiases();

        NetworkBuilder builder = new NetworkBuilder();
        Network network = null;

        if (existingNetwork) {
            System.out.println("Loading existing weights and biases");

            network = builder.newNetwork()
                    .addInputLayer(INPUT_NEURONS)
                    .addHiddenLayer(HIDDEN_LAYER_1_NEURONS)
                    .addHiddenLayer(HIDDEN_LAYER_2_NEURONS)
                    .addOutputLayer(OUTPUT_NEURONS)
                    .withWeights(storage.readWeightsFromFile(builder.getLayers()))
                    .withBiases(storage.readBiasesFromFile(builder.getLayers()))
                    .build();

            System.out.println("Loaded existing weights and biases");
        } else {
            network = builder.newNetwork()
                    .addInputLayer(INPUT_NEURONS)
                    .addHiddenLayer(HIDDEN_LAYER_1_NEURONS)
                    .addHiddenLayer(HIDDEN_LAYER_2_NEURONS)
                    .addOutputLayer(OUTPUT_NEURONS)
                    .withRandomWeights()
                    .withRandomBiases()
                    .build();

            TrainSet set = createTrainSet(TRAINING_IMAGES_START, TRAINING_IMAGES_END);
            trainData(network, set, 100, 50, TRAINING_BATCH_SIZE);

            storage.writeWeightsToFile(network.weights);
            storage.writeBiasesToFile(network.bias);
        }

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
            System.out.println("Testing finished, RESULT: " + correct + " / " + set.size()+ "  -> " + (double)correct*100 / (double)set.size() +" %");
        }
    }

}
