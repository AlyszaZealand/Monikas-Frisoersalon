package org.example.monikasfrisoersalon.Model;

import java.time.LocalDateTime;

public class Appointment {

    private int id;
    private Customer customer;
    private Employee employee;
    private Treatment treatment;
    private boolean appStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    public Appointment(int id,Customer customer, Employee employee, Treatment treatment, boolean appStatus, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.customer = customer;
        this.employee = employee;
        this.treatment = treatment;
        this.appStatus = appStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {return id;}
    public Customer getCustomer() {
        return customer;
    }
    public Employee getEmployee() {
        return employee;
    }
    public Treatment getTreatment() {
        return treatment;
    }
    public boolean getAppStatus() {
        return appStatus;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }


    public void setEmployee(Employee employee) {this.employee = employee;}
    public void setTreatment(Treatment treatment) {this.treatment = treatment;}
    public void setAppStatus(boolean appStatus) {this.appStatus = appStatus;}
    public void setStartDate(LocalDateTime startDate) {this.startDate = startDate;}
    public void setEndDate(LocalDateTime endDate) {this.endDate = endDate;}






}
