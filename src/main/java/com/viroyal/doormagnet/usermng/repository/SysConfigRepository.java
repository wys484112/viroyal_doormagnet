package com.viroyal.doormagnet.usermng.repository;

import com.viroyal.doormagnet.usermng.entity.SysConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
public interface SysConfigRepository extends JpaRepository<SysConfigEntity, Long> {
    SysConfigEntity findByCodeAndDeletedAtIsNull(String code);
}
