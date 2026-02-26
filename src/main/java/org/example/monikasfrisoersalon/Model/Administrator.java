package org.example.monikasfrisoersalon.Model;

public class Administrator extends User{

    public Administrator(int id, String username, String password, int phoneNumber) {
        super(id,username,password,phoneNumber);
    }
    public Administrator(String username, String password){
        super(username,password);
    }

    @Override
    public String toString() {
        return "(" + Id + ") Admin: " + username + "Password: " + password + "Phone-Number: " + phoneNumber;
    }
}
