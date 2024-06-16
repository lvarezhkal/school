package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarServiceImpl;
import ru.hogwarts.school.service.FacultyServiceImpl;
import ru.hogwarts.school.service.StudentServiceImpl;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.hogwarts.school.ConstantsForTests.*;


@WebMvcTest(FacultyController.class)
@ActiveProfiles("home")
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private StudentServiceImpl studentService;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @SpyBean
    private AvatarServiceImpl avatarService;

    @InjectMocks
    private FacultyController facultyController;

    private String url = "http://localhost/faculty";

    @Test
    void addTest() throws Exception {

        Faculty expected = BIOLOGY_EXPECTED;

        when(facultyRepository.save(any(Faculty.class))).thenReturn(BIOLOGY_EXPECTED);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(BIOLOGY_EXPECTED));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(getJsonObjectFaculty(expected))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.color").value(expected.getColor()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("id", String.valueOf(expected.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.color").value(expected.getColor()));
    }

    @Test
    void getAll() throws Exception {
        when(facultyRepository.findAll()).thenReturn(FACULTIES);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(FACULTIES.get(0)))
                .andExpect(jsonPath("$[1]").value(FACULTIES.get(1)))
                .andExpect(jsonPath("$[2]").value(FACULTIES.get(2)))
                .andExpect(jsonPath("$[3]").value(FACULTIES.get(3)))
                .andExpect(jsonPath("$[4]").value(FACULTIES.get(4)));
    }

    @Test
    void getByIdTest() throws Exception {
        Faculty expected = MATHEMATICS_EXPECTED;
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(MATHEMATICS_EXPECTED));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("id", String.valueOf(expected.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.color").value(expected.getColor()));
    }

    @Test
    void shouldReturnNotFoundById() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenThrow(FacultyNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("id", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getByNameTest() throws Exception {
        String substring = "gy";
        List<Faculty> facultyList = FACULTIES.stream().filter(s -> s.getName().toLowerCase().contains(substring.toLowerCase())).toList();

        when(facultyRepository.findByNameContainsIgnoreCase(substring)).thenReturn(facultyList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("name", substring)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$[0]").value(BIOLOGY_EXPECTED))
                .andExpectAll(jsonPath("$[1]").value(PHILOLOGY_EXPECTED));
    }

    @Test
    void shouldReturnNotFoundByName() throws Exception {
        String substring = "rrr";

        when(facultyRepository.findByNameContainsIgnoreCase(substring)).thenThrow(FacultyNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("name", substring)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByColorTest() throws Exception {
        String color = "ed";
        List<Faculty> facultyList = FACULTIES.stream().filter(s -> s.getColor().toLowerCase().contains(color.toLowerCase())).toList();
        when(facultyRepository.findByColorContainsIgnoreCase(color)).thenReturn(facultyList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("color", color)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$[0]").value(PHILOLOGY_EXPECTED));
    }

    @Test
    void shouldReturnNotFoundByColor() throws Exception {
        String color = "51";
        when(facultyRepository.findByColorContainsIgnoreCase(color)).thenThrow(FacultyNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .param("color", color)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTest() throws Exception {
        Faculty expected = PHILOLOGY_EXPECTED;
        String jsonFaculty = getJsonObjectFaculty(expected);
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(facultyRepository.save(expected)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(jsonFaculty)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.color").value(expected.getColor()));
    }

    @Test
    void shouldReturnNotFoundExceptionByUpdate() throws Exception {
        Faculty expected = MATHEMATICS_EXPECTED;
        String jsonFaculty = getJsonObjectFaculty(expected);

        when(facultyRepository.save(expected)).thenThrow(FacultyNotFoundException.class);


        mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(jsonFaculty)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Факультет с id=2 не найден"));
    }

    @Test
    void getStudentsByFacultyTest() throws Exception {
        Faculty expected = BIOLOGY_EXPECTED;
        List<Student> list = STUDENTS;
        list.get(0).setFaculty(BIOLOGY_EXPECTED);
        list.get(1).setFaculty(BIOLOGY_EXPECTED);
        list.get(2).setFaculty(MATHEMATICS_EXPECTED);
        list.get(3).setFaculty(ECONOMICS_EXPECTED);
        list.get(4).setFaculty(CHEMICAL_EXPECTED);
        List<Student> studentList = list.stream().filter(s -> s.getFaculty().getId().equals(expected.getId())).toList();

        when(studentRepository.findByFaculty_Id(2L)).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url + "/2/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(list.get(0)))
                .andExpect(jsonPath("$[1]").value(list.get(1)));
    }

    @Test
    void deleteTest() throws Exception {
        Faculty expected = BIOLOGY_EXPECTED;
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(BIOLOGY_EXPECTED));
        doNothing().when(facultyRepository).delete(BIOLOGY_EXPECTED);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(url)
                        .param("id", String.valueOf(expected.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.color").value(expected.getColor()));
    }


    private String getJsonObjectFaculty(Faculty faculty) throws Exception {
        JSONObject jsonFaculty = new JSONObject();
        jsonFaculty.put("id", faculty.getId());
        jsonFaculty.put("name", faculty.getName());
        jsonFaculty.put("color", faculty.getColor());

        return jsonFaculty.toString();
    }
}
