package com.test;

import dev.kratos.api.helloworld.v1.Greeter;
import dev.kratos.api.helloworld.v1.HelloReply;
import dev.kratos.api.helloworld.v1.HelloRequest;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;

public class TripleConsumerApplication {
    private final Greeter delegate;
    public TripleConsumerApplication(){
        ReferenceConfig<Greeter> ref=new ReferenceConfig<>();
        ref.setInterface(Greeter.class);
        ref.setCheck(false);
        ref.setTimeout(3000);
        ref.setProtocol(CommonConstants.TRIPLE);
        // ref.setLazy(true);
        ref.setUrl("tri://localhost:9000");
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(new ApplicationConfig("demo-consumer"))
                .reference(ref)
                .start();
        delegate=ref.get();
    }
    public String sayHello(String name){
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply helloReply = delegate.sayHello(request);
        return helloReply.getMessage();
    }

    public static void main(String[] args) {
        final TripleConsumerApplication consumer=new TripleConsumerApplication();
        System.out.println(consumer.sayHello("123132"));
    }
}
