package com.nd.auxo.recommend.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/23.
 */
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
}
