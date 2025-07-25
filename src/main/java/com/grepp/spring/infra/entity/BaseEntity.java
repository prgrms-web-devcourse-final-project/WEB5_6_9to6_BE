package com.grepp.spring.infra.entity;

import com.querydsl.core.annotations.QuerySupertype;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@QuerySupertype
public class BaseEntity {
    
    protected Boolean activated = true;
    
    @CreatedDate
    protected LocalDateTime createdAt = LocalDateTime.now();
    @LastModifiedDate
    protected LocalDateTime modifiedAt = LocalDateTime.now();

    public void unActivated(){
        this.activated = false;
    }
    public Boolean isActivated() {
        return activated;
    }
}
