package com.rollking.lightcloud.dao;

import com.rollking.lightcloud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-11
 * */

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  User findByEmail(String email);

  List<User> findByUsername(String username);
}
