import java.util.LinkedList;

public class TransportationProblem {
	
	final static int[] source = {50, 60, 50, 50};
	final static int[] sink = {30, 20, 70, 30, 60};
	final static int cost[][]  = {{16, 16, 13, 22, 17}, {14, 14, 13, 19, 15},
		    {19, 19, 20, 23, 50}, {50, 12, 50, 15, 11}};
	LinkedList<Variable> feasible = new LinkedList<Variable>();
	
	//Number of sources and sinks 
	int mSource;
	int nSink;
	
	public TransportationProblem(int pSource, int pSink) {
		mSource = pSource;
		nSink = pSink;
		//source = new int [pSource];
		//sink = new int [pSink];
		//cost = new int [pSource][pSink];

        for(int i=0; i < (pSink + pSource -1); i++)
            feasible.add(new Variable());
	}
	
	public void northWestCorner() {
		int min;
		int k=0;// feasible solution counter
		//was used in solution? 
		boolean isSet[][] = new boolean[nSink][mSource];
		//inicializar matriz
		for (int i = 0; i < nSink; i++) {
			for (int j = 0; j < mSource; j++) {
				isSet[i][j]= false;
			}
		}
		
		for (int i = 0; i < nSink; i++) 
			for (int j = 0; j < mSource; j++) 
				if (!isSet[i][j]) { 
					
					min = Math.min(source[j], sink[i]);
					feasible.get(k).setRequired(i);
					feasible.get(k).setStock(i);
					feasible.get(k).setValue(min);
					k++;
					source[j] -=min;
					sink[i] -=min;
				
					if(source[j] == 0)
                        for(int l = 0; l < mSource; l++)
                            isSet[j][l] = true;                    
                    else
                        for(int l = 0; l < nSink; l++)
                            isSet[l][i] = true;
				}
		
	}
	
	
    public double getSolution(){
        double result = 0;
        for(Variable x: feasible){
            result += x.getValue() * cost[x.getStock()][x.getRequired()];
        }
        
        return result;
    }
    
    public static void main(String[] args) {
    	TransportationProblem test = new TransportationProblem(4, 5);
    	test.northWestCorner();
    	
    	for(Variable t: test.feasible){
            System.out.println(t);        
        }
    }

}
