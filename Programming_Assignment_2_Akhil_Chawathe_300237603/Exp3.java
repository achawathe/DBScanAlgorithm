/**
 * DBScan Algorithm - updated to work with KDTrees.
 *
 * @author Akhil Chawathe
 *
 * Student Number: 300237603
 *
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Exp3 {
    // Fields

    int numberOfClusters;

    double eps;

    double minPts;

    int numpoints = 0;


    List<Point3D> pt3D;

    NearestNeighborsKD nearestNeighbors;



    /**
     * Base constructor for DBScan.
     * @param pt3D
     */
    public Exp3(List<Point3D> pt3D){
        this.pt3D = pt3D;
        this.eps = 1.2;
        this.minPts = 10;
        this.nearestNeighbors =  new NearestNeighborsKD(pt3D);
    }

    /**
     *
     * A setter to change the eps value, returning the previous one.
     *
     * @param Eps
     * @return double
     *
     */
    public double setEps(double Eps){
        double temp = this.eps;
        this.eps = Eps;
        return temp;
    }

    /**
     *
     * A setter to change the minPts value.
     *
     * @param minPts
     *
     */
    public void setMinPts(double minPts) {
        this.minPts = minPts;
    }

    /**
     *
     * Getter for number of clusters
     *
     * @return int
     *
     */
    public int getNumberOfClusters() {
        return this.numberOfClusters;
    }

    /**
     *
     * Returns the list of points within the object
     *
     * @return List
     *
     */
    public List<Point3D> getPoints() {
        return pt3D;
    }

    /**
     *
     * Executes DBScan algorithm.
     *
     */
    public void findClusters(){
        int C = 0; //Cluster counter
        int noise = 0;
        nearestNeighbors = new NearestNeighborsKD(getPoints());
        List<Point3D> DB = getPoints();
        for (Point3D p: DB) {
            numpoints = 0;
            if(p.label != -1){  //not undefined therefore already processed
                continue;
            }

            //Check if any given point is null
            if( p!= null) {

                List<Point3D> N = nearestNeighbors.rangeQuery(p, eps); //find neighbors of initial point

                //Check if noise
                if (N.size() < minPts) {
                    p.label = 0; // label as noise
                    noise++;
                    continue;
                }
                C++;

                p.label = C;

                Stack<Point3D> S = new Stack<>();
                S.addAll(N); //add neighbors to stack.

                while (!(S.empty())){


                    Point3D Q = S.pop();

                    //Set point Q to border point instead of noise
                    if(Q.label == 0){
                        Q.label = C;
                        noise--;
                    }

                    //Processed previously
                    if(Q.label != -1)
                        continue;

                    //Neighbor label
                    Q.label = C;

                    //Find Neighbors
                    List<Point3D> Neighbors = nearestNeighbors.rangeQuery(Q, eps);

                    //Add neighbors to stack if density check is satisfied.
                    if (Neighbors.size() >= minPts) {
                        S.addAll(Neighbors);
                    }


                }

                this.numberOfClusters = C;
            }
        }
        System.out.println("Number of noise points = " + noise);
    }

    /**
     *
     * Saves the new point3D Clusters into a new file.
     *
     * @param filename
     * @throws IOException
     *
     */
    public void save(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("x,y,z,C,R,G,B\n");


        //Randomize RGB Points.
        Random random = new Random();
        int prevcluster = getPoints().get(0).label;
        double R = random.nextDouble(0,1);
        double G = random.nextDouble(0,1);
        double B = random.nextDouble(0,1);

        //Save points to file
        for (int i = 0; i < getPoints().size(); i++) {


            Point3D p = getPoints().get(i);
            //Keep RGB values if in same cluster
            if (p.label == prevcluster){
                writer.write(p.getX() + "," + p.getY() + "," + p.getZ() + "," + p.label + "," + R + "," + G + "," + B + "\n");
                numpoints++;
                continue;
            }
            if(p.label !=0) {
                System.out.println("Number of points in cluster " + p.label + " = " + numpoints);
                prevcluster = p.label;
            }

            //Generate random RGB values if in different cluster and write new RGB values to file.
            R = random.nextDouble(0,1);
            G = random.nextDouble(0,1);
            B = random.nextDouble(0,1);
            writer.write(p.getX() + "," + p.getY() + "," + p.getZ() + "," + p.label + "," + R + "," + G + "," + B + "\n");

        }
        writer.close();
    }

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

        //Read File
        File file = new File(filename);
        Scanner reader = new Scanner(file);

        //Make sure line is readable.
        try{
            String str = reader.nextLine();
            String[] splitString = str.split(",");
            Point3D point = new Point3D((Double.parseDouble(splitString[0].strip())), (Double.parseDouble(splitString[1].strip())), (Double.parseDouble(splitString[2].strip())));
        }catch (Exception e){

        }


        //read file and add to stack.
        List<Point3D> l = new Stack<>();

        while (reader.hasNextLine()) {
            String str = reader.nextLine();
            String[] splitString = str.split(",");
            Point3D point = new Point3D((Double.parseDouble(splitString[0].strip())), (Double.parseDouble(splitString[1].strip())), (Double.parseDouble(splitString[2].strip())));
            l.add(point);
        }
        reader.close();
        return l;
    }

    /**
     *
     * Main method. For args input, cd into folder, type file name, followed by eps then minPoints.
     *
     * @param args
     * @throws Exception
     *
     */
    public static void main(String[] args) throws Exception {
        long startTimeFinal = System.currentTimeMillis();
        //Set args to variables.
        String inputFile = args[0];
        String inputEps = args[1];
        String inputMinimumPoints = args[2];

        //read input file
        long startTime = System.currentTimeMillis();
        List<Point3D> p = read(inputFile);
        long endTime = System.currentTimeMillis();
        System.out.println("File reading completed in " + (endTime-startTime) + "ms.");

        //Ensure that file is usable
        try {
            Exp3 scanTest = new Exp3(p);
            scanTest.setEps(Double.parseDouble(inputEps));
            scanTest.setMinPts(Integer.parseInt(inputMinimumPoints));
        }catch (Exception e){
            throw new Error("Cannot convert eps to double and/or Minpoints to int.");
        }

        //Set Values
        startTime = System.currentTimeMillis();
        Exp3 scan = new Exp3(p);
        scan.setEps(Double.parseDouble(inputEps));
        scan.setMinPts(Integer.parseInt(inputMinimumPoints));
        endTime = System.currentTimeMillis();
        System.out.println("Setting values completed in " + (endTime-startTime) + "ms.");

        startTime = System.currentTimeMillis();
        scan.nearestNeighbors = new NearestNeighborsKD(p);
        endTime = System.currentTimeMillis();
        System.out.println("KDTree creation completed in " + (endTime-startTime) + "ms.");

        //Run DBScan algorithm
        startTime = System.currentTimeMillis();
        scan.findClusters();
        endTime = System.currentTimeMillis();
        System.out.println("DBScan algorithm completed in " + (endTime-startTime) + "ms.");

        //Save file with new file name.
        startTime = System.currentTimeMillis();
        String finalFileName = inputFile.substring(0,inputFile.length()-4) + "_clusters_"+ scan.eps +"_"+ scan.minPts + "_" + scan.getNumberOfClusters() + ".csv";
        scan.save(finalFileName);
        endTime = System.currentTimeMillis();
        System.out.println("File Save completed in " + (endTime-startTime) + "ms.");


        long endTimeFinal = System.currentTimeMillis();
        long duration = endTimeFinal-startTimeFinal;
        System.out.println("Total Duration = " + duration);
    }
}
