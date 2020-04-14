package com.github.nullptr7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String messageKey;
    private String content;
    private boolean approved;
    private String comments;

}
