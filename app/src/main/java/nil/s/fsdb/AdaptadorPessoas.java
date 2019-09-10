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

public class AdaptadorPessoas extends RecyclerView.Adapter<AdaptadorPessoas.PessoaViewHolder> {

    private String TAG = "AdaptadorPessoas";

    private Context context;

    private ArrayList<ItemPessoas> itemList;

    private AdaptadorPessoas.OnItemClickListenerP mListener;

    private RecyclerView mRecyclerView;

    private boolean InUse = false;

    public boolean getInUse() {
        return InUse;
    }

    public void NotInUse() {
        InUse = false;
    }

    public interface OnItemClickListenerP {
        void onItemClick(int position);
    }

    public void setOnItemClickListenerP(AdaptadorPessoas.OnItemClickListenerP listener) {
        mListener = listener;
    }

    public AdaptadorPessoas(Context context, ArrayList<ItemPessoas> itemList) {

        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    public RecyclerView getmRecyclerView() {

        return mRecyclerView;
    }

    /**
     * Called when RecyclerView needs a new {@link PessoaViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(PessoaViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(PessoaViewHolder, int)
     */
    @NonNull
    @Override
    public AdaptadorPessoas.PessoaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_filme_pessoas, parent, false);
        return new PessoaViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link PessoaViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link PessoaViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(PessoaViewHolder, int)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final AdaptadorPessoas.PessoaViewHolder holder, int position) {

        ItemPessoas currentItem = itemList.get(position);

        String imageUrl = currentItem.getProfile_path();
        String nome = currentItem.getName();
        String personagem = currentItem.getCharacter();

        holder.textViewNome.setText(nome);
        holder.textViewPersonagem.setText(personagem);
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

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class PessoaViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public TextView textViewNome;
        public TextView textViewPersonagem;

        public PessoaViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewItemFilmePessoas);

            textViewNome = itemView.findViewById(R.id.textViewItemFilmePessoasNome);
            textViewPersonagem = itemView.findViewById(R.id.textViewItemFilmePessoasPersonagem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        InUse = true;
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
