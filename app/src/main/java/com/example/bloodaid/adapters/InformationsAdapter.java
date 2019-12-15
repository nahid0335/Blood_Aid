package com.example.bloodaid.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.R;
import com.example.bloodaid.fragments.AppInfoFragment;
import com.example.bloodaid.fragments.FactsFragment;
import com.example.bloodaid.fragments.HistoryFragment;
import com.example.bloodaid.fragments.TopDonorFragment;
import com.example.bloodaid.models.TopDonorModelClass;

import java.util.ArrayList;

public class InformationsAdapter extends RecyclerView.Adapter<InformationsAdapter.InformationsViewHolder> {

    ArrayList<String> infoTextList = new ArrayList<>();
    FragmentLoaderInterface loaderInterface;
    int[] infoImageList = {R.drawable.ic_top_donar,
            R.drawable.ic_facts,
            R.drawable.ic_history,
            R.drawable.ic_info,
            R.drawable.ic_person};


    public InformationsAdapter(FragmentLoaderInterface fragmentLoaderInterface){
        infoTextList.add("Top Donor");
        infoTextList.add("Facts");
        infoTextList.add("History");
        infoTextList.add("App Info");
        infoTextList.add("Admin");
        this.loaderInterface = fragmentLoaderInterface;
    }


    @NonNull
    @Override
    public InformationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.informations_column,parent,false);
        return new InformationsViewHolder(v, loaderInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationsViewHolder holder, final int position) {
        holder.mInfoImg.setImageResource(infoImageList[position]);
        holder.mInfoTxt.setText(infoTextList.get(position));
    }

    @Override
    public int getItemCount() {
        return infoTextList.size();
    }

    public interface FragmentLoaderInterface{
        void loadFragmentFromInterface(int position);
    }

    public class InformationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mInfoTxt;
        ImageView mInfoImg;
        FragmentLoaderInterface fragmentLoaderInterface;
        public InformationsViewHolder(@NonNull final View itemView, FragmentLoaderInterface fragmentLoaderInterface) {
            super(itemView);
            mInfoImg = itemView.findViewById(R.id.imageView_informations_img);
            mInfoTxt = itemView.findViewById(R.id.textView_information_text);
            this.fragmentLoaderInterface = fragmentLoaderInterface;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            fragmentLoaderInterface.loadFragmentFromInterface(getAdapterPosition());
        }
    }
}

