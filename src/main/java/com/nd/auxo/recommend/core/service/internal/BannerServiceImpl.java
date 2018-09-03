package com.nd.auxo.recommend.core.service.internal;

import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;

import com.nd.auxo.recommend.core.repository.Banner;
import com.nd.auxo.recommend.core.repository.BannerRepository;
import com.nd.auxo.recommend.core.service.BannerService;
import com.nd.auxo.sdk.recommend.RecommendBannerApi;
import com.nd.auxo.sdk.recommend.vo.banner.BannerVo;
import com.nd.auxo.sdk.recommend.vo.banner.BannerVoForCreate;
import com.nd.gaea.SR;
import com.nd.gaea.context.repository.Repository;
import com.nd.gaea.context.service.AbstractService;
import com.nd.gaea.core.utils.StringUtils;
import com.nd.gaea.repository.JpaRepositoryAdapter;
import com.nd.gaea.uranus.common.exception.AlreadyUsedException;
import com.nd.gaea.uranus.common.exception.ArgumentValidationException;
import com.nd.gaea.uranus.common.exception.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/23.
 */
@Service
@Validated
public class BannerServiceImpl extends AbstractService<Banner, UUID> implements BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendBannerApi.class);


    @Override
    public Repository<Banner, UUID> getRepository() {
        return new JpaRepositoryAdapter<>(this.bannerRepository);
    }

    @Override
    public BannerVo create(@Valid BannerVoForCreate bannerVoForCreate, Long projectId, Long userId) {
        //已推荐的不能再推荐
        if (StringUtils.isNotEmpty(bannerVoForCreate.getCustomId()) &&
                this.bannerRepository.findByProjectIdAndCustomTypeAndCustomId(projectId, bannerVoForCreate.getCustomType(), bannerVoForCreate.getCustomId()) != null) {
            throw new AlreadyUsedException(SR.getString(I18nExceptionMsgConstant.BANNER_ALREADY_USED));
        }
         /*
        sortNumber
         */
        Long sortNumber = this.bannerRepository.maxSortNumberByProjectId(projectId);
        if (sortNumber == null) {
            sortNumber = 0l;
        } else {
            sortNumber = sortNumber + 1;
        }
        Banner banner = this.convertByBannerVoForCreate(bannerVoForCreate);
        //默认（推荐）
        banner.setSortNumber(sortNumber.intValue());
        banner.setProjectId(projectId);
        banner.setCreateTime(new Date());
        banner.setCreateUserId(userId);
        banner.setStatus(Banner.STATUS_ENABLE);
        banner.setUpdateTime(new Date());
        banner.setUpdateUserId(userId);
        this.bannerRepository.save(banner);
        return convert(banner);
    }

    private Banner convertByBannerVoForCreate(BannerVoForCreate bannerVoForCreate) {
        Banner banner = new Banner();
        banner.setCustomId(bannerVoForCreate.getCustomId());
        banner.setCustomType(bannerVoForCreate.getCustomType());
        banner.setTitle(bannerVoForCreate.getTitle());
        banner.setWebStoreObjectId(bannerVoForCreate.getWebStoreObjectId());
        banner.setAppStoreObjectId(bannerVoForCreate.getAppStoreObjectId());
        banner.setWebUrl(bannerVoForCreate.getWebUrl());
        banner.setAppUrl(bannerVoForCreate.getAppUrl());
        return banner;
    }

    @Override
    public BannerVo update(@Valid Banner banner, Long userId) {
        banner.setUpdateTime(new Date());
        banner.setUpdateUserId(userId);
        this.bannerRepository.save(banner);
        return convert(banner);
    }

    @Override
    public BannerVo convert(Banner banner) {
        BannerVo bannerVo = new BannerVo();
        bannerVo.setCustomId(banner.getCustomId());
        bannerVo.setCustomType(banner.getCustomType());
        bannerVo.setTitle(banner.getTitle());
        bannerVo.setWebStoreObjectId(banner.getWebStoreObjectId());
        bannerVo.setAppStoreObjectId(banner.getAppStoreObjectId());

        bannerVo.setCreateUser(banner.getCreateUserId());
        bannerVo.setCreateTime(banner.getCreateTime());
        bannerVo.setUpdateTime(banner.getUpdateTime());
        bannerVo.setUpdateUser(banner.getUpdateUserId());

        bannerVo.setProjectId(banner.getProjectId());
        bannerVo.setId(banner.getId());
        bannerVo.setSortNumber(banner.getSortNumber());
        bannerVo.setStatus(banner.getStatus());

        bannerVo.setWebUrl(banner.getWebUrl());
        bannerVo.setAppUrl(banner.getAppUrl());
        return bannerVo;
    }

    @Override
    public Page<Banner> findByProjectId(Long projectId, Pageable pageable) {
        return this.bannerRepository.findByProjectId(projectId, pageable);
    }

    @Override
    public void move(UUID bannerId, Integer sortNumber, Long projectId) {
        Banner banner = this.bannerRepository.findOne(bannerId);
        if (banner == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND,bannerId));
        }
        Integer oldSortNumber = banner.getSortNumber();
        if (oldSortNumber == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NULL_OLD_SORT_NUMBER,bannerId));
        }
        if (sortNumber.compareTo(oldSortNumber) == 0) {
            return;
        } else if (sortNumber.compareTo(oldSortNumber) < 0) {
            //如果新值小
            this.bannerRepository.moveSortNumberByProjectIdAndBannerIdAndGeSortNumberAndLeSortNumber(1, projectId, bannerId, sortNumber, oldSortNumber);
        } else {
            this.bannerRepository.moveSortNumberByProjectIdAndBannerIdAndGeSortNumberAndLeSortNumber(-1, projectId, bannerId, oldSortNumber, sortNumber);
        }
        banner.setSortNumber(sortNumber);
        this.bannerRepository.save(banner);
        LOGGER.info("移动banner推荐\""+banner.getTitle()+"("+banner.getId()+")\"");
    }
}
