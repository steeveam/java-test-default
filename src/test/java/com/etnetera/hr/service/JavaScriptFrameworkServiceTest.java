package com.etnetera.hr.service;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkDTO;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.service.search.JavaScriptFrameworkSpecification;
import com.etnetera.hr.service.search.SearchCriteria;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Service layer JUnit tests.
 *
 * @author Stefan Marcin
 */
public class JavaScriptFrameworkServiceTest extends TestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    JavaScriptFrameworkRepository repositoryMock = mock(JavaScriptFrameworkRepository.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    JavaScriptFrameworkService service;


    @Override
    protected void setUp() {
        service = new JavaScriptFrameworkService(repositoryMock);
    }

    public void testFindAll() {
        service.findAll();

        verify(repositoryMock, times(1)).findAll();
    }

    public void testAddFramework() {
        String expectedName = "BackboneJs";
        Integer expectedHypeLevel = 123;
        String expectedVersion = "12.3";
        String expectedDate = "2001-09-11";
        Long notExpectedId = 1L;
        JavaScriptFrameworkDTO frameworkDTO = new JavaScriptFrameworkDTO(notExpectedId, expectedName, expectedVersion, expectedDate, expectedHypeLevel);
        ArgumentCaptor<JavaScriptFramework> frameworkCaptor = ArgumentCaptor.forClass(JavaScriptFramework.class);

        service.addFramework(frameworkDTO);

        verify(repositoryMock, times(1)).save(frameworkCaptor.capture());
        assertEquals(expectedName, frameworkCaptor.getValue().getName());
        assertEquals(expectedHypeLevel, frameworkCaptor.getValue().getHypeLevel());
        assertEquals(expectedVersion, frameworkCaptor.getValue().getVersion());
        assertEquals(expectedDate, dateFormat.format(frameworkCaptor.getValue().getDeprecationDate()));
        assertNull(frameworkCaptor.getValue().getId());
    }

    public void testEditFramework() {
        String expectedName = "Ember";
        Integer expectedHypeLevel = 13;
        String expectedVersion = "1.3";
        String expectedDate = "2002-09-11";
        Long id = 3L;
        JavaScriptFrameworkDTO frameworkDTO = new JavaScriptFrameworkDTO(id, expectedName, expectedVersion, expectedDate, expectedHypeLevel);
        JavaScriptFramework existingFramework = new JavaScriptFramework(id, expectedName, expectedVersion, null, expectedHypeLevel);
        when(repositoryMock.findById(eq(id))).thenReturn(of(existingFramework));
        ArgumentCaptor<JavaScriptFramework> frameworkCaptor = ArgumentCaptor.forClass(JavaScriptFramework.class);

        service.editFramework(frameworkDTO);

        verify(repositoryMock, times(1)).save(frameworkCaptor.capture());
        assertEquals(id, frameworkCaptor.getValue().getId());
        assertEquals(expectedName, frameworkCaptor.getValue().getName());
        assertEquals(expectedHypeLevel, frameworkCaptor.getValue().getHypeLevel());
        assertEquals(expectedVersion, frameworkCaptor.getValue().getVersion());
        assertEquals(expectedDate, dateFormat.format(frameworkCaptor.getValue().getDeprecationDate()));
    }

    public void testEditNonexistingFramework() {
        Long id = 3L;
        JavaScriptFrameworkDTO frameworkDTO = new JavaScriptFrameworkDTO(id, "Ember", "1.3", "2002-09-11", 13);
        when(repositoryMock.findById(eq(id))).thenReturn(Optional.empty());

        try {
            service.editFramework(frameworkDTO);
            fail("ResponseStatusException not thrown.");
        } catch (ResponseStatusException ex) {
            assertEquals("404 NOT_FOUND \"Framework with id 3 not found.\"", ex.getMessage());
            verify(repositoryMock, times(0)).save(any());
        }
    }

    public void testDeleteFramework() {
        JavaScriptFramework expectedJsFramework = new JavaScriptFramework("BackboneJs");
        when(repositoryMock.findById(eq(1L))).thenReturn(of(expectedJsFramework));

        service.deleteFramework(1L);

        verify(repositoryMock, times(1)).findById(eq(1L));
        verify(repositoryMock, times(1)).delete(eq(expectedJsFramework));
    }

    public void testDeleteNotExistingFramework() {
        when(repositoryMock.findById(eq(1L))).thenReturn(Optional.empty());

        try {
            service.deleteFramework(1L);
            fail("ResponseStatusException not thrown.");
        } catch (ResponseStatusException ex) {
            assertEquals("404 NOT_FOUND \"Framework with id 1 not found.\"", ex.getMessage());
        }

    }

    public void testSearchFrameworks() {
        ArrayList<SearchCriteria> criteria = new ArrayList<>();
        SearchCriteria criterion1 = new SearchCriteria("name", "@", "Js");
        SearchCriteria criterion2 = new SearchCriteria("hypeLevel", ">", 12);
        criteria.add(criterion1);
        criteria.add(criterion2);
        ArgumentCaptor<JavaScriptFrameworkSpecification> specCaptor = ArgumentCaptor.forClass(JavaScriptFrameworkSpecification.class);

        service.searchFrameworks(criteria);

        verify(repositoryMock, times(1)).findAll(specCaptor.capture());
        assertEquals(criterion1, specCaptor.getValue().getList().get(0));
        assertEquals(criterion2, specCaptor.getValue().getList().get(1));
    }
}