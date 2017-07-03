package bl.entities;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Order {

	private int id;
	private Timestamp orderDate;
	private Customer customer;
	private Status status;
	private ArrayList<OrderLine> lines = new ArrayList<>();
	private int numLine = 0;
	

	
	public Order(){
		this.lines = new ArrayList<>();
	}
	
	public Order(int id, Timestamp orderDate, Customer customer) {
		this.id = id;
		this.orderDate = orderDate;
		this.customer = customer;
		this.status = Status.OPEN;
		this.lines = new ArrayList<>();
	}
	
	public void removeAllLines(){
		lines = new ArrayList<>();
	}


	public int getId() {
		return id;
	}
	
	public int getNumLines() {
		return numLine;
	}

	public void setNumLine(int numLine) {
		this.numLine = numLine;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}
	
	public void addOrderLine(OrderLine line){
		lines.add(line);
	}


	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}


	public ArrayList<OrderLine> getLines() {
		return lines;
	}


	public void setLines(ArrayList<OrderLine> lines) {
		this.lines = lines;
	}


	public void setId(int id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		String s =  toStringThin();
		for(OrderLine line : lines){
			s += "\n" + line.toString();
		}
		return s;
	}
	
	public String toStringThin(){
		return  "Order ID: " + id + "\nOrder Date: " +orderDate.toString().substring(0, 10) + "\nCustomer: " + customer + "\nOrder Status: " + status +"\n";
	}
	
	
	
	
	
	
}
