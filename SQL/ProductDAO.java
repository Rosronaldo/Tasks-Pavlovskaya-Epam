package Stock;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ProductDAO {

	private ServerSocket server = null;
	private Socket sock = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	public void start(int port) throws IOException
	{
		server = new ServerSocket(port);
		while (true)
		{
			// ��������� ���������� �� ������ �������
			sock = server.accept();
			// �������� ������ �����-������
			in = new ObjectInputStream(sock.getInputStream());
			out = new ObjectOutputStream(sock.getOutputStream());
			// ���� ���������� �������, ������������ �������
			while (processQuery());
			server.close();
		}
	}
	
	private boolean processQuery()
	{
		try
		{
			// ������� ������ �� �������
			Product product = null;
			try {
			product = (Product) in.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // ��������
			

			try {

				switch (product.NumOper) {
				case 1:
				addProduct(product);
				break;
				case 2:
				setProduct(product);
				break;
				case 3:
				removeProduct(product.getId());
				break;
				case 4:
				getProductIds();
				break;
				case 5:
				getProductById(product.getId());
				break;
				default:
				System.out.println("�� ������ ��������!!!");	
				break;
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 			
			
			// ��������� ��������� �������
			//			out.writeObject(product);
			
			return true;
		}
		catch (IOException e){return false;}
	}

	
	// ��������������� ����� ��������� ����������
	private Connection getConnection() throws Exception {
	// ��������� �������� �� Derby
//	Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
	
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String url = "jdbc:mysql://localhost:3306/MYDB";
		
	// ��������� ���������� � ��
	return DriverManager.getConnection(url, "root", "rosronaldo");
			
//			DriverManager.getConnection("jdbc:derby://localhost:1527/myDB;create=true;user=me;password=mine");
	}
	/**
	* ���������� ������ ��������������� �������
	*
	* @return
	*/
	public List<Integer> getProductIds() throws Exception {
	List<Integer> productIds = new ArrayList<Integer>();
	// ��������� ���������� � ��
	Connection con = getConnection();
	// ���������� SQL-�������
	ResultSet rs = con.createStatement().executeQuery(
	"Select id From products");
	// ����������� ���������� �������
	while (rs.next()) {
	// �� ������ ������ ������� ��������
	// ��������� � �������� � ���������
	productIds.add(rs.getInt(1));
	}
	// ��������� ������� � ���������� � ��
	rs.close();
	con.close();
	return productIds;
	}
	/**
	* ���������� ����� �� ��������������
	*
	* @return
	*/
	public List<Product> getProductById(int id) throws Exception {
	List<Product> products = new ArrayList<Product>();
	// ��������� ���������� � ��
	Connection con = getConnection();
	// ���������� SQL-�������
	PreparedStatement st = con.prepareStatement(
	"Select description, rate, quantity " +
	"From products " +
	"Where id = ?");
	// �������� �������� ���������� �������
	st.setInt(1, id);
	// ���������� �������
	ResultSet rs = st.executeQuery();
	Product product = null;
	// ����������� ���������� �������
	while (rs.next()) {
	// �� ������ ������ ������� �������� ����������,
	// ��������� ����� ������ Product
	// � �������� ��� � ���������
	product = new Product(id, rs.getString(1), rs.getFloat(2), rs.getInt(3), 0); 
	products.add(product);
	}
	// ��������� ������� � ���������� � ��
	rs.close();
	con.close();
	return products;
	}
	/**
	* ��������� ����� �����
	* @param product
	* @throws Exception
	*/
	public void addProduct(Product product) throws Exception {
	// ��������� ���������� � ��
	Connection con = getConnection();
	// ���������� SQL-�������
	PreparedStatement st = con.prepareStatement(
	"Insert into products " +
	"(id, description, rate, quantity) " +
	"values (?, ?, ?, ?)");
	// �������� �������� ���������� �������
	st.setInt(1, product.getId());
	st.setString(2, product.getDescription());
	st.setFloat(3, product.getRate());
	st.setInt(4, product.getQuantity());
	// ���������� �������
	st.executeUpdate();
	con.close();
	}
	/**
	* ��������� ������ � ������
	* @param product
	* @throws Exception
	*/
	public void setProduct(Product product) throws Exception {
	// ��������� ���������� � ��
	Connection con = getConnection();
	// ���������� SQL-�������
	PreparedStatement st = con.prepareStatement(
	"Update products " +
	"Set description=?, rate=?, quantity=? " +
	"Where id=?");
	// �������� �������� ���������� �������
	st.setString(1, product.getDescription());
	st.setFloat(2, product.getRate());
	st.setInt(3, product.getQuantity());
	st.setInt(4, product.getId());
	// ���������� �������
	st.executeUpdate();
	con.close();
	}
	public void removeProduct(int id) throws Exception {
	// ��������� ���������� � ��
	Connection con = getConnection();
	// ���������� SQL-�������
	PreparedStatement st = con.prepareStatement(
	"Delete from products " +
	"Where id = ?");
	// �������� �������� ���������� �������
	st.setInt(1, id);
	// ���������� �������
	st.executeUpdate();
	con.close();
	}
	
	public static void main(String[] args)
	{
		try
		{
			ProductDAO srv = new ProductDAO();
			srv.start(12346);
		}
		catch (IOException e)
		{System.out.println("�������� ������");}
	}
}