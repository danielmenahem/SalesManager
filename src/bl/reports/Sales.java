package bl.reports;

public class Sales {
	
	private String itemName;
	private int quantity;
	
	public Sales(){
		
	}
	
		
	public Sales(String itemName, int quntity) {
		this.itemName = itemName;
		this.quantity = quntity;
	}




	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}




	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quntity) {
		this.quantity = quntity;
	}


	@Override
	public String toString() {
		return "Sales [itemName=" + itemName + ", quantity=" + quantity + "]";
	}
	
	

	
	
}
