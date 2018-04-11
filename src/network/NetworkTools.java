package network;

public class NetworkTools {

    /*
    * Creates a random array filled of size, size;
    * Fills it with random numbers within the lower and upper bound
    * */
    public static double[] createRandomArray(int size, double lowerBound, double upperBound) {
        if (size < 1) {
            return null;
        }
        double[] ar = new double[size];
        for(int i=0 ; i<size ; i++) {
            ar[i] = randomValue(lowerBound, upperBound);
        }
        return ar;
    }

    public static double[][] createRandomArray(int sizeX, int sizeY, double lowerBound, double upperBound) {
        if (sizeX < 1 || sizeY < 1) {
            return null;
        }
        double[][] ar = new double[sizeX][sizeY];
        for (int i=0 ; i<sizeX ; i++) {
            ar[i] = createRandomArray(sizeY, lowerBound, upperBound);
        }
        return ar;
    }

    public static double randomValue(double lowerBound, double upperBound){
        return Math.random()*(upperBound-lowerBound) + lowerBound;
    }
}
