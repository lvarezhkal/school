package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Faculty, Long> {
    List<Student> findByAgeBetween(int max, int min);

    Faculty findFacultyOfStudent(long id);


    @Query(value = "SELECT count(*)  FROM Student", nativeQuery = true)
    Integer getAllStudents();

    @Query(value = "SELECT  avg(age)  FROM Student", nativeQuery = true)
    Integer getAvgStudents();

    @Query(value = "SELECT * FROM Student ORDER BY id ASC LIMIT 5 ", nativeQuery = true)
    List<Student> getLastStudents();
}
