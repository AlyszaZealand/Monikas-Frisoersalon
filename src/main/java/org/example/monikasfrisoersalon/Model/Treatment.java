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

    public int getDuration(){
        return duration;
    }
    public String getTypeOfTreatment(){
        return typeOfTreatment;
    }

    public int getPrice(){
        return price;
    }
    public int getId(){
        return Id;
    }
}
