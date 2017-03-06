package com.app.customer.service;

import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import java.io.Serializable;

@WebService
@SOAPBinding(style=Style.DOCUMENT)
public interface ICustomer extends Serializable {
	
	@WebMethod public String insert(Customer customer) throws HexApplicationException;

	@WebMethod public String deleteAll(List<Customer> customers) throws HexApplicationException;

	@WebMethod public String update(Customer customer) throws HexApplicationException;

	@WebMethod public List<Customer> getAllCustomer() throws HexApplicationException;

	@WebMethod public List<Customer> search(String fieldValue, String columnName) throws HexApplicationException;

	
}
