package bl.entities;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Invoice {
	
	private int numLines = 0;
	private int id;
	private Order order;
	private Timestamp date;
	private ArrayList<InvoiceLine> lines = new ArrayList<>();
	
	
	public Invoice(){
		
	}
	
	public int getNumLines() {
		return numLines;
	}

	public void setNumLines(int numLines) {
		this.numLines = numLines;
	}

	public Invoice(int id, Order order, Timestamp date) {
		super();
		this.id = id;
		this.order = order;
		this.date = date;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Timestamp getDate() {
		return date;
	}
	
	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	public void addLine(InvoiceLine line){
		this.lines.add(line);
	}


	public ArrayList<InvoiceLine> getLines() {
		return lines;
	}
	
	public void clearAllLines(){
		this.lines = new ArrayList<>();
	}
	


	@Override
	public String toString() {
		String s =  "Inoice ID:" +id +"\nInvoice Date:" + date + "\nOrder details:\n" + order.toStringThin();
		for(InvoiceLine line : lines){
			s += "\n" + line.toString();
		}
		return s;
	}

	public void setLines(ArrayList<InvoiceLine> lines) {
		this.lines = lines;
	}
	
	
}
