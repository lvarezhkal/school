package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@Service
public interface FacultyService {

    Faculty add(Faculty faculty);

    Student get(Long id);

    Faculty update(Faculty faculty);

    Faculty remove(Long id);

    Collection<Faculty> getByColor(String color);

    Collection<Faculty> getAll();
}
