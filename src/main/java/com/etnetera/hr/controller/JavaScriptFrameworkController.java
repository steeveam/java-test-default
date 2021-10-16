package com.etnetera.hr.controller;

import com.etnetera.hr.data.JavaScriptFrameworkDTO;
import com.etnetera.hr.service.search.SearchCriteria;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.hr.data.JavaScriptFramework;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple REST controller for accessing application logic.
 *
 * @author Etnetera
 */
@RestController
public class JavaScriptFrameworkController {

    private final JavaScriptFrameworkService service;

    @Autowired
    public JavaScriptFrameworkController(JavaScriptFrameworkService service) {
        this.service = service;
    }

    @GetMapping("/frameworks")
    public Iterable<JavaScriptFramework> searchFrameworks(@RequestParam(name = "search", required = false) String search) {
        if (search == null) {
            return service.findAll();
        }

        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        if (search != null) {   //TODO: remove
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1),
                        matcher.group(2), matcher.group(3)));
            }
        }
        return service.searchFrameworks(params);
    }

    @PostMapping("/frameworks")
    public JavaScriptFramework addFramework(@RequestBody JavaScriptFrameworkDTO dto) {
        return service.addFramework(dto);
    }

    @PutMapping("/frameworks")
    public JavaScriptFramework editFramework(@RequestBody JavaScriptFrameworkDTO dto) {
        return service.editFramework(dto);
    }

    @DeleteMapping("/frameworks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFramework(@PathVariable Long id) {
        service.deleteFramework(id);
    }

}
