package com.nd.auxo.recommend.core.repository;

import com.nd.auxo.recommend.core.repository.param.KVSearchParam;
import com.nd.gaea.uranus.gql.PagerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * KV键值数据访问
 *
 * @author wbh
 * @version latest
 * @date 2016/08/12
 */
public interface KVRepository extends JpaRepository<KVInfo, UUID> {
    /**
     * 查询
     * @param param
     * @return
     */
    PagerResult<KVInfo> listPages(KVSearchParam param);

    /**
     * 获取
     * @param key
     * @param isolationStrategy
     * @param isolationParam
     * @return
     */
    KVInfo findByKeyAndIsolationStrategyAndIsolationParam(String key, Integer isolationStrategy, String isolationParam);
}
