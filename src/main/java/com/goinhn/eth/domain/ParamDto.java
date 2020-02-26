package com.goinhn.eth.domain;

import lombok.Data;
import org.apache.commons.fileupload.FileItem;

import java.util.HashMap;
import java.util.Map;

@Data
public class ParamDto {

    private Map<String, String> paramMap;
    private Map<String, FileItem> fileMap;

    public ParamDto() {
        paramMap = new HashMap<>();
        fileMap = new HashMap<>();
    }
}