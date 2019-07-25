import java.sql.NClob;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Vogel {
	final static int[] demand = {};
	final static int[] supply = {};
	//first calculate matrix add dummy if required
	final static int[][] cost = {{}, {}, {}, {}}; 
	
	//number of origins
	final static int nRows = supply.length;
	//number of destinations
	final static int mColums = demand.length;
	
	static boolean[] rowDone = new boolean [nRows];
	static boolean[] colDone = new boolean [mColums];
	static int [][] result = new int[nRows][mColums];
	
	ExecutorService executor = Executors.newFixedThreadPool(2);
	
	
	public static void main(String[] args) throws Exception {
		int totalSupply = IntStream.of(supply).sum();
		int totalCost =0;
		int supplyLeft = IntStream.of(supply).sum();
		
		while (supplyLeft>0) {
			
		}
		
		
	}
	
	static int [] maxPenalty() {
		//cant be negative
		int pc =-1, pm =-1, mc =-1;
		
		
	}
	
	
}
