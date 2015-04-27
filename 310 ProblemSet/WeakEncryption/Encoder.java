import java.util.HashMap;
import java.util.Scanner;


public class Encoder {

	public static void main(String[] args) {
		System.out.print("Enter value for 'a' :");
		Scanner input = new Scanner(System.in);
		int aValue = input.nextInt();
		HashMap<String, Integer> code = new HashMap<>();
		
		int asciiA = (int) 'a';
		
		code.put(" ", aValue-1);
		
		for(int i=0; i<26; i++){
			code.put(Character.toString((char) (asciiA + i) ), aValue + i);
		}
		
		System.out.println("Enter your message to encode: ");
		String message;
		while(true){
			message = input.nextLine();
			if (!message.isEmpty()) break;
		}
		
		String[] messageIterator = message.split("");
		
		StringBuilder sb = new StringBuilder();
		
		for(String s: messageIterator){
			sb.append(code.get(s));
			sb.append(" ");
		}

		System.out.println(sb.toString());
	}

}
