package uniandes.dse.examen1.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class StudentEntity {

    @PodamExclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The login of a student. It should be unique.
     */
    private String login;

    /**
     * The name of the student
     */
    private String name;

    /**
     * Student's code
     */
    private String code;

    /**
     * Courses the student has taken
     */
    @PodamExclude
    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<CourseEntity> courses = new ArrayList<>();

    /**
     * Records of the student
     */
    @PodamExclude
    @OneToMany(mappedBy = "student")
    private List<RecordEntity> records = new ArrayList<>();

    /**
     * A list of all the courses that the student has ever taken. No course should
     * appear more than once in this list.
     */
    // TODO
}
