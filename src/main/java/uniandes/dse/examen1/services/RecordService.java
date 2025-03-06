package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;

    /**
     * Crea un nuevo registro para un estudiante y un curso
     * 
     * @param record La entidad del registro a crear
     * @param studentId ID del estudiante
     * @param courseId ID del curso
     * @return La entidad del registro creada
     * @throws InvalidRecordException si no se cumplen las restricciones
     */
    @Transactional
    public RecordEntity createRecord(RecordEntity record, Long studentId, Long courseId) throws InvalidRecordException {
        
        Optional<StudentEntity> optionalStudent = studentRepository.findById(studentId);
        if (!optionalStudent.isPresent()) {
            throw new InvalidRecordException("no existe un estudiante con el ID: " + studentId);
        }
        StudentEntity student = optionalStudent.get();
        
        Optional<CourseEntity> optionalCourse = courseRepository.findById(courseId);
        if (!optionalCourse.isPresent()) {
            throw new InvalidRecordException("no existe un curso con el ID: " + courseId);
        }
        CourseEntity course = optionalCourse.get();
        
        // chequeo la restriccion de que la nota este en el rango (1.5-5.0)
        if (record.getGrade() == null || record.getGrade() < 1.5 || record.getGrade() > 5.0) {
            throw new InvalidRecordException("la nota debe estar entre 1.5 y 5.0");
        }
        
        // si el estudiante ya aprobo el curso (nota >= 3.0)
        List<RecordEntity> TodosRecords = recordRepository.findAll();
        boolean aprobo = TodosRecords.stream()
            .filter(r -> r.getStudent().getId().equals(student.getId()))
            .filter(r -> r.getCourse().getId().equals(course.getId()))
            .anyMatch(r -> r.getGrade() != null && r.getGrade() >= 3.0);

        if (aprobo) {
            throw new InvalidRecordException("estudiante ya aprobo este curso y no puede volver a verlo");
        }
        
        //relacionar con estudiante y curso y chequear que si existe
        record.setStudent(student);
        record.setCourse(course);
        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
            course.getStudents().add(student);
            studentRepository.save(student);
        }
        
        //guardo record
        return recordRepository.save(record);
    }
}
