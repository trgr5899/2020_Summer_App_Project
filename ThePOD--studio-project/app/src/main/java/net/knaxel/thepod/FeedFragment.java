package net.knaxel.thepod;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedFragment extends Fragment {
    public static FeedFragment newInstance(){
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    private RecyclerView storyView,feedView;
    private FeedFragment.AdapterStoryFeed mStoryAdapter;
    private FeedFragment.AdapterFeed mFeedAdapter;
    private RecyclerView.LayoutManager storyLayoutManager,feedLayoutManager;

    private ArrayList<StoryPreview> storyProfilePics = new ArrayList<>();
    private ArrayList<FeedObject> feedObjects = new ArrayList<>();

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        storyView = (RecyclerView) view.findViewById(R.id.storyList);
        storyLayoutManager=new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyView.setLayoutManager(storyLayoutManager);
        mStoryAdapter = new FeedFragment.AdapterStoryFeed(this.getContext(), storyProfilePics);
        storyView.setAdapter(mStoryAdapter);

        feedView = (RecyclerView) view.findViewById(R.id.postFeed);
        feedLayoutManager=new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        feedView.setLayoutManager(feedLayoutManager);
        mFeedAdapter = new FeedFragment.AdapterFeed(this.getContext(), feedObjects);
        feedView.setAdapter(mFeedAdapter);

        populateRecyclerView();
        return view;
    }

    public void populateRecyclerView(){
        FeedFragment.StoryPreview messagePreview1 = new FeedFragment.StoryPreview("test", false);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        storyProfilePics.add(messagePreview1);
        FeedObject feedObject = new FeedObject("knaxel", "@helloworldd" , "src", "Heres my tweet", (char) 0);
        feedObjects.add(feedObject);
        feedObject = new FeedObject("knaxel", "@helloworldd" , "src", "Heres my tweet", (char) 1);
        feedObjects.add(feedObject);
        feedObject = new FeedObject("knaxel", "@helloworldd" , "src", "Heres my tweet", (char) 2);
        feedObjects.add(feedObject);
        feedObject = new FeedObject("knaxel", "@helloworldd" , "src", "Heres my tweet", (char) 0);
        feedObjects.add(feedObject);
        feedObject = new FeedObject("knaxel", "@helloworldd" , "src", "Heres my tweet", (char) 1);
        feedObjects.add(feedObject);
        feedObject = new FeedObject("knaxel", "@helloworldd" , "src", "Heres my tweet", (char) 2);
        feedObjects.add(feedObject);
        feedObject = new FeedObject("knaxel", "@helloworldd" , "src", "Heres my tweet", (char) 0);
        feedObjects.add(feedObject);
        feedObject = new FeedObject("knaxel", "@helloworldd" , "src", "Heres my tweet", (char) 1);
        feedObjects.add(feedObject);
        feedObject = new FeedObject("knaxel", "@helloworldd" , "src", "Heres my tweet", (char) 2);
        feedObjects.add(feedObject);


    }
    public class AdapterStoryFeed extends RecyclerView.Adapter<FeedFragment.AdapterStoryFeed.MyViewHolder> {

        Context context;
        ArrayList<FeedFragment.StoryPreview> storyCards ;
        /*
         *chatBoxes holds allt he MessagePreviews that store the sender information for jus the preview.
         * */
        public AdapterStoryFeed(Context context, ArrayList<FeedFragment.StoryPreview> chatBoxes) {
            this.context = context;
            this.storyCards = chatBoxes;


        }


        @NonNull
        @Override
        public FeedFragment.AdapterStoryFeed.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_object_story, parent, false);
                    return new FeedFragment.AdapterStoryFeed.MyViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            final FeedFragment.StoryPreview storyPreview = storyCards.get(position);


        }

        @Override
        public int getItemCount() {
            return storyCards.size();
        }

        /*
         *custom view holder that stores protected view objects do be edited by Adapter.
         * */
        protected class MyViewHolder extends RecyclerView.ViewHolder {
            CircleImageView mCircleImageView;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                mCircleImageView = itemView.findViewById(R.id.storyProfilePic);



            }
        }
    }
    public class AdapterFeed extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;
        ArrayList<FeedFragment.FeedObject> feedObjects ;
        /*
         *chatBoxes holds allt he MessagePreviews that store the sender information for jus the preview.
         * */
        public AdapterFeed(Context context, ArrayList<FeedFragment.FeedObject> feedObjects) {
            this.context = context;

            this.feedObjects = feedObjects;
        }

        @Override
        public int getItemViewType(int position) {
            return feedObjects.get(position).getType();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0: {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_object_thread, parent, false);
                    return new FeedFragment.AdapterFeed.ThreadViewHolder(view);
                }
                case 1: {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_object_status, parent, false);
                    return new FeedFragment.AdapterFeed.StatusViewHolder(view);
                }
                case 2: {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_object_media, parent, false);
                    return new FeedFragment.AdapterFeed.MediaViewHolder(view);
                }
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            final FeedFragment.FeedObject feedObject = feedObjects.get(position);
            switch (holder.getItemViewType()) {
                case 0:
                    ThreadViewHolder threadViewHolder = (ThreadViewHolder)holder;

                    threadViewHolder.mAuthorUsername.setText("@"+ feedObject.authorUsername);

                    break;
                case 1:
                    StatusViewHolder statusViewHolder = (StatusViewHolder)holder;
                    statusViewHolder.mAuthorUsername.setText("@"+ feedObject.authorUsername);
                    break;
                case 2:
                    MediaViewHolder mediaViewHolder = (MediaViewHolder)holder;
                    mediaViewHolder.mAuthorUsername.setText("@"+ feedObject.authorUsername);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return feedObjects.size();
        }

        /*
         *custom view holder that stores protected view objects do be edited by Adapter.
         * */
        protected class FeedObjViewHolder extends RecyclerView.ViewHolder{

            Button mLikeButton ;
            TextView mAuthorUsername;
            TextView mAuthorDisplayname;
            boolean liked = false;
            public FeedObjViewHolder(@NonNull View itemView) {
                super(itemView);
                mAuthorUsername = itemView.findViewById(R.id.author_username);
                mLikeButton = itemView.findViewById(R.id.likeButton);
                mLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(liked) {
                            mLikeButton.setBackgroundResource(R.drawable.ic_heart);
                            liked =false;
                        }else{
                            liked =true;
                            mLikeButton.setBackgroundResource(R.drawable.ic_heart_colored);
                        }
                    }
                });
            }
        }
        protected class ThreadViewHolder extends FeedObjViewHolder {
            CircleImageView mCircleImageView;

            public ThreadViewHolder(@NonNull View itemView) {
                super(itemView);
                mCircleImageView = itemView.findViewById(R.id.storyProfilePic);


            }
        }
        protected class MediaViewHolder extends FeedObjViewHolder {
            CircleImageView mCircleImageView;


            public MediaViewHolder(@NonNull View itemView) {
                super(itemView);
                mCircleImageView = itemView.findViewById(R.id.storyProfilePic);



            }
        }
        protected class StatusViewHolder extends FeedObjViewHolder {
            CircleImageView mCircleImageView;


            public StatusViewHolder(@NonNull View itemView) {
                super(itemView);
                mCircleImageView = itemView.findViewById(R.id.storyProfilePic);



            }
        }
    }
    public class FeedObject{

        String authorDisplayname, authorUsername,authorProfilePicture,data;
        char type;

        public FeedObject(String authorDisplayName, String authorUserName, String authorProfilePicture, String data, char type) {
            this.authorDisplayname = authorDisplayName;
            this.authorUsername = authorUserName;
            this.authorProfilePicture = authorProfilePicture;
            this.data = data;
            this.type = type;
        }

        public String getAuthorDisplayName() {
            return authorDisplayname;
        }

        public void setAuthorDisplayName(String authorDisplayName) {
            this.authorDisplayname = authorDisplayName;
        }

        public String getAuthorUserName() {
            return authorUsername;
        }

        public void setAuthorUserName(String authorUserName) {
            this.authorUsername = authorUserName;
        }

        public String getAuthorProfilePicture() {
            return authorProfilePicture;
        }

        public void setAuthorProfilePicture(String authorProfilePicture) {
            this.authorProfilePicture = authorProfilePicture;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public int getType() {
            return (int)type;
        }

        public void setType(char type) {
            this.type = type;
        }
    }
    public class StoryPreview{
        String profilePic;
        boolean read;

        public StoryPreview(String profilePic, boolean read) {
            this.profilePic = profilePic;
            this.read = read;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }
    }
}
