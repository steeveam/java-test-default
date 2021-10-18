package com.etnetera.hr;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Class used for Spring Boot/MVC based tests.
 *
 * @author Etnetera
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JavaScriptFrameworkTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JavaScriptFrameworkRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void addingFrameworkIT() throws Exception {
        JavaScriptFramework framework = new JavaScriptFramework("VueJs");

        MvcResult result = mockMvc.perform(post("/frameworks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(framework))).andReturn();

        assertEquals(OK.value(), result.getResponse().getStatus());
        assertEquals(
                framework.getName(),
                objectMapper.readValue(result.getResponse().getContentAsString(), JavaScriptFramework.class).getName());
    }

    @Test
    public void listingFrameworksIT() throws Exception {
        repository.save(new JavaScriptFramework("ReactJs"));
        repository.save(new JavaScriptFramework("VueJs"));

        MvcResult result = mockMvc.perform(get("/frameworks")
                .contentType("application/json")).andReturn();

        assertEquals(OK.value(), result.getResponse().getStatus());
        List<JavaScriptFramework> returnedFrameworkList = readFrameworks(result);
        assertEquals(2, returnedFrameworkList.size());
        assertEquals("ReactJs", returnedFrameworkList.get(0).getName());
        assertEquals("VueJs", returnedFrameworkList.get(1).getName());
    }

    @Test
    public void changeFrameworkIT() throws Exception {
        repository.save(new JavaScriptFramework("VueJs"));
        JavaScriptFramework changeCandidate = repository.save(new JavaScriptFramework("ReactJs"));
        JavaScriptFramework targetNamedFramework = new JavaScriptFramework("React Native");
        targetNamedFramework.setId(changeCandidate.getId());

        MvcResult putResult = mockMvc.perform(put("/frameworks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(targetNamedFramework))).andReturn();

        MvcResult listResult = mockMvc.perform(get("/frameworks/")).andReturn();

        assertEquals(OK.value(), putResult.getResponse().getStatus());
        assertEquals(targetNamedFramework.getName(),
                readFrameworks(listResult).stream().filter(a -> changeCandidate.getId().equals(a.getId())).findFirst().get().getName());
    }

    @Test
    public void deleteFrameworkIT() throws Exception {
        JavaScriptFramework deleteCandidate = repository.save(new JavaScriptFramework("ReactJs"));
        repository.save(new JavaScriptFramework("VueJs"));

        MvcResult deleteResult = mockMvc.perform(delete("/frameworks/" + deleteCandidate.getId())).andReturn();
        MvcResult listResult = mockMvc.perform(get("/frameworks/")).andReturn();

        assertEquals(NO_CONTENT.value(), deleteResult.getResponse().getStatus());
        assertEquals(1, readFrameworks(listResult).size());
    }

    @Test
    public void searchFrameworks() throws Exception {
        repository.save(new JavaScriptFramework("ReactJs"));
        repository.save(new JavaScriptFramework("React Native"));
        repository.save(new JavaScriptFramework("Vue Js"));
        repository.save(new JavaScriptFramework("Mithril"));
        repository.save(new JavaScriptFramework("jQuery"));

        MvcResult result = mockMvc.perform(get("/frameworks/?search=name@React")
                .contentType("application/json")).andReturn();

        assertEquals(OK.value(), result.getResponse().getStatus());
        List<JavaScriptFramework> returnedFrameworkList = readFrameworks(result);
        assertEquals(2, returnedFrameworkList.size());
        assertEquals("ReactJs", returnedFrameworkList.get(0).getName());
        assertEquals("React Native", returnedFrameworkList.get(1).getName());
    }

    private List<JavaScriptFramework> readFrameworks(MvcResult result) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<JavaScriptFramework>>() {
                });
    }

}
