package com.example.phableassign.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.phableassign.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    MutableLiveData<List<User>> userLiveData;
    ArrayList<User> userArrayList;

    public UserViewModel(){
        userLiveData = new MutableLiveData<>();
        init();
    }

    public MutableLiveData<List<User>> getUserMutableLiveData(){
        return userLiveData;
    }

    public void init(){
        userLiveData.setValue(userArrayList);
    }
}
