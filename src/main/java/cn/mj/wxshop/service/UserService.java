package cn.mj.wxshop.service;

import cn.mj.wxshop.Dao.UserDao;
import cn.mj.wxshop.generate.User;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserByTel(String tel) {
        return userDao.getUserByTel(tel);
    }

    public User createUserIfExist(String tel) {
        User user = new User();
        user.setTel(tel);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        try {
            userDao.insertUser(user);
        } catch (PersistenceException e) {
            // e.printStackTrace();
            return userDao.getUserByTel(tel);
        }
        return user;
    }
}
