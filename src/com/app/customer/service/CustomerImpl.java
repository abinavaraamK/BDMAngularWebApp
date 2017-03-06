package com.app.customer.service;

import com.app.customer.dao.CustomerDao;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.util.List;

public class CustomerImpl implements ICustomer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -834079942480093517L;
	private CustomerDao customerDao;

	public void setCustomerDao ( CustomerDao customerDao ) {
		this.customerDao = customerDao;
	}
	
	public CustomerDao getCustomerDao () {
		return customerDao;
	}

	public String insert(Customer object) throws HexApplicationException {
	
		System.out.println("inside insert in Service ");			
		return customerDao.insert(object);
		
	}


	public String deleteAll(List<Customer> customers) throws HexApplicationException {
	
		System.out.println("inside deleteAll in Service ");		
		return customerDao.deleteAll(customers);
	
	}

	public String update(Customer customer) throws HexApplicationException {
	
		System.out.println("inside update in Service ");
		return customerDao.update(customer);
	
	}

	public List<Customer> getAllCustomer() throws HexApplicationException {
	
		System.out.println("inside getAllCustomer in Service ");	
		return customerDao.getAllCustomer();
	
	}

	public List<Customer> search(String fieldValue, String columnName) throws HexApplicationException {

	System.out.println("Entering into service implementation : " + fieldValue + "***" +columnName );
				
		return customerDao.search(fieldValue, columnName);
	
	}


}
