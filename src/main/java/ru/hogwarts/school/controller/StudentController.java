package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Создание студентов")
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student addedStudent = service.add(student);
        return ResponseEntity.ok(addedStudent);
    }

    @PutMapping
    @Operation(summary = "Обновление студентов")
    public ResponseEntity<Student> update(@RequestBody Student student) {
        Student updatedStudent = service.update(student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удаление студентов")
    public ResponseEntity<Student> remove(@PathVariable Long id) {
        Student deletedStudent = service.remove(id);
        return ResponseEntity.ok(deletedStudent);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получение студентов по id")
    public ResponseEntity<Student> get(@PathVariable Long id) {
        Student student = service.get(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("by-age")
    @Operation(summary = "Получение студентов по возрасту")
    public ResponseEntity<Collection<Student>> getByAge(@RequestParam Integer age) {
        Collection<Student> students = service.getByAge(age);
        return ResponseEntity.ok(students);
    }

    @GetMapping("all")
    @Operation(summary = "Получение всех студентов")
    public ResponseEntity<Collection<Student>> getAll() {
        Collection<Student> students = service.getAll();
        return ResponseEntity.ok(students);
    }

    @GetMapping ("/findStudentByAgeBetween")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam(required = true) int max, @RequestParam(required = true) int min) {
        List<Student> studentToFind = StudentService.findByAgeBetween(min, max);
        if (studentToFind == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentToFind);
    }

    @GetMapping("/facultyOfStudent")
    public ResponseEntity <Faculty> findFacultyOfStudent(@RequestParam long id) {
        Faculty facultyToFind = StudentService.findFacultyOfStudent(id);
        if (facultyToFind == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyToFind);
    }

    @GetMapping("/allNumber")
    public Integer getAllStudentsBySchool() {
        return studentService.getAllStudents();
    }

    @GetMapping("/averageAge")
    Integer getAvgStudents() {
        return studentService.getAvgStudents();
    }

    @GetMapping("/last5Id")
    List<Student> getLastStudents() {
        return studentService.getLastStudents();
    }
}
