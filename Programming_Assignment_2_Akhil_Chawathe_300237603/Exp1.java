/**
 * Experiment 1 Completed
 *
 * CSI2110 Data Structures and Algorithms
 * www.uottawa.ca
 *
 * @author Robert Laganiere
 * @author Akhil Chawathe (300237603)
 *
 * 2022
 *
*/

import java.util.List;
import java.util.ArrayList;

import java.io.*;
import java.util.Scanner;

public class Exp1 {

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
	/**
	 *
	 * Saves the Cluster of a query point into a new file.
	 *
	 * @param filename
	 * @param p
	 * @throws IOException
	 *
	 */
  public static void save(List<Point3D> p, String filename) throws IOException {
	  FileWriter writer = new FileWriter(filename);
	  writer.write("x,y,z\n");

	  for (Point3D point: p) {
		  writer.write(point.getX() + "," + point.getY() + "," + point.getZ()+"\n"); // Write the coordinates into the text file.
	  }
	  writer.close(); // Completes and saves file creates.
  }

  //main method
  public static void main(String[] args) throws Exception {
    // not reading args[0]
	double eps= Double.parseDouble(args[1]);
  
    // reads the csv file
    List<Point3D> points= Exp1.read(args[2]);
	  Point3D query = new Point3D(Double.parseDouble(args[3]),
			  Double.parseDouble(args[4]),
			  Double.parseDouble(args[5]));

	if (args[0].equals("lin")) {
		// creates the NearestNeighbor instance
		NearestNeighbors nn = new NearestNeighbors(points);
		List<Point3D> neighbors = nn.rangeQuery(query, eps);

		//Prints out the number of Neighbors and lists all the neighbors
		System.out.println("number of neighbors= " + neighbors.size());
		System.out.println(neighbors);

		//Saves neighbors into a new file
		save(neighbors, "pt_lin");

	} else if (args[0].equals("kd")) {
		// creates the KDTree instance
		KDtree k = new KDtree();

		// creates the NearestNeighborsKD instance
		NearestNeighborsKD nnkd= new NearestNeighborsKD(points);
		List<Point3D> neighborsKD = nnkd.rangeQuery(query,eps);


		//Prints out the number of Neighbors and lists all the neighbors
		System.out.println("number of neighbors= "+neighborsKD.size());
		System.out.println(neighborsKD);

		//Saves neighbors into a new file
		save(neighborsKD,"pt_kd");
	}

  }   
}
