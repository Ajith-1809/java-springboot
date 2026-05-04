package com.employee.backend.service;

import com.employee.backend.model.Employee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import java.util.*;

@Service
public class EmployeeService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate;

    public EmployeeService() {
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    private String getCurrentUser() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "SYSTEM";
        }
    }

    private void logAction(String action, String entity, String entityId) {
        try {
            String url = supabaseUrl + "/rest/v1/audit_logs";
            HttpHeaders headers = createHeaders();
            headers.set("Content-Type", "application/json");

            Map<String, Object> log = new HashMap<>();
            log.put("action", action);
            log.put("entity_name", entity);
            log.put("entity_id", entityId != null ? Long.parseLong(entityId) : null);
            log.put("performed_by", getCurrentUser());
            log.put("timestamp", new java.util.Date().toInstant().toString());

            restTemplate.postForEntity(url, new HttpEntity<>(log, headers), String.class);
        } catch (Exception e) {
            System.err.println("Audit Log Failed: " + e.getMessage());
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        return headers;
    }

    public Page<Employee> getAllEmployees(String status, String search, Pageable pageable) {
        try {
            String url = supabaseUrl + "/rest/v1/employees?select=*";
            
            if (status != null && !status.isEmpty()) {
                url += "&status=eq." + status;
            }
            
            if (search != null && !search.isEmpty()) {
                url += "&or=(employee_id.ilike.*" + search + "*" +
                       ",name.ilike.*" + search + "*" +
                       ",email.ilike.*" + search + "*" +
                       ",department.ilike.*" + search + "*" +
                       ",role.ilike.*" + search + "*" +
                       ",mobile.ilike.*" + search + "*" +
                       ",gender.ilike.*" + search + "*" +
                       ",status.ilike.*" + search + "*)";
            }
            
            String sortProp = "employee_id";
            if (pageable.getSort().isSorted()) {
                String rawProp = pageable.getSort().iterator().next().getProperty();
                if ("employeeId".equals(rawProp)) sortProp = "employee_id";
                else if ("name".equals(rawProp)) sortProp = "name";
                else if ("email".equals(rawProp)) sortProp = "email";
                else sortProp = rawProp;
            }
            String sortDir = pageable.getSort().isSorted() && pageable.getSort().iterator().next().isDescending() ? "desc" : "asc";
            url += "&order=" + sortProp + "." + sortDir;

            HttpHeaders headers = createHeaders();
            headers.set("Range", "0-99");
            
            ResponseEntity<Employee[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Employee[].class);
            List<Employee> list = Arrays.asList(response.getBody() != null ? response.getBody() : new Employee[0]);
            return new PageImpl<>(list, pageable, list.size());
        } catch (Exception e) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    public Employee createEmployee(Employee employee) {
        try {
            String checkUrl = supabaseUrl + "/rest/v1/employees?or=(email.eq." + employee.getEmail() + 
                              ",employee_id.eq." + employee.getEmployeeId() + ")&select=id";
            HttpHeaders headers = createHeaders();
            
            ResponseEntity<List> checkRes = restTemplate.exchange(checkUrl, HttpMethod.GET, new HttpEntity<>(headers), List.class);
            if (checkRes.getBody() != null && !checkRes.getBody().isEmpty()) {
                throw new RuntimeException("Duplicate entry detected.");
            }

            String url = supabaseUrl + "/rest/v1/employees";
            headers.set("Content-Type", "application/json");
            headers.set("Prefer", "return=representation");

            Map<String, Object> body = createBodyMap(employee);
            ResponseEntity<Employee[]> response = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), Employee[].class);
            
            Employee saved = response.getBody()[0];
            logAction("CREATE", "Employee", saved.getId().toString());
            return saved;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Employee updateEmployee(Long id, Employee details) {
        try {
            String url = supabaseUrl + "/rest/v1/employees?id=eq." + id;
            HttpHeaders headers = createHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Prefer", "return=representation");

            Map<String, Object> body = createBodyMap(details);
            ResponseEntity<Employee[]> response = restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity<>(body, headers), Employee[].class);
            
            logAction("UPDATE", "Employee", id.toString());
            return response.getBody()[0];
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteEmployee(Long id) {
        try {
            String url = supabaseUrl + "/rest/v1/employees?id=eq." + id;
            HttpHeaders headers = createHeaders();
            restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
            logAction("DELETE", "Employee", id.toString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Page<Map> getAuditLogs(Pageable pageable) {
        try {
            String url = supabaseUrl + "/rest/v1/audit_logs?select=*&order=timestamp.desc";
            HttpHeaders headers = createHeaders();
            headers.set("Range", "0-49");

            ResponseEntity<Map[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map[].class);
            List<Map> list = Arrays.asList(response.getBody() != null ? response.getBody() : new Map[0]);
            return new PageImpl<>(list, pageable, list.size());
        } catch (Exception e) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    private Map<String, Object> createBodyMap(Employee emp) {
        Map<String, Object> body = new HashMap<>();
        body.put("employee_id", emp.getEmployeeId());
        body.put("name", emp.getName());
        body.put("email", emp.getEmail());
        body.put("department", emp.getDepartment());
        body.put("role", emp.getRole());
        body.put("status", emp.getStatus() != null ? emp.getStatus().toString() : "ACTIVE");
        body.put("hire_date", emp.getHireDate() != null ? emp.getHireDate().toString() : null);
        body.put("profile_picture", emp.getProfilePicture());
        body.put("mobile", emp.getMobile());
        body.put("dob", emp.getDob() != null ? emp.getDob().toString() : null);
        body.put("gender", emp.getGender());
        return body;
    }
}
