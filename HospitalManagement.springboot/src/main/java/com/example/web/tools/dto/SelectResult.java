package com.example.web.tools.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectResult {

    @JsonProperty("Name")
    private String Name;

    @JsonProperty("Label")
    private String Label;
    @JsonProperty("Code")
    private String Code;



    @JsonProperty("Prop")
    private Object Prop;


}
