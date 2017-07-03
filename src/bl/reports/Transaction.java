package bl.reports;

import bl.entities.TransactionType;

public class Transaction {
	
	private String itemName;
	private String warehouseName;
	private int quantity;
	private TransactionType type;
	
	
	public Transaction(){
		
	}
	
	public Transaction(String itemName, String warehouseName, int quantity, char c) {
		this.itemName = itemName;
		this.warehouseName = warehouseName;
		this.quantity = quantity;
		setType(c);
		
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(char c) {
		
		if(c=='D')
			type = TransactionType.OUT;
		else if(c == 'C')
			type = TransactionType.IN;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}
	
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	


}
