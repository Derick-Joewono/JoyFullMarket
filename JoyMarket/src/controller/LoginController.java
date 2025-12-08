package controller;

import repository.CustomerRepository;

public class LoginController {
	
	private CustomerRepository customerRepository = new CustomerRepository();
	String email,password;
	
	
//	Email
//	Must be filled.
//	Must match an email in the database.
//	Password
//	Must be filled.
//	Must match password in the database.
	public String authentication() {
		if(email==null||email.isEmpty()) {
			return "Email must be filled";
		}
		if (password==null||password.isEmpty()) {
			return"Password must be filled";
		}
		
		if(!customerRepository.isMatchPassAndEmail(email,password)) {
			return "Wrong Email or Password";
		}
		
		return "Login Success";
		
	}

}
