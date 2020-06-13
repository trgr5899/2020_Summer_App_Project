package net.knaxel.thepod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.knaxel.thepod.pod.POD;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private String mediaSrc = "";

    Button mButtonBack, mButtonPost;

    FragmentPostingMedia mediaFragment;
    FragmentPostingStatus statusFragment;
    FragmentPostingThread threadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_post);


        mButtonBack = findViewById(R.id.buttonBack);
        mButtonPost = findViewById(R.id.buttonPost);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mediaSrc = extras.getString("media");

        }
        mediaFragment = new FragmentPostingMedia(mediaSrc);
        statusFragment = new FragmentPostingStatus(mediaSrc);
        threadFragment = new FragmentPostingThread(mediaSrc);


        final TabLayout tabLayout = findViewById(R.id.postingTabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (tabLayout.getSelectedTabPosition() == 0) {
                    if (!threadFragment.alertSyntax()) return;


                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    if (!statusFragment.alertSyntax()) {
                        return;
                    }

                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    if (!mediaFragment.alertSyntax()) return;

                }
                long time = Calendar.getInstance().getTime().getTime();


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseFirestore.getInstance();
               // FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                       // .setTimestampsInSnapshotsEnabled(true)
                      //  .build();
//                db.setFirestoreSettings(settings);
                final String[] mediaUpload = {""};
                if (mediaSrc != "") {

                    Uri file = Uri.fromFile(new File(mediaSrc));
                    final StorageReference riversRef = mStorageRef.child("uploads/" + auth.getCurrentUser().getUid() + "/" + time + ".bmp");

                    riversRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        { riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mediaUpload[0] = uri.toString();
                            }
                        });
                        };
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                    Toast.makeText(PostActivity.this, "There is a connection error!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            });

                }

                Map<String, Object> post = new HashMap<>();

                post.put("timestamp", time);
                post.put("author", auth.getCurrentUser().getUid());
                post.put("author_displayname", POD.USER.getDisplayname());
                post.put("author_username", POD.USER.getUsername());
                post.put("likes", 0);
                post.put("reposts", 0);
                post.put("comments", 0);
                post.put("media", Arrays.asList(mediaUpload));

                if (tabLayout.getSelectedTabPosition() == 0) {
                    post.put("type","THREAD");
                    // post.put("accounttype", type );

                    post.put("subject", threadFragment.getSubject());
                    post.put("title", threadFragment.getTitle());
                    post.put("description", threadFragment.getDescription());
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    post.put("type", "STATUS");
                    // post.put("accounttype", type );
                    post.put("status", statusFragment.getStatus());
                    post.put("hashtags", statusFragment.getHashTags());
                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    post.put("type", "MEDIA");
                    // post.put("accounttype", type );
                    post.put("caption", mediaFragment.getCaption());
                    post.put("hashtags", mediaFragment.getHashTags());
                    post.put("usertags", mediaFragment.getTags());
                }
                CollectionReference collection = db.collection("post");
                collection
                        .add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostActivity.this, "There is a connection error!", Toast.LENGTH_LONG).show();
                                return;
                            }
                        });

            }
        });

        tabLayout.getTabAt(2).select();
    }

    private void selectTab(String tabText) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (tabText) {
            case "media":
                fragmentTransaction.replace(R.id.view, mediaFragment, null);
                fragmentTransaction.commit();

                break;
            case "status":
                fragmentTransaction.replace(R.id.view, statusFragment, null);
                fragmentTransaction.commit();

                break;
            case "thread":
                fragmentTransaction.replace(R.id.view, threadFragment, null);
                fragmentTransaction.commit();

                break;

        }

    }

}