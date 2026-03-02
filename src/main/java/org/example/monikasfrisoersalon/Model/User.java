package org.example.monikasfrisoersalon.Model;

public abstract class User {

    protected String username;
    protected String password;
    protected int Id;
    protected int phoneNumber;

    public User(int id, String username, String password, int phoneNumber) {
        this.username = username;
        this.password = password;
        this.Id = id;
        this.phoneNumber = phoneNumber;
    }

    public User (String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public int getId() {return Id;}
    public int getPhoneNumber() {return phoneNumber;}

    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setPhoneNumber(int phoneNumber) {this.phoneNumber = phoneNumber;}

}
