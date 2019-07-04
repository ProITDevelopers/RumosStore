package proitappsolutions.com.rumosstore;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import proitappsolutions.com.rumosstore.fragmentos.FragMercadoMedia;
import proitappsolutions.com.rumosstore.fragmentos.FragRumoMedia;
import proitappsolutions.com.rumosstore.fragmentos.FragVanguardaMedia;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private String mWordUpdated;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);


    }

    //call this method to update fragments in ViewPager dynamically
    public void update(String word) {
        this.mWordUpdated = word;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch (position){
            case 0 :
                fragment = FragMercadoMedia.newInstance("","");

                break;
            case 1 :
                fragment = FragVanguardaMedia.newInstance("","");

                break;
            case 2 :
                fragment = FragRumoMedia.newInstance("","");

                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Mercado";
            case 1:
                return "Vanguarda";
            case 2:
                return "Rumo";

        }
        return null;
    }



}
