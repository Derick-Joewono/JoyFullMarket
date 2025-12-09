package controller;

import repository.CustomerBalanceRepository;

public class CustomerBalanceController {

    private CustomerBalanceRepository balanceRepo = new CustomerBalanceRepository();

    // Ambil saldo
    public double getBalance(int customerId) {
        return balanceRepo.getBalance(customerId);
    }

    // Top Up saldo
    public boolean topUp(int customerId, double amount) {
        if (amount <= 0) return false; // Tidak bisa minus atau 0
        return balanceRepo.topUp(customerId, amount);
    }
}
