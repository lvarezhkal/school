package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {
    Student add(Student student);

    Student get(Long id);

    Student update(Student student);

    Student remove(Long id);

    Collection<Student> getByAge(Integer age);

    Collection<Student> getAll();

    List<Student> findByAgeBetween(int max, int min);

    Faculty findFacultyOfStudent(long id);

}
