package net.khirr.android.bottombar.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentExample extends Fragment {

    private static final String FIELD_NUMBER = "number";

    public static FragmentExample newInstance(int number) {
        final FragmentExample fragment = new FragmentExample();
        final Bundle arguments = new Bundle();
        arguments.putInt(FIELD_NUMBER, number);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView numberTextView = view.findViewById(R.id.numberTextView);
        final int number = getArguments().getInt(FIELD_NUMBER);

        numberTextView.setText(String.valueOf(number));
    }

}
