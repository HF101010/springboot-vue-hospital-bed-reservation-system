package com.example.web.dto.query;

import com.example.web.tools.dto.PagedInput;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 床位信息查询模型
 */
@NoArgsConstructor
@Data
public class bedPagedInput extends PagedInput {
    
    /**
     * Id主键
     */
    @JsonProperty("Id")
    private Integer Id;

    /**
     * 床位编号
     */
    @JsonProperty("BedNumber")
    private String BedNumber;

    /**
     * 病区Id
     */
    @JsonProperty("WardId")
    private String WardId;

    /**
     * 床位状态
     */
    @JsonProperty("Status")
    private String Status;

    /**
     * 关键词（备注）
     */
    @JsonProperty("Keyword")
    private String Keyword;
}
