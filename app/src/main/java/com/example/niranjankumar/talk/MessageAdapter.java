package com.example.niranjankumar.talk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
{
  private List<Messages> userMessagesList;
  private FirebaseAuth mAuth;
  private DatabaseReference usersRef;

  public MessageAdapter (List<Messages> userMessagesList)
  {
      this.userMessagesList = userMessagesList;
  }


    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;
        public ImageView messageSenderPicture, messageReceiverPicture;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = (TextView) itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view);
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view);

        }
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_messages_layout, viewGroup, false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, final int position) {

        final String messageSenderID = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);

        String fromUserID = messages.getFrom();
        String fromMessageType = messages.getType();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(receiverImage).placeholder(R.drawable.profile_image).into(messageViewHolder.receiverProfileImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        messageViewHolder.receiverMessageText.setVisibility(View.GONE);
        messageViewHolder.receiverProfileImage.setVisibility(View.GONE);
        messageViewHolder.senderMessageText.setVisibility(View.GONE);
        messageViewHolder.messageSenderPicture.setVisibility(View.GONE);
        messageViewHolder.messageReceiverPicture.setVisibility(View.GONE);


        if (fromMessageType.equals("text")) {


            if (fromUserID.equals(messageSenderID)) {
                messageViewHolder.senderMessageText.setVisibility(View.VISIBLE);

                messageViewHolder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                messageViewHolder.senderMessageText.setTextColor(Color.BLACK);
                messageViewHolder.senderMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());

            } else {


                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.receiverMessageText.setVisibility(View.VISIBLE);


                messageViewHolder.receiverMessageText.setBackgroundResource(R.drawable.receiver_message_layout);
                messageViewHolder.receiverMessageText.setTextColor(Color.BLUE);
                messageViewHolder.receiverMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());

            }
        } else if (fromMessageType.equals("image")) {
            if (fromUserID.equals(messageSenderID)) {
                messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);

                Picasso.get().load(messages.getMessage()).into(messageViewHolder.messageSenderPicture);
            } else {
                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);

                Picasso.get().load(messages.getMessage()).into(messageViewHolder.messageReceiverPicture);
            }
        }
        else
        {
            if (fromUserID.equals(messageSenderID))
            {
               messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);

                messageViewHolder.messageSenderPicture.setBackgroundResource(R.drawable.file);

                messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        messageViewHolder.itemView.getContext().startActivity(intent);
                    }
                });
            }
            else {
                /*
                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);

                messageViewHolder.messageReceiverPicture.setBackgroundResource(R.drawable.file);

                messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        messageViewHolder.itemView.getContext().startActivity(intent);
                    }
                });
            }
                */


                if (fromUserID.equals(messageSenderID)) {
                    messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);

                    messageViewHolder.messageSenderPicture.setBackgroundResource(R.drawable.file);

                    messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                            messageViewHolder.itemView.getContext().startActivity(intent);
                        }
                    });

                } else if (fromMessageType.equals("pdf") || fromMessageType.equals("docx")) {
                    if (fromUserID.equals(messageSenderID)) {

                        messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);

                        //  messageViewHolder.messageSenderPicture.setBackgroundResource(R.drawable.file);

                        Picasso.get().
                                load("https://firebasestorage.googleapis.com/v0/b/talk-f8580.appspot.com/o/Image%20Files%2Ffile.png?alt=media&token=04fea733-24e5-4e5c-aa95-dbe34ebc5a4a").into(messageViewHolder.messageSenderPicture);


                    }
                } else {
                    messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                    messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);

                    Picasso.get().
                            load("https://firebasestorage.googleapis.com/v0/b/talk-f8580.appspot.com/o/Image%20Files%2Ffile.png?alt=media&token=04fea733-24e5-4e5c-aa95-dbe34ebc5a4a").into(messageViewHolder.messageReceiverPicture);


                }
            }

            }

        if (fromUserID.equals(messageSenderID))
        {
            messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(userMessagesList.get(position).getType().equals("pdf") || userMessagesList.get(position).getType().equals("docx")) {
                    CharSequence options[] = new CharSequence[]{

                            "Delete for me",
                            "Download and View This Document",
                            "Cancel",
                            "Delete for Everyone"

                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                    builder.setTitle("Delete Message?");

                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            if(i == 0)
                            {
                              deleteSentMessages(position, messageViewHolder);
                                Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                messageViewHolder.itemView.getContext().startActivity(intent);
                            }
                            else if(i == 1)
                            {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                messageViewHolder.itemView.getContext().startActivity(intent);
                            }

                            else if(i == 3)
                            {
                           deleteMessagesForEveryOne(position, messageViewHolder);
                                Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                messageViewHolder.itemView.getContext().startActivity(intent);
                            }


                        }
                    });
                    builder.show();
                }
                 else  if(userMessagesList.get(position).getType().equals("text") ) {
                        CharSequence options[] = new CharSequence[]{

                                "Delete for me",
                                "Cancel",
                                "Delete for Everyone"

                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i == 0)
                                {
                                    deleteSentMessages(position, messageViewHolder);
                                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                                else if(i == 2)
                                {
                                    deleteMessagesForEveryOne(position, messageViewHolder);
                                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }



                            }
                        });
                        builder.show();
                    }
                 else    if(userMessagesList.get(position).getType().equals("image")) {
                        CharSequence options[] = new CharSequence[]{

                                "Delete for me",
                                "View This Image",
                                "Cancel",
                                "Delete for Everyone"

                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i == 0)
                                {
                                    deleteSentMessages(position, messageViewHolder);
                                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                                else if(i == 1)
                                {
                                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), ImageViewerActivity.class);
                                    intent.putExtra("url", userMessagesList.get(position).getMessage());
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                                else if(i == 3)
                                {
                                    deleteMessagesForEveryOne(position, messageViewHolder);
                                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }


                            }
                        });
                        builder.show();
                    }
                }
            });
        }
        else
        {
            messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(userMessagesList.get(position).getType().equals("pdf") || userMessagesList.get(position).getType().equals("docx")) {
                        CharSequence options[] = new CharSequence[]{

                                "Delete for me",
                                "Download and View This Document",
                                "Cancel"

                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i == 0)
                                {
                                  deleteReceivedMessages(position, messageViewHolder);
                                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                                else if(i == 1)
                                {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }
                    else  if(userMessagesList.get(position).getType().equals("text") ) {
                        CharSequence options[] = new CharSequence[]{

                                "Delete for me",
                                "Cancel"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i == 0)
                                {
                                    deleteReceivedMessages(position, messageViewHolder);
                                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }
                  else   if(userMessagesList.get(position).getType().equals("image")) {
                        CharSequence options[] = new CharSequence[]{

                                "Delete for me",
                                "View This Image",
                                "Cancel"

                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i == 0)
                                {
                                    deleteReceivedMessages(position, messageViewHolder);
                                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), MainActivity.class);
                                    messageViewHolder.itemView.getContext().startActivity(intent);
                                }
                                else if(i == 1)
                                {
                                Intent intent = new Intent(messageViewHolder.itemView.getContext(), ImageViewerActivity.class);
                                intent.putExtra("url", userMessagesList.get(position).getMessage());
                                messageViewHolder.itemView.getContext().startActivity(intent);

                                }


                            }
                        });
                        builder.show();
                    }
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return  userMessagesList.size();
    }



    private void deleteSentMessages(final int position, final MessageViewHolder holder)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages")
                .child(userMessagesList.get(position).getFrom())
                 .child(userMessagesList.get(position).getTo())
                .child(userMessagesList.get(position).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
             if(task.isSuccessful())
             {
                 Toast.makeText(holder.itemView.getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
             }
             else
             {
                 Toast.makeText(holder.itemView.getContext(), "Error Occurred.", Toast.LENGTH_SHORT).show();
             }
            }
        });
    }

    private void deleteReceivedMessages(final int position, final MessageViewHolder holder)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages")
                .child(userMessagesList.get(position).getTo())
                .child(userMessagesList.get(position).getFrom())
                .child(userMessagesList.get(position).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(holder.itemView.getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(holder.itemView.getContext(), "Error Occurred.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void deleteMessagesForEveryOne(final int position, final MessageViewHolder holder)
    {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages")
                .child(userMessagesList.get(position).getTo())
                .child(userMessagesList.get(position).getFrom())
                .child(userMessagesList.get(position).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    rootRef.child("Messages")
                            .child(userMessagesList.get(position).getFrom())
                            .child(userMessagesList.get(position).getTo())
                            .child(userMessagesList.get(position).getMessageID())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(holder.itemView.getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(holder.itemView.getContext(), "Error Occurred.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
