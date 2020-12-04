package com.repositories.investigacion.v3.utilities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry{

	private String identifier;
	private String title;
	private String author;
	private String date;
	private String href;
	
	public Entry() {
		
	}
	
	public Entry(String identifier, String title, String author, String date, String href) {
		this.identifier = identifier;
		this.title = title;
		this.author = author;
		this.date = date;
		this.href = href;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String toString() {
		return "Entry [identifier=" + identifier + ", title=" + title + ", author=" + author + ", date=" + date
				+ ", href=" + href + "]";
	}
}