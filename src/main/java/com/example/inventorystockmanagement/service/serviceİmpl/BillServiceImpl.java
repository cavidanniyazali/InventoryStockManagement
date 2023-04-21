package com.example.inventorystockmanagement.service.serviceİmpl;

import com.example.inventorystockmanagement.entities.Bill;
import com.example.inventorystockmanagement.repositories.BillRepository;
import com.example.inventorystockmanagement.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	private BillRepository billRepository;
	
	@Override
	public List<Bill> getAllBills() {
		return billRepository.findAll();
	}

	@Override
	public void saveBill(Bill bill) {
		this.billRepository.save(bill);
	}

	@Override
	public Bill getBillById(long id) {
		Optional<Bill> optional = billRepository.findById(id);
		Bill bill = null;
		if (optional.isPresent()) {
			bill = optional.get();
		} else {
			throw new RuntimeException("Bill not found for id : " + id);
		}
		return bill;
	}

	@Override
	public void deleteBillById(long id) {
		this.billRepository.deleteById(id);
	}

}
