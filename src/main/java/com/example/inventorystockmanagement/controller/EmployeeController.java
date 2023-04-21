package com.example.inventorystockmanagement.controller;

import com.example.inventorystockmanagement.entities.Bill;
import com.example.inventorystockmanagement.entities.Product;
import com.example.inventorystockmanagement.entities.SoldProduct;
import com.example.inventorystockmanagement.service.BillService;
import com.example.inventorystockmanagement.service.ProductService;
import com.example.inventorystockmanagement.service.SoldProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private ProductService productService;

	@Autowired
	private SoldProductService soldProductService;

	@Autowired
	private BillService billService;

	private int number=0;

	@GetMapping("/listEmployeeProducts")
	public String listEmployeeProducts(Model theModel, HttpServletResponse response) {

		List<SoldProduct> soldProductss = soldProductService.getAllSoldProducts();
		List<SoldProduct> soldProducts = new ArrayList<SoldProduct>();
		for(SoldProduct sp:soldProductss) {
			if(sp.getIn_basket() == true) {
				soldProducts.add(sp);
			}
		}
		int productItem = 0;
		int totalPrice = 0;
		for(SoldProduct soldProduct : soldProducts) {
			productItem += soldProduct.getPiece();
			totalPrice += productService.getProductById(soldProduct.getProduct_id()).getPrice() * soldProduct.getPiece();
		}
		Cookie cookieProductItem = new Cookie("productItem", String.valueOf(productItem));
		Cookie cookieTotalPrice = new Cookie("totalPrice",String.valueOf(totalPrice));
		response.addCookie(cookieProductItem);
		response.addCookie(cookieTotalPrice);


		List<Product> theProducts = productService.getAllProducts();
		theModel.addAttribute("products", theProducts);
		return "listEmployeeProducts";
	}

	@GetMapping("/seeProduct/{id}")
	public String seeProduct(@PathVariable(value = "id") long id, Model model) {
		Product product = productService.getProductById(id);
		model.addAttribute("product", product);
		SoldProduct soldProduct = new SoldProduct();
		model.addAttribute("soldProduct", soldProduct);
		return "seeProduct";
	}


	@PostMapping("/addBasket")
	public String addBasket(String piece, String productId, HttpServletResponse response) {
		List<Bill> bills = billService.getAllBills();
		Boolean haveEmptyBill = false;
		long emptyBillId = 0;
		// If there is no bill
		if (bills.size() == 0) {
			Bill bill = new Bill();
			bill.setCustomer_name("-");
			bill.setCustomer_surname("-");
			bill.setTotal_price(0);
			bill.setBill_date(java.sql.Date.valueOf(LocalDate.now()));
			bill.setEmployee_confirmed(haveEmptyBill);
			bill.setAdmin_confirmed(false);
			billService.saveBill(bill);
			addBasket(piece, productId, response);
		} else {
			// There is a bill already, but there are no blank bills to use at the moment
			for (Bill bill : bills) {
				if (bill.getEmployee_confirmed() == false) {
					haveEmptyBill = true;
					emptyBillId = bill.getId();
				}
			}

			///// --------
			Boolean alreadyInBasketItem = false;
			List<SoldProduct> soldProductss = soldProductService.getAllSoldProducts();
			for (SoldProduct sp : soldProductss) {
				if (sp.getIn_basket() == true) {
					alreadyInBasketItem = true;
					break;
				}
			}

			if (alreadyInBasketItem) {
				Product product = productService.getProductById(Long.parseLong(productId));
				product.setStock(product.getStock() - Integer.parseInt(piece));
				productService.saveProduct(product);
			} else {
				Product product = productService.getProductById(Long.parseLong(productId));
				if (Integer.parseInt(piece)%2==0) {
					product.setStock(product.getStock() - (Integer.parseInt(piece) / 2));
				} else {
					number++;
					if (number==2){
						product.setStock(product.getStock() - 1);
						number=0;
					}
					product.setStock(product.getStock() - (Integer.parseInt(piece) / 2));
				}
				productService.saveProduct(product);
			}

			// adding sold product
			if (haveEmptyBill) {
				Boolean alreadyExist = true;
				List<SoldProduct> soldProducts = soldProductService.getAllSoldProducts();
				for (SoldProduct soldProduct : soldProducts) {
					if (soldProduct.getProduct_id() == Long.parseLong(productId)
							&& soldProduct.getIn_basket() == true) {
						soldProduct.setPiece(soldProduct.getPiece() + Integer.parseInt(piece));
						soldProductService.saveSoldProduct(soldProduct);
						alreadyExist = false;
					}
				}
				if (alreadyExist) {
					SoldProduct soldProduct = new SoldProduct();
					soldProduct.setProduct_id(Long.parseLong(productId));
					soldProduct.setPiece(Integer.parseInt(piece));
					soldProduct.setIn_basket(true);
					soldProduct.setBill_id(emptyBillId);
					soldProductService.saveSoldProduct(soldProduct);
				}
			} else {
				Bill bill = new Bill();
				bill.setCustomer_name("-");
				bill.setCustomer_surname("-");
				bill.setTotal_price(0);
				bill.setBill_date(java.sql.Date.valueOf(LocalDate.now()));
				bill.setEmployee_confirmed(false);
				bill.setAdmin_confirmed(false);
				billService.saveBill(bill);
				addBasket(piece, productId, response);
			}
		}

//		List<SoldProduct> soldProducts = billService.getBillById(emptyBillId).getSold_products();
//		int productItem = 0;
//		int totalPrice = 0;
//		for(SoldProduct soldProduct : soldProducts) {
//			productItem += soldProduct.getPiece();
//			totalPrice += productService.getProductById(soldProduct.getProduct_id()).getPrice() * soldProduct.getPiece();
//		}
//		Cookie cookieProductItm = new Cookie("productItem", String.valueOf(productItem));
//		Cookie cookieTotalPrice = new Cookie("totalPrice",String.valueOf(totalPrice));
//		response.addCookie(cookieProductItm);
//		response.addCookie(cookieTotalPrice);


		return "redirect:/employee/listEmployeeProducts";
	}

	@GetMapping("/seeBasket")
	public String seeBasket(Model theModel) {
		List<SoldProduct> allSoldProducts = soldProductService.getAllSoldProducts();
		List<SoldProduct> soldProducts = new ArrayList<SoldProduct>();
		for (SoldProduct sp : allSoldProducts) {
			if (sp.getIn_basket() == true) {
				soldProducts.add(sp);
			}
		}
		List<Product> products = productService.getAllProducts();
		theModel.addAttribute("products", products);
		theModel.addAttribute("soldProducts", soldProducts);
		return "seeBasket";
	}

	@GetMapping("/createBill")
	public String createBill(Model theModel) {
		List<Bill> bills = billService.getAllBills();
		Bill modelBill = null;
		for (Bill bill : bills) {
			if (bill.getEmployee_confirmed() == false) {
				modelBill = bill;
			}
		}
		int totalPrice = 0;
		List<SoldProduct> soldProducts = soldProductService.getAllSoldProducts();
		for (SoldProduct soldProduct : soldProducts) {
			if (soldProduct.getIn_basket()) {
				totalPrice += productService.getProductById(soldProduct.getProduct_id()).getPrice()
						* soldProduct.getPiece();
			}
		}
		System.out.println(totalPrice + " ** *** * *");
		modelBill.setTotal_price(totalPrice);
		theModel.addAttribute("modelBill", modelBill);
		return "createBill";
	}

	@PostMapping("/saveBill")
	public String saveBill(@ModelAttribute("modelBill") Bill bill,HttpServletResponse response) {
		List<Bill> bills = billService.getAllBills();
		Bill nowBill = null;
		for (Bill b : bills) {
			if (b.getEmployee_confirmed() == false) {
				nowBill = b;
				break;
			}
		}
		nowBill.setAdmin_confirmed(false);
		nowBill.setEmployee_confirmed(true);
		nowBill.setBill_date(bill.getBill_date());
		nowBill.setCustomer_name(bill.getCustomer_name());
		nowBill.setCustomer_surname(bill.getCustomer_surname());
		nowBill.setTotal_price(bill.getTotal_price());
		nowBill.setBill_date(java.sql.Date.valueOf(LocalDate.now()));
		billService.saveBill(nowBill);

		List<SoldProduct> soldProducts = soldProductService.getAllSoldProducts();
		for (SoldProduct sp : soldProducts) {
			if (sp.getIn_basket() == true) {
				sp.setIn_basket(false);
				soldProductService.saveSoldProduct(sp);
			}
		}

		Cookie cookieProductItem = new Cookie("productItem", "0");
		Cookie cookieTotalPrice = new Cookie("totalPrice","0");
		response.addCookie(cookieProductItem);
		response.addCookie(cookieTotalPrice);

		return "redirect:/employee/home";
	}

	@GetMapping("/cancelTheSale")
	public String cancelTheSale(HttpServletResponse response) {
		List<SoldProduct> soldProducts = soldProductService.getAllSoldProducts();
		// List<Long> soldProductIds = new ArrayList<Long>();
		List<SoldProduct> selectSoldProducts = new ArrayList<SoldProduct>();
		long billId = 0;
		for (SoldProduct soldProduct : soldProducts) {
			if (soldProduct.getIn_basket() == true) {
				selectSoldProducts.add(soldProduct);
				soldProductService.deleteSoldProductById(soldProduct.getId());
				billId = soldProduct.getBill_id();
			}
		}

		billService.deleteBillById(billId);

		for (SoldProduct selectSoldProduct : selectSoldProducts) {
			Product product = productService.getProductById(selectSoldProduct.getProduct_id());
			product.setStock(product.getStock() + selectSoldProduct.getPiece());
			productService.saveProduct(product);
		}


		Cookie cookieProductItem = new Cookie("productItem", "0");
		Cookie cookieTotalPrice = new Cookie("totalPrice","0");
		response.addCookie(cookieProductItem);
		response.addCookie(cookieTotalPrice);

		return "redirect:/employee/home";
	}



}