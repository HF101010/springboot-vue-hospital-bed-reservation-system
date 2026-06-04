package com.example.web.dto.query;

import com.example.web.tools.dto.PagedInput;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * 入出院登记查询模型
 */
@NoArgsConstructor
@Data
public class admissionPagedInput extends PagedInput {
    
    /**
     * Id主键
     */
    @JsonProperty("Id")
    private Integer Id;

    /**
     * 患者用户Id
     */
    @JsonProperty("PatientId")
    private String PatientId;

    /**
     * 床位Id
     */
    @JsonProperty("BedId")
    private String BedId;

    /**
     * 入出院状态
     */
    @JsonProperty("Status")
    private String Status;

    /**
     * 关键词（备注/病情简介）
     */
    @JsonProperty("Keyword")
    private String Keyword;
}
