package app.android.box.waveprotocol.org.androidwave.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.android.box.waveprotocol.org.androidwave.R;

/**
 * Created by roellk on 8/19/2015.
 */
public class MessageAdapter extends BaseAdapter {
    private Context context;
    String[] header;
    String[] content;
    int[] images = {R.drawable.letter_a,R.drawable.letter_r,R.drawable.letter_e,R.drawable.letter_g,R.drawable.letter_u,R.drawable.letter_b,R.drawable.letter_m,R.drawable.letter_p,R.drawable.letter_y,R.drawable.letter_d};

    public MessageAdapter(Context context){
        this.context = context;
        header = context.getResources().getStringArray(R.array.friend_names);
        content = context.getResources().getStringArray(R.array.friend_names2);
    }

    @Override
    public int getCount() {
        return header.length;
    }

    @Override
    public Object getItem(int position) {
        return header[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row =null;
        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_message_row,parent,false);
        }else{
            row = convertView;
        }
        TextView textViewHeader =(TextView) row.findViewById(R.id.textViewHeader);
        TextView textViewContent =(TextView) row.findViewById(R.id.textViewContent);
        ImageView imageViewFriend = (ImageView)row.findViewById(R.id.imageViewMessage);


        textViewHeader.setText(header[position]);
        textViewContent.setText(content[position]);
        imageViewFriend.setImageResource(images[position]);
        return row;
    }
}
