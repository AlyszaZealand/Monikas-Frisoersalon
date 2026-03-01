package org.example.monikasfrisoersalon.Repository;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Administrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorRepositoryTest {

    private DB db;
    private AdministratorRepository adminRepo;

    @BeforeEach
    void setUp() {
        db = new DB();
        adminRepo = new AdministratorRepository(db);
    }

    @Test
    void testFindAdministrators() {
        List<Administrator> result = adminRepo.findAdministrators();

        assertNotNull(result, "Listen må ikke være null");
        assertFalse(result.isEmpty(), "Listen må ikke være tom");
        Administrator firstAdmin = result.get(0);

        assertEquals("monika", firstAdmin.getUsername(), "Første administrator skal have brugernavnet 'monika'");
        assertEquals("monika123", firstAdmin.getPassword(), "Første administrator skal have adgangskoden 'monika123'");
    }
}