package com.example.phableassign.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phableassign.fragments.AddUpdateUser;
import com.example.phableassign.DatabaseClient;
import com.example.phableassign.MainActivity;
import com.example.phableassign.R;
import com.example.phableassign.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.TasksViewHolder> {
    private static final String TAG = "UsersAdapter";

    private Context mCtx;
    private List<User> userList;
    private FragmentManager manager;

    public UsersAdapter(Context mCtx, FragmentManager manager, List<User> userList) {
        this.mCtx = mCtx;
        this.manager = manager;
        this.userList = userList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_tasks, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        User t = userList.get(position);
        holder.textViewName.setText(t.getName());
        holder.textViewEmail.setText(t.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName, textViewEmail;

        public TasksViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            User user = userList.get(getAdapterPosition());
            alert(user);
        }
    }

    private void alert(final User user){
        CharSequence options[] = new CharSequence[] {"Update", "Delete"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setCancelable(false);
        builder.setTitle("Select your option:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                // the user clicked on options[which]
                if (pos == 0){
                    FragmentTransaction transaction = manager.beginTransaction();
                    AddUpdateUser intent = new AddUpdateUser();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    bundle.putString("type", "edit");
                    intent.setArguments(bundle);
                    transaction.replace(R.id.fragment_container, intent).commit();
                }else{
                    deleteUser(user);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void deleteUser(final User task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .userDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(mCtx, "Deleted", Toast.LENGTH_LONG).show();
                ((Activity)mCtx).finish();
                mCtx.startActivity(new Intent(mCtx, MainActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
}