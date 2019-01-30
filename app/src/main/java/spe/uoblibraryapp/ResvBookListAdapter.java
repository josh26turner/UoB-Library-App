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
import java.util.List;

import spe.uoblibraryapp.api.wmsobjects.WMSHold;

public class ResvBookListAdapter extends ArrayAdapter<WMSHold> {

    private Context mContext;
    int mResource;

    public ResvBookListAdapter(Context context, int resource, List<WMSHold> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String title = getItem(position).getBook().getTitle();
        String author = getItem(position).getBook().getAuthor();
        Integer pos = getItem(position).getHoldQueuePosition();
        Integer len = getItem(position).getHoldQueueLength();
        String loc = getItem(position).getPickupLocation();
        Boolean ready = getItem(position).isReadyToCollect();


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


        res_ready.setText(getItem(position).getRequestStatusType());
        if(ready == Boolean.TRUE){
//            res_ready.setText("Ready for Collection");
            res_ready.setTextColor(ContextCompat.getColor(getContext(), R.color.colorLoan));
        } else {
//            res_ready.setText("In Queue");
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
