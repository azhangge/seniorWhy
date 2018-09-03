package com.nd.auxo.recommend.core.repository;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * KV键值配置
 *
 * @author wbh
 * @version latest
 * @date 2016/08/12
 */
@Entity
@Table(name = "kv_info")
@ApiModel(description = "KV键值配置")
@Data
public class KVInfo {

    public static final String FIELD_UPDATE_TIME = "updateTime";


    public KVInfo() {
        this.setId(UUID.randomUUID());
    }

    public KVInfo(String key) {
        this.setId(UUID.randomUUID());
        this.setKey(key);
    }


    @Id
    @ApiModelProperty("标识（只读）")
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @ApiModelProperty("键")
    @Column(name = "kv_key")
    private String key;

    @ApiModelProperty("隔离策略：0(项目隔离),1(全站共享),2(虚拟项目隔离)")
    @Column(name = "isolation_strategy")
    private Integer isolationStrategy;

    @ApiModelProperty("隔离参数")
    @Column(name = "isolation_param")
    private String isolationParam;

    @ApiModelProperty("值")
    @Column(name = "kv_value")
    private String value;

    @ApiModelProperty("分组键")
    @Column(name = "group_key")
    private String groupKey;

    @ApiModelProperty("更新时间")
    @Column(name = "update_time")
    private Date updateTime;

    @ApiModelProperty("备注")
    @Column(name = "remark")
    private String remark;


    public enum IsolationStrategy {
        PROJECT_SINGLE(0),
        PROJECT_ALL(1),
        PROJECT_VIRTUAL(2);
        private Integer value;

        IsolationStrategy(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return this.value;
        }

        public static IsolationStrategy getByValue(Integer value) {
            IsolationStrategy[] isolationStrategies = IsolationStrategy.values();
            for (IsolationStrategy strategy : isolationStrategies) {
                if (strategy.getValue().equals(value)) {
                    return strategy;
                }
            }
            return null;
        }
    }
}
