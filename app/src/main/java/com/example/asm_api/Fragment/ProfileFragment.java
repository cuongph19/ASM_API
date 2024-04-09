package com.example.asm_api.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.asm_api.R;

public class ProfileFragment extends Fragment {

    private ImageView avatarImageView;
    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView emailTextView;
    private TextView nameTextView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String username, String password, String email, String name) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("password", password);
        args.putString("email", email);
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        avatarImageView = view.findViewById(R.id.avatar);
        usernameTextView = view.findViewById(R.id.tv_username);
        passwordTextView = view.findViewById(R.id.tv_password);
        emailTextView = view.findViewById(R.id.tv_email);
        nameTextView = view.findViewById(R.id.tv_name);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            String email = bundle.getString("email");
            String name = bundle.getString("name");

            usernameTextView.setText(username);
            passwordTextView.setText(password);
            emailTextView.setText(email);
            nameTextView.setText(name);
        }

        return view;
    }
}