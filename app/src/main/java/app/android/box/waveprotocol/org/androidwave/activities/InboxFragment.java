package app.android.box.waveprotocol.org.androidwave.activities;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import app.android.box.waveprotocol.org.androidwave.R;


public class InboxFragment extends ListFragment {

    MessageAdapter myadapter;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        String[] values = new String[] { "Message1", "Message2", "Message3",
//                "Message4", "Message5", "Message6", "Message7", "Message8",
//                "Message9", "Message10" };
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, values);
//        setListAdapter(adapter);
        myadapter = new MessageAdapter(getActivity());
        setListAdapter(myadapter);

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
        View rootView = inflater.inflate(R.layout.inbox_fragment, container, false);


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNewWavectivity = new Intent("app.android.box.waveprotocol.org.androidwave.NEWWAVEACTIVITY");
                startActivity(openNewWavectivity);
                //test5
                //setContentView(R.layout.activity_new_wave);
            }
        });
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
