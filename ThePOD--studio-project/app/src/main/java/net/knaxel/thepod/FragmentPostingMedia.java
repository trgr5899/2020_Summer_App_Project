package net.knaxel.thepod;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentPostingMedia extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ImageView mImageView;
    private EditText mCaption,mHashTags,mTags;
    private String mediaSrc;

    public FragmentPostingMedia(String mediaSrc) {
        // Required empty public constructor
        this.mediaSrc = mediaSrc;
    }
    public String getCaption(){

        return mCaption.getText().toString();
    }
    public List<String> getHashTags(){
        return Arrays.asList(mHashTags.getText().toString().replaceAll(" ", "").split(","));
    }
    public List<String> getTags(){
        return Arrays.asList(mTags.getText().toString().replaceAll(" ", "").split(","));
    }
    public boolean alertSyntax(){
    if(mHashTags.getText().toString()!= "" &&( mHashTags.getText().toString().replaceAll(" ", "").split(",") == null || mHashTags.getText().toString().replaceAll(" ", "").split(",").length == 0)){

        Toast.makeText(getContext(), "Hashtags must be written \"#hashtag, #hashtag2, ... \"", Toast.LENGTH_LONG).show();
            return false;
        }if(mTags.getText().toString().replaceAll(" ", "").split(",") == null || mTags.getText().toString().replaceAll(" ", "").split(",").length == 0){

            Toast.makeText(getContext(), "Tags must be written \"@hashtag, @hashtag2, ... \"", Toast.LENGTH_LONG).show();
            return false;
        }if(mediaSrc==null || mediaSrc ==""){
            Toast.makeText(getContext(), "Media posts must have a media subject (picture or video).", Toast.LENGTH_LONG).show();
            return false;
        }else{

            return true;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting_media, container, false);
        mImageView = view.findViewById(R.id.imageView);
        mCaption = view.findViewById(R.id.postMediaCaption);
        mHashTags = view.findViewById(R.id.postMediaHashTags);
        mTags = view.findViewById(R.id.postMediaUserTags);
        if (getArguments() != null) {
        }

        if (mediaSrc != null && mediaSrc !="") {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), Uri.fromFile(new File(mediaSrc)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mImageView.setImageBitmap(bitmap);
            // Inflate the layout for this fragment
        }

        return view;
    }
}