package com.cfriendsv2.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cfriendsv2.Fragments.chatsFragment;
import com.cfriendsv2.Fragments.myrequestsFragment;
import com.cfriendsv2.Fragments.requestsFragment;
import com.cfriendsv2.Fragments.usersFragment;

public class pagesAdapter extends FragmentStateAdapter {
    public pagesAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new usersFragment();
            case 1:
                return new chatsFragment();
            case 2:
                return  new requestsFragment();
            case 3:
                return new myrequestsFragment();
            default:
                return new usersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
