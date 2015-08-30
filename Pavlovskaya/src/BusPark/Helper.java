package BusPark;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Helper {
	int i = 0;
	String BusNumber;
	boolean Continue = true;
	Scanner sc = new Scanner(System.in);

	
	public void readFromConsole() {
		
		
		System.out
				.print("Make a choise: \n 1 - The bus leaves the park \n 2 - The bus enter the park \n 3 - Print the list of buses in the park \n 4 - Print the list of buses on the route \n");
		if (sc.hasNextInt()) {
			i = sc.nextInt();
			if (i == 1) {
				System.out.println("Enter the bus number:");
				if (sc.hasNext()) {
					BusNumber = sc.next();
				} else {
					System.out.println("You entered not integer number");
				}
			} else if (i == 2) {
				System.out.println("Enter the bus number:");
				if (sc.hasNext()) {
					BusNumber = sc.next();
				} else {
					System.out.println("You entered not integer number");
				}
			} else if (i == 3) 
			{
				System.out.println("The list of buses:");	
			}
			else if (i == 4)
			{
				System.out.println("The list of buses:");	
			}
			else
			{
				System.out.println("You did not make a choise");
			}
		} else {
			System.out.println("You entered not integer number");
		}

	}
		
		public void Continue() {
			System.out.println("Do you want to continue? \n 1 - Yes \n Other - No");			
			
			
			try {
				if (sc.hasNext()) {
				
					if (sc.nextInt()==1) {
						System.out.println("Program is working");
					}
				 else {
					System.out.println("The work of program is finished");
					Continue = false;			
				 }
				 }
			} catch (InputMismatchException  e) {
				// TODO Auto-generated catch block
				
				Continue = false;			

			}
			
			}
}
