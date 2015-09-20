package Stock;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProductInfoClient /*extends JDialog*/ {

	
	ProductClient client;
	
	
	private static final long serialVersionUID = 1L;
//	ProductDAO productDAO = new ProductDAO();

//	private final JPanel contentPanel = new JPanel();
	
	private JFrame frame;
	private JLabel lbSelectId = new JLabel("Выбор товара по Id");
	private JLabel lbId = new JLabel("Id");
	private JLabel lbDescription = new JLabel("Описание");
	private JLabel lbRate = new JLabel("Цена");
	private JLabel lbQuantity = new JLabel("Остаток");
	private JComboBox comboId = new JComboBox();
	private JTextField txtId = new JTextField();
	private JTextField txtDescription = new JTextField();
	private JTextField txtRate = new JTextField();
	private JTextField txtQuantity = new JTextField();
	private JButton btnAdd = new JButton("Добавить");
	private JButton btnUpdate = new JButton("Обновить");
	private JButton btnRemove = new JButton("Удалить");
	private JButton btnClear = new JButton("Очистить");
	Product res = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProductInfoClient window = new ProductInfoClient();
					
					window.frame.setVisible(true);
//					this.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
//		
//		try {
//			ProductInfoClient productInfoClient = new ProductInfoClient("localhost", 12346);
//			} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	
	
	
	/**
	 * Create the dialog.
	 */
	public ProductInfoClient() throws IOException {
			client = new ProductClient("localhost", 12346);
				
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
				try {
				client.disconnect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				}
			});	
			
			
		frame.setTitle("Информация о товарах");
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(8, 2));
		frame.setBounds(100, 50, 400, 200);
		frame.setVisible(true);
		
		frame.getContentPane().add(lbSelectId);
		frame.getContentPane().add(comboId);
		frame.getContentPane().add(lbId);
		frame.getContentPane().add(txtId);
		frame.getContentPane().add(lbDescription);
		frame.getContentPane().add(txtDescription);
		frame.getContentPane().add(lbRate);
		frame.getContentPane().add(txtRate);
		frame.getContentPane().add(lbQuantity);
		frame.getContentPane().add(txtQuantity);
		frame.getContentPane().add(btnAdd);
		frame.getContentPane().add(btnUpdate);
		frame.getContentPane().add(btnRemove);
		frame.getContentPane().add(btnClear);
		
		comboId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			showProductData();
			}
			});
			btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			addProduct();
			}
			});
			btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			updateProduct();
			}
			});
			btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			removeProduct();
			}
			});
			btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			clearProductInfo();
			}
			});
		
			// Обновляем список идентификаторов товаров
			refreshIdList();
			// Отображаем диалог на экране
			frame.setVisible(true);
	
	}
	
	private void refreshIdList() {
		try {
		// получаем список идентификаторов
		List<Integer> productIds = (List<Integer>) client.refreshIdList(); 
				
		//List<Integer> productIds = productDAO.getProductIds();
		// очищаем список
		comboId.removeAllItems();
		// заполняем список полученными данными
		for (Integer productId: productIds) {
		comboId.addItem(productId);
		}
		} catch (Exception e) {
		e.printStackTrace();
//		JOptionPane.showMessageDialog(this, e.getMessage());
		}
		}
	
	/**
	* Отображает данные о товаре по
	* выбранному в списке идентификатору
	*/
	protected void showProductData() {
	try {
	// забираем значение, выбранное в списке
	// идентификаторов товаров
	Integer productId = (Integer)comboId.getSelectedItem();
	if (productId != null) {
	// получаем товар по идентификатору
	Product product1 = new Product(productId, txtDescription.getText(),Float.parseFloat(txtRate.getText()), Integer.parseInt(txtQuantity.getText()), 5);
	List<Product> product = (List<Product>) client.showProductData(product1);
	
	//	List<Product> product = productDAO.getProductById(productId);
	// заполняем текстовые поля значениями
	// параметров товара
	for(int i=0; i<product.size(); i++){
	txtId.setText(String.valueOf(product.get(i).getId()));
	txtDescription.setText(product.get(i).getDescription());
	txtRate.setText(String.valueOf(product.get(i).getRate()));
	txtQuantity.setText(String.valueOf(product.get(i).getQuantity()));
	}
	}
	} catch (Exception e) {
	e.printStackTrace();
//	JOptionPane.showMessageDialog(this, e.getMessage());
	}
	}
	/**
	* Добавляет новый товар на основе
	* данных текстовых полей
	*/
	protected void addProduct() {
	try {
	// создаем новый объект-товар
	// на основе данных диалога
	Product product = new Product(
	Integer.parseInt(txtId.getText()),
	txtDescription.getText(),
	Float.parseFloat(txtRate.getText()),
	Integer.parseInt(txtQuantity.getText()),
	1);
	// сохраняем товар в БД
	
	client.addProduct(product);
	
	
//	productDAO.addProduct(product);
	// обновляем список идентификаторов
	refreshIdList();
	// устанавливаем текущим добавленный товар
	comboId.setSelectedItem(
	Integer.parseInt(txtId.getText()));
	} catch (Exception e) {
	e.printStackTrace();
//	JOptionPane.showMessageDialog(this, e.getMessage());
	}
	}
	/**
	* Обновляет информацию о товаре на основе
	* данных текстовых полей
	*/
	protected void updateProduct() {
	try {
	// формируем объект-товар
	// на основе данных диалога
	Product product = new Product(Integer.parseInt(txtId.getText()),
	txtDescription.getText(), Float.parseFloat(txtRate.getText()),
	Integer.parseInt(txtQuantity.getText()), 2);
	// обновляем данные о товаре в БД
	
	client.updateProduct(product);
		
//	productDAO.setProduct(product);
	} catch (Exception e) {
	e.printStackTrace();
//	JOptionPane.showMessageDialog(this, e.getMessage());
	}
	}
	/**
	* Удаляет выбранный товар
	*/
	protected void removeProduct() {
	try {
	// удаляем товар из БД
	Product product = new Product(Integer.parseInt(txtId.getText()),
	txtDescription.getText(), Float.parseFloat(txtRate.getText()),
	Integer.parseInt(txtQuantity.getText()), 3);
	
	client.removeProduct(product);
			
//	productDAO.removeProduct(
//	Integer.parseInt(txtId.getText()));
	// обновляем список идентификаторов товаров
	refreshIdList();
	// отображаем данные по первому товару в списке
	showProductData();
	} catch (Exception e) {
	e.printStackTrace();
//	JOptionPane.showMessageDialog(this, e.getMessage());
	}
	}
	/**
	* Очищает данные в текстовых полях
	*/
	protected void clearProductInfo() {
	try {
	txtId.setText("");
	txtDescription.setText("");
	txtRate.setText("");
	txtQuantity.setText("");
	} catch (Exception e) {
	e.printStackTrace();
//	JOptionPane.showMessageDialog(this, e.getMessage());
	}
	}
}
	
	

