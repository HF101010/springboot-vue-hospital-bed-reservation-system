package com.example.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * 床位池信息DTO
 * 用于展示某个科室的床位池信息
 */
@Data
public class BedPoolDto {
    
    /**
     * 科室名称
     */
    @JsonProperty("department")
    private String department;
    
    /**
     * 该科室下的病区列表
     */
    @JsonProperty("wards")
    private List<WardBedInfoDto> wards;
    
    /**
     * 该科室总床位数
     */
    @JsonProperty("totalBeds")
    private Integer totalBeds;
    
    /**
     * 该科室可用床位数
     */
    @JsonProperty("availableBeds")
    private Integer availableBeds;
    
    /**
     * 该科室已占用床位数
     */
    @JsonProperty("occupiedBeds")
    private Integer occupiedBeds;
    
    /**
     * 该科室维修中床位数
     */
    @JsonProperty("maintenanceBeds")
    private Integer maintenanceBeds;
    
    /**
     * 病区床位信息DTO
     */
    @Data
    public static class WardBedInfoDto {
        /**
         * 病区ID
         */
        @JsonProperty("wardId")
        private Integer wardId;
        
        /**
         * 病区名称
         */
        @JsonProperty("wardName")
        private String wardName;
        
        /**
         * 病区总床位数
         */
        @JsonProperty("totalBeds")
        private Integer totalBeds;
        
        /**
         * 病区可用床位数
         */
        @JsonProperty("availableBeds")
        private Integer availableBeds;
        
        /**
         * 病区已占用床位数
         */
        @JsonProperty("occupiedBeds")
        private Integer occupiedBeds;
        
        /**
         * 病区维修中床位数
         */
        @JsonProperty("maintenanceBeds")
        private Integer maintenanceBeds;
        
        /**
         * 该病区下的床位列表
         */
        @JsonProperty("beds")
        private List<BedInfoDto> beds;
    }
    
    /**
     * 床位信息DTO
     */
    @Data
    public static class BedInfoDto {
        /**
         * 床位ID
         */
        @JsonProperty("bedId")
        private Integer bedId;
        
        /**
         * 床位编号
         */
        @JsonProperty("bedNumber")
        private String bedNumber;
        
        /**
         * 床位状态：1-可用，2-已占用，3-维修中
         */
        @JsonProperty("status")
        private String status;
        
        /**
         * 床位状态文本
         */
        @JsonProperty("statusText")
        private String statusText;
        
        /**
         * 备注
         */
        @JsonProperty("remark")
        private String remark;
    }
}
