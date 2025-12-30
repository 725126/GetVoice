package com.voice.voice.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class KeywordsDTO {
    private List<Integer> serviceKeyList;
    private List<Integer> voiceKeyList;
    private List<Integer> recordingKeyList;
}
