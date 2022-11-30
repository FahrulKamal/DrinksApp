package com.rannstudio.drinksapp.ui.cart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rannstudio.drinksapp.databinding.FragmentCartBinding;
import com.rannstudio.drinksapp.ui.home.Drink;
import com.rannstudio.drinksapp.ui.home.HomeAdapter;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartArrayList;
    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getCartData();

        recyclerView = binding.cartRecycleview;
        cartAdapter = new CartAdapter(cartArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cartAdapter);

        return root;
    }

    private void getCartData() {
        cartArrayList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String email = prefs.getString("email", null);


        mDatabase.child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()) {
                    Cart data = snap.getValue(Cart.class);
                    if (data.getBuyer_email().equals(email)) {
                        cartArrayList.add(new Cart(data.getImage(), data.getType(), data.getTitle(), data.getDescription(), data.getBuyer_email(), data.getTable_number(), data.getPrice(), data.getAmount(), data.isAlready_paid()));
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}