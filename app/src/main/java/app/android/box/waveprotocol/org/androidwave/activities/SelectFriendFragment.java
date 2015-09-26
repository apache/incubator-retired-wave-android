package app.android.box.waveprotocol.org.androidwave.activities;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import app.android.box.waveprotocol.org.androidwave.R;


public class SelectFriendFragment extends ListFragment {

    Friendadapter friendadapter;

    public SelectFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        String[] values = new String[] { "friend1", "friend2", "friend3",
//                "friend4", "friend5", "friend6", "friend7", "friend8",
//                "friend9", "friend10" };
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, values);
//        setListAdapter(adapter);


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, values);

        friendadapter = new Friendadapter(getActivity());
        setListAdapter(friendadapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(getActivity(), item + " selected", Toast.LENGTH_LONG).show();
//        ImageView img= (ImageView) findViewById(R.id.image);
//        img.setImageResource(R.drawable.my_image);

//        ImageView img = new ImageView(this);
//        img.setImageResource(R.drawable.my_image);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_friend_fragment, container, false);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
