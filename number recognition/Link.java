import java.lang.reflect.Array;
import java.util.ArrayList;

public class Link {
    float weight;
    //int lay;
    Node n1;
    Node n2;
    ArrayList<Float> changes = new ArrayList<>();
    public Link (Node n1, Node n2) {
        //this.lay=lay;
        this.n1= n1;
        this.n2= n2;
        weight = (float) (Math.random() + .1);
        //mult = (float) .1;
    }
    public void setWeight (float m) {
        weight = m;
    }

    public void addChange(Float f) {
        changes.add(f);
    }
    public Node getN1() {
        return n1;
    }
    public Node getN2() {
        return n2;
    }
}
