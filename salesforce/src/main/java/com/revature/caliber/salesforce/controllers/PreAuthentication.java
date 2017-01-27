package com.revature.caliber.salesforce.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.caliber.salesforce.beans.Trainer;
import com.revature.caliber.salesforce.models.SalesforceToken;
import com.revature.caliber.salesforce.models.SalesforceUser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by louislopez on 1/18/17.
 */


@Controller
public class PreAuthentication {
    Map<String, String> environment = System.getenv();
    private final String authURL = environment.get("SALESFORCE_AUTH_URL");
    private final String accessTokenURL = environment.get("SALESFORCE_ACCESS_TOKEN_URL");
    private final String clientId = environment.get("SALESFORCE_CLIENT_ID");
    private final String clientSecret = environment.get("SALESFORCE_CLIENT_SECRET");
    private final String redirectUri = environment.get("SALESFORCE_REDIRECT_URI");
    private SalesforceToken salesforceToken;
    private SalesforceUser salesforceUser;
    private HttpClient httpClient;


    @RequestMapping(value = "/")
    public ModelAndView openAuth() {
        return new ModelAndView("redirect:" + authURL + "?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri);
    }

    @RequestMapping(value = "/authenticated")
    public String getCode(@RequestParam(value = "code") String code) {
        try {
            httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(accessTokenURL);
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
            parameters.add(new BasicNameValuePair("client_secret", clientSecret));
            parameters.add(new BasicNameValuePair("client_id", clientId));
            parameters.add(new BasicNameValuePair("redirect_uri", redirectUri));
            parameters.add(new BasicNameValuePair("code", code));
            post.setEntity(new UrlEncodedFormEntity(parameters));
            HttpResponse response = httpClient.execute(post);
            salesforceToken = new ObjectMapper().readValue(response.getEntity().getContent(), SalesforceToken.class);
            /*
                Made SalesforceUser class implement UserDetails(spring security).
                Role is currently hard coded role at the moment but can be fetched from DB.
                Since the response from the salesforce API proves user is authenticated
                I can now authenticate them in the application .. they now have access to any page
                with admin privileges.
             */

            setSalesforceUser(salesforceToken.getId());
            //set prefix
            //String role = "ROLE_TRAINER";

            //find user in database by making rest call
            Trainer currentUser = find(salesforceUser.getEmail());
            salesforceUser.setRole(currentUser.getTier().getTier());


            Authentication auth = new PreAuthenticatedAuthenticationToken(salesforceUser, salesforceUser.getUser_id(), salesforceUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            switch(salesforceUser.getRole()){
                case "ROLE_VP":
                    return "redirect:/vp/home";
                case "ROLE_QC":
                    return "redirect:/qc/home";
                case "ROLE_TRAINER":
                    return "redirect:/trainer/home";
            }


        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;

    }

    public void setSalesforceUser(String str) throws IOException {
        httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(str + "?access_token=" + salesforceToken.getAccess_token());
        HttpResponse response = httpClient.execute(get);
        salesforceUser = new ObjectMapper().readValue(response.getEntity().getContent(),SalesforceUser.class);
        salesforceUser.setSalesforceToken(salesforceToken);
    }

    public Trainer find(String email){
        // append path param
        RestTemplate rest = new RestTemplate();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl("http://localhost:8080/training/trainers/byemail/")
                        .path(String.valueOf(email+"/"));
        String URI = builder.build().toUriString();
        ResponseEntity<Trainer> response =
                rest.getForEntity(URI, Trainer.class);
        if(response.getStatusCode() == HttpStatus.NOT_FOUND)
            return null;
        else
            return response.getBody();
    }


}
