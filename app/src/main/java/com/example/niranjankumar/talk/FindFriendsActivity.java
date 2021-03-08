package com.example.niranjankumar.talk;



        import android.content.Intent;
                import android.support.annotation.NonNull;
                import android.support.v7.app.AppCompatActivity;
                import android.os.Bundle;
                import android.support.v7.widget.LinearLayoutManager;
                import android.support.v7.widget.RecyclerView;
                import android.support.v7.widget.Toolbar;
                import android.util.Log;
                import android.view.LayoutInflater;
                import android.view.View;
                import android.view.ViewGroup;
                import android.widget.ListView;
                import android.widget.TextView;

                import com.firebase.ui.database.FirebaseRecyclerAdapter;
                import com.firebase.ui.database.FirebaseRecyclerOptions;
                import com.google.firebase.database.DatabaseReference;
                import com.google.firebase.database.FirebaseDatabase;
                import com.squareup.picasso.Picasso;

                import org.w3c.dom.Text;

                import de.hdodenhof.circleimageview.CircleImageView;



public class FindFriendsActivity extends AppCompatActivity
{
    private static final String TAG = "FindFriendsActivity";

    private Toolbar mToolbar;
    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference UsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        FindFriendsRecyclerList = (RecyclerView) findViewById(R.id.find_friends_recycler_list);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        mToolbar =(Toolbar) findViewById(R.id.find_friends_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");

    }


    //  private void setSupportActionBar(Toolbar mToolbar) {
    //  }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(UsersRef, Contacts.class)
                        .build();



        FirebaseRecyclerAdapter<Contacts,FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, final int position, @NonNull Contacts model)
                    {
                        holder.userName.setText(model.getName());
                        holder.userStatus.setText(model.getStatus());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);


                        Log.d(TAG, "onBindViewHolder: username: " + model);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String visit_user_id = getRef(position).getKey();


                                Intent profileIntent = new Intent(FindFriendsActivity.this, ProfileActivity.class);
                                profileIntent.putExtra("visit_user_id", visit_user_id);
                                startActivity(profileIntent);

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View  view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                        return viewHolder;
                    }
                };

        FindFriendsRecyclerList.setAdapter(adapter);

        adapter.startListening();
    }

    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;

        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);


            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.user_profile_image);



        }
    }
}
























/*

<android.support.v7.widget.RecyclerView
        android:id="@+id/find_friends_recycler_list"
        android:layout_below="@+id/find_friends_toolbar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_alignParentEnd="true"
        android:layout_centerVertical="true" />



   */









//        import android.content.Intent;
//                import android.support.annotation.NonNull;
//                import android.support.v7.app.AppCompatActivity;
//                import android.os.Bundle;
//                import android.support.v7.widget.LinearLayoutManager;
//                import android.support.v7.widget.RecyclerView;
//                import android.support.v7.widget.Toolbar;
//                import android.util.Log;
//                import android.view.LayoutInflater;
//                import android.view.View;
//                import android.view.ViewGroup;
//                import android.widget.ListView;
//                import android.widget.TextView;
//
//                import com.firebase.ui.database.FirebaseRecyclerAdapter;
//                import com.firebase.ui.database.FirebaseRecyclerOptions;
//                import com.google.firebase.database.DatabaseReference;
//                import com.google.firebase.database.FirebaseDatabase;
//                import com.squareup.picasso.Picasso;
//
//                import org.w3c.dom.Text;
//
//                import de.hdodenhof.circleimageview.CircleImageView;
//
//
//
//public class FindFriendsActivity extends AppCompatActivity
//{
//    private static final String TAG = "FindFriendsActivity";
//
//    private Toolbar mToolbar;
//    private RecyclerView FindFriendsRecyclerList;
//    private DatabaseReference UsersRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_find_friends);
//
//        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
//
//        FindFriendsRecyclerList = (RecyclerView) findViewById(R.id.find_friends_recycler_list);
//        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));
//
//        mToolbar =(Toolbar) findViewById(R.id.find_friends_toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Find Friends");
//
//    }
//
//
//    //  private void setSupportActionBar(Toolbar mToolbar) {
//    //  }
//
//
//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//
//        FirebaseRecyclerOptions<Contacts> options =
//                new FirebaseRecyclerOptions.Builder<Contacts>()
//                        .setQuery(UsersRef, Contacts.class)
//                        .build();
//
//
//
//        FirebaseRecyclerAdapter<Contacts,FindFriendViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, final int position, @NonNull Contacts model)
//                    {
//                        holder.userName.setText(model.getName());
//                        holder.userStatus.setText(model.getStatus());
//                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);
//
//
//                        Log.d(TAG, "onBindViewHolder: username: " + model);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                String visit_user_id = getRef(position).getKey();
//
//
//                                Intent profileIntent = new Intent(FindFriendsActivity.this, ProfileActivity.class);
//                                profileIntent.putExtra("visit_user_id", visit_user_id);
//                                startActivity(profileIntent);
//
//                            }
//                        });
//
//                    }
//
//                    @NonNull
//                    @Override
//                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
//                    {
//                        View  view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
//                        FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
//                        return viewHolder;
//                    }
//                };
//
//        FindFriendsRecyclerList.setAdapter(adapter);
//
//        adapter.startListening();
//    }
//
//    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
//    {
//        TextView userName, userStatus;
//        CircleImageView profileImage;
//
//        public FindFriendViewHolder(@NonNull View itemView)
//        {
//            super(itemView);
//
//
//            userName = itemView.findViewById(R.id.user_profile_name);
//            userStatus = itemView.findViewById(R.id.user_status);
//            profileImage = itemView.findViewById(R.id.user_profile_image);
//
//
//
//        }
//    }
//}

