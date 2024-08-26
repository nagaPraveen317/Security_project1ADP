package com.webage.Security_Authorization.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.webage.Security_Authorization.JWTutil.JWTHelper;
import com.webage.Security_Authorization.Objects.Customer;
import com.webage.Security_Authorization.Objects.Token;

@RestController
@RequestMapping("/account/token")
public class TokenAPI {

    public static Token appUserToken;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<?> createTokenForCustomer(@RequestBody Customer customer) {
        String username = customer.getName();
        String password = customer.getPassword();

        if (username != null && username.length() > 0 && password != null && password.length() > 0 && checkPassword(username, password)) {
            Token token = createToken(username);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/check")
    public String check(){
        return "This api is working";
    }

    private boolean checkPassword(String username, String password) {
        if (username.equals("ApiClientApp") && password.equals("secret")) {
            return true;
        }

        Customer cust = getCustomerByNameFromCustomerAPI(username);

        if (cust != null && cust.getName().equals(username) && cust.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public static Token getAppUserToken() {
        if (appUserToken == null || appUserToken.getToken() == null || appUserToken.getToken().length() == 0) {
            appUserToken = createToken("ApiClientApp");
        }
        return appUserToken;
    }

    private static Token createToken(String username) {
        String scopes = "com.webage.data.apis";
        if (username.equalsIgnoreCase("ApiClientApp")) {
            scopes = "com.webage.auth.apis";
        }
        String tokenString = JWTHelper.createToken(scopes);
        return new Token(tokenString);
    }

    private Customer getCustomerByNameFromCustomerAPI(String username) {
        String url = "http://localhost:8080/api/customers/byname/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        Token token = getAppUserToken();
        headers.set("Authorization", "Bearer " + token.getToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Customer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Customer.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }
}