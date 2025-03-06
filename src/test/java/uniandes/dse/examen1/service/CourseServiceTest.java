package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.services.CourseService;

@DataJpaTest
@Transactional
@Import(CourseService.class)
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateCourse() {
        //creo un curso
        CourseEntity course = factory.manufacturePojo(CourseEntity.class);
        course.setCourseCode("CURSO_" + System.currentTimeMillis()); 
        course.setCredits(5); 
        
        try {
            
            CourseEntity savedCourse = courseService.createCourse(course);
            
           
            assertNotNull(savedCourse);
            assertEquals(course.getCourseCode(), savedCourse.getCourseCode());
            assertEquals(5, savedCourse.getCredits());
        } catch (RepeatedCourseException e) {
            fail("No debería fallar al crear un curso válido: " + e.getMessage());
        }
    }

    
    @Test
    void testCreateRepeatedCourse() {
        try {
            //creo un curso
            CourseEntity course1 = factory.manufacturePojo(CourseEntity.class);
            course1.setCredits(5); // Créditos válidos
            courseService.createCourse(course1);
            
            //creo otro curso con el mismo codigo
            CourseEntity course2 = factory.manufacturePojo(CourseEntity.class);
            course2.setCourseCode(course1.getCourseCode());
            course2.setCredits(3);
            
            // Intentar crear el curso repetido
            courseService.createCourse(course2);
            fail("Debeeria fallar porque el codigoya existe");
        } catch (RepeatedCourseException e) {
            // Verificar que sea la excepción correcta
            assertTrue(e.getMessage().contains("ya existe un curso con el codigo"));
        }
    }
}
