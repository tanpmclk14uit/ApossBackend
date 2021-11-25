package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected Date createTime;

    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected Date updateTime;

}
