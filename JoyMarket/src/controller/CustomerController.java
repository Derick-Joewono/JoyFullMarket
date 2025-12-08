package controller;

import model.Customer;
import repository.CustomerRepository;

public class CustomerController {

	CustomerRepository repo= new CustomerRepository();
	
	public boolean register() {
		int id = 0;
		String full_name = null;
		String email = null;
		String password = null;
		String phone = null;
		String address = null;
		String gender= null;
		Customer customerRegist = new Customer(id,full_name,email,password,phone,address,gender);
		
		repo.insertCustomer(customerRegist);
		
		return false;
		
	}
	
}
