package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    //private AutoCloseable autoCloseable;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        //autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        //autoCloseable.close();
    }

    @Test
    void getAllStudents() {
        //when
        underTest.getAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    //Este ja usa um metodo ja testado na interface se existe email
    @Test
    void canAddStudent() {
        //given
        String email = "jamila@gmail.com";
        Student student = new Student(
                "Jamila",
                email,
                Gender.FEMALE
        );

        //when
        underTest.addStudent(student);

        //then
            //ao capturarmos os estudante
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

            //verificamos se este existe repo dado a captura do estudante realizado
        verify(studentRepository).save(studentArgumentCaptor.capture());

            //obtemos o objecto
        Student capturedStudent = studentArgumentCaptor.getValue();

           //comparamos se o estudante capturado é igual ao estudante que quisemos adicionar
        assertThat(capturedStudent).isEqualTo(student);

    }

    @Test
    void addStudentThrowsException() {
        //given
        String email = "jamila@gmail.com";
        Student student = new Student(
                "Jamila",
                email,
                Gender.FEMALE
        );

        given(studentRepository.selectExistsEmail(anyString())) //student.getEmail()))
                .willReturn(true);

        //when
        //then
        assertThatThrownBy(()-> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");

        verify(studentRepository, never()).save(any());
    }





    @Test
    void deleteStudent() {
        // given - temos de dizer ao repo que existe um objecto via id = 10 e que retorna true
        long id = 10;
        given(studentRepository.existsById(id))
                .willReturn(true);

        // when - apaga com base no id que recebe
        underTest.deleteStudent(id);

        // then - verifica se existe no repo, porque num usa o metodo e noutro compara se tem o mesmo comportamento se
        //        usando o mock do deeteById
        verify(studentRepository).deleteById(id);

    }
}