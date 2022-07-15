package com.example.pimoscanner;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{

    Dummy[] dummies;
    Context context;

    public MyRecyclerViewAdapter(Dummy[] dummies, DummyWorkOrder activity) {
        this.dummies = dummies;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater = LayoutInflater.from ( parent.getContext () );
        View view = layoutInflater.inflate ( R.layout.workorder_list,parent,false );
        ViewHolder viewHolder = new ViewHolder ( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final  Dummy dummy = dummies[position];
    holder.name.setText ( dummy.getName () );
        holder.date.setText ( dummy.getDate ());
        holder.name.setText ( dummy.getName () );
//        holder.imageView.setImageResource ( dummy.getImage () );

        holder.itemView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText ( context,dummy.getName (), Toast.LENGTH_SHORT ).show ();
                Intent intent = new Intent(view.getContext(), ExtendWorkOrder.class);
                context.startActivity (intent);
            }
        } );

    }

    @Override
    public int getItemCount() {
        return dummies.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            imageView = itemView.findViewById ( R.id.image );
            name = itemView.findViewById ( R.id.name );
            date = itemView.findViewById ( R.id.date );
        }
    }
}

