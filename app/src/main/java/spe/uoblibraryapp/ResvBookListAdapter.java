package spe.uoblibraryapp;

import android.content.Context;
import android.content.RestrictionEntry;
import android.support.annotation.StringDef;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResvBookListAdapter extends ArrayAdapter<ResvBookEntry> {

    private Context mContext;
    int mResource;

    public ResvBookListAdapter(Context context, int resource, ArrayList<ResvBookEntry> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String title = getItem(position).getTitle();
        String author = getItem(position).getAuthor();
        Integer pos = getItem(position).getQueuePos();
        Integer len = getItem(position).getQueueLength();
        String loc = getItem(position).getCollectLocation();
        Boolean ready = getItem(position).getCollectReady();

        ResvBookEntry book = new ResvBookEntry(title, author, pos, len, loc, ready);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false );
        TextView resTitle = convertView.findViewById(R.id.res_title);
        TextView resAuthor = convertView.findViewById(R.id.res_author);
        TextView res_pos = convertView.findViewById(R.id.res_pos);
        TextView res_len = convertView.findViewById(R.id.res_len);
        TextView res_ready = convertView.findViewById(R.id.res_ready);
        TextView res_loc = convertView.findViewById(R.id.res_loc);

        resTitle.setText(title);
        resAuthor.setText(author);
        res_pos.setText(pos.toString());
        res_len.setText(len.toString());
        res_loc.setText(loc);

        if(ready == Boolean.TRUE){
            res_ready.setText("Ready for Collection");
            res_ready.setTextColor(ContextCompat.getColor(getContext(), R.color.colorLoan));
        } else {
            res_ready.setText("In Queue");
            res_ready.setTextColor(ContextCompat.getColor(getContext(), R.color.colorReservation));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }


}
