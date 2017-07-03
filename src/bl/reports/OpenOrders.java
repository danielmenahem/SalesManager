package bl.reports;

import java.sql.Timestamp;

public class OpenOrders {
	
	private int orderID;
	private int customerID;
	private Timestamp orderDate;
	private String name;
	
	public OpenOrders(){
		
	}
	
	
	public OpenOrders(int orderID, int customerID, Timestamp orderDate, String name) {
		this.orderID = orderID;
		this.customerID = customerID;
		this.orderDate = orderDate;
		this.name = name;
	}
	
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public Timestamp getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	@Override
	public String toString() {
		return "OpenOrders [orderID=" + orderID + ", customerID=" + customerID + ", orderDate=" + orderDate + ", name="
				+ name + "]";
	}
}
