package com.nd.auxo.recommend.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * 学员
 * <p/>
 * Created by jsc on 2016/7/19.
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, UUID> {
}
