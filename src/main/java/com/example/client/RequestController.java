package com.example.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class RequestController {

    private static final Logger logger = LogManager.getLogger(RequestController.class);
    private String node;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/connection")
    public UsersDB connection(@RequestBody UsersDB user){

        ResponseEntity<UsersDB> responseEntity;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //    responseEntity = restTemp.postForEntity("http://localhost:8060/update",
        responseEntity = restTemplate.postForEntity("http://localhost:8000/auth/connect",
                new HttpEntity<UsersDB>(user,headers),
                UsersDB.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            logger.info("redirect to node for read requests");
        }
        this.node = responseEntity.getBody().database;

        UsersDB usersDB = responseEntity.getBody();
        logger.info(usersDB.getDatabase());
        return responseEntity.getBody();
    }

    @PostMapping("/registration")
    public UsersDB registration(@RequestBody UsersDB user){

        String response = null;
        ResponseEntity<UsersDB> responseEntity;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        responseEntity = restTemplate.postForEntity("http://localhost:8000/registration",
                new HttpEntity<UsersDB>(user,headers),
                UsersDB.class);

        //if (responseEntity.getStatusCode() == HttpStatus.OK)
        //    response = "User created!";
        return responseEntity.getBody();
    }

    @GetMapping("/read")
    public List<Student> read() {

        ResponseEntity<List<Student>> responseEntity;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = node+"read";
        logger.info("url request:"+url);
        responseEntity = restTemplate.exchange(url,
                //    responseEntity = restTemplate.exchange("http://slavedb:9080/read",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        return responseEntity.getBody();

    }
}
