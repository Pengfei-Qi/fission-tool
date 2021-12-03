package io.fission;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fission.util.DeAndEnUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HelloTool implements Function {
    // private static final Log log = LogFactory.get();
    @Override
    public ResponseEntity<?> call(RequestEntity req, Context context) {
        Map<String, Object> query_pairs = new LinkedHashMap<String, Object>();
        query_pairs.put("msg", "加载成功");

        try {
            final ObjectMapper mapper = new ObjectMapper();
            HashMap reqBody = (HashMap) req.getBody();
            // Data iotData = mapper.convertValue(reqBody, Data.class);

            HttpHeaders headers = req.getHeaders();
            query_pairs.put("header-names",headers.get("X-Fission-Params-Name").get(0));
           // query_pairs.put("iotData",iotData);
            query_pairs.put("watch3",reqBody.get("watch"));
            // if (CryptType.ENCRYPT_AES.typeValue.equalsIgnoreCase(headers.get("X-Fission-Params-Name").get(0))) {
            if ("EncryptionAes".equalsIgnoreCase(headers.get("X-Fission-Params-Name").get(0))) {
                Map<String, Object> result = DeAndEnUtils.enAes(String.valueOf(reqBody.get("watch")));
                query_pairs.put("result", result);
            }
            if (CryptType.DECRYPT_AES.typeValue.equalsIgnoreCase(headers.get("X-Fission-Params-Name").get(0))) {
                Map<String, Object> result = DeAndEnUtils.deAes(String.valueOf(reqBody.get("watch")));
                query_pairs.put("result", result);
            }
            if (CryptType.ENCRYPT_DES.typeValue.equalsIgnoreCase(headers.get("X-Fission-Params-Name").get(0))) {
                Map<String, Object> result = DeAndEnUtils.enDes(String.valueOf(reqBody.get("watch")));
                query_pairs.put("result", result);
            }
            if (CryptType.DECRYPT_DES.typeValue.equalsIgnoreCase(headers.get("X-Fission-Params-Name").get(0))) {
                Map<String, Object> result = DeAndEnUtils.deDes(String.valueOf(reqBody.get("watch")));
                query_pairs.put("result", result);
            }
            if (CryptType.ENCRYPT_SM4.typeValue.equalsIgnoreCase(headers.get("X-Fission-Params-Name").get(0))) {
                Map<String, Object> result = DeAndEnUtils.enSm4(String.valueOf(reqBody.get("watch")));
                query_pairs.put("result", result);
            }
            if (CryptType.DECRYPT_SM4.typeValue.equalsIgnoreCase(headers.get("X-Fission-Params-Name").get(0))) {
                Map<String, Object> result = DeAndEnUtils.deSm4(String.valueOf(reqBody.get("watch")));
                query_pairs.put("result", result);
            }
            query_pairs.put("success", true);
            query_pairs.put("typeValue",CryptType.ENCRYPT_AES.typeValue);
        } catch (Exception e) {
            query_pairs.put("success", false);
            query_pairs.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok(query_pairs);
    }
}
enum CryptType {
    ENCRYPT_AES("EncryptionAes"),
    DECRYPT_AES("DecryptionAes"),
    ENCRYPT_DES("EncryptionDes"),
    DECRYPT_DES("DecryptionDes"),
    ENCRYPT_SM4("EncryptionSm4"),
    DECRYPT_SM4("DecryptionSm4");

    public final String typeValue;

    private CryptType(String typeValue) {
        this.typeValue = typeValue;
    }
}
