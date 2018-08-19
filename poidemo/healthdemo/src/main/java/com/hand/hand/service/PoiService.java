package com.hand.hand.service;

import com.hand.hand.util.Json;

import java.io.InputStream;

public interface PoiService {

    Json readExcel(InputStream inputStream, String fileName);

}
