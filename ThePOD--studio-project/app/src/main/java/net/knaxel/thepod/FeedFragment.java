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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import net.knaxel.thepod.pod.POD;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedFragment extends Fragment {
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    long feedtime = Calendar.getInstance().getTime().getTime();
    private Button buttonTest;
    private RecyclerView storyView, feedView;
    private FeedFragment.AdapterStoryFeed mStoryAdapter;
    private FeedFragment.AdapterFeed mFeedAdapter;
    private RecyclerView.LayoutManager storyLayoutManager, feedLayoutManager;

    private ArrayList<StoryPreview> storyProfilePics = new ArrayList<>();
    private ArrayList<FeedItem> feedObjects = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        buttonTest = view.findViewById(R.id.buttonTest);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshFeed();
            }
        });
        storyView = (RecyclerView) view.findViewById(R.id.storyList);
        storyLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyView.setLayoutManager(storyLayoutManager);
        mStoryAdapter = new FeedFragment.AdapterStoryFeed(this.getContext(), storyProfilePics);
        storyView.setAdapter(mStoryAdapter);

        feedView = (RecyclerView) view.findViewById(R.id.postFeed);
        feedLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        feedView.setLayoutManager(feedLayoutManager);

        initFeed();

        return view;
    }

    public void initFeed() {

        Query q = db.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING).limit(5);
        for (String uuid : POD.USER.getFollowArray()) {
            q.whereEqualTo("author", uuid.replace(" ", ""));
        }
            q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                        DocumentSnapshot d =task.getResult().getDocuments().get(i);
                        PostType type = PostType.valueOf(d.getString("type").toUpperCase());
                        if(i == 5){

                            feedtime = d.getLong("timestamp");
                        }
                        switch (type) {
                            case MEDIA: {
                                feedObjects.add(new MediaFeedItem(
                                        d.getId(),
                                        d.getString("author_displayname"),
                                        d.getString("author_username"),
                                        d.getString("author_profilePicURL"),
                                        (List<String>) d.get("media"),
                                        type,
                                        d.getDouble("likes").longValue(), d.getDouble("reposts").longValue(), d.getDouble("comments").longValue(),
                                        d.getString("caption"),
                                        (List<String>) d.get("hashtags")));
                                continue;
                            }
                            case STATUS: {
                                feedObjects.add(new StatusFeedItem(
                                        d.getId(),
                                        d.getString("author_displayname"),
                                        d.getString("author_username"),
                                        d.getString("author_profilePicURL"),
                                        (List<String>) d.get("media"),
                                        type,
                                        d.getDouble("likes").longValue(), d.getDouble("reposts").longValue(), d.getDouble("comments").longValue(),
                                        d.getString("status"),
                                        (List<String>) d.get("hashtags")));
                                continue;
                            }
                            case THREAD: {
                                feedObjects.add(new ThreadFeedItem(
                                        d.getId(),
                                        d.getString("author_displayname"),
                                        d.getString("author_username"),
                                        d.getString("author_profilePicURL"),
                                        (List<String>) d.get("media"),
                                        type,
                                        d.getDouble("likes").longValue(), d.getDouble("reposts").longValue(), d.getDouble("comments").longValue(),
                                        d.getString("subect"),
                                        d.getString("title"),
                                        d.getString("summary")));
                                continue;
                            }
                            default: {
                                continue;
                            }
                        }
                    }
                    mFeedAdapter = new FeedFragment.AdapterFeed(getContext(), feedObjects);
                    feedView.setAdapter(mFeedAdapter);

                }
            });

    }

    public void refreshFeed() {

        for (String uuid : POD.USER.getFollowArray()) {
            Query q = db.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING).whereLessThanOrEqualTo("timestamp", feedtime).whereEqualTo("author", uuid.replace(" ", "")).limit(5);
            q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.getResult().getDocuments().size() == 0){
                    }
                    for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                        DocumentSnapshot d =task.getResult().getDocuments().get(i);
                        PostType type = PostType.valueOf(d.getString("type").toUpperCase());
                        if(i == 5){

                            feedtime = d.getLong("timestamp");
                        }
                        switch(type){
                            case MEDIA: {
                                feedObjects.add(new MediaFeedItem(
                                        d.getId(),
                                        d.getString("author_displayname"),
                                        d.getString("author_username"),
                                        d.getString("author_profilePicURL"),
                                        (List<String>)d.get("media"),
                                        type,
                                        d.getLong("likes"),d.getLong("reposts"),d.getLong("comments"),
                                        d.getString("caption"),
                                        (List<String>)d.get("hashtags")));
                                continue;
                            }
                            case STATUS: {
                                feedObjects.add(new StatusFeedItem(
                                        d.getId(),
                                        d.getString("author_displayname"),
                                        d.getString("author_username"),
                                        d.getString("author_profilePicURL"),
                                        (List<String>)d.get("media"),
                                        type,
                                        d.getLong("likes"),d.getLong("reposts"),d.getLong("comments"),
                                        d.getString("status"),
                                        (List<String>)d.get("hashtags")));
                                continue;
                            }
                            case THREAD: {
                                feedObjects.add(new ThreadFeedItem(
                                        d.getId(),
                                        d.getString("author_displayname"),
                                        d.getString("author_username"),
                                        d.getString("author_profilePicURL"),
                                        (List<String>)d.get("media"),
                                        type,
                                        d.getLong("likes"),d.getLong("reposts"),d.getLong("comments"),
                                        d.getString("subect"),
                                        d.getString("title"),
                                        d.getString("summary")));
                                continue;
                            }
                            default:{ continue;}
                        }
                    }
                    //mFeedAdapter = new FeedFragment.AdapterFeed(getContext(), feedObjects);
                    //mFeedAdapter.addNewFeedObjects(feedObjects);
                    mFeedAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public enum PostType {
        THREAD, STATUS, MEDIA,STORY;
    }

    public class AdapterFeed extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;
        ArrayList<FeedItem> feedObjects;

        /*
         *chatBoxes holds allt he MessagePreviews that store the sender information for jus the preview.
         * */
        public AdapterFeed(Context context, ArrayList<FeedItem> feedObjects) {
            this.context = context;

            this.feedObjects = feedObjects;
        }
        public void addNewFeedObjects(List<FeedItem> feed)
        {
            this.feedObjects.addAll(feed);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return feedObjects.get(position).getType().ordinal();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0: {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_thread, parent, false);
                    return new FeedFragment.AdapterFeed.ThreadViewHolder(view);
                }
                case 1: {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_status, parent, false);
                    return new FeedFragment.AdapterFeed.StatusViewHolder(view);
                }
                case 2: {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_media, parent, false);
                    return new FeedFragment.AdapterFeed.MediaViewHolder(view);
                }
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    ThreadViewHolder threadViewHolder = (ThreadViewHolder) holder;
                    ThreadFeedItem threadItem = (ThreadFeedItem) feedObjects.get(position);
                    threadViewHolder.mAuthorUsername.setText("@" + threadItem.getAuthorUsername());
                    threadViewHolder.mSubject.setText(threadItem.subject);
                    threadViewHolder.mTitle.setText(threadItem.title);
                    threadViewHolder.mSummary.setText(threadItem.summary);
                    threadViewHolder.mLikeCount.setText(threadItem.getLikeCount()+"");
                    threadViewHolder.mRepostCount.setText(threadItem.getRepostCount()+"");
                    threadViewHolder.mCommentCount.setText(threadItem.getCommentCount()+"");
                    //threadViewHolder.setPicture(feedObject);

                    break;
                case 1:
                    StatusFeedItem statusItem = (StatusFeedItem) feedObjects.get(position);
                    StatusViewHolder statusViewHolder = (StatusViewHolder) holder;
                    statusViewHolder.mAuthorUsername.setText("@" + statusItem.getAuthorUsername());
                    statusViewHolder.mAuthorDisplayname.setText( statusItem.getAuthorDisplayname());
                    statusViewHolder.mLikeCount.setText(statusItem.getLikeCount()+"");
                    statusViewHolder.mRepostCount.setText(statusItem.getRepostCount()+"");
                    statusViewHolder.mCommentCount.setText(statusItem.getCommentCount()+"");
                    statusViewHolder.mStatus.setText( statusItem.status);
                    break;
                case 2:
                    MediaFeedItem mediaItem = (MediaFeedItem) feedObjects.get(position);
                    MediaViewHolder mediaViewHolder = (MediaViewHolder) holder;
                    mediaViewHolder.mAuthorUsername.setText("@" + mediaItem.getAuthorUsername());
                    mediaViewHolder.mAuthorDisplayname.setText(mediaItem.getAuthorDisplayname());
                    mediaViewHolder.mLikeCount.setText(mediaItem.getLikeCount()+"");
                    mediaViewHolder.mRepostCount.setText(mediaItem.getRepostCount()+"");
                    mediaViewHolder.mCommentCount.setText(mediaItem.getCommentCount()+"");
                    mediaViewHolder.mCaption.setText(mediaItem.getCaption());

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
        protected class FeedObjViewHolder extends RecyclerView.ViewHolder {

            Button mLikeButton;
            TextView mAuthorUsername, mLikeCount,mCommentCount,mRepostCount;
            TextView mAuthorDisplayname;
            boolean liked = false;

            public FeedObjViewHolder(@NonNull View itemView) {
                super(itemView);
                mAuthorDisplayname = itemView.findViewById(R.id.author_displayname);
                mAuthorUsername = itemView.findViewById(R.id.author_username);
                mLikeButton = itemView.findViewById(R.id.likeButton);
                mLikeCount = itemView.findViewById(R.id.textview_likecount);
                mRepostCount = itemView.findViewById(R.id.textview_repostcount);
                mCommentCount = itemView.findViewById(R.id.textview_commentcount);
                mLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (liked) {
                            mLikeButton.setBackgroundResource(R.drawable.ic_heart);
                            liked = false;
                        } else {
                            liked = true;
                            mLikeButton.setBackgroundResource(R.drawable.ic_heart_colored);
                        }
                    }
                });
            }
        }

        protected class ThreadViewHolder extends FeedObjViewHolder {

            TextView mSubject,mTitle,mSummary;
            CircleImageView mCircleImageView;

            public ThreadViewHolder(@NonNull View itemView) {
                super(itemView);
                //mCircleImageView = itemView.findViewById(R.id.storyProfilePic);
                mSubject = itemView.findViewById(R.id.thread_subject);
                mTitle = itemView.findViewById(R.id.thread_title);
                mSummary = itemView.findViewById(R.id.thread_description);

            }
        }

        protected class MediaViewHolder extends FeedObjViewHolder {
            TextView mCaption;

            public MediaViewHolder(@NonNull View itemView) {
                super(itemView);
                mCaption = itemView.findViewById(R.id.media_caption);

            }
        }

        protected class StatusViewHolder extends FeedObjViewHolder {
            TextView mStatus;


            public StatusViewHolder(@NonNull View itemView) {
                super(itemView);
                // mCircleImageView = itemView.findViewById(R.id.storyProfilePic);
                mStatus = itemView.findViewById(R.id.status_status);

            }
        }
    }


    public class ThreadFeedItem extends FeedItem {

        private String subject,title,summary;


        public ThreadFeedItem(String uid, String authorDisplayname, String authorUsername, String authorProfilePicture, List<String> media, PostType type, long likes, long repost, long comments, String subject, String title, String summary) {
            super(uid, authorDisplayname, authorUsername, authorProfilePicture, media, type, likes, repost, comments);
            this.subject = subject;
            this.title = title;
            this.summary = summary;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }
    public class StatusFeedItem extends FeedItem {

        private String status;
        private List<String> hashtags;

        public StatusFeedItem(String uid, String authorDisplayname, String authorUsername, String authorProfilePicture, List<String> media, PostType type, long likes, long repost, long comments, String status, List<String> hashtags) {
            super(uid, authorDisplayname, authorUsername, authorProfilePicture, media, type, likes, repost, comments);
            this.status = status;
            this.hashtags = hashtags;
        }
        public List<String> getHashtags() {
            return hashtags;
        }

        public void setHashtags(List<String> hashtags) {
            this.hashtags = hashtags;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
    public class MediaFeedItem extends FeedItem {

        private String caption;
        private List<String> hashtags;

        public MediaFeedItem(String uid, String authorDisplayname, String authorUsername, String authorProfilePicture, List<String> media, PostType type, long likes, long repost, long comments, String caption, List<String> hashtags) {
            super(uid, authorDisplayname, authorUsername, authorProfilePicture, media, type, likes, repost, comments);
            this.caption = caption;
            this.hashtags = hashtags;
        }

        public List<String> getHashtags() {
            return hashtags;
        }

        public void setHashtags(List<String> hashtags) {
            this.hashtags = hashtags;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }
    }
    public class FeedItem {
        private String uid;
        private String authorDisplayname, authorUsername, authorProfilePicture;
        private List<String> media;
        private PostType type;
        private long likeCount, repostCount, commentCount;

        @Override
        public String toString() {
            return "FeedItem{" +
                    "authorDisplayname='" + authorDisplayname + '\'' +
                    ", authorUsername='" + authorUsername + '\'' +
                    ", authorProfilePicture='" + authorProfilePicture + '\'' +
                    ", media=" + media +
                    ", type=" + type +
                    ", likes=" + likeCount +
                    ", repost=" + repostCount +
                    ", comments=" + commentCount +
                    '}';
        }

        public FeedItem(String uid, String authorDisplayname, String authorUsername, String authorProfilePicture, List<String> media, PostType type, long likes, long repost, long comments) {
            this.uid = uid;
            this.authorDisplayname = authorDisplayname;
            this.authorUsername = authorUsername;
            this.authorProfilePicture = authorProfilePicture;
            this.media = media;
            this.type = type;
            this.likeCount = likes;
            this.repostCount = repost;
            this.commentCount = comments;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setLikeCount(long likeCount) {
            this.likeCount = likeCount;
        }

        public long getRepostCount() {
            return repostCount;
        }

        public void setRepostCount(long repostCount) {
            this.repostCount = repostCount;
        }

        public long getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(long commentCount) {
            this.commentCount = commentCount;
        }

        public long getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public String getAuthorDisplayname() {
            return authorDisplayname;
        }

        public void setAuthorDisplayname(String authorDisplayname) {
            this.authorDisplayname = authorDisplayname;
        }

        public String getAuthorUsername() {
            return authorUsername;
        }

        public void setAuthorUsername(String authorUsername) {
            this.authorUsername = authorUsername;
        }

        public String getAuthorProfilePicture() {
            return authorProfilePicture;
        }

        public void setAuthorProfilePicture(String authorProfilePicture) {
            this.authorProfilePicture = authorProfilePicture;
        }

        public List<String> getMedia() {
            return media;
        }

        public void setMedia(List<String> media) {
            this.media = media;
        }

        public PostType getType() {
            return type;
        }

        public void setType(PostType type) {
            this.type = type;
        }
    }

    public class AdapterStoryFeed extends RecyclerView.Adapter<FeedFragment.AdapterStoryFeed.MyViewHolder> {

        Context context;
        ArrayList<FeedFragment.StoryPreview> storyCards;

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_story, parent, false);
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

    public class StoryPreview {
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
