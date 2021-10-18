package com.etnetera.hr.controller;

import com.etnetera.hr.data.JavaScriptFrameworkDTO;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import com.etnetera.hr.service.search.SearchCriteria;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.etnetera.hr.service.search.SearchOperation.EQUALS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


/**
 * Controller layer JUnit/partial IT tests.
 *
 * @author Stefan Marcin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JavaScriptFrameworkControllerTest extends TestCase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaScriptFrameworkService service;

    @Captor
    ArgumentCaptor<List<SearchCriteria>> criteriaCaptor;

    @Captor
    ArgumentCaptor<JavaScriptFrameworkDTO> dtoCaptor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getFrameworksWithoutSearch() throws Exception {
        mockMvc.perform(get("/frameworks")
                .contentType("application/json")).andReturn();

        verify(service, times(1)).findAll();
        verify(service, times(0)).searchFrameworks(any());
    }

    @Test
    public void getFrameworksWithConstructerSearchCriteria() throws Exception {
        mockMvc.perform(get("/frameworks/?search=name:John")
                .contentType("application/json")).andReturn();

        verify(service, times(0)).findAll();
        verify(service, times(1)).searchFrameworks(criteriaCaptor.capture());
        assertEquals(EQUALS, criteriaCaptor.getValue().get(0).getOperation());
        assertEquals("name", criteriaCaptor.getValue().get(0).getKey());
        assertEquals("John", criteriaCaptor.getValue().get(0).getValue());
    }

    @Test
    public void testAddFramework() throws Exception {
        JavaScriptFrameworkDTO framework = new JavaScriptFrameworkDTO("jQuery");

        mockMvc.perform(post("/frameworks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(framework))).andReturn();

        verify(service, times(1)).addFramework(dtoCaptor.capture());
        assertEquals(framework.getName(), dtoCaptor.getValue().getName());
    }

    @Test
    public void testEditFramework() throws Exception {
        JavaScriptFrameworkDTO framework = new JavaScriptFrameworkDTO(1L, "jQuery", null, null, null);

        mockMvc.perform(post("/frameworks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(framework))).andReturn();

        verify(service, times(1)).addFramework(dtoCaptor.capture());
        assertEquals(framework.getId(), dtoCaptor.getValue().getId());
        assertEquals(framework.getName(), dtoCaptor.getValue().getName());
    }

    @Test
    public void testDeleteFramework() throws Exception {
        mockMvc.perform(delete("/frameworks/123")).andReturn();

        verify(service, times(1)).deleteFramework(eq(123L));
    }
}