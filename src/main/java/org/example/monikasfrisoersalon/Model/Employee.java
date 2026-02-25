package org.example.monikasfrisoersalon.Model;

public class Employee extends User{

    public Employee(int id, String username, String password, int phoneNumber) {
        super(id,username,password,phoneNumber);
    }

    public Employee(String username, String password){
      super(username,password);
    }

    @Override
    public String toString() {
        return "(" + Id + ") Employee: " + username + "Password: " + password + "Phone-Number: " + phoneNumber;
    }

}
