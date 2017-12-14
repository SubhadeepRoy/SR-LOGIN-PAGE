package com.example.hp.srlogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    private static final int CHOOSE_IMAGE =101 ;
    ImageView imageView;
    EditText editText;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String profileImageUrl;
    Button buttonSave;
    FirebaseAuth mAuth;


    Uri uriProfileImage;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        editText=(EditText)findViewById(R.id.editTextDisplayName);
        imageView= (ImageView) findViewById(R.id.imageView);
        buttonSave=(Button)findViewById(R.id.buttonSave);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveUserInformation();

        }
    });

}

    private void saveUserInformation() {
        String displayName = editText.getText().toString();
        if (displayName.isEmpty()) {
            editText.setError("NAME REQUIRED");
            editText.requestFocus();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && profileImageUrl !=null) {

            UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder().setDisplayName(displayName).
                    setPhotoUri(Uri.parse(profileImageUrl)).build();



            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ProfileActivity.this, "PROFILE UPDATED", Toast.LENGTH_SHORT).show();


                    }
                }
            });


        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== CHOOSE_IMAGE && resultCode == RESULT_OK && data !=null && data.getData()!=null){
           uriProfileImage=data.getData();

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImageToFirebaseStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference profileImageRef=FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis() + ".jpg");
        if(uriProfileImage!=null){
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
profileImageUrl=taskSnapshot.getDownloadUrl().toString();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
         }

        }



    private void showImageChooser(){
    Intent intent=new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent,"SELECT PROFILE IMAGE"),CHOOSE_IMAGE);}
}