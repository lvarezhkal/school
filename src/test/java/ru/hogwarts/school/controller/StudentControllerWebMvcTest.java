package ru.hogwarts.school.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarServiceImpl;
import ru.hogwarts.school.service.FacultyServiceImpl;
import ru.hogwarts.school.service.StudentServiceImpl;


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.hogwarts.school.ConstantsForTests.*;


@WebMvcTest(StudentController.class)
@ActiveProfiles("home")
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentServiceImpl studentService;

    @SpyBean
    private AvatarServiceImpl avatarService;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @InjectMocks
    private StudentController studentController;

    private String url = "http://localhost/student";

    @Test
    void addTest() throws Exception {
        Student expected = MARIIA_EXPECTED;
        expected.setFaculty(BIOLOGY_EXPECTED);
        String jsonStudent = getJsonObjectStudent(expected);

        when(studentRepository.save(any(Student.class))).thenReturn(expected);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(jsonStudent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty").value(expected.getFaculty()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("id", String.valueOf(expected.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty").value(expected.getFaculty()));
    }

    @Test
    void getAll() throws Exception {
        when(studentRepository.findAll()).thenReturn(STUDENTS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(STUDENTS.get(0)))
                .andExpect(jsonPath("$[1]").value(STUDENTS.get(1)))
                .andExpect(jsonPath("$[2]").value(STUDENTS.get(2)))
                .andExpect(jsonPath("$[3]").value(STUDENTS.get(3)))
                .andExpect(jsonPath("$[4]").value(STUDENTS.get(4)));
    }

    @Test
    void getByIdTest() throws Exception {
        Student expected = ALEX_EXPECTED;
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("id", String.valueOf(expected.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty").value(expected.getFaculty()));
    }

    @Test
    void shouldReturnNotFoundById() throws Exception {
        Long id = 77L;
        when(studentRepository.findById(id)).thenThrow(StudentNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("id", String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByNameTest() throws Exception {
        String substring = "mAr";
        List<Student> studentList = STUDENTS.stream().filter(s -> s.getName().toLowerCase().contains(substring.toLowerCase())).toList();

        when(studentRepository.findByNameContainsIgnoreCase(any(String.class))).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("name", substring)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$[0]").value(studentList.get(0)));
    }

    @Test
    void shouldReturnNotFoundByName() throws Exception {
        String substring = "rrr";
        List<Student> studentList = STUDENTS.stream().filter(s -> s.getName().toLowerCase().contains(substring.toLowerCase())).toList();

        when(studentRepository.findByNameContainsIgnoreCase(any(String.class))).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("name", substring)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByAgeTest() throws Exception {
        int age = 35;
        List<Student> studentList = STUDENTS.stream().filter(s -> s.getAge() == age).toList();

        when(studentRepository.findByAge(any(Integer.class))).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("age", String.valueOf(age))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$[0]").value(MARIIA_EXPECTED));
    }

    @Test
    void shouldReturnNotFoundByAge() throws Exception {
        int age = 51;
        List<Student> studentList = STUDENTS.stream().filter(s -> s.getAge() == age).toList();

        when(studentRepository.findByAge(any(Integer.class))).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("age", String.valueOf(age))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByAgeBetweenTest() throws Exception {
        int minAge = 40;
        int maxAge = 50;
        List<Student> studentList = STUDENTS.stream().filter(s -> s.getAge() >= minAge && s.getAge() <= maxAge).toList();

        when(studentRepository.findByAgeBetween(any(Integer.class), any(Integer.class))).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("minAge", String.valueOf(minAge))
                        .param("maxAge", String.valueOf(maxAge))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(studentList.get(0)))
                .andExpect(jsonPath("$[1]").value(studentList.get(1)));
    }

    @Test
    void shouldReturnNotFoundByAgeBetween() throws Exception {
        int minAge = 10;
        int maxAge = 15;
        List<Student> studentList = STUDENTS.stream().filter(s -> s.getAge() >= minAge && s.getAge() <= maxAge).toList();

        when(studentRepository.findByAgeBetween(any(Integer.class), any(Integer.class))).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("minAge", String.valueOf(minAge))
                        .param("maxAge", String.valueOf(maxAge))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("provideParamsForShouldReturnBadRequest")
    void shouldReturnBadRequestByAgeBetween(String minAge, String maxAge, String expected) throws Exception {
        url = url + '?';
        if (minAge != null && maxAge != null) {
            url = url + minAge + "&" + maxAge;
        }
        if (minAge == null && maxAge != null) {
            url = url + maxAge;
        }
        if (minAge != null && maxAge == null) {
            url = url + minAge;
        }

        when(studentRepository.findByAgeBetween(any(Integer.class), any(Integer.class))).thenReturn(STUDENTS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(expected));
    }

    public static Stream<Arguments> provideParamsForShouldReturnBadRequest() {
        return Stream.of(
                Arguments.of(null, "maxAge=30", "Один из параметров введён не корректно"),
                Arguments.of("minAge=30", null, "Один из параметров введён не корректно"),
                Arguments.of("minAge=30", "maxAge=-5", "Один из параметров введён не корректно"),
                Arguments.of("minAge=-5", "maxAge=30", "Один из параметров введён не корректно"),
                Arguments.of(null, "maxAge=-5", "Один из параметров введён не корректно"),
                Arguments.of("minAge=-5", null, "Один из параметров введён не корректно")
        );
    }

    @Test
    void updateTest() throws Exception {
        Student expected = OLGA;
        expected.setFaculty(POLYTECHNIC);
        String jsonStudent = getJsonObjectStudent(expected);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(studentRepository.save(any(Student.class))).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(jsonStudent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty").value(expected.getFaculty()));
    }

    @Test
    void shouldReturnNotFoundExceptionByUpdate() throws Exception {
        Student expected = ALEX_EXPECTED;
        expected.setFaculty(ECONOMICS_EXPECTED);
        String jsonStudent = getJsonObjectStudent(expected);

        when(studentRepository.save(any(Student.class))).thenThrow(StudentNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(jsonStudent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Студент с id=1 не найден"));
    }

    @Test
    void getFacultyOfStudent() throws Exception {
        OLGA.setFaculty(POLYTECHNIC);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(OLGA));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost/student/1/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(POLYTECHNIC.getId()))
                .andExpect(jsonPath("$.name").value(POLYTECHNIC.getName()))
                .andExpect(jsonPath("$.color").value(POLYTECHNIC.getColor()));
    }

    @Test
    void deleteTest() throws Exception {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(OLGA));
        doNothing().when(studentRepository).delete(any(Student.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("http://localhost/student")
                        .param("id", String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(OLGA.getId()))
                .andExpect(jsonPath("$.name").value(OLGA.getName()))
                .andExpect(jsonPath("$.age").value(OLGA.getAge()))
                .andExpect(jsonPath("$.faculty").value(OLGA.getFaculty()));

    }

    private String getJsonObjectStudent(Student student) throws JSONException {
        JSONObject jsonFaculty = new JSONObject();
        jsonFaculty.put("id", student.getFaculty().getId());
        jsonFaculty.put("name", student.getFaculty().getName());
        jsonFaculty.put("color", student.getFaculty().getColor());
        JSONObject jsonStudent = new JSONObject();
        jsonStudent.put("id", student.getId());
        jsonStudent.put("name", student.getName());
        jsonStudent.put("age", student.getAge());
        jsonStudent.put("faculty", jsonFaculty);

        return jsonStudent.toString();
    }
}
