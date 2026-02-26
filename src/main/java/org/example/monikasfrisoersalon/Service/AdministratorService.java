package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Repoistory.AdministratorRepository;

import java.util.List;

public class AdministratorService {
    private final AdministratorRepository administratorRepository;

    //
    public AdministratorService(AdministratorRepository administratorRepo) {
        this.administratorRepository = administratorRepo;
    }

    // Get Administrators
    public List<Administrator> getAdministrators(){
       return administratorRepository.FindAdministrators();
    }

}
