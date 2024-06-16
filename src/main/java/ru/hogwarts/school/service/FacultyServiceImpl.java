package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exeption.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty add(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Student get(Long id) {
        return facultyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Faculty update(Faculty faculty) {
        if (faculties.containsKey(faculty.getId())) {
            return faculties.put(faculty.getId(), faculty);
        }
        throw new EntityNotFoundException();
    }

    @Override
    public Faculty remove(Long id) {
        if (faculties.containsKey(id)) {
            return faculties.remove(id);
        }
        throw new EntityNotFoundException();
    }

    @Override
    public Collection<Faculty> getByColor(String color) {
        return faculties.values().stream()
                .filter(s -> s.getColor().equals(color))
                .collect(Collectors.toList());
    }


    @Override
    public Collection<Faculty> getAll() {
        return faculties.values();
    }

    @Override
    public List<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    @Override
    public List<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color) {
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
    }

    @Override
    public List<Student> getStudentByFaculty(Long Id) {
        Faculty faculty = facultyRepository.findById(Id).orElseThrow();
        return faculty.getStudents();
    }

}
