package tools;

public class ToStringTools {

    public static String double3DArrayToString(double[][][] arr) {

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

}
