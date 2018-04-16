package trainset;

import network.NetworkTools;

import java.util.ArrayList;

public class TrainSet {

    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;

    //double[][] <- index1: 0 = input, 1 = output || index2: index of element
    private ArrayList<double[][]> data = new ArrayList<>();

    public TrainSet(int INPUT_SIZE, int OUTPUT_SIZE) {
        this.INPUT_SIZE = INPUT_SIZE;
        this.OUTPUT_SIZE = OUTPUT_SIZE;
    }

    public void addData(double[] in, double[] expected) {
        if (in.length != INPUT_SIZE || expected.length != OUTPUT_SIZE) {
            return;
        }
        data.add(new double[][]{in, expected});
    }

    public TrainSet extractBatch(int size) {
        if (size > 0 && size <= this.size()) {
            TrainSet set = new TrainSet(INPUT_SIZE, OUTPUT_SIZE);
            Integer[] ids = NetworkTools.randomValues(0, this.size()-1, size);
            for (Integer id : ids) {
                set.addData(this.getInput(id), this.getOutput(id));
            }
            return set;
        } else {
            return this;
        }
    }

    public int size() {
        return data.size();
    }

    public double[] getInput(int index) {
        if(index >=0 && index < size()) {
            return data.get(index)[0];
        }
        return null;
    }

    public double[] getOutput(int index) {
        if(index >=0 && index < size()) {
            return data.get(index)[1];
        }
        return null;
    }

}
