package com.app.customer.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import com.app.customer.dao.CustomerDao;
import com.app.customer.util.BootStrapper;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

@WebService(endpointInterface = "com.app.customer.service.ICustomer")
public class SOAPService implements ICustomer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6115098335114488177L;
	CustomerDao customerDao;
	public SOAPService(){
		customerDao = (CustomerDao) BootStrapper.getService().getBean("CustomerDao");
	}
	
	@Override
	public ArrayList<Customer> getAllCustomer() throws HexApplicationException {
		
		return (ArrayList<Customer>) customerDao.getAllCustomer();
	}

	@Override
	public String insert(Customer customer) throws HexApplicationException {
		customerDao.insert(customer);
		return "Customer Added Successfully";
	}

	@Override
	public String deleteAll(List<Customer> customers) throws HexApplicationException {
		customerDao.deleteAll(customers);
		return "Customers deleted Successfully";
	}

	@Override
	public String update(Customer customer) throws HexApplicationException {
		customerDao.update(customer);
		return "Customer Updated Successfully";
	}

	@Override
	public ArrayList<Customer> search(String fieldValue, String columnName) throws HexApplicationException {
		
		return (ArrayList<Customer>) customerDao.search(fieldValue, columnName);
	}

}
