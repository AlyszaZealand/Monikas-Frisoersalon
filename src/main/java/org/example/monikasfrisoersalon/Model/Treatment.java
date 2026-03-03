package org.example.monikasfrisoersalon.Model;

public class Treatment {

    private int Id;
    private String typeOfTreatment;
    private int duration;
    private int price;
    private boolean isActive;

    public Treatment(int id, String typeOfTreatment, int duration, int price, boolean isActive) {
        this.Id = id;
        this.typeOfTreatment = typeOfTreatment;
        this.duration = duration;
        this.price = price;
        this.isActive = isActive;
    }

    public int getId(){
        return Id;
    }
    public String getTypeOfTreatment(){
        return typeOfTreatment;
    }
    public int getDuration(){
        return duration;
    }
    public int getPrice() {return price;}
    public boolean getIsActive() {return isActive;}


    public void setTypeOfTreatment(String typeOfTreatment) {this.typeOfTreatment = typeOfTreatment;}
    public void setDuration(int duration) {this.duration = duration;}
    public void setPrice(int price) {this.price = price;}
    public void setIsActive(boolean isActive) {this.isActive = isActive;}

    @Override
    public String toString() {
        return typeOfTreatment + " (" + duration + " min) - " + price + " kr.";
    }
}

