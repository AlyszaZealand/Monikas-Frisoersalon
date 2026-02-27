package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Administrator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorRepositoryTest {

    @Test
    void testFindAdministrators() {
        // Arrange - Set up the repository and expected results
        DB db = new DB();
        AdministratorRepository adminRepo = new AdministratorRepository(db);

        // Act - Call the method to be tested
        List<Administrator> result = adminRepo.findAdministrators();

        // Assert - Verify the results
        assertNotNull(result, "listen må ikke være null");
        assertFalse(result.isEmpty(), "listen må ikke være tom");

        // Additional assertions can be added here based on expected data
        String expectedName = result.get(0).getUsername();
        assertEquals("monika", expectedName, "Første administrator skal have brugernavnet 'admin'");

    }

}