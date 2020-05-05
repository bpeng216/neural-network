import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

import static javax.imageio.ImageIO.*;

public class Main {
    public float lr = (float) .1;
    private static Node[] n1 = new Node[784];
    private static Node[] n2 = new Node[16];
    private static Node[] n3 = new Node[16];
    private static Node[] n4 = new Node[10];

    private static ArrayList<Link> l1 = new ArrayList<Link>();
    private static ArrayList<Link> l2 = new ArrayList<Link>();
    private static ArrayList<Link> l3 = new ArrayList<Link>();
    public static void main(String[] args) {

        //set up neural network
        for (int i = 0; i < n1.length; i++) {
            n1[i] = new Node(i);
        }
        for (int i = 0; i < n2.length; i++) {
            n2[i] = new Node(i);
        }
        for (int i = 0; i < n3.length; i++) {
            n3[i] = new Node(i);
        }
        for (int i = 0; i < n4.length; i++) {
            n4[i] = new Node(i);
        }

        for (int i = 0; i < n1.length; i++) {
            for (int v = 0; v < n2.length; v++) {
                l1.add(new Link(n1[i], n2[v]));
            }
        }
        for (int i = 0; i < n2.length; i++) {
            for (int v = 0; v < n3.length; v++) {
                l2.add(new Link(n2[i], n3[v]));
            }
        }
        for (int i = 0; i < n3.length; i++) {
            for (int v = 0; v < n4.length; v++) {
                l3.add(new Link(n3[i], n4[v]));
            }
        }
        String link = "0_01.png";
        setInput(link);
        int value = 0;
        int runs = 50;
        float err = 0;
        float old = 0;
        while (runs > 0) {
            //first gets results
            calculate();
            //sets errors
            for (int i = 0; i < n4.length; i++) {
                if (i == value) {
                    n4[i].setError((float) Math.pow(1 - n4[i].value, 2));
                    n4[i].E = 1 - n4[i].value;
                }
                else {
                    n4[i].setError((float) Math.pow(0 - n4[i].value, 2) * -1);
                    n4[i].E = 0 - n4[i].value;
                }
            }
            //prints errors
            old = err;
            err = 0;
            for (int i = 0; i < n4.length; i++) {
                err += Math.abs(n4[i].error);
            }
            //System.out.println(err);


            backpropogate(l3, n3);
            backpropogate(l2, n2);
            backpropogate(l1, n1);
            runs--;
        }
        for (int i = 0; i < n4.length; i++) {
            //System.out.println(i + ": " + n4[i].value);
        }

        /*float[] errors = new float[n3.length];
        Arrays.fill(errors, 0);
        for (int link = 0; link < l3.size(); link++) {
            int num = l3.get(link).getN1().num;
            errors[num] += l3.get(link).mult * l3.get(link).getN2().error;
        }
        for (int i = 0; i < n3.length; i++) {
            n3[i].setError(errors[i]);
            System.out.println(n3[i].error);
        }
        System.out.println();
        errors = new float[n2.length];
        Arrays.fill(errors, 0);
        for (int link = 0; link < l2.size(); link++) {
            int num = l2.get(link).getN1().num;
            errors[num] += l2.get(link).mult * l2.get(link).getN2().error;
        }
        for (int i = 0; i < n2.length; i++) {
            n2[i].setError(errors[i]);
            System.out.println(n2[i].error);
        }
        */

        for (int i = 0; i < n1.length; i++) {
            int r = 0;
            if (n1[i].value < .5) System.out.print(" ");
            else System.out.print("X");
            if (r > 27) {
                r = 0;
                System.out.println();
            }
        }
    }

    public static void setInput(String link) {
        BufferedImage i;
        try {
            i = read(new File("Test_Images_ARGB\\" + link));
            int n = 0;
            for (int x = 0; x < 28; x++) {
                for (int y = 0; y < 28; y++) {
                    n1[n].setValue((float) (new Color(i.getRGB(x, y)).getRed())/255);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void backpropogate(ArrayList<Link> l, Node[] n) {
        for (int i = 0; i < l.size(); i++) {
            float aprime = l.get(i).getN2().value * (1-l.get(i).getN2().value);
            float err = 2*l.get(i).getN2().error;
            l.get(i).setWeight(  l.get(i).weight + err * aprime * l.get(i).getN1().value    );
            l.get(i).getN2().setBias( l.get(i).getN2().bias + err * aprime    );
            l.get(i).getN1().errors.add(  l.get(i).weight * err * aprime    );
        }
        for (int i = 0; i < n.length; i++) {
            n[i].combError();
        }
    }

    public static void calculate() {
        for (int i = 0; i < n2.length; i++) {
            float sum = 0;
            for (int v = 0; v < l1.size(); v++) {
                if (l1.get(v).getN2() == n2[i]) {
                    sum += l1.get(v).weight * l1.get(v).getN1().value;
                }
            }
            n2[i].setValue(sigmoid(sum + n2[i].bias));
        }
        for (int i = 0; i < n3.length; i++) {
            float sum = 0;
            for (int v = 0; v < l2.size(); v++) {
                if (l2.get(v).getN2() == n3[i]) {
                    sum += l2.get(v).weight * l2.get(v).getN1().value;
                }
            }
            n3[i].setValue(sigmoid(sum + n3[i].bias));
        }
        for (int i = 0; i < n4.length; i++) {
            float sum = 0;
            for (int v = 0; v < l3.size(); v++) {
                if (l3.get(v).getN2() == n4[i]) {
                    sum += l3.get(v).weight * l3.get(v).getN1().value;
                }
            }
            n4[i].setValue(sigmoid(sum + n4[i].bias));
        }
        /*for (int i = 0; i < n4.length; i++) {
            System.out.println(i + ": " + n4[i].value);
        }*/
    }
    public static float sigmoid(float f) {
        return (float) (1/(1+Math.pow(Math.E,-(f))));
    }
}
