package com.msb.netty.pre;


import com.msb.netty.V1.client.Client1;
import com.msb.netty.V2.client.Client2;
import com.msb.netty.V3.client.Client3;

public class RpcDemo {

    public static void main(String[] args) throws Exception{
        IUserService service =  new UserServiceImpl();
        User user;
        //本地调用
        //user =service.findUserByID(13);
        //System.out.println(user.getName());

        //RPC调用1
        //user = Client1.findUserByIDRemote(13);
        //System.out.println(user.getName());

        //RPC调用2
        //service  = Client2.getStub();
       // user = service.findUserByID(13);

        //System.out.println(user.getName());








        //RPC调用3
        service =(IUserService)Client3.getStub(IUserService.class);
        user = service.findUserByID(13);
        System.out.println(user.getName());

    }

}
