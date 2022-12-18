package com.backend.test.model;

public class User {
	private long id;
	private String firstname;
	private String lastname;
	private String contactNum;
	public long getId() {
		return id;
	}

	public User() {
	}

	public User(long id, String firstname, String lastname, String contactNum){
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.contactNum = contactNum;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getContactNum() {
		return contactNum;
	}
	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", contactNum=" + contactNum + "]";
	}
}
