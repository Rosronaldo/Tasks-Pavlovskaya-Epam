package Stock;

import java.awt.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ProductClient {


	private Socket sock = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	 // конструктор
	 public ProductClient(String ip, int port) throws IOException
	 {
		// Устанавливаем соединение
		// Получаем потоки ввода-вывода
				
				sock = new Socket(ip, port);
				in = new ObjectInputStream(sock.getInputStream());
				out = new ObjectOutputStream(sock.getOutputStream());
	 }

	public void disconnect() throws IOException {
		sock.close();
	}

	private void sendQuery(Product product)	throws IOException {
		out.writeObject(product);
	}
	
	public List refreshIdList() throws ClassNotFoundException {
		Product product = new Product(0, null, 0, 0, 4);
		try {
			sendQuery (product);
			List productIds = (List) in.readObject();
			return productIds;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		}
	
	public List showProductData(Product product1) throws IOException, ClassNotFoundException {
		sendQuery (product1);				
		List product = (List) in.readObject();
		return product;
	}
	
	public void addProduct (Product product) throws IOException{
		sendQuery (product);
		}
	
	public void updateProduct(Product product) throws IOException {
	sendQuery (product);	
	}

	public void removeProduct(Product product) throws IOException {
	sendQuery (product);	
	} 

}
