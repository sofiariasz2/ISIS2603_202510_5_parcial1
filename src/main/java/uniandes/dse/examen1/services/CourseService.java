package uniandes.dse.examen1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.repositories.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Crea un nuevo curso
     * 
     * @param course La entidad del curso a crear
     * @return La entidad del curso creada
     * @throws RepeatedCourseException Si el código del curso ya existe
     */
    @Transactional
    public CourseEntity createCourse(CourseEntity course) throws RepeatedCourseException {
        
        if (course.getCourseCode() == null || course.getCourseCode().isEmpty()) {
            throw new RepeatedCourseException("el codigo del curso no puede ser nulo o vacio");
        }
        
        //chequeo si existe un curso con el mismo código
        if (courseRepository.findByCourseCode(course.getCourseCode()) != null) {
            throw new RepeatedCourseException("ya existe un curso con el codigo: " + course.getCourseCode());
        }
        
        //miro el rango válido de 1 a 9 
        if (course.getCredits() == null || course.getCredits() < 1 || course.getCredits() > 9) {
            throw new RepeatedCourseException("los créditos del curso deben estar entre 1 y 9");
        }
        
        //crea el curso
        return courseRepository.save(course);
    }
}
