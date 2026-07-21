package com.npd.npdstreaming.service;

public interface IDataConvert {

    <T> T obtainData (String json, Class <T> clas);

}
