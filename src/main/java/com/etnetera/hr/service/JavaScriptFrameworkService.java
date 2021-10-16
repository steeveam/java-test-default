package com.etnetera.hr.service;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkDTO;
import com.etnetera.hr.service.search.SearchCriteria;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository repository;
    private final String datePattern = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    @Autowired
    public JavaScriptFrameworkService(JavaScriptFrameworkRepository repository) {
        this.repository = repository;
    }

    public Iterable<JavaScriptFramework> findAll() {
        return repository.findAll();
    }

    public JavaScriptFramework addFramework(JavaScriptFrameworkDTO dto) {
        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setName(dto.getName());
        framework.setDeprecationDate(convertToDate(dto.getDeprecationDate()));
        framework.setVersion(dto.getVersion());
        framework.setHypeLevel(dto.getHypeLevel());
        return repository.save(framework);
    }

    public JavaScriptFramework editFramework(JavaScriptFrameworkDTO dto) {
        JavaScriptFramework framework = findById(dto.getId());
        framework.setName(dto.getName());
        framework.setVersion(dto.getVersion());
        framework.setDeprecationDate(convertToDate(dto.getDeprecationDate()));
        framework.setHypeLevel(dto.getHypeLevel());
        return repository.save(framework);
    }

    public void deleteFramework(Long id) {
        JavaScriptFramework framework = findById(id);
        repository.delete(framework);
    }

    private JavaScriptFramework findById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing framework id");
        }
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Framework with id " + id + " not found."));
    }

    private Date convertToDate(String stringDate) {
        if (stringDate == null) return null;
        try {
            return new Date(dateFormat.parse(stringDate).getTime());
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format, expected: " + datePattern);
        }
    }

    public Iterable<JavaScriptFramework> searchFrameworks(List<SearchCriteria> params) {
        System.out.println(params);
        //TODO build predicates and filter based on them
        return repository.findAll();
    }
}
