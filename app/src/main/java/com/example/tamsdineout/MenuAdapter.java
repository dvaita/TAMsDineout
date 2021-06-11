package com.example.tamsdineout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class MenuAdapter extends ArrayAdapter<itemsMenu> {

    private Context context;
    private List<itemsMenu> menuList;

    MenuAdapter(Context context, int resource, List<itemsMenu> menuList) {
        super(context, resource, menuList);

        this.context=context;
        this.menuList=menuList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.menu_custom,null);

        TextView itemName=view.findViewById(R.id.name);
        TextView itemPrice=view.findViewById(R.id.price);

        itemsMenu menu=menuList.get(position);

        itemName.setText(menu.getItemName());
        itemPrice.setText(menu.getItemPrice());


    return view;
    }


}
