package com.example.shopapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Column(name = "created_at")
    @CreatedDate
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        this.updatedAt = null;
    }
}
