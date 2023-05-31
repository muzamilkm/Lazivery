package com.example.lazivery;

public class Request {
    private String id;
    private String location;
    private String payment;
    private String items;
    private String instructions;
    private String method;
    private String status;

    public Request() {}

    public Request(String location, String payment, String items, String instructions, String method, String status) {
        this.location = location;
        this.payment = payment;
        this.items = items;
        this.instructions = instructions;
        this.method = method;
        this.status = status;
    }

    public String getId() {return id;}
    public String getLocation() {return location;}
    public String getPayment() {return payment;}
    public String getItems() {return items;}
    public String getInstructions() {return instructions;}
    public String getMethod() {return method;}
    public String getStatus(){return status;}

}

