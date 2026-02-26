package org.example.monikasfrisoersalon.Model;

public class Customer extends User{

    public Customer(int id, String username, String password, int phoneNumber) {
        super(id, username, password, phoneNumber);
    }
    public Customer(String username, String password){
        super(username, password);
    }

    @Override
    public String toString() {
        return "(" + Id + ") Customer: " + username + "Password: " + password + "Phone-Number: " + phoneNumber;
    }

}
