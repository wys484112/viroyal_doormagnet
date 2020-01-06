package com.viroyal.doormagnet.usermng.repository;


import com.viroyal.doormagnet.usermng.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
public interface AccountRepository extends JpaRepository<AccountEntity, Long>, JpaSpecificationExecutor<AccountEntity> {
    AccountEntity findByAccNameAndDeletedAtIsNull(String accName);

    AccountEntity findByAccNameAndPasswordAndDeletedAtIsNull(String accName, String password);

    AccountEntity findByAccNameAndTypeAndDeletedAtIsNull(String accName, Integer type);

    //ACAccountEntity findByacUserEntity_IdAndAccNameAndDeletedAtIsNull(Long id, String accName);

    AccountEntity findByuserEntity_IdAndTypeAndDeletedAtIsNull(Long id, Integer type);

    List<AccountEntity> findByUserIdAndDeletedAtIsNull(Long userId);

    AccountEntity findByIdAndUserIdAndDeletedAtIsNull(Long id, Long userId);

    AccountEntity findByTypeAndAccNameAndDeletedAtIsNull(Integer type, String accName);
}
