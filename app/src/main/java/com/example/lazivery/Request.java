package com.example.lazivery;

public class Request {
    private String id;
    private String location;
    private String payment;
    private String items;
    private String instructions;
    private String method;

    public Request() {}

    public Request(String id, String location, String payment, String items, String instructions, String method) {
        this.id = id;
        this.location = location;
        this.payment = payment;
        this.items = items;
        this.instructions = instructions;
        this.method = method;
    }

    public String getId() {return id;}
    public String getLocation() {return location;}
    public String getPayment() {return payment;}
    public String getItems() {return items;}
    public String getInstructions() {return instructions;}
    public String getMethod() {return method;}

}

