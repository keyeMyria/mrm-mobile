package com.andela.mrm.login_flow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.andela.mrm.R;
import com.andela.mrm.room_setup.country.CountryActivity;

/**
 * A {@link Fragment} subclass.
 */
public class LoginButtonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_button, container, false);

        ImageButton imageButton = view.findViewById(R.id.button_login);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CountryActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
