package bl.entities;

public class OrderLine extends Line {
	
	private int orderId;

	public OrderLine() {
		super();
	}

	public OrderLine(int rowNumber, Item item, int quantity, int orderId) {
		super(rowNumber, item, quantity);
		this.orderId = orderId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	@Override
	public String toString(){
		return super.toString();
	}

}
