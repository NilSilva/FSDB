package nil.s.fsdb;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorFilmografia extends RecyclerView.Adapter<AdaptadorFilmografia.FilmografiaViewHolder> {

    private String TAG = "AdaptadorFilmografia";

    private Context context;

    private ArrayList<ItemFilme> itemList;

    private AdaptadorFilmografia.OnItemClickListenerP mListener;

    private RecyclerView mRecyclerView;

    public interface OnItemClickListenerP {
        void onItemClick(int position);
    }

    public void setOnItemClickListenerP(AdaptadorFilmografia.OnItemClickListenerP listener) {
        mListener = listener;
    }

    public AdaptadorFilmografia(Context context, ArrayList<ItemFilme> itemList) {

        this.context = context;
        this.itemList = itemList;
    }

    /**
     * Called when RecyclerView needs a new {@link FilmografiaViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(FilmografiaViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(FilmografiaViewHolder, int)
     */
    @NonNull
    @Override
    public AdaptadorFilmografia.FilmografiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_filmografia, parent, false);
        return new FilmografiaViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link androidx.recyclerview.widget.RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link androidx.recyclerview.widget.RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(FilmografiaViewHolder, int)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final FilmografiaViewHolder holder, int position) {

        ItemFilme currentItem = itemList.get(position);

        String imageUrl = currentItem.getPoster_path();
        String nome = currentItem.getTitle();
        String char_job = currentItem.getChar_job();
        String ano = currentItem.getRelease_date();
        String media = currentItem.getType();

        holder.textViewMedia.setText(media);
        holder.textViewNome.setText(nome);
        holder.textViewPersonagemJob.setText(char_job);
        holder.textViewAno.setText(ano);
        Picasso.get().load(imageUrl).placeholder(R.drawable.progress_animation).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

                holder.imageView.setImageResource(R.mipmap.ic_no_image);
            }
        });
    }

    public String getId(int position) {
        return itemList.get(position).getId();
    }

    public String getMedia(int position) {
        return itemList.get(position).getType();
    }

    public String getNome(int position) {
        return itemList.get(position).getTitle();
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class FilmografiaViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public TextView textViewNome;
        public TextView textViewPersonagemJob;
        public TextView textViewAno;
        public TextView textViewMedia;

        public FilmografiaViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewItemFilmografia);

            textViewNome = itemView.findViewById(R.id.textViewItemFilmografiaNome);
            textViewPersonagemJob = itemView.findViewById(R.id.textViewItemFilmografiaPersonagem_job);
            textViewAno = itemView.findViewById(R.id.textViewItemFilmografiaAno);
            textViewMedia = itemView.findViewById(R.id.textViewItemFilmografiaMedia);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
