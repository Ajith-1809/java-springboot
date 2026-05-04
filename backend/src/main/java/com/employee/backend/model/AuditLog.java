package com.employee.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDateTime;

public class AuditLog {
    
    private Long id;
    private String action;

    @JsonProperty("entity_name")
    @JsonAlias("entityName")
    private String entityName;

    @JsonProperty("entity_id")
    @JsonAlias("entityId")
    private Long entityId;

    @JsonProperty("performed_by")
    @JsonAlias("performedBy")
    private String performedBy;

    private LocalDateTime timestamp;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
