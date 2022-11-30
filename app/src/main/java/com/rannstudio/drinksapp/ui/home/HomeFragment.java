package com.rannstudio.drinksapp.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rannstudio.drinksapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private ArrayList<Drink> drinkArrayList;
    private DatabaseReference mDatabase;
    private AutoCompleteTextView filterChooser;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String[] drink_lists = {"Coffee", "Tea", "Juice"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, drink_lists);

        filterChooser = binding.filterChooser;

        filterChooser.setOnFocusChangeListener((view, b) -> {
            filterChooser.setAdapter(adapter);
            if (b) {
                filterChooser.showDropDown();
            }
        });

       filterChooser.setOnItemClickListener((adapterView, view, i, l) -> {
           String selected_filter = String.valueOf(adapterView.getItemAtPosition(i));
           filter(selected_filter);
        });

        getDrinksData();

        recyclerView = binding.drinksRecycleview;
        homeAdapter = new HomeAdapter(drinkArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(homeAdapter);

        return root;
    }

    private void getDrinksData() {
        drinkArrayList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("drinks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()) {
                    Drink data = snap.getValue(Drink.class);
                    drinkArrayList.add(new Drink(data.getType(), data.getImage(), data.getTitle(), data.getDescription(), data.getHarga()));
                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void filter(String text) {
        List<Drink> temp = new ArrayList();
        for (Drink d : drinkArrayList) {
            if (d.getType().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        recyclerView.setAdapter(new HomeAdapter((ArrayList<Drink>) temp));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}