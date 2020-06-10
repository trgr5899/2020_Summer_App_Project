package net.knaxel.thepod;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentPostingMedia extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    String mediaSrc;
    ImageView mImageView;

    public FragmentPostingMedia(String mediaSrc) {
        // Required empty public constructor
        this.mediaSrc = mediaSrc;
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
        if (getArguments() != null) {
        }

        if (mediaSrc != null) {
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