package com.bookmyshow.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mysql")
public class MySqlController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/query")
    public ResponseEntity<?> executeQuery(@RequestParam("query") String query) {
        // For safety, this example only allows SELECT queries.
        // A true "universal" runner would be a major security risk.
        if (!query.trim().toLowerCase().startsWith("select")) {
            return new ResponseEntity<>("Only SELECT queries are allowed for security reasons.", HttpStatus.FORBIDDEN);
        }

        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
