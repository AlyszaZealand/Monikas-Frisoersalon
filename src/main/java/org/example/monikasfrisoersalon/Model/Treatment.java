package org.example.monikasfrisoersalon.Model;

public class Treatment {

    private int Id;
    private String typeOfTreatment;
    private int duration;

    public Treatment(int id, String typeOfTreatment, int duration) {
        this.Id = id;
        this.typeOfTreatment = typeOfTreatment;
        this.duration = duration;
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
}
