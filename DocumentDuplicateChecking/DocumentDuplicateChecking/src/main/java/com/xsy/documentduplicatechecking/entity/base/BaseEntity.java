package com.xsy.documentduplicatechecking.entity.base;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xushiyue
 * @date 2019年7月30日11:32:29
 */
@Data
@MappedSuperclass
public class BaseEntity {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
