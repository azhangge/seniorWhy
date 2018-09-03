package com.nd.auxo.recommend.core.repository;

import com.nd.gaea.uranus.gql.PagerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
@Repository
public interface FAQManageRepository extends JpaRepository<FAQManage,UUID> {

     public PagerResult<FAQManage> listPages(Long  projectId, String questionName, String queryText, Integer questionType, String customType, int page, int size);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(" update FAQManage f set f.sortNumber=f.sortNumber+?1 where f.id<>?2 and f.sortNumber>=?3 and f.sortNumber<=?4")
    void moveSortNumberByIdAndGeSortNumberAndLeSortNumber(int offSet, UUID id, Integer beginSortNumber, Integer endSortNumber);

    @Query("select count(1) from FAQManage where projectId=?1")
    int findCount(Long projectId);

    @Query("select max(sortNumber) from FAQManage where projectId=?1")
    int findMaxSortNumber(Long projectId);
}
