package com.repositories.investigacion.utilities;

public class Repository {

	private String id;
	private String url;
	private String key;
	private String count;
	private String sort;
	
	public Repository() {
	}
	
	public Repository(String id, String url, String key, String count, String sort) {
		this.id = id;
		this.url = url;
		this.key = key;
		this.count = count;
		this.sort = sort;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "Repository [id=" + id + ", url=" + url + ", key=" + key + ", count=" + count + ", sort=" + sort + "]";
	}
}
