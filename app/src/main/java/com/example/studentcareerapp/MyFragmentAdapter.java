package com.example.studentcareerapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.studentcareerapp.Faculty.Activity.FFeedbackFragment;
import com.example.studentcareerapp.Faculty.Activity.FGenralNotificationFragment;
import com.example.studentcareerapp.Faculty.Activity.FNotesFragment;
import com.example.studentcareerapp.Faculty.Activity.FProfileFragment;
import com.example.studentcareerapp.Student.Activity.SFeedbackFragment;
import com.example.studentcareerapp.Student.Activity.SGenralNotificationFragment;
import com.example.studentcareerapp.Student.Activity.SNotesFragment;
import com.example.studentcareerapp.Student.Activity.SProfileFragment;

public class MyFragmentAdapter extends FragmentStateAdapter {

    int role;

    public MyFragmentAdapter(@NonNull androidx.fragment.app.FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int role) {
        super(fragmentManager, lifecycle);
        this.role = role;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(role == 1){
            switch (position){

                case 1:
                    return new SGenralNotificationFragment();

                case 2:
                    return new SNotesFragment();

                case 3:
                    return new SFeedbackFragment();

                default:
                    return new SProfileFragment();

            }
        }

        else if(role == 2){

            switch (position){

                case 1:
                    return new FGenralNotificationFragment();

                case 2:
                    return new FNotesFragment();

                case 3:
                    return new FFeedbackFragment();

                default:
                    return new FProfileFragment();

            }
        }
        else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
