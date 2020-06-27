package com.example.phableassign.fragments;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phableassign.DatabaseClient;
import com.example.phableassign.R;
import com.example.phableassign.adapter.UsersAdapter;
import com.example.phableassign.model.User;
import com.example.phableassign.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserList extends Fragment {
    private FloatingActionButton buttonAddTask;
    private RecyclerView recyclerView;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_list, container, false);
        init(view);

        return view;
    }

    /**
     * Set initial view
     * @param view views
     */
    private void init(View view){
        recyclerView = view.findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel.class);
        userViewModel.getUserMutableLiveData().observe((LifecycleOwner) getContext(), userListUpdateObserver);

        buttonAddTask = view.findViewById(R.id.floating_button_add);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                AddUpdateUser intent = new AddUpdateUser();
                Bundle bundle = new Bundle();
                bundle.putString("type", "add");
                intent.setArguments(bundle);
                transaction.replace(R.id.fragment_container, intent).commit();
            }
        });
        FragmentManager manager = getActivity().getSupportFragmentManager();
        //getUsers(manager);
    }

    Observer<List<User>> userListUpdateObserver = new Observer<List<User>>() {
        @Override
        public void onChanged(List<User> userArrayList) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            getUsers(manager);
        }
    };

    /**
     * Get users
     */
    private void getUsers(final FragmentManager manager) {
        class GetUsers extends AsyncTask<Void, Void, List<User>> {

            @Override
            protected List<User> doInBackground(Void... voids) {
                List<User> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .userDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(final List<User> tasks) {
                super.onPostExecute(tasks);
                UsersAdapter adapter = new UsersAdapter(getActivity(),manager, tasks);
                recyclerView.setAdapter(adapter);
            }
        }

        GetUsers getUsers = new GetUsers();
        getUsers.execute();
    }
}
