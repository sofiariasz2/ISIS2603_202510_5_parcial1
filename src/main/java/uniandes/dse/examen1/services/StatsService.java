package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.RecordRepository;
import uniandes.dse.examen1.repositories.StudentRepository;

@Service
public class StatsService {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private RecordRepository recordRepository;
    
    /**
     * Calcula el promedio de notas de un estudiante
     * 
     * @param login Login del estudiante
     * @return Promedio de notas del estudiante
     * @throws InvalidRecordException Si el estudiante no existe
     */
    @Transactional
    public Double calculateStudentAverage(String login) throws InvalidRecordException {
        //busco al estudiante por su login
        List<StudentEntity> allStudents = studentRepository.findAll();
        StudentEntity student = allStudents.stream()
            .filter(s -> login.equals(s.getLogin()))
            .findFirst()
            .orElseThrow(() -> new InvalidRecordException("no existe un estudiante con el login: " + login));
        
        //saco todos los registros
        List<RecordEntity> allRecords = recordRepository.findAll();
        
        //filtro los registros del estudiante
        List<RecordEntity> studentRecords = allRecords.stream()
            .filter(r -> r.getStudent().getId().equals(student.getId()))
            .toList();
        
        
        if (studentRecords.isEmpty()) {
            return 0.0;
        }
        
        //prom
        double sum = 0.0;
        for (RecordEntity record : studentRecords) {
            if (record.getGrade() != null) {
                sum += record.getGrade();
            }
        }
        
        return sum / studentRecords.size();
    }
    
    /**
     * Calcula el promedio de notas de un curso
     * 
     * @param courseCode Código del curso
     * @return Promedio de notas del curso
     * @throws InvalidRecordException Si el curso no existe
     */
    @Transactional
    public Double calculateCourseAverage(String courseCode) throws InvalidRecordException {
        // Buscar el curso por su código
        List<CourseEntity> allCourses = courseRepository.findAll();
        CourseEntity course = allCourses.stream()
            .filter(c -> courseCode.equals(c.getCourseCode()))
            .findFirst()
            .orElseThrow(() -> new InvalidRecordException("No existe un curso con el código: " + courseCode));
        
        // Obtener todos los registros
        List<RecordEntity> allRecords = recordRepository.findAll();
        
        // Filtrar los registros del curso
        List<RecordEntity> courseRecords = allRecords.stream()
            .filter(r -> r.getCourse().getId().equals(course.getId()))
            .toList();
        
        // Si no hay registros, retornar 0
        if (courseRecords.isEmpty()) {
            return 0.0;
        }
        
        // Calcular el promedio
        double sum = 0.0;
        for (RecordEntity record : courseRecords) {
            if (record.getGrade() != null) {
                sum += record.getGrade();
            }
        }
        
        return sum / courseRecords.size();
    }
}
