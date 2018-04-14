package network;

public class NetworkConstants {

    public static int INPUT_NEURONS  = 28*28; // 784 - Pictures are 28 by 28 pixels
    public static int OUTPUT_NEURONS = 10; // There are 10 possible outputs , 0-9

    public static int HIDDEN_LAYER_1_NEURONS = 70; // TODO Why this number?
    public static int HIDDEN_LAYER_2_NEURONS = 35; // TODO Why this number?

    public static int TRAINING_IMAGES_START = 0;
    public static int TRAINING_IMAGES_END = 4999;

    public static int TEST_IMAGES_START = 5000;
    public static int TEST_IMAGES_END = 9999;

    public static int TRAINING_BATCH_SIZE = 100;

    /*
     * IDX is an index file extension commonly used in Windows to speed up the search
     * in a db
     * */
    public static String IMAGE_FILE_PATH = "/res/trainImage.idx3-ubyte";
    public static String IMAGE_LABEL_PATH = "/res/trainLabel.idx1-ubyte";

    public static String IMAGE_FILE_ACCESS_MODE = "rw";

    /*
    * 0 == white
    * 255 == black
    * */
    public static double PIXEL_COLOUR_VALUE_LIMIT = 256;
}
