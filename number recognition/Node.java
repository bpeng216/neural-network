import java.lang.reflect.Array;
import java.util.ArrayList;

public class Node {
    float value;
    float bias;
    float error;
    float E;
    ArrayList<Float> errors;
    //public int lay;
    public int num;

    public Node(int number) {
        //lay = layer;
        num = number;
        bias = 1;
        error = 0;
        errors = new ArrayList<>();
    }

    public void setError(float error) {
        this.error = error;
    }
    public void setBias (float b) {
        bias = b;
    }
    public void setValue (float v) {
        value = v;
    }
    public void combError() {
        float sum = 0;
        int div = errors.size();
        while (errors.size() > 0) {
            sum += errors.remove(0);
        }
        error = sum;
    }
}
