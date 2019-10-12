package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.questionEntity;
import com.upgrad.quora.service.entity.userAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.upgrad.quora.service.entity.userEntity;

import java.util.List;

@Service
public class QuestionByUserBusinessService {

    @Autowired
    UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<questionEntity> getQuestions(String authorizationToken,String userId)throws AuthorizationFailedException, UserNotFoundException{

        userAuthEntity token = userDao.getUserAuthToken(authorizationToken);
        //if the access token is not there in the database, AuthorizationFailedException is thrown
        if(token == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        //if the access token is valid but the user has not logged in, AuthorizationFailedException is thrown
        if(token.getLogoutAt()!= null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        userEntity user = userDao.getUserById(userId);
        if(user == null){
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }
        return user.getQuestions();
    }
}
