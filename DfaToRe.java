import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class DfaToRe{
	
	//variables for reading file and keeping data
	static String str;
	static String[][] dfaToRe;
	static String[] startArr;
	static String startState;
	static String[] acceptArrTemp;
	static String[] acceptArr;
	static String[] alphabetArrTemp;
	static String[] alphabetArr;
	static String[] allStateArrTemp;
	static String[] allStateArr;
	static int numState,numAccept;
	static String empty="ε";
	static String finalRE;
	
	//output of regular expressions
	static String removedState;
	static ArrayList<String> currentState = new ArrayList<String>();
	static ArrayList<String>tempCurrentState = new ArrayList<String>();
	static boolean isOnce;
	
	public static void main(String args[]){
		
		readFile();
		ConvertFromDfaToRe();

	}
	
	public static void ConvertFromDfaToRe() {
		
		for(int i=1;i<numState-1;i++){ //qrip count
			for(int y=0;y<numState;y++){
				if(y!=i && !dfaToRe[y][i].equals("") && !dfaToRe[i][y].equals("")){
					if(!dfaToRe[i][i].equals("")){
						if(!dfaToRe[y][y].equals("")){
							if(dfaToRe[i][y].equals(empty))
								dfaToRe[y][y]+=("U"+dfaToRe[y][i]+"("+dfaToRe[i][i]+")*");
							else
							dfaToRe[y][y]+=("U"+dfaToRe[y][i]+"("+dfaToRe[i][i]+")*"+dfaToRe[i][y]);
						}
						else{
							if(dfaToRe[y][i].equals(empty) &&dfaToRe[i][y].equals(empty))
								dfaToRe[y][y]="("+dfaToRe[i][i]+")*";
							else if(dfaToRe[y][i].equals(empty))
								dfaToRe[y][y]="("+dfaToRe[i][i]+")*"+dfaToRe[i][y];
							else if(dfaToRe[i][y].equals(empty))
								dfaToRe[y][y]=dfaToRe[y][i]+"("+dfaToRe[i][i]+")*";
							else
								dfaToRe[y][y]=dfaToRe[y][i]+"("+dfaToRe[i][i]+")*"+dfaToRe[i][y];
						}
					}else{
						if(!dfaToRe[y][y].equals("")){
							if(dfaToRe[y][i].equals(empty) &&dfaToRe[i][y].equals(empty))
								dfaToRe[y][y]+="U";
							else if(dfaToRe[y][i].equals(empty))
								dfaToRe[y][y]+="U"+dfaToRe[i][y];
							else if(dfaToRe[i][y].equals(empty))
								dfaToRe[y][y]+="U"+dfaToRe[y][i];
							else
								dfaToRe[y][y]+=("U"+dfaToRe[y][i]+dfaToRe[i][y]);
						}
						else{
							if(dfaToRe[y][i].equals(empty) &&dfaToRe[i][y].equals(empty))
								dfaToRe[y][y]="";
							else if(dfaToRe[y][i].equals(empty))
								dfaToRe[y][y]=dfaToRe[i][y];
							else if(dfaToRe[i][y].equals(empty))
								dfaToRe[y][y]=dfaToRe[y][i];
							else
								dfaToRe[y][y]=dfaToRe[y][i]+dfaToRe[i][y];
						}
					}
				}
				
				for(int z=Math.max(y+1,i+1);z<numState;z++){
					if(y!=i && z!=i){// ignore the lines return itself only linked states
						if(!dfaToRe[i][i].equals("")){
							if(!dfaToRe[y][i].equals("") && !dfaToRe[i][z].equals("")){//from qn to qn+..(next states)
								if(!dfaToRe[y][z].equals("")) {
									if(dfaToRe[i][z].equals(empty) && dfaToRe[y][i].equals(empty))
										dfaToRe[y][z]+=("U"+dfaToRe[y][i]+"("+dfaToRe[i][i]+")*");
									else if(dfaToRe[i][z].equals(empty))
										dfaToRe[y][z]+=("U"+dfaToRe[y][i]+"("+dfaToRe[i][i]+")*");
									else if(dfaToRe[y][i].equals(empty))
										dfaToRe[y][z]+=("U"+"("+dfaToRe[i][i]+")*"+dfaToRe[i][z]);
									else
										dfaToRe[y][z]+=("U"+dfaToRe[y][i]+"("+dfaToRe[i][i]+")*"+dfaToRe[i][z]);
								}
									
								else {
									if(dfaToRe[i][z].equals(empty) && dfaToRe[y][i].equals(empty))
										dfaToRe[y][z]="("+dfaToRe[i][i]+")*";
									else if(dfaToRe[i][z].equals(empty))
										dfaToRe[y][z]=dfaToRe[y][i]+"("+dfaToRe[i][i]+")*";
									else if(dfaToRe[y][i].equals(empty))
										dfaToRe[y][z]="("+dfaToRe[i][i]+")*"+dfaToRe[i][z];
									else
										dfaToRe[y][z]=dfaToRe[y][i]+"("+dfaToRe[i][i]+")*"+dfaToRe[i][z];
								}
									
							}
				
							if(!dfaToRe[z][i].equals("") && !dfaToRe[i][y].equals("")){ //start from qstart to qn(old states)
								if(!dfaToRe[z][y].equals("")) {
									if(dfaToRe[z][i].equals(empty) &&dfaToRe[i][y].equals(empty))
										dfaToRe[z][y]+="U("+dfaToRe[i][i]+")*";
									else if(dfaToRe[z][i].equals(empty))
										dfaToRe[z][y]+="U("+dfaToRe[i][i]+")*"+dfaToRe[i][y];
									else if(dfaToRe[i][y].equals(empty))
										dfaToRe[z][y]+="U"+dfaToRe[z][i]+"("+dfaToRe[i][i]+")*";
									else
										dfaToRe[z][y]+=("U"+dfaToRe[z][i]+"("+dfaToRe[i][i]+")*"+dfaToRe[i][y]);
								}	
								else {
									if(dfaToRe[z][i].equals(empty) && dfaToRe[i][y].equals(empty))
										dfaToRe[z][y]="("+dfaToRe[i][i]+")*";
									else if(dfaToRe[z][i].equals(empty))
										dfaToRe[z][y]="("+dfaToRe[i][i]+")*"+dfaToRe[i][y];
									else if(dfaToRe[i][y].equals(empty))
										dfaToRe[z][y]=dfaToRe[z][i]+"("+dfaToRe[i][i]+")*";
									else
										dfaToRe[z][y]=dfaToRe[z][i]+"("+dfaToRe[i][i]+")*"+dfaToRe[i][y];
								}
									
							}
						}
						else{
							if(!dfaToRe[y][i].equals("") && !dfaToRe[i][z].equals("")){
								if(!dfaToRe[y][z].equals("")) {
									if(dfaToRe[y][i].equals(empty) &&dfaToRe[i][z].equals(empty))
										dfaToRe[y][z]+="U";
									else if(dfaToRe[y][i].equals(empty))
										dfaToRe[y][z]+="U"+dfaToRe[i][z];
									else if(dfaToRe[i][z].equals(empty))
										dfaToRe[y][z]+="U"+dfaToRe[y][i];
									else
										dfaToRe[y][z]+=("U"+dfaToRe[y][i]+dfaToRe[i][z]);
								}
								else {
									if(dfaToRe[y][i].equals(empty) &&dfaToRe[i][z].equals(empty))
										dfaToRe[y][z]="";
									else if(dfaToRe[y][i].equals(empty))
										dfaToRe[y][z]=dfaToRe[i][z];
									else if(dfaToRe[i][z].equals(empty))
										dfaToRe[y][z]=dfaToRe[y][i];
									else
										dfaToRe[y][z]=(dfaToRe[y][i]+dfaToRe[i][z]);
								}
									
							}
					
							if(!dfaToRe[z][i].equals("") && !dfaToRe[i][y].equals("")){
								if(!dfaToRe[z][y].equals("")) {
									if(dfaToRe[z][i].equals(empty) &&dfaToRe[i][y].equals(empty))
										dfaToRe[z][y]+="U";
									else if(dfaToRe[z][i].equals(empty))
										dfaToRe[z][y]+="U"+dfaToRe[i][y];
									else if(dfaToRe[i][y].equals(empty))
										dfaToRe[z][y]+="U"+dfaToRe[z][i];
									else
										dfaToRe[z][y]+=("U"+dfaToRe[z][i]+dfaToRe[i][y]);
								}
								else {
									if(dfaToRe[z][i].equals(empty) &&dfaToRe[i][y].equals(empty))
										dfaToRe[z][y]="";
									else if(dfaToRe[z][i].equals(empty))
										dfaToRe[z][y]=dfaToRe[i][y];
									else if(dfaToRe[i][y].equals(empty))
										dfaToRe[z][y]=dfaToRe[z][i];
									else
										dfaToRe[z][y]=(dfaToRe[z][i]+dfaToRe[i][y]);
								}
							}
						}
					}
				}
				if(y==0)
					y=i;
			}
			
			for(int k=0;k<numState;k++){
				dfaToRe[k][i]="";
				dfaToRe[i][k]="";
			}
			
			System.out.println();
			System.out.println("--------------------------");
			System.out.println("\nGNFA -> "+(currentState.size()-1)+" state");
			System.out.println("qrip -> "+ allStateArr[i-1] );
			
			removedState=allStateArr[i-1];
			
			if(currentState.contains(removedState)){
				currentState.remove(removedState);
			}
			System.out.println("Q = "+ currentState);
		
			for(int a=0;a<dfaToRe.length;a++) {
				for(int b=0;b<dfaToRe.length;b++) {
					if(!dfaToRe[a][b].equals("")) {
						System.out.println(tempCurrentState.get(a) + " , "+ dfaToRe[a][b] + " = " +tempCurrentState.get(b));
						finalRE=tempCurrentState.get(a) + " , "+ dfaToRe[a][b] + " = " +tempCurrentState.get(b);
					}
				}
			}
	
	//		displayArr();
		}  
		System.out.println();
		System.out.println("--------------------------");
		System.out.println("\nRegular Expression");
		System.out.println(finalRE);
		System.out.println("\n--------------------------");
	}

	
	public static void createArray(String fromState,String way, String toState) {
		
    	int indexFrom=0;
    	while(!fromState.equals(allStateArr[indexFrom])) { // find the index of states in states array
    		indexFrom++;
    	}
    	indexFrom =indexFrom +1;
    	
    	int indexTo=0;
    	while(!toState.equals(allStateArr[indexTo])) {// find the index of states in states array
    		indexTo++;
    	}
    	indexTo =indexTo +1;
    	
    	if(fromState.equals(startState) && !isOnce) {
    		dfaToRe[0][indexFrom]=empty; //qstart state
    		isOnce=true;
    		currentState.add("qS");
    		tempCurrentState.add("qS");
    		
    		for (int i = 0; i < allStateArr.length; i++) {
				currentState.add(allStateArr[i]);
				tempCurrentState.add(allStateArr[i]);
			}
			currentState.add("qa");
			tempCurrentState.add("qa");
    	}
    	
    	for(int i=0;i<acceptArr.length;i++) {
    		if(fromState.equals(acceptArr[i])) {
        		dfaToRe[indexFrom][numState-1]=empty;
        	}
    	}
    	
    	for(int i=0;i<acceptArr.length;i++) {
    		if(toState.equals(acceptArr[i])) {
        		dfaToRe[indexTo][numState-1]=empty;
        	}
    	}
    	
    	if(!dfaToRe[indexFrom][indexTo].equals("")) {
    		dfaToRe[indexFrom][indexTo]=dfaToRe[indexFrom][indexTo]+"U"+way;
    	}
    	else {
    		dfaToRe[indexFrom][indexTo]=way;
    	}
	}
	
	public static void readFile() {
		
		try (BufferedReader readerDelimiter = Files.newBufferedReader(Paths.get("DFA.txt"))) {
			
			String readStart=readerDelimiter.readLine(); //S
			startArr = readStart.split("=");
			startState=startArr[1];
			
			String readAccept=readerDelimiter.readLine(); //A
			acceptArrTemp = readAccept.split("=");
			acceptArr = acceptArrTemp[1].split(",");
			
			String readAlphabet=readerDelimiter.readLine(); //E
			alphabetArrTemp = readAlphabet.split("=");
			alphabetArr = alphabetArrTemp[1].split(",");
			
			String readAllState=readerDelimiter.readLine(); //Q
			allStateArrTemp = readAllState.split("=");
			allStateArr = allStateArrTemp[1].split(",");
			
		  	numState=allStateArr.length+2;
		    dfaToRe=new String[numState][numState];
		    
		    for(int i=0;i<numState;i++) {
		    	for(int j=0;j<numState;j++) {
		    		dfaToRe[i][j]="";
		    	}
		    }
		    
		    numAccept=acceptArr.length;
		    
			System.out.println("\n--------------------------\n");
		    System.out.println("Initial Situation");
		    System.out.println("DFA -> "+allStateArr.length+" state");
		    System.out.print("Q = [");
		    for(int a=0;a<allStateArr.length;a++) {
		    	if(a!=allStateArr.length-1)
		    		System.out.print(allStateArr[a]+", ");
		    	else
		    		System.out.print(allStateArr[a]);
		    }
		    System.out.print("]\n");
            while ((str = readerDelimiter.readLine()) != null) {
            	
            	String[] temp=str.split("=");
            	String[] tempState = temp[0].split(",");
            	String fromState=tempState[0];
            	String way=tempState[1];
            	String toState=temp[1];
            	
            	System.out.println(fromState + " , "+ way + " = " +toState);	
				
        
            	createArray(fromState, way, toState);
            }
            System.out.println("\n--------------------------\n");
            System.out.println("GNFA -> "+currentState.size()+" state");
            System.out.println("Q = "+ currentState+"  / qS and qa stats added!");
        	for(int a=0;a<dfaToRe.length;a++) {
				for(int b=0;b<dfaToRe.length;b++) {
					if(!dfaToRe[a][b].equals("")) {
						
						if(dfaToRe[a][b].indexOf("U")!=-1) {
							String[] splitElem=dfaToRe[a][b].split("U");
							
							for(int k=0;k<splitElem.length;k++)
								System.out.println(tempCurrentState.get(a) + " , "+ splitElem[k] + " = " +tempCurrentState.get(b));	
							
						}
						else
							System.out.println(tempCurrentState.get(a) + " , "+ dfaToRe[a][b] + " = " +tempCurrentState.get(b));
					}
				}
			}
           
         //   displayArr();
        	
		}
		catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	
	public static void displayArr() {
		
		System.out.println(" ");
		for(int x=0;x<numState;x++){
			System.out.print(" | ");
			for(int y=0;y<numState;y++){
				System.out.print(dfaToRe[x][y]+" | ");
			}
			System.out.println(" ");
		}
	}
} 	