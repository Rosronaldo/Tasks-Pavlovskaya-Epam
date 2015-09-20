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
			// Принимаем соединение от нового клиента
			sock = server.accept();
			// Получаем потоки ввода-вывода
			in = new ObjectInputStream(sock.getInputStream());
			out = new ObjectOutputStream(sock.getOutputStream());
			// Пока соединение активно, обрабатываем запросы
			while (processQuery());
			server.close();
		}
	}
	
	private boolean processQuery()
	{
		try
		{
			// Получаю запрос от клиента
			Product product = null;
			try {
			product = (Product) in.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Операция
			

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
				System.out.println("Не такого действия!!!");	
				break;
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 			
			
			// Отправляю результат клиенту
			//			out.writeObject(product);
			
			return true;
		}
		catch (IOException e){return false;}
	}

	
	// Вспомогательный метод получения соединения
	private Connection getConnection() throws Exception {
	// Подгрузка драйвера БД Derby
//	Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
	
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String url = "jdbc:mysql://localhost:3306/MYDB";
		
	// Получение соединения с БД
	return DriverManager.getConnection(url, "root", "rosronaldo");
			
//			DriverManager.getConnection("jdbc:derby://localhost:1527/myDB;create=true;user=me;password=mine");
	}
	/**
	* Возвращает список идентификаторов товаров
	*
	* @return
	*/
	public List<Integer> getProductIds() throws Exception {
	List<Integer> productIds = new ArrayList<Integer>();
	// Получение соединения с БД
	Connection con = getConnection();
	// Выполнение SQL-запроса
	ResultSet rs = con.createStatement().executeQuery(
	"Select id From products");
	// Перечисляем результаты выборки
	while (rs.next()) {
	// Из каждой строки выборки выбираем
	// результат и помещаем в коллекцию
	productIds.add(rs.getInt(1));
	}
	// Закрываем выборку и соединение с БД
	rs.close();
	con.close();
	return productIds;
	}
	/**
	* Возвращает товар по идентификатору
	*
	* @return
	*/
	public List<Product> getProductById(int id) throws Exception {
	List<Product> products = new ArrayList<Product>();
	// Получение соединения с БД
	Connection con = getConnection();
	// Подготовка SQL-запроса
	PreparedStatement st = con.prepareStatement(
	"Select description, rate, quantity " +
	"From products " +
	"Where id = ?");
	// Указание значений параметров запроса
	st.setInt(1, id);
	// Выполнение запроса
	ResultSet rs = st.executeQuery();
	Product product = null;
	// Перечисляем результаты выборки
	while (rs.next()) {
	// Из каждой строки выборки выбираем результаты,
	// формируем новый объект Product
	// и помещаем его в коллекцию
	product = new Product(id, rs.getString(1), rs.getFloat(2), rs.getInt(3), 0); 
	products.add(product);
	}
	// Закрываем выборку и соединение с БД
	rs.close();
	con.close();
	return products;
	}
	/**
	* Добавляет новый товар
	* @param product
	* @throws Exception
	*/
	public void addProduct(Product product) throws Exception {
	// Получение соединения с БД
	Connection con = getConnection();
	// Подготовка SQL-запроса
	PreparedStatement st = con.prepareStatement(
	"Insert into products " +
	"(id, description, rate, quantity) " +
	"values (?, ?, ?, ?)");
	// Указание значений параметров запроса
	st.setInt(1, product.getId());
	st.setString(2, product.getDescription());
	st.setFloat(3, product.getRate());
	st.setInt(4, product.getQuantity());
	// Выполнение запроса
	st.executeUpdate();
	con.close();
	}
	/**
	* Обновляет данные о товаре
	* @param product
	* @throws Exception
	*/
	public void setProduct(Product product) throws Exception {
	// Получение соединения с БД
	Connection con = getConnection();
	// Подготовка SQL-запроса
	PreparedStatement st = con.prepareStatement(
	"Update products " +
	"Set description=?, rate=?, quantity=? " +
	"Where id=?");
	// Указание значений параметров запроса
	st.setString(1, product.getDescription());
	st.setFloat(2, product.getRate());
	st.setInt(3, product.getQuantity());
	st.setInt(4, product.getId());
	// Выполнение запроса
	st.executeUpdate();
	con.close();
	}
	public void removeProduct(int id) throws Exception {
	// Получение соединения с БД
	Connection con = getConnection();
	// Подготовка SQL-запроса
	PreparedStatement st = con.prepareStatement(
	"Delete from products " +
	"Where id = ?");
	// Указание значений параметров запроса
	st.setInt(1, id);
	// Выполнение запроса
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
		{System.out.println("Возникла ошибка");}
	}
}