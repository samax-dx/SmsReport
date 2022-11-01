package com.telcobright.smsreport.Util;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

public class AuthToken {
    public static String findPartyId(String token) {
        try {
            String base64Url = token.replace("(?i)(?:bearer )*\\s?", "").split("\\.")[1];
            String base64 = base64Url.replace('-', '+').replace('_', '/');
            String json = new String(Base64.getDecoder().decode(base64));
            return new ObjectMapper().readValue(json, Map.class).get("partyId").toString();
        } catch (Exception ignore) {
            return null;
        }
    }
}
