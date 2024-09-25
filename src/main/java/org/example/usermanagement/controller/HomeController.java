package org.example.usermanagement.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Connection;
import java.sql.SQLException;

@Controller
public class HomeController {

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.driver-class-name}")
    private String dbType;

    public HomeController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/")
    public String home(Model model) {
        boolean connectionStatus = false;
        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
            connectionStatus = !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        model.addAttribute("dbIp", extractIpFromUrl(dbUrl));
        model.addAttribute("dbType", dbType);
        model.addAttribute("connectionStatus", connectionStatus ? "Connected" : "Disconnected");

        return "index";  // This refers to index.html under src/main/resources/templates/
    }

    private String extractIpFromUrl(String url) {
        // Assumes database URL is in the format jdbc:postgresql://<IP>:<port>/<dbname>
        String[] parts = url.split("//");
        if (parts.length > 1) {
            return parts[1].split(":")[0];
        }
        return "Unknown";
    }
}