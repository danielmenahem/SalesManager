package bl.entities;

public abstract class Line {
	private int rowNumber;
	private Item item;
	private int quantity;
	
	public Line(){
		
	}
	
	public Line(int rowNumber, Item item, int quantity) {
		super();
		this.rowNumber = rowNumber;
		this.item = item;
		this.quantity = quantity;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return rowNumber + ". item: " + item.getName() + ", quantity: " + quantity;
	}
}

