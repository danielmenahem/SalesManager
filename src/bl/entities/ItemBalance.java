package bl.entities;

public class ItemBalance {
	
	private Item item;
	private int Quantity;
	
	public ItemBalance(){
		
	}
	
	public ItemBalance(Item item, int quantity) {
		super();
		this.item = item;
		Quantity = quantity;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public int getQuantity() {
		return Quantity;
	}
	
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	
	

}
