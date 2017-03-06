package com.app.customer.delegate;

import com.app.customer.service.CustomerImpl;
import com.app.customer.service.ICustomer;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class CustomerBusinessDelegate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3440828310386661784L;
	private ICustomer customerService;

	public CustomerBusinessDelegate(String serviceType) throws MalformedURLException {
		if (serviceType.equals("Rest")) {

		} else if (serviceType.equals("SOAP")) {
			String serverHost = "http://localhost:8080";
			customerService = getSoapCustomerService(serverHost);
		} else {
			customerService = new CustomerImpl();
		}
	}

	public void insert(Customer object) throws HexApplicationException {
		customerService.insert(object);
	}

	public void deleteAll(List<Customer> customers) throws HexApplicationException {
		customerService.deleteAll(customers);
	}

	public void update(Customer customer) throws HexApplicationException {
		customerService.update(customer);
	}

	public List<Customer> getAllCustomer() throws HexApplicationException {
		return customerService.getAllCustomer();
	}

	public List<Customer> search(String fieldValue, String columnName) throws HexApplicationException {
		return customerService.search(fieldValue, columnName);
	}

	

	private ICustomer getSoapCustomerService(String serverHost) throws MalformedURLException {
		URL url = new URL(serverHost + "/AngularApp/customerService?wsdl");
		QName qname = new QName("http://service.customer.app.com/", "SOAPServiceService");

		Service service = Service.create(url, qname);

		ICustomer customerService = service.getPort(ICustomer.class);

		return customerService;
	}

}
