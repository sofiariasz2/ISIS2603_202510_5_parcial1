package uniandes.dse.examen1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Crea un nuevo estudiante
     * 
     * @param student La entidad del estudiante a crear
     * @return La entidad del estudiante creada
     * @throws RepeatedStudentException Si el login del estudiante ya existe
     */
    @Transactional
    public StudentEntity createStudent(StudentEntity student) throws RepeatedStudentException {
        // chequeo que el login no se repita
        if (student.getLogin() == null || student.getLogin().isEmpty()) {
            throw new RepeatedStudentException("el login del estudiante no puede ser nulo o vacio");
        }
        
        //busco si existe un estiudiante con el mismo login
        if (studentRepository.findByLogin(student.getLogin()) != null) {
            throw new RepeatedStudentException("ya existe un estudiante con el login: " + student.getLogin());
        }
        
        //crea el estudiante
        return studentRepository.save(student);
    }
}
