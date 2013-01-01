package pl.edu.agh.security.rest.provider.service;

public class Product {

	private String id;

	private String name;

	public Product(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Product() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
