package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Service
public interface FacultyService {
    Faculty add(Faculty faculty);

    Student get(Long id);

    Faculty update(Faculty faculty);

    Faculty remove(Long id);

    Collection<Faculty> getByColor(String color);

    Collection<Faculty> getAll();

    List<Faculty> getFacultiesByColor(String color);

    List<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color);

    Collection<Student> getStudentByFaculty (long id);
}



