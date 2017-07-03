package bl.entities;

public class Item {
	private int id;
	private String name;
	private int balance;
	
	
	public Item(){
		
	}
	
	public Item(int id, String name, int balance) {
		super();
		this.id = id;
		this.name = name;
		this.balance = balance;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return name +" ("+id+")";
	}
	
	
	
	
	
	
	
}
