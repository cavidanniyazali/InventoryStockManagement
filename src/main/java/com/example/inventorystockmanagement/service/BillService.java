package com.example.inventorystockmanagement.service;

import com.example.inventorystockmanagement.entities.Bill;

import java.util.List;

public interface BillService {
	List<Bill> getAllBills();
	void saveBill(Bill bill);
	Bill getBillById(long id);
	void deleteBillById(long id);
}
