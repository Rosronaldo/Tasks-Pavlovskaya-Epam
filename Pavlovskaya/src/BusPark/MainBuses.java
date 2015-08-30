package BusPark;

import java.io.Console;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;

public class MainBuses {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<Bus> BusesInThePark = new ArrayList<Bus>();

		BusesInThePark.add(new Bus("BX5965AI", "Petrenko P.P.", 5));
		BusesInThePark.add(new Bus("BX9233AI", "Sydorenko I.I.", 3));
		BusesInThePark.add(new Bus("BX2526AI", "Shevchenko O.O.", 15));

		List<Bus> BusesOnTheRoute = new ArrayList<Bus>();

		Helper helper = new Helper();
		while (helper.Continue) {
		
			helper.readFromConsole();

		if (helper.i == 1) {

			for (Iterator iterator = BusesInThePark.iterator(); iterator
					.hasNext();) {
				Bus bus = (Bus) iterator.next();
				if (bus.BusNumber.equals(helper.BusNumber)) {
					BusesOnTheRoute.add(bus);
					BusesInThePark.remove(bus);
					helper.i = 0;
					break;
				} 
//				else {
//					System.out
//							.println("Bus with such number is absent in the park");
//				}
//				
			}

		} else if (helper.i == 2) {
		
			for (Iterator iterator = BusesOnTheRoute.iterator(); iterator
					.hasNext();) {
				Bus bus = (Bus) iterator.next();
				if (bus.BusNumber.equals(helper.BusNumber)) {
					BusesInThePark.add(bus);
					BusesOnTheRoute.remove(bus);
					helper.i = 0;
					break;
				} 
				}
		}

		else if (helper.i == 3) {
		
		if 	(BusesInThePark.isEmpty()){
		System.out.println("There is no buses in the list \n");	
		} else {
			for (Iterator iterator = BusesInThePark.iterator();
					 iterator.hasNext();) {
					 Bus bus = (Bus) iterator.next();
					 System.out.println(bus.toString());}
			System.out.println();	
		}
		}
		
		else if (helper.i == 4) {
			if 	(BusesOnTheRoute.isEmpty()) {
				System.out.println("There is no buses in the list \n");	
			} else {
			 for (Iterator iterator = BusesOnTheRoute.iterator();
					 iterator.hasNext();) {
					 Bus bus = (Bus) iterator.next();
					 System.out.println(bus.toString());}
			 System.out.println();
	
		}
		}
		helper.Continue();
		}

	}	
}