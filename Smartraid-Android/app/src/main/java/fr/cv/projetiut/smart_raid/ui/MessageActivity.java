package fr.cv.projetiut.smart_raid.ui;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.cv.projetiut.smart_raid.R;
import fr.cv.projetiut.smart_raid.data.Message;
import fr.cv.projetiut.smart_raid.data.retrofit.RestInterface;
import fr.cv.projetiut.smart_raid.ui.list.message.dummy.MessageObjectContent;
import fr.cv.projetiut.smart_raid.ui.list.message.dummy.MessageViewAdapter;
import fr.cv.projetiut.smart_raid.user.User;

/**
 * Created by valentincroset on December,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class MessageActivity extends AppCompatActivity {

    private User user = User.getUserInstance();
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        createMessageList();

    }

    private void createMessageList() {
        for (Message message : user.getMessageList()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yy");
            String dateFormatString = simpleDateFormat.format(new Date(message.getDate()));
            MessageObjectContent.addItem(
                    new MessageObjectContent.DummyItem(message.getMessage(), dateFormatString));
        }
        for (MessageObjectContent.DummyItem item : MessageObjectContent.ITEMS) {
            System.out.println(item.toString());
        }
        recyclerView = findViewById(R.id.course_list_message);
        MessageViewAdapter messageViewAdapter = new MessageViewAdapter(MessageObjectContent.ITEMS);
        recyclerView.setAdapter(messageViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


}
