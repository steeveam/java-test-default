package com.etnetera.hr.service;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkDTO;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.service.search.JavaScriptFrameworkSpecification;
import com.etnetera.hr.service.search.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository repository;
    private final String datePattern = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    @PostConstruct
    private void loadDb() {
        repository.save(new JavaScriptFramework(1L, "ReactJs", "1", convertToDate("2021-12-12"), 1));
        repository.save(new JavaScriptFramework(2L, "React Native", "2", convertToDate("2021-12-12"), 2));
        repository.save(new JavaScriptFramework(3L, "AngularJs", "3", convertToDate("2021-12-12"), 3));
        repository.save(new JavaScriptFramework(4L, "jQuery", "4", convertToDate("2021-12-12"), 5));
        repository.save(new JavaScriptFramework(5L, "Mithril", "5", convertToDate("2021-12-12"), 3));
        repository.save(new JavaScriptFramework(6L, "Meteor", "6", convertToDate("2021-12-12"), 4));
        repository.save(new JavaScriptFramework(7L, "VueJs", "7", convertToDate("2021-12-12"), 1));
    }

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
        JavaScriptFrameworkSpecification spec = new JavaScriptFrameworkSpecification(params);
        return repository.findAll(spec);
    }
}
