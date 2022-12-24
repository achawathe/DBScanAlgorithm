/**
 * Completed Experiment 2
 *
 * CSI2110 Data Structures and Algorithms
 * www.uottawa.ca
 *
 * @author Akhil Chawathe
 *
 * 300237603
 *
 * 2022
 *
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Exp2 {

    /**
     *
     * Reads file and adds to the point3d list object attibute.
     *
     * @param filename
     * @return List
     * @throws Exception
     *
     */
    public static List<Point3D> read(String filename) throws Exception {

        List<Point3D> points= new ArrayList<Point3D>();
        double x,y,z;

        Scanner sc = new Scanner(new File(filename));
        // sets the delimiter pattern to be , or \n \r
        sc.useDelimiter(",|\n|\r");

        // skipping the first line x y z
        sc.next(); sc.next(); sc.next();

        // read points
        while (sc.hasNext())
        {
            x= Double.parseDouble(sc.next());
            y= Double.parseDouble(sc.next());
            z= Double.parseDouble(sc.next());
            points.add(new Point3D(x,y,z));
        }

        sc.close();  //closes the scanner

        return points;
    }

    public static void main(String[] args) throws Exception {
        double eps= Double.parseDouble(args[1]);

        // reads the csv file
        List<Point3D> points= Exp2.read(args[2]);

        long timeavg = (long) 0;
        int count = 0;
        if (args[0].equals("lin")) {
            //Calculate average time for linear function
            for (int i = 0; i < points.size(); i += Integer.parseInt(args[3])) {
                Point3D query = points.get(i);

                NearestNeighbors nn = new NearestNeighbors(points);

                long startTime = System.nanoTime();
                List<Point3D> neighbors = nn.rangeQuery(query, eps);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                count++;
                timeavg+=duration;
            }
        } else if (args[0].equals("kd")) {
            //Calculate average time for KDTree function
            for (int i = 0; i < points.size(); i+=Integer.parseInt(args[3])) {
                Point3D query = points.get(i);
                KDtree k = new KDtree();

                NearestNeighborsKD nnkd= new NearestNeighborsKD(points);

                long startTime = System.nanoTime();
                List<Point3D> neighborsKD = nnkd.rangeQuery(query,eps);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime); // in milliseconds.
                count++;
                timeavg+=duration;
            }
        }
        double timeavgtest = timeavg/count;
        System.out.println("Average Runtime = "+timeavgtest/1000000 +"ms");
        }
}
