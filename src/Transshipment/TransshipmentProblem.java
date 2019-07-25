package Transshipment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class TransshipmentProblem {
	
final static int INF = 999999;
	// Names from the source nodes
	ArrayList<String> sources;

	// Names for the destination nodes
	ArrayList<String> sink;

	// All names of nodes
	ArrayList<String> all;

	// Cost matrix (going from one place to another)
	int[][] cost;

	// Supply values from each source
	int[] supply;

	// Demand values to each sink
	int[] demand;

	// The total supply from sources, (should be balanced with demand)
	int totalSupply;
	
	AllPairShortestPath apsp;

	public TransshipmentProblem() {
		sources = new ArrayList<String>();
		sink = new ArrayList<String>();
		all = new ArrayList<String>();
		totalSupply = 0;
		apsp = new AllPairShortestPath();

	}

	public void addSource(String pSourceName) {
		sources.add(pSourceName);
	}

	public void addSink(String pSinkName) {
		sink.add(pSinkName);
	}

	public void addAll(String pNode) {
		all.add(pNode);
	}

	public int problemSize() {
		return all.size();
	}

	public void initializeCostMatrix() {

		// Square matrix (all vs. all)
		cost = new int[all.size()][all.size()];
		// Initialize the matrix assuming is impossible to get anywhere
		for (int i = 0; i < cost.length; i++) {
			for (int j = 0; j < cost.length; j++) {
				// cost[i][j] = 0;
				cost[i][j] = INF;
			}
		}
	}

	public void setCost(String[] tokens, int value) {
		// assign cost value
		int i = all.indexOf(tokens[0]);
		int j = all.indexOf(tokens[1]);
		cost[i][j] = value;
		// in case they forget the bidirectionality
		cost[j][i] = value;
	}

	public void initializeSupplyDemand() {
		supply = new int[all.size()];
		demand = new int[all.size()];
	}

	public void setSupplyDemand(String[] tokens) {

		int i = sources.indexOf(tokens[0]);
		int j = sink.indexOf(tokens[0]);

		if (i >= 0)
			supply[i] = Integer.parseInt(tokens[1]);
		if (j >= 0)
			demand[j] = Integer.parseInt(tokens[1]);
	}

	/**
	 * Sets the total supply value (which should be balanced with demand) Adjusts
	 * the supply and demand values for transshipment problem
	 */
	public void setTotalSupply() {

		if (IntStream.of(supply).sum() == IntStream.of(demand).sum())
			totalSupply = IntStream.of(supply).sum();

		for (int i = 0; i < demand.length; i++) {
			demand[i] += totalSupply;
		}

		for (int i = 0; i < supply.length; i++) {
			supply[i] += totalSupply;
		}
	}

	public void printCostMatrix() {
		System.out.println("The cost matrix is: ");
		for (int i = 0; i < cost.length; i++) {
			System.out.println(Arrays.toString(cost[i]));
		}
	}

	public void printSD() {
		System.out.println("\n");
		System.out.println("The supply array is: ");
		System.out.println(Arrays.toString(supply));
		System.out.println("The demand array is:");
		System.out.println(Arrays.toString(demand));
		System.out.println("The total supply is: " + totalSupply);
	}

	/**
	 * Solves the transportation problem using least cost cell method
	 */
	public void leastCostCellMethod() {
		// Where we are storing the results
		int[][] table = new int[all.size()][all.size()];
		int smallest = cost[0][0]-1;
		boolean c[][] = new boolean[all.size()][all.size()];
		for (int i = 0; i < all.size(); i++) {
			Arrays.fill(c[i], Boolean.TRUE);
		}

		// saved indexes
		int ii = 0, jj = 0, min = 0, x = 0;
		// Iterate while we 
		while (smallest < INF) {
			x++;
			smallest = INF;
			// find next minimun cost
			
			for (int i = 0; i < supply.length; i++) {
				for (int j = 0; j < demand.length; j++) {
					if (c[i][j]) {
						if (smallest > cost[i][j]) {
							smallest = cost[i][j];
							ii = i;
							jj = j;
						}
					}
				}
			}
			System.out.println(
					"smallest = " + smallest + " ii: " + ii + " s " + supply[ii] + " -jj: " + jj + " d" + demand[jj]);

			min = Math.min(supply[ii], demand[jj]);
			table[ii][jj] = min;
			supply[ii] = supply[ii] - min;
			demand[jj] = demand[jj] - min;
			// cancel supply
			if (supply[ii] == 0) {
				c[ii] = new boolean[all.size()];
				System.out.println("cancelled supply");
			}
			// cancel demand
			else if (demand[jj] == 0) {
				System.out.println("cancelled demand");
				for (int i = 0; i < c.length; i++) {
					c[i][jj] = false;
				}
			}

			System.out.println("Allocated " + table[ii][jj]);

			printSD();
			
			System.out.println("The cancelled matrix is");
			for (int i = 0; i < cost.length; i++) {
				System.out.println(Arrays.toString(c[i]));
			}

		}

		System.out.println("The table matrix is: ");
		for (int i = 0; i < cost.length; i++) {
			System.out.println(Arrays.toString(table[i]));
		}
		
		System.out.println("The cost matrix is: ");
		for (int i = 0; i < cost.length; i++) {
			System.out.println(Arrays.toString(cost[i]));
		}
		
		calculateTotal(table);
		printSD();

		System.out.println(x);
		System.out.println();

	}
	
	public void getShortestMatrix() {
		cost=apsp.floydWarshall(cost);
	}
	
	public void calculateTotal(int[][]table) {
		int total =0;
		for (int i = 0; i < cost.length; i++) {
			for (int j = 0; j < cost.length; j++) {
				if(cost[i][j]*table[i][j]>0) {
					System.out.println(i);
				}
				total += cost[i][j]*table[i][j];
			}
		}
		System.out.println("The total cost is:" + total);
	}

}
