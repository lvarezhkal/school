package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;


import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.hogwarts.school.ConstantsForTests.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("home")
class StudentControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String url;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/student";
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void addTest() {
        Student expected = ALEX;
        Student actual = testRestTemplate.postForObject(url, expected, Student.class);
        expected.setId(actual.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllTest() {
        Student added = testRestTemplate.postForObject(url, SERGEY, Student.class);
        String actual = testRestTemplate.getForObject(url, String.class);

        assertThat(actual).contains(
                "[",
                "]",
                String.format("{\"id\":%d,\"name\":\"%s\",\"age\":%d,\"faculty\":%s}", added.getId(), added.getName(), added.getAge(), added.getFaculty())
        );
    }

    @Test
    void getByIdTest() {
        Student added = testRestTemplate.postForObject(url, MARIIA, Student.class);
        String actual = testRestTemplate.getForObject(url + "?id=" + added.getId(), String.class);

        assertThat(actual).isEqualTo(String.format("{\"id\":%d,\"name\":\"%s\",\"age\":%d,\"faculty\":%s}", added.getId(), added.getName(), added.getAge(), added.getFaculty()));
    }

    @Test
    void getByNameTest() {
        Student added = testRestTemplate.postForObject(url, ALEX, Student.class);
        String actual = testRestTemplate.getForObject(url + "?name=" + added.getName(), String.class);

        assertThat(actual).contains(
                "[",
                "]",
                String.format("{\"id\":%d,\"name\":\"%s\",\"age\":%d,\"faculty\":%s}", added.getId(), added.getName(), added.getAge(), added.getFaculty()));
    }

    @Test
    void getByAgeTest() {
        Student added = testRestTemplate.postForObject(url, SERGEY, Student.class);
        String actual = testRestTemplate.getForObject(url + "?age=" + added.getAge(), String.class);

        assertThat(actual).contains(
                "[",
                "]",
                String.format("{\"id\":%d,\"name\":\"%s\",\"age\":%d,\"faculty\":%s}", added.getId(), added.getName(), added.getAge(), added.getFaculty()));
    }

    @Test
    void getByAgeBetweenTest() {
        Student added = testRestTemplate.postForObject(url, TATYANA, Student.class);
        String actual = testRestTemplate.getForObject(url + "?minAge=" + (added.getAge() - 5) + "&maxAge=" + (added.getAge() + 5), String.class);

        assertThat(actual).contains(
                "[",
                "]",
                String.format("{\"id\":%d,\"name\":\"%s\",\"age\":%d,\"faculty\":%s}", added.getId(), added.getName(), added.getAge(), added.getFaculty()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForShouldReturnBadRequest")
    void shouldReturnBadRequest(String minAge, String maxAge, String expected) {
        url = url + "?";
        if (minAge != null && maxAge != null) {
            url = url + minAge + "&" + maxAge;
        }
        if (minAge == null && maxAge != null) {
            url = url + maxAge;
        }
        if (minAge != null && maxAge == null) {
            url = url + minAge;
        }
        String response = testRestTemplate.getForObject(url, String.class);
        assertThat(response).isEqualTo(expected);
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
    void shouldReturnNotFoundExceptionByUpdate() {
        try {
            studentController.delete(1L);
        } catch (Exception e) {
            Student updated = new Student(1L, "Roman", 53);

            ResponseEntity<String> response = testRestTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    new HttpEntity<>(updated),
                    String.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    void updateTest() {
        ResponseEntity<Student> response = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(ALEX),
                Student.class
        );
        SERGEY.setId(response.getBody().getId());

        ResponseEntity<Student> actual = testRestTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(SERGEY),
                Student.class
        );

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo(SERGEY);
    }

    @Test
    void deleteTest() {
        ResponseEntity<Student> response = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(TATYANA),
                Student.class
        );
        Long id = response.getBody().getId();

        testRestTemplate.delete(url + "?id=" + id);
        String actual = testRestTemplate.getForObject(url + "?id=" + id, String.class);

        assertThat(actual).contains("Студент с id=" + id + " не найден");
    }

    @Test
    void getFacultyTest() {
        Faculty[] faculties = testRestTemplate.getForObject("http://localhost:" + port + "/faculty", Faculty[].class);
        TATYANA.setFaculty(faculties[0]);

        ResponseEntity<Student> response = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(TATYANA),
                Student.class
        );

        Faculty actual = testRestTemplate.getForObject(
                url + "/" + response.getBody().getId() + "/faculty",
                Faculty.class);
        assertThat(actual).isEqualTo(faculties[0]);
    }
}
