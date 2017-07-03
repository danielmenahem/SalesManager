package dal;
import java.sql.*;
import java.util.ArrayList;

import bl.entities.Customer;
import bl.entities.Invoice;
import bl.entities.InvoiceLine;
import bl.entities.Item;
import bl.entities.Order;
import bl.entities.OrderLine;
import bl.entities.Status;
import bl.reports.ItemBalance;
import bl.reports.OpenOrders;
import bl.reports.Sales;
import bl.reports.Transaction;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;

public class DBManager {
	
	Connection connection;
	
	public DBManager(){
		connection = getConnection();
	}
	
	public static Connection getConnection() throws RuntimeException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "PROJECT", "PROJECT");
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Error while connecting to database", ex);
		} catch (SQLException ex) {
			throw new RuntimeException("Error while connecting to database", ex);
		}
	}
	
	public void saveCustomer(Customer customer){
		try {
			String sql = "INSERT INTO CUSTOMERS (CUST_ID,NAME)  values (?,?)";
			PreparedStatement query = connection.prepareStatement(sql);
			query.setInt(1, customer.getId());
			query.setString(2, customer.getName());
			query.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isCustomerExist(int id) {
		String sql = "SELECT NAME FROM CUSTOMERS WHERE CUST_ID = ?";
		try {
			PreparedStatement query = connection.prepareStatement(sql);
			query.setInt(1, id);
			ResultSet rs = query.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public String getCustomerName(int id){
		String sql = "SELECT NAME FROM CUSTOMERS WHERE CUST_ID = ?";
		try {
			PreparedStatement query = connection.prepareStatement(sql);
			query.setInt(1, id);
			ResultSet rs = query.executeQuery();
			if(rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Item> getAllItems(){
		String sql = "SELECT * FROM Items";
		ArrayList<Item> items = new ArrayList<>();
		try {
			PreparedStatement query = connection.prepareStatement(sql);
			ResultSet rs = query.executeQuery();
			//System.out.println(rs.next());
			while(rs.next()){
				items.add(new Item(rs.getInt(1),rs.getString(2),rs.getInt(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
		
	}

	public int saveOrder(Order order) {
		int id = 0;
		String sql = "{call OPEN_NEW_ORDER(?,?,?,?,?)}";
        try {
			CallableStatement statement = connection.prepareCall(sql);
			statement.setDate(1, null);
			statement.setInt(2, order.getCustomer().getId());
			statement.registerOutParameter(3, java.sql.Types.INTEGER);
			statement.registerOutParameter(4, java.sql.Types.INTEGER);
			statement.registerOutParameter(5, java.sql.Types.VARCHAR);
			statement.execute();
			id = statement.getInt(4);
			order.setId(id);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

        int count = 1;
        for(OrderLine line : order.getLines()){
        	line.setRowNumber(count);
        	saveOrderLine(id, line, connection);
        	count++;
        }
		return id;
	}

	private void saveOrderLine(int id, OrderLine line, Connection connection) {

		String sql = "{call OPEN_NEW_ORDER_LINE(?,?,?,?,?)}";
        try {
			CallableStatement statement = connection.prepareCall(sql);
			statement.setInt(1,id);
			statement.setInt(2, line.getItem().getId());
			statement.setInt(3, line.getQuantity());
			statement.registerOutParameter(4, java.sql.Types.INTEGER);
			statement.registerOutParameter(5, java.sql.Types.VARCHAR);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isOrderExist(int id) {
		String sql = "SELECT * FROM ORDERS WHERE ORD_NO = ?";
		try {
			PreparedStatement query = connection.prepareStatement(sql);
			query.setInt(1, id);
			ResultSet rs = query.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
			
	}

	public boolean isOrderOpen(int id) {
		String sql = "SELECT STATUS FROM ORDERS WHERE ORD_NO = ?";
		try {
			PreparedStatement query = connection.prepareStatement(sql);
			query.setInt(1, id);
			ResultSet rs = query.executeQuery();
			rs.next();
			if(rs.getInt(1)==0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
			
	}

	public Order getOrder(int id) {
		String sql = "SELECT * FROM ORDERS WHERE ORD_NO = ?";
		Order order = new Order();
		try {
			order.setId(id);
			PreparedStatement query = connection.prepareStatement(sql);
			query.setInt(1, id);
			ResultSet rs = query.executeQuery();
			rs.next();
			order.setOrderDate(rs.getTimestamp(2));
			int customerID = rs.getInt(3);
			order.setStatus(Status.values()[rs.getInt(4)]);
			order.setCustomer(new Customer(customerID, getCustomerName(customerID)));
			
			sql = "Select * from Order_Line WHERE ORD_NO = ?";
			query = connection.prepareStatement(sql);
			query.setInt(1, order.getId());
			rs = query.executeQuery();
			while(rs.next()){
				OrderLine line = new OrderLine();
				line.setOrderId(order.getId());
				line.setRowNumber(rs.getInt(2));
				line.setQuantity(rs.getInt(4));
				Item item = new Item();
				item.setId(rs.getInt(3));
				line.setItem(item);
				order.addOrderLine(line);
			}
			
			for(OrderLine line : order.getLines()){
				sql = "SELECT * FROM Items where ITEM_NO = ?";
				query = connection.prepareStatement(sql);
				query.setInt(1, line.getItem().getId());
				rs = query.executeQuery();
				rs.next();
				line.getItem().setName(rs.getString(2));
				line.getItem().setBalance(rs.getInt(3));
			}
			
			order.setNumLine(order.getLines().size());
			
			} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return order;
	}

	public void editOrder(Order order) {
		int numLines = order.getNumLines();
		System.out.println(order);
		try{
			for(int i = 1; i<=numLines; i++){
				String sql = "{call DELETE_ORDER_LINE(?,?,?,?)}";
				CallableStatement statement = connection.prepareCall(sql);
				statement.setInt(1, order.getId());
				statement.setInt(2, i);
				statement.registerOutParameter(3, java.sql.Types.INTEGER);
				statement.registerOutParameter(4, java.sql.Types.VARCHAR);
				statement.execute();
				System.out.println(i);
			}
	        int count = 1;
	        for(OrderLine line : order.getLines()){
	        	line.setRowNumber(count);
	        	saveOrderLine(order.getId(), line, connection);
	        	count++;
	        }
	        order.setNumLine(order.getLines().size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void deleteOrder(Order order) {
		
		String sql = "{call DELETE_ORDER(?,?,?)}";
		try {
			CallableStatement statement = connection.prepareCall(sql);
			statement.setInt(1, order.getId());
			statement.registerOutParameter(2, java.sql.Types.INTEGER);
			statement.registerOutParameter(3, java.sql.Types.VARCHAR);
			statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public Invoice createInvoice(Order order) throws Exception{
		String sql = "{call OPEN_NEW_INVOICE(?,?,?,?,?)}";
		try { 
                        connection.setAutoCommit(false);
			CallableStatement statement = connection.prepareCall(sql);                        
			statement.setString(1, null);
			statement.setInt(2, order.getId());
			statement.registerOutParameter(3, java.sql.Types.INTEGER);
			statement.registerOutParameter(4, java.sql.Types.INTEGER);
			statement.registerOutParameter(5, java.sql.Types.VARCHAR);
			statement.execute();
			int id = statement.getInt(3);
			System.out.println(id);
			System.out.println(order.getId());
			sql = "{call COPY_ORDER_LINES_TO_INVOICE(?,?,?,?)}";
			statement = connection.prepareCall(sql);
			statement.setInt(1, id);
			statement.setInt(2, order.getId());
			statement.registerOutParameter(3, java.sql.Types.INTEGER);
			statement.registerOutParameter(4, java.sql.Types.VARCHAR);
			statement.execute();
			sql = "{call CONFIRM_INVOICE(?,?,?)}";
			statement = connection.prepareCall(sql);
			statement.setInt(1, id);
			statement.registerOutParameter(2, java.sql.Types.INTEGER);
			statement.registerOutParameter(3, java.sql.Types.VARCHAR);
			statement.execute();
			String message = statement.getString(3);
			if(message!=null)
                        {
                                connection.rollback();
				throw new Exception(message);                                
                        }
			Invoice invoice = getInvoice(id);
			invoice.setOrder(order);
			invoice.setNumLines(invoice.getLines().size());
                        connection.commit();
                        connection.setAutoCommit(true);
			return invoice;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Invoice getInvoice(int id) {
		String sql = "SELECT * FROM INVOICES WHERE INV_NO = ?";
		Invoice invoice = new Invoice();
		int orderID = 0;
		try {
			invoice.setId(id);
			PreparedStatement query = connection.prepareStatement(sql);
			query.setInt(1, id);
			ResultSet rs = query.executeQuery();
			rs.next();
			invoice.setDate(rs.getTimestamp(3));
			orderID = rs.getInt(2);
			
			sql = "Select * from INV_LINE WHERE INV_NO = ?";
			query = connection.prepareStatement(sql);
			query.setInt(1, invoice.getId());
			rs = query.executeQuery();
			
			while(rs.next()){
				InvoiceLine line = new InvoiceLine();
				line.setInvoiceId(invoice.getId());
				line.setRowNumber(rs.getInt(2));
				line.setQuantity(rs.getInt(4));
				Item item = new Item();
				item.setId(rs.getInt(3));
				line.setItem(item);
				invoice.addLine(line);
			}
			
			
			for(InvoiceLine line : invoice.getLines()){
				sql = "SELECT * FROM Items where ITEM_NO = ?";
				query = connection.prepareStatement(sql);
				query.setInt(1, line.getItem().getId());
				rs = query.executeQuery();
				rs.next();
				line.getItem().setName(rs.getString(2));
				line.getItem().setBalance(rs.getInt(3));
			}
						
			} catch (SQLException e) {
			e.printStackTrace();
		}
		invoice.setNumLines(invoice.getLines().size());
		invoice.setOrder(getOrder(orderID));
		return invoice;
	}
	
	public void closeConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isInvoiceExist(int id) {
		String sql = "SELECT * FROM INVOICES WHERE INV_NO = ?";
		try {
			PreparedStatement query = connection.prepareStatement(sql);
			query.setInt(1, id);
			ResultSet rs = query.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public void deleteInvoice(Invoice invoice) {
		String sql = "{call DELETE_INVOICES(?,?,?)}";
		try {
			CallableStatement statement = connection.prepareCall(sql);
			statement.setInt(1, invoice.getId());
			statement.registerOutParameter(2, java.sql.Types.INTEGER);
			statement.registerOutParameter(3, java.sql.Types.VARCHAR);
			statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<ItemBalance> getItemBalance() {
		String sql = "{call GET_INVENTORY_STATUS_REPORT(?)}";
		ArrayList<ItemBalance> items = new ArrayList<>();
		try {
			CallableStatement statement = connection.prepareCall(sql);
			statement.registerOutParameter(1, OracleTypes.CURSOR);
			statement.execute();
			ResultSet rs = ((OracleCallableStatement)statement).getCursor(1);
			while(rs.next()){
				items.add(new ItemBalance(rs.getString(1), rs.getString(2), rs.getInt(3)));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(items.toString());
		return items;
	}

	public ArrayList<OpenOrders> getOpenOrders() {
		String sql = "{call GET_OPEN_ORDERS_VIEW(?)}";
		ArrayList<OpenOrders> items = new ArrayList<>();
		try {
			CallableStatement statement = connection.prepareCall(sql);
			statement.registerOutParameter(1, OracleTypes.CURSOR);
			statement.execute();
			ResultSet rs = ((OracleCallableStatement)statement).getCursor(1);
			while(rs.next()){
				items.add(new OpenOrders(rs.getInt(1), rs.getInt(2), rs.getTimestamp(3), rs.getString(4)));
			}
			System.out.println(items.toString());
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(items.toString());
		return items;
	}

	public ArrayList<Sales> getSalesReport(Timestamp from, Timestamp to) {
		String sql = "{call GET_INVOICES_REPORTS(?,?,?)}";
		ArrayList<Sales> items = new ArrayList<>();
		try {
			CallableStatement statement = connection.prepareCall(sql);
			Date dateFrom = new Date(from.getTime());
			Date dateTo = new Date(to.getTime());

			System.out.println(dateFrom +"   "+ dateTo);
			statement.setDate(1, dateFrom);
			statement.setDate(2, dateTo);
			statement.registerOutParameter(3, OracleTypes.CURSOR);
			statement.execute();
			ResultSet rs = ((OracleCallableStatement)statement).getCursor(3);
			while(rs.next()){
				items.add(new Sales(rs.getString(1), rs.getInt(2)));
			}
			System.out.println(items.toString());
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(items.toString());
		return items;
	}

	public ArrayList<Transaction> getTransactionReport(Timestamp from, Timestamp to) {
		String sql = "{call GET_ALL_TRANSACTION_LOG(?,?,?)}";
		ArrayList<Transaction> items = new ArrayList<>();
		try {
			CallableStatement statement = connection.prepareCall(sql);
			Date dateFrom = new Date(from.getTime());
			Date dateTo = new Date(to.getTime());

			statement.setDate(1, dateFrom);
			statement.setDate(2, dateTo);
			statement.registerOutParameter(3, OracleTypes.CURSOR);
			statement.execute();
			ResultSet rs = ((OracleCallableStatement)statement).getCursor(3);
			while(rs.next()){
				items.add(new Transaction(rs.getString(1),rs.getString(3),rs.getInt(4), rs.getString(2).charAt(0)));
			}
			rs.close();
			System.out.println(items.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(items.toString());
		return items;
	}

	public void editInvoice(Invoice invoice) {
		int numLines = invoice.getNumLines();
		String sql;
		CallableStatement statement;
		
		try{
			System.out.println(numLines);
			for(int i = 1; i<=numLines; i++){
				sql = "{call DELETE_INVOICES_LINE(?,?,?,?)}";
				statement = connection.prepareCall(sql);
				statement.setInt(1, invoice.getId());
				statement.setInt(2, i);
				statement.registerOutParameter(3, java.sql.Types.INTEGER);
				statement.registerOutParameter(4, java.sql.Types.VARCHAR);
				statement.execute();
				System.out.println("AAAAA");
			}
	        int count = 1;
	        for(InvoiceLine line : invoice.getLines()){
	        	line.setRowNumber(count);
	        	sql = "{call OPEN_NEW_INVOICE_LINE(?,?,?,?,?)}";
	        	statement = connection.prepareCall(sql);
				statement.setInt(1, invoice.getId());
				statement.setInt(2, line.getItem().getId());
				statement.setInt(3, line.getQuantity());
				statement.registerOutParameter(4, java.sql.Types.INTEGER);
				statement.registerOutParameter(5, java.sql.Types.VARCHAR);
				statement.execute();
	        	count++;
	        }
	        invoice.setNumLines(invoice.getLines().size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
