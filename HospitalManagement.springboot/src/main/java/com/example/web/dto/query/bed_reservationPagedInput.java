package com.example.web.dto.query;

import com.example.web.tools.dto.PagedInput;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 床位预约查询模型
 */
@NoArgsConstructor
@Data
public class bed_reservationPagedInput extends PagedInput {
    
    /**
     * Id主键
     */
    @JsonProperty("Id")
    private Integer Id;

    /**
     * 患者Id
     */
    @JsonProperty("PatientId")
    private String PatientId;

    /**
     * 病区Id
     */
    @JsonProperty("WardId")
    private String WardId;

    /**
     * 床位Id
     */
    @JsonProperty("BedId")
    private String BedId;

    /**
     * 预约状态
     */
    @JsonProperty("Status")
    private String Status;

    /**
     * 预约开始时间
     */
    @JsonProperty("StartTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime StartTime;

    /**
     * 预约结束时间
     */
    @JsonProperty("EndTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime EndTime;

    /**
     * 关键词（原因）
     */
    @JsonProperty("Keyword")
    private String Keyword;
}
