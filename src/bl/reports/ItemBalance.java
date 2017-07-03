package bl.reports;

public class ItemBalance {
	
	private String itemName;
	private String wareHouseName;
	private int quantity;
	
	public ItemBalance(){
		
	}
	
	
	public ItemBalance(String itemName, String wareHouseName, int quantity) {
		this.itemName = itemName;
		this.wareHouseName = wareHouseName;
		this.quantity = quantity;
	}


	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getWareHouseName() {
		return wareHouseName;
	}
	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	@Override
	public String toString() {
		return "ItemBalance [itemName=" + itemName + ", wareHouseName=" + wareHouseName + ", quantity=" + quantity
				+ "]";
	}
	
	
	
	

}
