package org.apache.dubbo.demo.provider;

import com.alibaba.dubbo.config.annotation.Service;
import org.apache.dubbo.demo.DemoService;

@Service
public class DemoServiceImpl implements DemoService {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
