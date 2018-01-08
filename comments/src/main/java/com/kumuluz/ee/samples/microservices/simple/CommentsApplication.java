package com.kumuluz.ee.samples.microservices.simple;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService(value = "microservices-comments", ttl = 20, environment = "dev", version = "1.0.0")
@ApplicationPath("/")
public class CommentsApplication extends Application{
}
