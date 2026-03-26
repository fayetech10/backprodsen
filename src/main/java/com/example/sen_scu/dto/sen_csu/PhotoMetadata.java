package com.example.sen_scu.dto.sen_csu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoMetadata {
    private String filename;
    private String type; // profile, recto, verso
    private long size;
    private long lastModified;
}
