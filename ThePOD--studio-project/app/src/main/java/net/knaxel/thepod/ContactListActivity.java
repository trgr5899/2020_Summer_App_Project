package net.knaxel.thepod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.UUID;

public class ContactListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

       RecyclerView recyclerView=  findViewById(R.id.recyclerViewContactList);
    }

    public  class Contact {
    private UUID uuid;
        private String title, imageSrc;
        private boolean selecte;

        public Contact(UUID uuid, String title, String imageSrc, boolean selecte) {
            this.uuid = uuid;
            this.title = title;
            this.imageSrc = imageSrc;
            this.selecte = selecte;
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

        public String getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }

        public boolean isSelecte() {
            return selecte;
        }

        public void setSelecte(boolean selecte) {
            this.selecte = selecte;
        }
    }
}