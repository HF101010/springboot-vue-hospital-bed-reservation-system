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
 * 系统通知查询模型
 */
@NoArgsConstructor
@Data
public class notificationPagedInput extends PagedInput {
    
    /**
     * Id主键
     */
    @JsonProperty("Id")
    private Integer Id;

    /**
     * 关键词（标题搜索）
     */
    @JsonProperty("Keyword")
    private String Keyword;

    /**
     * 是否已读
     */
    @JsonProperty("IsRead")
    private Boolean IsRead;

}
