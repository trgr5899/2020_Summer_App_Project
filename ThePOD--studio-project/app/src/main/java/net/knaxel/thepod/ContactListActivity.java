package net.knaxel.thepod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListActivity extends AppCompatActivity {



   private final ArrayList<ContactListActivity.Contact> contacts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_contact_list);

        Button mButtonBack = findViewById(R.id.buttonBack);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView=  findViewById(R.id.recyclerViewContactList);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new AdapterContact(this,contacts));

        contacts.add(new Contact(UUID.randomUUID(), "Knaxel","group", "err"));
        contacts.add(new Contact(UUID.randomUUID(), "Knaxel","personal", "err"));
        contacts.add(new Contact(UUID.randomUUID(), "Knaxel","etc", "err"));
    }

    public class AdapterContact extends RecyclerView.Adapter<ContactListActivity.AdapterContact.ContactViewHolder> {

        Context context;
        ArrayList<ContactListActivity.Contact> contacts ;
        /*
         *chatBoxes holds allt he MessagePreviews that store the sender information for jus the preview.
         * */
        public AdapterContact(Context context, ArrayList<ContactListActivity.Contact> contacts) {
            this.context = context;
            this.contacts = contacts;


        }

        // public int getItemViewType(int position);

        @NonNull
        @Override
        public ContactListActivity.AdapterContact.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact, parent, false);

            return new ContactListActivity.AdapterContact.ContactViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactListActivity.AdapterContact.ContactViewHolder holder, int position) {
            final ContactListActivity.Contact contact = contacts.get(position);

            holder.contactName.setText(contact.title);

            holder.contactProfileImage.setImageURI(Uri.parse(contact.getImageSrc()));
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        /*
         *custom view holder that stores protected view objects do be edited by Adapter.
         * */
        public class ContactViewHolder extends RecyclerView.ViewHolder {
            FrameLayout contactLayout;
            TextView contactName,contactDesc;
            ImageView contactProfileImage,selectedIndicator;
            boolean selected = false;
            public ContactViewHolder(@NonNull View itemView) {
                super(itemView);
                contactLayout = itemView.findViewById(R.id.contactLayout);
                contactLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selected){
                            contactLayout.setBackgroundColor(0xFFAAAAAA);
                            selectedIndicator.setImageResource(R.drawable.ic_sent_message_colored);
                            selected = false;
                        }else{
                            selectedIndicator.setImageResource(R.drawable.ic_sent_message);
                            selected = true;
                        }
                    }
                });
                contactName = itemView.findViewById(R.id.contactName);
                contactDesc = itemView.findViewById(R.id.contactDescription);
                contactProfileImage = itemView.findViewById(R.id.contactProfileImage);
                selectedIndicator = itemView.findViewById(R.id.contactSelectedIndicator);

            }
        }
    }
    public  class Contact {
    private UUID uuid;
        private String title,description, imageSrc;

        public Contact(UUID uuid, String title,String description, String imageSrc) {
            this.uuid = uuid;
            this.title = title;
            this.description = description;
            this.imageSrc = imageSrc;
        }

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }

    }
}