package bl.entities;

public class InvoiceLine extends Line {
	
	private int invoiceId;

	public InvoiceLine() {
		super();
	}

	public InvoiceLine(int rowNumber, Item item, int quantity, int invoiceId) {
		super(rowNumber, item, quantity);
		this.invoiceId = invoiceId;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

}
