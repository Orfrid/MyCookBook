package com.example.mycookbook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AddRecipeFragment extends Fragment {
    private static final int SELECT_PICTURE = 100;
    EditText Title, ingredients, directions;
    Button sendButton;
    TextView attach_name;
    ImageView attached_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        Title = (EditText) v.findViewById(R.id.title);
        ingredients = (EditText) v.findViewById(R.id.ingredients);
        directions = (EditText) v.findViewById(R.id.directions);
        sendButton = (Button) v.findViewById(R.id.sendButton);
        attach_name = (TextView) v.findViewById(R.id.attach_name);
        attached_image = (ImageView) v.findViewById(R.id.attached_image);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/didact.otf");
        Title.setTypeface(font);
        ingredients.setTypeface(font);
        directions.setTypeface(font);
        sendButton.setTypeface(font);
        attach_name.setTypeface(font);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Title.length() == 0 || ingredients.length() == 0 || directions.length() == 0) {
                    Toast.makeText(getActivity(), "please check you inputs" + "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        attach_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        return v;
    }

    public void chooseImage() {
        attached_image.setVisibility(View.INVISIBLE);
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Image"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    attached_image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i("IMAGE PATH TAG", "Image Path : " + path);
                    // Set the image in ImageView
                    attached_image.setImageURI(selectedImageUri);
                    attached_image.setDrawingCacheEnabled(true);
                    attached_image.buildDrawingCache();
                    attach_name.setText("Image Attached");

                }
            }
        }
    }

    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}