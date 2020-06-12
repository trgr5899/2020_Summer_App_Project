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
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentPostingThread extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private EditText mSubject,mTitle,mDesc;
    private String mediaSrc;
    private ImageView mImageView;

    public FragmentPostingThread(String mediaSrc) {
        // Required empty public constructor
        this.mediaSrc = mediaSrc;
    }
    public String getTitle(){

        return mTitle.getText().toString();
    }
    public String getSubject(){
        return mSubject.getText().toString();
    }
    public String getDescription(){
        return mDesc.getText().toString();
    }
    public boolean alertSyntax(){

        if(mSubject.getText().length() == 0 || mSubject.getText().toString().replace(" ", "").length() == 0 || mSubject.getText().length() > 64 ){

            Toast.makeText(getContext(), "A thread subject must be 1-64 letters long!", Toast.LENGTH_LONG).show();
            return false;
        }if(mTitle.getText().length() == 0 || mTitle.getText().toString().replace(" ", "").length() == 0 || mTitle.getText().length() > 64 ){

            Toast.makeText(getContext(), "A thread title must be 1-64 letters long!", Toast.LENGTH_LONG).show();
            return false;
        }if(mDesc.getText().length() > 200 || mTitle.getText().toString().replace(" ", "").length() == 0 || mTitle.getText().length() > 1500 ){

            Toast.makeText(getContext(), "A thread post summary must be 200-1500 letters long!", Toast.LENGTH_LONG).show();
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
        View view = inflater.inflate(R.layout.fragment_posting_thread, container, false);
        mImageView = view.findViewById(R.id.imageView);
        mSubject = view.findViewById(R.id.postThreadSubject);
        mTitle = view.findViewById(R.id.postThreadTitle);
        mDesc = view.findViewById(R.id.postThreadDescription);
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