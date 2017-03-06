package com.app.customer.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.app.customer.util.HibernateUtil;
import com.app.customer.vo.Customer;
import com.app.framework.exception.HexApplicationException;

public class CustomerDaoImpl implements CustomerDao {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8753890845263573260L;


	@Override
	public String insert(Customer object) throws HexApplicationException {
		System.out.println("inside insert..");
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			Session session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			session.save(object);
			transaction.commit();
			session.close();
			System.out.println("done...");
			return "Customer added Successfully";
		} catch (Exception exception) {
			throw new HexApplicationException ( exception );
		}

	}

	@Override
	public String update(Customer object) throws HexApplicationException {
		System.out.println("inside update..");
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			Session session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(object);
			transaction.commit();
			session.close();
			System.out.println("done...");
			return "Customer Updated Successfully";
		} catch (Exception exception) {
			throw new HexApplicationException ( exception );
		}

	}

	@Override
	public String deleteAll(List<Customer> customers) throws HexApplicationException {
		System.out.println("inside delete..");
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			Session session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			for(Customer customer : customers)
				session.delete(customer);
			transaction.commit();
			session.close();
			System.out.println("done...");
			return "Customers Deleted Successfully.";
		} catch (Exception exception) {
			throw new HexApplicationException ( exception );
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> getAllCustomer() throws HexApplicationException {
		System.out.println("inside getAllCustomer..");
		List<Customer> customers = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			Session session = sessionFactory.openSession();
			String hql = "From Customer";
			Query query = session.createQuery(hql);
			customers = query.list();
			session.close();
			System.out.println("done...");
		} catch (Exception exception) {
			throw new HexApplicationException ( exception );
		}
		return customers;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> search(String fieldValue, String columnName)
			throws HexApplicationException {
		System.out.println("inside getAllCustomer..");
		List<Customer> customers = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			Session session = sessionFactory.openSession();
			String hql = "From Customer where " + columnName + " like '%" + fieldValue + "%'	";
			Query query = session.createQuery(hql);
			customers = query.list();
			session.close();
			System.out.println("done...");
		} catch (Exception exception) {
			throw new HexApplicationException ( exception );
		}
		return customers;
	}
	
}
