package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.RecordService;
import uniandes.dse.examen1.services.StudentService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class })
public class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    private PodamFactory factory = new PodamFactoryImpl();

    @Test
    void testCreateRecordWithInvalidGrade() {
        // Crear un registro con nota inválida (menor a 1.5)
        RecordEntity record = new RecordEntity();
        record.setGrade(1.0);
        record.setSemester("2023-10");
        
        try {
            //uso ids que no existen para el test
            recordService.createRecord(record, 1L, 1L);
            fail("Debería fallar porque la nota es menor a 1.5");
        } catch (InvalidRecordException e) {
            
            assertTrue(true);
        }
    }
    
    @Test
    void testCreateRecordWithNonExistentStudent() {
        RecordEntity record = new RecordEntity();
        record.setGrade(4.0);
        record.setSemester("2023-10");
        
        try {
            //pruebo usar un id que no existe
            recordService.createRecord(record, 999L, 1L);
            fail("Debería fallar porque el estudiante no existe");
        } catch (InvalidRecordException e) {
            
            assertTrue(true);
        }
    }
    
    @Test
    void testCreateRecordWithNonExistentCourse() {
        RecordEntity record = new RecordEntity();
        record.setGrade(4.0);
        record.setSemester("2023-10");
        
        try {
            // Usar un ID que no existe
            recordService.createRecord(record, 1L, 999L);
            fail("Debería fallar porque el curso no existe");
        } catch (InvalidRecordException e) {
            
            assertTrue(true);
        }
    }
}
