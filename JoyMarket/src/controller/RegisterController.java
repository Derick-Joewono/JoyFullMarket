package controller;

import model.Customer;
import repository.CustomerRepository;
import java.util.regex.Pattern;

public class RegisterController {

    private CustomerRepository customerRepository = new CustomerRepository();

    public String validateAndRegister(
            String full_name,
            String email,
            String password,
            String confirmPassword,
            String phone,
            String address,
            String gender
    ) {
    	// 1. name cannot be empty
        if (full_name == null || full_name.trim().isEmpty()) {
            return "Customer Full Name cannot be empty!";
        }

        // 2. Email must be filled
        if (email == null || email.trim().isEmpty()) {
            return "Customer Email must be filled!";
        }

        // 3. Must end with @gmail.com
        if (!email.endsWith("@gmail.com")) {
            return "Email must end with @gmail.com!";
        }

        // 3. Must be unique
        if (customerRepository.isEmailExist(email)) {
            return "Email already registered!";
        }

        // 4. Password min 6 chars
        if (password == null || password.length() < 6) {
            return "Password must be at least 6 characters!";
        }

        // 5. Confirm password must match
        if (!password.equals(confirmPassword)) {
            return "Confirm password must match password!";
        }

        // 6. Phone must be filled
        if (phone == null || phone.trim().isEmpty()) {
            return "Customer phone must be filled!";
        }

        // 6. Must be numeric
        if (!phone.matches("\\d+")) {
            return "Customer phone must be numeric!";
        }

        // 6. Must be 10–13 digits
        if (phone.length() < 10 || phone.length() > 13) {
            return "Customer phone must be 10–13 digits!";
        }

        // 7. Address must be filled
        if (address == null || address.trim().isEmpty()) {
            return "Customer address must be filled!";
        }

        // 8. Gender must be chosen
        if (gender == null || gender.trim().isEmpty()) {
            return "Customer gender must be chosen!";
        }

        Customer customer = new Customer(
                full_name,
                email,
                password,
                phone,
                address,
                gender
        );

        customerRepository.insertCustomer(customer);

        return "SUCCESS";
    }
}
