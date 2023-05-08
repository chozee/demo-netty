package com.demo.netty.pre;

//把它放入到其他的项目，或者其他的服务器
public class UserServiceImpl implements IUserService {
    @Override
    public User findUserByID(Integer id) {
        return new User(id,"lijin");
    }
    @Override
    public User findUserByID2(Integer id) {
        return new User(id,"666666666666");
    }
}
