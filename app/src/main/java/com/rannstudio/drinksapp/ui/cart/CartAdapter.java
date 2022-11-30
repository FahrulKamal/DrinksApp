package com.rannstudio.drinksapp.ui.cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rannstudio.drinksapp.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolderView> {
    private ArrayList<Cart> cartList;
    private DatabaseReference mDatabase;

    public CartAdapter(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_item_cart_layout, parent, false);
        return new CartHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolderView holder, @SuppressLint("RecyclerView") int position) {
        int res = holder.itemView.getResources().getIdentifier(cartList.get(position).getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.image.setImageResource(res);
        holder.title.setText(cartList.get(position).getTitle());
        holder.description.setText(cartList.get(position).getDescription());
        holder.amount.setText(String.valueOf(cartList.get(position).getAmount()));
        holder.table.setText(cartList.get(position).getTable_number());

        NumberFormat formatter = new DecimalFormat("#,###");
        int prices = cartList.get(position).getPrice();
        holder.total.setText("Rp. " + formatter.format(prices));

        holder.btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Pesan sekarang?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogIdnterface, int id) {

                                AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());
                                builder2.setMessage("Pesanan anda sedang dibuat.\nBayar ketika pesanan anda datang.")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogIdnterface, int id) {

                                            }
                                        });
                                AlertDialog alert2 = builder2.create();
                                alert2.show();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogIdnterface, int i) {
                                dialogIdnterface.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());
                builder2.setMessage("Hapus pesanan dari keranjang ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogIdnterface, int id) {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                                String email = prefs.getString("email", null);

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                Query Querydata = mDatabase.child("cart").orderByChild("buyer_email").equalTo(email);

                                Querydata.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot snap : snapshot.getChildren()) {
                                            Cart data = snap.getValue(Cart.class);
                                            if (data.getAmount() == cartList.get(position).getAmount() && data.getPrice() == prices && data.getTable_number() == cartList.get(position).getTable_number()) {
                                                snap.getRef().removeValue();
                                                cartList.remove(position);
                                                notifyItemRemoved(position);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogIdnterface, int i) {
                                dialogIdnterface.dismiss();
                            }
                        });
                AlertDialog alert = builder2.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (cartList != null) ? cartList.size() : 0;
    }

    public class CartHolderView extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, description, amount, table, total;
        private Button btn_pesan, btn_hapus;


        public CartHolderView(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cart_item_image);
            title = itemView.findViewById(R.id.cart_item_title);
            description = itemView.findViewById(R.id.cart_item_description);
            amount = itemView.findViewById(R.id.cart_item_amount);
            table = itemView.findViewById(R.id.cart_item_table);
            total = itemView.findViewById(R.id.cart_item_total);
            btn_pesan = itemView.findViewById(R.id.cart_btn_pesan);
            btn_hapus = itemView.findViewById(R.id.cart_btn_hapus);
        }
    }
}