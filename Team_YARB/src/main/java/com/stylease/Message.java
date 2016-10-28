package com.stylease;

public class Message {
    int id;
    String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public Message(int id, String text) {
        this.id = id;
		this.text = text;
	}

	public Message() {
        this.id = 0;
        this.text = "";
	}
}
