package net.knaxel.thepod;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentPostingStatus extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private EditText mStatus, mHashTags;
    private String mediaSrc;
    private ImageView mImageView;

    public FragmentPostingStatus(String mediaSrc) {
        // Required empty public constructor
        this.mediaSrc = mediaSrc;
    }
    public boolean alertSyntax(){

        if(mStatus.getText().length() == 0 || mStatus.getText().toString().replace(" ", "").length() == 0 || mStatus.getText().length() > 140 ){

            Toast.makeText(getContext(), "A status must be 0-140 characters long!", Toast.LENGTH_LONG).show();
            return false;
        }if(mHashTags.getText().toString()!= "" &&( mHashTags.getText().toString().replaceAll(" ", "").split(",") == null || mHashTags.getText().toString().replaceAll(" ", "").split(",").length == 0)){

            Toast.makeText(getContext(), "Hashtags must be written \"#hashtag, #hashtag2, ... \"", Toast.LENGTH_LONG).show();
            return false;
        }else{

            return true;
        }

    }

    public String getStatus(){

        return mStatus.getText().toString();
    }
    public List<String> getHashTags(){
        return Arrays.asList(mHashTags.getText().toString().replaceAll(" ", "").split(","));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting_status, container, false);
        mImageView = view.findViewById(R.id.imageView);
        mStatus = view.findViewById(R.id.postStatusStatus);
        mHashTags = view.findViewById(R.id.postStatusHashTags);
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