package com.app.customer.vo;

import java.util.Date;

import java.io.Serializable;

public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532249044247365699L;
	private Integer customerid;
	private String name;
	private String email;
	private String phone;
	private String address;
	private String orders;
	private String action;
	
	private Date createddate;
	private String createdby = "";
	
	private Date modifieddate;
	private String modifiedby = "";

	public Customer() {
		super();
	}

	public Customer(Integer customerid, String name, String email, String phone, String address, String orders,
			String action, Date createddate, String createdby, Date modifieddate, String modifiedby,
			boolean toBeDeleted) {
		super();
		this.customerid = customerid;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.orders = orders;
		this.action = action;
		this.createddate = createddate;
		this.createdby = createdby;
		this.modifieddate = modifieddate;
		this.modifiedby = modifiedby;
		this.toBeDeleted = toBeDeleted;
	}

	public void setCustomerid(Integer newValue) {
		customerid = newValue;
	}

	public Integer getCustomerid() {
		return customerid;
	}

	public void setName(String newValue) {
		name = newValue;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String newValue) {
		email = newValue;
	}

	public String getEmail() {
		return email;
	}

	public void setPhone(String newValue) {
		phone = newValue;
	}

	public String getPhone() {
		return phone;
	}

	public void setAddress(String newValue) {
		address = newValue;
	}

	public String getAddress() {
		return address;
	}

	public void setOrders(String newValue) {
		orders = newValue;
	}

	public String getOrders() {
		return orders;
	}

	public void setAction(String newValue) {
		action = newValue;
	}

	public String getAction() {
		return action;
	}

	public void setCreateddate(Date newValue) {
		createddate = newValue;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreatedby(String newValue) {
		createdby = newValue;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setModifieddate(Date newValue) {
		modifieddate = newValue;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifiedby(String newValue) {
		modifiedby = newValue;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	private boolean toBeDeleted;

	public boolean isToBeDeleted() {
		return toBeDeleted;
	}

	public void setToBeDeleted(boolean toBeDeleted) {
		this.toBeDeleted = toBeDeleted;
	}

}
