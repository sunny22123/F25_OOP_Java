//hsiangyl, leehsiangyu
package exam2;

import java.util.Scanner;

public class Messenger {

	Message[] messages = {new Caesar(), new Keyword()}; //these objects must be used for encryption / decryption. No new instances of Caesar or Keywold should be created.
	Scanner input = new Scanner(System.in); //to take user inputs

	public static void main(String[] args) {
		Messenger messenger = new Messenger();
		messenger.runMessenger();
	}

	/** runMessenger() prints the menu options, takes user input,
	 * invokes methods from appropriate classes to encrypt or decrypt
	 * messages, and then prints the output
	 */
	void runMessenger() {
		System.out.println("Choose ...");
		System.out.println("1. Caesar encryption");
		System.out.println("2. Keyword encryption");
		System.out.println("3. Caesar decryption");
		System.out.println("4. Keyword decryption");
		int choice = input.nextInt();
		input.nextLine(); //to clear buffer

		System.out.print("Enter message: ");
		String message = input.nextLine();

		System.out.print("Enter key: ");
		String key = input.nextLine();

		String result = "";


		switch (choice) {
			case 1:
				messages[0].setMessage(message, key);
				result = messages[0].encrypt();
				break;
			case 2:
				messages[1].setMessage(message, key);
				result = messages[1].encrypt();
				break;
			case 3:
				messages[0].setMessage(message, key);
				result = messages[0].decrypt();
				break;
			case 4:
				messages[1].setMessage(message, key);
				result = messages[1].decrypt();
				break;
			default:
				System.out.println("Invalid choice");
				return;
		}
		System.out.println("Result: " + result);
	}
}