package ru.hogwarts.school;



import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public class ConstantsForTests {


    public static final Student MARIIA = new Student("Mariia", 35);
    public static final Student IVAN = new Student("Ivan", 60);
    public static final Student IVAN_EXPECTED = new Student(1L, "Ivan", 60);
    public static final Student MARIIA_EXPECTED = new Student(3L, "Mariia", 35);
    public static final Student TATYANA = new Student("Tatyana", 50);
    public static final Student TATYANA_EXPECTED = new Student(4L, "Tatyana", 50);
    public static final Student ALEX = new Student("Alex", 40);
    public static final Student ALEX_EXPECTED = new Student(1L, "Alex", 40);
    public static final Student SERGEY = new Student("Sergey", 30);
    public static final Student SERGEY_EXPECTED = new Student(2L, "Sergey", 30);
    public static final Student OLGA = new Student(1L, "Olga", 41);

    public static final List<Student> STUDENTS = List.of(ALEX_EXPECTED, SERGEY_EXPECTED, MARIIA_EXPECTED, TATYANA_EXPECTED, IVAN_EXPECTED);

    public static final Faculty POLYTECHNIC = new Faculty(1L, "polytechnic", "Black");
    public static final Faculty BIOLOGY = new Faculty("Biology", "Green");
    public static final Faculty BIOLOGY_EXPECTED = new Faculty(1L, "Biology", "Green");
    public static final Faculty PHILOLOGY = new Faculty("Philology", "Red");
    public static final Faculty PHILOLOGY_EXPECTED = new Faculty(1L, "Philology", "Red");
    public static final Faculty ECONOMICS_EXPECTED = new Faculty(3L, "Economics", "Pink");
    public static final Faculty CHEMICAL = new Faculty("Chemical", "Blue");
    public static final Faculty CHEMICAL_EXPECTED = new Faculty(4L, "Chemical", "Blue");
    public static final Faculty MATHEMATICS = new Faculty("Mathematics", "Grey");
    public static final Faculty MATHEMATICS_EXPECTED = new Faculty(2L, "Mathematics", "Grey");
    public static final Faculty ECONOMICS = new Faculty("Economics", "Pink");

    public static final List<Faculty> FACULTIES = List.of(BIOLOGY_EXPECTED, MATHEMATICS_EXPECTED, ECONOMICS_EXPECTED, CHEMICAL_EXPECTED, PHILOLOGY_EXPECTED);
}