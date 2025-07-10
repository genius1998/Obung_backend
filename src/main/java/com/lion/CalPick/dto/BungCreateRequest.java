package com.lion.CalPick.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BungCreateRequest {
    private String title;
    private String location;
    private String store;
    private Integer totalParticipants;
    private String organizerUserId;
}
