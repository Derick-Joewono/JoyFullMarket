package model;

public class Customer {

	private int id;
	private String full_name;
	private String email;
	private String password;
	private String phone;
	private String address;
	private String gender;
	
	public Customer(int id, String full_name, String email, String password, String phone, String address, String gender) {
		super();
		this.id = id;
		this.full_name = full_name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.phone = phone;
		this.gender = gender;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}
