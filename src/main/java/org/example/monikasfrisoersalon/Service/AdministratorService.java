package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Repository.AdministratorRepository;

import java.util.List;

public class AdministratorService {

    private final AdministratorRepository administratorRepo;

    public AdministratorService(AdministratorRepository administratorRepo) {
        this.administratorRepo = administratorRepo;
    }

    public List<Administrator> getAdministrators() {
        return administratorRepo.findAdministrators();
    }
}
