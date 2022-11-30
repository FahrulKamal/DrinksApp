package com.rannstudio.drinksapp.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rannstudio.drinksapp.R;
import com.rannstudio.drinksapp.ui.cart.Cart;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolderView> {
    private ArrayList<Drink> drinkList;
    private DatabaseReference mDatabase;
    private int jumlahHarga;

    public HomeAdapter(ArrayList<Drink> drinkList) {
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public HomeHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.drinks_lists, parent, false);
        return new HomeHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolderView holder, @SuppressLint("RecyclerView") int position) {
        int res = holder.itemView.getResources().getIdentifier(drinkList.get(position).getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.image.setImageResource(res);
        holder.title.setText(drinkList.get(position).getTitle());
        holder.description.setText(drinkList.get(position).getDescription());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.view_item_layout);

                ImageView image = dialog.findViewById(R.id.view_item_image);
                int res = view.getResources().getIdentifier(drinkList.get(position).getImage(), "drawable", view.getContext().getPackageName());
                image.setImageResource(res);

                TextView title = dialog.findViewById(R.id.view_item_title);
                title.setText(drinkList.get(position).getTitle());

                TextView description = dialog.findViewById(R.id.view_item_description);
                description.setText(drinkList.get(position).getDescription());

                TextView harga = dialog.findViewById(R.id.view_item_harga);

                NumberFormat formatter = new DecimalFormat("#,###");
                int prices = drinkList.get(position).getHarga();
                harga.setText("Rp. " + formatter.format(prices));

                jumlahHarga = prices;

                TextView jumlah = dialog.findViewById(R.id.view_item_jumlah);

                ImageButton plus = dialog.findViewById(R.id.btn_add);
                plus.setOnClickListener(view1 -> {
                    int tmp_jumlah = Integer.parseInt(jumlah.getText().toString());
                    if (tmp_jumlah >= 0) {
                        jumlah.setText(String.valueOf(tmp_jumlah + 1));
                        jumlahHarga = prices * (tmp_jumlah + 1);
                        harga.setText("Rp. " + formatter.format(jumlahHarga));
                    }
                });

                ImageButton minus = dialog.findViewById(R.id.btn_min);
                minus.setOnClickListener(view13 -> {
                    int tmp_jumlah = Integer.parseInt(jumlah.getText().toString());
                    if (tmp_jumlah > 0) {
                        jumlah.setText(String.valueOf(tmp_jumlah - 1));
                        jumlahHarga = prices * (tmp_jumlah - 1);
                        harga.setText("Rp. " + formatter.format(jumlahHarga));
                    }
                });

                Spinner nomor_meja = dialog.findViewById(R.id.view_item_nomor_meja);

                Button add_cart = dialog.findViewById(R.id.view_item_add_cart);
                add_cart.setOnClickListener(view12 -> {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(holder.itemView.getContext());
                    Map<String, Object> orderdata = new HashMap<>();
                    orderdata.put("image", drinkList.get(position).getImage());
                    orderdata.put("type", drinkList.get(position).getType());
                    orderdata.put("title", drinkList.get(position).getTitle());
                    orderdata.put("description", drinkList.get(position).getDescription());
                    orderdata.put("buyer_email", prefs.getString("email", null));
                    orderdata.put("table_number", nomor_meja.getSelectedItem().toString());
                    orderdata.put("price", jumlahHarga);
                    orderdata.put("amount", Integer.parseInt(jumlah.getText().toString()));
                    orderdata.put("already_paid", false);


                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("cart").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                int key = (int) task.getResult().getChildrenCount();
                                Map<String, Object> neworder = new HashMap<>();
                                if (key < 1) {
                                    neworder.put("0", orderdata);
                                } else {
                                    neworder.put(String.valueOf(key), orderdata);
                                }
                                mDatabase.child("cart").updateChildren(neworder);

                                AlertDialog.Builder builder2 = new AlertDialog.Builder(dialog.getContext());
                                builder2.setMessage("Berhasil menambah ke keranjang")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface d, int id) {
                                                d.dismiss();
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder2.create();
                                alert.show();
                            }
                        }
                    });

                });

                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (drinkList != null) ? drinkList.size() : 0;
    }

    public class HomeHolderView extends RecyclerView.ViewHolder {
        private CardView card;
        private ImageView image;
        private TextView title, description;


        public HomeHolderView(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item_card);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
        }
    }
}