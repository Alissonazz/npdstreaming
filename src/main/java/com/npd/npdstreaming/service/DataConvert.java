package com.npd.npdstreaming.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DataConvert implements IDataConvert{

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obtainData(String json, Class<T> clas) {
        try {
            return mapper.readValue(json, clas);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
