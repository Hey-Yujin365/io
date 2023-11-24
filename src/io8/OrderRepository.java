package io8;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

	private List<Order> db = new ArrayList<Order>();
	
	public OrderRepository() {
		load();
	}
	
	private void load () {
		db = getOrders(getLottoNo());
	}
	
	private void stored() {
		try {
			String path = "src/io8/" + getLottoNo() + ".csv";
			PrintWriter writer = new PrintWriter(path);
			
			for (Order order : db) {
				writer.println(order.toText());
			}
			
			writer.flush();
			writer.close();
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("파일을 찾을 수 없습니다.", ex);
		}
	}
	
	public List<Order> getOrders(int lottoNo) {
		try {
			List<Order> orders = new ArrayList<Order>();
			
			String path = "src/io8/" + lottoNo + ".csv";
			BufferedReader reader = new BufferedReader(new FileReader(path));
			
			String text = null;
			while ((text = reader.readLine()) != null) {
				orders.add(Order.toOrder(text));
			}
			reader.close();
			
			return orders;
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("파일을 찾을 수 없습니다.", ex);
		} catch (IOException ex) {
			throw new RuntimeException("파일을 읽어오는 중 오류가 발생하였습니다.", ex);
		}
	}
	
	public int getLottoNo() {
		LocalDate today = LocalDate.now();
		String todayText = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		return Integer.parseInt(todayText);
	}
	
	/*
	 * 주문정보를 저장한다.
	 */
	public void save(Order order) {
		// 전달받은 주문정보를 ArrayList객체에 저장한다.
		db.add(order);
		// 변경된 정보를 파일에 저장시킨다.
		stored();
	}
	
}