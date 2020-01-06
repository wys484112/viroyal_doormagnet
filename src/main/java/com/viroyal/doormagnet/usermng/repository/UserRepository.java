package com.viroyal.doormagnet.usermng.repository;


import com.viroyal.doormagnet.usermng.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    UserEntity findByPhoneAndDeletedAtIsNull(String phone);

    UserEntity findByTokenAndDeletedAtIsNull(String token);

    UserEntity findByIdAndDeletedAtIsNull(Long id);

    UserEntity findByH5tokenAndDeletedAtIsNull(String h5token);

    UserEntity findByIdAndTokenAndDeletedAtIsNull(Long id, String token);

    List<UserEntity> findByImToken(String imToken);
}
