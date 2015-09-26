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
 * Created by roellk on 8/15/2015.
 */
public class Friendadapter extends BaseAdapter{
    private Context context;
    String[] friendNames;
    int[] images = {R.drawable.letter_a,R.drawable.letter_r,R.drawable.letter_e,R.drawable.letter_g,R.drawable.letter_u,R.drawable.letter_b,R.drawable.letter_m,R.drawable.letter_p,R.drawable.letter_y,R.drawable.letter_d};

    public Friendadapter(Context context){
        this.context = context;
        friendNames = context.getResources().getStringArray(R.array.friend_names);
    }

    @Override
    public int getCount() {
        return friendNames.length;
    }

    @Override
    public Object getItem(int position) {
        return friendNames[position];
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
            row = inflater.inflate(R.layout.custom_row,parent,false);
        }else{
            row = convertView;
        }
        TextView textViewFriend =(TextView) row.findViewById(R.id.textViewFriend);
        ImageView imageViewFriend = (ImageView)row.findViewById(R.id.imageViewFriend);

        textViewFriend.setText(friendNames[position]);
        imageViewFriend.setImageResource(images[position]);
        return row;
    }
}
