package bl.controlers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import bl.entities.Customer;
import bl.entities.Invoice;
import bl.entities.Item;
import bl.entities.Order;
import bl.entities.Status;
import dal.DBManager;

public class AppManager {
	
	private static AppManager manager;
	private DBManager dbManager;
	
	private AppManager(){
		this.dbManager = new DBManager();
	}
	
	public static AppManager getInstance(){
		if(manager==null)
			manager = new AppManager();
		return manager;
	}

	public void insertNewCustomer(int id, String name) {
		dbManager.saveCustomer(new Customer(id, name));
	}

	public boolean isCustomerExist(int id) {
		return dbManager.isCustomerExist(id);
	}

	public Customer getCustomer(int id) {
		return new Customer(id, dbManager.getCustomerName(id));
	}

	public ArrayList<Item> getItems() {
		return dbManager.getAllItems();
	}

	public int saveOrder(Order order) {
		order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
		order.setStatus(Status.OPEN);
		return dbManager.saveOrder(order);
	}

	public boolean isOrderExist(int id) {
		return dbManager.isOrderExist(id);
	}

	public boolean isOrderOpen(int id) {
		return dbManager.isOrderOpen(id);
	}

	public Order getOrder(int id) {
		return dbManager.getOrder(id);
	}

	public void editOrder(Order order) {
		dbManager.editOrder(order);
	}

	public void deleteOrder(Order order) {
		dbManager.deleteOrder(order);
	}

	public Invoice createInvoice(Order order) throws Exception {
		order.setStatus(Status.CLOSED);
		return dbManager.createInvoice(order);
	}

	public Invoice getInvoice(int id) {
		return dbManager.getInvoice(id);
	}

	public void editInvoice(Invoice invoice) {
		dbManager.editInvoice(invoice);
	}

	public boolean isInvoiceExist(int id) {
		return dbManager.isInvoiceExist(id);
	}

	public void deleteInvoice(Invoice invoice) {
		dbManager.deleteInvoice(invoice);
	}

	public ArrayList<bl.reports.ItemBalance> getItemBalanceReport() {
		return dbManager.getItemBalance();
	}

	public ArrayList<bl.reports.Transaction> getTransactionReport(Timestamp from, Timestamp to) {
		return dbManager.getTransactionReport(from, to);
	}

	public ArrayList<bl.reports.Sales> getSalesReport(Timestamp from, Timestamp to) {
		return dbManager.getSalesReport(from, to);
	}

	public ArrayList<bl.reports.OpenOrders> getOpenOrders() {
		return dbManager.getOpenOrders();
	}

	public void closeConnection() {
		dbManager.closeConnection();
	}
}
