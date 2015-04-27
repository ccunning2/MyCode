import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class WeakEncryption {
	
	static int solveEquation(String equation){
		//First parse the equation (space delimited)
		String[] statement = equation.split(" ");
		
		String operand = statement[1];
		int val1 = Integer.parseInt(statement[2]);
		int val2 = Integer.parseInt(statement[4]);
		/*
		 * Equations will have the form [var (operand) val1 = val2]
		 */
		
		int returnVal = 0; //Solution to equation
		
		if (operand.equalsIgnoreCase("+")){
			returnVal = val2 - val1;
		} else if (operand.equalsIgnoreCase("-")){
			returnVal = val2 + val1;
		} else if (operand.equalsIgnoreCase("/")){
			returnVal = val2 * val1;
		} else { //Operand must be multiplication, assuming valid input
			returnVal = val2 / val1;
		}
		
		return returnVal;
	}
	
	
	static HashMap<Integer, String> makeMap(char variable, int value){
		HashMap<Integer, String> map = new HashMap<>(26); //26 letters
		
		
		//Get ascii value of variable
		int variableAscii = (int) variable;
		int start = (int) 'a';
		int firstValue = value - (variableAscii - start);
		
		map.put(firstValue-1, " "); //First value will be the space 
		
		for (int i=0; i<26; i++){
			map.put(firstValue+i, Character.toString(((char) (start+i))));
		}
		
		
		return map;
	}
	
	static String decodeMessage(Map<Integer,String> key, String message){
		String[] messageSplit = message.split(" ");
		StringBuilder sb = new StringBuilder();
		for (String s : messageSplit){
			sb.append(key.get(Integer.parseInt(s)));
		}
		
		return sb.toString();
		
	}
	
	
	
	public static void main(String[] args){
		
		Scanner input = new Scanner(System.in);
		
		boolean reading = true;
		ArrayList<String> equations = new ArrayList<>();
		ArrayList<String> messages = new ArrayList<>();
		
		while (reading) {
			String in = input.nextLine();
			if (in.equalsIgnoreCase("end")){
				reading = false;
				break;
			} else {
				equations.add(in);
				messages.add(input.nextLine());
			}
		}
		
		input.close();
		
		int caseNum = 0;
		for (String s : equations){
		int val = solveEquation(s);
		String[] statement = s.split(" ");
		HashMap<Integer,String> map = makeMap(statement[0].charAt(0), val);
		System.out.println("Message " + (caseNum+1) + ": " + decodeMessage(map, messages.get(caseNum)));
		caseNum++;
		}
		
		
		
		
		
		
	
		
		
	
		
	}

}
