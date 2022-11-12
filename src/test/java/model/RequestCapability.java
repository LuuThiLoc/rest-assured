package model;

import io.restassured.http.Header;

import java.util.function.Function;

public interface RequestCapability {

    // public static by default: nen ko can ghi
    Header defautHeader = new Header("Content-type", "application/json; charset=UTF-8");

    // Handle input value is null (empty, length...)
    static Header getAuthenticatedHeader(String encodedCredStr){
        if (encodedCredStr == null){
            throw new IllegalArgumentException("[ERR] encodedCredStr can not be null!");
        }
        return new Header("Authorization", "Basic " + encodedCredStr);
    }

    // Use Functional Interface with Lambda Expression
    // String: kieu du lieu dau vao
    // Header: kieu du lieu dau ra
    Function<String, Header> getAuthenticatedHeader = encodedCredStr -> {
        if (encodedCredStr == null){
            throw new IllegalArgumentException("[ERR] encodedCredStr can not be null!");
        }
        return new Header("Authorization", "Basic " + encodedCredStr);
    };

}
