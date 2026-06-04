package com.example.web.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("`allocation_audit`")
public class allocation_audit extends BaseEntity {

    @JsonProperty("reservation_id")
    @TableField(value = "reservation_id", updateStrategy = FieldStrategy.ALWAYS)
    private String reservation_id;

    @JsonProperty("bed_id")
    @TableField(value = "bed_id", updateStrategy = FieldStrategy.ALWAYS)
    private String bed_id;

    @JsonProperty("algorithm")
    @TableField(value = "algorithm", updateStrategy = FieldStrategy.ALWAYS)
    private String algorithm;

    @JsonProperty("score")
    @TableField(value = "score", updateStrategy = FieldStrategy.ALWAYS)
    private java.math.BigDecimal score;

    @JsonProperty("reason")
    @TableField(value = "reason", updateStrategy = FieldStrategy.ALWAYS)
    private String reason;
}
