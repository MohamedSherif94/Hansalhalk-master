package com.hansalhalk.users_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hansalhalk.HelperClass;
import com.hansalhalk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> usersList;

    private final MyOnClickHandler mClickHandler;

    public interface MyOnClickHandler{
        void onClick(User user);
    }

    public UsersAdapter(List<User> usersList, MyOnClickHandler clickHandler) {
        this.usersList = usersList;
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_single_layout, parent, false);

        UserViewHolder vh = new UserViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = usersList.get(position);

        Picasso.get().load(HelperClass.profile_images_url + user.getAvatar()).placeholder(R.drawable.avatar).into(holder.mImageView);
        holder.mNameTextView.setText(user.getName());
        holder.mAddressTextView.setText(user.getAddress());
        holder.mDepartmentTextView.setText(user.getCategory().get("title").toString());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mNameTextView;
        public TextView mAddressTextView;
        public TextView mDepartmentTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.user_single_layout_image_view);
            mNameTextView = itemView.findViewById(R.id.user_single_layout_name);
            mAddressTextView = itemView.findViewById(R.id.user_single_layout_address);
            mDepartmentTextView = itemView.findViewById(R.id.user_single_layout_department);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //We get the position of the adapter
            int adapterPosition = getAdapterPosition();

            //Then we get the user we are looking for
            User user = usersList.get(adapterPosition);

            //Then we can invoke the Click Listener of the Adapter
            mClickHandler.onClick(user);

        }
    }
}
