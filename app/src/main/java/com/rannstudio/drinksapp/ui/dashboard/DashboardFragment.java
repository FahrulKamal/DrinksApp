package com.rannstudio.drinksapp.ui.dashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rannstudio.drinksapp.MainActivity;
import com.rannstudio.drinksapp.R;
import com.rannstudio.drinksapp.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // EMAIL ADDRESS
        binding.dashboardEmail.setText(consorEmail(prefs.getString("email", null)));

        // LOGOUT
        binding.dashboardLogout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Keluar");
            builder.setMessage("Anda yakin ingin keluar?");
            builder.setCancelable(true);

            builder.setPositiveButton("Ya", (dialog, id) -> {
                prefs.edit().clear().apply();
                Toast.makeText(getActivity(), "Berhasil keluar", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            });

            builder.setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
            builder.create().show();
        });

        return root;
    }

    public static String consorEmail(String email) {
        return email.replaceAll("(?<=[^\\.])[^\\.](?=...*@)", "*");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}