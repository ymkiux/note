package wooyun.note.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wooyun.note.R;
import wooyun.note.model.Write;


public class WriteAdapter extends BaseAdapter {
    private Context context;
    private List<Write> notes;

    public WriteAdapter(Context context, List<Write> notes) {
        this.context = context;
        this.notes = notes;
    }

    public void removeAllItem() {
        notes.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        notes.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Write getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView
                    = LayoutInflater.from(context).inflate(R.layout.activity_show, null);
            viewHolder = new ViewHolder();
            viewHolder.tvId
                    = (TextView) convertView.findViewById(R.id.note_id);
            viewHolder.tvTitle
                    = (TextView) convertView.findViewById(R.id.note_title);
            viewHolder.tvContent
                    = (TextView) convertView.findViewById(R.id.note_content);
            viewHolder.tvTime
                    = (TextView) convertView.findViewById(R.id.note_time);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvId.setText(notes.get(position).getId() + "");
        viewHolder.tvTitle.setText(notes.get(position).getTitle());
        viewHolder.tvContent.setText(notes.get(position).getContent());
        viewHolder.tvTime.setText(notes.get(position).getTime());
        return convertView;
    }

    public static class ViewHolder {
        public TextView tvId;
        public TextView tvTitle;
        public TextView tvContent;
        public TextView tvTime;
    }
}