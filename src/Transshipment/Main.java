package Transshipment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class Main {

	final static String SOURCE = "Provider";
	final static String TRANSSHIPING = "Intermediate";
	final static String SINK = "Warehouse";
	final static int INF = 99999, V = 8; 

	// oferta
	ArrayList<String> sources;
	// no se si lo necesito
	// demanda
	ArrayList<String> sink;
	// todos
	ArrayList<String> all;
	int total;

	int[] supply;
	int[] demand;
	LinkedList<Variable> feasible = new LinkedList<Variable>();

	TransshipmentProblem tp;

	public Main() {
		sources = new ArrayList<String>();
		sink = new ArrayList<String>();
		all = new ArrayList<String>();
		total = 0;
		tp = new TransshipmentProblem();
	
	}

	/**
	 * Reads the txt file with the graph and the costs
	 * @param path the path to the file
	 * @throws IOException
	 */

	public void readGraph(String path) throws IOException {
		String sentence;
		FileReader f = new FileReader(path);
		BufferedReader br = new BufferedReader(f);
		
		// separates the graph from the classification of nodes in the file
		boolean graphTime = true;
		int[][] cost =new int [1][1];
		while ((sentence = br.readLine()) != null) {

			String[] tokens = sentence.split(",");
			String lastToken = tokens[tokens.length - 1];

			if (!lastToken.matches("-?(0|[1-9]\\d*)")) {
				String name = tokens[0];
				//adds each node name to an array according to where it belongs
				switch (lastToken) {
				case SOURCE:
					tp.addSource(name);
					tp.addAll(name);
					break;
				case TRANSSHIPING:
					tp.addSource(name);
					tp.addSink(name);
					tp.addAll(name);
					break;
				case SINK:
					tp.addSink(name);
					tp.addAll(name);
					break;
				default:
					System.out.println("Format error in file "+lastToken);
					break;
				}
			} else {
				// If the last token is a number begin the graph 
				if (graphTime) {
					//no longer graph time
					graphTime = false;
					//After we learn the problem size we can initialize the arrays
					tp.initializeCostMatrix();
					tp.initializeSupplyDemand();
				}
				
				tp.setCost(tokens, Integer.parseInt(lastToken));
			}
		}
		//shows cost matrix 
		tp.printCostMatrix();
		tp.getShortestMatrix();
		br.close();
	}
	

	public void readCase(String path) throws IOException {
		String sentence;
		FileReader f = new FileReader(path);
		BufferedReader br = new BufferedReader(f);
		supply = new int[all.size()];
		demand = new int[all.size()];
		
		while ((sentence = br.readLine()) != null) {
			String[] tokens = sentence.split(",");
			tp.setSupplyDemand(tokens);
		}
		// shows supply and demand (adjusted)
		tp.setTotalSupply();
		tp.printSD();
		

		if (IntStream.of(supply).sum() == IntStream.of(demand).sum())
			total = IntStream.of(supply).sum();

		for (int i = 0; i < demand.length; i++) {
			demand[i] += total;
		}

		for (int i = 0; i < supply.length; i++) {
			supply[i] += total;
		}
		// MinCost(supply, demand, cost);

	}


	public static int MinCost(int[] s, int[] d, int[][] c) {
		int[][] table = new int[s.length][d.length];

		int[] supply = s.clone(); // copy supply and demand and cost array
		int[] demand = d.clone(); // so original arrays remain unchanged
		int[][] cost = c.clone();
		int totalCost = 0;
		int smallest = c[0][0];
		int ii = 0, jj = 0;
		boolean rowSatisfied = false;
		boolean colSatisfied = false;

		// find smallest cost of shipment from supplier
		for (int i = 0; i < supply.length; i++) {
			for (int j = 0; j < demand.length; j++) {
				if (smallest > cost[i][j]) {
					smallest = cost[i][j];
					ii = i;
					jj = j;
				}
			}
		}

		// find the smallest value, store it in smallest
		int last = smallest;

		while (colSatisfied != true) {
			// If Supply >= Demand, demand is filled completely, demand = 0
			if (supply[ii] >= demand[jj]) {
				table[ii][jj] = demand[jj];
				supply[ii] = supply[ii] - demand[jj];
				demand[jj] = 0;
			}
			// If Supply < Demand, demand = demand - supply, supply = 0
			else {
				table[ii][jj] = supply[ii];
				demand[jj] = demand[jj] - supply[ii];
				supply[ii] = 0;
			}
			System.out.println("table " + table[ii][jj]);
			System.out.println(ii + "  " + jj);

			System.out.println("supply " + supply[ii]);
			System.out.println("demand " + demand[jj]);
			System.out.println("smallest " + smallest + " last " + last);
			int temp = 0;
			// Find next smallest cost of shipping
			for (int i = 0; i < s.length; i++) {
				if (cost[i][jj] > last) {
					temp = cost[i][jj];
					System.out.println(temp);
					if (temp <= cost[i][jj]) {
						smallest = cost[i][jj];
						ii = i;
						System.out.println(i + " " + temp);
					}
				}
				System.out.println(i + " " + ii + "  " + jj + " " + smallest);
			}
			System.out.println("smallest " + smallest + " last " + last);
			System.out.println(ii + "  " + jj);
			if (demand[jj] == 0)
				colSatisfied = true;
		}

		for (int i = 0; i < cost.length; i++) {
			System.out.println(Arrays.toString(table[i]));
		}
		// Calculate Shipping cost using Minimum Cost Method
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				totalCost = totalCost + c[i][j] * table[i][j];
				// System.out.println("partial cost "+c[i][j]+" - " +table[i][j]);
			}
		}
		System.out.println("Total Cost = $" + totalCost);
		return (totalCost);
	}
	
	public void solve() {
		tp.leastCostCellMethod();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String path = ".\\data\\ChallengeGraph.txt";
		Main m = new Main();
		m.readGraph(path);
		path = ".\\data\\caso1.txt";
		m.readCase(path);
		m.solve();
		
		//System.out.println(m.getSolution());

	}

}
