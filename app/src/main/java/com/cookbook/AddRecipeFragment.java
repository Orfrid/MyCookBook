package com.cookbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cookbook.Model.CurrentUser;
import com.cookbook.Model.Recipe;
import com.cookbook.Model.Model;
import com.cookbook.Model.User;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddRecipeFragment extends Fragment {
    private static final int SELECT_PICTURE = 100;
    EditText Title, ingredients, instructions;
    Button sendButton;
    ImageView attached_image;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        Title = (EditText) v.findViewById(R.id.title);
        ingredients = (EditText) v.findViewById(R.id.ingredients);
        instructions = (EditText) v.findViewById(R.id.directions);
        sendButton = (Button) v.findViewById(R.id.sendButton);
        attached_image = (ImageView) v.findViewById(R.id.attached_image);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/didact.otf");
        Title.setTypeface(font);
        ingredients.setTypeface(font);
        instructions.setTypeface(font);
        sendButton.setTypeface(font);

        attached_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImage();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecipe();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(CurrentUser.instance.getUser().getName() == null) {
            NavController nav = Navigation.findNavController(v);
            NavDirections a = AddRecipeFragmentDirections.actionNavigationAddToLogin();
            nav.navigate(a);
        }
    }

    private void saveRecipe() {
        final Recipe recipe = new Recipe();
        recipe.setName(Title.getText().toString());
        recipe.setIngredients(ingredients.getText().toString());
        recipe.setInstructions(instructions.getText().toString());
        recipe.setUser(CurrentUser.instance.getUser().getName());
        BitmapDrawable drawable = (BitmapDrawable)attached_image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Model.instance.uploadImage(bitmap, recipe.getUser(), new Model.UploadImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null){
                    displayFailedError();
                }else{
                    recipe.setImageUrl(url);
                    Model.instance.addRecipe(recipe, new Model.AddRecipeListener() {
                        @Override
                        public void onComplete() {
                            Navigation.findNavController(sendButton).popBackStack();
                        }
                    });
                }
            }
        });
    }

    private void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void editImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        attached_image.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                attached_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }
}