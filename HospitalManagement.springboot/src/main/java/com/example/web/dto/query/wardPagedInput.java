package com.example.web.dto.query;

import com.example.web.tools.dto.PagedInput;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 病区信息查询模型
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class wardPagedInput extends PagedInput {
    
    /**
     * Id主键
     */
    @JsonProperty("Id")
    private Integer Id;

    /**
     * 病区名称
     */
    @JsonProperty("WardName")
    private String WardName;

    /**
     * 所属科室
     */
    @JsonProperty("Department")
    private String Department;

    /**
     * 关键词（备注）
     */
    @JsonProperty("Keyword")
    private String Keyword;

}
