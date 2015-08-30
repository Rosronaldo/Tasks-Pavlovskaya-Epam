package BusPark;

import java.util.List;

public class Bus {

String BusNumber;
String driver;
int RoutingId;

public Bus(String busNumber, String driver, int RoutingId) {
	super();
	BusNumber = busNumber;
	this.driver = driver;
	this.RoutingId = RoutingId;
}

public String toString() {
	return "Bus [BusNumber=" + BusNumber + ", driver=" + driver
			+ ", RoutingId=" + RoutingId + "]";
}


//public void busesInThePark (List<Bus> BusesInThePark) {
//
//System.out.println(toString());	
//}

}

