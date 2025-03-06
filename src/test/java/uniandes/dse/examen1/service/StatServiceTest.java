package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.RecordService;
import uniandes.dse.examen1.services.StudentService;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.services.StatsService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class, StatsService.class })
public class StatServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private StatsService statsService;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException {
        // No es necesario inicializar nada aquí
    }

    @Test
    void testCalculateStudentAverage() throws Exception {
        // Crear un estudiante con login único
        StudentEntity student = new StudentEntity();
        student.setName("Test Student");
        student.setLogin("test_student_" + System.currentTimeMillis());
        studentService.createStudent(student);
        
        // Crear un curso con código único
        CourseEntity course = new CourseEntity();
        course.setName("Test Course");
        course.setCourseCode("TEST_" + System.currentTimeMillis());
        course.setCredits(3);
        courseService.createCourse(course);
        
        // Crear un registro
        RecordEntity record = new RecordEntity();
        record.setGrade(4.0);
        record.setSemester("2023-10");
        recordService.createRecord(record, student.getId(), course.getId());
        
        // Calcular el promedio
        Double average = statsService.calculateStudentAverage(student.getLogin());
        
        // Verificar que el promedio sea correcto
        assertEquals(4.0, average);
    }

    @Test
    void testCalculateCourseAverage() throws Exception {
        // Crear un estudiante con login único
        StudentEntity student = new StudentEntity();
        student.setName("Test Student 2");
        student.setLogin("test_student2_" + System.currentTimeMillis());
        studentService.createStudent(student);
        
        // Crear un curso con código único
        CourseEntity course = new CourseEntity();
        course.setName("Test Course 2");
        course.setCourseCode("TEST2_" + System.currentTimeMillis());
        course.setCredits(3);
        courseService.createCourse(course);
        
        // Crear un registro
        RecordEntity record = new RecordEntity();
        record.setGrade(3.5);
        record.setSemester("2023-20");
        recordService.createRecord(record, student.getId(), course.getId());
        
        // Calcular el promedio
        Double average = statsService.calculateCourseAverage(course.getCourseCode());
        
        // Verificar que el promedio sea correcto
        assertEquals(3.5, average);
    }
}
