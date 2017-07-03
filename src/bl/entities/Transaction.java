package bl.entities;

import java.time.LocalDateTime;

public class Transaction {
	
	private int id;
	private LocalDateTime date;
	private Item item;
	private int quantity;
	private Warehouse warehouse;
	private TransactionType type;
	
	public Transaction(){
		
	}
	
	public Transaction(int id, LocalDateTime date, Item item, int quantity, Warehouse warehouse, TransactionType type) {
		super();
		this.id = id;
		this.date = date;
		this.item = item;
		this.quantity = quantity;
		this.warehouse = warehouse;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
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

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}
	
	
	
	

}
