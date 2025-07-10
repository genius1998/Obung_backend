package com.lion.CalPick.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BungResponse {
    private Long id;
    private String title;
    private String location;
    private String store;
    private Integer totalParticipants;
    private String organizerUserId;
}
