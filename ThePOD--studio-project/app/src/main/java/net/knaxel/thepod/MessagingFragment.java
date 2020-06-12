package net.knaxel.thepod;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingFragment extends Fragment {
    public static MessagingFragment newInstance(){
        MessagingFragment fragment = new MessagingFragment();
        return fragment;
    }

    private RecyclerView recyclerView;
    private AdapterFeed mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private final ArrayList<MessagePreview> messagePreviews = new ArrayList<>();
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messaging, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.messageFeed);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AdapterFeed(this.getContext(), messagePreviews);
        recyclerView.setAdapter(mAdapter);


        populateRecyclerView();
        return view;
    }
    public void populateRecyclerView(){
        MessagePreview messagePreview1 = new MessagePreview("test", "4", (char)0);
        messagePreviews.add(messagePreview1);
         messagePreview1 = new MessagePreview("testt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("testgfdgdsfdgdfst", "4", (char)0);
        messagePreviews.add(messagePreview1);
         messagePreview1 = new MessagePreview("tegdfsgstt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
        messagePreview1 = new MessagePreview("ttt", "4", (char)0);
        messagePreviews.add(messagePreview1);
    }
    /*******************************************************************************
     * Adapter class handles loading and setting variables into the views
     *
     */
    public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

        Context context;
        ArrayList<MessagePreview> chatBoxes ;
        /*
         *chatBoxes holds allt he MessagePreviews that store the sender information for jus the preview.
         * */
        public AdapterFeed(Context context, ArrayList<MessagePreview> chatBoxes) {
            this.context = context;
            this.chatBoxes = chatBoxes;


        }

       // public int getItemViewType(int position);

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final MessagePreview messagePreview = chatBoxes.get(position);

            holder.mName.setText(messagePreview.getDisplayName());
            //holder.mCircleImageView.setImageURI(messagePreview.getProfilePic());
        }

        @Override
        public int getItemCount() {
            return chatBoxes.size();
        }

        /*
         *custom view holder that stores protected view objects do be edited by Adapter.
         * */
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mName ;
            CircleImageView mCircleImageView;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                mCircleImageView = itemView.findViewById(R.id.messenger_profile_pic);
                mName = itemView.findViewById(R.id.messenger_displayname);
            }
        }
    }
    public class MessagePreview{
        String profilePic, displayName;
        char status; //0 = new message, 2 = recieved-read, 3= sent-read, 4= sent

        public MessagePreview(String displayName,String profilePic, char status) {
            this.displayName = displayName;
            this.profilePic = profilePic;
            this.status = status;
        }

        public char getStatus() {
            return status;
        }

        public void setStatus(char status) {
            this.status = status;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }
    }
}
