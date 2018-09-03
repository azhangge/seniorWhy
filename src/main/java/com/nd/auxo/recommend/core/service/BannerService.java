package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.Banner;
import com.nd.auxo.sdk.recommend.vo.banner.BannerVo;
import com.nd.auxo.sdk.recommend.vo.banner.BannerVoForCreate;
import com.nd.gaea.context.service.EntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/23.
 */
public interface BannerService extends EntityService<Banner,UUID> {


    /**
     * 创建
     * @param bannerVoForCreate
     * @param appId
     * @param userId
     * @return
     */
    BannerVo create(@Valid BannerVoForCreate bannerVoForCreate, Long appId, Long userId);


    /**
     * 更新
     * @param banner
     * @param userId
     * @return
     */
    BannerVo update(@Valid Banner banner, Long userId);

    BannerVo convert(Banner banner);

    Page<Banner> findByProjectId(Long appId, Pageable pageable);

    /**
     * 移动
     * @param bannerId
     * @param sortNumber
     * @param projectId
     */
    void move(UUID bannerId, Integer sortNumber, Long projectId);
}
