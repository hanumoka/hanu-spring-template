package hanu.exam.spring_template.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "hanu.security.permit-all")
public class SecurityPermitAllConfig {
    private final List<String> getList = new ArrayList<String>();
    private final List<String> postList = new ArrayList<String>();

    public List<String> getGetList() {
        return getList;
    }

    public List<String> getPostList() {
        return postList;
    }
}
