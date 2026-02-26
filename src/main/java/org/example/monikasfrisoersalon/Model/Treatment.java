package org.example.monikasfrisoersalon.Model;

public class Treatment {

    private int Id;
    private String typeOfTreatment;
    private int duration;
    private boolean isActive;

    public Treatment(int id, String typeOfTreatment, int duration, boolean isActive) {
        this.Id = id;
        this.typeOfTreatment = typeOfTreatment;
        this.duration = duration;
        this.isActive = isActive;
    }

    public int getDuration(){
        return duration;
    }
    public String getTypeOfTreatment(){
        return typeOfTreatment;
    }
    public int getId(){
        return Id;
    }
    public boolean getIsActive() {return isActive;}
}
